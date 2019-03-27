package com.gb.apm.profiler.context;

import com.gb.apm.dapper.context.Span;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface CallStackFactory {
    CallStack newCallStack(Span span);
}
