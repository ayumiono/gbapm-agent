package com.gb.apm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Copy from google-guava
 * https://github.com/google/guava/tree/master/guava/src/com/google/common/annotations
 *
 * Annotates a program element that exists, or is more widely visible than
 * otherwise necessary, only for use in test code.
 *
 * @author Johannes Henkel
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface VisibleForTesting {
}
