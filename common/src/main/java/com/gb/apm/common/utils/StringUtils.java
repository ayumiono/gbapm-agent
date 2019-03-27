package com.gb.apm.common.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StringUtils {

    private static final int DEFAULT_ABBREVIATE_MAX_WIDTH = 64;

    private StringUtils() {
    }

    public static String defaultString(final String str, final String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String toString(final Object object) {
        if (object == null) {
            return "null";
        }
        return object.toString();
    }

    public static List<String> splitAndTrim(String value, String separator) {
        if(isEmpty(value)) {
            return Collections.emptyList();
        }
        if (separator == null) {
            throw new NullPointerException("separator must not be null");
        }
        final List<String> result = new ArrayList<String>();
        // TODO remove regex 'separator'
        final String[] split = value.split(separator);
        for (String method : split) {
            if (isEmpty(method)) {
                continue;
            }
            method = method.trim();
            if (method.isEmpty()) {
                continue;
            }
            result.add(method);
        }
        return result;
    }

    /**
     * @deprecated Since 1.6.1. Use {@link StringUtils#abbreviate(String)}
     */
    @Deprecated
    public static String drop(final String str) {
        return abbreviate(str);
    }


    public static String abbreviate(final String str) {
        return abbreviate(str, DEFAULT_ABBREVIATE_MAX_WIDTH);
    }

    /**
     * @deprecated Since 1.6.1. Use {@link StringUtils#abbreviate(String, int)}
     */
    @Deprecated
    public static String drop(final String str, final int maxWidth) {
        return abbreviate(str, maxWidth);
    }

    public static String abbreviate(final String str, final int maxWidth) {
        if (str == null) {
            return "null";
        }
        if (maxWidth < 0) {
            throw new IllegalArgumentException("negative maxWidth:" + maxWidth);
        }
        if (str.length() > maxWidth) {
            StringBuilder buffer = new StringBuilder(maxWidth + 10);
            buffer.append(str, 0, maxWidth);
            appendAbbreviateMessage(buffer, str.length());
            return buffer.toString();
        } else {
            return str;
        }
    }

    /**
     * @deprecated Since 1.6.1. Use {@link StringUtils#appendAbbreviate(StringBuilder, String, int)}
     */
    @Deprecated
    public static void appendDrop(StringBuilder builder, final String str, final int maxWidth) {
        appendAbbreviate(builder, str, maxWidth);
    }

    public static void appendAbbreviate(StringBuilder builder, final String str, final int maxWidth) {
        if (str == null) {
            return;
        }
        if (maxWidth < 0) {
            return;
        }
        if (str.length() > maxWidth) {
            builder.append(str, 0, maxWidth);
            appendAbbreviateMessage(builder, str.length());
        } else {
            builder.append(str);
        }
    }

    private static void appendAbbreviateMessage(StringBuilder buffer, int strLength) {
        buffer.append("...(");
        buffer.append(strLength);
        buffer.append(')');
    }
}
