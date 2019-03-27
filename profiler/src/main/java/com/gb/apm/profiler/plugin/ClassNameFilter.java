package com.gb.apm.profiler.plugin;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface ClassNameFilter {
    boolean ACCEPT = true;
    boolean REJECT = false;

    boolean accept(String className);
}
