package com.gb.apm.dapper.context;

import com.gb.apm.annotations.InterfaceAudience;

/**
 * @author jaehong.kim
 */
@InterfaceAudience.LimitedPrivate("vert.x")
public interface AsyncState {

    void setup();

    boolean await();

    void finish();
}