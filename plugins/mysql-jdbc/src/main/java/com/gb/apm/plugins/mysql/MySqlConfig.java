package com.gb.apm.plugins.mysql;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcConfig;

/**
 * @author Jongho Moon
 */
public class MySqlConfig extends JdbcConfig {
    private final boolean profileSetAutoCommit;
    private final boolean profileCommit;
    private final boolean profileRollback;

    public MySqlConfig(ProfilerConfig config) {
        super(config.readBoolean("profiler.jdbc.mysql", false),
                config.readBoolean("profiler.jdbc.mysql.tracesqlbindvalue", config.isTraceSqlBindValue()),
                config.getMaxSqlBindValueSize());
        this.profileSetAutoCommit = config.readBoolean("profiler.jdbc.mysql.setautocommit", false);
        this.profileCommit = config.readBoolean("profiler.jdbc.mysql.commit", false);
        this.profileRollback = config.readBoolean("profiler.jdbc.mysql.rollback", false);
    }

    public boolean isProfileSetAutoCommit() {
        return profileSetAutoCommit;
    }

    public boolean isProfileCommit() {
        return profileCommit;
    }

    public boolean isProfileRollback() {
        return profileRollback;
    }

    @Override
    public String toString() {
        return "MySqlConfig [" + super.toString() + ", profileSetAutoCommit=" + profileSetAutoCommit + ", profileCommit=" + profileCommit + ", profileRollback=" + profileRollback + "]";
    }
}
