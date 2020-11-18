package com.oracle.eot.dao;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "\"master\"")
public class Master {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int cid;	//Sequence
	
	@Column
	private String uuid;
	
	@Column
	private String title;
	
	@Column
	private String txid;	//Blockchain Tx
	@Column
	private String gid;		//goods Id

	@Column
	private Timestamp requestDT;		//request DateTime
	@Column
	private String requestName;	//request Name 
	@Column
	private String requestEmail;	//request Email

	@Column
	private Timestamp approveDT;
	@Column
	private String approveName;
	@Column
	private String approveEmail;
	
	@Column
	private String contractFile;
	@Column
	private int contractHash;
	
	@Column
	private String requestFile;
	@Column
	private int requestHash;
	
	@Column
	private String approveFile;
	@Column
	private int approveHash;
	
	@Column
	private String agreementFile;
	@Column
	private int agreementHash;
	
	@Column
	private String status;
	
	
	public Master() {
		super();
	}


	public Master(int cid) {
		super();
		this.cid = cid;
	}

	

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


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getTxid() {
		return txid;
	}


	public void setTxid(String txid) {
		this.txid = txid;
	}


	public String getGid() {
		return gid;
	}


	public void setGid(String gid) {
		this.gid = gid;
	}


	public Timestamp getRequestDT() {
		return requestDT;
	}


	public void setRequestDT(Timestamp requestDT) {
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





	public Timestamp getApproveDT() {
		return approveDT;
	}


	public void setApproveDT(Timestamp approveDT) {
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


	public String getContractFile() {
		return contractFile;
	}


	public void setContractFile(String contractFile) {
		this.contractFile = contractFile;
	}


	public int getContractHash() {
		return contractHash;
	}


	public void setContractHash(int contractHash) {
		this.contractHash = contractHash;
	}


	public String getRequestFile() {
		return requestFile;
	}


	public void setRequestFile(String requestFile) {
		this.requestFile = requestFile;
	}


	public int getRequestHash() {
		return requestHash;
	}


	public void setRequestHash(int requestHash) {
		this.requestHash = requestHash;
	}


	public String getApproveFile() {
		return approveFile;
	}


	public void setApproveFile(String approveFile) {
		this.approveFile = approveFile;
	}


	public int getApproveHash() {
		return approveHash;
	}


	public void setApproveHash(int approveHash) {
		this.approveHash = approveHash;
	}


	public String getAgreementFile() {
		return agreementFile;
	}


	public void setAgreementFile(String agreementFile) {
		this.agreementFile = agreementFile;
	}


	public int getAgreementHash() {
		return agreementHash;
	}


	public void setAgreementHash(int agreementHash) {
		this.agreementHash = agreementHash;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}




}
