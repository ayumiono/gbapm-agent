package com.gb.apm.dapper.context;

import com.gb.apm.annotations.InterfaceAudience;

/**
 * @author emeroad
 */
public interface _BaseTraceFactory {

    _Trace disableSampling();

    // picked as sampling target at remote
    _Trace continueTraceObject(TraceId traceId);

    _Trace continueTraceObject(_Trace trace);

    @InterfaceAudience.LimitedPrivate("vert.x")
    _Trace continueAsyncTraceObject(TraceId traceId);

    _Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime);

    _Trace newTraceObject();

    @InterfaceAudience.LimitedPrivate("vert.x")
    _Trace newAsyncTraceObject();
}
