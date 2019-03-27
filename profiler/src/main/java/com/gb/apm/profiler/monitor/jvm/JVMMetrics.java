package com.gb.apm.profiler.monitor.jvm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gb.apm.profiler.monitor.jvm.JVMGCMXBeanMetrics.JVMGCMetrics;
import com.gb.apm.profiler.monitor.jvm.MemoryPoolMXbeanMetrics.JvmMemoryUsage;
import com.gb.apm.profiler.monitor.jvm.ThreadingMXBeanMetrics.JVMThreadMetrics;

public class JVMMetrics implements Serializable{
	
	private static final long serialVersionUID = 6424089950774617481L;
	
	private double processCpuLoad;
	private JVMThreadMetrics thread;
	private JvmMemoryUsage heapMemory;
	private JvmMemoryUsage nonHeapMemory;
	private List<JVMGCMetrics> gc;
	private Map<String,JvmMemoryUsage> memoryPool;
	private String agentName;
	private String agentId;
	private long startMs;
	
	public JVMMetrics(String agentName,String agentId) {
		this.startMs = System.currentTimeMillis();
		this.agentName = agentName;
		this.agentId = agentId;
	}
	
	public double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public void setProcessCpuLoad(double processCpuLoad) {
		this.processCpuLoad = processCpuLoad;
	}

	public JVMThreadMetrics getThread() {
		return thread;
	}

	public void setThread(JVMThreadMetrics thread) {
		this.thread = thread;
	}

	public JvmMemoryUsage getHeapMemory() {
		return heapMemory;
	}

	public void setHeapMemory(JvmMemoryUsage heapMemory) {
		this.heapMemory = heapMemory;
	}

	public JvmMemoryUsage getNonHeapMemory() {
		return nonHeapMemory;
	}

	public void setNonHeapMemory(JvmMemoryUsage nonHeapMemory) {
		this.nonHeapMemory = nonHeapMemory;
	}

	public Map<String, JvmMemoryUsage> getMemoryPool() {
		return memoryPool;
	}

	public void setMemoryPool(Map<String, JvmMemoryUsage> memoryPool) {
		this.memoryPool = memoryPool;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public long getStartMs() {
		return startMs;
	}

	public void setStartMs(long startMs) {
		this.startMs = startMs;
	}

	public List<JVMGCMetrics> getGc() {
		return gc;
	}

	public void setGc(List<JVMGCMetrics> gc) {
		this.gc = gc;
	}
}
