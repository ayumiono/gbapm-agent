package com.gb.apm.bootstrap.core.context;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcContext;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.dapper.context._Trace;
import com.gb.apm.dapper.context._TraceContext;

/**
 * 扩展dapper中的TraceContext协议 将原getProfilerConfig接口从dapper模块中移出 新增sql解析功能
 * 
 * @author xuelong.chen
 *
 */
public interface TraceContext extends _TraceContext {
	
	ProfilerConfig getProfilerConfig();

	ParsingResult parseSql(String sql);// TODO extract jdbc related methods

	boolean cacheSql(ParsingResult parsingResult);

	JdbcContext getJdbcContext();
	
	
	@Override
	Trace currentTraceObject();

	@Override
    Trace currentRawTraceObject();

	@Override
    Trace continueTraceObject(TraceId traceId);

	@Override
    Trace continueTraceObject(_Trace trace);

	@Override
    Trace newTraceObject();

	@Override
    @InterfaceAudience.LimitedPrivate("vert.x")
    Trace newAsyncTraceObject();

	@Override
    @InterfaceAudience.LimitedPrivate("vert.x")
    Trace continueAsyncTraceObject(TraceId traceId);

	@Override
    Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime);

	@Override
    Trace removeTraceObject();
    
	@Override
    Trace disableSampling();
}
