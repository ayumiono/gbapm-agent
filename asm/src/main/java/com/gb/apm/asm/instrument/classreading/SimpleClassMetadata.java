package com.gb.apm.asm.instrument.classreading;

import java.util.List;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface SimpleClassMetadata {

    int getVersion();

    int getAccessFlag();

    String getClassName();

    String getSuperClassName();

    List<String> getInterfaceNames();

    byte[] getClassBinary();

    void setDefinedClass(Class<?> definedClass);

    Class<?> getDefinedClass();
}
