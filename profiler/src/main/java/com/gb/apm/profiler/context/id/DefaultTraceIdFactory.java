package com.gb.apm.profiler.context.id;

import com.gb.apm.common.TransactionId;
import com.gb.apm.common.utils.TransactionIdUtils;
import com.gb.apm.dapper.context.TraceId;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.google.inject.Inject;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultTraceIdFactory implements TraceIdFactory {

    private final String agentId;
    private final long agentStartTime;
    private final IdGenerator idGenerator;

    @Inject
    public DefaultTraceIdFactory(@AgentId String agentId, @AgentStartTime long agentStartTime, IdGenerator idGenerator) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (idGenerator == null) {
            throw new NullPointerException("idGenerator must not be null");
        }
        this.agentId = agentId;
        this.agentStartTime = agentStartTime;
        this.idGenerator = idGenerator;
    }

    @Override
    public TraceId newTraceId() {
        final long localTransactionId = idGenerator.nextTransactionId();
        final TraceId traceId = new DefaultTraceId(agentId, agentStartTime, localTransactionId);
        return traceId;
    }

    public TraceId parse(String transactionId, long parentSpanId, long spanId, short flags) {
        if (transactionId == null) {
            throw new NullPointerException("transactionId must not be null");
        }
        final TransactionId parseId = TransactionIdUtils.parseTransactionId(transactionId);
        return new DefaultTraceId(parseId.getAgentId(), parseId.getAgentStartTime(), parseId.getTransactionSequence(), parentSpanId, spanId, flags);
    }
}
