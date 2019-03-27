package com.gb.apm.profiler.context.id;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;

/**
 * @author jaehong.kim
 */
public class DefaultAsyncTraceId implements AsyncTraceId {

    private static final AtomicIntegerFieldUpdater<DefaultAsyncTraceId> ASYNC_SEQUENCE_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(DefaultAsyncTraceId.class, "asyncSequence");

    private final TraceId traceId;
    private final int asyncId;
    private final long startTime;

    @SuppressWarnings("unused")
    private volatile int asyncSequence = 0;

    public DefaultAsyncTraceId(final TraceId traceId, final int asyncId, final long startTime) {
        if (traceId == null) {
            throw new IllegalArgumentException("traceId must not be null.");
        }

        this.traceId = traceId;
        this.asyncId = asyncId;
        this.startTime = startTime;
    }

    public int getAsyncId() {
        return asyncId;
    }

    public short nextAsyncSequence() {
        return (short) ASYNC_SEQUENCE_UPDATER.incrementAndGet(this);
    }

    @Override
    public TraceId getNextTraceId() {
        return traceId.getNextTraceId();
    }

    @Override
    public long getSpanId() {
        return traceId.getSpanId();
    }

    @Override
    public String getTransactionId() {
        return traceId.getTransactionId();
    }

    @Override
    public String getAgentId() {
        return traceId.getAgentId();
    }

    @Override
    public long getAgentStartTime() {
        return traceId.getAgentStartTime();
    }

    @Override
    public long getTransactionSequence() {
        return traceId.getTransactionSequence();
    }

    @Override
    public long getParentSpanId() {
        return traceId.getParentSpanId();
    }

    @Override
    public short getFlags() {
        return traceId.getFlags();
    }

    @Override
    public boolean isRoot() {
        return traceId.isRoot();
    }

    @Override
    public long getSpanStartTime() {
        return startTime;
    }

    @Override
    public TraceId getParentTraceId() {
        return traceId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("traceId=").append(traceId);
        sb.append(", asyncId=").append(asyncId);
        sb.append(", startTime=").append(startTime);
        sb.append(", asyncSequence=").append(asyncSequence);
        sb.append('}');
        return sb.toString();
    }
}