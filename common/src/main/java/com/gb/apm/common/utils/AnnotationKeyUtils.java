package com.gb.apm.common.utils;

import com.gb.apm.common.trace.AnnotationKey;

/**
 * @author emeroad
 */
public final class AnnotationKeyUtils {

    private AnnotationKeyUtils() {
    }

    public static AnnotationKey getArgs(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index:" + index);
        }
        switch (index) {
            case 0:
                return AnnotationKey.ARGS0;
            case 1:
                return AnnotationKey.ARGS1;
            case 2:
                return AnnotationKey.ARGS2;
            case 3:
                return AnnotationKey.ARGS3;
            case 4:
                return AnnotationKey.ARGS4;
            case 5:
                return AnnotationKey.ARGS5;
            case 6:
                return AnnotationKey.ARGS6;
            case 7:
                return AnnotationKey.ARGS7;
            case 8:
                return AnnotationKey.ARGS8;
            case 9:
                return AnnotationKey.ARGS9;
            default:
                return AnnotationKey.ARGSN;
        }
    }

    public static boolean isArgsKey(int index) {
        if (index <= AnnotationKey.ARGS0.getCode() && index >= AnnotationKey.ARGSN.getCode()) {
            return true;
        }
        return false;
    }


    public static AnnotationKey getCachedArgs(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index:" + index);
        }
        switch (index) {
            case 0:
                return AnnotationKey.CACHE_ARGS0;
            case 1:
                return AnnotationKey.CACHE_ARGS1;
            case 2:
                return AnnotationKey.CACHE_ARGS2;
            case 3:
                return AnnotationKey.CACHE_ARGS3;
            case 4:
                return AnnotationKey.CACHE_ARGS4;
            case 5:
                return AnnotationKey.CACHE_ARGS5;
            case 6:
                return AnnotationKey.CACHE_ARGS6;
            case 7:
                return AnnotationKey.CACHE_ARGS7;
            case 8:
                return AnnotationKey.CACHE_ARGS8;
            case 9:
                return AnnotationKey.CACHE_ARGS9;
            default:
                return AnnotationKey.CACHE_ARGSN;
        }
    }

    public static boolean isCachedArgsKey(int index) {
        if (index <= AnnotationKey.CACHE_ARGS0.getCode() && index >= AnnotationKey.CACHE_ARGSN.getCode()) {
            return true;
        }
        return false;
    }

    public static int cachedArgsToArgs(int index) {
        if (!isCachedArgsKey(index)) {
            throw new IllegalArgumentException("non CACHED_ARGS:" + index);
        }

        final int cachedIndex = AnnotationKey.CACHE_ARGS0.getCode() - AnnotationKey.ARGS0.getCode();
        // you have to - (minus) operation because of negative name
        return index - cachedIndex;
    }
}
