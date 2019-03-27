package com.gb.apm.profiler.context.recorder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gb.apm.bootstrap.core.context.ParsingResult;
import com.gb.apm.bootstrap.core.context.SpanEventRecorder;
import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.model.TIntStringStringValue;
import com.gb.apm.model.TStringStringStringValue;
import com.gb.apm.profiler.context.DefaultTrace;
import com.gb.apm.profiler.context.KVAnnotation;
import com.gb.apm.profiler.context.SpanEvent;
import com.gb.apm.profiler.metadata.SqlMetaDataService;
import com.gb.apm.profiler.metadata.StringMetaDataService;

/**
 * 
 * @author jaehong.kim
 *
 */
public class WrappedSpanEventRecorder extends AbstractRecorder implements SpanEventRecorder {
	
	private final Logger logger = LoggerFactory.getLogger(DefaultTrace.class.getName());
    private final boolean isDebug = logger.isDebugEnabled();
    private final boolean rawsql;
	
    private SpanEvent spanEvent;

    public WrappedSpanEventRecorder(final boolean rawsql, final StringMetaDataService stringMetaDataService, final SqlMetaDataService sqlMetaCacheService) {
        super(stringMetaDataService, sqlMetaCacheService);
        this.rawsql = rawsql;
    }

    public void setWrapped(final SpanEvent spanEvent) {
        this.spanEvent = spanEvent;
    }

    @Override
    public void recordDestinationId(String destinationId) {
        spanEvent.setDestinationId(destinationId);
    }

    @Override
    public void recordNextSpanId(long nextSpanId) {
        if (nextSpanId == -1) {
            return;
        }
        spanEvent.setNextSpanId(nextSpanId);
    }

    @Override
    public void recordAsyncId(int asyncId) {
        spanEvent.setAsyncId(asyncId);
    }

    @Override
    public void recordNextAsyncId(int nextAsyncId) {
        spanEvent.setNextAsyncId(nextAsyncId);
    }

    @Override
    public void recordAsyncSequence(short asyncSequence) {
        spanEvent.setAsyncSequence(asyncSequence);
    }

    @Override
    void setExceptionInfo(int exceptionClassId, String exceptionMessage) {
        this.setExceptionInfo(true, exceptionClassId, exceptionMessage);
    }

    @Override
    void setExceptionInfo(boolean markError, int exceptionClassId, String exceptionMessage) {
        spanEvent.setExceptionInfo(exceptionClassId, exceptionMessage);
        if (markError) {
            final Span span = spanEvent.getSpan();
            if (!span.isErr()) {
                span.setErrCode(1);
            }
        }
    }

    @Override
    public void recordApiId(final int apiId) {
        setApiId0(apiId);
    }

    void setApiId0(final int apiId) {
        spanEvent.setApiId(apiId);
    }

    void addAnnotation(KVAnnotation annotation) {
        spanEvent.addAnnotation(annotation);
    }

    @Override
    public void recordServiceType(ServiceType serviceType) {
        spanEvent.setServiceType(serviceType.getCode());
    }

    @Override
    public void recordRpcName(String rpc) {
        spanEvent.setRpc(rpc);
    }

    @Override
    public void recordEndPoint(String endPoint) {
        spanEvent.setEndPoint(endPoint);
    }

    @Override
    public void recordTime(boolean time) {
        spanEvent.setTimeRecording(time);
    }

    @Override
    public Object detachFrameObject() {
        return spanEvent.detachFrameObject();
    }

    @Override
    public Object getFrameObject() {
        return spanEvent.getFrameObject();
    }

    @Override
    public Object attachFrameObject(Object frameObject) {
        return spanEvent.attachFrameObject(frameObject);
    }

	@Override
	public ParsingResult recordSqlInfo(String sql) {
		if (sql == null) {
            return null;
        }
        ParsingResult parsingResult = sqlMetaDataService.parseSql(sql);
        recordSqlParsingResult(parsingResult);
        return parsingResult;
	}

	@Override
	public void recordSqlParsingResult(ParsingResult parsingResult) {
		recordSqlParsingResult(parsingResult, null);
		
	}

	@Override
	public void recordSqlParsingResult(ParsingResult parsingResult, String bindValue) {
		if (parsingResult == null) {
            return;
        }
		if(rawsql) {//如果是记录rawsql，则不需要缓存
			final TStringStringStringValue tSqlValue = new TStringStringStringValue(parsingResult.getSql());
			final String output = parsingResult.getOutput();
	        if (isNotEmpty(output)) {
	            tSqlValue.setStrValue2(output);
	        }
	        if (isNotEmpty(bindValue)) {
	            tSqlValue.setStrValue3(bindValue);
	        }
	        recordSqlParam(tSqlValue);
		}else {//如果不是记录rawsql，则不需要缓存生成sql id
			final boolean isNewCache = sqlMetaDataService.cacheSql(parsingResult);
	        if (isDebug) {
	            if (isNewCache) {
	                logger.debug("update sql cache. parsingResult:{}", parsingResult);
	            } else {
	                logger.debug("cache hit. parsingResult:{}", parsingResult);
	            }
	        }
	        final TIntStringStringValue tSqlValue = new TIntStringStringValue(parsingResult.getId());
	        final String output = parsingResult.getOutput();
	        if (isNotEmpty(output)) {
	            tSqlValue.setStringValue1(output);
	        }
	        if (isNotEmpty(bindValue)) {
	            tSqlValue.setStringValue2(bindValue);
	        }
	        recordSqlParam(tSqlValue);
		}
        
	}
	
	private void recordSqlParam(TIntStringStringValue tIntStringStringValue) {
        spanEvent.addAnnotation(new KVAnnotation(AnnotationKey.SQL_ID.getCode(), JSON.toJSONString(tIntStringStringValue)));
    }
	
	private void recordSqlParam(TStringStringStringValue tStringStringStringValue) {
		spanEvent.addAnnotation(new KVAnnotation(AnnotationKey.SQL.getCode(), JSON.toJSONString(tStringStringStringValue)));
	}
	
	private static boolean isNotEmpty(final String bindValue) {
        return bindValue != null && !bindValue.isEmpty();
    }
}