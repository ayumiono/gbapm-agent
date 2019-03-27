package com.gb.apm.bootstrap.core.instrument;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author emeroad
 */
public interface DynamicTransformTrigger {
    void retransform(Class<?> target, ClassFileTransformer transformer);
    void addClassFileTransformer(ClassLoader classLoader, String targetClassName, ClassFileTransformer transformer);
}
