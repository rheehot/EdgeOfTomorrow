package com.oracle.eot.storage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadResponse;

@Service
public class ObjectStorageService implements StorageService {
	String configurationFilePath = "./oci/config";
	String profile = "DEFAULT";

	Region region = null;
	String namespaceName = null;
	String bucketName = null;
	Map<String, String> metadata = null;
	String contentType = null;
	String contentEncoding = null;
	String contentLanguage = null;

	ObjectStorage client;

	@Autowired
	public ObjectStorageService(StorageProperties properties) {
		try {
			this.region = properties.getRegion();
			this.namespaceName = properties.getNamespaceName();
			this.bucketName = properties.getBucketName();
			this.metadata = properties.getMetadata();
			this.contentType = properties.getContentType();
			this.contentEncoding = properties.getContentEncoding();
			this.contentLanguage = properties.getContentLanguage();

//			System.out.println(new File(".").getAbsolutePath());
//			InputStream configInputStream = this.getClass().getClassLoader().getResourceAsStream(this.configurationFilePath);
//			ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configInputStream, "DEFAULT");

			ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(this.configurationFilePath);
			ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			client = new ObjectStorageClient(provider);
			client.setRegion(region);
		} catch (Exception e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage(), e);
		}
	}

	@Override
	public String store(MultipartFile file) {
		return store("", file);
	}
	
	@Override
	public String store(String prefix, MultipartFile file) {
		String objectName = prefix + file.getOriginalFilename();
		try {

			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}

			UploadConfiguration uploadConfiguration = UploadConfiguration.builder().allowMultipartUploads(true)
					.allowParallelUploads(true).build();

			UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

			PutObjectRequest request = PutObjectRequest.builder().namespaceName(namespaceName).bucketName(bucketName)
					.objectName(objectName).contentType(contentType)
//						.contentLanguage(contentLanguage)
//						.contentEncoding(contentEncoding)
//						.opcMeta(metadata)
					.build();
			System.out.println(request);

			UploadRequest uploadDetails = UploadRequest.builder(file.getInputStream(), file.getSize())
					.allowOverwrite(true).build(request);
			System.out.println(uploadDetails);
//			UploadRequest uploadDetails = UploadRequest.builder(body).allowOverwrite(true).build(request);

			UploadResponse response = uploadManager.upload(uploadDetails);

////			https://objectstorage.us-ashburn-1.oraclecloud.com/n/id3tdyhkmip4/b/econtract-bucket/o/2-request%40oracle.comrequest.jpg
//			String uri = new StringBuilder()
//				.append(client.getEndpoint())
//				.append("/n/" + namespaceName)
//				.append("/b/" + bucketName)
//				.append("/o/" + URLEncoder.encode(objectName, StandardCharsets.UTF_8)).toString();
//			
//			return uri;
			
			return objectName;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}

	}

	@Override
	public Stream<Path> loadAll() {
		return null;
	}

	@Override
	public Path load(String objectName) {
		GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(namespaceName)
				.bucketName(bucketName).objectName(objectName).build());

//		try (final InputStream fileStream = getResponse.getInputStream()) {
//			// use fileStream
//		}
		return null;
	}

	@Override
	public Resource loadAsResource(String filename) {
		return null;
	}

	@Override
	public void deleteAll() {
	}

	@Override
	public void init() {
	}
}
