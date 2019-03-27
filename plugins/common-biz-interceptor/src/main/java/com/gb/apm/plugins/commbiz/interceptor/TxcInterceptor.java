package com.gb.apm.plugins.commbiz.interceptor;

import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;
import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.dapper.context.MethodDescriptor;

public class TxcInterceptor implements AroundInterceptor {
	
	protected final MethodDescriptor methodDescriptor;
    protected final TraceContext traceContext;
	
	public TxcInterceptor(TraceContext traceContext, MethodDescriptor methodDescriptor) {
        this.traceContext = traceContext;
        this.methodDescriptor = methodDescriptor;
    }

	@Override
	public void before(Object target, Object[] args) {
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		SpanEventRecorder recorder = this.traceContext.currentTraceObject().currentSpanEventRecorder();
		if(throwable == null) {
			long txc_id = (long) result;
			recorder.recordAttribute(AnnotationKey.TXC_XID, txc_id);
		}
	}
}
