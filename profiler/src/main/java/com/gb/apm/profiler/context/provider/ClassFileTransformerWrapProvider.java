package com.gb.apm.profiler.context.provider;

import java.lang.instrument.ClassFileTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.profiler.ClassFileTransformerDispatcher;
import com.gb.apm.profiler.instrument.ASMBytecodeDumpService;
import com.gb.apm.profiler.instrument.transformer.BytecodeDumpTransformer;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ClassFileTransformerWrapProvider implements Provider<ClassFileTransformer> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfilerConfig profilerConfig;
    private final Provider<ClassFileTransformerDispatcher> classFileTransformerDispatcherProvider;

    @Inject
    public ClassFileTransformerWrapProvider(ProfilerConfig profilerConfig, Provider<ClassFileTransformerDispatcher> classFileTransformerDispatcherProvider) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (classFileTransformerDispatcherProvider == null) {
            throw new NullPointerException("classFileTransformerDispatcherProvider must not be null");
        }

        this.profilerConfig = profilerConfig;
        this.classFileTransformerDispatcherProvider = classFileTransformerDispatcherProvider;
    }


    public ClassFileTransformer get() {

        ClassFileTransformerDispatcher classFileTransformerDispatcher = classFileTransformerDispatcherProvider.get();
        final boolean enableBytecodeDump = profilerConfig.readBoolean(ASMBytecodeDumpService.ENABLE_BYTECODE_DUMP, ASMBytecodeDumpService.ENABLE_BYTECODE_DUMP_DEFAULT_VALUE);
        if (enableBytecodeDump) {
            logger.info("wrapBytecodeDumpTransformer");
            return BytecodeDumpTransformer.wrap(classFileTransformerDispatcher, profilerConfig);
        }
        return classFileTransformerDispatcher;
    }
}
