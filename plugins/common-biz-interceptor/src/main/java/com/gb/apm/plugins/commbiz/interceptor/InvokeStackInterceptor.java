package com.gb.apm.plugins.commbiz.interceptor;

import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.ApiIdAwareAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.annotation.TargetFilter;
import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.dapper.context.MethodDescriptor;
import com.gb.apm.plugins.commbiz.CommBizConstants;

@TargetFilter(type="com.gb.apm.plugins.commbiz.CommBizMethodFilter")
public class InvokeStackInterceptor implements ApiIdAwareAroundInterceptor {
	
	protected final MethodDescriptor methodDescriptor;
    protected final TraceContext traceContext;

    public InvokeStackInterceptor(TraceContext traceContext, MethodDescriptor methodDescriptor) {
        this.traceContext = traceContext;
        this.methodDescriptor = methodDescriptor;
    }

	@Override
	public void before(Object target,int apiId, Object[] args) {
		
		if(this.traceContext.currentTraceObject() == null) return;
		
		Trace trace = this.traceContext.currentTraceObject();
		trace.traceBlockBegin();
		SpanEventRecorder spanEventRecorder = trace.currentSpanEventRecorder();
		spanEventRecorder.recordServiceType(ServiceType.INTERNAL_METHOD);
		spanEventRecorder.recordApi(methodDescriptor);
		spanEventRecorder.recordAttribute(CommBizConstants.COMMBIZ_API_ARGS, args);//如果用原生的api会导致参数被拆分成多个注解
	}

	@Override
	public void after(Object target,int apiId, Object[] args, Object result, Throwable throwable) {
		Trace trace = this.traceContext.currentTraceObject();
		if(trace == null) {
			return;
		}
		SpanEventRecorder spanEventRecorder = trace.currentSpanEventRecorder();
		if(throwable != null) {
			spanEventRecorder.recordException(throwable);
		}
		spanEventRecorder.recordAttribute(AnnotationKey.RETURN_DATA, result);
		trace.traceBlockEnd();
	}
}
