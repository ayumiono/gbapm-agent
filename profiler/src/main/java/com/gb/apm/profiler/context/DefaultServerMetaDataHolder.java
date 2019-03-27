package com.gb.apm.profiler.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.gb.apm.dapper.context.ServerMetaData;
import com.gb.apm.dapper.context.ServerMetaDataHolder;
import com.gb.apm.dapper.context.ServiceInfo;

/**
 * @author hyungil.jeong
 */
public class DefaultServerMetaDataHolder implements ServerMetaDataHolder {
    
    private final List<ServerMetaDataListener> listeners;

    protected String serverName;
    private final List<String> vmArgs;
    private final Map<Integer, String> connectors = new ConcurrentHashMap<Integer, String>();
    protected final Queue<ServiceInfo> serviceInfos = new ConcurrentLinkedQueue<ServiceInfo>();

    public DefaultServerMetaDataHolder(List<String> vmArgs) {
        this.listeners = new ArrayList<ServerMetaDataListener>();
        this.vmArgs = vmArgs;
    }

    @Override
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public void addConnector(String protocol, int port) {
        this.connectors.put(port, protocol);
    }
    
    @Override
    public void addServiceInfo(String serviceName, List<String> serviceLibs) {
        ServiceInfo serviceInfo = new DefaultServiceInfo(serviceName, serviceLibs);
        this.serviceInfos.add(serviceInfo);
    }

    @Override
    public void addListener(ServerMetaDataListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ServerMetaDataListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public synchronized void notifyListeners() {
        final ServerMetaData serverMetaData = createServerMetaData();
        for (ServerMetaDataListener listener : this.listeners) {
            listener.publishServerMetaData(serverMetaData);
        }
    }

    private ServerMetaData createServerMetaData() {
        String serverName = this.serverName == null ? "" : this.serverName;
        List<String> vmArgs = 
                this.vmArgs == null ? Collections.<String>emptyList() : new ArrayList<String>(this.vmArgs);
        Map<Integer, String> connectors = 
                this.connectors.isEmpty() ? Collections.<Integer, String>emptyMap() : new HashMap<Integer, String>(this.connectors);
        List<ServiceInfo> serviceInfos = 
                this.serviceInfos.isEmpty() ? Collections.<ServiceInfo>emptyList() : new ArrayList<ServiceInfo>(this.serviceInfos);
        return new DefaultServerMetaData(serverName, vmArgs, connectors, serviceInfos);
    }

}
