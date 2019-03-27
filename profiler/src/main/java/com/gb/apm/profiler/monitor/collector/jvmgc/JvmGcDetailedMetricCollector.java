package com.gb.apm.profiler.monitor.collector.jvmgc;

import com.gb.apm.model.TJvmGc;
import com.gb.apm.model.TJvmGcDetailed;
import com.gb.apm.profiler.monitor.metric.gc.GarbageCollectorMetric;
import com.gb.apm.profiler.monitor.metric.memory.MemoryMetric;

/**
 * @author dawidmalina
 * @author HyunGil Jeong
 */
public class JvmGcDetailedMetricCollector implements JvmGcMetricCollector {

    private final JvmGcCommonMetricCollector jvmGcCommonMetricCollector;
    private final MemoryMetric memoryMetric;
    private final GarbageCollectorMetric garbageCollectorMetric;

    public JvmGcDetailedMetricCollector(JvmGcCommonMetricCollector jvmGcCommonMetricCollector, MemoryMetric memoryMetric, GarbageCollectorMetric garbageCollectorMetric) {
        this.jvmGcCommonMetricCollector = jvmGcCommonMetricCollector;
        this.memoryMetric = memoryMetric;
        this.garbageCollectorMetric = garbageCollectorMetric;
    }

    @Override
    public TJvmGc collect() {
        TJvmGcDetailed jvmGcDetailed = new TJvmGcDetailed();
        jvmGcDetailed.setJvmPoolNewGenUsed(memoryMetric.newGenUsage());
        jvmGcDetailed.setJvmPoolOldGenUsed(memoryMetric.oldGenUsage());
        jvmGcDetailed.setJvmPoolCodeCacheUsed(memoryMetric.codeCacheUsage());
        jvmGcDetailed.setJvmPoolSurvivorSpaceUsed(memoryMetric.survivorUsage());
        Double permGenUsed = memoryMetric.permGenUsage();
        if (permGenUsed != null) {
            // metric for jvm < 1.8
            jvmGcDetailed.setJvmPoolPermGenUsed(memoryMetric.permGenUsage());
        } else {
            // metric for jvm >= 1.8
            jvmGcDetailed.setJvmPoolMetaspaceUsed(memoryMetric.metaspaceUsage());
        }
        jvmGcDetailed.setJvmGcNewCount(garbageCollectorMetric.gcNewCount());
        jvmGcDetailed.setJvmGcNewTime(garbageCollectorMetric.gcNewTime());

        TJvmGc jvmGc = jvmGcCommonMetricCollector.collect();
        jvmGc.setJvmGcDetailed(jvmGcDetailed);
        return jvmGc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JvmGcDetailedMetricCollector{");
        sb.append("memoryMetric=").append(memoryMetric);
        sb.append(", garbageCollectorMetric=").append(garbageCollectorMetric);
        sb.append('}');
        return sb.toString();
    }
}
