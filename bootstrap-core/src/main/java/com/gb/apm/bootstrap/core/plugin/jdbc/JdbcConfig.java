package com.gb.apm.bootstrap.core.plugin.jdbc;

/**
 * @author HyunGil Jeong
 */
public class JdbcConfig {
    private final boolean pluginEnable;
    private final boolean traceSqlBindValue;
    private final int maxSqlBindValueSize;


    protected JdbcConfig( boolean pluginEnable, boolean traceSqlBindValue, int maxSqlBindValue) {
        this.pluginEnable = pluginEnable;
        this.traceSqlBindValue = traceSqlBindValue;
        this.maxSqlBindValueSize = maxSqlBindValue;
    }

    public boolean isPluginEnable() {
        return pluginEnable;
    }

    public boolean isTraceSqlBindValue() {
        return traceSqlBindValue;
    }

    public int getMaxSqlBindValueSize() {
        return maxSqlBindValueSize;
    }

    @Override
    public String toString() {
        return "pluginEnable=" + pluginEnable +", traceSqlBindValue=" + this.traceSqlBindValue + ", maxSqlBindValueSize=" + this.maxSqlBindValueSize;
    }
}
