package com.gb.apm.profiler.metadata;

import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.google.inject.Inject;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultStringMetaDataService implements StringMetaDataService {

    private final SimpleCache<String> stringCache = new SimpleCache<String>();

    private final String agentId;
    private final long agentStartTime;

    @Inject
    public DefaultStringMetaDataService(@AgentId String agentId, @AgentStartTime long agentStartTime) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        this.agentId = agentId;
        this.agentStartTime = agentStartTime;
    }

    @Override
    public int cacheString(final String value) {
        if (value == null) {
            return 0;
        }
        final Result result = this.stringCache.put(value);
        if (result.isNewValue()) {
        }
        return result.getId();
    }
}
