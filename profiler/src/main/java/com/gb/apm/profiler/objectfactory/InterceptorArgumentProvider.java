package com.gb.apm.profiler.objectfactory;

import java.lang.annotation.Annotation;

import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ArgumentProvider;
import com.gb.apm.asm.objectfactory.Option;
import com.gb.apm.bootstrap.core.PinpointException;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.interceptor.annotation.Name;
import com.gb.apm.bootstrap.core.interceptor.annotation.NoCache;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitorRegistry;
import com.gb.apm.common.utils.TypeUtils;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author Jongho Moon
 *
 */
public class InterceptorArgumentProvider implements ArgumentProvider {
    private final DataSourceMonitorRegistry dataSourceMonitorRegistry;
    private final ApiMetaDataService apiMetaDataService;
    private final InterceptorScope interceptorScope;
    private final InstrumentClass targetClass;
    private final InstrumentMethod targetMethod;

    public InterceptorArgumentProvider(DataSourceMonitorRegistry dataSourceMonitorRegistry, 
    		ApiMetaDataService apiMetaDataService, InstrumentClass targetClass) {
        this(
        		dataSourceMonitorRegistry, 
        		apiMetaDataService, null, targetClass, null);
    }
    
    public InterceptorArgumentProvider(DataSourceMonitorRegistry dataSourceMonitorRegistry, 
    		ApiMetaDataService apiMetaDataService, InterceptorScope interceptorScope, InstrumentClass targetClass, InstrumentMethod targetMethod) {
        if (dataSourceMonitorRegistry == null) {
            throw new NullPointerException("dataSourceMonitorRegistry must not be null");
        }
        if (apiMetaDataService == null) {
            throw new NullPointerException("apiMetaDataService must not be null");
        }
        this.dataSourceMonitorRegistry = dataSourceMonitorRegistry;
        this.apiMetaDataService = apiMetaDataService;
        this.interceptorScope = interceptorScope;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
    }

    @Override
    public Option get(int index, Class<?> type, Annotation[] annotations) {
        if (type == InstrumentClass.class) {
            return Option.withValue(targetClass);
        } else if (type == MethodDescriptor.class) {
            MethodDescriptor descriptor = targetMethod.getDescriptor();
            cacheApiIfAnnotationNotPresent(annotations, descriptor);
            
            return Option.withValue(descriptor);
        } else if (type == InstrumentMethod.class) {
            return Option.withValue(targetMethod);
        } else if (type == InterceptorScope.class) {
            Name annotation = TypeUtils.findAnnotation(annotations, Name.class);
            
            if (annotation == null) {
                if (interceptorScope == null) {
                    throw new PinpointException("Scope parameter is not annotated with @Name and the target class is not associated with any Scope");
                } else {
                    return Option.withValue(interceptorScope);
                }
            } else {
                return Option.empty();
            }
        } 
        else if (type == DataSourceMonitorRegistry.class) {
            return Option.withValue(dataSourceMonitorRegistry);
        }
        
        return Option.empty();
    }

    private void cacheApiIfAnnotationNotPresent(Annotation[] annotations, MethodDescriptor descriptor) {
        Annotation annotation = TypeUtils.findAnnotation(annotations, NoCache.class);
        if (annotation == null) {
            this.apiMetaDataService.cacheApi(descriptor);
        }
    }
}
