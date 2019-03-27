package com.gb.apm.profiler.monitor.metric.memory;

import com.codahale.metrics.Gauge;
import com.gb.apm.profiler.monitor.codahale.MetricMonitorValues;

/**
 * @author dawidmalina
 * @author HyunGil Jeong
 */
public interface MemoryMetric {

    Gauge<Long> EMPTY_LONG_GAUGE = new MetricMonitorValues.EmptyGauge<Long>(-1L);
    Gauge<Double> EMPTY_DOUBLE_GAUGE = new MetricMonitorValues.EmptyGauge<Double>(-1D);

    Long heapMax();

    Long heapUsed();

    Long nonHeapMax();

    Long nonHeapUsed();

    Double newGenUsage();

    Double oldGenUsage();

    Double codeCacheUsage();

    Double survivorUsage();

    Double permGenUsage();

    Double metaspaceUsage();
}
