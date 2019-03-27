package com.gb.apm.profiler.context.monitor;

import java.util.List;

import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitor;

/**
 * @author Taejin Koo
 */
public interface DataSourceMonitorRegistryService {

    boolean register(DataSourceMonitor dataSourceMonitor);

    boolean unregister(DataSourceMonitor dataSourceMonitor);

    List<DataSourceMonitorWrapper> getPluginMonitorWrapperList();

    int getRemainingIdNumber();
}
