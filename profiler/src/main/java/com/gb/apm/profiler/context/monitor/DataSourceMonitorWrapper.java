package com.gb.apm.profiler.context.monitor;

import java.lang.ref.WeakReference;

import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitor;
import com.gb.apm.common.trace.ServiceType;

/**
 * @author Taejin Koo
 */
public class DataSourceMonitorWrapper implements PluginMonitorWrapper, DataSourceMonitor {

    private final int id;
    private final WeakReference<DataSourceMonitor> monitorReference;

    private volatile ServiceType serviceType;

    public DataSourceMonitorWrapper(int id, DataSourceMonitor dataSourceMonitor) {
        if (dataSourceMonitor == null) {
            throw new NullPointerException("dataSourceMonitor may not be null");
        }

        this.id = id;
        this.monitorReference = new WeakReference<DataSourceMonitor>(dataSourceMonitor);
    }

    private DataSourceMonitor getInstance() {
        return monitorReference.get();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ServiceType getServiceType() {
        if (this.serviceType != null) {
            return this.serviceType;
        }

        DataSourceMonitor dataSourceMonitor = getInstance();
        if (dataSourceMonitor != null) {
            ServiceType serviceType = dataSourceMonitor.getServiceType();
            if (serviceType != null) {
                this.serviceType = serviceType;
            }
            return serviceType;
        }
        return ServiceType.UNKNOWN;
    }

    @Override
    public String getUrl() {
        DataSourceMonitor dataSourceMonitor = getInstance();
        if (dataSourceMonitor != null) {
            return dataSourceMonitor.getUrl();
        }
        return null;
    }

    @Override
    public int getActiveConnectionSize() {
        DataSourceMonitor dataSourceMonitor = getInstance();
        if (dataSourceMonitor != null) {
            return dataSourceMonitor.getActiveConnectionSize();
        }
        return -1;
    }

    @Override
    public int getMaxConnectionSize() {
        DataSourceMonitor dataSourceMonitor = getInstance();
        if (dataSourceMonitor != null) {
            return dataSourceMonitor.getMaxConnectionSize();
        }
        return -1;
    }

    @Override
    public boolean isDisabled() {
        DataSourceMonitor dataSourceMonitor = getInstance();
        if (dataSourceMonitor == null) {
            return true;
        }

        return dataSourceMonitor.isDisabled();
    }

    @Override
    public boolean equalsWithUnwrap(Object object) {
        if (object == null) {
            return false;
        }

        DataSourceMonitor instance = getInstance();
        if (instance == null) {
            return false;
        }

        return instance == object;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataSourceMonitorWrapper{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }

}
