package com.gb.apm.common.service;

import java.util.List;

import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.common.trace.ServiceTypeInfo;
import com.gb.apm.common.trace.ServiceTypeRegistry;
import com.gb.apm.common.utils.StaticFieldLookUp;
import com.gb.apm.common.utils.logger.CommonLogger;
import com.gb.apm.common.utils.logger.CommonLoggerFactory;
import com.gb.apm.common.utils.logger.StdoutCommonLoggerFactory;


/**
 * @author emeroad
 */
public class DefaultServiceTypeRegistryService implements ServiceTypeRegistryService {
	private final CommonLogger logger;

    private final TraceMetadataLoaderService typeLoaderService;
    private final ServiceTypeRegistry registry;

    public DefaultServiceTypeRegistryService() {
    	this(new DefaultTraceMetadataLoaderService(), StdoutCommonLoggerFactory.INSTANCE);
    }


    public DefaultServiceTypeRegistryService(TraceMetadataLoaderService typeLoaderService, CommonLoggerFactory commonLoggerFactory) {
        if (typeLoaderService == null) {
            throw new NullPointerException("typeLoaderService must not be null");
        }
        if (commonLoggerFactory == null) {
            throw new NullPointerException("commonLoggerFactory must not be null");
        }
        this.logger = commonLoggerFactory.getLogger(DefaultServiceTypeRegistryService.class.getName());
        this.typeLoaderService = typeLoaderService;
        this.registry = buildServiceTypeRegistry();
    }

    private ServiceTypeRegistry buildServiceTypeRegistry() {
        ServiceTypeRegistry.Builder builder = new ServiceTypeRegistry.Builder();

        StaticFieldLookUp<ServiceType> staticFieldLookUp = new StaticFieldLookUp<ServiceType>(ServiceType.class, ServiceType.class);
        List<ServiceType> lookup = staticFieldLookUp.lookup();
        for (ServiceType serviceType: lookup) {
            if (logger.isInfoEnabled()) {
                logger.info("add Default ServiceType:" + serviceType);
            }
            builder.addServiceType(serviceType);
        }

        final List<ServiceTypeInfo> types = loadType();
        for (ServiceTypeInfo type : types) {
            if (logger.isInfoEnabled()) {
                logger.info("add Plugin ServiceType:" + type.getServiceType());
            }
            builder.addServiceType(type.getServiceType());
        }

        return builder.build();
    }


    private List<ServiceTypeInfo> loadType() {
        return typeLoaderService.getServiceTypeInfos();
    }

    @Override
    public ServiceType findServiceType(short serviceType) {
        return registry.findServiceType(serviceType);
    }

    public ServiceType findServiceTypeByName(String typeName) {
        return registry.findServiceTypeByName(typeName);
    }

    @Override
    public List<ServiceType> findDesc(String desc) {
        return registry.findDesc(desc);
    }

}
