package com.gb.apm.profiler.interceptor.factory;


import com.gb.apm.asm.apiadapter.ScopeInfo;
import com.gb.apm.asm.interceptor.ExceptionHandleApiIdAwareAroundInterceptor;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor0;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor1;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor2;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor3;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor4;
import com.gb.apm.asm.interceptor.ExceptionHandleAroundInterceptor5;
import com.gb.apm.asm.interceptor.ExceptionHandleStaticAroundInterceptor;
import com.gb.apm.asm.interceptor.factory.InterceptorFactory;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedApiIdAwareAroundInterceptor;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor0;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor1;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor2;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor3;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor4;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedInterceptor5;
import com.gb.apm.asm.interceptor.scope.ExceptionHandleScopedStaticAroundInterceptor;
import com.gb.apm.asm.interceptor.scope.ScopedApiIdAwareAroundInterceptor;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor0;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor1;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor2;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor3;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor4;
import com.gb.apm.asm.interceptor.scope.ScopedInterceptor5;
import com.gb.apm.asm.interceptor.scope.ScopedStaticAroundInterceptor;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ObjectFactory;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.context.TraceContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.interceptor.ApiIdAwareAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor0;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor1;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor2;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor3;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor4;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor5;
import com.gb.apm.bootstrap.core.interceptor.Interceptor;
import com.gb.apm.bootstrap.core.interceptor.StaticAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.bootstrap.core.plugin.monitor.DataSourceMonitorRegistry;
import com.gb.apm.profiler.objectfactory.AutoBindingObjectFactory;
import com.gb.apm.profiler.objectfactory.InterceptorArgumentProvider;
//import com.navercorp.pinpoint.bootstrap.plugin.monitor.DataSourceMonitorRegistry;

/**
 * @author Jongho Moon
 * @author jaehong.kim
 */
public class AnnotatedInterceptorFactory implements InterceptorFactory {
    private final ProfilerConfig profilerConfig;
    private final TraceContext traceContext;
    private final DataSourceMonitorRegistry dataSourceMonitorRegistry;
    private final ApiMetaDataService apiMetaDataService;
    private final InstrumentContext pluginContext;
    private final boolean exceptionHandle;

