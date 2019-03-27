package com.gb.apm.asm.objectfactory;

/**
 * @author Jongho Moon
 *
 */
public interface JudgingParameterResolver extends ArgumentProvider {
    void prepare();
    boolean isAcceptable();
}