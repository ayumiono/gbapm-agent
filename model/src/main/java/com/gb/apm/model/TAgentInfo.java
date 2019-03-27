package com.gb.apm.model;

public class TAgentInfo extends APMModel {
	private String hostname; // required
	private String ip; // required
	private String ports; // required
	private String agentId; // required
	private String applicationName; // required
	private short serviceType; // required
	private int pid; // required
	private String agentVersion; // required
	private String vmVersion; // required
	private long startTimestamp; // required
	private long endTimestamp; // optional
	private int endStatus; // optional
	private TServerMetaData serverMetaData; // optional
	private TJvmInfo jvmInfo; // optional

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public short getServiceType() {
		return serviceType;
	}

	public void setServiceType(short serviceType) {
		this.serviceType = serviceType;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getAgentVersion() {
		return agentVersion;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public int getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(int endStatus) {
		this.endStatus = endStatus;
	}

	public TServerMetaData getServerMetaData() {
		return serverMetaData;
	}

	public void setServerMetaData(TServerMetaData serverMetaData) {
		this.serverMetaData = serverMetaData;
	}

	public TJvmInfo getJvmInfo() {
		return jvmInfo;
	}

	public void setJvmInfo(TJvmInfo jvmInfo) {
		this.jvmInfo = jvmInfo;
	}
}
