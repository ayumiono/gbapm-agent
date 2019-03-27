package com.gb.apm.profiler.context.provider.stat.transaction;

import com.gb.apm.profiler.context.id.TransactionCounter;
import com.gb.apm.profiler.monitor.metric.transaction.DefaultTransactionMetric;
import com.gb.apm.profiler.monitor.metric.transaction.TransactionMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author HyunGil Jeong
 */
public class TransactionMetricProvider implements Provider<TransactionMetric> {

    private final TransactionCounter transactionCounter;

    @Inject
    public TransactionMetricProvider(TransactionCounter transactionCounter) {
        this.transactionCounter = transactionCounter;
    }

    @Override
    public TransactionMetric get() {
        if (transactionCounter == null) {
            return TransactionMetric.UNSUPPORTED_TRANSACTION_METRIC;
        }
        return new DefaultTransactionMetric(transactionCounter);
    }
}
