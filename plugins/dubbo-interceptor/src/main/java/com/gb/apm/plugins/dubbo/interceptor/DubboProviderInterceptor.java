package com.gb.apm.plugins.dubbo.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.RpcStatus;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.interceptor.SpanSimpleAroundInterceptor;
import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.common.utils.NumberUtils;
import com.gb.apm.dapper.context.MethodDescriptor;
import com.gb.apm.dapper.context.SpanId;
import com.gb.apm.dapper.context.SpanRecorder;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.plugins.dubbo.DubboConstants;
import com.gb.apm.plugins.dubbo.RpcUtil;

/**
 * @author Jinkai.Ma
 */
public class DubboProviderInterceptor extends SpanSimpleAroundInterceptor {
	
	static Class<?> GB_MESSAGE_PACK_CLASS; 
	static Method getCodeMethod;
	static {
		System.out.println(Thread.currentThread().getContextClassLoader());
		try {
			GB_MESSAGE_PACK_CLASS = Thread.currentThread().getContextClassLoader().loadClass("com.gb.soa.omp.ccommon.api.response.MessagePack");
			getCodeMethod = GB_MESSAGE_PACK_CLASS.getMethod("getCode");
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
			
    public DubboProviderInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor, DubboProviderInterceptor.class);
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected Trace createTrace(Object target, Object[] args) {
        Invoker invoker = (Invoker) target;

        // Ignore monitor service.
        if (DubboConstants.MONITOR_SERVICE_FQCN.equals(invoker.getInterface().getName())) {
            return traceContext.disableSampling();
        }

        RpcInvocation invocation = (RpcInvocation) args[0];

        // If this transaction is not traceable, mark as disabled.
        if (invocation.getAttachment(DubboConstants.META_DO_NOT_TRACE) != null) {
            return traceContext.disableSampling();
        }

        String transactionId = invocation.getAttachment(DubboConstants.META_TRANSACTION_ID);

        // If there's no trasanction id, a new trasaction begins here.
        if (transactionId == null) {
            return traceContext.newTraceObject();
        }

        // otherwise, continue tracing with given data.
        long parentSpanID = NumberUtils.parseLong(invocation.getAttachment(DubboConstants.META_PARENT_SPAN_ID), SpanId.NULL);
        long spanID = NumberUtils.parseLong(invocation.getAttachment(DubboConstants.META_SPAN_ID), SpanId.NULL);
        short flags = NumberUtils.parseShort(invocation.getAttachment(DubboConstants.META_FLAGS), (short) 0);
        TraceId traceId = traceContext.createTraceId(transactionId, parentSpanID, spanID, flags);

        return traceContext.continueTraceObject(traceId);
    }


    @Override
    protected void doInBeforeTrace(SpanRecorder recorder, Object target, Object[] args) {
        RpcInvocation invocation = (RpcInvocation) args[0];
        RpcContext rpcContext = RpcContext.getContext();

        // You have to record a service type within Server range.
        recorder.recordServiceType(DubboConstants.DUBBO_PROVIDER_SERVICE_TYPE);

        // Record rpc name, client address, server address.
        recorder.recordRpcName(RpcUtil.parseInterface(invocation));
        recorder.recordEndPoint(rpcContext.getLocalAddressString());
        recorder.recordRemoteAddress(rpcContext.getRemoteAddressString());
        String group = invocation.getInvoker().getUrl().getParameter(Constants.GROUP_KEY);
        String version = invocation.getInvoker().getUrl().getParameter(Constants.VERSION_KEY);
        
        long txc_id = NumberUtils.parseLong(invocation.getAttachment(DubboConstants.TXC_XID), 0);
        if(txc_id != 0) {
        	recorder.recordAttribute(AnnotationKey.TXC_XID, txc_id);
        }
        
        recorder.recordAttribute(DubboConstants.DUBBO_GROUP, group);
        recorder.recordAttribute(DubboConstants.DUBBO_VERSION, version);
        
        @SuppressWarnings("rawtypes")
		Invoker invoker = (Invoker) target;
        String methodName = invocation.getMethodName();
        RpcStatus count = RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName());
        int max = invoker.getUrl().getMethodParameter(methodName, Constants.EXECUTES_KEY, 0);
        recorder.recordAttribute(DubboConstants.DUBBO_LOAD_LIMIT, max);
        if(max > 0) {
        	Semaphore semaphore = count.getSemaphore(max);
            int currentLoad = max - semaphore.availablePermits();
            recorder.recordAttribute(DubboConstants.DUBBO_PROVIDER_LOAD, currentLoad);
        }

        // If this transaction did not begin here, record parent(client who sent this request) information
        if (!recorder.isRoot()) {
            String parentApplicationName = invocation.getAttachment(DubboConstants.META_PARENT_APPLICATION_NAME);
            if (parentApplicationName != null) {
                short parentApplicationType = NumberUtils.parseShort(invocation.getAttachment(DubboConstants.META_PARENT_APPLICATION_TYPE), ServiceType.UNDEFINED.getCode());
                recorder.recordParentApplication(parentApplicationName, parentApplicationType);
                // Pinpoint finds caller - callee relation by matching caller's end point and callee's acceptor host.
                // https://github.com/naver/pinpoint/issues/1395
                recorder.recordAcceptorHost(rpcContext.getLocalAddressString());
            }
        }
    }

    @Override
    protected void doInAfterTrace(SpanRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        RpcInvocation invocation = (RpcInvocation) args[0];
        
        recorder.recordApi(methodDescriptor);
        recorder.recordAttribute(DubboConstants.DUBBO_ARGS_ANNOTATION_KEY, invocation.getArguments());
        
        RpcResult rpcResult = (RpcResult) result;
        if (throwable == null) {
        	if(rpcResult.hasException()) {
        		recorder.recordException(rpcResult.getException());
            }else {
            	Object value = rpcResult.getValue();
            	recorder.recordAttribute(DubboConstants.DUBBO_RESULT_ANNOTATION_KEY, value);
            	if(GB_MESSAGE_PACK_CLASS.isAssignableFrom(value.getClass())) {
            		try {
						long code = (long) getCodeMethod.invoke(value);
						recorder.recordAttribute(DubboConstants.GB_MESSAGE_PACK_CODE, code);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
            	}
            }
        } else {
            recorder.recordException(throwable);
        }
    }
}
