package com.oracle.eot.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.eot.dao.Master;

@Service
public class ConvertService {
	private final Path fileLocation;
	private final String configurationFilePath;
	private final Region region;
	private final String namespaceName;
	private final String bucketName;
	private final Map<String, String> metadata;
	private final String contentType;
	private final String contentEncoding;
	private final String contentLanguage;

	ObjectStorage client;

	@Autowired
	public ConvertService(StorageProperties properties) {
		try {
			this.fileLocation = Paths.get(properties.getLocation());
			this.configurationFilePath = properties.getConfigurationFilePath();
			this.region = properties.getRegion();
			this.namespaceName = properties.getNamespaceName();
			this.bucketName = properties.getBucketName();
			this.metadata = properties.getMetadata();
			this.contentType = properties.getContentType();
			this.contentEncoding = properties.getContentEncoding();
			this.contentLanguage = properties.getContentLanguage();

			ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath);
			ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			client = new ObjectStorageClient(provider);
			client.setRegion(region);
		} catch (Exception e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage(), e);
		}
	}

	public String copyToLocation(String objectName) throws IOException {
		// 읽을 contract 파일 로컬에 저장
		GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(namespaceName)
				.bucketName(bucketName).objectName(objectName).build());

		InputStream contractFileStream = getResponse.getInputStream();
		Files.copy(contractFileStream, this.fileLocation.resolve(objectName), StandardCopyOption.REPLACE_EXISTING);

		System.out.println(this.fileLocation.getFileName());

		return this.fileLocation.toString() + File.separator + objectName;

	}

	public boolean isExist(String fileName) {
		Path filePath = this.fileLocation.resolve(fileName).normalize();
		try {
			Resource resource = new UrlResource(filePath.toUri());
			return resource.exists();
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	public String makeThumbnail(String fileName) {
		Path srcFile = this.fileLocation.resolve(fileName).normalize();
		Path desFile = this.fileLocation.resolve("thumbnail-"+fileName).normalize();
		
		try {
			BufferedImage srcImg = ImageIO.read(new File(srcFile.toString()));
			BufferedImage destImg = Scalr.resize(srcImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, 1024);

			File thumbFile = new File(desFile.toString()); 
			ImageIO.write(destImg, "jpg", thumbFile);

		} catch (IOException e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage(), e);
		}
		return "thumbnail-"+fileName;
	}
	
	public Resource loadFileAsResource(String fileName) {
		Path filePath = this.fileLocation.resolve(fileName).normalize();
		try {
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new StorageException(fileName + " 파일을 찾을 수 없습니다.");
			}
		} catch (MalformedURLException e) {
			throw new StorageException(fileName + " 파일을 찾을 수 없습니다.", e);
		}
	}

	public String convertToPdf(final Master master) {

		try {
			// 파일들을 로컬에 저장
			String copyedContractFile = copyToLocation(master.getContractFile());
			String copyedRequestFile = copyToLocation(master.getRequestFile());
			String copyedApproveFile = copyToLocation(master.getApproveFile());

			String agreementFile = "agreement.pdf";
			File outFilename = new File(this.fileLocation.toString(), agreementFile);

			// 도큐먼트 생성
			Document document = new Document(PageSize.A4);
			PdfWriter.getInstance(document, FileUtils.openOutputStream(outFilename));
			document.open();

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 2 });
			table.addCell(createImageCell(copyedContractFile));
			table.addCell(createTextCell(master.getRequestDT().toString()));
			document.add(table);

			document.newPage();
			table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 2 });
			table.addCell(createImageCell(copyedRequestFile));
			table.addCell(createTextCell(master.getRequestName() + ":" + master.getRequestEmail()));
			document.add(table);

			document.newPage();
			table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 2 });
			table.addCell(createImageCell(copyedApproveFile));
			table.addCell(createTextCell(master.getApproveName() + ":" + master.getApproveEmail()));
			document.add(table);

			document.close();

			FileSystemUtils.deleteRecursively(new File(copyedContractFile));
			FileSystemUtils.deleteRecursively(new File(copyedRequestFile));
			FileSystemUtils.deleteRecursively(new File(copyedApproveFile));

			return agreementFile;
		} catch (Exception e) {
			throw new StorageException(e.getMessage(), e);
		}
	}

	private PdfPCell createImageCell(String path) throws DocumentException, IOException {
		Image img = Image.getInstance(path);
		PdfPCell cell = new PdfPCell(img, true);
		return cell;
	}

	private PdfPCell createTextCell(String text) throws DocumentException, IOException {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text);
		p.setAlignment(Element.ALIGN_RIGHT);
		cell.addElement(p);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}
}
