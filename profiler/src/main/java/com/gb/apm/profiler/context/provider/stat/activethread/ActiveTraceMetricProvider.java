package com.gb.apm.profiler.context.provider.stat.activethread;

import com.gb.apm.profiler.context.active.ActiveTraceHistogramFactory;
import com.gb.apm.profiler.context.active.ActiveTraceRepository;
import com.gb.apm.profiler.monitor.metric.activethread.ActiveTraceMetric;
import com.gb.apm.profiler.monitor.metric.activethread.DefaultActiveTraceMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author HyunGil Jeong
 */
public class ActiveTraceMetricProvider implements Provider<ActiveTraceMetric> {

    private final ActiveTraceRepository activeTraceRepository;

    @Inject
    public ActiveTraceMetricProvider(Provider<ActiveTraceRepository> activeTraceRepositoryProvider) {
        if (activeTraceRepositoryProvider == null) {
            throw new NullPointerException("activeTraceRepositoryProvider must not be null");
        }
        this.activeTraceRepository = activeTraceRepositoryProvider.get();
    }

    @Override
    public ActiveTraceMetric get() {
        if (activeTraceRepository == null) {
            return ActiveTraceMetric.UNSUPPORTED_ACTIVE_TRACE_METRIC;
        } else {
            ActiveTraceHistogramFactory activeTraceHistogramFactory = new ActiveTraceHistogramFactory(activeTraceRepository);
            return new DefaultActiveTraceMetric(activeTraceHistogramFactory);
        }
    }
}
