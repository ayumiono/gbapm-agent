package com.gb.apm.common.trace;


/**
 * @author emeroad
 */
public class DefaultServiceTypeInfo implements ServiceTypeInfo {

    private final ServiceType serviceType;
    private final AnnotationKeyMatcher annotationKeyMatcher;

    public DefaultServiceTypeInfo(ServiceType serviceType, AnnotationKeyMatcher annotationKeyMatcher) {
        if (serviceType == null) {
            throw new NullPointerException("serviceType must not be null");
        }
        if (annotationKeyMatcher == null) {
            throw new NullPointerException("annotationKeyMatcher must not be null");
        }
        this.serviceType = serviceType;
        this.annotationKeyMatcher = annotationKeyMatcher;
    }

    public DefaultServiceTypeInfo(ServiceType serviceType) {
        if (serviceType == null) {
            throw new NullPointerException("serviceType must not be null");
        }
        this.serviceType = serviceType;
        this.annotationKeyMatcher = null;
    }

    public AnnotationKeyMatcher getPrimaryAnnotationKeyMatcher() {
        return annotationKeyMatcher;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}
