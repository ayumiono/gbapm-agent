package com.gb.apm.collector.log;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.collector.DataSender;
import com.gb.apm.collector.DataSenderFactory;
import com.gb.apm.collector.QueueDataSender.InvokeStackEvent;
import com.google.inject.Inject;

public class LogDataSenderFactory implements DataSenderFactory{
	
	private ProfilerConfig config;
	
	@Inject
	public LogDataSenderFactory(ProfilerConfig config) {
		this.config = config;
	}

	@Override
	public DataSender create(String typeName) {
		if(typeName.equals(InvokeStackEvent.AGENTINFO.getType())) {
			return new AgentInfoQueueDataSender(InvokeStackEvent.AGENTINFO, config);
		}else if(typeName.equals(InvokeStackEvent.FINISH.getType())){
			return new LogQueueDataSender(InvokeStackEvent.FINISH,config);
		}else {
			return null;
		}
	}

}
