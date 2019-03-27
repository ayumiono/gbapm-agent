package com.gb.apm.profiler.monitor.metric.datasource;

import java.util.Collections;
import java.util.List;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.common.utils.CollectionUtils;
import com.gb.apm.model.TDataSource;
import com.gb.apm.model.TDataSourceList;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryService;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorWrapper;
import com.gb.apm.profiler.context.monitor.JdbcUrlParsingService;

/**
 * @author Taejin Koo
 * @author HyunGil Jeong
 */
public class DefaultDataSourceMetric implements DataSourceMetric {

    private final DataSourceMonitorRegistryService dataSourceMonitorRegistryService;
    private final JdbcUrlParsingService jdbcUrlParsingService;

    public DefaultDataSourceMetric(DataSourceMonitorRegistryService dataSourceMonitorRegistryService, JdbcUrlParsingService jdbcUrlParsingService) {
        if (dataSourceMonitorRegistryService == null) {
            throw new NullPointerException("dataSourceMonitorRegistryService must not be null");
        }
        if (jdbcUrlParsingService == null) {
            throw new NullPointerException("jdbcUrlParsingService must not be null");
        }
        this.dataSourceMonitorRegistryService = dataSourceMonitorRegistryService;
        this.jdbcUrlParsingService = jdbcUrlParsingService;

    }

    @Override
    public TDataSourceList dataSourceList() {
        TDataSourceList dataSourceList = new TDataSourceList();

        List<DataSourceMonitorWrapper> dataSourceMonitorList = dataSourceMonitorRegistryService.getPluginMonitorWrapperList();
        if (!CollectionUtils.isEmpty(dataSourceMonitorList)) {
            for (DataSourceMonitorWrapper dataSourceMonitor : dataSourceMonitorList) {
                TDataSource dataSource = collectDataSource(dataSourceMonitor);
                dataSourceList.addToDataSourceList(dataSource);
            }
        } else {
            dataSourceList.setDataSourceList(Collections.<TDataSource>emptyList());
        }
        return dataSourceList;
    }

    @Override
    public String toString() {
        return "Default DataSourceMetric";
    }

    private TDataSource collectDataSource(DataSourceMonitorWrapper dataSourceMonitor) {
        TDataSource dataSource = new TDataSource();
        dataSource.setId(dataSourceMonitor.getId());
        dataSource.setServiceTypeCode(dataSourceMonitor.getServiceType().getCode());

        String jdbcUrl = dataSourceMonitor.getUrl();
        if (jdbcUrl != null) {
            dataSource.setUrl(jdbcUrl);

            DatabaseInfo databaseInfo = jdbcUrlParsingService.getDatabaseInfo(jdbcUrl);
            if (databaseInfo != null) {
                dataSource.setDatabaseName(databaseInfo.getDatabaseId());
            }
        }

        int activeConnectionSize = dataSourceMonitor.getActiveConnectionSize();
        // this field is optional (default value is 0)
        if (activeConnectionSize != 0) {
            dataSource.setActiveConnectionSize(activeConnectionSize);
        }

        dataSource.setMaxConnectionSize(dataSourceMonitor.getMaxConnectionSize());

        return dataSource;
    }
}
