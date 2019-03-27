package com.gb.apm.bootstrap.core.instrument.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author emeroad
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface JointPoint {

}
