package com.gb.apm.profiler.objectfactory;

import com.gb.apm.asm.interceptor.factory.InterceptorFactory;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ArgumentProvider;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitorRegistry;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryAdaptor;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryService;
import com.gb.apm.profiler.interceptor.factory.AnnotatedInterceptorFactory;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ObjectBinderFactory implements com.gb.apm.asm.objectfactory.ObjectBinderFactory{
    private final ProfilerConfig profilerConfig;
    private final Provider<TraceContext> traceContextProvider;
    private final DataSourceMonitorRegistry dataSourceMonitorRegistry;
    private final Provider<ApiMetaDataService> apiMetaDataServiceProvider;

    public ObjectBinderFactory(ProfilerConfig profilerConfig, Provider<TraceContext> traceContextProvider, DataSourceMonitorRegistryService dataSourceMonitorRegistryService, Provider<ApiMetaDataService> apiMetaDataServiceProvider) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (traceContextProvider == null) {
            throw new NullPointerException("traceContextProvider must not be null");
        }
        if (dataSourceMonitorRegistryService == null) {
            throw new NullPointerException("dataSourceMonitorRegistryService must not be null");
        }
        if (apiMetaDataServiceProvider == null) {
            throw new NullPointerException("apiMetaDataServiceProvider must not be null");
        }

        this.profilerConfig = profilerConfig;
        this.traceContextProvider = traceContextProvider;

        this.dataSourceMonitorRegistry = new DataSourceMonitorRegistryAdaptor(dataSourceMonitorRegistryService);
        this.apiMetaDataServiceProvider = apiMetaDataServiceProvider;
    }

    public AutoBindingObjectFactory newAutoBindingObjectFactory(InstrumentContext pluginContext, ClassLoader classLoader, ArgumentProvider... argumentProviders) {
        final TraceContext traceContext = this.traceContextProvider.get();
        return new AutoBindingObjectFactory(profilerConfig, traceContext, pluginContext, classLoader, argumentProviders);
    }


    @Override
    public ArgumentProvider newInterceptorArgumentProvider(InstrumentClass instrumentClass) {
        ApiMetaDataService apiMetaDataService = this.apiMetaDataServiceProvider.get();
        return new InterceptorArgumentProvider(dataSourceMonitorRegistry, apiMetaDataService, instrumentClass);
    }

    @Override
    public InterceptorFactory newAnnotatedInterceptorFactory(InstrumentContext pluginContext, boolean exceptionHandle) {
        final TraceContext traceContext = this.traceContextProvider.get();
        ApiMetaDataService apiMetaDataService = this.apiMetaDataServiceProvider.get();
        return new AnnotatedInterceptorFactory(profilerConfig, traceContext, dataSourceMonitorRegistry, apiMetaDataService, pluginContext, exceptionHandle);
    }
}
