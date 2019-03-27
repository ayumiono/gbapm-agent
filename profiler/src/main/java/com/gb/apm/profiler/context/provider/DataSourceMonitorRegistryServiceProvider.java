package com.gb.apm.profiler.context.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryService;
import com.gb.apm.profiler.context.monitor.DefaultDataSourceMonitorRegistryService;
import com.gb.apm.profiler.context.monitor.DisabledDataSourceMonitorRegistryService;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DataSourceMonitorRegistryServiceProvider implements Provider<DataSourceMonitorRegistryService> {

    private static final int DEFAULT_LIMIT_SIZE = 20;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final boolean traceAgentDataSource;
    private final int dataSourceTraceLimitSize;

    @Inject
    public DataSourceMonitorRegistryServiceProvider(ProfilerConfig profilerConfig) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        this.traceAgentDataSource = profilerConfig.isTraceAgentDataSource();
        this.dataSourceTraceLimitSize = profilerConfig.getDataSourceTraceLimitSize();

    }

    @Override
    public DataSourceMonitorRegistryService get() {
        if (!traceAgentDataSource) {
            return new DisabledDataSourceMonitorRegistryService();
        }

        if (dataSourceTraceLimitSize <= 0) {
            logger.info("dataSourceTraceLimitSize must greater than 0. It will be set default size {}", DEFAULT_LIMIT_SIZE);
            return new DefaultDataSourceMonitorRegistryService(DEFAULT_LIMIT_SIZE);
        } else {
            return new DefaultDataSourceMonitorRegistryService(dataSourceTraceLimitSize);
        }
    }

}
