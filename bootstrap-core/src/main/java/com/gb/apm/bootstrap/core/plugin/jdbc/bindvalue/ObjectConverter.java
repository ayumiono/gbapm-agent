package com.gb.apm.bootstrap.core.plugin.jdbc.bindvalue;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;

import com.gb.apm.common.utils.ArrayUtils;
import com.gb.apm.common.utils.StringUtils;

/**
 * @author emeroad
 */
public class ObjectConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            final Object param = args[1];
            return getParameter(param);

        } else if (args.length == 3) {
            final Object param = args[1];
            return getParameter(param);
        }
        return "error";
    }

    private String getParameter(Object param) {
        if (param == null) {
            return "null";
        } else {
            if (param instanceof Byte) {
                return abbreviate(param);
            } else if (param instanceof String) {
                return abbreviate(param);
            } else if (param instanceof BigDecimal) {
                return abbreviate(param);
            } else if (param instanceof Short) {
                return abbreviate(param);
            } else if (param instanceof Integer) {
                return abbreviate(param);
            } else if (param instanceof Long) {
                return abbreviate(param);
            } else if (param instanceof Float) {
                return abbreviate(param);
            } else if (param instanceof Double) {
                return abbreviate(param);
            } else if (param instanceof BigInteger) {
                return abbreviate(param);
            } else if (param instanceof java.sql.Date) {
                return abbreviate(param);
            } else if (param instanceof Time) {
                return abbreviate(param);
            } else if (param instanceof Timestamp) {
                return abbreviate(param);
            } else if (param instanceof Boolean) {
                return abbreviate(param);
            } else if (param instanceof byte[]) {
                return ArrayUtils.abbreviate((byte[]) param);
            } else if (param instanceof InputStream) {
                return getClassName(param);
            } else if (param instanceof java.sql.Blob) {
                return getClassName(param);
            } else if (param instanceof java.sql.Clob) {
                return getClassName(param);
            } else {
                return getClassName(param);
            }
        }
    }

    private String abbreviate(Object param) {
        return StringUtils.abbreviate(param.toString());
    }

    private String getClassName(Object param) {
        return param.getClass().getName();
    }
}
