package com.gb.apm.asm.apiadapter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Woonduk Kang(emeroad)
 */
public class BootstrapPackage {

    private static final List<String> BOOTSTRAP_PACKAGE_LIST = Arrays.asList("com.gb.apm.bootstrap", "com.gb.apm.common", "com.gb.apm.dapper");

    public boolean isBootstrapPackage(String className) {
        for (String bootstrapPackage : BOOTSTRAP_PACKAGE_LIST) {
            if (className.startsWith(bootstrapPackage)) {
                return true;
            }
        }
        return false;
    }
}
