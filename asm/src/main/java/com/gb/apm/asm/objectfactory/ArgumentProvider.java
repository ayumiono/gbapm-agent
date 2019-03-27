package com.gb.apm.asm.objectfactory;

import java.lang.annotation.Annotation;

/**
 * @author Jongho Moon
 *
 */
public interface ArgumentProvider {
    Option get(int index, Class<?> type, Annotation[] annotations);
}
