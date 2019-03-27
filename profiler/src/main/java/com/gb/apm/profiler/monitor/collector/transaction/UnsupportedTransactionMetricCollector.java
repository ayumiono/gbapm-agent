package com.gb.apm.profiler.monitor.collector.transaction;

import com.gb.apm.model.TTransaction;

/**
 * @author HyunGil Jeong
 */
public class UnsupportedTransactionMetricCollector implements TransactionMetricCollector {

    @Override
    public TTransaction collect() {
        return null;
    }

    @Override
    public String toString() {
        return "UnsupportedTransactionMetricCollector";
    }
}
