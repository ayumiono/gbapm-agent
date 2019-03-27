package com.gb.apm.bootstrap.core.plugin.jdbc.bindvalue;

/**
 * @author emeroad
 */
public class NullTypeConverter implements Converter {

    @Override
    public String convert(Object[] args) {
        return "null";
    }
}
