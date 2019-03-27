package com.gb.apm.profiler.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.ParsingResult;
import com.gb.apm.model.TSqlMetaData;
import com.gb.apm.profiler.context.module.AgentId;
import com.gb.apm.profiler.context.module.AgentStartTime;
import com.google.inject.Inject;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultSqlMetaDataService implements SqlMetaDataService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final CachingSqlNormalizer cachingSqlNormalizer;

    private final String agentId;
    private final long agentStartTime;

    @Inject
    public DefaultSqlMetaDataService(ProfilerConfig profilerConfig, @AgentId String agentId,
                                     @AgentStartTime long agentStartTime) {
        this(agentId, agentStartTime, profilerConfig.getJdbcSqlCacheSize());
    }

    public DefaultSqlMetaDataService(String agentId, long agentStartTime, int jdbcSqlCacheSize) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        this.agentId = agentId;
        this.agentStartTime = agentStartTime;
        this.cachingSqlNormalizer = new DefaultCachingSqlNormalizer(jdbcSqlCacheSize);
    }

    @Override
    public ParsingResult parseSql(final String sql) {
        // lazy sql normalization
        return this.cachingSqlNormalizer.wrapSql(sql);
    }


    @Override
    public boolean cacheSql(ParsingResult parsingResult) {

        if (parsingResult == null) {
            return false;
        }
        // lazy sql parsing
        boolean isNewValue = this.cachingSqlNormalizer.normalizedSql(parsingResult);
        if (isNewValue) {
            if (isDebug) {
                // TODO logging hit ratio could help debugging
                logger.debug("NewSQLParsingResult:{}", parsingResult);
            }

            // isNewValue means that the value is newly cached.
            // So the sql could be new one. We have to send sql metadata to collector.
            final TSqlMetaData sqlMetaData = new TSqlMetaData();
            sqlMetaData.setAgentId(agentId);
            sqlMetaData.setAgentStartTime(agentStartTime);

            sqlMetaData.setSqlId(parsingResult.getId());
            sqlMetaData.setSql(parsingResult.getSql());
        }
        return isNewValue;
    }

}
