package com.gb.apm.model;

import java.util.List;

public class TServerMetaData extends APMModel {
	private String serverInfo; // optional
	private List<String> vmArgs; // optional
	private List<TServiceInfo> serviceInfos; // optional

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}

	public List<String> getVmArgs() {
		return vmArgs;
	}

	public void setVmArgs(List<String> vmArgs) {
		this.vmArgs = vmArgs;
	}

	public List<TServiceInfo> getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(List<TServiceInfo> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}
}
