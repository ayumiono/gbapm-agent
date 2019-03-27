package com.gb.apm.common.hbase;

@SuppressWarnings("serial")
public class HbaseSystemException extends RuntimeException {

    public HbaseSystemException(Exception cause) {
        super(cause.getMessage(), cause);
    }
}
