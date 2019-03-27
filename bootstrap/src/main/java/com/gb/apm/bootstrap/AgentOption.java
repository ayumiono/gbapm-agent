package com.gb.apm.bootstrap;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.List;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.common.service.AnnotationKeyRegistryService;
import com.gb.apm.common.service.ServiceTypeRegistryService;

/**
 * @author emeroad
 */
public interface AgentOption {

    Instrumentation getInstrumentation();

    String getAgentId();

    String getApplicationName();

    ProfilerConfig getProfilerConfig();

    URL[] getPluginJars();

    List<String> getBootstrapJarPaths();

    ServiceTypeRegistryService getServiceTypeRegistryService();
    
    AnnotationKeyRegistryService getAnnotationKeyRegistryService();
}
