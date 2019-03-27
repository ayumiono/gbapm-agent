package com.gb.apm.profiler.sampler;

import com.gb.apm.dapper.context.Sampler;

/**
 * @author emeroad
 */
public class FalseSampler implements Sampler {
    @Override
    public boolean isSampling() {
        return false;
    }

    @Override
    public String toString() {
        // To fix sampler name even if the class name is changed.
        return "FalseSampler";
    }
}
