package com.gb.apm.profiler.context.module;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.List;

import com.gb.apm.asm.apiadapter.InstrumentEngine;
import com.gb.apm.asm.interceptor.registry.InterceptorRegistryBinder;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ObjectBinderFactory;
import com.gb.apm.bootstrap.AgentOption;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.context.TraceFactory;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformTrigger;
import com.gb.apm.bootstrap.core.instrument.DynamicTransformerRegistry;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcContext;
import com.gb.apm.collector.DataSender;
import com.gb.apm.collector.EnhancedDataSender;
import com.gb.apm.collector.kafka.KafkaDataSenderFactory;
import com.gb.apm.collector.log.LogDataSenderFactory;
import com.gb.apm.common.service.ServiceTypeRegistryService;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.dapper.context.Sampler;
import com.gb.apm.dapper.context.SpanFactory;
import com.gb.apm.profiler.AgentInformation;
import com.gb.apm.profiler.ClassFileTransformerDispatcher;
import com.gb.apm.profiler.DefaultDynamicTransformerRegistry;
import com.gb.apm.profiler.context.CallStackFactory;
import com.gb.apm.profiler.context.DefaultCallStackFactory;
import com.gb.apm.profiler.context.DefaultSpanFactory;
import com.gb.apm.profiler.context.active.ActiveTraceRepository;
import com.gb.apm.profiler.context.id.AsyncIdGenerator;
import com.gb.apm.profiler.context.id.AtomicIdGenerator;
import com.gb.apm.profiler.context.id.DefaultAsyncIdGenerator;
import com.gb.apm.profiler.context.id.DefaultTraceIdFactory;
import com.gb.apm.profiler.context.id.DefaultTransactionCounter;
import com.gb.apm.profiler.context.id.IdGenerator;
import com.gb.apm.profiler.context.id.TraceIdFactory;
import com.gb.apm.profiler.context.id.TransactionCounter;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryService;
import com.gb.apm.profiler.context.monitor.DefaultJdbcContext;
import com.gb.apm.profiler.context.monitor.JdbcUrlParsingService;
import com.gb.apm.profiler.context.provider.ActiveTraceRepositoryProvider;
import com.gb.apm.profiler.context.provider.AgentInformationProvider;
import com.gb.apm.profiler.context.provider.AgentStartTimeProvider;
import com.gb.apm.profiler.context.provider.ApiMetaDataServiceProvider;
import com.gb.apm.profiler.context.provider.ApplicationServerTypeProvider;
import com.gb.apm.profiler.context.provider.ClassFileTransformerDispatcherProvider;
import com.gb.apm.profiler.context.provider.DataSourceMonitorRegistryServiceProvider;
import com.gb.apm.profiler.context.provider.DynamicTransformTriggerProvider;
import com.gb.apm.profiler.context.provider.InstrumentEngineProvider;
import com.gb.apm.profiler.context.provider.JdbcUrlParsingServiceProvider;
import com.gb.apm.profiler.context.provider.KafkaDataSenderProvider;
import com.gb.apm.profiler.context.provider.LogDataSenderProvider;
import com.gb.apm.profiler.context.provider.ObjectBinderFactoryProvider;
import com.gb.apm.profiler.context.provider.PluginContextLoadResultProvider;
import com.gb.apm.profiler.context.provider.SamplerProvider;
import com.gb.apm.profiler.context.provider.Slf4jJVMMetricsReporterProvider;
import com.gb.apm.profiler.context.provider.StorageFactoryProvider;
import com.gb.apm.profiler.context.provider.TcpEnhancedDataSenderProvider;
import com.gb.apm.profiler.context.provider.TraceContextProvider;
import com.gb.apm.profiler.context.provider.TraceFactoryProvider;
import com.gb.apm.profiler.context.recorder.DefaultRecorderFactory;
import com.gb.apm.profiler.context.recorder.RecorderFactory;
import com.gb.apm.profiler.context.storage.StorageFactory;
import com.gb.apm.profiler.metadata.DefaultSqlMetaDataService;
import com.gb.apm.profiler.metadata.DefaultStringMetaDataService;
import com.gb.apm.profiler.metadata.SqlMetaDataService;
import com.gb.apm.profiler.metadata.StringMetaDataService;
import com.gb.apm.profiler.monitor.jvm.Slf4jJVMMetricsReporter;
import com.gb.apm.profiler.plugin.PluginContextLoadResult;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;


/**
 * 使用原gb_monitor_client中的jvm监控代码替换原生的agent stat 监控
 * 
 * @author Woonduk Kang(emeroad)
 */
