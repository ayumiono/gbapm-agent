package com.gb.apm.profiler.context;

import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.SpanRecorder;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.dapper.context.scope.TraceScope;
import com.gb.apm.profiler.context.scope.dapper.DefaultTraceScopePool;


/**
 * @author emeroad
 * @author jaehong.kim
 */
public class DisableTrace implements Trace {

    public static final String UNSUPPORTED_OPERATION  = "disable trace";
    public static final long DISABLE_TRACE_OBJECT_ID = -1;

    private final long id;
    private final long startTime;
    private final Thread bindThread;
    private final DefaultTraceScopePool scopePool = new DefaultTraceScopePool();
    
    public DisableTrace(long id) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
        this.bindThread = Thread.currentThread();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public Thread getBindThread() {
        return bindThread;
    }

    @Override
    public SpanEventRecorder traceBlockBegin() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public SpanEventRecorder traceBlockBegin(int stackId) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public void traceBlockEnd() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public void traceBlockEnd(int stackId) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public TraceId getTraceId() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public boolean canSampled() {
        // always return false
        return false;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isRootStack() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public AsyncTraceId getAsyncTraceId() {
        return getAsyncTraceId(false);
    }

    @Override
    public AsyncTraceId getAsyncTraceId(boolean closeable) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    public int getCallStackFrameId() {
        return 0;
    }

    @Override
    public SpanRecorder getSpanRecorder() {
        return null;
    }

    @Override
    public SpanEventRecorder currentSpanEventRecorder() {
        return null;
    }

    @Override
    public TraceScope getScope(String name) {
        return scopePool.get(name);
    }

    @Override
    public TraceScope addScope(String name) {
        return scopePool.add(name);
    }
}