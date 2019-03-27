package com.gb.apm.model;

import java.util.List;

public class TActiveTraceHistogram extends APMModel {
	private short version; // required
	private int histogramSchemaType; // optional
	private List<Integer> activeTraceCount; // optional

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public int getHistogramSchemaType() {
		return histogramSchemaType;
	}

	public void setHistogramSchemaType(int histogramSchemaType) {
		this.histogramSchemaType = histogramSchemaType;
	}

	public List<Integer> getActiveTraceCount() {
		return activeTraceCount;
	}

	public void setActiveTraceCount(List<Integer> activeTraceCount) {
		this.activeTraceCount = activeTraceCount;
	}
}
