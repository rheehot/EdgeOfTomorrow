package com.oracle.eot.exception;

public class EotException extends RuntimeException {

	private static final long serialVersionUID = 8157950270805037015L;
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public EotException() {
		super();
	}

	public EotException(int code, String message) {
		super(message);
		this.code = code;
	}

	public EotException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public EotException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}
}
