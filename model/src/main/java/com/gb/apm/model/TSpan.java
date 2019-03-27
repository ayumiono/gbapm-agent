package com.gb.apm.model;

import java.util.ArrayList;
import java.util.List;

public class TSpan extends APMModel {

	private String agentId; // required
	private String applicationName; // required
	private long agentStartTime; // required
	private String transactionId; // required
	private long spanId; // required
	private Long parentSpanId; // optional
	private long startTime; // required
	private int elapsed; // optional
	private String rpc; // optional
	private short serviceType; // required
	private String endPoint; // optional
	private String remoteAddr; // optional
	private List<Annotation> annotations; // optional
	private Integer err; // optional
	private List<TSpanEvent> tspanEventList; // optional
	private String parentApplicationName; // optional
	private Short parentApplicationType; // optional
	private String acceptorHost; // optional
	private Integer apiId; // optional
	private TIntStringValue exceptionInfo; // optional
	private Short applicationServiceType; // optional

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public long getAgentStartTime() {
		return agentStartTime;
	}

	public void setAgentStartTime(long agentStartTime) {
		this.agentStartTime = agentStartTime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public long getSpanId() {
		return spanId;
	}

	public void setSpanId(long spanId) {
		this.spanId = spanId;
	}

	public Long getParentSpanId() {
		return parentSpanId;
	}

	public void setParentSpanId(Long parentSpanId) {
		this.parentSpanId = parentSpanId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public Integer getElapsed() {
		return elapsed;
	}

	public void setElapsed(Integer elapsed) {
		this.elapsed = elapsed;
	}

	public String getRpc() {
		return rpc;
	}

	public void setRpc(String rpc) {
		this.rpc = rpc;
	}

	public short getServiceType() {
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

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public void addAnnotation(Annotation annotations) {
		if (this.annotations == null) {
			this.annotations = new ArrayList<>();
		}
		this.annotations.add(annotations);
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
	
	public Annotation annotationFor(int annotationKey) {
		for (Annotation annotation : this.annotations) {
			if (String.valueOf(annotation.getKey()).equals(String.valueOf(annotationKey))) {
				return annotation;
			}
		}
		return null;
	}

	public Integer getErr() {
		return err;
	}

	public void setErr(Integer err) {
		this.err = err;
	}

	public String getParentApplicationName() {
		return parentApplicationName;
	}

	public void setParentApplicationName(String parentApplicationName) {
		this.parentApplicationName = parentApplicationName;
	}

	public Short getParentApplicationType() {
		return parentApplicationType;
	}

	public void setParentApplicationType(Short parentApplicationType) {
		this.parentApplicationType = parentApplicationType;
	}

	public String getAcceptorHost() {
		return acceptorHost;
	}

	public void setAcceptorHost(String acceptorHost) {
		this.acceptorHost = acceptorHost;
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

	public Short getApplicationServiceType() {
		return applicationServiceType;
	}

	public void setApplicationServiceType(Short applicationServiceType) {
		this.applicationServiceType = applicationServiceType;
	}

	public List<TSpanEvent> getTspanEventList() {
		return tspanEventList;
	}

	public void setTspanEventList(List<TSpanEvent> tspanEventList) {
		this.tspanEventList = tspanEventList;
	}
}
