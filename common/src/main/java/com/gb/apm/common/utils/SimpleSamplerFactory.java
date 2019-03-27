package com.gb.apm.common.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author emeroad
 */
public class SimpleSamplerFactory {
    // functionally identical to profiler's Sampler  
    public static final SimpleSampler FALSE_SAMPLER = new SimpleFalseSampler();
    public static final SimpleSampler TRUE_SAMPLER = new SimpleTrueSampler();

    public static SimpleSampler createSampler(boolean sampling, int samplingRate) {
        if (!sampling || samplingRate <= 0) {
            return FALSE_SAMPLER;
        }
        if (samplingRate == 1) {
            return TRUE_SAMPLER;
        }
        return new SamplingRateSampler(samplingRate);
    }

    public static class SimpleTrueSampler implements SimpleSampler {
        @Override
        public boolean isSampling() {
            return true;
        }
    }

    public static class SimpleFalseSampler implements SimpleSampler {
        @Override
        public boolean isSampling() {
            return false;
        }
    }

    public static class SamplingRateSampler implements SimpleSampler {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final int samplingRate;

        public SamplingRateSampler(int samplingRate) {
            if (samplingRate <= 0) {
                throw new IllegalArgumentException("Invalid samplingRate " + samplingRate);
            }
            this.samplingRate = samplingRate;
        }

        @Override
        public boolean isSampling() {
            int samplingCount = MathUtils.fastAbs(counter.getAndIncrement());
            int isSampling = samplingCount % samplingRate;
            return isSampling == 0;
        }
    }
}
