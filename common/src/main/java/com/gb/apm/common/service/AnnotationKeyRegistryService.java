package com.gb.apm.common.service;

import com.gb.apm.common.trace.AnnotationKey;

/**
 * @author emeroad
 */
public interface AnnotationKeyRegistryService {
    AnnotationKey findAnnotationKey(int annotationCode);

    AnnotationKey findAnnotationKeyByName(String keyName);

    AnnotationKey findApiErrorCode(int annotationCode);
}
