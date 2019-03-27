package com.gb.apm.dapper.async;

import com.gb.apm.dapper.context.AsyncTraceId;

/**
 * @author Jongho Moon
 *
 */
public interface AsyncTraceIdAccessor {
    void _$PINPOINT$_setAsyncTraceId(AsyncTraceId id);
    AsyncTraceId _$PINPOINT$_getAsyncTraceId();
}
