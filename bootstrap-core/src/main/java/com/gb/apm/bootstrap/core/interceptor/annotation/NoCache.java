package com.gb.apm.bootstrap.core.interceptor.annotation;

import java.beans.MethodDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;

/**
 * Indicates that the target have to be cached. 
 * 
 * For now, only {@link MethodDescriptor} can be cached. 
 * You can also annotate {@link InstrumentMethod} with this annotation 
 * but it makes the {@link MethodDescriptor} returned by {@link InstrumentMethod#getDescriptor()} cached 
 * not {@link InstrumentMethod} itself.
 * 
 * @author Jongho Moon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NoCache {
}
