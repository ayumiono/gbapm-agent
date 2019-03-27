package com.gb.apm.profiler.context.module;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.asm.interceptor.registry.InterceptorRegistryBinder;
import com.gb.apm.bootstrap.AgentOption;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.collector.DataSender;
import com.gb.apm.common.service.ServiceTypeRegistryService;
import com.gb.apm.profiler.AgentInformation;
import com.gb.apm.profiler.ClassFileTransformerDispatcher;
import com.gb.apm.profiler.instrument.ASMBytecodeDumpService;
import com.gb.apm.profiler.instrument.transformer.BytecodeDumpTransformer;
import com.gb.apm.profiler.monitor.AgentStatMonitor;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultApplicationContext implements ApplicationContext {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProfilerConfig profilerConfig;

//    private final AgentStatMonitor agentStatMonitor;

    private final TraceContext traceContext;


//    private final DataSender statDataSender;
    private final DataSender spanDataSender;

    private final AgentInformation agentInformation;
    private final AgentOption agentOption;

    private final ServiceTypeRegistryService serviceTypeRegistryService;

    private final ClassFileTransformerDispatcher classFileDispatcher;

    private final Instrumentation instrumentation;
    private final InstrumentEngine instrumentEngine;
    private final DynamicTransformTrigger dynamicTransformTrigger;

    private final Injector injector;


    public DefaultApplicationContext(AgentOption agentOption, final InterceptorRegistryBinder interceptorRegistryBinder) {
        if (agentOption == null) {
            throw new NullPointerException("agentOption must not be null");
        }
        if (interceptorRegistryBinder == null) {
            throw new NullPointerException("interceptorRegistryBinder must not be null");
        }

        this.agentOption = agentOption;
        this.profilerConfig = agentOption.getProfilerConfig();
        this.instrumentation = agentOption.getInstrumentation();
        this.serviceTypeRegistryService = agentOption.getServiceTypeRegistryService();

        if (logger.isInfoEnabled()) {
            logger.info("DefaultAgent classLoader:{}", this.getClass().getClassLoader());
        }

        final Module applicationContextModule = newApplicationContextModule(agentOption, interceptorRegistryBinder);
        this.injector = Guice.createInjector(Stage.PRODUCTION, applicationContextModule);

        this.instrumentEngine = injector.getInstance(InstrumentEngine.class);

        this.classFileDispatcher = injector.getInstance(ClassFileTransformerDispatcher.class);
        this.dynamicTransformTrigger = injector.getInstance(DynamicTransformTrigger.class);
        ClassFileTransformer classFileTransformer = wrap(classFileDispatcher);
        instrumentation.addTransformer(classFileTransformer, true);

        this.spanDataSender = newUdpSpanDataSender();
        logger.info("spanDataSender:{}", spanDataSender);

//        this.statDataSender = newUdpStatDataSender();
//        logger.info("statDataSender:{}", statDataSender);

        this.traceContext = injector.getInstance(TraceContext.class);

        this.agentInformation = injector.getInstance(AgentInformation.class);
        logger.info("agentInformation:{}", agentInformation);

//        this.agentStatMonitor = injector.getInstance(AgentStatMonitor.class);
    }

    public ClassFileTransformer wrap(ClassFileTransformerDispatcher classFileTransformerDispatcher) {

        final boolean enableBytecodeDump = profilerConfig.readBoolean(ASMBytecodeDumpService.ENABLE_BYTECODE_DUMP, ASMBytecodeDumpService.ENABLE_BYTECODE_DUMP_DEFAULT_VALUE);
        if (enableBytecodeDump) {
            logger.info("wrapBytecodeDumpTransformer");
            return BytecodeDumpTransformer.wrap(classFileTransformerDispatcher, profilerConfig);
        }
        return classFileTransformerDispatcher;
    }

    protected Module newApplicationContextModule(AgentOption agentOption, InterceptorRegistryBinder interceptorRegistryBinder) {
        return new ApplicationContextModule(agentOption, profilerConfig, serviceTypeRegistryService, interceptorRegistryBinder);
    }

    private DataSender newUdpStatDataSender() {

        Key<DataSender> statDataSenderKey = Key.get(DataSender.class, StatDataSender.class);
        return injector.getInstance(statDataSenderKey);
    }

    private DataSender newUdpSpanDataSender() {
        Key<DataSender> spanDataSenderKey = Key.get(DataSender.class, SpanDataSender.class);
        return injector.getInstance(spanDataSenderKey);
    }

    @Override
    public ProfilerConfig getProfilerConfig() {
        return profilerConfig;
    }

    public Injector getInjector() {
        return injector;
    }

    @Override
    public TraceContext getTraceContext() {
        return traceContext;
    }

    public DataSender getSpanDataSender() {
        return spanDataSender;
    }

    public InstrumentEngine getInstrumentEngine() {
        return instrumentEngine;
    }


    @Override
    public DynamicTransformTrigger getDynamicTransformTrigger() {
        return dynamicTransformTrigger;
    }


    @Override
    public ClassFileTransformerDispatcher getClassFileTransformerDispatcher() {
        return classFileDispatcher;
    }

    @Override
    public AgentInformation getAgentInformation() {
        return this.agentInformation;
    }



    @Override
    public void start() {
//        this.agentStatMonitor.start();
    }

    @Override
    public void close() {
//        this.agentStatMonitor.stop();

        // Need to process stop
        this.spanDataSender.stop();
//        this.statDataSender.stop();
    }
}
