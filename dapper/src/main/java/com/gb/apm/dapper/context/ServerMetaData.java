package com.gb.apm.dapper.context;

import java.util.List;
import java.util.Map;

/**
 * @author hyungil.jeong
 */
public interface ServerMetaData {

    String getServerInfo();
    
    List<String> getVmArgs();

    Map<Integer, String> getConnectors();

    List<ServiceInfo> getServiceInfos();
}
