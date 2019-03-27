package com.gb.apm.common.utils;

import java.util.jar.JarEntry;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface JarEntryFilter {
    boolean filter(JarEntry jarEntry);
}
