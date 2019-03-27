package com.gb.apm.profiler.monitor.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import com.codahale.metrics.Gauge;
import com.gb.apm.profiler.monitor.jvm.ThreadingMXBeanMetrics.JVMThreadMetrics;

public class ThreadingMXBeanMetrics implements Gauge<JVMThreadMetrics>{
	
	private ThreadMXBean threadMXBean;
	
	public ThreadingMXBeanMetrics() {
		threadMXBean = ManagementFactory.getThreadMXBean();
	}
	
	@Override
	public JVMThreadMetrics getValue() {
		return new JVMThreadMetrics(threadMXBean.getPeakThreadCount(), threadMXBean.getThreadCount(), threadMXBean.getTotalStartedThreadCount());
	}
	
	static class JVMThreadMetrics {
		private int peakThreadCount;
		private int threadCount;
		private long totalStartedThreadCount;
		
		public int getPeakThreadCount() {
			return peakThreadCount;
		}

		public void setPeakThreadCount(int peakThreadCount) {
			this.peakThreadCount = peakThreadCount;
		}

		public int getThreadCount() {
			return threadCount;
		}

		public void setThreadCount(int threadCount) {
			this.threadCount = threadCount;
		}

		public long getTotalStartedThreadCount() {
			return totalStartedThreadCount;
		}

		public void setTotalStartedThreadCount(long totalStartedThreadCount) {
			this.totalStartedThreadCount = totalStartedThreadCount;
		}

		public JVMThreadMetrics(int peakThreadCount,int threadCount,long totalStartedThreadCount) {
			this.peakThreadCount = peakThreadCount;
			this.threadCount = threadCount;
			this.totalStartedThreadCount = totalStartedThreadCount;
		}
	}
}