public class ApplicationContextModule extends AbstractModule {
    private final ProfilerConfig profilerConfig;
    private final ServiceTypeRegistryService serviceTypeRegistryService;
    private final AgentOption agentOption;
    private final InterceptorRegistryBinder interceptorRegistryBinder;

    public ApplicationContextModule(AgentOption agentOption, ProfilerConfig profilerConfig,
                                    ServiceTypeRegistryService serviceTypeRegistryService, InterceptorRegistryBinder interceptorRegistryBinder) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        this.agentOption = agentOption;
        this.profilerConfig = profilerConfig;
        this.serviceTypeRegistryService = serviceTypeRegistryService;
        this.interceptorRegistryBinder = interceptorRegistryBinder;
    }

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        binder().requireAtInjectOnConstructors();
        binder().disableCircularProxies();

        bind(ProfilerConfig.class).toInstance(profilerConfig);
        bind(ServiceTypeRegistryService.class).toInstance(serviceTypeRegistryService);
        bind(AgentOption.class).toInstance(agentOption);
        bind(Instrumentation.class).toInstance(agentOption.getInstrumentation());
        bind(InterceptorRegistryBinder.class).toInstance(interceptorRegistryBinder);

        bind(URL[].class).annotatedWith(PluginJars.class).toInstance(agentOption.getPluginJars());

        TypeLiteral<List<String>> listString = new TypeLiteral<List<String>>() {};
        bind(listString).annotatedWith(BootstrapJarPaths.class).toInstance(agentOption.getBootstrapJarPaths());

        bindAgentInformation(agentOption.getAgentId(), agentOption.getApplicationName());

        bindDataTransferComponent();
        
//        bind(ServerMetaDataHolder.class).toProvider(ServerMetaDataHolderProvider.class).in(Scopes.SINGLETON);
        bind(StorageFactory.class).toProvider(StorageFactoryProvider.class).in(Scopes.SINGLETON);

        bindServiceComponent();

        bind(DataSourceMonitorRegistryService.class).toProvider(DataSourceMonitorRegistryServiceProvider.class).in(Scopes.SINGLETON);

        bind(IdGenerator.class).to(AtomicIdGenerator.class).in(Scopes.SINGLETON);
        bind(AsyncIdGenerator.class).to(DefaultAsyncIdGenerator.class).in(Scopes.SINGLETON);
        bind(TransactionCounter.class).to(DefaultTransactionCounter.class).in(Scopes.SINGLETON);

        bind(Sampler.class).toProvider(SamplerProvider.class).in(Scopes.SINGLETON);

        bind(TraceContext.class).toProvider(TraceContextProvider.class).in(Scopes.SINGLETON);

        bindTraceComponent();

        bind(ActiveTraceRepository.class).toProvider(ActiveTraceRepositoryProvider.class).in(Scopes.SINGLETON);

        bind(PluginContextLoadResult.class).toProvider(PluginContextLoadResultProvider.class).in(Scopes.SINGLETON);

        bind(JdbcContext.class).to(DefaultJdbcContext.class).in(Scopes.SINGLETON);
        bind(JdbcUrlParsingService.class).toProvider(JdbcUrlParsingServiceProvider.class).in(Scopes.SINGLETON);

        bind(AgentInformation.class).toProvider(AgentInformationProvider.class).in(Scopes.SINGLETON);

        bind(InstrumentEngine.class).toProvider(InstrumentEngineProvider.class).in(Scopes.SINGLETON);
        bind(ObjectBinderFactory.class).toProvider(ObjectBinderFactoryProvider.class).in(Scopes.SINGLETON);
        bind(ClassFileTransformerDispatcher.class).toProvider(ClassFileTransformerDispatcherProvider.class).in(Scopes.SINGLETON);
        bind(DynamicTransformerRegistry.class).to(DefaultDynamicTransformerRegistry.class).in(Scopes.SINGLETON);
        bind(DynamicTransformTrigger.class).toProvider(DynamicTransformTriggerProvider.class).in(Scopes.SINGLETON);

//        bindAgentStatComponent();

