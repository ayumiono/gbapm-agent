package com.gb.apm.profiler.context.provider.stat.datasource;

import com.gb.apm.profiler.monitor.collector.datasource.DataSourceMetricCollector;
import com.gb.apm.profiler.monitor.collector.datasource.DefaultDataSourceMetricCollector;
import com.gb.apm.profiler.monitor.collector.datasource.UnsupportedDataSourceMetricCollector;
import com.gb.apm.profiler.monitor.metric.datasource.DataSourceMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Taejin Koo
 * @author HyunGil Jeong
 */
public class DataSourceMetricCollectorProvider implements Provider<DataSourceMetricCollector> {

    private final DataSourceMetric dataSourceMetric;

    @Inject
    public DataSourceMetricCollectorProvider(DataSourceMetric dataSourceMetric) {
        if (dataSourceMetric == null) {
            throw new NullPointerException("dataSourceMetric must not be null");
        }
        this.dataSourceMetric = dataSourceMetric;
    }

    @Override
    public DataSourceMetricCollector get() {
        if (dataSourceMetric == DataSourceMetric.UNSUPPORTED_DATA_SOURCE_METRIC) {
            return new UnsupportedDataSourceMetricCollector();
        }
        return new DefaultDataSourceMetricCollector(dataSourceMetric);
    }
}
