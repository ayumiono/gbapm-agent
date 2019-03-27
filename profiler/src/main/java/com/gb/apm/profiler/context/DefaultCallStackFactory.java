package com.gb.apm.profiler.context;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.dapper.context.Span;
import com.google.inject.Inject;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultCallStackFactory implements CallStackFactory {

    private final int maxDepth;

    @Inject
    public DefaultCallStackFactory(ProfilerConfig profilerConfig) {
        this(profilerConfig.getCallStackMaxDepth());
    }

    public DefaultCallStackFactory(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public CallStack newCallStack(Span span) {
        return new CallStack(span, maxDepth);
    }
}
