package com.gb.apm.asm.apiadapter;

import java.util.Collections;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.interceptor.registry.InterceptorRegistryBinder;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ObjectBinderFactory;
import com.gb.apm.bootstrap.core.instrument.ClassFilter;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.instrument.MethodFilter;
import com.gb.apm.bootstrap.core.interceptor.scope.ExecutionPolicy;
import com.gb.apm.bootstrap.core.interceptor.scope.InterceptorScope;
import com.gb.apm.common.utils.Asserts;

/**
 * @author jaehong.kim
 */
public class ASMNestedClass implements InstrumentClass {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ASMClass aClass;

    public ASMNestedClass(ObjectBinderFactory objectBinderFactory, final InstrumentContext pluginContext, final InterceptorRegistryBinder interceptorRegistryBinder, ApiMetaDataService apiMetaDataService, final ClassLoader classLoader, final ClassNode classNode) {
        this.aClass = new ASMClass(objectBinderFactory, pluginContext, interceptorRegistryBinder, apiMetaDataService, classLoader, classNode);
    }

    public ASMNestedClass(ObjectBinderFactory objectBinderFactory, final InstrumentContext pluginContext, final InterceptorRegistryBinder interceptorRegistryBinder, ApiMetaDataService apiMetaDataService, final ClassLoader classLoader, final ASMClassNodeAdapter classNodeAdapter) {
        this.aClass = new ASMClass(objectBinderFactory, pluginContext, interceptorRegistryBinder, apiMetaDataService, classLoader, classNodeAdapter);
    }

    public ClassLoader getClassLoader() {
        return this.aClass.getClassLoader();
    }

    @Override
    public boolean isInterceptable() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return this.aClass.isInterface();
    }

    @Override
    public String getName() {
        return this.aClass.getName();
    }

    @Override
    public String getSuperClass() {
        return this.aClass.getSuperClass();
    }

    @Override
    public String[] getInterfaces() {
        return this.aClass.getInterfaces();
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String name, String... parameterTypes) {
        return null;
    }

    @Override
    public List<InstrumentMethod> getDeclaredMethods() {
        return Collections.emptyList();
    }

    @Override
    public List<InstrumentMethod> getDeclaredMethods(MethodFilter methodFilter) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasDeclaredMethod(String methodName, String... args) {
        return this.aClass.hasDeclaredMethod(methodName, args);
    }

    @Override
    public boolean hasMethod(String methodName, String... parameterTypes) {
        return this.aClass.hasMethod(methodName, parameterTypes);
    }

    @Override
    public boolean hasEnclosingMethod(String methodName, String... parameterTypes) {
        return this.aClass.hasEnclosingMethod(methodName, parameterTypes);
    }

    @Override
    public InstrumentMethod getConstructor(String... parameterTypes) {
        return null;
    }

    @Override
    public boolean hasConstructor(String... parameterTypeArray) {
        return this.aClass.hasConstructor(parameterTypeArray);
    }

    @Override
    public boolean hasField(String name, String type) {
        return this.aClass.hasField(name, type);
    }

    @Override
    public boolean hasField(String name) {
        return this.aClass.hasField(name);
    }

    @Override
    public void weave(String adviceClassInternalName) throws InstrumentException {
        // nothing.
    }

    @Override
    public InstrumentMethod addDelegatorMethod(String methodName, String... paramTypes) throws InstrumentException {
        return null;
    }

    @Override
    public void addField(String accessorTypeName) throws InstrumentException {
        // nothing.
    }

    @Override
    public void addGetter(String getterTypeName, String fieldName) throws InstrumentException {
        // nothing.
    }

    @Override
    public void addSetter(String setterTypeName, String fieldName) throws InstrumentException {
        // nothing.
    }

    @Override
    public void addSetter(String setterTypeName, String fieldName, boolean removeFinal) throws InstrumentException {
        // nothing.
    }

    @Override
    public int addInterceptor(String interceptorClassName) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        return 0;
    }

    @Override
    public int addInterceptor(String interceptorClassName, Object[] constructorArgs) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, String scopeName) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scopeName, "scopeName");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, InterceptorScope scope) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scope, "scope");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, String scopeName) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scopeName, "scopeName");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, InterceptorScope scope) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scope, "scope");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, String scopeName, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scopeName, "scopeName");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scope, "scope");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, String scopeName, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scopeName, "scopeName");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scope, "scope");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addInterceptor(MethodFilter filter, String interceptorClassName) throws InstrumentException {
        Asserts.notNull(filter, "filter");
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        return 0;
    }

    @Override
    public int addInterceptor(MethodFilter filter, String interceptorClassName, Object[] constructorArgs) throws InstrumentException {
        Asserts.notNull(filter, "filter");
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        return 0;
    }

    @Override
    public int addScopedInterceptor(MethodFilter filter, String interceptorClassName, String scopeName, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(filter, "filter");
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scopeName, "scopeName");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addScopedInterceptor(MethodFilter filter, String interceptorClassName, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(filter, "filter");
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(scope, "scope");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addScopedInterceptor(MethodFilter filter, String interceptorClassName, Object[] constructorArgs, String scopeName, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(filter, "filter");
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scopeName, "scopeName");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public int addScopedInterceptor(MethodFilter filter, String interceptorClassName, Object[] constructorArgs, InterceptorScope scope, ExecutionPolicy executionPolicy) throws InstrumentException {
        Asserts.notNull(filter, "filter");
        Asserts.notNull(interceptorClassName, "interceptorClassName");
        Asserts.notNull(constructorArgs, "constructorArgs");
        Asserts.notNull(scope, "scope");
        Asserts.notNull(executionPolicy, "executionPolicy");
        return 0;
    }

    @Override
    public List<InstrumentClass> getNestedClasses(ClassFilter filter) {
        return this.aClass.getNestedClasses(filter);
    }

    @Override
    public byte[] toBytecode() {
        return null;
    }
}