package com.gb.apm.bootstrap.core.plugin;


/**
 * Pinpoint profiler plugin should implement this interface.
 * 
 * When Pinpoint agent initialize, plugins are loaded by the agent, and then their {@link #setup(ProfilerPluginSetupContext)} methods are invoked.
 * 
 * @author Jongho Moon
 *
 */
public interface ProfilerPlugin {
    void setup(ProfilerPluginSetupContext context);
}
