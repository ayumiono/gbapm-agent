package com.gb.apm.profiler.monitor.collector.activethread;

import com.gb.apm.model.TActiveTrace;
import com.gb.apm.profiler.monitor.metric.activethread.ActiveTraceMetric;

/**
 * @author HyunGil Jeong
 */
public class DefaultActiveTraceMetricCollector implements ActiveTraceMetricCollector {

    private final ActiveTraceMetric activeTraceMetric;

    public DefaultActiveTraceMetricCollector(ActiveTraceMetric activeTraceMetric) {
        if (activeTraceMetric == null) {
            throw new NullPointerException("activeTraceMetric must not be null");
        }
        this.activeTraceMetric = activeTraceMetric;
    }

    @Override
    public TActiveTrace collect() {
        TActiveTrace activeTrace = new TActiveTrace();
        activeTrace.setHistogram(activeTraceMetric.activeTraceHistogram());
        return activeTrace;
    }
}
