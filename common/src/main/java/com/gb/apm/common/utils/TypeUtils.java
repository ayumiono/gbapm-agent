package com.gb.apm.common.utils;

import java.lang.annotation.Annotation;

// TODO move package
public final class TypeUtils {

    private TypeUtils() {
    }

    public static Class<?> getWrapperOf(Class<?> primitive) {
        if (primitive == boolean.class) {
            return Boolean.class;
        } else if (primitive == byte.class) {
            return Byte.class;
        } else if (primitive == char.class) {
            return Character.class;
        } else if (primitive == short.class) {
            return Short.class;
        } else if (primitive == int.class) {
            return Integer.class;
        } else if (primitive == long.class) {
            return Long.class;
        } else if (primitive == float.class) {
            return Float.class;
        } else if (primitive == double.class) {
            return Double.class;
        } else if (primitive == void.class) {
            return Void.class;
        }
        
        throw new IllegalArgumentException("Unexpected argument: " + primitive);
    }
    
    public static String[] toClassNames(Class<?>... classes) {
        final int length = classes.length;
        final String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = classes[i].getName();
        }
        
        return result;
    }
        
    public static <T extends Annotation> T findAnnotation(Annotation[] annotations, Class<T> type) {
        for (Annotation a : annotations) {
            if (a.annotationType() == type) {
                return type.cast(a);
            }
        }
        
        return null;
    }
}
