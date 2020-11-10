package com.oracle.eot.storage;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;

@ConfigurationProperties("storage")
public class StorageProperties {

	/**
	 * 폴더 위치 (테스트용)
	 */
	private String location = "upload-dir";
	
	/**
	 * OCI ObjectStorage 설정
	 */
	private String namespaceName = null;
	private String bucketName = null;
	private Map<String, String> metadata = null;
	private String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;
	private String contentEncoding = "gzip";
	private String contentLanguage = null;
	
	
	//--------
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNamespaceName() {
		return namespaceName;
	}

	public void setNamespaceName(String namespaceName) {
		this.namespaceName = namespaceName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContentLanguage() {
		return contentLanguage;
	}

	public void setContentLanguage(String contentLanguage) {
		this.contentLanguage = contentLanguage;
	}

	
}