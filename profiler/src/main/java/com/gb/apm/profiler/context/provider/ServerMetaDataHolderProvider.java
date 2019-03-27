package com.gb.apm.profiler.context.provider;

import java.util.List;

import com.gb.apm.dapper.context.ServerMetaDataHolder;
import com.gb.apm.profiler.AgentInfoSender;
import com.gb.apm.profiler.context.DefaultServerMetaDataHolder;
import com.gb.apm.profiler.util.RuntimeMXBeanUtils;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ServerMetaDataHolderProvider implements Provider<ServerMetaDataHolder> {

    private final Provider<AgentInfoSender> agentInfoSender;

    @Inject
    public ServerMetaDataHolderProvider(Provider<AgentInfoSender> agentInfoSender) {
        this.agentInfoSender = agentInfoSender;
    }

    @Override
    public ServerMetaDataHolder get() {
        AgentInfoSender agentInfoSender = this.agentInfoSender.get();
        List<String> vmArgs = RuntimeMXBeanUtils.getVmArgs();
        ServerMetaDataHolder serverMetaDataHolder = new DefaultServerMetaDataHolder(vmArgs);
        serverMetaDataHolder.addListener(agentInfoSender);
        return serverMetaDataHolder;
    }

}
