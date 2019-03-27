package com.gb.apm.profiler.context.provider;

import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.profiler.context.monitor.DataSourceMonitorRegistryService;
import com.gb.apm.profiler.objectfactory.ObjectBinderFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ObjectBinderFactoryProvider implements Provider<com.gb.apm.asm.objectfactory.ObjectBinderFactory> {

    private final ProfilerConfig profilerConfig;
    private final Provider<TraceContext> traceContextProvider;
    private final DataSourceMonitorRegistryService dataSourceMonitorRegistryService;
    private final Provider<ApiMetaDataService> apiMetaDataServiceProvider;

    @Inject
    public ObjectBinderFactoryProvider(ProfilerConfig profilerConfig, Provider<TraceContext> traceContextProvider, DataSourceMonitorRegistryService dataSourceMonitorRegistryService, Provider<ApiMetaDataService> apiMetaDataServiceProvider) {
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
        this.dataSourceMonitorRegistryService = dataSourceMonitorRegistryService;
        this.apiMetaDataServiceProvider = apiMetaDataServiceProvider;
    }

    @Override
    public com.gb.apm.asm.objectfactory.ObjectBinderFactory get() {
        return new ObjectBinderFactory(profilerConfig, traceContextProvider, dataSourceMonitorRegistryService, apiMetaDataServiceProvider);
    }

}
