package com.gb.apm.profiler.plugin;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.plugin.ApplicationTypeDetector;
import com.gb.apm.bootstrap.core.plugin.ProfilerPluginSetupContext;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;

/**
 * @author emeroad
 */
public class GuardProfilerPluginContext implements ProfilerPluginSetupContext {

    private final ProfilerPluginSetupContext delegate;
    private boolean close = false;

    public GuardProfilerPluginContext(ProfilerPluginSetupContext delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }
        this.delegate = delegate;
    }



    @Override
    public ProfilerConfig getConfig() {
//        checkOpen();
        return this.delegate.getConfig();
    }

    @Override
    public void addApplicationTypeDetector(ApplicationTypeDetector... detectors) {
        checkOpen();
        this.delegate.addApplicationTypeDetector(detectors);
    }

    @Override
    public void addJdbcUrlParser(JdbcUrlParserV2 jdbcUrlParser) {
        checkOpen();
        this.delegate.addJdbcUrlParser(jdbcUrlParser);
    }

    private void checkOpen() {
        if (close) {
            throw new IllegalStateException("ProfilerPluginSetupContext already initialized");
        }
    }

    public void close() {
        this.close = true;
    }
}
