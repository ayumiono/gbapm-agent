package com.gb.apm.bootstrap.core.plugin;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;

/**
 *  Provides attributes and objects to interceptors.
 * 
 *  Only interceptors can acquire an instance of this class as a constructor argument.
 * 
 * @author Jongho Moon
 *
 */
public interface ProfilerPluginSetupContext {
    /**
     * Get the {@link ProfilerConfig}
     * 
     * @return {@link ProfilerConfig}
     */
    ProfilerConfig getConfig();

    /**
     * Add a {@link ApplicationTypeDetector} to Pinpoint agent.
     * 
     * @param detectors
     */
    void addApplicationTypeDetector(ApplicationTypeDetector... detectors);

    void addJdbcUrlParser(JdbcUrlParserV2 jdbcUrlParserV2);

}
