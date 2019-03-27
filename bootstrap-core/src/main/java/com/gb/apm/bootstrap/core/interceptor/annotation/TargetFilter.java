package com.gb.apm.bootstrap.core.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gb.apm.bootstrap.core.instrument.MethodFilter;

/**
 * Specify the {@link MethodFilter} which will be used to filter the annotated interceptor's target methods.
 * 
 * @author Jongho Moon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetFilter {
    /**
     * Filter type
     */
    String type();
    
    /**
     * Arguments for specified {@link MethodFilter}'s constructor. 
     */
    String[] constructorArguments() default {};
    
    boolean singleton() default false;
}
