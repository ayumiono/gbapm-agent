package com.gb.apm.profiler.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.bootstrap.core.PinpointException;
import com.gb.apm.bootstrap.core.context.BaseTraceFactory;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.bootstrap.core.context.TraceFactory;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.dapper.context._Trace;


/**
 * @author emeroad
 * @author Taejin Koo
 */
public class ThreadLocalTraceFactory implements TraceFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Binder<Trace> threadLocalBinder = new ThreadLocalBinder<Trace>();

    private final BaseTraceFactory baseTraceFactory;

    public ThreadLocalTraceFactory(BaseTraceFactory baseTraceFactory) {
        if (baseTraceFactory == null) {
            throw new NullPointerException("baseTraceFactory must not be null");
        }
        this.baseTraceFactory = baseTraceFactory;
    }


    /**
     * Return Trace object AFTER validating whether it can be sampled or not.
     *
     * @return Trace
     */
    @Override
    public Trace currentTraceObject() {
        final Trace trace = threadLocalBinder.get();
        if (trace == null) {
            return null;
        }
        if (trace.canSampled()) {
            return trace;
        }
        return null;
    }

    /**
     * Return Trace object without validating
     *
     * @return
     */
    @Override
    public Trace currentRpcTraceObject() {
        final Trace trace = threadLocalBinder.get();
        if (trace == null) {
            return null;
        }
        return trace;
    }

    @Override
    public Trace currentRawTraceObject() {
        return threadLocalBinder.get();
    }

    @Override
    public Trace disableSampling() {
        checkBeforeTraceObject();
        final Trace trace = this.baseTraceFactory.disableSampling();

        bind(trace);

        return trace;
    }

    // continue to trace the request that has been determined to be sampled on previous nodes
    @Override
    public Trace continueTraceObject(final TraceId traceId) {
        checkBeforeTraceObject();

        Trace trace = this.baseTraceFactory.continueTraceObject(traceId);

        bind(trace);
        return trace;
    }


    @Override
    public Trace continueTraceObject(_Trace trace) {
        checkBeforeTraceObject();
        Trace _trace = (Trace) trace;
        bind(_trace);
        return _trace;
    }

    private void checkBeforeTraceObject() {
        final Trace old = this.threadLocalBinder.get();
        if (old != null) {
            final PinpointException exception = new PinpointException("already Trace Object exist.");
            if (logger.isWarnEnabled()) {
                logger.warn("beforeTrace:{}", old, exception);
            }
            throw exception;
        }
    }

    @Override
    public Trace newTraceObject() {
        checkBeforeTraceObject();

        final Trace trace = this.baseTraceFactory.newTraceObject();

        bind(trace);
        return trace;
    }

    private void bind(Trace trace) {
        threadLocalBinder.set(trace);
    }

    @Override
    public Trace removeTraceObject() {
        return this.threadLocalBinder.remove();
    }

    // internal async trace.
    @Override
    public Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime) {
        checkBeforeTraceObject();

        final Trace trace = this.baseTraceFactory.continueAsyncTraceObject(traceId, asyncId, startTime);

        bind(trace);
        return trace;
    }

    // entry point async trace.
    @InterfaceAudience.LimitedPrivate("vert.x")
    @Override
    public Trace continueAsyncTraceObject(final TraceId traceId) {
        checkBeforeTraceObject();

        final Trace trace = this.baseTraceFactory.continueAsyncTraceObject(traceId);

        bind(trace);
        return trace;
    }

    // entry point async trace.
    @InterfaceAudience.LimitedPrivate("vert.x")
    @Override
    public Trace newAsyncTraceObject() {
        checkBeforeTraceObject();

        final Trace trace = this.baseTraceFactory.newAsyncTraceObject();

        bind(trace);
        return trace;
    }
}