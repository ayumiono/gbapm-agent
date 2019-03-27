package com.gb.apm.common.utils;

import java.util.jar.JarEntry;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ExtensionFilter implements JarEntryFilter {

    public static final JarEntryFilter CLASS_FILTER = new ExtensionFilter(".class");

    private final String extension;

    public ExtensionFilter(String extension) {
        if (extension == null) {
            throw new NullPointerException("extension must not be null");
        }
        this.extension = extension;
    }

    @Override
    public boolean filter(JarEntry jarEntry) {
        final String name = jarEntry.getName();
        return name.endsWith(extension);
    }
}
