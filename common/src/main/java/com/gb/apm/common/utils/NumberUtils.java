package com.gb.apm.common.utils;

public final class NumberUtils {
    private NumberUtils() {
    }

    public static long parseLong(String str, long defaultLong) {
        if (str == null) {
            return defaultLong;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultLong;
        }
    }

    public static int parseInteger(String str, int defaultInt) {
        if (str == null) {
            return defaultInt;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultInt;
        }
    }

    public static short parseShort(String str, short defaultShort) {
        if (str == null) {
            return defaultShort;
        }
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            return defaultShort;
        }
    }

    public static Integer toInteger(Object integer) {
        if (integer == null) {
            return null;
        }
        if (integer instanceof Integer) {
            return (Integer) integer;
        } else {
            return null;
        }
    }

}
