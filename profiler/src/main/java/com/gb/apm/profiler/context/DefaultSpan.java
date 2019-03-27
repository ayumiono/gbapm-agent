package com.gb.apm.profiler.context;

import java.util.ArrayList;
import java.util.List;

import com.gb.apm.common.utils.TransactionIdUtils;
import com.gb.apm.dapper.context.IAnnotation;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.model.Annotation;
import com.gb.apm.model.TIntStringValue;
import com.gb.apm.model.TSpan;
import com.gb.apm.model.TSpanEvent;

public class DefaultSpan extends TSpan implements Span {
	
	private Object frameObject;
	
	private transient boolean timeRecording = true;
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T findAnnotation(int annotationKey) {
		for(Annotation annotation : this.getAnnotations()) {
			if(String.valueOf(annotation.getKey()).equals(String.valueOf(annotationKey))) {
				return (T) annotation.getValue();
			}
		}
		return null;
	}

	@Override
	public Object attachFrameObject(Object attachObject) {
		final Object before = this.frameObject;
        this.frameObject = attachObject;
        return before;
	}

	@Override
	public Object getFrameObject() {
		return this.frameObject;
	}

	@Override
	public Object detachFrameObject() {
		final Object delete = this.frameObject;
        this.frameObject = null;
        return delete;
	}

	@Override
	public void recordTraceId(TraceId traceId) {
		if (traceId == null) {
			throw new NullPointerException("traceId must not be null");
		}
		final String agentId = this.getAgentId();
		if (agentId == null) {
			throw new NullPointerException("agentId must not be null");
		}

		final String transactionAgentId = traceId.getAgentId();
		if(!agentId.equals(transactionAgentId)) {
			this.setTransactionId(TransactionIdUtils.formatString(transactionAgentId, traceId.getAgentStartTime(), traceId.getTransactionSequence()));
		}else {
			this.setTransactionId(TransactionIdUtils.formatString(agentId, traceId.getAgentStartTime(), traceId.getTransactionSequence()));
		}

		this.setSpanId(traceId.getSpanId());
		final long parentSpanId = traceId.getParentSpanId();
		if (traceId.getParentSpanId() != -1) {
			this.setParentSpanId(parentSpanId);
		}
	}


	@Override
	public void markBeforeTime() {
		this.setStartTime(System.currentTimeMillis());
	}

	@Override
	public void setExceptionInfo(String exceptionInfo) {
		this.setExceptionInfo(-1, exceptionInfo);
	}

	@Override
	public boolean isErr() {
		if(super.getErr() != null) {
			return true;
		}
		return false;
	}

	@Override
	public void setErrCode(int err) {
		super.setErr(err);
	}

	@Override
	public void markAfterTime() {
		final int after = (int)(System.currentTimeMillis() - this.getStartTime());
        if (after != 0) {
            this.setElapsed(after);
        }
	}

	@Override
	public void setExceptionInfo(int exceptionClassId, String exceptionMessage) {
		final TIntStringValue exceptionInfo = new TIntStringValue(exceptionClassId);
        if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
            exceptionInfo.setStringValue(exceptionMessage);
        }
        super.setExceptionInfo(exceptionInfo);
	}

	@Override
	public void setSpanEventList(List<_SpanEvent> spanEvent) {
		List<TSpanEvent> castList = new ArrayList<>();
		for(_SpanEvent event : spanEvent) {
			castList.add((TSpanEvent)event);
		}
		super.setTspanEventList(castList);
	}

	@Override
	public boolean isTimeRecording() {
		return this.timeRecording;
	}

	@Override
	public void addAnnotation(IAnnotation annotation) {
		super.addAnnotation((KVAnnotation)annotation);
	}
}
