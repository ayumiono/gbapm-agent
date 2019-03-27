package com.gb.apm.profiler.context.provider;

import com.gb.apm.collector.DataSender;
import com.gb.apm.collector.QueueDataSender.InvokeStackEvent;
import com.gb.apm.collector.kafka.KafkaDataSenderFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class KafkaDataSenderProvider implements Provider<DataSender> {

	private KafkaDataSenderFactory factory;

	@Inject
	public KafkaDataSenderProvider(KafkaDataSenderFactory factory) {
		this.factory = factory;
	}

	@Override
	public DataSender get() {
		return factory.create(InvokeStackEvent.FINISH.getType());
	}
}
