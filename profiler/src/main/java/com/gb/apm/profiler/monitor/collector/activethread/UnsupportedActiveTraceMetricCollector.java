package com.gb.apm.profiler.monitor.collector.activethread;

import com.gb.apm.model.TActiveTrace;

/**
 * @author HyunGil Jeong
 */
public class UnsupportedActiveTraceMetricCollector implements ActiveTraceMetricCollector {

    @Override
    public TActiveTrace collect() {
        return null;
    }

    @Override
    public String toString() {
        return "Unsupported CpuLoadMetricCollector";
    }
}
