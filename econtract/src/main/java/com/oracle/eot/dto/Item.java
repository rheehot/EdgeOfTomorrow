package com.oracle.eot.dto;

import java.sql.Timestamp;

public class Item {
	private int cid;
	private String uuid;
	private String txid;
	private String title;
	private Timestamp dt;
	private String approveName;
	private String approveEmail;
	private String state;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Timestamp getDt() {
		return dt;
	}
	public void setDt(Timestamp dt) {
		this.dt = dt;
	}
	public String getApproveName() {
		return approveName;
	}
	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}
	public String getApproveEmail() {
		return approveEmail;
	}
	public void setApproveEmail(String approveEmail) {
		this.approveEmail = approveEmail;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Item [cid=" + cid + ", uuid=" + uuid + ", txid=" + txid + ", title=" + title + ", dt=" + dt
				+ ", approveName=" + approveName + ", approveEmail=" + approveEmail + ", state=" + state + ", getCid()="
				+ getCid() + ", getUuid()=" + getUuid() + ", getTxid()=" + getTxid() + ", getTitle()=" + getTitle()
				+ ", getDt()=" + getDt() + ", getApproveName()=" + getApproveName() + ", getApproveEmail()="
				+ getApproveEmail() + ", getState()=" + getState() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}


	
	
}
