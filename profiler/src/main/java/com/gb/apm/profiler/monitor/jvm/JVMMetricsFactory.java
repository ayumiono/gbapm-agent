package com.gb.apm.profiler.monitor.jvm;

public class JVMMetricsFactory {
	
	String agentName;
	
	String agentId;
	
	public JVMMetricsFactory(String agentName, String agentId) {
		this.agentName = agentName;
		this.agentId = agentId;
	}
	
	public JVMMetrics createJVMMetrics() {
		return new JVMMetrics(agentName,agentId);
	}
	
}
