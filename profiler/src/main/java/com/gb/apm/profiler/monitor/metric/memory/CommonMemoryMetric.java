package com.gb.apm.profiler.monitor.metric.memory;

import java.util.Map;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.gb.apm.profiler.monitor.codahale.MetricMonitorValues;

/**
 * @author HyunGil Jeong
 */
public abstract class CommonMemoryMetric implements MemoryMetric {

    private final Gauge<Long> heapMaxGauge;
    private final Gauge<Long> heapUsedGauge;
    private final Gauge<Long> nonHeapMaxGauge;
    private final Gauge<Long> nonHeapUsedGauge;

    @SuppressWarnings("unchecked")
    protected CommonMemoryMetric(Map<String, Metric> memoryUsageMetrics) {
        if (memoryUsageMetrics == null) {
            throw new NullPointerException("memoryUsageMetrics must not be null");
        }
        heapMaxGauge = (Gauge<Long>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_HEAP_MAX, EMPTY_LONG_GAUGE);
        heapUsedGauge = (Gauge<Long>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_HEAP_USED, EMPTY_LONG_GAUGE);
        nonHeapMaxGauge = (Gauge<Long>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_NONHEAP_MAX, EMPTY_LONG_GAUGE);
        nonHeapUsedGauge = (Gauge<Long>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_NONHEAP_USED, EMPTY_LONG_GAUGE);
    }

    @Override
    public Long heapMax() {
        return heapMaxGauge.getValue();
    }

    @Override
    public Long heapUsed() {
        return heapUsedGauge.getValue();
    }

    @Override
    public Long nonHeapMax() {
        return nonHeapMaxGauge.getValue();
    }

    @Override
    public Long nonHeapUsed() {
        return nonHeapUsedGauge.getValue();
    }
}
