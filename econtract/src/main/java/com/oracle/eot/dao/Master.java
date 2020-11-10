package com.oracle.eot.dao;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"master\"")
public class Master {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int cid;	//Sequence
	
	@Column
	private String txid;	//Blockchain Tx
	@Column
	private String pid;		//Property Id

	@Column
	private Date requestDT;		//request DateTime
	@Column
	private String requestName;	//request Name 
	@Column
	private String requestEmail;	//request Email

	@Column
	private Date approveDT;
	@Column
	private String approveName;
	@Column
	private String approveEmail;
	
	@Column
	private String contractPath;
	@Column
	private String contractHash;
	
	@Column
	private String requestPath;
	@Column
	private String requestHash;
	
	@Column
	private String approvePath;
	@Column
	private String approveHash;
	
	@Column
	private String pdfPath;
	@Column
	private String pdfHash;
	
	
	
	
	//----------------------
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Date getRequestDT() {
		return requestDT;
	}
	public void setRequestDT(Date requestDT) {
		this.requestDT = requestDT;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public String getRequestEmail() {
		return requestEmail;
	}
	public void setRequestEmail(String requestEmail) {
		this.requestEmail = requestEmail;
	}
	public Date getApproveDT() {
		return approveDT;
	}
	public void setApproveDT(Date approveDT) {
		this.approveDT = approveDT;
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
	public String getContractPath() {
		return contractPath;
	}
	public void setContractPath(String contractPath) {
		this.contractPath = contractPath;
	}
	public String getContractHash() {
		return contractHash;
	}
	public void setContractHash(String contractHash) {
		this.contractHash = contractHash;
	}
	public String getRequestPath() {
		return requestPath;
	}
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	public String getRequestHash() {
		return requestHash;
	}
	public void setRequestHash(String requestHash) {
		this.requestHash = requestHash;
	}
	public String getApprovePath() {
		return approvePath;
	}
	public void setApprovePath(String approvePath) {
		this.approvePath = approvePath;
	}
	public String getApproveHash() {
		return approveHash;
	}
	public void setApproveHash(String approveHash) {
		this.approveHash = approveHash;
	}
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	public String getPdfHash() {
		return pdfHash;
	}
	public void setPdfHash(String pdfHash) {
		this.pdfHash = pdfHash;
	}

}
