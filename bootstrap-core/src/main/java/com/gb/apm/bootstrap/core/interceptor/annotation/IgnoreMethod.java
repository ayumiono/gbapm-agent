package com.gb.apm.bootstrap.core.interceptor.annotation;

import java.lang.annotation.*;

/**
 *  @author Woonduk Kang(emeroad)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreMethod {
}
