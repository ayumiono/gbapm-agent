package com.gb.apm.common.utils;;

/**
 * @author emeroad
 */
public interface SimpleProperty {
    void setProperty(String key, String value);

    String getProperty(String key);

    String getProperty(String key, String defaultValue);
}
