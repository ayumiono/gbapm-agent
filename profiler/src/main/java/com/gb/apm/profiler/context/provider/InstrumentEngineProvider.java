package com.gb.apm.profiler.context.provider;

import java.lang.instrument.Instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.ASMEngine;
import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.asm.interceptor.registry.InterceptorRegistryBinder;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ObjectBinderFactory;
import com.gb.apm.bootstrap.AgentOption;
import com.gb.apm.bootstrap.core.config.DefaultProfilerConfig;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class InstrumentEngineProvider implements Provider<InstrumentEngine> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProfilerConfig profilerConfig;
    private final AgentOption agentOption;
    private final InterceptorRegistryBinder interceptorRegistryBinder;
    private final Provider<ApiMetaDataService> apiMetaDataServiceProvider;
    private final ObjectBinderFactory objectBinderFactory;
    private final Instrumentation instrumentation;

    @Inject
    public InstrumentEngineProvider(ProfilerConfig profilerConfig, Instrumentation instrumentation, ObjectBinderFactory objectBinderFactory, AgentOption agentOption, InterceptorRegistryBinder interceptorRegistryBinder, Provider<ApiMetaDataService> apiMetaDataServiceProvider) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (instrumentation == null) {
            throw new NullPointerException("instrumentation must not be null");
        }
        if (objectBinderFactory == null) {
            throw new NullPointerException("objectBinderFactory must not be null");
        }
        if (agentOption == null) {
            throw new NullPointerException("agentOption must not be null");
        }
        if (interceptorRegistryBinder == null) {
            throw new NullPointerException("interceptorRegistryBinder must not be null");
        }
        if (apiMetaDataServiceProvider == null) {
            throw new NullPointerException("apiMetaDataServiceProvider must not be null");
        }

        this.profilerConfig = profilerConfig;
        this.instrumentation = instrumentation;
        this.objectBinderFactory = objectBinderFactory;
        this.agentOption = agentOption;
        this.interceptorRegistryBinder = interceptorRegistryBinder;
        this.apiMetaDataServiceProvider = apiMetaDataServiceProvider;
    }

    public InstrumentEngine get() {
        final String instrumentEngine = profilerConfig.getProfileInstrumentEngine().toUpperCase();
        if (DefaultProfilerConfig.INSTRUMENT_ENGINE_ASM.equals(instrumentEngine)) {
            logger.info("ASM InstrumentEngine.");
            return new ASMEngine(instrumentation, objectBinderFactory, interceptorRegistryBinder, apiMetaDataServiceProvider, agentOption.getBootstrapJarPaths());

        } else {
            logger.warn("Unknown InstrumentEngine:{}", instrumentEngine);

            throw new IllegalArgumentException("Unknown InstrumentEngine:" + instrumentEngine);
        }
    }
}
