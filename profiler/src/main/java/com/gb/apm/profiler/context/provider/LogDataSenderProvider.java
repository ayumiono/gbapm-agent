package com.gb.apm.profiler.context.provider;

import com.gb.apm.collector.DataSender;
import com.gb.apm.collector.QueueDataSender.InvokeStackEvent;
import com.gb.apm.collector.log.LogDataSenderFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class LogDataSenderProvider implements Provider<DataSender> {
	
	private LogDataSenderFactory factory;
	
	@Inject
	public LogDataSenderProvider(LogDataSenderFactory factory) {
		this.factory = factory;
	}

	@Override
	public DataSender get() {
		return factory.create(InvokeStackEvent.FINISH.getType());
	}

}
