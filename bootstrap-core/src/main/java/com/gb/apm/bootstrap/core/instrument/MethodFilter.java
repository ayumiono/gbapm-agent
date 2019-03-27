package com.gb.apm.bootstrap.core.instrument;


/**
 * @author emeroad
 */
public interface MethodFilter {
    boolean ACCEPT = true;
    boolean REJECT = false;

    boolean accept(InstrumentMethod method);  
}
