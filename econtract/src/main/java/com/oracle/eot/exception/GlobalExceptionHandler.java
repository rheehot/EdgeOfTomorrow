package com.oracle.eot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice  
@RestController  
public class GlobalExceptionHandler {  
  
    @ResponseStatus(HttpStatus.BAD_REQUEST)  
    @ExceptionHandler(value = EotException.class)  
    public ErrorResponse handleBaseException(EotException e){  
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(e.getCode());
		errorResponse.setMessage(e.getMessage());
		return errorResponse;	
    }  
  
    @ExceptionHandler(value = Exception.class)  
    public ErrorResponse handleException(Exception e){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(400);
		errorResponse.setMessage(e.getMessage());
		return errorResponse;	
	}  
  
  
}  
