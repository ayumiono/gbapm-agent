package com.gb.apm.common.trace;


/**
 * @author Jongho Moon
 *
 */
public interface TraceMetadataSetupContext {

    void addServiceType(ServiceType serviceType);

    void addServiceType(ServiceType serviceType, AnnotationKeyMatcher primaryAnnotationKeyMatcher);

    void addAnnotationKey(AnnotationKey annotationKey);
}
