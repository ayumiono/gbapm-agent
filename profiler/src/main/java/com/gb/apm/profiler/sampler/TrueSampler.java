package com.gb.apm.profiler.sampler;

import com.gb.apm.dapper.context.Sampler;

/**
 * @author emeroad
 */
public class TrueSampler implements Sampler {

    @Override
    public boolean isSampling() {
        return true;
    }

    @Override
    public String toString() {
        // To fix sampler name even if the class name is changed.
        return "TrueSampler";
    }
}
