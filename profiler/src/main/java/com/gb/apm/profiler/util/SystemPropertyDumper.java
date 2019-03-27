package com.gb.apm.profiler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

/**
 * @author Woonduk Kang(emeroad)
 */
public class SystemPropertyDumper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void dump() {
        if (logger.isInfoEnabled()) {
            Properties properties = System.getProperties();
            Set<String> strings = properties.stringPropertyNames();
            for (String key : strings) {
                logger.info("SystemProperties {}={}", key, properties.get(key));
            }
        }
    }
}
