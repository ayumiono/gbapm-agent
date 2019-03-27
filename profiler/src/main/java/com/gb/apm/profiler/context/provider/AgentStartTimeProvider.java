package com.gb.apm.profiler.context.provider;

import com.gb.apm.profiler.util.RuntimeMXBeanUtils;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class AgentStartTimeProvider implements Provider<Long> {

    @Inject
    public AgentStartTimeProvider() {
    }

    @Override
    public Long get() {
        return RuntimeMXBeanUtils.getVmStartTime();
    }
}
