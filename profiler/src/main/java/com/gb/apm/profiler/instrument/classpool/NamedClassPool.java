package com.gb.apm.profiler.instrument.classpool;

import javassist.ClassPool;

/**
 * @author emeroad
 */
public class NamedClassPool extends ClassPool {
    private final String name;

    public NamedClassPool(String name) {
        this.name = name;
    }

    public NamedClassPool(boolean useDefaultPath, String name) {
        super(useDefaultPath);
        this.name = name;
    }

    public NamedClassPool(ClassPool parent, String name) {
        super(parent);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NamedClassPool{");
        sb.append("name='").append(name).append('\'');
        sb.append(", super='").append(super.toString()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
