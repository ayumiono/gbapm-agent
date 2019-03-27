package com.gb.apm.profiler;

/**
 * @author HyunGil Jeong
 */
public class JvmInformation {

    private final String jvmVersion;
    private final int gcTypeCode;

    public JvmInformation(String jvmVersion, int gcTypeCode) {
        this.jvmVersion = jvmVersion;
        this.gcTypeCode = gcTypeCode;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public int getGcTypeCode() {
        return gcTypeCode;
    }
}
