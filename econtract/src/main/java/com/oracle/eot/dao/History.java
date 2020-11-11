package com.oracle.eot.dao;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "\"history\"")
@IdClass(HistoryPK.class)
public class History {
	@Id
	private int cid;

	@Id
	private Date historyDT;

	@Column
	private int state;

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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	
}
