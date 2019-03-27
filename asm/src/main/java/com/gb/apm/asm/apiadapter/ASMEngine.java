package com.gb.apm.asm.apiadapter;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.asm.interceptor.registry.InterceptorRegistryBinder;
import com.gb.apm.asm.metadata.ApiMetaDataService;
import com.gb.apm.asm.objectfactory.ObjectBinderFactory;
import com.gb.apm.asm.util.JavaAssistUtils;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.NotFoundInstrumentException;
import com.google.inject.Provider;

/**
 * @author jaehong.kim
 */
public class ASMEngine implements InstrumentEngine {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isInfo = logger.isInfoEnabled();

    private final Instrumentation instrumentation;
    private final ObjectBinderFactory objectBinderFactory;
    private final InterceptorRegistryBinder interceptorRegistryBinder;
    private final Provider<ApiMetaDataService> apiMetaDataService;


    public ASMEngine(Instrumentation instrumentation, ObjectBinderFactory objectBinderFactory, final InterceptorRegistryBinder interceptorRegistryBinder, Provider<ApiMetaDataService> apiMetaDataService, final List<String> bootStrapJars) {
        if (instrumentation == null) {
            throw new NullPointerException("instrumentation must not be null");
        }
        if (objectBinderFactory == null) {
            throw new NullPointerException("objectBinderFactory must not be null");
        }
        if (interceptorRegistryBinder == null) {
            throw new NullPointerException("interceptorRegistryBinder must not be null");
        }
        if (apiMetaDataService == null) {
            throw new NullPointerException("apiMetaDataService must not be null");
        }

        this.instrumentation = instrumentation;
        this.objectBinderFactory = objectBinderFactory;
        this.interceptorRegistryBinder = interceptorRegistryBinder;
        this.apiMetaDataService = apiMetaDataService;

    }

    @Override
    public InstrumentClass getClass(InstrumentContext instrumentContext, ClassLoader classLoader, String className, byte[] classFileBuffer) throws NotFoundInstrumentException {
        if (className == null) {
            throw new NullPointerException("class name must not be null.");
        }
        
        try {
            if (classFileBuffer == null) {
                ASMClassNodeAdapter classNode = ASMClassNodeAdapter.get(instrumentContext, classLoader, JavaAssistUtils.javaNameToJvmName(className));
                if (classNode == null) {
                    return null;
                }
                ApiMetaDataService apiMetaDataService = this.apiMetaDataService.get();
                return new ASMClass(objectBinderFactory, instrumentContext, interceptorRegistryBinder, apiMetaDataService, classLoader, classNode);
            }

            // Use ASM tree api.
            final ClassReader classReader = new ClassReader(classFileBuffer);
            final ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            ApiMetaDataService apiMetaDataService = this.apiMetaDataService.get();
            return new ASMClass(objectBinderFactory, instrumentContext, interceptorRegistryBinder, apiMetaDataService, classLoader, classNode);
        } catch (Throwable e) {
        	e.printStackTrace();
            throw new NotFoundInstrumentException(e);
        }
    }

    @Override
    public boolean hasClass(ClassLoader classLoader, String className) {
        // TODO deprecated
        return classLoader.getResource(JavaAssistUtils.javaNameToJvmName(className) + ".class") != null;
    }

    @Override
    public void appendToBootstrapClassPath(JarFile jarFile) {
        if (jarFile == null) {
            throw new NullPointerException("jarFile must not be null");
        }
        if (isInfo) {
            logger.info("appendToBootstrapClassPath:{}", jarFile);
        }
        instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
    }
}