package com.oracle.eot.dao;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"history\"")
public class History {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int id;	//Sequence
	
	@Column
	private String uuid;
	
	@Column
	private Timestamp historyDT;
	
	@Column
	private String state;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Timestamp getHistoryDT() {
		return historyDT;
	}

	public void setHistoryDT(Timestamp historyDT) {
		this.historyDT = historyDT;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
