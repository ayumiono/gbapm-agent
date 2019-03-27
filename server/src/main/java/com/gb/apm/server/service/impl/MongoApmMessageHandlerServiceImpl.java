package com.gb.apm.server.service.impl;

import com.gb.apm.model.TAgentInfo;
import com.gb.apm.model.TApiMetaData;
import com.gb.apm.server.Result;
import com.gb.apm.server.compoent.MongoClientTemplate;
import com.gb.apm.server.service.ApmMessageHandlerService;
import com.google.inject.Inject;

public class MongoApmMessageHandlerServiceImpl implements ApmMessageHandlerService {
	
	@Inject
	MongoClientTemplate template;
	
	@Inject
	AgentInfoService agentInfoService;
	
	@Inject
	ApiMetaInfoService apiMetaInfoService;
	
	@Override
	public Result cacheApi(TApiMetaData apiMetaData) {
		return null;
	}

	@Override
	public void handleAgentInfo(TAgentInfo agentInfo) {
		// TODO Auto-generated method stub

	}

}
