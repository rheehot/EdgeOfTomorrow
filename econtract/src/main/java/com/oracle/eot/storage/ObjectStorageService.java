package com.oracle.eot.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadResponse;

//@Service
public class ObjectStorageService implements StorageService {
	String configurationFilePath = "oci/config";
	String profile = "DEFAULT";

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
			this.namespaceName = properties.getNamespaceName();
			this.bucketName = properties.getBucketName();
			this.metadata = properties.getMetadata();
			this.contentType = properties.getContentType();
			this.contentEncoding = properties.getContentEncoding();
			this.contentLanguage = properties.getContentLanguage();

			ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
			ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
			client = new ObjectStorageClient(provider);
			client.setRegion(Region.US_PHOENIX_1);
		} catch (IOException e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage(), e);
		}
	}

	@Override
	public void store(MultipartFile file) {

		String objectName = file.getOriginalFilename();

		try {

			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
//			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));

			// configure upload settings as desired
			UploadConfiguration uploadConfiguration = 
					UploadConfiguration.builder()
						.allowMultipartUploads(true)
						.allowParallelUploads(true)
						.build();

			UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

			PutObjectRequest request = 
					PutObjectRequest.builder()
						.bucketName(bucketName)
						.namespaceName(namespaceName)
						.objectName(objectName)
						.contentType(contentType)
						.contentLanguage(contentLanguage)
						.contentEncoding(contentEncoding)
						.opcMeta(metadata)
						.build();

			UploadRequest uploadDetails = 
					UploadRequest.builder(file.getInputStream(), file.getSize())
						.allowOverwrite(true)
						.build(request);
//			UploadRequest uploadDetails = UploadRequest.builder(body).allowOverwrite(true).build(request);

			// upload request and print result
			// if multi-part is used, and any part fails, the entire upload fails and will throw BmcException
			UploadResponse response = uploadManager.upload(uploadDetails);
			System.out.println(response);

//		// fetch the object just uploaded
//		GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(namespaceName)
//				.bucketName(bucketName).objectName(objectName).build());
//
//		// stream contents should match the file uploaded
//		try (final InputStream fileStream = getResponse.getInputStream()) {
//			// use fileStream
//		} // try-with-resources automatically closes fileStream

		} catch (IOException e) {
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}

	}

	@Override
	public Stream<Path> loadAll() {
//		try {
//			return Files.walk(this.rootLocation, 1)
//					.filter(path -> !path.equals(this.rootLocation))
//					.map(path -> this.rootLocation.relativize(path));
//		} catch (IOException e) {
//			throw new StorageException("Failed to read stored files", e);
//		}
		return null;
	}

	@Override
	public Path load(String filename) {
//		return rootLocation.resolve(filename);
		return null;
	}

	@Override
	public Resource loadAsResource(String filename) {
//		try {
//			Path file = load(filename);
//			Resource resource = new UrlResource(file.toUri());
//			if(resource.exists() || resource.isReadable()) {
//				return resource;
//			}
//			else {
//				throw new StorageFileNotFoundException("Could not read file: " + filename);
//
//			}
//		} catch (MalformedURLException e) {
//			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
//		}
		return null;
	}

	@Override
	public void deleteAll() {
//		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
//		try {
//			Files.createDirectory(rootLocation);
//		} catch (IOException e) {
//			throw new StorageException("Could not initialize storage", e);
//		}
	}
}
