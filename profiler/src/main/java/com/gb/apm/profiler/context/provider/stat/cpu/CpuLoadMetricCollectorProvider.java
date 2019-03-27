package com.gb.apm.profiler.context.provider.stat.cpu;

import com.gb.apm.profiler.monitor.collector.cpu.CpuLoadMetricCollector;
import com.gb.apm.profiler.monitor.collector.cpu.DefaultCpuLoadMetricCollector;
import com.gb.apm.profiler.monitor.collector.cpu.UnsupportedCpuLoadMetricCollector;
import com.gb.apm.profiler.monitor.metric.cpu.CpuLoadMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author HyunGil Jeong
 */
public class CpuLoadMetricCollectorProvider implements Provider<CpuLoadMetricCollector> {

    private final CpuLoadMetric cpuLoadMetric;

    @Inject
    public CpuLoadMetricCollectorProvider(CpuLoadMetric cpuLoadMetric) {
        if (cpuLoadMetric == null) {
            throw new NullPointerException("cpuLoadMetric must not be null");
        }
        this.cpuLoadMetric = cpuLoadMetric;
    }

    @Override
    public CpuLoadMetricCollector get() {
        if (cpuLoadMetric == CpuLoadMetric.UNSUPPORTED_CPU_LOAD_METRIC) {
            return new UnsupportedCpuLoadMetricCollector();
        }
        return new DefaultCpuLoadMetricCollector(cpuLoadMetric);
    }
}
