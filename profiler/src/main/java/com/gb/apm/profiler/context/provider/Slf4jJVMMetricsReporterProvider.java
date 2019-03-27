package com.gb.apm.profiler.context.provider;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.profiler.AgentInformation;
import com.gb.apm.profiler.monitor.jvm.JVMMetricRegistry;
import com.gb.apm.profiler.monitor.jvm.JVMMetricRegistry.JVMMetricRegistryBuilder;
import com.gb.apm.profiler.monitor.jvm.JVMMetricsFactory;
import com.gb.apm.profiler.monitor.jvm.Slf4jJVMMetricsReporter;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class Slf4jJVMMetricsReporterProvider implements Provider<Slf4jJVMMetricsReporter> {
	
	private final ProfilerConfig config;
	private final AgentInformation agentInformation;
	
	@Inject
	public Slf4jJVMMetricsReporterProvider(AgentInformation agentInformation, ProfilerConfig profileConfig) {
		if(profileConfig == null) {
			throw new NullPointerException("profileConfig must not be null");
		}
		this.config = profileConfig;
		this.agentInformation = agentInformation;
	}

	@Override
	public Slf4jJVMMetricsReporter get() {
		String jvminfoPath = config.readString("profiler.agent.info.log", null);
		if(jvminfoPath == null) return null;
		boolean cpuload = config.readBoolean("profiler.agent.info.cpuload", true);
		boolean thread = config.readBoolean("profiler.agent.info.thread", true);
		boolean memory = config.readBoolean("profiler.agent.info.memory", true);
		boolean gc = config.readBoolean("profiler.agent.info.gc", true);
		int duration = config.readInt("profiler.agent.info.duration", 5);
		JVMMetricRegistryBuilder builder = JVMMetricRegistry.config()
		.output(jvminfoPath).delay(20)//spring boot项目
		.duration(duration);
		if(thread) {
			builder.thread();
		}
		if(cpuload) {
			builder.cpuload();
		}
		if(memory) {
			builder.memory();
		}
		if(gc) {
			builder.gc();
		}
		JVMMetricsFactory facotry = new JVMMetricsFactory(agentInformation.getApplicationName(),agentInformation.getHostIp()+"-"+"-"+agentInformation.getAgentId());
		return builder.build(facotry);
	}

}
