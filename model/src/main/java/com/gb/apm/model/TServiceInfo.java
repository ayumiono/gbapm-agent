package com.gb.apm.model;

import java.util.List;

public class TServiceInfo extends APMModel {
	private String serviceName; // optional
	private List<String> serviceLibs; // optional

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<String> getServiceLibs() {
		return serviceLibs;
	}

	public void setServiceLibs(List<String> serviceLibs) {
		this.serviceLibs = serviceLibs;
	}
}
