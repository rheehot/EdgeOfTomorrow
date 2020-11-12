package com.oracle.eot.dao;

import java.sql.Date;

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
	private int cid;
	
	@Column
	private Date historyDT;
	
	@Column
	private String state;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public Date getHistoryDT() {
		return historyDT;
	}

	public void setHistoryDT(Date historyDT) {
		this.historyDT = historyDT;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
