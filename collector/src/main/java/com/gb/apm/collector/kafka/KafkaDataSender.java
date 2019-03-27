package com.gb.apm.collector.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.alibaba.fastjson.JSON;
import com.gb.apm.bootstrap.AgentOption;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.collector.QueueDataSender;
import com.gb.apm.model.APMModel;

public class KafkaDataSender extends QueueDataSender {

	private KafkaProducer<String, String> producer;
	private String agentId;
	private String applicationName;
	private String kafkaTopic;

	private ProfilerConfig config;
	private AgentOption agentOption;

	public KafkaDataSender(InvokeStackEvent interest, ProfilerConfig config, AgentOption agentOption) {
		super(interest);
		this.config = config;
		this.agentOption = agentOption;
		this.agentId = this.agentOption.getAgentId();
		this.applicationName = this.agentOption.getApplicationName();
		String brokerList = this.config.readString("kafka.brokers", null);
		if(brokerList == null) throw new RuntimeException("kafka.brokers not set in pinpoint.config");
		String kafka_topic = this.config.readString("kafka.topic", null);
		if(kafka_topic == null) throw new RuntimeException("kafka.topic not set in pinpoint.config");
		this.kafkaTopic = kafka_topic;
		Properties properties = new Properties();
		properties.put("client.id", "gbapm-trace-producer-" + this.applicationName + "-" + this.agentId);
		properties.put("bootstrap.servers",brokerList);
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("linger.ms", 5000);
        properties.put("retries", 3);
		this.producer = new KafkaProducer<>(properties);
	}

	@Override
	public boolean deal(APMModel frame) {
		String record = JSON.toJSONString(frame);
		ProducerRecord<String, String> message = new ProducerRecord<String, String>(kafkaTopic, this.agentId, record);
		this.producer.send(message, new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if(metadata == null && exception != null) {
					logger.error("apm监控数据发送到kafka失败", exception);
				}
			}
		});
		return true;
	}
}
