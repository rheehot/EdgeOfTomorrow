package com.oracle.eot.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"status\"")
public class ContractStatus {
	
	public static final int REQUEST = 1;
	public static final int EMAIL = 2;
	public static final int APPROVE = 3;
	public static final int DONE = 4;
	
	@Id
	private int code;
	
	@Column
	private String context;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
