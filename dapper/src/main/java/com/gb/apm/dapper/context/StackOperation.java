package com.gb.apm.dapper.context;

/**
 * @author emeroad
 */
public interface StackOperation {

	int DEFAULT_STACKID = -1;

	int ROOT_STACKID = 0;

	_SpanEventRecorder traceBlockBegin();

	_SpanEventRecorder traceBlockBegin(int stackId);

	void traceBlockEnd();

	void traceBlockEnd(int stackId);

	boolean isRootStack();

	int getCallStackFrameId();
}