package com.gb.apm.asm.instrument.classreading;


import java.util.List;

import com.gb.apm.asm.util.JavaAssistUtils;

/**
 * @author Woonduk Kang(emeroad)
 * @author jaehong.kim
 */
public class DefaultSimpleClassMetadata implements SimpleClassMetadata {

    private final int version;

    private final int accessFlag;

    private final String className;

    private final String superClassName;

    private final List<String> interfaceNameList;

    private final byte[] classBinary;

    private Class<?> definedClass;

    public DefaultSimpleClassMetadata(int version, int accessFlag, String className, String superClassName, String[] interfaceNameList, byte[] classBinary) {
        this.version = version;
        this.accessFlag = accessFlag;
        this.className = JavaAssistUtils.jvmNameToJavaName(className);
        this.superClassName = JavaAssistUtils.jvmNameToJavaName(superClassName);
        this.interfaceNameList = JavaAssistUtils.jvmNameToJavaName(interfaceNameList);
        this.classBinary = classBinary;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public int getAccessFlag() {
        return accessFlag;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getSuperClassName() {
        return superClassName;
    }

    @Override
    public List<String> getInterfaceNames() {
        return interfaceNameList;
    }

    @Override
    public byte[] getClassBinary() {
        return classBinary;
    }

    public void setDefinedClass(final Class<?> definedClass) {
        this.definedClass = definedClass;
    }

    @Override
    public Class<?> getDefinedClass() {
        return this.definedClass;
    }
}
