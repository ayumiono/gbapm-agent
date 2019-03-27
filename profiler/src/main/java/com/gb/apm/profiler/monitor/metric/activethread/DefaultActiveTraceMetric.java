package com.gb.apm.profiler.monitor.metric.activethread;

import java.util.List;

import com.gb.apm.model.TActiveTraceHistogram;
import com.gb.apm.profiler.context.active.ActiveTraceHistogramFactory;
import com.gb.apm.profiler.context.active.ActiveTraceHistogramFactory.ActiveTraceHistogram;

/**
 * @author HyunGil Jeong
 */
public class DefaultActiveTraceMetric implements ActiveTraceMetric {

    private final ActiveTraceHistogramFactory activeTraceHistogramFactory;

    public DefaultActiveTraceMetric(ActiveTraceHistogramFactory activeTraceHistogramFactory) {
        if (activeTraceHistogramFactory == null) {
            throw new NullPointerException("activeTraceHistogramFactory must not be null");
        }
        this.activeTraceHistogramFactory = activeTraceHistogramFactory;
    }

    @Override
    public TActiveTraceHistogram activeTraceHistogram() {
        ActiveTraceHistogram activeTraceHistogram = activeTraceHistogramFactory.createHistogram();
        int histogramSchemaTypeCode = activeTraceHistogram.getHistogramSchema().getTypeCode();
        List<Integer> activeTraceCounts = activeTraceHistogram.getActiveTraceCounts();

        TActiveTraceHistogram tActiveTraceHistogram = new TActiveTraceHistogram();
        tActiveTraceHistogram.setHistogramSchemaType(histogramSchemaTypeCode);
        tActiveTraceHistogram.setActiveTraceCount(activeTraceCounts);
        return tActiveTraceHistogram;
    }

    @Override
    public String toString() {
        return "Default ActiveTraceCountMetric";
    }
}
