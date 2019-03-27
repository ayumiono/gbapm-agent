package com.gb.apm.profiler.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.collector.DataSender;
import com.gb.apm.model.TAgentStat;
import com.gb.apm.profiler.monitor.collector.AgentStatMetricCollector;

/**
 * @author Woonduk Kang(emeroad)
 */
public class CollectJob implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSender dataSender;
    private final String agentId;
    private final long agentStartTimestamp;
    private final AgentStatMetricCollector<TAgentStat> agentStatCollector;

    private long prevCollectionTimestamp = System.currentTimeMillis();

    public CollectJob(DataSender dataSender,
                       String agentId, long agentStartTimestamp,
                       AgentStatMetricCollector<TAgentStat> agentStatCollector,
                       int numCollectionsPerBatch) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        this.dataSender = dataSender;
        this.agentId = agentId;
        this.agentStartTimestamp = agentStartTimestamp;
        this.agentStatCollector = agentStatCollector;
    }

    @Override
    public void run() {
        final long currentCollectionTimestamp = System.currentTimeMillis();
        final long collectInterval = currentCollectionTimestamp - this.prevCollectionTimestamp;
        try {
            final TAgentStat agentStat = agentStatCollector.collect();
            agentStat.setTimestamp(currentCollectionTimestamp);
            agentStat.setCollectInterval(collectInterval);
            sendAgentStats(agentStat);
        } catch (Exception ex) {
            logger.warn("AgentStat collect failed. Caused:{}", ex.getMessage(), ex);
        } finally {
            this.prevCollectionTimestamp = currentCollectionTimestamp;
        }
    }

    private void sendAgentStats(TAgentStat agentStat) {
        logger.trace("collect agentStat:{}", agentStat);
        dataSender.send(agentStat);
    }
}
