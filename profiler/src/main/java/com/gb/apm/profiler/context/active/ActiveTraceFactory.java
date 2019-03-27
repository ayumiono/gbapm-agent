package com.gb.apm.profiler.context.active;

import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceFactory;
import com.gb.apm.bootstrap.core.context.TraceFactoryWrapper;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.dapper.context._Trace;

/**
 * @author Taejin Koo
 * @author emeroad
 * @author HyunGil Jeong
 */
public class ActiveTraceFactory implements TraceFactory, TraceFactoryWrapper {

    private final TraceFactory delegate;
    private final ActiveTraceRepository activeTraceRepository;

    private ActiveTraceFactory(TraceFactory delegate, ActiveTraceRepository activeTraceRepository) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }
        if (activeTraceRepository == null) {
            throw new NullPointerException("activeTraceRepository must not be null");
        }

        this.delegate = delegate;
        this.activeTraceRepository = activeTraceRepository;
    }

    public static TraceFactory wrap(TraceFactory traceFactory, ActiveTraceRepository activeTraceRepository) {
        return new ActiveTraceFactory(traceFactory, activeTraceRepository);
    }

    @Override
    public TraceFactory unwrap() {
        final TraceFactory copy = this.delegate;
        if (copy instanceof TraceFactoryWrapper) {
            return ((TraceFactoryWrapper) copy).unwrap();
        }
        return copy;
    }

    @Override
    public Trace currentTraceObject() {
        return this.delegate.currentTraceObject();
    }

    @Override
    public Trace currentRpcTraceObject() {
        return this.delegate.currentRpcTraceObject();
    }

    @Override
    public Trace currentRawTraceObject() {
        return this.delegate.currentRawTraceObject();
    }

    @Override
    public Trace disableSampling() {
        final Trace trace = this.delegate.disableSampling();
        // Unsampled continuation
        attachTrace(trace);
        return trace;
    }

    @Override
    public Trace continueTraceObject(TraceId traceID) {
        final Trace trace = this.delegate.continueTraceObject(traceID);
        // Sampled continuation
        attachTrace(trace);
        return trace;
    }

    @Override
    public Trace continueTraceObject(_Trace continueTrace) {
        final Trace trace = this.delegate.continueTraceObject(continueTrace);
        return trace;
    }

    @Override
    public Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime) {
        return this.delegate.continueAsyncTraceObject(traceId, asyncId, startTime);
    }

    @Override
    public Trace continueAsyncTraceObject(TraceId traceID) {
        final Trace trace = this.delegate.continueAsyncTraceObject(traceID);
        // Sampled continuation
        attachTrace(trace);
        return trace;
    }

    @Override
    public Trace newTraceObject() {
        final Trace trace = this.delegate.newTraceObject();
        attachTrace(trace);
        return trace;
    }

    @Override
    public Trace newAsyncTraceObject() {
        final Trace trace = this.delegate.newAsyncTraceObject();
        attachTrace(trace);
        return trace;
    }

    @Override
    public Trace removeTraceObject() {
        final Trace trace = this.delegate.removeTraceObject();
        detachTrace(trace);
        return trace;
    }

    private void attachTrace(Trace trace) {
        if (trace == null) {
            return;
        }
        this.activeTraceRepository.put(new ActiveTrace(trace));
    }

    private void detachTrace(Trace trace) {
        if (trace == null) {
            return;
        }
        final long id = trace.getId();
        this.activeTraceRepository.remove(id);
    }

}
