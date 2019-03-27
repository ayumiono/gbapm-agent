package com.gb.apm.profiler.metadata;

import com.gb.apm.bootstrap.core.context.ParsingResult;

/**
 * @author emeroad
 */
public interface CachingSqlNormalizer {
    ParsingResult wrapSql(String sql);

    boolean normalizedSql(ParsingResult sql);
}
