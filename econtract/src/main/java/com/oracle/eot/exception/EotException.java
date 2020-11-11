package com.oracle.eot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EotException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EotException(String msg) {
		super(msg);
	}
	
	public EotException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public EotException(Throwable e) {
		super(e);
	}

}
