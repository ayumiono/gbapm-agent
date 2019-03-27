package com.gb.apm.profiler.context.monitor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcContext;
import com.gb.apm.common.trace.ServiceType;
import com.google.inject.Inject;


/**
 * @author Taejin Koo
 */
public class DefaultJdbcContext implements JdbcContext {

    private final JdbcUrlParsingService jdbcUrlParsingService;

    @Inject
    public DefaultJdbcContext(JdbcUrlParsingService jdbcUrlParsingService) {
        if (jdbcUrlParsingService == null) {
            throw new NullPointerException("jdbcUrlParsingService must not be null");
        }
        this.jdbcUrlParsingService = jdbcUrlParsingService;
    }


    @Override
    public DatabaseInfo parseJdbcUrl(ServiceType serviceType, String jdbcUrl) {
        return this.jdbcUrlParsingService.parseJdbcUrl(serviceType, jdbcUrl);
    }


}
