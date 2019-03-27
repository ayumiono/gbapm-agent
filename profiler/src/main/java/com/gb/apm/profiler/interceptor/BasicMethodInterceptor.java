package com.gb.apm.profiler.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * 
 * @author netspider
 * @author emeroad
 */
public class BasicMethodInterceptor implements AroundInterceptor {

    private final Logger logger = LoggerFactory.getLogger(BasicMethodInterceptor.class);
    private final boolean isDebug = logger.isDebugEnabled();

    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;
    private final ServiceType serviceType;

    public BasicMethodInterceptor(TraceContext traceContext, MethodDescriptor descriptor, ServiceType serviceType) {
        this.descriptor = descriptor;
        this.traceContext = traceContext;
        this.serviceType = serviceType;
    }
    
    public BasicMethodInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        this(traceContext, descriptor, ServiceType.INTERNAL_METHOD);
    }

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
//            logger.beforeInterceptor(target, args); FIXME
        }

        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        final SpanEventRecorder recorder = trace.traceBlockBegin();
        recorder.recordServiceType(serviceType);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
//            logger.afterInterceptor(target, args); FIXME
        }

        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            final SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            recorder.recordApi(descriptor);
            recorder.recordException(throwable);
        } finally {
            trace.traceBlockEnd();
        }
    }
}