package com.gb.apm.profiler.instrument.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.instrument.MethodFilters;
import com.gb.apm.profiler.interceptor.BasicMethodInterceptor;

/**
 * @author Jongho Moon
 *
 */
public class DebugTransformer implements ClassFileTransformer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final InstrumentContext instrumentContext;
    private final InstrumentEngine instrumentEngine;

    public DebugTransformer(InstrumentEngine instrumentEngine, InstrumentContext instrumentContext) {
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (instrumentContext == null) {
            throw new NullPointerException("instrumentContext must not be null");
        }
        this.instrumentEngine = instrumentEngine;
        this.instrumentContext = instrumentContext;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            final InstrumentClass target = instrumentEngine.getClass(instrumentContext, loader, className, classfileBuffer);
            if (target == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("targetClass not found. className:{}, classBeingRedefined:{} :{} ", className, classBeingRedefined, loader);
                }
                // throw exception ?
                return null;
            }
            
            if (!target.isInterceptable()) {
                return null;
            }
    
            for (InstrumentMethod method : target.getDeclaredMethods(MethodFilters.ACCEPT_ALL)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("### c={}, m={}, params={}", className, method.getName(), Arrays.toString(method.getParameterTypes()));
                }
                
                method.addInterceptor(BasicMethodInterceptor.class.getName());
            }
    
            return target.toBytecode();
        } catch (InstrumentException e) {
            logger.warn("Failed to instrument " + className, e);
            return null;
        }
    }
    
    
}
