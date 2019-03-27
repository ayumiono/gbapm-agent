package com.gb.apm.plugins.dubbo;

import java.security.ProtectionDomain;

import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.Instrumentor;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplate;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplateAware;
import com.gb.apm.bootstrap.core.logging.PLogger;
import com.gb.apm.bootstrap.core.logging.PLoggerFactory;
import com.gb.apm.bootstrap.core.plugin.ProfilerPlugin;
import com.gb.apm.bootstrap.core.plugin.ProfilerPluginSetupContext;

/**
 * @author Jinkai.Ma
 */
public class DubboPlugin implements ProfilerPlugin, TransformTemplateAware {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());

    private TransformTemplate transformTemplate;

    @Override
    public void setup(ProfilerPluginSetupContext context) {
        DubboConfiguration config = new DubboConfiguration(context.getConfig());
        if (!config.isDubboEnabled()) {
            logger.info("DubboPlugin disabled");
            return;
        }
        this.addApplicationTypeDetector(context, config);
        this.addTransformers();
    }

    private void addTransformers() {
        transformTemplate.transform("com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker", new TransformCallback() {
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                target.getDeclaredMethod("doInvoke", "com.alibaba.dubbo.rpc.Invocation").addInterceptor("com.gb.apm.plugins.dubbo.interceptor.DubboConsumerInterceptor");

                return target.toBytecode();
            }
        });
        transformTemplate.transform("com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker", new TransformCallback() {
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);

                target.getDeclaredMethod("invoke", "com.alibaba.dubbo.rpc.Invocation").addInterceptor("com.gb.apm.plugins.dubbo.interceptor.DubboProviderInterceptor");

                return target.toBytecode();
            }
        });
    }

    /**
     * Pinpoint profiler agent uses this detector to find out the service type of current application.
     */
    private void addApplicationTypeDetector(ProfilerPluginSetupContext context, DubboConfiguration config) {
        context.addApplicationTypeDetector(new DubboProviderDetector(config.getDubboBootstrapMains()));
    }

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}
