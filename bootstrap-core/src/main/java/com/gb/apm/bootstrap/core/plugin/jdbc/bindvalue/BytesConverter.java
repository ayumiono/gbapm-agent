package com.gb.apm.bootstrap.core.plugin.jdbc.bindvalue;

import com.gb.apm.common.utils.ArrayUtils;

/**
 * @author emeroad
 */
public class BytesConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            final byte[] bytes = (byte[]) args[1];
            if (bytes == null) {
                return "null";
            } else {
                return ArrayUtils.abbreviate(bytes);
            }
        }
        return "error";
    }
}
