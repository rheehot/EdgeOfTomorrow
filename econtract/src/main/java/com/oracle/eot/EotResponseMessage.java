package com.oracle.eot;

public class EotResponseMessage {

	private String status;
	private String message;
	private String errorCode;
	private String errorMessage;
	
	
	public EotResponseMessage() {
		super();
	}
	
	public EotResponseMessage(String status, String message, String errorCode, String errorMessage) {
		super();
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "EotResponseMessage [status=" + status + ", message=" + message + ", errorCode=" + errorCode
				+ ", errorMessage=" + errorMessage + "]";
	}
	
	
}
