package com.gb.apm.asm.apiadapter;


import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.instrument.DefaultMethodDescriptor;
import com.gb.apm.asm.interceptor.CaptureType;
import com.gb.apm.asm.interceptor.InterceptorDefinition;
import com.gb.apm.asm.interceptor.InterceptorDefinitionFactory;
import com.gb.apm.asm.interceptor.InterceptorType;
import com.gb.apm.asm.interceptor.factory.InterceptorFactory;
import com.gb.apm.asm.interceptor.registry.InterceptorRegistryBinder;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ObjectBinderFactory;
import com.gb.apm.asm.util.JavaAssistUtils;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.interceptor.Interceptor;
import com.gb.apm.bootstrap.core.interceptor.registry.InterceptorRegistry;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.common.utils.Asserts;
import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author jaehong.kim
 */
public class ASMMethod implements InstrumentMethod {
    // TODO fix inject InterceptorDefinitionFactory
    private static final InterceptorDefinitionFactory interceptorDefinitionFactory = new InterceptorDefinitionFactory();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final ObjectBinderFactory objectBinderFactory;
    private final InstrumentContext pluginContext;
    private final InterceptorRegistryBinder interceptorRegistryBinder;
    private final ASMClass declaringClass;
    private final ASMMethodNodeAdapter methodNode;
    private final MethodDescriptor descriptor;
    private final ApiMetaDataService apiMetaDataService;

    // TODO fix inject ScopeFactory
    private static final ScopeFactory scopeFactory = new ScopeFactory();


    public ASMMethod(ObjectBinderFactory objectBinderFactory, InstrumentContext pluginContext, ApiMetaDataService apiMetaDataService, InterceptorRegistryBinder interceptorRegistryBinder, ASMClass declaringClass, MethodNode methodNode) {
        this(objectBinderFactory, pluginContext, interceptorRegistryBinder, apiMetaDataService, declaringClass, new ASMMethodNodeAdapter(JavaAssistUtils.javaNameToJvmName(declaringClass.getName()), methodNode));

    }

    public ASMMethod(ObjectBinderFactory objectBinderFactory, InstrumentContext pluginContext, InterceptorRegistryBinder interceptorRegistryBinder, ApiMetaDataService apiMetaDataService, ASMClass declaringClass, ASMMethodNodeAdapter methodNode) {
        if (objectBinderFactory == null) {
            throw new NullPointerException("objectBinderFactory must not be null");
        }
        if (pluginContext == null) {
            throw new NullPointerException("pluginContext must not be null");
        }
        if (apiMetaDataService == null) {
            throw new NullPointerException("apiMetaDataService must not be null");
        }
        this.objectBinderFactory = objectBinderFactory;
        this.pluginContext = pluginContext;
        this.interceptorRegistryBinder = interceptorRegistryBinder;
        this.apiMetaDataService = apiMetaDataService;
        this.declaringClass = declaringClass;
        this.methodNode = methodNode;

        final String[] parameterVariableNames = this.methodNode.getParameterNames();
        final int lineNumber = this.methodNode.getLineNumber();

        final DefaultMethodDescriptor descriptor = new DefaultMethodDescriptor(declaringClass.getName(), methodNode.getName(), getParameterTypes(), parameterVariableNames);
        descriptor.setLineNumber(lineNumber);

        this.descriptor = descriptor;
    }

    @Override
    public String getName() {
        return this.methodNode.getName();
    }

    @Override
    public String[] getParameterTypes() {
        return this.methodNode.getParameterTypes();
    }

    @Override
    public String getReturnType() {
        return this.methodNode.getReturnType();
    }

    @Override
    public int getModifiers() {
        return this.methodNode.getAccess();
    }

    @Override
    public boolean isConstructor() {
        return this.methodNode.isConstructor();
    }

