package com.gb.apm.profiler.context.monitor;

import java.util.concurrent.atomic.AtomicInteger;

import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitor;

/**
 * @author Taejin Koo
 */
public class DataSourceMonitorWrapperFactory {

    private final AtomicInteger idGenerator = new AtomicInteger();

    public DataSourceMonitorWrapper create(DataSourceMonitor pluginMonitor) {
        return new DataSourceMonitorWrapper(idGenerator.incrementAndGet(), pluginMonitor);
    }

    public int latestIssuedId() {
        return idGenerator.get();
    }

}
