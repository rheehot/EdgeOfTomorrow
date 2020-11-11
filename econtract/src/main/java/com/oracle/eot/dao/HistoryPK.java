package com.oracle.eot.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Id;

public class HistoryPK implements Serializable {
	@Id
	private int cid;

	@Id
	private Date historyDT;

	
	public HistoryPK() {
		super();
	}

	public HistoryPK(int cid, Date historyDT) {
		super();
		this.cid = cid;
		this.historyDT = historyDT;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cid;
		result = prime * result + ((historyDT == null) ? 0 : historyDT.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryPK other = (HistoryPK) obj;
		if (cid != other.cid)
			return false;
		if (historyDT == null) {
			if (other.historyDT != null)
				return false;
		} else if (!historyDT.equals(other.historyDT))
			return false;
		return true;
	}
	
	
}
