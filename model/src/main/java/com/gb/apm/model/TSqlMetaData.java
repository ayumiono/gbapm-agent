package com.gb.apm.model;

public class TSqlMetaData extends APMModel {
	private String agentId; // required
	private long agentStartTime; // required
	private int sqlId; // required
	private String sql; // required

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public long getAgentStartTime() {
		return agentStartTime;
	}

	public void setAgentStartTime(long agentStartTime) {
		this.agentStartTime = agentStartTime;
	}

	public int getSqlId() {
		return sqlId;
	}

	public void setSqlId(int sqlId) {
		this.sqlId = sqlId;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
