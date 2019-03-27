package com.gb.apm.bootstrap.core.plugin.jdbc;

import java.lang.reflect.Method;

/**
 * @author emeroad
 */
public interface BindVariableFilter {
    boolean filter(Method method);
}
