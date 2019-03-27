package com.gb.apm.remoting.protocol;

import com.gb.apm.remoting.protocol.RemotingSerializable;

public class DbChangeEvent_delete extends RemotingSerializable {
	private String database;
	private String table;
	private Long typeNumId;
	private Long version;
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Long getTypeNumId() {
		return typeNumId;
	}
	public void setTypeNumId(Long typeNumId) {
		this.typeNumId = typeNumId;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "DbChangeEvent [database=" + database + ", table=" + table + ", typeNumId=" + typeNumId + ", version="
				+ version + "]";
	}
}
