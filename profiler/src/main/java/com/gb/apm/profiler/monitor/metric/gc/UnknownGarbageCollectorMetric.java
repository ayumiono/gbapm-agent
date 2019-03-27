package com.gb.apm.profiler.monitor.metric.gc;

import com.gb.apm.model.TJvmGcType;

/**
 * Unknown garbage collector metrics
 *
 * @author HyunGil Jeong
 */
public class UnknownGarbageCollectorMetric implements GarbageCollectorMetric {

    private static final TJvmGcType GC_TYPE = TJvmGcType.UNKNOWN;

    @Override
    public TJvmGcType gcType() {
        return GC_TYPE;
    }

    @Override
    public Long gcOldCount() {
        return EMPTY_LONG_GAUGE.getValue();
    }

    @Override
    public Long gcOldTime() {
        return EMPTY_LONG_GAUGE.getValue();
    }

    @Override
    public Long gcNewCount() {
        return EMPTY_LONG_GAUGE.getValue();
    }

    @Override
    public Long gcNewTime() {
        return EMPTY_LONG_GAUGE.getValue();
    }

    @Override
    public String toString() {
        return "Unknown garbage collector metrics";
    }
}
