package com.gb.apm.asm.interceptor.factory;

import com.gb.apm.asm.apiadapter.ScopeInfo;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.interceptor.Interceptor;

public interface InterceptorFactory {
    Interceptor getInterceptor(ClassLoader classLoader, String interceptorClassName, Object[] providedArguments, ScopeInfo scopeInfo, InstrumentClass target, InstrumentMethod targetMethod);
}
