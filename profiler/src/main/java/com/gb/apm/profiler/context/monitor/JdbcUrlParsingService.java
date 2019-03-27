package com.gb.apm.profiler.context.monitor;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.common.trace.ServiceType;


/**
 * @author Taejin Koo
 */
public interface JdbcUrlParsingService {

    DatabaseInfo getDatabaseInfo(String jdbcUrl);

    DatabaseInfo getDatabaseInfo(ServiceType serviceType, String jdbcUrl);

    DatabaseInfo parseJdbcUrl(ServiceType serviceType, String jdbcUrl);
}
