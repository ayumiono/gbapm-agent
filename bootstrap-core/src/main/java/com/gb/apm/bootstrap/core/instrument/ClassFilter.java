package com.gb.apm.bootstrap.core.instrument;


/**
 * @author jaehong.kim
 */
public interface ClassFilter {
    boolean ACCEPT = true;
    boolean REJECT = false;

    boolean accept(InstrumentClass clazz);  
}
