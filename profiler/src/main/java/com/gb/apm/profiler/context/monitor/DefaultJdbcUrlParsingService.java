package com.gb.apm.profiler.context.monitor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.gb.apm.bootstrap.core.context.DatabaseInfo;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;
import com.gb.apm.bootstrap.core.plugin.jdbc.UnKnownDatabaseInfo;
import com.gb.apm.common.trace.ServiceType;

/**
 * @author Taejin Koo
 */
public class DefaultJdbcUrlParsingService implements JdbcUrlParsingService {

    private final List<JdbcUrlParserV2> jdbcUrlParserList;

    private final ConcurrentHashMap<String, DatabaseInfo> cache = new ConcurrentHashMap<String, DatabaseInfo>();
    private final ConcurrentHashMap<CacheKey, DatabaseInfo> eachServiceTypeCache = new ConcurrentHashMap<CacheKey, DatabaseInfo>();

    public DefaultJdbcUrlParsingService(List<JdbcUrlParserV2> jdbcUrlParserList) {
        this.jdbcUrlParserList = jdbcUrlParserList;
    }

    @Override
    public DatabaseInfo getDatabaseInfo(String jdbcUrl) {
        DatabaseInfo databaseInfo = cache.get(jdbcUrl);
        return databaseInfo;
    }

    @Override
    public DatabaseInfo getDatabaseInfo(ServiceType serviceType, String jdbcUrl) {
        CacheKey cacheKey = new CacheKey(serviceType, jdbcUrl);
        DatabaseInfo databaseInfo = eachServiceTypeCache.get(cacheKey);
        if (databaseInfo != null && databaseInfo.isParsingComplete()) {
            return databaseInfo;
        }
        return null;
    }

    @Override
    public DatabaseInfo parseJdbcUrl(ServiceType serviceType, String jdbcUrl) {
        if (serviceType == null) {
            return UnKnownDatabaseInfo.INSTANCE;
        }

        if (jdbcUrl == null) {
            return UnKnownDatabaseInfo.INSTANCE;
        }

        CacheKey cacheKey = new CacheKey(serviceType, jdbcUrl);
        DatabaseInfo cacheValue = eachServiceTypeCache.get(cacheKey);
        if (cacheValue != null) {
            return cacheValue;
        }

        for (JdbcUrlParserV2 parser : jdbcUrlParserList) {
            if (parser.getServiceType() != null && serviceType.getCode() == parser.getServiceType().getCode()) {
                DatabaseInfo databaseInfo = parser.parse(jdbcUrl);
                return putCacheIfAbsent(cacheKey, databaseInfo);
            }
        }

        return putCacheIfAbsent(cacheKey, UnKnownDatabaseInfo.createUnknownDataBase(jdbcUrl));
    }

    private DatabaseInfo putCacheIfAbsent(CacheKey cacheKey, DatabaseInfo databaseInfo) {
        if (databaseInfo.isParsingComplete()) {
            cache.putIfAbsent(cacheKey.getJdbcUrl(), databaseInfo);
        }

        DatabaseInfo old = eachServiceTypeCache.putIfAbsent(cacheKey, databaseInfo);
        if (old != null) {
            return old;
        }

        return databaseInfo;
    }

    private static class CacheKey {

        private final ServiceType serviceType;
        private final String jdbcUrl;

        public CacheKey(ServiceType serviceType, String jdbcUrl) {
            this.serviceType = serviceType;
            this.jdbcUrl = jdbcUrl;
        }

        public ServiceType getServiceType() {
            return serviceType;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (serviceType != null ? !serviceType.equals(cacheKey.serviceType) : cacheKey.serviceType != null) return false;
            return jdbcUrl != null ? jdbcUrl.equals(cacheKey.jdbcUrl) : cacheKey.jdbcUrl == null;

        }

        @Override
        public int hashCode() {
            int result = serviceType != null ? serviceType.hashCode() : 0;
            result = 31 * result + (jdbcUrl != null ? jdbcUrl.hashCode() : 0);
            return result;
        }

    }
}
