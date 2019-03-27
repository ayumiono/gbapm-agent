package com.gb.apm.bootstrap.core.instrument;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author emeroad
 */
public interface DynamicTransformerRegistry extends DynamicTransformRequestListener {
    ClassFileTransformer getTransformer(ClassLoader classLoader, String targetClassName);
}
