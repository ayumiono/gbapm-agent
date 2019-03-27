package com.gb.apm.profiler.context.id;

import com.gb.apm.dapper.context.TraceId;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface TraceIdFactory {

    TraceId newTraceId();

    TraceId parse(String transactionId, long parentSpanId, long spanId, short flags);

}
