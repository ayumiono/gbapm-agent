package com.gb.apm.model;

import java.util.ArrayList;
import java.util.List;

public class TSpanEvent extends APMModel {
	
	private String agentId; //required
	protected Short sequence; // required
	protected Short parentSequence;// required
	protected Integer startElapsed; // required
	protected Integer endElapsed; // required
	protected Short serviceType; // required
	protected String transactionId;//required
	protected Long startTime;//required
	protected Long afterTime;//required
	
	protected transient Long spanId; // optional
	
	protected String rpc; // optional
	protected String endPoint; // optional
	protected String destinationId; // optional
	protected List<Annotation> annotations; // optional
	protected Integer depth; // optional
	protected Long nextSpanId; // optional
	protected Integer apiId; // optional
	protected TIntStringValue exceptionInfo; // optional
	protected Integer asyncId; // optional
	protected Integer nextAsyncId; // optional
	protected Short asyncSequence; // optional

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Long getSpanId() {
		return spanId;
	}

	public void setSpanId(Long spanId) {
		this.spanId = spanId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Short getSequence() {
		return sequence;
	}

	public void setSequence(Short sequence) {
		this.sequence = sequence;
	}

	public Short getParentSequence() {
		return parentSequence;
	}

	public void setParentSequence(Short parentSequence) {
		this.parentSequence = parentSequence;
	}

	public Integer getStartElapsed() {
		return startElapsed;
	}

	public void setStartElapsed(Integer startElapsed) {
		this.startElapsed = startElapsed;
	}

	public Integer getEndElapsed() {
		return endElapsed;
	}

	public void setEndElapsed(Integer endElapsed) {
		this.endElapsed = endElapsed;
	}

	public String getRpc() {
		return rpc;
	}

	public void setRpc(String rpc) {
		this.rpc = rpc;
	}

	public Short getServiceType() {
		return serviceType;
	}

	public void setServiceType(Short serviceType) {
		this.serviceType = serviceType;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Long getNextSpanId() {
		return nextSpanId;
	}

	public void setNextSpanId(Long nextSpanId) {
		this.nextSpanId = nextSpanId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	public TIntStringValue getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(TIntStringValue exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	public Integer getAsyncId() {
		return asyncId;
	}

	public void setAsyncId(Integer asyncId) {
		this.asyncId = asyncId;
	}

	public Integer getNextAsyncId() {
		return nextAsyncId;
	}

	public void setNextAsyncId(Integer nextAsyncId) {
		this.nextAsyncId = nextAsyncId;
	}

	public Short getAsyncSequence() {
		return asyncSequence;
	}

	public void setAsyncSequence(Short asyncSequence) {
		this.asyncSequence = asyncSequence;
	}

	public Long getAfterTime() {
		return afterTime;
	}

	public void setAfterTime(Long afterTime) {
		this.afterTime = afterTime;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findAnnotation(int annotationKey) {
		for (Annotation annotation : this.annotations) {
			if (String.valueOf(annotation.getKey()).equals(String.valueOf(annotationKey))) {
				return (T) annotation.getValue();
			}
		}
		return null;
	}
	
	public void addToAnnotations(Annotation elem) {
		if (this.annotations == null) {
			this.annotations = new ArrayList<Annotation>();
		}
		this.annotations.add(elem);
	}
}
