package com.gb.apm.profiler.metadata;

import com.gb.apm.bootstrap.core.context.ParsingResult;

/**
 * @author emeroad
 */
public class DefaultParsingResult implements ParsingResultInternal {

    private String originalSql;
    private String sql ;
    private String output;

    private int id = ParsingResult.ID_NOT_EXIST;


    public DefaultParsingResult(String originalSql) {
        this.originalSql = originalSql;
    }


    public String getOriginalSql() {
        return originalSql;
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public int getId() {
        return id;
    }


    public boolean setId(int id) {
        // clear originalSql reference
        this.originalSql = null;

        if (this.id == ID_NOT_EXIST) {
            this.id = id;
            return true;
        }
        return false;
    }

    @Override
    public String getOutput() {
        if (this.output == null) {
            return "";
        }
        return this.output;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultParsingResult{");
        sb.append("sql='").append(sql).append('\'');
        sb.append(", output=").append(output);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
