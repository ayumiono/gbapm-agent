package com.gb.apm.bootstrap.core.plugin.jdbc;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.common.trace.ServiceType;

/**
 * @author Taejin Koo
 */
public interface JdbcContext {

    DatabaseInfo parseJdbcUrl(ServiceType serviceType, String jdbcUrl);

}
