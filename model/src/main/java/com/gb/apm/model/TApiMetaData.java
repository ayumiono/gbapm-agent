package com.gb.apm.model;

public class TApiMetaData extends APMModel {
	private String agentId; // required
	private long agentStartTime; // required
	private int apiId; // required
	private String apiInfo; // required
	private int line; // optional
	private int type; // optional

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

	public int getApiId() {
		return apiId;
	}

	public void setApiId(int apiId) {
		this.apiId = apiId;
	}

	public String getApiInfo() {
		return apiInfo;
	}

	public void setApiInfo(String apiInfo) {
		this.apiInfo = apiInfo;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
