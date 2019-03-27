package com.gb.apm.profiler;

import com.gb.apm.common.trace.ServiceType;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface AgentInformation {
    String getAgentId();

    String getApplicationName();

    long getStartTime();

    int getPid();

    String getMachineName();

    String getHostIp();

    ServiceType getServerType();

    String getJvmVersion();

    String getAgentVersion();
}
