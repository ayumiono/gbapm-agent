package com.gb.apm.profiler.monitor.metric.cpu;

import com.codahale.metrics.Gauge;
import com.gb.apm.profiler.monitor.codahale.MetricMonitorValues;

/**
 * @author HyunGil Jeong
 */
public interface CpuLoadMetric {

    Gauge<Double> UNSUPPORTED_GAUGE = new MetricMonitorValues.EmptyGauge<Double>(-1D);

    CpuLoadMetric UNSUPPORTED_CPU_LOAD_METRIC = new CpuLoadMetric() {
        @Override
        public Double jvmCpuLoad() {
            return null;
        }

        @Override
        public Double systemCpuLoad() {
            return null;
        }

        @Override
        public String toString() {
            return "Unsupported CpuLoadMetric";
        }
    };

    Double jvmCpuLoad();

    Double systemCpuLoad();
}
