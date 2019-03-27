package com.gb.apm.profiler.context.provider.stat.transaction;

import com.gb.apm.profiler.monitor.collector.transaction.DefaultTransactionMetricCollector;
import com.gb.apm.profiler.monitor.collector.transaction.TransactionMetricCollector;
import com.gb.apm.profiler.monitor.collector.transaction.UnsupportedTransactionMetricCollector;
import com.gb.apm.profiler.monitor.metric.transaction.TransactionMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author HyunGil Jeong
 */
public class TransactionMetricCollectorProvider implements Provider<TransactionMetricCollector> {

    private final TransactionMetric transactionMetric;

    @Inject
    public TransactionMetricCollectorProvider(TransactionMetric transactionMetric) {
        if (transactionMetric == null) {
            throw new NullPointerException("transactionMetric must not be null");
        }
        this.transactionMetric = transactionMetric;
    }

    @Override
    public TransactionMetricCollector get() {
        if (transactionMetric == TransactionMetric.UNSUPPORTED_TRANSACTION_METRIC) {
            return new UnsupportedTransactionMetricCollector();
        }
        return new DefaultTransactionMetricCollector(transactionMetric);
    }
}
