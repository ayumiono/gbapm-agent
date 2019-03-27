package com.gb.apm.profiler.context.provider;

import com.gb.apm.collector.DataSender;
import com.gb.apm.collector.QueueDataSender.InvokeStackEvent;
import com.gb.apm.collector.log.LogDataSenderFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AgentInfoDataSenderProvider implements Provider<DataSender> {
	
	private LogDataSenderFactory factory;
	
	@Inject
	public AgentInfoDataSenderProvider(LogDataSenderFactory factory) {
		this.factory = factory;
	}

	@Override
	public DataSender get() {
		return factory.create(InvokeStackEvent.AGENTINFO.getType());
	}

}
