package com.gb.apm.asm.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jongho Moon
 *
 */
public class InterceptorInvokerHelper {
    private static boolean propagateException = false;
    private static final Logger logger = LoggerFactory.getLogger(InterceptorInvokerHelper.class.getName());
    
    public static void handleException(Throwable t) {
        if (propagateException) {
            throw new RuntimeException(t);
        } else {
            logger.warn("Exception occurred from interceptor", t);
        }
    }
    
    public static void setPropagateException(boolean propagate) {
        propagateException = propagate;
    }
}
