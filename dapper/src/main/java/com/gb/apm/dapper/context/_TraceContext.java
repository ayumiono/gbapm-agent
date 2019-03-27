package com.gb.apm.dapper.context;



import com.gb.apm.annotations.InterfaceAudience;

/**
 * @author emeroad
 * @author hyungil.jeong
 * @author Taejin Koo
 */
public interface _TraceContext {

    _Trace currentTraceObject();

    /**
     * return a trace whose sampling rate should be further verified
     * 
     * @return
     */
    _Trace currentRawTraceObject();

    _Trace continueTraceObject(TraceId traceId);

    _Trace continueTraceObject(_Trace trace);

    _Trace newTraceObject();

    /**
     * internal experimental api
     */
    @InterfaceAudience.LimitedPrivate("vert.x")
    _Trace newAsyncTraceObject();

    /**
     * internal experimental api
     */
    @InterfaceAudience.LimitedPrivate("vert.x")
    _Trace continueAsyncTraceObject(TraceId traceId);

    _Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime);

    _Trace removeTraceObject();
    
    _Trace disableSampling();

    String getAgentId();

    String getApplicationName();

    long getAgentStartTime();

    short getServerTypeCode();

    String getServerType();

    int cacheApi(MethodDescriptor methodDescriptor);

    int cacheString(String value);

    TraceId createTraceId(String transactionId, long parentSpanID, long spanID, short flags);

    ServerMetaDataHolder getServerMetaDataHolder();

    int getAsyncId();
}
