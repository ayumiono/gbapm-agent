package com.gb.apm.profiler.context.storage;

import com.gb.apm.dapper.context.Span;

/**
 * @author emeroad
 * @author jaehong.kim
 */
public interface Storage {

    /**
     *
     * @param spanEvent
     */
    void store(Span._SpanEvent spanEvent);

    /**
     *
     * @param span
     */
    void store(Span span);

    void flush();

    void close();
}
