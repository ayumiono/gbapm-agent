package com.gb.apm.profiler.metadata;

import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.collector.EnhancedDataSender;
import com.gb.apm.dapper.context.MethodDescriptor;
import com.gb.apm.model.TApiMetaData;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;

public class DefaultApiMetaDataService implements ApiMetaDataService {

	private final String agentId;
	private final long agentStartTime;
	private final EnhancedDataSender enhancedDataSender;

	public DefaultApiMetaDataService(@AgentId String agentId, @AgentStartTime long agentStartTime,
			EnhancedDataSender enhancedDataSender) {
		if (agentId == null) {
			throw new NullPointerException("agentId must not be null");
		}
		if (enhancedDataSender == null) {
			throw new NullPointerException("enhancedDataSender must not be null");
		}
		this.agentId = agentId;
		this.agentStartTime = agentStartTime;
		this.enhancedDataSender = enhancedDataSender;
	}

	@Override
	public int cacheApi(final MethodDescriptor methodDescriptor) {
//		final String fullName = methodDescriptor.getFullName();
//		
//		final TApiMetaData apiMetadata = new TApiMetaData();
//		apiMetadata.setAgentId(agentId);
//		apiMetadata.setAgentStartTime(agentStartTime);
//		apiMetadata.setApiInfo(methodDescriptor.getApiDescriptor());
//		apiMetadata.setLine(methodDescriptor.getLineNumber());
//		apiMetadata.setType(methodDescriptor.getType());
//
//		this.enhancedDataSender.request(apiMetadata,1);
//		apiMetadata.setApiId(result.getId());
//		return result.getId();
		return 0;
	}
}
