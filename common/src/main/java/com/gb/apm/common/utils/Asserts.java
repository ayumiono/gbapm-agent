package com.gb.apm.common.utils;

/**
 * @author Jongho Moon
 *
 */
public class Asserts {
    private Asserts() {}
    
    public static void notNull(Object value, String name) {
        if (value == null) {
            throw new NullPointerException(name + " can not be null");
        }
    }
}
