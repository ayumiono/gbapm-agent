package com.gb.apm.profiler.monitor.collector;

import com.gb.apm.model.TAgentStat;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.gb.apm.profiler.monitor.collector.activethread.ActiveTraceMetricCollector;
import com.gb.apm.profiler.monitor.collector.cpu.CpuLoadMetricCollector;
import com.gb.apm.profiler.monitor.collector.datasource.DataSourceMetricCollector;
import com.gb.apm.profiler.monitor.collector.jvmgc.JvmGcMetricCollector;
import com.gb.apm.profiler.monitor.collector.transaction.TransactionMetricCollector;
import com.google.inject.Inject;

/**
 * @author HyunGil Jeong
 */
public class AgentStatCollector implements AgentStatMetricCollector<TAgentStat> {

    private final String agentId;
    private final long agentStartTimestamp;
    private final JvmGcMetricCollector jvmGcMetricCollector;
//    private final CpuLoadMetricCollector cpuLoadMetricCollector;
    private final TransactionMetricCollector transactionMetricCollector;
    private final ActiveTraceMetricCollector activeTraceMetricCollector;
    private final DataSourceMetricCollector dataSourceMetricCollector;

    @Inject
    public AgentStatCollector(
            @AgentId String agentId,
            @AgentStartTime long agentStartTimestamp,
            JvmGcMetricCollector jvmGcMetricCollector,
//            CpuLoadMetricCollector cpuLoadMetricCollector,
            TransactionMetricCollector transactionMetricCollector,
            ActiveTraceMetricCollector activeTraceMetricCollector,
            DataSourceMetricCollector dataSourceMetricCollector) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (jvmGcMetricCollector == null) {
            throw new NullPointerException("jvmGcMetricCollector must not be null");
        }
//        if (cpuLoadMetricCollector == null) {
//            throw new NullPointerException("cpuLoadMetricCollector must not be null");
//        }
        if (transactionMetricCollector == null) {
            throw new NullPointerException("transactionMetricCollector must not be null");
        }
        if (activeTraceMetricCollector == null) {
            throw new NullPointerException("activeTraceMetricCollector must not be null");
        }
        if (dataSourceMetricCollector == null) {
            throw new NullPointerException("dataSourceMetricCollector must not be null");
        }
        this.agentId = agentId;
        this.agentStartTimestamp = agentStartTimestamp;
        this.jvmGcMetricCollector = jvmGcMetricCollector;
//        this.cpuLoadMetricCollector = cpuLoadMetricCollector;
        this.transactionMetricCollector = transactionMetricCollector;
        this.activeTraceMetricCollector = activeTraceMetricCollector;
        this.dataSourceMetricCollector = dataSourceMetricCollector;
    }

    @Override
    public TAgentStat collect() {
        TAgentStat agentStat = new TAgentStat();
        agentStat.setAgentId(agentId);
        agentStat.setStartTimestamp(agentStartTimestamp);
        agentStat.setGc(jvmGcMetricCollector.collect());
//        agentStat.setCpuLoad(cpuLoadMetricCollector.collect());
        agentStat.setTransaction(transactionMetricCollector.collect());
        agentStat.setActiveTrace(activeTraceMetricCollector.collect());
        agentStat.setDataSourceList(dataSourceMetricCollector.collect());
        return agentStat;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AgentStatCollector{");
        sb.append("agentId='").append(agentId).append('\'');
        sb.append(", agentStartTimestamp=").append(agentStartTimestamp);
        sb.append(", jvmGcMetricCollector=").append(jvmGcMetricCollector);
//        sb.append(", cpuLoadMetricCollector=").append(cpuLoadMetricCollector);
        sb.append(", transactionMetricCollector=").append(transactionMetricCollector);
        sb.append(", activeTraceMetricCollector=").append(activeTraceMetricCollector);
        sb.append(", dataSourceMetricCollector=").append(dataSourceMetricCollector);
        sb.append('}');
        return sb.toString();
    }
}
