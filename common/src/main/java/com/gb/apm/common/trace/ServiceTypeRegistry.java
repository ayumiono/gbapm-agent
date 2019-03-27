package com.gb.apm.common.trace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gb.apm.common.utils.apache.IntHashMap;
import com.gb.apm.common.utils.apache.IntHashMapUtils;

/**
 * @author emeroad
 */
public class ServiceTypeRegistry {

    private final IntHashMap<ServiceType> codeLookupTable;

    private final Map<String, ServiceType> nameLookupTable;

    private final Map<String, List<ServiceType>> descLookupTable;

    private ServiceTypeRegistry() {
        this.codeLookupTable = new IntHashMap<ServiceType>();
        this.nameLookupTable = new HashMap<String, ServiceType>();
        this.descLookupTable = new HashMap<String, List<ServiceType>>();
    }

    private ServiceTypeRegistry(HashMap<Integer, ServiceType> buildMap) {
        if (buildMap == null) {
            throw new NullPointerException("codeLookupTable must not be null");
        }
        this.codeLookupTable = IntHashMapUtils.copy(buildMap);
        this.nameLookupTable = buildNameLookupTable(buildMap.values());
        this.descLookupTable = buildDescLookupTable(buildMap.values());
    }

    private Map<String, ServiceType> buildNameLookupTable(Collection<ServiceType> serviceTypes) {
        final Map<String, ServiceType> copy = new HashMap<String, ServiceType>();

        for (ServiceType serviceType : serviceTypes) {
            final ServiceType duplicated = copy.put(serviceType.getName(), serviceType);
            if (duplicated  != null) {
                throw new IllegalStateException("duplicated ServiceType " + serviceType + " / " + duplicated);
            }
        }
        return copy;
    }

    public ServiceType findServiceType(short code) {
        final ServiceType serviceType = this.codeLookupTable.get(code);
        if (serviceType == null) {
            return ServiceType.UNDEFINED;
        }
        return serviceType;
    }

    public ServiceType findServiceTypeByName(String name) {
        final ServiceType serviceType = this.nameLookupTable.get(name);
        if (serviceType == null) {
            return ServiceType.UNDEFINED;
        }
        return serviceType;
    }

    @Deprecated
    public List<ServiceType> findDesc(String desc) {
        if (desc == null) {
            throw new NullPointerException("desc must not be null");
        }

        return descLookupTable.get(desc);
    }

    private Map<String, List<ServiceType>> buildDescLookupTable(Collection<ServiceType> serviceTypes) {
        final Map<String, List<ServiceType>> table = new HashMap<String, List<ServiceType>>();

        for (ServiceType serviceType : serviceTypes) {
            if (serviceType.isRecordStatistics()) {
                List<ServiceType> serviceTypeList = table.get(serviceType.getDesc());
                if (serviceTypeList == null) {
                    serviceTypeList = new ArrayList<ServiceType>();
                    table.put(serviceType.getDesc(), serviceTypeList);
                }
                serviceTypeList.add(serviceType);
            }
        }
        return unmodifiableMap(table);


    }

    private static Map<String, List<ServiceType>> unmodifiableMap(Map<String, List<ServiceType>> table) {
        // value of this table will be exposed. so make them unmodifiable.
        final Map<String, List<ServiceType>> copy = new HashMap<String, List<ServiceType>>(table.size());

        for (Map.Entry<String, List<ServiceType>> entry : table.entrySet()) {
            List<ServiceType> newValue = Collections.unmodifiableList(entry.getValue());
            copy.put(entry.getKey(), newValue);
        }

        return copy;
    }


    public static class Builder {

        private final HashMap<Integer, ServiceType> buildMap = new HashMap<Integer, ServiceType>();

        public void addServiceType(ServiceType serviceType) {
            if (serviceType == null) {
                throw new NullPointerException("serviceType must not be null");
            }
            int code = serviceType.getCode();
            final ServiceType exist = this.buildMap.put(code, serviceType);
            if (exist != null) {
                throw new IllegalStateException("already exist. serviceType:" + serviceType + ", exist:" + exist);
            }
        }


        public ServiceTypeRegistry build() {

            return new ServiceTypeRegistry(buildMap);
        }
    }

}
