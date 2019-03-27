package com.gb.apm.common.utils;

public class StopWatch {
	private long start;

	public void start() {
		this.start = System.currentTimeMillis();
	}

	public long stop() {
		return System.currentTimeMillis() - this.start;
	}

}
