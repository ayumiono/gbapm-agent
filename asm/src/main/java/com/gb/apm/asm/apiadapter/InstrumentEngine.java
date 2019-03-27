package com.gb.apm.asm.apiadapter;


import java.util.jar.JarFile;

import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.NotFoundInstrumentException;

/**
 * @author Jongho Moon
 *
 */
public interface InstrumentEngine {

    InstrumentClass getClass(InstrumentContext instrumentContext, ClassLoader classLoader, String classInternalName, byte[] classFileBuffer) throws NotFoundInstrumentException;

    boolean hasClass(ClassLoader classLoader, String classBinaryName);

    void appendToBootstrapClassPath(JarFile jarFile);
}
