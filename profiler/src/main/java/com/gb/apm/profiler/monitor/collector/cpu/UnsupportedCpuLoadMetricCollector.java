package com.gb.apm.profiler.monitor.collector.cpu;

import com.gb.apm.model.TCpuLoad;

/**
 * @author HyunGil Jeong
 */
public class UnsupportedCpuLoadMetricCollector implements CpuLoadMetricCollector {

    @Override
    public TCpuLoad collect() {
        return null;
    }

    @Override
    public String toString() {
        return "UnsupportedCpuLoadMetricCollector";
    }
}
