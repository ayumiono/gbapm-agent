package com.gb.apm.profiler.plugin;

import java.lang.instrument.ClassFileTransformer;
import java.util.ArrayList;
import java.util.List;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.matcher.Matcher;
import com.gb.apm.bootstrap.core.instrument.matcher.Matchers;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.profiler.util.JavaAssistUtils;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ClassFileTransformerLoader {

    private final ProfilerConfig profilerConfig;
    private final DynamicTransformTrigger dynamicTransformTrigger;

    private final List<ClassFileTransformer> classTransformers = new ArrayList<ClassFileTransformer>();

    public ClassFileTransformerLoader(ProfilerConfig profilerConfig, DynamicTransformTrigger dynamicTransformTrigger) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.dynamicTransformTrigger = dynamicTransformTrigger;
    }

    public void addClassFileTransformer(InstrumentContext instrumentContext, final String targetClassName, final TransformCallback transformCallback) {
        if (targetClassName == null) {
            throw new NullPointerException("targetClassName must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

//        final Matcher matcher = Matchers.newClassNameMatcher(JavaAssistUtils.javaNameToJvmName(targetClassName));
        final Matcher matcher = Matchers.newClassNameMatcher(targetClassName);
        final MatchableClassFileTransformerGuardDelegate guard = new MatchableClassFileTransformerGuardDelegate(profilerConfig, instrumentContext, matcher, transformCallback);
        classTransformers.add(guard);
    }
    
    public void addClassFileTransformer(InstrumentContext instrumentContext, final Matcher matcher, final TransformCallback transformCallback) {
        if (matcher == null) {
            throw new NullPointerException("matcher must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

        final MatchableClassFileTransformerGuardDelegate guard = new MatchableClassFileTransformerGuardDelegate(profilerConfig, instrumentContext, matcher, transformCallback);
        classTransformers.add(guard);
    }

    public void addClassFileTransformer(InstrumentContext instrumentContext, ClassLoader classLoader, String targetClassName, final TransformCallback transformCallback) {
        if (targetClassName == null) {
            throw new NullPointerException("targetClassName must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }

        final ClassFileTransformerGuardDelegate classFileTransformerGuardDelegate = new ClassFileTransformerGuardDelegate(profilerConfig, instrumentContext, transformCallback);

        this.dynamicTransformTrigger.addClassFileTransformer(classLoader, targetClassName, classFileTransformerGuardDelegate);
    }

    public List<ClassFileTransformer> getClassTransformerList() {
        return classTransformers;
    }
}
