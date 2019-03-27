package com.gb.apm.common.service;

import java.util.List;

import com.gb.apm.common.trace.ServiceType;

/**
 * @author emeroad
 */
public interface ServiceTypeRegistryService {
    ServiceType findServiceType(short serviceType);

    ServiceType findServiceTypeByName(String typeName);

    @Deprecated
    List<ServiceType> findDesc(String desc);
}
