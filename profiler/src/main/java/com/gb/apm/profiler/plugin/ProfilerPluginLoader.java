package com.gb.apm.profiler.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.plugin.ProfilerPlugin;
import com.gb.apm.common.plugin.PluginLoader;
import com.gb.apm.common.utils.StringUtils;
import com.gb.apm.profiler.instrument.classloading.ClassInjector;
import com.gb.apm.profiler.instrument.classloading.JarProfilerPluginClassInjector;

/**
 * @author Jongho Moon
 *
 */
public class ProfilerPluginLoader {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ClassNameFilter profilerPackageFilter = new PinpointProfilerPackageSkipFilter();

    private final ProfilerConfig profilerConfig;
    private final PluginSetup pluginSetup;
    private final InstrumentEngine instrumentEngine;


    public ProfilerPluginLoader(ProfilerConfig profilerConfig, PluginSetup pluginSetup, InstrumentEngine instrumentEngine) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (pluginSetup == null) {
            throw new NullPointerException("pluginSetup must not be null");
        }
        if (instrumentEngine == null) {
            throw new NullPointerException("instrumentEngine must not be null");
        }


        this.profilerConfig = profilerConfig;
        this.pluginSetup = pluginSetup;
        this.instrumentEngine = instrumentEngine;

    }

    public List<SetupResult> load(URL[] pluginJars) {

        List<SetupResult> pluginContexts = new ArrayList<SetupResult>(pluginJars.length);

        for (URL pluginJar : pluginJars) {

            final JarFile pluginJarFile = createJarFile(pluginJar);
            final List<String> pluginPackageList = getPluginPackage(pluginJarFile);

            final ClassNameFilter pluginFilterChain = createPluginFilterChain(pluginPackageList);

            final List<ProfilerPlugin> original = PluginLoader.load(ProfilerPlugin.class, new URL[] { pluginJar });

            List<ProfilerPlugin> plugins = filterDisablePlugin(original);

            for (ProfilerPlugin plugin : plugins) {
                 if (logger.isInfoEnabled()) {
                    logger.info("{} Plugin {}:{}", plugin.getClass(), PluginConfig.PINPOINT_PLUGIN_PACKAGE, pluginPackageList);
                }
                
                logger.info("Loading plugin:{} pluginPackage:{}", plugin.getClass().getName(), plugin);

                PluginConfig pluginConfig = new PluginConfig(pluginJar, pluginFilterChain);
                final ClassInjector classInjector = new JarProfilerPluginClassInjector(pluginConfig, instrumentEngine);
                final SetupResult result = pluginSetup.setupPlugin(plugin, classInjector);
                pluginContexts.add(result);
            }
        }
        

        return pluginContexts;
    }

    private List<ProfilerPlugin> filterDisablePlugin(List<ProfilerPlugin> plugins) {

        List<String> disabled = profilerConfig.getDisabledPlugins();

        List<ProfilerPlugin> result = new ArrayList<ProfilerPlugin>();
        for (ProfilerPlugin plugin : plugins) {
            if (disabled.contains(plugin.getClass().getName())) {
                logger.info("Skip disabled plugin: {}", plugin.getClass().getName());
                continue;
            }
            result.add(plugin);
        }
        return result;
    }

    private ClassNameFilter createPluginFilterChain(List<String> packageList) {

        final ClassNameFilter pluginPackageFilter = new PluginPackageFilter(packageList);

        final List<ClassNameFilter> chain = Arrays.asList(profilerPackageFilter, pluginPackageFilter);

        final ClassNameFilter filterChain = new ClassNameFilterChain(chain);

        return filterChain;
    }

    private JarFile createJarFile(URL pluginJar) {
        try {
            final URI uri = pluginJar.toURI();
            return new JarFile(new File(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException("URISyntax error. " + e.getCause(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO error. " + e.getCause(), e);
        }
    }
    private Manifest getManifest(JarFile pluginJarFile) {
        try {
            return pluginJarFile.getManifest();
        } catch (IOException ex) {
            logger.info("{} IoError :{}", pluginJarFile.getName(), ex.getMessage(), ex);
            return null;
        }
    }

    public List<String> getPluginPackage(JarFile pluginJarFile) {

        final Manifest manifest =  getManifest(pluginJarFile);
        if (manifest == null) {
            return PluginConfig.DEFAULT_PINPOINT_PLUGIN_PACKAGE_NAME;
        }

        final Attributes attributes = manifest.getMainAttributes();
        final String pluginPackage = attributes.getValue(PluginConfig.PINPOINT_PLUGIN_PACKAGE);
        if (pluginPackage == null) {
            return PluginConfig.DEFAULT_PINPOINT_PLUGIN_PACKAGE_NAME;
        }
        return StringUtils.splitAndTrim(pluginPackage, ",");
    }


}
