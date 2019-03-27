package com.gb.apm.profiler.plugin;

import java.lang.instrument.ClassFileTransformer;
import java.util.List;

import com.gb.apm.bootstrap.core.plugin.ApplicationTypeDetector;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;

/**
 * @author Woonduk Kang(emeroad)
 */
public class SetupResult {

    private final DefaultProfilerPluginSetupContext setupContext;
    private final ClassFileTransformerLoader transformerRegistry;

    public SetupResult(DefaultProfilerPluginSetupContext setupContext, ClassFileTransformerLoader transformerRegistry) {
        this.setupContext = setupContext;
        this.transformerRegistry = transformerRegistry;
    }


    public List<ApplicationTypeDetector> getApplicationTypeDetectors() {
        return this.setupContext.getApplicationTypeDetectors();
    }

    public List<JdbcUrlParserV2> getJdbcUrlParserList() {
        return this.setupContext.getJdbcUrlParserList();
    }

    public List<ClassFileTransformer> getClassTransformerList() {
        return transformerRegistry.getClassTransformerList();
    }


}
