package com.gb.apm.profiler.context.monitor;

/**
 * @author Taejin Koo
 */
public interface PluginMonitorWrapper {

    int getId();

    boolean equalsWithUnwrap(Object object);

}
