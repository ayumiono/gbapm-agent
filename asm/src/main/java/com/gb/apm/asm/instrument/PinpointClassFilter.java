package com.gb.apm.asm.instrument;

import java.security.ProtectionDomain;

/**
 * @author emeroad
 */
public class PinpointClassFilter implements ClassFileFilter {

    private final ClassLoader agentLoader;

    public PinpointClassFilter(ClassLoader agentLoader) {
        if (agentLoader == null) {
            throw new NullPointerException("agentLoader must not be null");
        }
        this.agentLoader = agentLoader;
    }

    @Override
    public boolean accept(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        // bootstrap classLoader
        if (classLoader == null) {
            return CONTINUE;
        }
        if (classLoader == agentLoader) {
            // skip classes loaded by agent class loader.
            return SKIP;
        }

        // Skip pinpoint packages too.
        if (className.startsWith("com/navercorp/pinpoint/")) {
            if (className.startsWith("com/navercorp/pinpoint/web/")) {
                return CONTINUE;
            }
            return SKIP;
        }

        return CONTINUE;
    }
}
