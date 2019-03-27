package com.gb.apm.profiler.monitor.metric.gc;

import com.codahale.metrics.Gauge;
import com.gb.apm.model.TJvmGcType;
import com.gb.apm.profiler.monitor.codahale.MetricMonitorValues;

/**
 * @author HyunGil Jeong
 */
public interface GarbageCollectorMetric {

    Gauge<Long> EMPTY_LONG_GAUGE = new MetricMonitorValues.EmptyGauge<Long>(-1L);

    TJvmGcType gcType();

    Long gcOldCount();

    Long gcOldTime();

    Long gcNewCount();

    Long gcNewTime();
}
