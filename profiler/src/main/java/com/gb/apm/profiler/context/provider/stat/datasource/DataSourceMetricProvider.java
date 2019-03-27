package com.gb.apm.profiler.context.provider.stat.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryService;
import com.gb.apm.profiler.context.monitor.JdbcUrlParsingService;
import com.gb.apm.profiler.monitor.metric.datasource.DataSourceMetric;
import com.gb.apm.profiler.monitor.metric.datasource.DefaultDataSourceMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Taejin Koo
 * @author HyunGil Jeong
 */
public class DataSourceMetricProvider implements Provider<DataSourceMetric> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSourceMonitorRegistryService dataSourceMonitorRegistryService;
    private final JdbcUrlParsingService jdbcUrlParsingService;

    @Inject
    public DataSourceMetricProvider(DataSourceMonitorRegistryService dataSourceMonitorRegistryService, JdbcUrlParsingService jdbcUrlParsingService) {
        this.dataSourceMonitorRegistryService = dataSourceMonitorRegistryService;
        this.jdbcUrlParsingService = jdbcUrlParsingService;
    }

    @Override
    public DataSourceMetric get() {
        DataSourceMetric dataSourceMetric;
        if (dataSourceMonitorRegistryService == null || jdbcUrlParsingService == null) {
            dataSourceMetric = DataSourceMetric.UNSUPPORTED_DATA_SOURCE_METRIC;
        } else {
            dataSourceMetric = new DefaultDataSourceMetric(dataSourceMonitorRegistryService, jdbcUrlParsingService);
        }
        logger.info("loaded : {}", dataSourceMetric);
        return dataSourceMetric;
    }
}
