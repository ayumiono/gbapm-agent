package com.gb.apm.bootstrap.core.context;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.dapper.context._Trace;
import com.gb.apm.dapper.context._TraceFactory;

public interface TraceFactory extends _TraceFactory {
	
	Trace currentTraceObject();

	Trace currentRpcTraceObject();

	Trace currentRawTraceObject();

	Trace removeTraceObject();
	
	Trace disableSampling();

	Trace continueTraceObject(TraceId traceId);

	Trace continueTraceObject(_Trace trace);

    @InterfaceAudience.LimitedPrivate("vert.x")
    Trace continueAsyncTraceObject(TraceId traceId);

    Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime);

    Trace newTraceObject();

    @InterfaceAudience.LimitedPrivate("vert.x")
    Trace newAsyncTraceObject();
}
