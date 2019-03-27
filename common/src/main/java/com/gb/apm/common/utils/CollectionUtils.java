package com.gb.apm.common.utils;

import java.util.Collection;

/**
 * @author Woonduk Kang(emeroad)
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> int nullSafeSize(Collection<T> collection) {
        return nullSafeSize(collection, 0);
    }

    public static <T> int nullSafeSize(Collection<T> collection, int nullValue) {
        if (collection == null) {
            return nullValue;
        }
        return collection.size();
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }
}
