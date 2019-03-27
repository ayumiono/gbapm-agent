package com.gb.apm.profiler.context.id;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface IdGenerator {


    long nextTransactionId();

    long nextContinuedTransactionId();

    long nextDisabledId();

    long nextContinuedDisabledId();

    long currentTransactionId();

    long currentContinuedTransactionId();

    long currentDisabledId();

    long currentContinuedDisabledId();
}
