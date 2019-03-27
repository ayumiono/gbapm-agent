package com.gb.apm.profiler.context;

import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.SpanFactory;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.gb.apm.profiler.context.module.ApplicationName;
import com.gb.apm.profiler.context.module.ApplicationServerType;
import com.google.inject.Inject;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultSpanFactory implements SpanFactory {

    private final String applicationName;
    private final String agentId;
    private final long agentStartTime;
    private final ServiceType applicationServiceType;

    @Inject
    public DefaultSpanFactory(@ApplicationName String applicationName, @AgentId String agentId, @AgentStartTime long agentStartTime,
                                   @ApplicationServerType ServiceType applicationServiceType) {

        if (applicationName == null) {
            throw new NullPointerException("applicationName must not be null");
        }
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (applicationServiceType == null) {
            throw new NullPointerException("applicationServiceType must not be null");
        }

        this.applicationName = applicationName;
        this.agentId = agentId;
        this.agentStartTime = agentStartTime;
        this.applicationServiceType = applicationServiceType;
    }

    @Override
    public Span newSpan() {
        final Span span = new DefaultSpan();
        span.setAgentId(agentId);
        span.setApplicationName(applicationName);
        span.setAgentStartTime(agentStartTime);
        span.setApplicationServiceType(applicationServiceType.getCode());
        span.markBeforeTime();
        return span;
    }

}
