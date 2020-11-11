package com.oracle.eot.storage;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;

import com.oracle.bmc.Region;

@ConfigurationProperties("storage")
public class StorageProperties {

	/**
	 * 폴더 위치 (테스트용)
	 */
	private String location = "upload-dir";
	
	/**
	 * OCI 설정
	 */
	private String configurationFilePath = "./oci/config";
	private Region region = Region.US_ASHBURN_1;
	private String namespaceName = "id3tdyhkmip4";
	private String bucketName = "econtract-bucket";
	private String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;
	private String contentEncoding = null;
	private String contentLanguage = null;
	private Map<String, String> metadata = null;	
	
	//--------
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	

	public String getConfigurationFilePath() {
		return configurationFilePath;
	}

	public void setConfigurationFilePath(String configurationFilePath) {
		this.configurationFilePath = configurationFilePath;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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