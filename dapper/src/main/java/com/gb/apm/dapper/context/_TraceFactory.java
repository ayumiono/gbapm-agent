package com.gb.apm.dapper.context;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface _TraceFactory extends _BaseTraceFactory {

	_Trace currentTraceObject();

	_Trace currentRpcTraceObject();

	_Trace currentRawTraceObject();

	_Trace removeTraceObject();
}
