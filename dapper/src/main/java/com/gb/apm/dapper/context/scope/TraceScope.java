package com.gb.apm.dapper.context.scope;

/**
 * @author jaehong.kim
 */
public interface TraceScope {
    String getName();

    boolean tryEnter();
    boolean canLeave();
    void leave();

    boolean isActive();
}
