package com.gb.apm.bootstrap.core.context;

import com.gb.apm.dapper.context._TraceFactoryWrapper;

public interface TraceFactoryWrapper extends _TraceFactoryWrapper {
	TraceFactory unwrap();
}
