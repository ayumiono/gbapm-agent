package com.gb.apm.profiler.context.provider;

import java.util.List;

import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;
import com.gb.apm.profiler.context.monitor.DefaultJdbcUrlParsingService;
import com.gb.apm.profiler.context.monitor.JdbcUrlParsingService;
import com.gb.apm.profiler.plugin.PluginContextLoadResult;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Taejin Koo
 */
public class JdbcUrlParsingServiceProvider implements Provider<JdbcUrlParsingService> {

    private final Provider<PluginContextLoadResult> pluginContextLoadResultProvider;

    @Inject
    public JdbcUrlParsingServiceProvider(Provider<PluginContextLoadResult> pluginContextLoadResultProvider) {
        if (pluginContextLoadResultProvider == null) {
            throw new NullPointerException("pluginContextLoadResult must not be null");
        }
        this.pluginContextLoadResultProvider = pluginContextLoadResultProvider;
    }

    @Override
    public JdbcUrlParsingService get() {
        PluginContextLoadResult pluginContextLoadResult = this.pluginContextLoadResultProvider.get();
        List<JdbcUrlParserV2> jdbcUrlParserList = pluginContextLoadResult.getJdbcUrlParserList();

        return new DefaultJdbcUrlParsingService(jdbcUrlParserList);
    }

}