    public AnnotatedInterceptorFactory(ProfilerConfig profilerConfig, TraceContext traceContext
    		, DataSourceMonitorRegistry dataSourceMonitorRegistry
    		, ApiMetaDataService apiMetaDataService, InstrumentContext pluginContext, boolean exceptionHandle) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        if (traceContext == null) {
            throw new NullPointerException("traceContext must not be null");
        }
        if (dataSourceMonitorRegistry == null) {
            throw new NullPointerException("dataSourceMonitorRegistry must not be null");
        }
        if (apiMetaDataService == null) {
            throw new NullPointerException("apiMetaDataService must not be null");
        }
        if (pluginContext == null) {
            throw new NullPointerException("pluginContext must not be null");
        }
        this.profilerConfig = profilerConfig;
        this.traceContext = traceContext;
        this.dataSourceMonitorRegistry = dataSourceMonitorRegistry;
        this.apiMetaDataService = apiMetaDataService;
        this.pluginContext = pluginContext;
        this.exceptionHandle = exceptionHandle;
    }

    @Override
    public Interceptor getInterceptor(ClassLoader classLoader, String interceptorClassName, Object[] providedArguments, ScopeInfo scopeInfo, InstrumentClass target, InstrumentMethod targetMethod) {

        AutoBindingObjectFactory factory = new AutoBindingObjectFactory(profilerConfig, traceContext, pluginContext, classLoader);
        ObjectFactory objectFactory = ObjectFactory.byConstructor(interceptorClassName, providedArguments);
        final InterceptorScope interceptorScope = scopeInfo.getInterceptorScope();
        InterceptorArgumentProvider interceptorArgumentProvider = new InterceptorArgumentProvider(
        		dataSourceMonitorRegistry, 
        		apiMetaDataService, scopeInfo.getInterceptorScope(), target, targetMethod);

        Interceptor interceptor = (Interceptor) factory.createInstance(objectFactory, interceptorArgumentProvider);

        if (interceptorScope != null) {
            if (exceptionHandle) {
                interceptor = wrapByExceptionHandleScope(interceptor, interceptorScope, getExecutionPolicy(scopeInfo.getExecutionPolicy()));
            } else {
                interceptor = wrapByScope(interceptor, interceptorScope, getExecutionPolicy(scopeInfo.getExecutionPolicy()));
            }
        } else {
            if (exceptionHandle) {
                interceptor = wrapByExceptionHandle(interceptor);
            }
        }

        return interceptor;
    }

    private ExecutionPolicy getExecutionPolicy(ExecutionPolicy policy) {
        if (policy == null) {
            return ExecutionPolicy.BOUNDARY;
        }
        return policy;
    }

    private Interceptor wrapByScope(Interceptor interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor instanceof AroundInterceptor) {
            return new ScopedInterceptor((AroundInterceptor) interceptor, scope, policy);
        } else if (interceptor instanceof StaticAroundInterceptor) {
            return new ScopedStaticAroundInterceptor((StaticAroundInterceptor) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor5) {
            return new ScopedInterceptor5((AroundInterceptor5) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor4) {
            return new ScopedInterceptor4((AroundInterceptor4) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor3) {
            return new ScopedInterceptor3((AroundInterceptor3) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor2) {
            return new ScopedInterceptor2((AroundInterceptor2) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor1) {
            return new ScopedInterceptor1((AroundInterceptor1) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor0) {
            return new ScopedInterceptor0((AroundInterceptor0) interceptor, scope, policy);
        } else if (interceptor instanceof ApiIdAwareAroundInterceptor) {
            return new ScopedApiIdAwareAroundInterceptor((ApiIdAwareAroundInterceptor) interceptor, scope, policy);
        }

        throw new IllegalArgumentException("Unexpected interceptor type: " + interceptor.getClass());
    }

    private Interceptor wrapByExceptionHandleScope(Interceptor interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor instanceof AroundInterceptor) {
            return new ExceptionHandleScopedInterceptor((AroundInterceptor) interceptor, scope, policy);
        } else if (interceptor instanceof StaticAroundInterceptor) {
            return new ExceptionHandleScopedStaticAroundInterceptor((StaticAroundInterceptor) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor5) {
            return new ExceptionHandleScopedInterceptor5((AroundInterceptor5) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor4) {
            return new ExceptionHandleScopedInterceptor4((AroundInterceptor4) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor3) {
            return new ExceptionHandleScopedInterceptor3((AroundInterceptor3) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor2) {
            return new ExceptionHandleScopedInterceptor2((AroundInterceptor2) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor1) {
            return new ExceptionHandleScopedInterceptor1((AroundInterceptor1) interceptor, scope, policy);
        } else if (interceptor instanceof AroundInterceptor0) {
            return new ExceptionHandleScopedInterceptor0((AroundInterceptor0) interceptor, scope, policy);
        } else if (interceptor instanceof ApiIdAwareAroundInterceptor) {
            return new ExceptionHandleScopedApiIdAwareAroundInterceptor((ApiIdAwareAroundInterceptor) interceptor, scope, policy);
        }

        throw new IllegalArgumentException("Unexpected interceptor type: " + interceptor.getClass());
    }

    private Interceptor wrapByExceptionHandle(Interceptor interceptor) {
        if (interceptor instanceof AroundInterceptor) {
            return new ExceptionHandleAroundInterceptor((AroundInterceptor) interceptor);
        } else if (interceptor instanceof StaticAroundInterceptor) {
            return new ExceptionHandleStaticAroundInterceptor((StaticAroundInterceptor) interceptor);
        } else if (interceptor instanceof AroundInterceptor5) {
            return new ExceptionHandleAroundInterceptor5((AroundInterceptor5) interceptor);
        } else if (interceptor instanceof AroundInterceptor4) {
            return new ExceptionHandleAroundInterceptor4((AroundInterceptor4) interceptor);
        } else if (interceptor instanceof AroundInterceptor3) {
            return new ExceptionHandleAroundInterceptor3((AroundInterceptor3) interceptor);
        } else if (interceptor instanceof AroundInterceptor2) {
            return new ExceptionHandleAroundInterceptor2((AroundInterceptor2) interceptor);
        } else if (interceptor instanceof AroundInterceptor1) {
            return new ExceptionHandleAroundInterceptor1((AroundInterceptor1) interceptor);
        } else if (interceptor instanceof AroundInterceptor0) {
            return new ExceptionHandleAroundInterceptor0((AroundInterceptor0) interceptor);
        } else if (interceptor instanceof ApiIdAwareAroundInterceptor) {
            return new ExceptionHandleApiIdAwareAroundInterceptor((ApiIdAwareAroundInterceptor) interceptor);
        }

        throw new IllegalArgumentException("Unexpected interceptor type: " + interceptor.getClass());
    }
}
