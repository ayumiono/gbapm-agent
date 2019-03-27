package com.gb.apm.common.trace;

/**
 * @author emeroad
 */
public abstract class ServiceTypeFactory {

    private static final ServiceTypeFactory DEFAULT_FACTORY = new DefaultServiceTypeFactory();

    public static ServiceType of(int code, String name, ServiceTypeProperty... properties) {
        return of(code, name, name, properties);
    }

    public static ServiceType of(int code, String name, String desc, ServiceTypeProperty... properties) {
        return DEFAULT_FACTORY.createServiceType(code, name, desc, properties);
    }

    ServiceTypeFactory() {
    }



    abstract ServiceType createServiceType(int code, String name, String desc, ServiceTypeProperty... properties);
}
