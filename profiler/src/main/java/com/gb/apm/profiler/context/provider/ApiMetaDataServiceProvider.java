package com.gb.apm.profiler.context.provider;

import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.collector.EnhancedDataSender;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.gb.apm.profiler.metadata.DefaultApiMetaDataService;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ApiMetaDataServiceProvider implements Provider<ApiMetaDataService> {
	 private final String agentId;
	    private final long agentStartTime;
	    private final Provider<EnhancedDataSender> enhancedDataSenderProvider;

	    @Inject
	    public ApiMetaDataServiceProvider(@AgentId String agentId, @AgentStartTime long agentStartTime, Provider<EnhancedDataSender> enhancedDataSenderProvider) {
	        if (enhancedDataSenderProvider == null) {
	            throw new NullPointerException("enhancedDataSenderProvider must not be null");
	        }
	        this.agentId = agentId;
	        this.agentStartTime = agentStartTime;
	        this.enhancedDataSenderProvider = enhancedDataSenderProvider;
	    }

	    @Override
	    public ApiMetaDataService get() {
	        final EnhancedDataSender enhancedDataSender = this.enhancedDataSenderProvider.get();
	        return new DefaultApiMetaDataService(agentId, agentStartTime, enhancedDataSender);
	    }
}
