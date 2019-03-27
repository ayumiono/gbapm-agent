package com.gb.apm.profiler.context.provider;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.dapper.context.Sampler;
import com.gb.apm.profiler.sampler.SamplerFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class SamplerProvider implements Provider<Sampler> {

    private final ProfilerConfig profilerConfig;

    @Inject
    public SamplerProvider(ProfilerConfig profilerConfig) {
        this.profilerConfig = profilerConfig;
    }

    @Override
    public Sampler get() {
        boolean samplingEnable = profilerConfig.isSamplingEnable();
        int samplingRate = profilerConfig.getSamplingRate();

        SamplerFactory samplerFactory = new SamplerFactory();
        return samplerFactory.createSampler(samplingEnable, samplingRate);
    }
}
