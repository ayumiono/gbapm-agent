package com.gb.apm.profiler.context.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitor;

/**
 * @author Taejin Koo
 */
public class DefaultDataSourceMonitorRegistryService implements DataSourceMonitorRegistryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int limitIdNumber;

    private final CopyOnWriteArrayList<DataSourceMonitorWrapper> repository = new CopyOnWriteArrayList<DataSourceMonitorWrapper>();

    private final DataSourceMonitorWrapperFactory wrapperFactory = new DataSourceMonitorWrapperFactory();

    public DefaultDataSourceMonitorRegistryService(int limitIdNumber) {
        this.limitIdNumber = limitIdNumber;
    }

    @Override
    public boolean register(DataSourceMonitor dataSourceMonitor) {
        if (wrapperFactory.latestIssuedId() >= limitIdNumber) {
            if (logger.isInfoEnabled()) {
                logger.info("can't register {}. The maximum value of id number has been exceeded.");
            }
            return false;
        }

        DataSourceMonitorWrapper dataSourceMonitorWrapper = wrapperFactory.create(dataSourceMonitor);
        return repository.add(dataSourceMonitorWrapper);
    }

    @Override
    public boolean unregister(DataSourceMonitor dataSourceMonitor) {
        for (DataSourceMonitorWrapper dataSourceMonitorWrapper : repository) {
            if (dataSourceMonitorWrapper.equalsWithUnwrap(dataSourceMonitor)) {
                return repository.remove(dataSourceMonitorWrapper);
            }
        }
        return false;
    }

    @Override
    public List<DataSourceMonitorWrapper> getPluginMonitorWrapperList() {
        List<DataSourceMonitorWrapper> pluginMonitorList = new ArrayList<DataSourceMonitorWrapper>(repository.size());
        List<DataSourceMonitorWrapper> disabledPluginMonitorList = new ArrayList<DataSourceMonitorWrapper>();

        for (DataSourceMonitorWrapper dataSourceMonitorWrapper : repository) {
            if (dataSourceMonitorWrapper.isDisabled()) {
                disabledPluginMonitorList.add(dataSourceMonitorWrapper);
            } else {
                pluginMonitorList.add(dataSourceMonitorWrapper);
            }
        }

        // bulk delete for reduce copy
        if (disabledPluginMonitorList.size() > 0) {
            logger.info("PluginMonitorWrapper was disabled(list:{})", disabledPluginMonitorList);
            repository.removeAll(disabledPluginMonitorList);
        }

        return pluginMonitorList;
    }

    @Override
    public int getRemainingIdNumber() {
        return limitIdNumber - wrapperFactory.latestIssuedId();
    }

}
