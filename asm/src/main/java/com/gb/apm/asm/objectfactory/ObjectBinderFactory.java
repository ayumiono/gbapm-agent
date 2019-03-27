package com.gb.apm.asm.objectfactory;

import com.gb.apm.asm.interceptor.factory.InterceptorFactory;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;

public interface ObjectBinderFactory {
	public ArgumentProvider newInterceptorArgumentProvider(InstrumentClass instrumentClass);
	public InterceptorFactory newAnnotatedInterceptorFactory(InstrumentContext pluginContext, boolean exceptionHandle);
	public AutoBindingObjectFactory newAutoBindingObjectFactory(InstrumentContext pluginContext, ClassLoader classLoader, ArgumentProvider... argumentProviders);
}
