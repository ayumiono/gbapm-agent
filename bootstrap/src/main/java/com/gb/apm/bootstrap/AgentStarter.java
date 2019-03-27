package com.gb.apm.bootstrap;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gb.apm.ProductInfo;
import com.gb.apm.Version;
import com.gb.apm.bootstrap.core.Agent;
import com.gb.apm.bootstrap.core.config.DefaultProfilerConfig;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.common.service.AnnotationKeyRegistryService;
import com.gb.apm.common.service.DefaultAnnotationKeyRegistryService;
import com.gb.apm.common.service.DefaultServiceTypeRegistryService;
import com.gb.apm.common.service.DefaultTraceMetadataLoaderService;
import com.gb.apm.common.service.ServiceTypeRegistryService;
import com.gb.apm.common.service.TraceMetadataLoaderService;
import com.gb.apm.common.utils.PinpointThreadFactory;
import com.gb.apm.common.utils.SimpleProperty;
import com.gb.apm.common.utils.SystemProperty;
import com.gb.apm.common.utils.logger.CommonLoggerFactory;
import com.gb.apm.common.utils.logger.StdoutCommonLoggerFactory;

public class AgentStarter {

	private final BootLogger logger = BootLogger.getLogger(AgentStarter.class.getName());

	public static final String AGENT_TYPE = "AGENT_TYPE";

	public static final String DEFAULT_AGENT = "DEFAULT_AGENT";
	public static final String BOOT_CLASS = "com.gb.apm.profiler.DefaultAgent";

	private SimpleProperty systemProperty = SystemProperty.INSTANCE;

	private final Map<String, String> agentArgs;
	private final BootstrapJarFile bootstrapJarFile;
	private final ClassPathResolver classPathResolver;
	private final Instrumentation instrumentation;

	public AgentStarter(Map<String, String> agentArgs, BootstrapJarFile bootstrapJarFile,
			ClassPathResolver classPathResolver, Instrumentation instrumentation) {
		if (agentArgs == null) {
			throw new NullPointerException("agentArgs must not be null");
		}
		if (bootstrapJarFile == null) {
			throw new NullPointerException("bootstrapJarFile must not be null");
		}
		if (classPathResolver == null) {
			throw new NullPointerException("classPathResolver must not be null");
		}
		if (instrumentation == null) {
			throw new NullPointerException("instrumentation must not be null");
		}
		this.agentArgs = agentArgs;
		this.bootstrapJarFile = bootstrapJarFile;
		this.classPathResolver = classPathResolver;
		this.instrumentation = instrumentation;
	}

	public boolean start() {
		final IdValidator idValidator = new IdValidator();
		final String agentId = idValidator.getAgentId();
		if (agentId == null) {
			return false;
		}
		final String applicationName = idValidator.getApplicationName();
		if (applicationName == null) {
			return false;
		}

		URL[] pluginJars = classPathResolver.resolvePlugins();
		
		// TODO using PLogger instead of CommonLogger
        CommonLoggerFactory loggerFactory = StdoutCommonLoggerFactory.INSTANCE;
        TraceMetadataLoaderService typeLoaderService = new DefaultTraceMetadataLoaderService(pluginJars, loggerFactory);
        ServiceTypeRegistryService serviceTypeRegistryService = new DefaultServiceTypeRegistryService(typeLoaderService, loggerFactory);
        AnnotationKeyRegistryService annotationKeyRegistryService = new DefaultAnnotationKeyRegistryService(typeLoaderService, loggerFactory);

		String configPath = getConfigPath(classPathResolver);
		if (configPath == null) {
			return false;
		}

		// set the path of log file as a system property
		saveLogFilePath(classPathResolver);

		savePinpointVersion();

		try {
			// Is it right to load the configuration in the bootstrap?
			ProfilerConfig profilerConfig = DefaultProfilerConfig.load(configPath);

			// this is the library list that must be loaded
			List<URL> libUrlList = resolveLib(classPathResolver);
			AgentClassLoader agentClassLoader = new AgentClassLoader(libUrlList.toArray(new URL[libUrlList.size()]));
			final String bootClass = getBootClass();
			agentClassLoader.setBootClass(bootClass);
			logger.info("pinpoint agent [" + bootClass + "] starting...");

			AgentOption option = createAgentOption(agentId, applicationName, profilerConfig, instrumentation,
					pluginJars, bootstrapJarFile, serviceTypeRegistryService, annotationKeyRegistryService);
			Agent pinpointAgent = agentClassLoader.boot(option);
			pinpointAgent.start();
			registerShutdownHook(pinpointAgent);
			logger.info("pinpoint agent started normally.");
		} catch (Exception e) {
			logger.warn(ProductInfo.NAME + " start failed.", e);
			return false;
		}
		return true;
	}

