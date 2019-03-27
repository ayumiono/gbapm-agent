package com.gb.apm.asm.instrument;

import java.security.ProtectionDomain;

/**
 * @author emeroad
 */
public interface ClassFileFilter {
    boolean SKIP = false;
    boolean CONTINUE = true;

    boolean accept(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer);
}
