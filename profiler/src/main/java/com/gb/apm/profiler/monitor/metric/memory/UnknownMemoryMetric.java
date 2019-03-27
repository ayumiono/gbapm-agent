package com.gb.apm.profiler.monitor.metric.memory;

import com.codahale.metrics.Metric;

import java.util.Map;

/**
 * Unknown memory metrics
 *
 * @author HyunGil Jeong
 */
public class UnknownMemoryMetric extends CommonMemoryMetric {

    public UnknownMemoryMetric(Map<String, Metric> memoryUsageMetrics) {
        super(memoryUsageMetrics);
    }

    @Override
    public Double newGenUsage() {
        return EMPTY_DOUBLE_GAUGE.getValue();
    }

    @Override
    public Double oldGenUsage() {
        return EMPTY_DOUBLE_GAUGE.getValue();
    }

    @Override
    public Double codeCacheUsage() {
        return EMPTY_DOUBLE_GAUGE.getValue();
    }

    @Override
    public Double survivorUsage() {
        return EMPTY_DOUBLE_GAUGE.getValue();
    }

    @Override
    public Double permGenUsage() {
        return EMPTY_DOUBLE_GAUGE.getValue();
    }

    @Override
    public Double metaspaceUsage() {
        return EMPTY_DOUBLE_GAUGE.getValue();
    }

    @Override
    public String toString() {
        return "Unknown memory metrics";
    }
}
