package com.gb.apm.bootstrap.core.instrument.transfomer;

import java.security.ProtectionDomain;

import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.Instrumentor;


@Plugin
public interface TransformCallback {

    byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException;

}
