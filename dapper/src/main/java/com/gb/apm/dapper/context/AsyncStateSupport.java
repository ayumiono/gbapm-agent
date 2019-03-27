package com.gb.apm.dapper.context;

import com.gb.apm.annotations.InterfaceAudience;

/**
 * @author Woonduk Kang(emeroad)
 */
@InterfaceAudience.LimitedPrivate("vert.x")
public interface AsyncStateSupport {
    AsyncState getAsyncState();
}
