package com.gb.apm.bootstrap;

import java.net.URL;
import java.util.List;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface ClassPathResolver {

    boolean verify();

    BootstrapJarFile getBootstrapJarFile();

    String getPinpointCommonsJar();

    String getBootStrapCoreJar();

    String getBootStrapCoreOptionalJar();

    String getAgentJarName();

    String getAgentJarFullPath();

    String getAgentLibPath();

    String getAgentLogFilePath();

    String getAgentPluginPath();

    List<URL> resolveLib();

    URL[] resolvePlugins();

    String getAgentDirPath();

    String getAgentConfigPath();
}
