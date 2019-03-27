package com.gb.apm.server.compoent;

import com.gb.apm.remoting.RemotingServer;
import com.gb.apm.server.service.ApmMessageHandlerService;
import com.google.inject.Inject;

public class ApmServer {
	
	@Inject
	private ApmMessageHandlerService messageHandler;
	
	private RemotingServer server;
	
	public ApmServer() {}
}