    @Override
    public MethodDescriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    public int addInterceptor(String interceptorClassName) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        return addInterceptor0(interceptorClassName, null, null, null);
    }

    @Override
    public int addInterceptor(String interceptorClassName, Object[] constructorArgs) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        return addInterceptor0(interceptorClassName, constructorArgs, null, null);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, String scopeName) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scopeName, "scopeName");
        final InterceptorScope interceptorScope = this.pluginContext.getInterceptorScope(scopeName);
        return addInterceptor0(interceptorClassName, null, interceptorScope, null);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, InterceptorScope scope) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scope, "scope");
        return addInterceptor0(interceptorClassName, null, scope, null);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, String scopeName, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scopeName, "scopeName");
        Asserts.notNull(executionPolicy, "executionPolicy");
        final InterceptorScope interceptorScope = this.pluginContext.getInterceptorScope(scopeName);
        return addInterceptor0(interceptorClassName, null, interceptorScope, executionPolicy);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scope, "scope");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return addInterceptor0(interceptorClassName, null, scope, executionPolicy);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, String scopeName) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scopeName, "scopeName");
        final InterceptorScope interceptorScope = this.pluginContext.getInterceptorScope(scopeName);
        return addInterceptor0(interceptorClassName, constructorArgs, interceptorScope, null);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, InterceptorScope scope) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scope, "scope");
        return addInterceptor0(interceptorClassName, constructorArgs, scope, null);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, String scopeName, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scopeName, "scopeName");
        Asserts.notNull(executionPolicy, "executionPolicy");
        final InterceptorScope interceptorScope = this.pluginContext.getInterceptorScope(scopeName);
        return addInterceptor0(interceptorClassName, constructorArgs, interceptorScope, executionPolicy);
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scope, "scope");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return addInterceptor0(interceptorClassName, constructorArgs, scope, executionPolicy);
    }

    @Override
    public void addInterceptor(int interceptorId) throws InstrumentException {
        final Interceptor interceptor = InterceptorRegistry.getInterceptor(interceptorId);
        try {
            addInterceptor0(interceptor, interceptorId);
        } catch (Exception e) {
            throw new InstrumentException("Failed to add interceptor " + interceptor.getClass().getName() + " to " + this.methodNode.getLongName(), e);
        }
    }



    // for internal api
    int addInterceptorInternal(String interceptorClassName, Object[] constructorArgs, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        if (interceptorClassName == null) {
            throw new NullPointerException("interceptorClassName must not be null");
        }
        return addInterceptor0(interceptorClassName, constructorArgs, scope, executionPolicy);
    }

    private int addInterceptor0(String interceptorClassName, Object[] constructorArgs, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        final ClassLoader classLoader = this.declaringClass.getClassLoader();
        final ScopeInfo scopeInfo = scopeFactory.newScopeInfo(classLoader, pluginContext, interceptorClassName, scope, executionPolicy);
        final Interceptor interceptor = createInterceptor(classLoader, interceptorClassName, scopeInfo, constructorArgs);
        final int interceptorId = this.interceptorRegistryBinder.getInterceptorRegistryAdaptor().addInterceptor(interceptor);

        addInterceptor0(interceptor, interceptorId);
        return interceptorId;
    }

    private Interceptor createInterceptor(ClassLoader classLoader, String interceptorClassName, ScopeInfo scopeInfo, Object[] constructorArgs) {
        // exception handling.
        final InterceptorFactory factory = objectBinderFactory.newAnnotatedInterceptorFactory(this.pluginContext, true);
        final Interceptor interceptor = factory.getInterceptor(classLoader, interceptorClassName, constructorArgs, scopeInfo, this.declaringClass, this);

        return interceptor;
    }

    private void addInterceptor0(Interceptor interceptor, int interceptorId) {
        if (interceptor == null) {
            throw new NullPointerException("interceptor must not be null");
        }

        final InterceptorDefinition interceptorDefinition = this.interceptorDefinitionFactory.createInterceptorDefinition(interceptor.getClass());
        final Class<?> interceptorClass = interceptorDefinition.getInterceptorClass();
        final CaptureType captureType = interceptorDefinition.getCaptureType();
        if (this.methodNode.hasInterceptor()) {
            logger.warn("Skip adding interceptor. 'already intercepted method' class={}, interceptor={}", this.declaringClass.getName(), interceptorClass.getName());
            return;
        }

        if (this.methodNode.isAbstract() || this.methodNode.isNative()) {
            logger.warn("Skip adding interceptor. 'abstract or native method' class={}, interceptor={}", this.declaringClass.getName(), interceptorClass.getName());
            return;
        }

        int apiId = -1;
        if (interceptorDefinition.getInterceptorType() == InterceptorType.API_ID_AWARE) {
            apiId = this.apiMetaDataService.cacheApi(this.descriptor);
        }

        // add before interceptor.
        if (isBeforeInterceptor(captureType) && interceptorDefinition.getBeforeMethod() != null) {
            this.methodNode.addBeforeInterceptor(interceptorId, interceptorDefinition, apiId);
            this.declaringClass.setModified(true);
        } else {
            if (isDebug) {
                logger.debug("Skip adding before interceptorDefinition because the interceptorDefinition doesn't have before method: {}", interceptorClass.getName());
            }
        }

        // add after interface.
        if (isAfterInterceptor(captureType) && interceptorDefinition.getAfterMethod() != null) {
            this.methodNode.addAfterInterceptor(interceptorId, interceptorDefinition, apiId);
            this.declaringClass.setModified(true);
        } else {
            if (isDebug) {
                logger.debug("Skip adding after interceptor because the interceptor doesn't have after method: {}", interceptorClass.getName());
            }
        }
    }

    private boolean isBeforeInterceptor(CaptureType captureType) {
        return CaptureType.BEFORE == captureType || CaptureType.AROUND == captureType;
    }

    private boolean isAfterInterceptor(CaptureType captureType) {
        return CaptureType.AFTER == captureType || CaptureType.AROUND == captureType;
    }
}