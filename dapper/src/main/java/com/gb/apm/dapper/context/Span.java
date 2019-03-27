package com.gb.apm.dapper.context;

import java.util.List;

/**
 * 
 * 暂实只有这两个接口，具体后面再看 TODO
 * 
 * @author xuelong.chen
 *
 */
public interface Span extends FrameAttachment {
	
	/**
	 * 记录spanId,traceId(transactionId),parent spanId
	 * @param traceId
	 */
	public void recordTraceId(TraceId traceId);

	/**
	 * opentracing tag
	 * @param annotation
	 */
	public void addAnnotation(IAnnotation annotation);
	
	public <T> T findAnnotation(int key);

	public void setAgentId(String agentId);
	
	public String getTransactionId();
	
	public String getAgentId();

	public void setApplicationName(String applicationName);

	public void setAgentStartTime(long agentStartTime);

	public void setApplicationServiceType(Short serviceType);

	public void markBeforeTime();

	public long getSpanId();

	public Long getParentSpanId();

	public void setStartTime(long startTime);

	public long getStartTime();

	public void setElapsed(Integer elapsed);

	public String getRpc();

	public void setRpc(String rpc);

	public void setExceptionInfo(String exceptionInfo);

	public void setExceptionInfo(int exceptionId, String exceptionInfo);

	public boolean isErr();

	public void setErrCode(int err);

	public void setApiId(Integer apiId);

	public void setServiceType(Short serviceType);

	//dubbo
	public void setEndPoint(String endPoint);
	
	public void setRemoteAddr(String remoteAddr);

	public void setParentApplicationName(String parentApplicationName);

	public void setParentApplicationType(Short parentApplicationType);

	public void setAcceptorHost(String acceptorHost);

	public void markAfterTime();//记录结束时间

	public boolean isTimeRecording();//是否需要记录时间
	
	public void setSpanEventList(List<_SpanEvent> spanEvent);

	public interface _SpanEvent {
		
		public void addAnnotation(IAnnotation annotation);
		
		public <T> T findAnnotation(int key);

		public Span getSpan();

		public Long getSpanId();

		public Short getSequence();

		public Short getParentSequence();

		public Integer getStartElapsed();

		public Integer getEndElapsed();

		public String getRpc();

		public Short getServiceType();

		public String getEndPoint();

		public Integer getDepth();

		public Long getNextSpanId();

		public String getDestinationId();

		public Integer getApiId();

		public Integer getAsyncId();

		public Integer getNextAsyncId();

		public Short getAsyncSequence();
	}
}
