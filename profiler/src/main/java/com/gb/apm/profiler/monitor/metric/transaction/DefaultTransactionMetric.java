package com.gb.apm.profiler.monitor.metric.transaction;

import com.codahale.metrics.Gauge;
import com.gb.apm.profiler.context.id.TransactionCounter;

/**
 * @author HyunGil Jeong
 */
public class DefaultTransactionMetric implements TransactionMetric {

    private final Gauge<Long> sampledNewGauge;
    private final Gauge<Long> sampledContinuationGauge;
    private final Gauge<Long> unsampledNewGauge;
    private final Gauge<Long> unsampledContinuationGauge;

    public DefaultTransactionMetric(final TransactionCounter transactionCounter) {
        if (transactionCounter == null) {
            throw new NullPointerException("transactionCounter must not be null");
        }
        sampledNewGauge = TransactionGauge.wrap(new LongGauge() {
            @Override
            public long getValue() {
                return transactionCounter.getSampledNewCount();
            }
        });
        sampledContinuationGauge = TransactionGauge.wrap(new LongGauge() {
            @Override
            public long getValue() {
                return transactionCounter.getSampledContinuationCount();
            }
        });
        unsampledNewGauge = TransactionGauge.wrap(new LongGauge() {
            @Override
            public long getValue() {
                return transactionCounter.getUnSampledNewCount();
            }
        });
        unsampledContinuationGauge = TransactionGauge.wrap(new LongGauge() {
            @Override
            public long getValue() {
                return transactionCounter.getUnSampledContinuationCount();
            }
        });
    }

    @Override
    public Long sampledNew() {
        return sampledNewGauge.getValue();
    }

    @Override
    public Long sampledContinuation() {
        return sampledContinuationGauge.getValue();
    }

    @Override
    public Long unsampledNew() {
        return unsampledNewGauge.getValue();
    }

    @Override
    public Long unsampledContinuation() {
        return unsampledContinuationGauge.getValue();
    }

    @Override
    public String toString() {
        return "Default TransactionMetric";
    }

    private interface LongGauge {
        long getValue();
    }

    private static class TransactionGauge implements Gauge<Long> {
        private static final long UNINITIALIZED = -1L;

        private long prevTransactionCount = UNINITIALIZED;
        private final LongGauge longGauge;

        static TransactionGauge wrap(LongGauge longGauge) {
            return new TransactionGauge(longGauge);
        }

        private TransactionGauge(LongGauge longGauge) {
            if (longGauge == null) {
                throw new NullPointerException("longGauge must not be null");
            }
            this.longGauge = longGauge;
        }

        @Override
        public final Long getValue() {
            final long transactionCount = longGauge.getValue();
            if (transactionCount < 0) {
                return 0L;
            }
            if (this.prevTransactionCount == UNINITIALIZED) {
                this.prevTransactionCount = transactionCount;
                return 0L;
            }
            final long transactionCountDelta = transactionCount - this.prevTransactionCount;
            this.prevTransactionCount = transactionCount;
            return transactionCountDelta;
        }

    }
}
