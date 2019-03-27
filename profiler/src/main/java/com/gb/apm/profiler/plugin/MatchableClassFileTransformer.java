package com.gb.apm.profiler.plugin;

import java.lang.instrument.ClassFileTransformer;

import com.gb.apm.bootstrap.core.instrument.matcher.Matchable;


public interface MatchableClassFileTransformer extends ClassFileTransformer, Matchable {
    
}
