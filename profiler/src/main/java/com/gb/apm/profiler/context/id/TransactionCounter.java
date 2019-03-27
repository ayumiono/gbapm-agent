package com.gb.apm.profiler.context.id;

/**
 * @author HyunGil Jeong
 */
public interface TransactionCounter {

    long getSampledNewCount();

    long getSampledContinuationCount();

    long getUnSampledNewCount();

    long getUnSampledContinuationCount();

    long getTotalTransactionCount();

}
