package com.gb.apm.model;

public class TJvmGcDetailed extends APMModel {
	private long jvmGcNewCount; // optional
	private long jvmGcNewTime; // optional
	private double jvmPoolCodeCacheUsed; // optional
	private double jvmPoolNewGenUsed; // optional
	private double jvmPoolOldGenUsed; // optional
	private double jvmPoolSurvivorSpaceUsed; // optional
	private double jvmPoolPermGenUsed; // optional
	private double jvmPoolMetaspaceUsed; // optional

	public long getJvmGcNewCount() {
		return jvmGcNewCount;
	}

	public void setJvmGcNewCount(long jvmGcNewCount) {
		this.jvmGcNewCount = jvmGcNewCount;
	}

	public long getJvmGcNewTime() {
		return jvmGcNewTime;
	}

	public void setJvmGcNewTime(long jvmGcNewTime) {
		this.jvmGcNewTime = jvmGcNewTime;
	}

	public double getJvmPoolCodeCacheUsed() {
		return jvmPoolCodeCacheUsed;
	}

	public void setJvmPoolCodeCacheUsed(double jvmPoolCodeCacheUsed) {
		this.jvmPoolCodeCacheUsed = jvmPoolCodeCacheUsed;
	}

	public double getJvmPoolNewGenUsed() {
		return jvmPoolNewGenUsed;
	}

	public void setJvmPoolNewGenUsed(double jvmPoolNewGenUsed) {
		this.jvmPoolNewGenUsed = jvmPoolNewGenUsed;
	}

	public double getJvmPoolOldGenUsed() {
		return jvmPoolOldGenUsed;
	}

	public void setJvmPoolOldGenUsed(double jvmPoolOldGenUsed) {
		this.jvmPoolOldGenUsed = jvmPoolOldGenUsed;
	}

	public double getJvmPoolSurvivorSpaceUsed() {
		return jvmPoolSurvivorSpaceUsed;
	}

	public void setJvmPoolSurvivorSpaceUsed(double jvmPoolSurvivorSpaceUsed) {
		this.jvmPoolSurvivorSpaceUsed = jvmPoolSurvivorSpaceUsed;
	}

	public double getJvmPoolPermGenUsed() {
		return jvmPoolPermGenUsed;
	}

	public void setJvmPoolPermGenUsed(double jvmPoolPermGenUsed) {
		this.jvmPoolPermGenUsed = jvmPoolPermGenUsed;
	}

	public double getJvmPoolMetaspaceUsed() {
		return jvmPoolMetaspaceUsed;
	}

	public void setJvmPoolMetaspaceUsed(double jvmPoolMetaspaceUsed) {
		this.jvmPoolMetaspaceUsed = jvmPoolMetaspaceUsed;
	}
}
