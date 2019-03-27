package com.gb.apm.bootstrap.core.plugin.jdbc.bindvalue;

import com.gb.apm.common.utils.StringUtils;

/**
 * @author emeroad
 */
public class SimpleTypeConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            return StringUtils.abbreviate(StringUtils.toString(args[1]));
        } else if (args.length == 3) {
            // need to handle 3rd arg?
            return StringUtils.abbreviate(StringUtils.toString(args[1]));
        }
        return "error";
    }
}
