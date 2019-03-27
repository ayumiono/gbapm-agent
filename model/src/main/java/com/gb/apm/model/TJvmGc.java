package com.gb.apm.model;

public class TJvmGc extends APMModel {
	private TJvmGcType type; // required
	private long jvmMemoryHeapUsed; // required
	private long jvmMemoryHeapMax; // required
	private long jvmMemoryNonHeapUsed; // required
	private long jvmMemoryNonHeapMax; // required
	private long jvmGcOldCount; // required
	private long jvmGcOldTime; // required
	private TJvmGcDetailed jvmGcDetailed; // optional

	public TJvmGcType getType() {
		return type;
	}

	public void setType(TJvmGcType type) {
		this.type = type;
	}

	public long getJvmMemoryHeapUsed() {
		return jvmMemoryHeapUsed;
	}

	public void setJvmMemoryHeapUsed(long jvmMemoryHeapUsed) {
		this.jvmMemoryHeapUsed = jvmMemoryHeapUsed;
	}

	public long getJvmMemoryHeapMax() {
		return jvmMemoryHeapMax;
	}

	public void setJvmMemoryHeapMax(long jvmMemoryHeapMax) {
		this.jvmMemoryHeapMax = jvmMemoryHeapMax;
	}

	public long getJvmMemoryNonHeapUsed() {
		return jvmMemoryNonHeapUsed;
	}

	public void setJvmMemoryNonHeapUsed(long jvmMemoryNonHeapUsed) {
		this.jvmMemoryNonHeapUsed = jvmMemoryNonHeapUsed;
	}

	public long getJvmMemoryNonHeapMax() {
		return jvmMemoryNonHeapMax;
	}

	public void setJvmMemoryNonHeapMax(long jvmMemoryNonHeapMax) {
		this.jvmMemoryNonHeapMax = jvmMemoryNonHeapMax;
	}

	public long getJvmGcOldCount() {
		return jvmGcOldCount;
	}

	public void setJvmGcOldCount(long jvmGcOldCount) {
		this.jvmGcOldCount = jvmGcOldCount;
	}

	public long getJvmGcOldTime() {
		return jvmGcOldTime;
	}

	public void setJvmGcOldTime(long jvmGcOldTime) {
		this.jvmGcOldTime = jvmGcOldTime;
	}

	public TJvmGcDetailed getJvmGcDetailed() {
		return jvmGcDetailed;
	}

	public void setJvmGcDetailed(TJvmGcDetailed jvmGcDetailed) {
		this.jvmGcDetailed = jvmGcDetailed;
	}
}