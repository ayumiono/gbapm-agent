package com.gb.apm.profiler;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.instrument.DynamicTransformRequestListener;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.RequestHandle;
import com.gb.apm.common.utils.Asserts;

/**
 * @author emeroad
 */
public class DynamicTransformService implements DynamicTransformTrigger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Instrumentation instrumentation;

    private DynamicTransformRequestListener dynamicTransformRequestListener;

    public DynamicTransformService(Instrumentation instrumentation, DynamicTransformRequestListener listener) {
        Asserts.notNull(instrumentation, "instrumentation");
        Asserts.notNull(listener, "listener");

        this.instrumentation = instrumentation;
        this.dynamicTransformRequestListener = listener;
    }

    @Override
    public void retransform(Class<?> target, ClassFileTransformer transformer) {
        if (this.logger.isDebugEnabled()) {
            logger.debug("retransform request class:{}", target.getName());
        }
        assertClass(target);

        final RequestHandle requestHandle = this.dynamicTransformRequestListener.onRetransformRequest(target, transformer);
        boolean success = false;
        try {
            triggerRetransform(target);
            success = true;
        } finally {
            if (!success) {
                requestHandle.cancel();
            }
        }
    }
    
    @Override
    public void addClassFileTransformer(ClassLoader classLoader, String targetClassName, ClassFileTransformer transformer) {
        if (this.logger.isDebugEnabled()) {
            logger.debug("Add dynamic transform. classLoader={}, class={}", classLoader, targetClassName);
        }
        
        this.dynamicTransformRequestListener.onTransformRequest(classLoader, targetClassName, transformer);
    }

    private void assertClass(Class<?> target) {
        if (!instrumentation.isModifiableClass(target)) {
            throw new ProfilerException("Target class " + target + " is not modifiable");
        }
    }

    private void triggerRetransform(Class<?> target) {
        try {
            instrumentation.retransformClasses(target);
        } catch (UnmodifiableClassException e) {
            throw new ProfilerException(e);
        }
    }

    public void setTransformRequestEventListener(DynamicTransformRequestListener retransformEventListener) {
        if (retransformEventListener == null) {
            throw new NullPointerException("dynamicTransformRequestListener must not be null");
        }
        this.dynamicTransformRequestListener = retransformEventListener;
    }

}
