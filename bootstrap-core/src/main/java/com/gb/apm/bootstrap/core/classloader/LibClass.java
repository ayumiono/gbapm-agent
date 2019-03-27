package com.gb.apm.bootstrap.core.classloader;

/**
 * @author emeroad
 */
public interface LibClass {

    boolean ON_LOAD_CLASS = true;
    boolean DELEGATE_PARENT = false;

    boolean onLoadClass(String clazzName);
}
