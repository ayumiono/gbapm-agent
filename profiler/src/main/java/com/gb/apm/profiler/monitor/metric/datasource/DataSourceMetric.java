package com.gb.apm.profiler.monitor.metric.datasource;

import com.gb.apm.model.TDataSourceList;

/**
 * @author Taejin Koo
 * @author HyunGil Jeong
 */
public interface DataSourceMetric {

    DataSourceMetric UNSUPPORTED_DATA_SOURCE_METRIC = new DataSourceMetric() {
        @Override
        public TDataSourceList dataSourceList() {
            return null;
        }

        @Override
        public String toString() {
            return "Unsupported DataSourceMetric";
        }
    };

    TDataSourceList dataSourceList();
}
