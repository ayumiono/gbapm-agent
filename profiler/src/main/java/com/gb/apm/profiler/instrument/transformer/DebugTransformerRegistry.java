package com.gb.apm.profiler.instrument.transformer;

import java.lang.instrument.ClassFileTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.Filter;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.profiler.instrument.classloading.ClassInjector;
import com.gb.apm.profiler.instrument.classloading.DebugTransformerClassInjector;
import com.gb.apm.profiler.instrument.classloading.LegacyProfilerPluginClassInjector;
import com.gb.apm.profiler.plugin.ClassFileTransformerLoader;
import com.gb.apm.profiler.plugin.PluginInstrumentContext;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DebugTransformerRegistry implements TransformerRegistry {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // TODO remove next release
    private static final String DEBUG_INJECTOR_TYPE = "profiler.debug.injector.type";
    private static final String LEGACY = "legacy";
    private static final String DEBUG = "debug";

    private final Filter<String> debugTargetFilter;
    private final DebugTransformer debugTransformer;

    public DebugTransformerRegistry(ProfilerConfig profilerConfig, InstrumentEngine instrumentEngine, DynamicTransformTrigger dynamicTransformTrigger) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        this.debugTargetFilter = profilerConfig.getProfilableClassFilter();
        this.debugTransformer = newDebugTransformer(profilerConfig, instrumentEngine, dynamicTransformTrigger);
    }

    private DebugTransformer newDebugTransformer(ProfilerConfig profilerConfig, InstrumentEngine instrumentEngine, DynamicTransformTrigger dynamicTransformTrigger) {

        ClassInjector classInjector = newClassInjector(profilerConfig);

        ClassFileTransformerLoader transformerRegistry = new ClassFileTransformerLoader(profilerConfig, dynamicTransformTrigger);
        InstrumentContext debugContext = new PluginInstrumentContext(profilerConfig, instrumentEngine, dynamicTransformTrigger, classInjector, transformerRegistry);

        return new DebugTransformer(instrumentEngine, debugContext);
    }

    private ClassInjector newClassInjector(ProfilerConfig profilerConfig) {
        // TODO remove next release
        //  bug workaround for DebugTransformerClassInjector
        final String debugInjectorType = profilerConfig.readString(DEBUG_INJECTOR_TYPE, DEBUG);
        logger.info("{}:{}", DEBUG_INJECTOR_TYPE, debugInjectorType);
        if (LEGACY.equals(debugInjectorType)) {
            return new LegacyProfilerPluginClassInjector(getClass().getClassLoader());
        }
        logger.info("newDebugTransformerClassInjector()");
        return new DebugTransformerClassInjector();

    }

    @Override
    public ClassFileTransformer findTransformer(String classInternalName) {
        if (classInternalName == null) {
            throw new NullPointerException("classInternalName must not be null");
        }
        if (this.debugTargetFilter.filter(classInternalName)) {
            // Added to see if call stack view is OK on a test machine.
            return debugTransformer;
        }
        return null;
    }
}