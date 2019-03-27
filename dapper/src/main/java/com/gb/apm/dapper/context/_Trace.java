package com.gb.apm.dapper.context;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.dapper.context.scope.TraceScope;

/**
 * @author emeroad
 * @author jaehong.kim
 */
public interface _Trace extends StackOperation {
	// ----------------------------------------------
	// activeTrace related api
	// TODO extract interface???
	long getId();

	long getStartTime();

	Thread getBindThread();

	// ------------------------------------------------

	TraceId getTraceId();

	AsyncTraceId getAsyncTraceId();

	/**
	 * internal experimental api
	 */
	@InterfaceAudience.LimitedPrivate("vert.x")
	AsyncTraceId getAsyncTraceId(boolean closeable);

	boolean canSampled();

	boolean isRoot();

	boolean isAsync();

	SpanRecorder getSpanRecorder();

	_SpanEventRecorder currentSpanEventRecorder();

	void close();

	/**
	 * internal experimental api
	 */
	@InterfaceAudience.LimitedPrivate("vert.x")
	void flush();

	TraceScope getScope(String name);

	TraceScope addScope(String name);
}