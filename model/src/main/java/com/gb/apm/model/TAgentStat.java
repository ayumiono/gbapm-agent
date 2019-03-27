package com.gb.apm.model;

public class TAgentStat extends APMModel {
	private String agentId; // optional
	private long startTimestamp; // optional
	private long timestamp; // optional
	private long collectInterval; // optional
	private TJvmGc gc; // optional
	private TCpuLoad cpuLoad; // optional
	private TTransaction transaction; // optional
	private TActiveTrace activeTrace; // optional
	private TDataSourceList dataSourceList; // optional
	private String metadata; // optional

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getCollectInterval() {
		return collectInterval;
	}

	public void setCollectInterval(long collectInterval) {
		this.collectInterval = collectInterval;
	}

	public TJvmGc getGc() {
		return gc;
	}

	public void setGc(TJvmGc gc) {
		this.gc = gc;
	}

	public TCpuLoad getCpuLoad() {
		return cpuLoad;
	}

	public void setCpuLoad(TCpuLoad cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	public TTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(TTransaction transaction) {
		this.transaction = transaction;
	}

	public TActiveTrace getActiveTrace() {
		return activeTrace;
	}

	public void setActiveTrace(TActiveTrace activeTrace) {
		this.activeTrace = activeTrace;
	}

	public TDataSourceList getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(TDataSourceList dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
}
