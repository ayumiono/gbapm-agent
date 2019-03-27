package com.gb.apm.profiler.monitor.metric.transaction;


/**
 * @author HyunGil Jeong
 */
public interface TransactionMetric {

    TransactionMetric UNSUPPORTED_TRANSACTION_METRIC = new TransactionMetric() {
        @Override
        public Long sampledNew() {
            return null;
        }

        @Override
        public Long sampledContinuation() {
            return null;
        }

        @Override
        public Long unsampledNew() {
            return null;
        }

        @Override
        public Long unsampledContinuation() {
            return null;
        }

        @Override
        public String toString() {
            return "Unsupported TransactionMetric";
        }
    };

    Long sampledNew();

    Long sampledContinuation();

    Long unsampledNew();

    Long unsampledContinuation();
}
