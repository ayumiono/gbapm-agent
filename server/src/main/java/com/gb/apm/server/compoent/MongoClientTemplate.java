package com.gb.apm.server.compoent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

public class MongoClientTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoClientTemplate.class);
	
	private MongoClient client;
	
	@Inject
	private ServerConfig conf;
	
	public MongoClient client() {
		return this.client;
	}
	
	public MongoClientTemplate() {
		init();
	}

	private void init() {
		logger.info("mogo conf:{}",conf);
		MongoClientOptions.Builder build = new MongoClientOptions.Builder();
		build.connectionsPerHost(conf.readInt("max.connections.per.host", 5));
		build.connectTimeout(conf.readInt("connect.timeout", 5000));
		build.maxWaitTime(conf.readInt("max.wait.time", 5));
		build.maxConnectionIdleTime(conf.readInt("max.connection.idletime", 5));
		build.minConnectionsPerHost(conf.readInt("min.connections.per.host",5));
		MongoClientOptions option = build.build();
		this.client = new MongoClient(conf.readString("host","localhost:20071"), option);
	}
}
