package com.gb.apm.server.module;

import java.io.IOException;

import com.gb.apm.server.compoent.ServerConfig;
import com.gb.apm.server.provider.ApmMessageHandlerServiceProvider;
import com.gb.apm.server.service.ApmMessageHandlerService;
import com.google.inject.AbstractModule;

public class ServerApplicationContextModule extends AbstractModule {
	
	private final ServerConfig config;
	
	
	public ServerApplicationContextModule() throws IOException {
		this.config = ServerConfig.load("");//FIXME
	}

	@Override
	protected void configure() {
		bind(ApmMessageHandlerService.class).toProvider(ApmMessageHandlerServiceProvider.class);
		bind(ServerConfig.class).toInstance(config);
	}

}
