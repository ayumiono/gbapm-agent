package com.gb.apm.profiler.context.recorder;

import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.SpanRecorder;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface RecorderFactory {

    SpanRecorder newSpanRecorder(final Span span, final boolean isRoot, final boolean sampling);

    WrappedSpanEventRecorder newWrappedSpanEventRecorder();
}
