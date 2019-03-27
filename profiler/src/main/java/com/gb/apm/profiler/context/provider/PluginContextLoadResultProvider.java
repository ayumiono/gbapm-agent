package com.gb.apm.profiler.context.provider;

import java.net.URL;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.profiler.context.module.PluginJars;
import com.gb.apm.profiler.plugin.DefaultPluginContextLoadResult;
import com.gb.apm.profiler.plugin.PluginContextLoadResult;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class PluginContextLoadResultProvider implements Provider<PluginContextLoadResult> {

    private final ProfilerConfig profilerConfig;
    private final InstrumentEngine instrumentEngine;
    private final URL[] pluginJars;
    private final DynamicTransformTrigger dynamicTransformTrigger;

    @Inject
    public PluginContextLoadResultProvider(ProfilerConfig profilerConfig, DynamicTransformTrigger dynamicTransformTrigger, InstrumentEngine instrumentEngine,
                                           @PluginJars URL[] pluginJars) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (pluginJars == null) {
            throw new NullPointerException("pluginJars must not be null");
        }

        this.profilerConfig = profilerConfig;
        this.dynamicTransformTrigger = dynamicTransformTrigger;

        this.instrumentEngine = instrumentEngine;
        this.pluginJars = pluginJars;
    }

    @Override
    public PluginContextLoadResult get() {
        return new DefaultPluginContextLoadResult(profilerConfig, dynamicTransformTrigger, instrumentEngine, pluginJars);

    }
}
