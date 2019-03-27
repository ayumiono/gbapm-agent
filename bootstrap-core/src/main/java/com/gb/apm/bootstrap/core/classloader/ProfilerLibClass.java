package com.gb.apm.bootstrap.core.classloader;

/**
 * @author emeroad
 */
public class ProfilerLibClass implements LibClass {

    private static final String[] PINPOINT_PROFILER_CLASS = new String[] {
            "com.gb.apm.profiler",
            "com.gb.apm.thrift",
            "com.gb.apm.rpc",
            "com.gb.apm.asm",
            "com.gb.apm.collector",
            "com.gb.apm.model",
            /*
             * @deprecated javassist
             */
            "javassist",
            "org.objectweb.asm",
            "org.slf4j",
            "org.apache.thrift",
            "org.jboss.netty",
            "com.google.common",
            // google guice
            "com.google.inject",
            "org.aopalliance",

            "org.apache.commons.lang",
            "org.apache.log4j",
            "com.codahale.metrics",
            "com.nhncorp.nelo2",
            "ch.qos.logback"
    };

    @Override
    public boolean onLoadClass(String clazzName) {
        final int length = PINPOINT_PROFILER_CLASS.length;
        for (int i = 0; i < length; i++) {
            if (clazzName.startsWith(PINPOINT_PROFILER_CLASS[i])) {
                return ON_LOAD_CLASS;
            }
        }
        return DELEGATE_PARENT;
    }
}
