package com.gb.apm.model;

public class TActiveTrace extends APMModel {
	private TActiveTraceHistogram histogram; // optional

	public TActiveTraceHistogram getHistogram() {
		return histogram;
	}

	public void setHistogram(TActiveTraceHistogram histogram) {
		this.histogram = histogram;
	}
}
