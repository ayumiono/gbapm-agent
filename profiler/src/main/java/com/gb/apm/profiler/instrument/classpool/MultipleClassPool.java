package com.gb.apm.profiler.instrument.classpool;

/**
 * @author emeroad
 */
public interface MultipleClassPool {

    NamedClassPool getClassPool(ClassLoader classLoader);

}
