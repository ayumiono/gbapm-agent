package com.gb.apm.common.trace;


/**
 * @author emeroad
 */
public interface ServiceTypeInfo {

    ServiceType getServiceType();

    AnnotationKeyMatcher getPrimaryAnnotationKeyMatcher();
}
