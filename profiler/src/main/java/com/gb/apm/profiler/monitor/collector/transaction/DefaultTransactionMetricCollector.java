package com.gb.apm.profiler.monitor.collector.transaction;

import com.gb.apm.model.TTransaction;
import com.gb.apm.profiler.monitor.metric.transaction.TransactionMetric;
import com.google.inject.Inject;

/**
 * @author HyunGil Jeong
 */
public class DefaultTransactionMetricCollector implements TransactionMetricCollector {

    private final TransactionMetric transactionMetric;

    @Inject
    public DefaultTransactionMetricCollector(TransactionMetric transactionMetric) {
        this.transactionMetric = transactionMetric;
    }

    @Override
    public TTransaction collect() {
        TTransaction transaction = new TTransaction();
        transaction.setSampledNewCount(transactionMetric.sampledNew());
        transaction.setSampledContinuationCount(transactionMetric.sampledContinuation());
        transaction.setUnsampledNewCount(transactionMetric.unsampledNew());
        transaction.setUnsampledContinuationCount(transactionMetric.unsampledContinuation());
        return transaction;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultTransactionMetricCollector{");
        sb.append("transactionMetric=").append(transactionMetric);
        sb.append('}');
        return sb.toString();
    }
}
