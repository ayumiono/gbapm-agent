package com.gb.apm.model;

public class TDataSource extends APMModel {
	private int id; // required
	private short serviceTypeCode; // optional
	private String databaseName; // optional
	private String url; // optional
	private int activeConnectionSize; // optional
	private int maxConnectionSize; // optional

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(short serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getActiveConnectionSize() {
		return activeConnectionSize;
	}

	public void setActiveConnectionSize(int activeConnectionSize) {
		this.activeConnectionSize = activeConnectionSize;
	}

	public int getMaxConnectionSize() {
		return maxConnectionSize;
	}

	public void setMaxConnectionSize(int maxConnectionSize) {
		this.maxConnectionSize = maxConnectionSize;
	}
}
