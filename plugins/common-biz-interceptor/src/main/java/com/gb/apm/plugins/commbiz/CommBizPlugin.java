package com.gb.apm.plugins.commbiz;

import java.security.ProtectionDomain;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.bootstrap.core.instrument.InstrumentClass;
import com.gb.apm.bootstrap.core.instrument.InstrumentException;
import com.gb.apm.bootstrap.core.instrument.Instrumentor;
import com.gb.apm.bootstrap.core.instrument.matcher.CustomClassNameMatcher.CustomClassNameMatcherBuilder;
import com.gb.apm.bootstrap.core.instrument.matcher.CustomMatcher;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformCallback;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplate;
import com.gb.apm.bootstrap.core.instrument.transfomer.TransformTemplateAware;
import com.gb.apm.bootstrap.core.plugin.ProfilerPlugin;
import com.gb.apm.bootstrap.core.plugin.ProfilerPluginSetupContext;
import com.gb.apm.bootstrap.core.util.InstrumentUtils;

public class CommBizPlugin implements ProfilerPlugin, TransformTemplateAware {

	private TransformTemplate transformTemplate;

	private ProfilerConfig config;// 这里可能通过在pinpoint.config配协置文件里配置额外的业务参数，来调控transfomer逻辑

	@Override
	public void setTransformTemplate(TransformTemplate transformTemplate) {
		this.transformTemplate = transformTemplate;
	}

	@Override
	public void setup(ProfilerPluginSetupContext context) {
		this.addTransformers();
		this.addTxcContextTransformer();
		this.config = context.getConfig();
	}

	private void addTxcContextTransformer() {
		transformTemplate.transform("com.tranboot.client.core.txc.TxcContext", new TransformCallback() {
			@Override
			public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className,
					Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
					throws InstrumentException {
				InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
				InstrumentUtils.findMethod(target, "beginTxc", "java.lang.String", "int", "int")
						.addInterceptor("com.gb.apm.plugins.commbiz.interceptor.TxcInterceptor");
				return target.toBytecode();
			}
		});
	}

	private void addTransformers() {
		CustomMatcher matcher0 = new CustomClassNameMatcherBuilder()
				.startWith("com.gb.").endWith("Service").or()
				.startWith("com.gb.").endWith("ServiceImpl").or()
				.startWith("com.gb.").endWith("Dao").or()
				.startWith("test.gb.").or()
				.startWith("me.ayumi.").or()
				.startWith("me.dubbo.client").or()
				.startWith("me.dubbo.server").build();
		transformTemplate.transform(matcher0, new TransformCallback() {
			@Override
			public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className,
					Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
					throws InstrumentException {
				InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
				target.addInterceptor("com.gb.apm.plugins.commbiz.interceptor.InvokeStackInterceptor");
				return target.toBytecode();
			}
		});
	}
}
