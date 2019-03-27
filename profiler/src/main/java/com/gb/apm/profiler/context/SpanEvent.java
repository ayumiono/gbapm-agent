package com.gb.apm.profiler.context;

import com.gb.apm.dapper.context.FrameAttachment;
import com.gb.apm.dapper.context.IAnnotation;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.model.Annotation;
import com.gb.apm.model.TIntStringValue;
import com.gb.apm.model.TSpanEvent;

/**
 * Span represent RPC
 *
 * @author netspider
 * @author emeroad
 */
public class SpanEvent extends TSpanEvent implements FrameAttachment,Span._SpanEvent {

	private transient Span span;
    private transient int stackId;
    private transient boolean timeRecording = true;
    private Object frameObject;
    
    public SpanEvent() {}
    
    public SpanEvent(Span span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }
        this.span = span;
    }
    
    @Override
    @SuppressWarnings("unchecked")
	public <T> T findAnnotation(int annotationKey) {
    	for(Annotation annotation : this.annotations) {
    		if(String.valueOf(annotation.getKey()).equals(String.valueOf(annotationKey))) {
    			return (T) annotation.getValue();
    		}
    	}
    	return null;
    }

    public void addAnnotation(KVAnnotation annotation) {
        this.addToAnnotations(annotation);
    }

    public void setExceptionInfo(int exceptionClassId, String exceptionMessage) {
        final TIntStringValue exceptionInfo = new TIntStringValue(exceptionClassId);
        if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
            exceptionInfo.setStringValue(exceptionMessage);
        }
        super.setExceptionInfo(exceptionInfo);
    }


    public void markStartTime() {
        final int startElapsed = (int)(System.currentTimeMillis() - span.getStartTime());
        this.setStartElapsed(startElapsed);
    }

    public Long getStartTime() {
        this.startTime = span.getStartTime() + getStartElapsed();
        return this.startTime;
    }

    public void markAfterTime() {
        final int endElapsed = (int)(System.currentTimeMillis() - getStartTime());
        this.setEndElapsed(endElapsed);
    }

    public Long getAfterTime() {
        this.afterTime = span.getStartTime() + getStartElapsed() + getEndElapsed();
        return this.afterTime;
    }

    public int getStackId() {
        return stackId;
    }

    public void setStackId(int stackId) {
        this.stackId = stackId;
    }

    public boolean isTimeRecording() {
        return timeRecording;
    }

    public void setTimeRecording(boolean timeRecording) {
        this.timeRecording = timeRecording;
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
	public Span getSpan() {
		return this.span;
	}

	@Override
	public void addAnnotation(IAnnotation annotation) {
		this.addAnnotation((KVAnnotation) annotation);
	}
}
