package com.gb.apm.profiler.context.provider;


import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.profiler.context.active.ActiveTraceRepository;
import com.gb.apm.profiler.context.active.DefaultActiveTraceRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;



/**
 * @author Woonduk Kang(emeroad)
 */
public class ActiveTraceRepositoryProvider implements Provider<ActiveTraceRepository> {

    private final ProfilerConfig profilerConfig;

    @Inject
    public ActiveTraceRepositoryProvider(ProfilerConfig profilerConfig) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        this.profilerConfig = profilerConfig;
    }

    public ActiveTraceRepository get() {
        if (profilerConfig.isTraceAgentActiveThread()) {
            return new DefaultActiveTraceRepository();
        }
        return null;
    }

}
