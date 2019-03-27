package com.gb.apm.common.service;

import java.util.List;

import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.ServiceTypeInfo;

/**
 * @author emeroad
 */
public interface TraceMetadataLoaderService {
    List<ServiceTypeInfo> getServiceTypeInfos();

    List<AnnotationKey> getAnnotationKeys();
}
