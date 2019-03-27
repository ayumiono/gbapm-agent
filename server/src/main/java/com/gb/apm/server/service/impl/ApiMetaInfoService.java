package com.gb.apm.server.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.gb.apm.model.TApiMetaData;
import com.gb.apm.server.GBMongodbException;
import com.gb.apm.server.Result;
import com.gb.apm.server.compoent.MongoClientTemplate;

public class ApiMetaInfoService extends BaseImpl<TApiMetaData> {
	
	private static final AtomicInteger _ider = new AtomicInteger(1);

	protected ApiMetaInfoService(MongoClientTemplate template) throws Exception {
		super(TApiMetaData.class,template);
	}

	public Result cacheApi(TApiMetaData metaData) throws GBMongodbException {
		TApiMetaData condition = new TApiMetaData();
		condition.setAgentId(metaData.getAgentId());
		condition.setApiInfo(metaData.getApiInfo());
		List<TApiMetaData> res = this.query(condition);
		if(res.size() == 0) {
			metaData.setApiId(_ider.getAndIncrement());
			this.insert(metaData);
			return new Result(true, metaData.getApiId());
		} else if(res.size() == 1){
			TApiMetaData d = res.get(0);
			return new Result(false,d.getApiId());
		} else {
			throw new GBMongodbException("");
		}
	}
}
