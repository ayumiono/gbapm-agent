package com.gb.apm.bootstrap.core.plugin.jdbc;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;

/**
 * @author Jongho Moon
 *
 */
public interface DatabaseInfoAccessor {
    void _$PINPOINT$_setDatabaseInfo(DatabaseInfo info);
    DatabaseInfo _$PINPOINT$_getDatabaseInfo();
}
