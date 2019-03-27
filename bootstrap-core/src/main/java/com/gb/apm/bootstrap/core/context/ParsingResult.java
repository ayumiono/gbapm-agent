package com.gb.apm.bootstrap.core.context;

/**
 * @author emeroad
 */
public interface ParsingResult {

    int ID_NOT_EXIST = 0;

    String getSql();

    String getOutput();

    int getId();
}
