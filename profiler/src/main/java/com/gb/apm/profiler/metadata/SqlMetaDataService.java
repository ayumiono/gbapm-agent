package com.gb.apm.profiler.metadata;

import com.gb.apm.bootstrap.core.context.ParsingResult;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface SqlMetaDataService {

    ParsingResult parseSql(final String sql);

    boolean cacheSql(ParsingResult parsingResult);
}
