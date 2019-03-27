package com.gb.apm.profiler.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Woonduk Kang(emeroad)
 */
public class PluginConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String PINPOINT_PLUGIN_PACKAGE = "Pinpoint-Plugin-Package";
    public static final List<String> DEFAULT_PINPOINT_PLUGIN_PACKAGE_NAME = Collections.singletonList("com.navercorp.pinpoint.plugin");

    private final URL pluginJar;
    private final JarFile pluginJarFile;
    private String pluginJarURLExternalForm;

    private final ClassNameFilter pluginPackageFilter;

    public PluginConfig(URL pluginJar, ClassNameFilter pluginPackageFilter) {
        if (pluginJar == null) {
            throw new NullPointerException("pluginJar must not be null");
        }
        this.pluginJar = pluginJar;
        this.pluginJarFile = createJarFile(pluginJar);

        this.pluginPackageFilter = pluginPackageFilter;
    }


    public URL getPluginJar() {
        return pluginJar;
    }

    public JarFile getPluginJarFile() {
        return pluginJarFile;
    }

    public String getPluginJarURLExternalForm() {
        if (this.pluginJarURLExternalForm == null) {
            this.pluginJarURLExternalForm = pluginJar.toExternalForm();
        }
        return this.pluginJarURLExternalForm;
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


    public ClassNameFilter getPluginPackageFilter() {
        return pluginPackageFilter;
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                "pluginJar=" + pluginJar +
                ", pluginJarFile=" + pluginJarFile +
                ", pluginJarURLExternalForm='" + pluginJarURLExternalForm + '\'' +
                ", pluginPackageFilter=" + pluginPackageFilter +
                '}';
    }
}