package com.gb.apm.common.utils;

import java.util.List;

/**
 * @author emeroad
 */
public interface SqlParser {

    NormalizedSql normalizedSql(String sql);

    String combineOutputParams(String sql, List<String> outputParams);

    String combineBindValues(String sql, List<String> bindValues);
}
