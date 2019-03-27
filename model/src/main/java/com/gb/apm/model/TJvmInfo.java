package com.gb.apm.model;

public class TJvmInfo extends APMModel {
	private short version; // required
	private String vmVersion; // optional
	private TJvmGcType gcType; // optional

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public TJvmGcType getGcType() {
		return gcType;
	}

	public void setGcType(TJvmGcType gcType) {
		this.gcType = gcType;
	}
}
