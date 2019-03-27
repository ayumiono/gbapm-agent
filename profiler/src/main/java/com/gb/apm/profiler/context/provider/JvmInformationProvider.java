package com.gb.apm.profiler.context.provider;

import com.gb.apm.common.utils.jdk.JvmUtils;
import com.gb.apm.common.utils.jdk.SystemPropertyKey;
import com.gb.apm.profiler.JvmInformation;
import com.gb.apm.profiler.monitor.metric.gc.GarbageCollectorMetric;
import com.gb.apm.profiler.monitor.metric.gc.UnknownGarbageCollectorMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author HyunGil Jeong
 */
public class JvmInformationProvider implements Provider<JvmInformation> {

    private final String jvmVersion;
    private final GarbageCollectorMetric garbageCollectorMetric;


    @Inject
    public JvmInformationProvider(GarbageCollectorMetric garbageCollectorMetric) {
        this.jvmVersion = JvmUtils.getSystemProperty(SystemPropertyKey.JAVA_VERSION);
        this.garbageCollectorMetric = garbageCollectorMetric;
    }

    public JvmInformationProvider() {
        this(new UnknownGarbageCollectorMetric());
    }

    public JvmInformation get() {
        return new JvmInformation(this.jvmVersion, this.garbageCollectorMetric.gcType().getValue());
    }
}
