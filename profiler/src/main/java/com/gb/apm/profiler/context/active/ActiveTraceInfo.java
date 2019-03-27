package com.gb.apm.profiler.context.active;

/**
 * @author Taejin Koo
 */
public class ActiveTraceInfo {

    private final long localTraceId;
    private final long startTime;
    private final Thread thread;
    private final boolean sampled;
    private final String transactionId;
    private final String entryPoint;

    public ActiveTraceInfo(long id, long startTime) {
        this(id, startTime, null);
    }

    public ActiveTraceInfo(long id, long startTime, Thread thread) {
        this(id, startTime, thread, false, null, null);
    }

    public ActiveTraceInfo(long id, long startTime, Thread thread, boolean sampled, String transactionId, String entryPoint) {
        this.localTraceId = id;
        this.startTime = startTime;
        this.thread = thread;

        this.sampled = sampled;
        this.transactionId = transactionId;
        this.entryPoint = entryPoint;
    }

    public long getLocalTraceId() {
        return localTraceId;
    }

    public long getStartTime() {
        return startTime;
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isSampled() {
        return sampled;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActiveTraceInfo{");
        sb.append("localTraceId=").append(localTraceId);
        sb.append(", startTime=").append(startTime);
        sb.append(", thread=").append(thread);
        sb.append(", sampled=").append(sampled);
        sb.append(", transactionId='").append(transactionId).append('\'');
        sb.append(", entryPoint='").append(entryPoint).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
