package com.gb.apm.profiler.context.active;

import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.SpanRecorder;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.profiler.context.recorder.DefaultSpanRecorder;

/**
 * @author HyunGil Jeong
 */
public class ActiveTrace {

    private final Trace trace;

    public ActiveTrace(Trace trace) {
        if (trace == null) {
            throw new NullPointerException("trace must not be null");
        }
        this.trace = trace;
    }

    public long getStartTime() {
        return this.trace.getStartTime();
    }

    public long getId() {
        return this.trace.getId();
    }

    public Thread getBindThread() {
        return this.trace.getBindThread();
    }

    public boolean isSampled() {
        return trace.canSampled();
    }

    public String getTransactionId() {
        if (!trace.canSampled()) {
            return null;
        }

        TraceId traceId = trace.getTraceId();
        if (traceId == null) {
            return null;
        }

        return traceId.getTransactionId();
    }

    public String getEntryPoint() {
        if (!trace.canSampled()) {
            return null;
        }

        SpanRecorder spanRecorder = trace.getSpanRecorder();
        if (!(spanRecorder instanceof DefaultSpanRecorder)) {
            return null;
        }

        Span span = ((DefaultSpanRecorder) spanRecorder).getSpan();
        if (span == null) {
            return null;
        }

        return span.getRpc();
    }

}
