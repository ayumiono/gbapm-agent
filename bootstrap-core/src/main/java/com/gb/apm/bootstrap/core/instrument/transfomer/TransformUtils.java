package com.gb.apm.bootstrap.core.instrument.transfomer;

import java.security.ProtectionDomain;

import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.Instrumentor;

/**
 * @author Jongho Moon
 *
 */
public final class TransformUtils {
    private TransformUtils() {
    }

    public static TransformCallback addInterceptor(final String interceptorClassName, final Object[] constructorArgs) {
        return new TransformCallback() {
            
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
                target.addInterceptor(interceptorClassName, constructorArgs);
                return target.toBytecode();
            }
        };
    }
    
    public static TransformCallback addField(final String fieldAccessorClassName) {
        return new TransformCallback() {
            
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
                target.addField(fieldAccessorClassName);
                return target.toBytecode();
            }
        };
    }
 
}
