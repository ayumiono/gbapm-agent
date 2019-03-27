package com.gb.apm.profiler.monitor.metric.activethread;

import com.gb.apm.model.TActiveTraceHistogram;

/**
 * @author HyunGil Jeong
 */
public interface ActiveTraceMetric {

    ActiveTraceMetric UNSUPPORTED_ACTIVE_TRACE_METRIC = new ActiveTraceMetric() {
        @Override
        public TActiveTraceHistogram activeTraceHistogram() {
            return null;
        }

        @Override
        public String toString() {
            return "Unsupported ActiveTraceMetric";
        }
    };

    TActiveTraceHistogram activeTraceHistogram();
}
