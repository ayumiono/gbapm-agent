package com.gb.apm.profiler.context.monitor;

import java.util.ArrayList;
import java.util.List;

import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitor;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DisabledDataSourceMonitorRegistryService implements DataSourceMonitorRegistryService {

    @Override
    public boolean register(DataSourceMonitor pluginMonitor) {
        return false;
    }

    @Override
    public boolean unregister(DataSourceMonitor pluginMonitor) {
        return false;
    }

    @Override
    public List<DataSourceMonitorWrapper> getPluginMonitorWrapperList() {
        return new ArrayList<DataSourceMonitorWrapper>();
    }

    @Override
    public int getRemainingIdNumber() {
        return 0;
    }

}
