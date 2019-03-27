package com.gb.apm.plugins.commbiz;

import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.AnnotationKeyFactory;

public interface CommBizConstants {
    AnnotationKey RESPONSE_CODE = AnnotationKeyFactory.of(902, "response.code");
    AnnotationKey CHAIN_METHOD_NODE_KEY = AnnotationKeyFactory.of(903, "chainlevel.methodid");
    AnnotationKey METHOD_NODE_KEY = AnnotationKeyFactory.of(905, "methodid");
    AnnotationKey COMMBIZ_API_ARGS = AnnotationKeyFactory.of(904, "method.args");
}
