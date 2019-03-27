package com.gb.apm.dapper.context;

public interface AsyncTraceId extends TraceId {

	int getAsyncId();

	long getSpanStartTime();

	TraceId getParentTraceId();

	short nextAsyncSequence();
}
