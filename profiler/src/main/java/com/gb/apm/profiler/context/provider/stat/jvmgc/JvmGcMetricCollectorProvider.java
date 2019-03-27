package com.gb.apm.profiler.context.provider.stat.jvmgc;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.profiler.monitor.collector.jvmgc.JvmGcCommonMetricCollector;
import com.gb.apm.profiler.monitor.collector.jvmgc.JvmGcDetailedMetricCollector;
import com.gb.apm.profiler.monitor.collector.jvmgc.JvmGcMetricCollector;
import com.gb.apm.profiler.monitor.metric.gc.GarbageCollectorMetric;
import com.gb.apm.profiler.monitor.metric.memory.MemoryMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;


/**
 * @author HyunGil Jeong
 */
public class JvmGcMetricCollectorProvider implements Provider<JvmGcMetricCollector> {

    private final boolean collectDetailedMetrics;
    private final MemoryMetric memoryMetric;
    private final GarbageCollectorMetric garbageCollectorMetric;

    @Inject
    public JvmGcMetricCollectorProvider(ProfilerConfig profilerConfig, MemoryMetric memoryMetric, GarbageCollectorMetric garbageCollectorMetric) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (memoryMetric == null) {
            throw new NullPointerException("memoryMetric must not be null");
        }
        if (garbageCollectorMetric == null) {
            throw new NullPointerException("garbageCollectorMetric must not be null");
        }
        this.collectDetailedMetrics = profilerConfig.isProfilerJvmCollectDetailedMetrics();
        this.memoryMetric = memoryMetric;
        this.garbageCollectorMetric = garbageCollectorMetric;
    }

    @Override
    public JvmGcMetricCollector get() {
        JvmGcMetricCollector jvmGcMetricCollector;
        JvmGcCommonMetricCollector jvmGcCommonMetricCollector = new JvmGcCommonMetricCollector(memoryMetric, garbageCollectorMetric);
        if (collectDetailedMetrics) {
            jvmGcMetricCollector = new JvmGcDetailedMetricCollector(jvmGcCommonMetricCollector, memoryMetric, garbageCollectorMetric);
        } else {
            jvmGcMetricCollector = jvmGcCommonMetricCollector;
        }
        return jvmGcMetricCollector;
    }
}
