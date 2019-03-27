package com.gb.apm.bootstrap.core.plugin.monitor;

/**
 * @author Taejin Koo
 */
public interface DataSourceMonitorRegistry {

    boolean register(DataSourceMonitor dataSourceMonitor);

    boolean unregister(DataSourceMonitor dataSourceMonitor);


}
