package com.gb.apm.bootstrap.core.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the target constructor of the annotated interceptor.
 * 
 * @author Jongho Moon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetConstructor {
    /**
     * Target constructor's parameter types.
     * 
     * @return
     */
    String[] value() default {};
}
