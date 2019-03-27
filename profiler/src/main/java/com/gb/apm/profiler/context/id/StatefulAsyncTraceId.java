package com.gb.apm.profiler.context.id;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.dapper.context.AsyncState;
import com.gb.apm.dapper.context.AsyncStateSupport;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;

/**
 * @author jaehong.kim
 */
@InterfaceAudience.LimitedPrivate("vert.x")
public class StatefulAsyncTraceId implements AsyncTraceId, AsyncStateSupport {
    private final AsyncTraceId traceId;
    private final AsyncState asyncState;

    public StatefulAsyncTraceId(final AsyncTraceId traceId, final AsyncState asyncState) {
        if (traceId == null ) {
            throw new IllegalArgumentException("traceId must not be null");
        }
        if (asyncState == null) {
            throw new NullPointerException("asyncState must not be null");
        }
        this.traceId = traceId;
        this.asyncState = asyncState;
    }

    @Override
    public int getAsyncId() {
        return this.traceId.getAsyncId();
    }

    @Override
    public short nextAsyncSequence() {
        return this.traceId.nextAsyncSequence();
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
        return this.traceId.getSpanStartTime();
    }

    @Override
    public TraceId getParentTraceId() {
        return this.traceId.getParentTraceId();
    }

    @Override
    public AsyncState getAsyncState() {
        return asyncState;
    }

}
