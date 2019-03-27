package com.gb.apm.profiler.plugin;

import java.lang.instrument.ClassFileTransformer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.plugin.ApplicationTypeDetector;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultPluginContextLoadResult implements PluginContextLoadResult {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final URL[] pluginJars;
    private final InstrumentEngine instrumentEngine;

    private final ProfilerConfig profilerConfig;
    private final DynamicTransformTrigger dynamicTransformTrigger;

    private final List<SetupResult> setupResultList;

    public DefaultPluginContextLoadResult(ProfilerConfig profilerConfig, DynamicTransformTrigger dynamicTransformTrigger, InstrumentEngine instrumentEngine,
                                          URL[] pluginJars) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (dynamicTransformTrigger == null) {
            throw new NullPointerException("dynamicTransformTrigger must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }
        if (pluginJars == null) {
            throw new NullPointerException("pluginJars must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.dynamicTransformTrigger = dynamicTransformTrigger;

        this.pluginJars = pluginJars;
        this.instrumentEngine = instrumentEngine;
        this.setupResultList = load();
    }




    private List<SetupResult> load() {
        logger.info("load plugin");
        PluginSetup pluginSetup = new DefaultPluginSetup(profilerConfig, instrumentEngine, dynamicTransformTrigger);
        final ProfilerPluginLoader loader = new ProfilerPluginLoader(profilerConfig, pluginSetup, instrumentEngine);
        List<SetupResult> load = loader.load(pluginJars);
        return load;
    }

    @Override
    public List<ClassFileTransformer> getClassFileTransformer() {
        // TODO Need plugin context level grouping
        final List<ClassFileTransformer> transformerList = new ArrayList<ClassFileTransformer>();
        for (SetupResult pluginContext : setupResultList) {
            List<ClassFileTransformer> classTransformerList = pluginContext.getClassTransformerList();
            transformerList.addAll(classTransformerList);
        }
        return transformerList;
    }



    @Override
    public List<ApplicationTypeDetector> getApplicationTypeDetectorList() {

        final List<ApplicationTypeDetector> registeredDetectors = new ArrayList<ApplicationTypeDetector>();

        for (SetupResult context : setupResultList) {
            List<ApplicationTypeDetector> applicationTypeDetectors = context.getApplicationTypeDetectors();
            registeredDetectors.addAll(applicationTypeDetectors);
        }

        return registeredDetectors;
    }

    @Override
    public List<JdbcUrlParserV2> getJdbcUrlParserList() {
        final List<JdbcUrlParserV2> result = new ArrayList<JdbcUrlParserV2>();

        for (SetupResult context : setupResultList) {
            List<JdbcUrlParserV2> jdbcUrlParserList = context.getJdbcUrlParserList();
            result.addAll(jdbcUrlParserList);
        }

        return result;
    }

}
