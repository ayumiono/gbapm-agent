package com.gb.apm.profiler.objectfactory;

import java.lang.annotation.Annotation;

import com.gb.apm.asm.objectfactory.ArgumentProvider;
import com.gb.apm.asm.objectfactory.Option;
import com.gb.apm.bootstrap.core.PinpointException;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.Instrumentor;
import com.gb.apm.bootstrap.core.instrument.InstrumentorDelegate;
import com.gb.apm.bootstrap.core.interceptor.annotation.Name;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.common.utils.TypeUtils;

/**
 * @author Jongho Moon
 *
 */
public class ProfilerPluginArgumentProvider implements ArgumentProvider {
    private final ProfilerConfig profilerConfig;
    private final TraceContext traceContext;
    private final InstrumentContext pluginContext;

    public ProfilerPluginArgumentProvider(ProfilerConfig profilerConfig, TraceContext traceContext, InstrumentContext pluginContext) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (traceContext == null) {
            throw new NullPointerException("traceContext must not be null");
        }
        if (pluginContext == null) {
            throw new NullPointerException("pluginContext must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.traceContext = traceContext;
        this.pluginContext = pluginContext;

    }

    @Override
    public Option get(int index, Class<?> type, Annotation[] annotations) {
        if (type == Trace.class) {
            return Option.withValue(traceContext.currentTraceObject());
        } else if (type == TraceContext.class) {
            return Option.withValue(traceContext);
        } else if (type == Instrumentor.class) {
            final InstrumentorDelegate delegate = new InstrumentorDelegate(profilerConfig, pluginContext);
            return Option.withValue(delegate);
        } else if (type == InterceptorScope.class) {
            Name annotation = TypeUtils.findAnnotation(annotations, Name.class);
            
            if (annotation == null) {
                return Option.empty();
            }
            
            InterceptorScope scope = pluginContext.getInterceptorScope(annotation.value());
            
            if (scope == null) {
                throw new PinpointException("No such Scope: " + annotation.value());
            }
            
            return Option.withValue(scope);
        }
        
        return Option.empty();
    }
}
