package com.gb.apm.collector.kafka;

import com.gb.apm.bootstrap.AgentOption;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.collector.DataSender;
import com.gb.apm.collector.DataSenderFactory;
import com.gb.apm.collector.QueueDataSender.InvokeStackEvent;
import com.google.inject.Inject;

public class KafkaDataSenderFactory implements DataSenderFactory{
	
	private ProfilerConfig config;
	private AgentOption agentOption;

	@Inject
	public KafkaDataSenderFactory(ProfilerConfig config,AgentOption agentOption) {
		this.config = config;
		this.agentOption = agentOption;
	}

	@Override
	public DataSender create(String typeName) {
		return new KafkaDataSender(InvokeStackEvent.FINISH,config,agentOption);
	}
}
