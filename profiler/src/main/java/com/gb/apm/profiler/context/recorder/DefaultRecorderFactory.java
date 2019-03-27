package com.gb.apm.profiler.context.recorder;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.SpanRecorder;
import com.gb.apm.profiler.metadata.SqlMetaDataService;
import com.gb.apm.profiler.metadata.StringMetaDataService;
import com.google.inject.Inject;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultRecorderFactory implements RecorderFactory {

    private final StringMetaDataService stringMetaDataService;
    private final SqlMetaDataService sqlMetaDataService;
    private final ProfilerConfig profilerConfig;
    private final boolean jdbcRawSQL;

    @Inject
    public DefaultRecorderFactory(StringMetaDataService stringMetaDataService, SqlMetaDataService sqlMetaDataService, ProfilerConfig profilerConfig) {
        if (stringMetaDataService == null) {
            throw new NullPointerException("stringMetaDataService must not be null");
        }
        if (sqlMetaDataService == null) {
            throw new NullPointerException("sqlMetaDataService must not be null");
        }
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        this.stringMetaDataService = stringMetaDataService;
        this.sqlMetaDataService = sqlMetaDataService;
        this.profilerConfig = profilerConfig;
        this.jdbcRawSQL = this.profilerConfig.readBoolean("profiler.jdbc.rawsql", true);
    }

    @Override
    public SpanRecorder newSpanRecorder(Span span, boolean isRoot, boolean sampling) {
        return new DefaultSpanRecorder(span, isRoot, sampling, stringMetaDataService, sqlMetaDataService);
    }

    @Override
    public WrappedSpanEventRecorder newWrappedSpanEventRecorder() {
        return new WrappedSpanEventRecorder(jdbcRawSQL, stringMetaDataService, sqlMetaDataService);
    }
}