	private String getConfigPath(ClassPathResolver classPathResolver) {
		final String configName = ProductInfo.NAME + ".config";
		String pinpointConfigFormSystemProperty = systemProperty.getProperty(configName);
		if (pinpointConfigFormSystemProperty != null) {
			logger.info(configName + " systemProperty found. " + pinpointConfigFormSystemProperty);
			return pinpointConfigFormSystemProperty;
		}

		String classPathAgentConfigPath = classPathResolver.getAgentConfigPath();
		if (classPathAgentConfigPath != null) {
			logger.info("classpath " + configName + " found. " + classPathAgentConfigPath);
			return classPathAgentConfigPath;
		}

		logger.info(configName + " file not found.");
		return null;
	}

	private void saveLogFilePath(ClassPathResolver classPathResolver) {
		String agentLogFilePath = classPathResolver.getAgentLogFilePath();
		logger.info("logPath:" + agentLogFilePath);

		systemProperty.setProperty(ProductInfo.NAME + ".log", agentLogFilePath);
	}

	private void savePinpointVersion() {
		logger.info("pinpoint version:" + Version.VERSION);
		systemProperty.setProperty(ProductInfo.NAME + ".version", Version.VERSION);
	}

	private List<URL> resolveLib(ClassPathResolver classPathResolver) {
		String agentJarFullPath = classPathResolver.getAgentJarFullPath();
		String agentLibPath = classPathResolver.getAgentLibPath();
		List<URL> urlList = resolveLib(classPathResolver.resolveLib());
		String agentConfigPath = classPathResolver.getAgentConfigPath();

		if (logger.isInfoEnabled()) {
			logger.info("agent JarPath:" + agentJarFullPath);
			logger.info("agent LibDir:" + agentLibPath);
			for (URL url : urlList) {
				logger.info("agent Lib:" + url);
			}
			logger.info("agent config:" + agentConfigPath);
		}

		return urlList;
	}

	private String getBootClass() {
		return BOOT_CLASS;
	}

	private String getAgentType() {
		String agentType = agentArgs.get(AGENT_TYPE);
		if (agentType == null) {
			return DEFAULT_AGENT;
		}
		return agentType;

	}

	private AgentOption createAgentOption(String agentId, String applicationName, ProfilerConfig profilerConfig,
			Instrumentation instrumentation, URL[] pluginJars, BootstrapJarFile bootstrapJarFile,
			ServiceTypeRegistryService serviceTypeRegistryService,
			AnnotationKeyRegistryService annotationKeyRegistryService) {
		List<String> bootstrapJarPaths = bootstrapJarFile.getJarNameList();
		return new DefaultAgentOption(instrumentation, agentId, applicationName, profilerConfig, pluginJars,
				bootstrapJarPaths, serviceTypeRegistryService, annotationKeyRegistryService);
	}

	private List<URL> resolveLib(List<URL> urlList) {
		if (DEFAULT_AGENT.equals(getAgentType().toUpperCase())) {
			final List<URL> releaseLib = new ArrayList<URL>(urlList.size());
			for (URL url : urlList) {
				//
				if (!url.toExternalForm().contains("pinpoint-profiler-test")) {
					releaseLib.add(url);
				}
			}
			return releaseLib;
		} else {
//			logger.info("load " + PLUGIN_TEST_AGENT + " lib");
			return urlList;
		}
	}

	private void registerShutdownHook(final Agent pinpointAgent) {
		final Runnable stop = new Runnable() {
			@Override
			public void run() {
				pinpointAgent.stop();
			}
		};
		PinpointThreadFactory pinpointThreadFactory = new PinpointThreadFactory("Pinpoint-shutdown-hook");
		Thread thread = pinpointThreadFactory.newThread(stop);
		Runtime.getRuntime().addShutdownHook(thread);
	}

}
