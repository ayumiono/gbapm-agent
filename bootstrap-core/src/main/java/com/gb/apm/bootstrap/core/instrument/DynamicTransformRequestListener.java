package com.gb.apm.bootstrap.core.instrument;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author emeroad
 */
public interface DynamicTransformRequestListener {

    RequestHandle onRetransformRequest(Class<?> target, ClassFileTransformer transformer);

    void onTransformRequest(ClassLoader classLoader, String targetClassName, ClassFileTransformer transformer);
}
