package com.gb.apm.profiler.instrument.classloading;

import com.gb.apm.profiler.plugin.PluginConfig;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface PluginClassInjector extends ClassInjector {

    PluginConfig getPluginConfig();
}
