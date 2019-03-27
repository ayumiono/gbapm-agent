package com.gb.apm.profiler.monitor.collector.datasource;

import com.gb.apm.model.TDataSourceList;
import com.gb.apm.profiler.monitor.metric.datasource.DataSourceMetric;

/**
 * @author HyunGil Jeong
 */
public class DefaultDataSourceMetricCollector implements DataSourceMetricCollector {

    private final DataSourceMetric dataSourceMetric;

    public DefaultDataSourceMetricCollector(DataSourceMetric dataSourceMetric) {
        if (dataSourceMetric == null) {
            throw new NullPointerException("dataSourceMetric must not be null");
        }
        this.dataSourceMetric = dataSourceMetric;
    }

    @Override
    public TDataSourceList collect() {
        return dataSourceMetric.dataSourceList();
    }
}
