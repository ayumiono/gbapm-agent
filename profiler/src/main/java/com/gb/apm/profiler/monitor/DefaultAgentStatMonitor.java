package com.gb.apm.profiler.monitor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.collector.DataSender;
import com.gb.apm.common.utils.PinpointThreadFactory;
import com.gb.apm.model.TAgentStat;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.gb.apm.profiler.context.module.StatDataSender;
import com.gb.apm.profiler.monitor.collector.AgentStatMetricCollector;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * AgentStat monitor
 *
 * @author harebox
 * @author hyungil.jeong
 */
public class DefaultAgentStatMonitor implements AgentStatMonitor {

    private static final long DEFAULT_COLLECTION_INTERVAL_MS = 1000 * 5;
    private static final int DEFAULT_NUM_COLLECTIONS_PER_SEND = 6;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final long collectionIntervalMs;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, new PinpointThreadFactory("Pinpoint-stat-monitor", true));

    private final CollectJob collectJob;

    @Inject
    public DefaultAgentStatMonitor(@StatDataSender DataSender dataSender,
                                   @AgentId String agentId, @AgentStartTime long agentStartTimestamp,
                                   @Named("AgentStatCollector") AgentStatMetricCollector<TAgentStat> agentStatCollector) {
        this(dataSender, agentId, agentStartTimestamp, agentStatCollector, DEFAULT_COLLECTION_INTERVAL_MS, DEFAULT_NUM_COLLECTIONS_PER_SEND);
    }

    public DefaultAgentStatMonitor(DataSender dataSender,
                                   String agentId, long agentStartTimestamp,
                                   AgentStatMetricCollector<TAgentStat> agentStatCollector,
                                   long collectionInterval, int numCollectionsPerBatch) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (agentStatCollector == null) {
            throw new NullPointerException("agentStatCollector must not be null");
        }
        this.collectionIntervalMs = collectionInterval;
        this.collectJob = new CollectJob(dataSender, agentId, agentStartTimestamp, agentStatCollector, numCollectionsPerBatch);

        preLoadClass(agentId, agentStartTimestamp, agentStatCollector);
    }

    // https://github.com/naver/pinpoint/issues/2881
    // #2881 AppClassLoader and PinpointUrlClassLoader Circular dependency deadlock
    // prevent deadlock for JDK6
    // Single thread execution is more safe than multi thread execution.
    // eg) executor.scheduleAtFixedRate(collectJob, 0(initialDelay is zero), this.collectionIntervalMs, TimeUnit.MILLISECONDS);
    private void preLoadClass(String agentId, long agentStartTimestamp, AgentStatMetricCollector<TAgentStat> agentStatCollector) {
        logger.debug("pre-load class start");
//        CollectJob collectJob = new CollectJob(EmptyDataSender.INSTANCE, agentId, agentStartTimestamp, agentStatCollector, 1);

        // It is called twice to initialize some fields.
        collectJob.run();
        collectJob.run();
        logger.debug("pre-load class end");
    }

    @Override
    public void start() {
        executor.scheduleAtFixedRate(collectJob, this.collectionIntervalMs, this.collectionIntervalMs, TimeUnit.MILLISECONDS);
        logger.info("AgentStat monitor started");
    }

    @Override
    public void stop() {
        executor.shutdown();
        try {
            executor.awaitTermination(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("AgentStat monitor stopped");
    }

}
