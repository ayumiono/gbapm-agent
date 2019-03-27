package com.gb.apm.common.utils.jdk;

public enum JvmType {
    UNKNOWN(null),
    IBM("IBM"),
    OPENJDK("OpenJDK"),
    ORACLE("HotSpot");

    private final String inclusiveString;

    JvmType(String inclusiveString) {
        this.inclusiveString = inclusiveString;
    }

    public static JvmType fromVendor(String vendorName) {
        if (vendorName == null) {
            return UNKNOWN;
        }
        final String vendorNameTrimmed = vendorName.trim();
        for (JvmType jvmType : JvmType.values()) {
            if (jvmType.toString().equalsIgnoreCase(vendorNameTrimmed)) {
                return jvmType;
            }
        }
        return UNKNOWN;
    }

    public static JvmType fromVmName(String vmName) {
        if (vmName == null) {
            return UNKNOWN;
        }
        for (JvmType jvmType : JvmType.values()) {
            if (jvmType.inclusiveString == null) {
                continue;
            } else if (vmName.contains(jvmType.inclusiveString)) {
                return jvmType;
            }
        }
        return UNKNOWN;
    }
}
