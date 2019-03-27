package com.gb.apm.profiler.plugin;

import java.util.ArrayList;
import java.util.List;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.plugin.ApplicationTypeDetector;
import com.gb.apm.bootstrap.core.plugin.ProfilerPluginSetupContext;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;


/**
 * @author jaehong.kim
 */
public class DefaultProfilerPluginSetupContext implements ProfilerPluginSetupContext {

    private final ProfilerConfig profilerConfig;

    private final List<ApplicationTypeDetector> serverTypeDetectors = new ArrayList<ApplicationTypeDetector>();
    private final List<JdbcUrlParserV2> jdbcUrlParserList = new ArrayList<JdbcUrlParserV2>();

    public DefaultProfilerPluginSetupContext(ProfilerConfig profilerConfig) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }

        this.profilerConfig = profilerConfig;
    }

    @Override
    public ProfilerConfig getConfig() {
        return profilerConfig;
    }


    @Override
    public void addApplicationTypeDetector(ApplicationTypeDetector... detectors) {
        if (detectors == null) {
            return;
        }
        for (ApplicationTypeDetector detector : detectors) {
            serverTypeDetectors.add(detector);
        }
    }

    public List<ApplicationTypeDetector> getApplicationTypeDetectors() {
        return serverTypeDetectors;
    }

    @Override
    public void addJdbcUrlParser(JdbcUrlParserV2 jdbcUrlParser) {
        if (jdbcUrlParser == null) {
            return;
        }

        this.jdbcUrlParserList.add(jdbcUrlParser);
    }

    public List<JdbcUrlParserV2> getJdbcUrlParserList() {
        return jdbcUrlParserList;
    }

}
