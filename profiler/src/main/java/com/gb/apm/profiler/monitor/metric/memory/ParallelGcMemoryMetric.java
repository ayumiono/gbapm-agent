package com.gb.apm.profiler.monitor.metric.memory;

import java.util.Map;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.gb.apm.profiler.monitor.codahale.MetricMonitorValues;

/**
 * HotSpot's Parallel (Old) gc memory metrics
 *
 * @author dawidmalina
 * @author HyunGil Jeong
 */
public class ParallelGcMemoryMetric extends CommonMemoryMetric {

    private final Gauge<Double> newGenUsageGauge;
    private final Gauge<Double> oldGenUsageGauge;
    private final Gauge<Double> codeCacheUsageGauge;
    private final Gauge<Double> survivorUsageGauge;
    private final Gauge<Double> permGenUsageGauge;
    private final Gauge<Double> metaspaceUsageGauge;

    @SuppressWarnings("unchecked")
    public ParallelGcMemoryMetric(Map<String, Metric> memoryUsageMetrics) {
        super(memoryUsageMetrics);
        newGenUsageGauge = (Gauge<Double>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_POOLS_PS_NEWGEN_USAGE, EMPTY_DOUBLE_GAUGE);
        oldGenUsageGauge = (Gauge<Double>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_POOLS_PS_OLDGEN_USAGE, EMPTY_DOUBLE_GAUGE);
        codeCacheUsageGauge = (Gauge<Double>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_POOLS_PS_CODE_CACHE_USAGE, EMPTY_DOUBLE_GAUGE);
        survivorUsageGauge = (Gauge<Double>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_POOLS_PS_SURVIVOR_USAGE, EMPTY_DOUBLE_GAUGE);
        if (memoryUsageMetrics.containsKey(MetricMonitorValues.METRIC_MEMORY_POOLS_PS_PERMGEN_USAGE)) {
            permGenUsageGauge = (Gauge<Double>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_POOLS_PS_PERMGEN_USAGE, EMPTY_DOUBLE_GAUGE);
            metaspaceUsageGauge = MetricMonitorValues.EXCLUDED_DOUBLE;
        } else {
            permGenUsageGauge = MetricMonitorValues.EXCLUDED_DOUBLE;
            metaspaceUsageGauge = (Gauge<Double>) MetricMonitorValues.getMetric(memoryUsageMetrics, MetricMonitorValues.METRIC_MEMORY_POOLS_PS_METASPACE_USAGE, EMPTY_DOUBLE_GAUGE);
        }
    }

    @Override
    public Double newGenUsage() {
        return newGenUsageGauge.getValue();
    }

    @Override
    public Double oldGenUsage() {
        return oldGenUsageGauge.getValue();
    }

    @Override
    public Double codeCacheUsage() {
        return codeCacheUsageGauge.getValue();
    }

    @Override
    public Double survivorUsage() {
        return survivorUsageGauge.getValue();
    }

    @Override
    public Double permGenUsage() {
        return permGenUsageGauge.getValue();
    }

    @Override
    public Double metaspaceUsage() {
        return metaspaceUsageGauge.getValue();
    }

    @Override
    public String toString() {
        return "HotSpot's Parallel (Old) gc memory metrics";
    }
}
