package com.gb.apm.common.utils;

/**
 * @author Woonduk Kang(emeroad)
 */
public final class ArrayUtils {

    private static final int DEFAULT_ABBREVIATE_MAX_WIDTH = 32;

    private ArrayUtils() {
    }

    public static String abbreviate(byte[] bytes) {
        return abbreviate(bytes, DEFAULT_ABBREVIATE_MAX_WIDTH);
    }

    public static String abbreviate(byte[] bytes, int maxWidth) {
        if (bytes == null) {
            return "null";
        }
        if (maxWidth < 0) {
            throw new IllegalArgumentException("negative maxWidth:" + maxWidth);
        }
        // TODO handle negative limit

        // Last valid index is length - 1
        int bytesMaxLength = bytes.length - 1;
        final int maxLimit = maxWidth - 1;
        if (bytesMaxLength > maxLimit) {
            bytesMaxLength = maxLimit;
        }
        if (bytesMaxLength == -1) {
            if (bytes.length == 0) {
                return "[]";
            } else {
                return "[...(" + bytes.length + ")]";
            }
        }


        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; ; i++) {
            sb.append(bytes[i]);
            if (i == bytesMaxLength) {
                if ((bytes.length - 1) <= maxLimit) {
                    return sb.append(']').toString();
                } else {
                    sb.append(", ...(");
                    sb.append(bytes.length - (i+1));
                    sb.append(")]");
                    return sb.toString();
                }
            }
            sb.append(", ");
        }
    }
}
