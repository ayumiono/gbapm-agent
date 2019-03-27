package com.gb.apm.profiler.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.bootstrap.core.context.BaseTraceFactory;
import com.gb.apm.bootstrap.core.context.Trace;
import com.gb.apm.dapper.context.AsyncTraceId;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.dapper.context._Trace;

/**
 * @author Woonduk Kang(emeroad)
 */
public class LoggingBaseTraceFactory implements BaseTraceFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BaseTraceFactory baseTraceFactory;

    public static BaseTraceFactory wrap(BaseTraceFactory baseTraceFactory) {
        if (baseTraceFactory == null) {
            throw new NullPointerException("baseTraceFactory must not be null");
        }
        return new LoggingBaseTraceFactory(baseTraceFactory);
    }

    private LoggingBaseTraceFactory(BaseTraceFactory baseTraceFactory) {
        if (baseTraceFactory == null) {
            throw new NullPointerException("baseTraceFactory must not be null");
        }

        this.baseTraceFactory = baseTraceFactory;
    }

    @Override
    public Trace disableSampling() {
        logger.debug("disableSampling()");

        return baseTraceFactory.disableSampling();
    }

    @Override
    public Trace continueTraceObject(TraceId traceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("continueTraceObject(traceId = [{}])", traceId);
        }

        return baseTraceFactory.continueTraceObject(traceId);
    }

    @Override
    public Trace continueTraceObject(_Trace trace) {
        if (logger.isDebugEnabled()) {
            logger.debug("continueTraceObject(trace = [{}])", trace);
        }

        return baseTraceFactory.continueTraceObject(trace);
    }

    @Override
    @InterfaceAudience.LimitedPrivate("vert.x")
    public Trace continueAsyncTraceObject(TraceId traceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("continueAsyncTraceObject(traceId = [{}])", traceId);
        }


        return baseTraceFactory.continueAsyncTraceObject(traceId);
    }

    @Override
    public Trace continueAsyncTraceObject(AsyncTraceId traceId, int asyncId, long startTime) {
        if (logger.isDebugEnabled()) {
            logger.debug("continueAsyncTraceObject(traceId = [{}], asyncId = [{}], startTime = [{}])", traceId, asyncId, startTime);
        }

        return baseTraceFactory.continueAsyncTraceObject(traceId, asyncId, startTime);
    }

    @Override
    public Trace newTraceObject() {
        logger.debug("newTraceObject()");

        return baseTraceFactory.newTraceObject();
    }

    @Override
    @InterfaceAudience.LimitedPrivate("vert.x")
    public Trace newAsyncTraceObject() {
        logger.debug("newAsyncTraceObject()");

        return baseTraceFactory.newAsyncTraceObject();
    }
}
