package com.gb.apm.common.utils;


/**
 * @author emeroad
 */
public class SystemProperty implements SimpleProperty {

    public static final SystemProperty INSTANCE = new SystemProperty();

    @Override
    public void setProperty(String key, String value) {
        System.setProperty(key, value);
    }

    @Override
    public String getProperty(String key) {
        return System.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    public String getEnv(String name) {
        return System.getenv(name);
    }
}