//        bind(JvmInformation.class).toProvider(JvmInformationProvider.class).in(Scopes.SINGLETON);
//        bind(AgentInfoSender.class).toProvider(AgentInfoSenderProvider.class).in(Scopes.SINGLETON);
//        bind(AgentStatMonitor.class).to(DefaultAgentStatMonitor.class).in(Scopes.SINGLETON);
        
        
        //FIXME
        String jvminfoPath = this.profilerConfig.readString("profiler.agent.info.log", null);
		if(jvminfoPath != null) {
			bindJVMMetricsReporter();
		}
    }

    private void bindTraceComponent() {
        bind(TraceIdFactory.class).to(DefaultTraceIdFactory.class).in(Scopes.SINGLETON);
        bind(CallStackFactory.class).to(DefaultCallStackFactory.class).in(Scopes.SINGLETON);

        bind(SpanFactory.class).to(DefaultSpanFactory.class).in(Scopes.SINGLETON);
        bind(RecorderFactory.class).to(DefaultRecorderFactory.class).in(Scopes.SINGLETON);

        bind(TraceFactory.class).toProvider(TraceFactoryProvider.class).in(Scopes.SINGLETON);
    }

    private void bindDataTransferComponent() {
    	
    	bind(EnhancedDataSender.class).toProvider(TcpEnhancedDataSenderProvider.class).in(Scopes.SINGLETON);
    	
    	if(this.profilerConfig.readString("kafka.brokers", null) != null && this.profilerConfig.readString("kafka.topic", null) != null) {
    		bind(KafkaDataSenderFactory.class).toInstance(new KafkaDataSenderFactory(profilerConfig, agentOption));
    		bind(DataSender.class).annotatedWith(SpanDataSender.class)
        		.toProvider(KafkaDataSenderProvider.class).in(Scopes.SINGLETON);
    	}else {
    		bind(LogDataSenderFactory.class).toInstance(new LogDataSenderFactory(profilerConfig));
    		bind(DataSender.class).annotatedWith(SpanDataSender.class)
            	.toProvider(LogDataSenderProvider.class).in(Scopes.SINGLETON);
    	}
//        bind(DataSender.class).annotatedWith(StatDataSender.class)
//        		.toProvider(AgentInfoDataSenderProvider.class).in(Scopes.SINGLETON);
    }

    private void bindServiceComponent() {

        bind(StringMetaDataService.class).to(DefaultStringMetaDataService.class).in(Scopes.SINGLETON);
        bind(ApiMetaDataService.class).toProvider(ApiMetaDataServiceProvider.class).in(Scopes.SINGLETON);
        bind(SqlMetaDataService.class).to(DefaultSqlMetaDataService.class).in(Scopes.SINGLETON);
    }

    private void bindAgentInformation(String agentId, String applicationName) {
        bind(String.class).annotatedWith(AgentId.class).toInstance(agentId);
        bind(String.class).annotatedWith(ApplicationName.class).toInstance(applicationName);
        bind(Long.class).annotatedWith(AgentStartTime.class).toProvider(AgentStartTimeProvider.class).in(Scopes.SINGLETON);
        bind(ServiceType.class).annotatedWith(ApplicationServerType.class).toProvider(ApplicationServerTypeProvider.class).in(Scopes.SINGLETON);
    }

//    private void bindAgentStatComponent() {
//        bind(MemoryMetric.class).toProvider(MemoryMetricProvider.class).in(Scopes.SINGLETON);
//        bind(GarbageCollectorMetric.class).toProvider(GarbageCollectorMetricProvider.class).in(Scopes.SINGLETON);
//        bind(JvmGcMetricCollector.class).toProvider(JvmGcMetricCollectorProvider.class).in(Scopes.SINGLETON);

//        bind(CpuLoadMetric.class).toProvider(CpuLoadMetricProvider.class).in(Scopes.SINGLETON);
//        bind(CpuLoadMetricCollector.class).toProvider(CpuLoadMetricCollectorProvider.class).in(Scopes.SINGLETON);

//        bind(TransactionMetric.class).toProvider(TransactionMetricProvider.class).in(Scopes.SINGLETON);
//        bind(TransactionMetricCollector.class).toProvider(TransactionMetricCollectorProvider.class).in(Scopes.SINGLETON);
//
//        bind(ActiveTraceMetric.class).toProvider(ActiveTraceMetricProvider.class).in(Scopes.SINGLETON);
//        bind(ActiveTraceMetricCollector.class).toProvider(ActiveTraceMetricCollectorProvider.class).in(Scopes.SINGLETON);

//        bind(DataSourceMetric.class).toProvider(DataSourceMetricProvider.class).in(Scopes.SINGLETON);
//        bind(DataSourceMetricCollector.class).toProvider(DataSourceMetricCollectorProvider.class).in(Scopes.SINGLETON);

//        bind(new TypeLiteral<AgentStatMetricCollector<TAgentStat>>() {})
//                .annotatedWith(Names.named("AgentStatCollector"))
//                .to(AgentStatCollector.class).in(Scopes.SINGLETON);
//    }
    
    private void bindJVMMetricsReporter() {
    	bind(Slf4jJVMMetricsReporter.class).toProvider(Slf4jJVMMetricsReporterProvider.class).in(Scopes.SINGLETON);
    }
}