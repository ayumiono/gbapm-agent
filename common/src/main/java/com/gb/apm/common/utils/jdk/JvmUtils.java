package com.gb.apm.common.utils.jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;

/**
 * @author hyungil.jeong
 */
public final class JvmUtils {
    private static final RuntimeMXBean RUNTIME_MX_BEAN = ManagementFactory.getRuntimeMXBean();
    private static final Map<String, String> SYSTEM_PROPERTIES = RUNTIME_MX_BEAN.getSystemProperties();

    private static final JvmVersion JVM_VERSION = _getVersion();
    private static final JvmType JVM_TYPE = _getType();

    private JvmUtils() {
    }

    public static JvmVersion getVersion() {
        return JVM_VERSION;
    }

    public static JvmType getType() {
        return JVM_TYPE;
    }

    public static boolean supportsVersion(JvmVersion other) {
        return JVM_VERSION.onOrAfter(other);
    }

    public static String getSystemProperty(SystemPropertyKey systemPropertyKey) {
        String key = systemPropertyKey.getKey();
        if (SYSTEM_PROPERTIES.containsKey(key)) {
            return SYSTEM_PROPERTIES.get(key);
        }
        return "";
    }

    private static JvmVersion _getVersion() {
        String javaVersion = getSystemProperty(SystemPropertyKey.JAVA_SPECIFICATION_VERSION);
        return JvmVersion.getFromVersion(javaVersion);
    }

    private static JvmType _getType() {
        String javaVmName = getSystemProperty(SystemPropertyKey.JAVA_VM_NAME);
        return JvmType.fromVmName(javaVmName);
    }
}
