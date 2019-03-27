package com.gb.apm.dapper.context;

/**
 * @author emeroad
 */
public interface FrameAttachment {

    Object attachFrameObject(Object frameObject);

    Object getFrameObject();

    Object detachFrameObject();
}
