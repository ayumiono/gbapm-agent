package com.gb.apm.profiler.context.module;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.profiler.AgentInformation;
import com.gb.apm.profiler.ClassFileTransformerDispatcher;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface ApplicationContext {

    ProfilerConfig getProfilerConfig();

    TraceContext getTraceContext();

    InstrumentEngine getInstrumentEngine();


    DynamicTransformTrigger getDynamicTransformTrigger();

    ClassFileTransformerDispatcher getClassFileTransformerDispatcher();

    AgentInformation getAgentInformation();


    void start();

    void close();
}
