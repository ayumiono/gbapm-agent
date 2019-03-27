package com.gb.apm.profiler.context.recorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.SpanRecorder;
import com.gb.apm.profiler.context.DefaultTrace;
import com.gb.apm.profiler.context.KVAnnotation;
import com.gb.apm.profiler.metadata.SqlMetaDataService;
import com.gb.apm.profiler.metadata.StringMetaDataService;

/**
 * 
 * @author jaehong.kim
 *
 */
public class DefaultSpanRecorder extends AbstractRecorder implements SpanRecorder {
    private final Logger logger = LoggerFactory.getLogger(DefaultTrace.class.getName());
    private final boolean isDebug = logger.isDebugEnabled();
    
    private final Span span;
    private final boolean isRoot;
    private final boolean sampling;
    
    public DefaultSpanRecorder(final Span span, final boolean isRoot, final boolean sampling, final StringMetaDataService stringMetaDataService, SqlMetaDataService sqlMetaDataService) {
        super(stringMetaDataService, sqlMetaDataService);
        this.span = span;
        this.isRoot = isRoot;
        this.sampling = sampling;
    }

    public Span getSpan() {
        return span;
    }

    @Override
    public void recordStartTime(long startTime) {
        span.setStartTime(startTime);
    }

    @Override
    void setExceptionInfo(int exceptionClassId, String exceptionMessage) {
        setExceptionInfo(true, exceptionClassId, exceptionMessage);
    }

    @Override
    void setExceptionInfo(boolean markError, int exceptionClassId, String exceptionMessage) {
        span.setExceptionInfo(exceptionClassId, exceptionMessage);
        if (markError) {
            if (!span.isErr()) {
                span.setErrCode(1);
            }
        }
    }

    @Override
    public void recordApiId(int apiId) {
        setApiId0(apiId);
    }

    void setApiId0(final int apiId) {
        span.setApiId(apiId);
    }

    @Override
    void addAnnotation(KVAnnotation annotation) {
        span.addAnnotation(annotation);
    }

    @Override
    public void recordServiceType(ServiceType serviceType) {
        span.setServiceType(serviceType.getCode());
    }

    @Override
    public void recordRpcName(String rpc) {
        span.setRpc(rpc);
    }

    @Override
    public void recordRemoteAddress(String remoteAddress) {
        span.setRemoteAddr(remoteAddress);
    }

    @Override
    public void recordEndPoint(String endPoint) {
        span.setEndPoint(endPoint);
    }

    @Override
    public void recordParentApplication(String parentApplicationName, short parentApplicationType) {
            span.setParentApplicationName(parentApplicationName);
            span.setParentApplicationType(parentApplicationType);
            if (isDebug) {
                logger.debug("ParentApplicationName marked. parentApplicationName={}", parentApplicationName);
            }
    }

    @Override
    public void recordAcceptorHost(String host) {
        span.setAcceptorHost(host); // me
        if (isDebug) {
            logger.debug("Acceptor host received. host={}", host);
        }
    }

    @Override
    public boolean canSampled() {
        return sampling;
    }

    @Override
    public boolean isRoot() {
        return isRoot;
    }
    
    @Override
    public void recordTime(boolean time) {
        if (time) {
        	span.markBeforeTime();
        } else {
            span.setElapsed(0);
            span.setStartTime(0);
        }
    }


    @Override
    public Object attachFrameObject(Object frameObject) {
        return span.attachFrameObject(frameObject);
    }

    @Override
    public Object getFrameObject() {
        return span.getFrameObject();
    }

    @Override
    public Object detachFrameObject() {
        return span.detachFrameObject();
    }
}