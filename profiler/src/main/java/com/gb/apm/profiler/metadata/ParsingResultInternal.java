package com.gb.apm.profiler.metadata;

import com.gb.apm.bootstrap.core.context.ParsingResult;

/**
 * @author emeroad
 */
public interface ParsingResultInternal extends ParsingResult {


    String getOriginalSql();

    boolean setId(int id);

    void setSql(String sql);

    void setOutput(String output);

}
