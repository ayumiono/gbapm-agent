package com.gb.apm.bootstrap.core.context;

import com.gb.apm.dapper.context._Trace;

public interface Trace extends _Trace {

	@Override
	SpanEventRecorder currentSpanEventRecorder();

	@Override
	SpanEventRecorder traceBlockBegin();

	@Override
	SpanEventRecorder traceBlockBegin(int stackId);
}
