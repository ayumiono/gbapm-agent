package com.gb.apm.bootstrap.core.instrument.transfomer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author emeroad
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Plugin {
}
