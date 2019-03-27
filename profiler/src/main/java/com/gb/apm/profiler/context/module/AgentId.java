package com.gb.apm.profiler.context.module;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Woonduk Kang(emeroad)
 */
@BindingAnnotation
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface AgentId {
}
