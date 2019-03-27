package com.gb.apm.server.service;

import com.gb.apm.model.TAgentInfo;
import com.gb.apm.model.TApiMetaData;
import com.gb.apm.server.Result;

public interface ApmMessageHandlerService {
	public Result cacheApi(TApiMetaData apiMetaData);
	public void handleAgentInfo(TAgentInfo agentInfo);
}
