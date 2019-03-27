package com.gb.apm.profiler.monitor.jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import com.codahale.metrics.Gauge;
import com.gb.apm.profiler.monitor.jvm.JVMGCMXBeanMetrics.JVMGCMetrics;

public class JVMGCMXBeanMetrics implements Gauge<List<JVMGCMetrics>>{
	
	List<GarbageCollectorMXBean> gcMXBean;
	
	public JVMGCMXBeanMetrics() {
		gcMXBean = ManagementFactory.getGarbageCollectorMXBeans();
	}
	
	@Override
	public List<JVMGCMetrics> getValue() {
		final List<JVMGCMetrics> metris = new ArrayList<>();
		for(GarbageCollectorMXBean mxbean : gcMXBean) {
			metris.add(new JVMGCMetrics(mxbean.getName(), mxbean.getCollectionCount(), mxbean.getCollectionTime()));
		}
		return metris;
	}
	
	static class JVMGCMetrics {
		private String gcAlgorithm;
		private long collectionCount;
		private long collectionTime;
		
		public JVMGCMetrics(String gcAlgorithm,long collectionCount,long collectionTime) {
			this.gcAlgorithm = gcAlgorithm;
			this.collectionCount = collectionCount;
			this.collectionTime = collectionTime;
		}

		public String getGcAlgorithm() {
			return gcAlgorithm;
		}

		public void setGcAlgorithm(String gcAlgorithm) {
			this.gcAlgorithm = gcAlgorithm;
		}

		public long getCollectionCount() {
			return collectionCount;
		}

		public void setCollectionCount(long collectionCount) {
			this.collectionCount = collectionCount;
		}

		public long getCollectionTime() {
			return collectionTime;
		}

		public void setCollectionTime(long collectionTime) {
			this.collectionTime = collectionTime;
		}
	}
}
