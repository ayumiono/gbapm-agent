package com.gb.apm.dapper.context;

import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.ServiceType;

public interface _SpanEventRecorder extends FrameAttachment {

    void recordTime(boolean time);
    
    void recordException(Throwable throwable);

    void recordException(boolean markError, Throwable throwable);

    void recordApiId(int apiId);

    void recordApi(MethodDescriptor methodDescriptor);

    void recordApi(MethodDescriptor methodDescriptor, Object[] args);

    void recordApi(MethodDescriptor methodDescriptor, Object args, int index);

    void recordApi(MethodDescriptor methodDescriptor, Object[] args, int start, int end);

    void recordApiCachedString(MethodDescriptor methodDescriptor, String args, int index);

    void recordAttribute(AnnotationKey key, String value);

    void recordAttribute(AnnotationKey key, int value);

    void recordAttribute(AnnotationKey key, Object value);

    void recordServiceType(ServiceType serviceType);

    void recordRpcName(String rpc);

    void recordDestinationId(String destinationId);

    void recordEndPoint(String endPoint);

    void recordNextSpanId(long spanId);

    void recordAsyncId(int asyncId);
    
    void recordNextAsyncId(int asyncId);
    
    void recordAsyncSequence(short sequence);
}