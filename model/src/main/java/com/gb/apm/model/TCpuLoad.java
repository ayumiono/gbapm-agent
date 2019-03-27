package com.gb.apm.model;

public class TCpuLoad extends APMModel {
	private double jvmCpuLoad; // optional
	private double systemCpuLoad; // optional

	public double getJvmCpuLoad() {
		return jvmCpuLoad;
	}

	public void setJvmCpuLoad(double jvmCpuLoad) {
		this.jvmCpuLoad = jvmCpuLoad;
	}

	public double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public void setSystemCpuLoad(double systemCpuLoad) {
		this.systemCpuLoad = systemCpuLoad;
	}
}
