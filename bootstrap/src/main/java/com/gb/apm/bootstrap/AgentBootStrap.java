package com.gb.apm.bootstrap;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import com.gb.apm.ProductInfo;

/**
 * 
 * @author xuelong.chen
 *
 */
public class AgentBootStrap {
	
	private static final BootLogger logger = BootLogger.getLogger(AgentBootStrap.class.getName());
	
	private static final LoadState STATE = new LoadState();
	
	public static void premain(String agentArgs, Instrumentation instrumentation) {
		if (agentArgs == null) {
            agentArgs = "";
        }
        logger.info(ProductInfo.NAME + " agentArgs:" + agentArgs);

        final boolean success = STATE.start();
        if (!success) {
            logger.warn("pinpoint-bootstrap already started. skipping agent loading.");
            return;
        }
        Map<String, String> agentArgsMap = argsToMap(agentArgs);
        
        final ClassPathResolver classPathResolver = new AgentDirBaseClassPathResolver();
        if (!classPathResolver.verify()) {
            logger.warn("Agent Directory Verify fail. skipping agent loading.");
            logPinpointAgentLoadFail();
            return;
        }
        

        BootstrapJarFile bootstrapJarFile = classPathResolver.getBootstrapJarFile();
        appendToBootstrapClassLoader(instrumentation, bootstrapJarFile);
        
        AgentStarter bootStrap = new AgentStarter(agentArgsMap, bootstrapJarFile, classPathResolver, instrumentation);
        
        if (!bootStrap.start()) {
            logPinpointAgentLoadFail();
        }
	}
	
	
	private static void appendToBootstrapClassLoader(Instrumentation instrumentation, BootstrapJarFile agentJarFile) {
        List<JarFile> jarFileList = agentJarFile.getJarFileList();
        for (JarFile jarFile : jarFileList) {
            logger.info("appendToBootstrapClassLoader:" + jarFile.getName());
            instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
        }
    }
	
	private static Map<String, String> argsToMap(String agentArgs) {
        ArgsParser argsParser = new ArgsParser();
        Map<String, String> agentArgsMap = argsParser.parse(agentArgs);
        if (!agentArgsMap.isEmpty()) {
            logger.info("agentParameter :" + agentArgs);
        }
        return agentArgsMap;
    }
	
	private static void logPinpointAgentLoadFail() {
        final String errorLog =
                "*****************************************************************************\n" +
                        "* Pinpoint Agent load failure\n" +
                        "*****************************************************************************";
        System.err.println(errorLog);
    }
}
