package com.gb.apm.profiler.plugin;

import com.gb.apm.bootstrap.core.plugin.ProfilerPlugin;
import com.gb.apm.profiler.instrument.classloading.ClassInjector;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface PluginSetup {
    SetupResult setupPlugin(ProfilerPlugin plugin, ClassInjector classInjector);
}
