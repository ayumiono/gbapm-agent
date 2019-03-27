package com.gb.apm.dapper.context;

/**
 * @author emeroad
 */
public interface TraceWrapper {

	_Trace unwrap();

	void wrap(_Trace trace);
}
