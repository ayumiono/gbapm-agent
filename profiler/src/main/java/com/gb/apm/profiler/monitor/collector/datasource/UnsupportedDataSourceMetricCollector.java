package com.gb.apm.profiler.monitor.collector.datasource;

import com.gb.apm.model.TDataSourceList;

/**
 * @author HyunGil Jeong
 */
public class UnsupportedDataSourceMetricCollector implements DataSourceMetricCollector {

    @Override
    public TDataSourceList collect() {
        return null;
    }

    @Override
    public String toString() {
        return "Unsupported DataSourceMetricCollector";
    }
}
