package com.gb.apm.plugins.dubbo.interceptor;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor1;
import com.gb.apm.dapper.context.MethodDescriptor;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.plugins.dubbo.DubboConstants;
import com.gb.apm.plugins.dubbo.RpcUtil;

/**
 * @author Jinkai.Ma
 */
public class DubboConsumerInterceptor implements AroundInterceptor1 {

    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;

    public DubboConsumerInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        this.descriptor = descriptor;
        this.traceContext = traceContext;
    }

    @Override
    public void before(Object target, Object arg0) {
        Trace trace = this.getTrace(target);
        if (trace == null) {
            return;
        }

        RpcInvocation invocation = (RpcInvocation) arg0;

        if (trace.canSampled()) {
            SpanEventRecorder recorder = trace.traceBlockBegin();

            // RPC call trace have to be recorded with a service code in RPC client code range.
            recorder.recordServiceType(DubboConstants.DUBBO_CONSUMER_SERVICE_TYPE);

            // You have to issue a TraceId the receiver of this request will use.
            TraceId nextId = trace.getTraceId().getNextTraceId();

            // Then record it as next span id.
            recorder.recordNextSpanId(nextId.getSpanId());

            // Finally, pass some tracing data to the server.
            // How to put them in a message is protocol specific.
            // This example assumes that the target protocol message can include any metadata (like HTTP headers).
            invocation.setAttachment(DubboConstants.META_TRANSACTION_ID, nextId.getTransactionId());
            invocation.setAttachment(DubboConstants.META_SPAN_ID, Long.toString(nextId.getSpanId()));
            invocation.setAttachment(DubboConstants.META_PARENT_SPAN_ID, Long.toString(nextId.getParentSpanId()));
            invocation.setAttachment(DubboConstants.META_PARENT_APPLICATION_TYPE, Short.toString(traceContext.getServerTypeCode()));
            invocation.setAttachment(DubboConstants.META_PARENT_APPLICATION_NAME, traceContext.getApplicationName());
            invocation.setAttachment(DubboConstants.META_FLAGS, Short.toString(nextId.getFlags()));
        } else {
            // If sampling this transaction is disabled, pass only that infomation to the server.
            invocation.setAttachment(DubboConstants.META_DO_NOT_TRACE, "1");
        }
    }

    @Override
    public void after(Object target, Object arg0, Object result, Throwable throwable) {
        Trace trace = this.getTrace(target);
        if (trace == null) {
            return;
        }

        RpcInvocation invocation = (RpcInvocation) arg0;
        
        @SuppressWarnings("rawtypes")
		DubboInvoker dubboInvoker = (DubboInvoker) target;
        
        try {
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            int timeout = dubboInvoker.getUrl().getParameter(Constants.TIMEOUT_KEY,Constants.DEFAULT_TIMEOUT);
            recorder.recordAttribute(DubboConstants.DUBBO_TIMEOUT, timeout);
            int actives = dubboInvoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.ACTIVES_KEY, 0);
            recorder.recordAttribute(DubboConstants.DUBBO_LOAD_LIMIT, actives);
            recorder.recordApi(descriptor);
            recorder.recordAttribute(DubboConstants.DUBBO_INTERFACE_ANNOTATION_KEY, RpcUtil.parseInterface(invocation));
            String group = invocation.getInvoker().getUrl().getParameter(Constants.GROUP_KEY);
            String version = invocation.getInvoker().getUrl().getParameter(Constants.VERSION_KEY);
            recorder.recordAttribute(DubboConstants.DUBBO_GROUP, group);
            recorder.recordAttribute(DubboConstants.DUBBO_VERSION, version);
            recorder.recordAttribute(DubboConstants.DUBBO_ARGS_ANNOTATION_KEY, invocation.getArguments());
            
            if (throwable == null) {
                String endPoint = RpcContext.getContext().getRemoteAddressString();
                // RPC client have to record end point (server address)
                recorder.recordEndPoint(endPoint);
                // Optionally, record the destination id (logical name of server. e.g. DB name)
                recorder.recordDestinationId(endPoint);
                recorder.recordAttribute(DubboConstants.DUBBO_RESULT_ANNOTATION_KEY, result);
            } else {
                recorder.recordException(throwable);
            }
        } finally {
            trace.traceBlockEnd();
        }
    }

    @SuppressWarnings("rawtypes")
	private Trace getTrace(Object target) {
        Invoker invoker = (Invoker) target;
        // Ignore monitor service.
        if (DubboConstants.MONITOR_SERVICE_FQCN.equals(invoker.getInterface().getName())) {
            return null;
        }

        return traceContext.currentTraceObject();
    }

}
