package com.gb.apm.server.service.impl;

import com.gb.apm.model.TAgentInfo;
import com.gb.apm.server.GBMongodbException;
import com.gb.apm.server.compoent.MongoClientTemplate;

public class AgentInfoService extends BaseImpl<TAgentInfo> {

	protected AgentInfoService(MongoClientTemplate template) throws Exception {
		super(TAgentInfo.class,template);
	}

	public void handleAgentInfo(TAgentInfo info) throws GBMongodbException {
		this.insert(info);
	}
}
