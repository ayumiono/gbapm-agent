package com.gb.apm.common.trace;


/**
 * @author emeroad
 */
public class DefaultAnnotationKeyFactory extends AnnotationKeyFactory {

    public AnnotationKey createAnnotationKey(int code, String name, AnnotationKeyProperty... properties) {
        return new DefaultAnnotationKey(code, name, properties);
    }

}