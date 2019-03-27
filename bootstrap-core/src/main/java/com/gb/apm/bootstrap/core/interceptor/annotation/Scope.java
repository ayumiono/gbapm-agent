package com.gb.apm.bootstrap.core.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gb.apm.bootstrap.core.interceptor.Interceptor;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;

/**
 * Indicates that the annotated {@link Interceptor} participate in a {@link Scope}.
 * 
 * @author Jongho Moon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    /**
     * scope name
     */
    String value();
    
    /**
     * specify when this interceptor have to be invoked.
     */
    ExecutionPolicy executionPolicy() default ExecutionPolicy.BOUNDARY;
}
