package com.gb.apm.profiler.context.provider;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformerRegistry;
import com.gb.apm.profiler.ClassFileTransformerDispatcher;
import com.gb.apm.profiler.DefaultClassFileTransformerDispatcher;
import com.gb.apm.profiler.plugin.PluginContextLoadResult;
import com.google.inject.Inject;
import com.google.inject.Provider;



/**
 * @author Woonduk Kang(emeroad)
 */
public class ClassFileTransformerDispatcherProvider implements Provider<ClassFileTransformerDispatcher> {

    private final ProfilerConfig profilerConfig;
    private final PluginContextLoadResult pluginContextLoadResult;
    private final InstrumentEngine instrumentEngine;
    private final DynamicTransformTrigger dynamicTransformTrigger;
    private final DynamicTransformerRegistry dynamicTransformerRegistry;

    @Inject
    public ClassFileTransformerDispatcherProvider(ProfilerConfig profilerConfig, InstrumentEngine instrumentEngine, PluginContextLoadResult pluginContextLoadResult,
                                                  DynamicTransformTrigger dynamicTransformTrigger, DynamicTransformerRegistry dynamicTransformerRegistry) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (pluginContextLoadResult == null) {
            throw new NullPointerException("pluginContextLoadResult must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        if (dynamicTransformerRegistry == null) {
            throw new NullPointerException("dynamicTransformerRegistry must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.instrumentEngine = instrumentEngine;
        this.pluginContextLoadResult = pluginContextLoadResult;
        this.dynamicTransformTrigger = dynamicTransformTrigger;
        this.dynamicTransformerRegistry = dynamicTransformerRegistry;
    }

    @Override
    public ClassFileTransformerDispatcher get() {
        return new DefaultClassFileTransformerDispatcher(profilerConfig, pluginContextLoadResult, instrumentEngine, dynamicTransformTrigger, dynamicTransformerRegistry);
    }
}
