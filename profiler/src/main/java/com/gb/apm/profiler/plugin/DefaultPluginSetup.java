package com.gb.apm.profiler.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.GuardInstrumentContext;
import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplate;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplateAware;
import com.gb.apm.bootstrap.core.plugin.ProfilerPlugin;
import com.gb.apm.profiler.instrument.classloading.ClassInjector;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultPluginSetup implements PluginSetup {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfilerConfig profilerConfig;
    private final InstrumentEngine instrumentEngine;
    private final DynamicTransformTrigger dynamicTransformTrigger;


    public DefaultPluginSetup(ProfilerConfig profilerConfig, InstrumentEngine instrumentEngine, DynamicTransformTrigger dynamicTransformTrigger) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.instrumentEngine = instrumentEngine;
        this.dynamicTransformTrigger = dynamicTransformTrigger;
    }

    @Override
    public SetupResult setupPlugin(ProfilerPlugin profilerPlugin, ClassInjector classInjector) {

        final ClassFileTransformerLoader transformerRegistry = new ClassFileTransformerLoader(profilerConfig, dynamicTransformTrigger);
        final DefaultProfilerPluginSetupContext setupContext = new DefaultProfilerPluginSetupContext(profilerConfig);
        final GuardProfilerPluginContext guardSetupContext = new GuardProfilerPluginContext(setupContext);

        final InstrumentContext instrumentContext = new PluginInstrumentContext(profilerConfig, instrumentEngine, dynamicTransformTrigger, classInjector, transformerRegistry );
        final GuardInstrumentContext guardInstrumentContext = preparePlugin(profilerPlugin, instrumentContext);
        try {
            // WARN external plugin api
            if (logger.isInfoEnabled()) {
                logger.info("{} Plugin setup", profilerPlugin.getClass().getName());
            }
            profilerPlugin.setup(guardSetupContext);
        } finally {
            guardSetupContext.close();
            guardInstrumentContext.close();
        }
        SetupResult setupResult = new SetupResult(setupContext, transformerRegistry);
        return setupResult;
    }

    private GuardInstrumentContext preparePlugin(ProfilerPlugin plugin, InstrumentContext instrumentContext) {

        final GuardInstrumentContext guardInstrumentContext = new GuardInstrumentContext(instrumentContext);
        if (plugin instanceof TransformTemplateAware) {
            if (logger.isDebugEnabled()) {
                logger.debug("{}.setTransformTemplate", plugin.getClass().getName());
            }
            final TransformTemplate transformTemplate = new TransformTemplate(guardInstrumentContext);
            ((TransformTemplateAware) plugin).setTransformTemplate(transformTemplate);
        }
        return guardInstrumentContext;
    }

}
