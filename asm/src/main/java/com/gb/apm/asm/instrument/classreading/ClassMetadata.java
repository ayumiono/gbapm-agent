package com.gb.apm.asm.instrument.classreading;

/**
 * under-development Class
 * not fixed.
 * @author Woonduk Kang(emeroad)
 */
// @Beta
public interface ClassMetadata extends SimpleClassMetadata {

    String getSignature();

    String getSourceFile();

    String getSourceDebug();

    String getOuterClass();

    String getOuterMethod();

    String getOuterMethodDesc();

//    List<MethodMetadata> getMethods();

}
