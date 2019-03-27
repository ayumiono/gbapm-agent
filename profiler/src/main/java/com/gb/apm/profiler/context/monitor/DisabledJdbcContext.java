package com.gb.apm.profiler.context.monitor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcContext;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.common.trace.ServiceType;

/**
 * @author Taejin Koo
 */
public final class DisabledJdbcContext implements JdbcContext {

    public static final DisabledJdbcContext INSTANCE = new DisabledJdbcContext();

    @Override
    public DatabaseInfo parseJdbcUrl(ServiceType serviceType, String jdbcUrl) {
        return UnKnownDatabaseInfo.createUnknownDataBase(jdbcUrl);
    }

}
