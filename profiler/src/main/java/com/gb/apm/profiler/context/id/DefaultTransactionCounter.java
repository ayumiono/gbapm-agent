package com.gb.apm.profiler.context.id;

import com.google.inject.Inject;

/**
 * @author HyunGil Jeong
 */
public class DefaultTransactionCounter implements TransactionCounter {

    private final IdGenerator idGenerator;

    @Inject
    public DefaultTransactionCounter(IdGenerator idGenerator) {
        if (idGenerator == null) {
            throw new NullPointerException("idGenerator cannot be null");
        }
        this.idGenerator = idGenerator;
    }
    
    @Override
    public long getSampledNewCount() {
        return idGenerator.currentTransactionId() - AtomicIdGenerator.INITIAL_TRANSACTION_ID;
    }

    @Override
    public long getSampledContinuationCount() {
        return Math.abs(idGenerator.currentContinuedTransactionId() - AtomicIdGenerator.INITIAL_CONTINUED_TRANSACTION_ID) / AtomicIdGenerator.DECREMENT_CYCLE;
    }

    @Override
    public long getUnSampledNewCount() {
        return Math.abs(idGenerator.currentDisabledId() - AtomicIdGenerator.INITIAL_DISABLED_ID) / AtomicIdGenerator.DECREMENT_CYCLE;
    }

    @Override
    public long getUnSampledContinuationCount() {
        return Math.abs(idGenerator.currentContinuedDisabledId() - AtomicIdGenerator.INITIAL_CONTINUED_DISABLED_ID) / AtomicIdGenerator.DECREMENT_CYCLE;
    }

    @Override
    public long getTotalTransactionCount() {
        long count = getSampledNewCount();
        count += getSampledContinuationCount();
        count += getUnSampledNewCount();
        count += getUnSampledContinuationCount();
        return count;
    }
}
