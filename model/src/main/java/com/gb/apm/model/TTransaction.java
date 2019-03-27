package com.gb.apm.model;

public class TTransaction extends APMModel {
	private long sampledNewCount; // optional
	private long sampledContinuationCount; // optional
	private long unsampledNewCount; // optional
	private long unsampledContinuationCount; // optional

	public long getSampledNewCount() {
		return sampledNewCount;
	}

	public void setSampledNewCount(long sampledNewCount) {
		this.sampledNewCount = sampledNewCount;
	}

	public long getSampledContinuationCount() {
		return sampledContinuationCount;
	}

	public void setSampledContinuationCount(long sampledContinuationCount) {
		this.sampledContinuationCount = sampledContinuationCount;
	}

	public long getUnsampledNewCount() {
		return unsampledNewCount;
	}

	public void setUnsampledNewCount(long unsampledNewCount) {
		this.unsampledNewCount = unsampledNewCount;
	}

	public long getUnsampledContinuationCount() {
		return unsampledContinuationCount;
	}

	public void setUnsampledContinuationCount(long unsampledContinuationCount) {
		this.unsampledContinuationCount = unsampledContinuationCount;
	}
}
