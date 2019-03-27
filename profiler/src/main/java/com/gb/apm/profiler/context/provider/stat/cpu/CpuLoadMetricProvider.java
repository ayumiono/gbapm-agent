package com.gb.apm.profiler.context.provider.stat.cpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.common.utils.jdk.JvmType;
import com.gb.apm.common.utils.jdk.JvmUtils;
import com.gb.apm.common.utils.jdk.JvmVersion;
import com.gb.apm.profiler.monitor.metric.cpu.CpuLoadMetric;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author HyunGil Jeong
 */
public class CpuLoadMetricProvider implements Provider<CpuLoadMetric> {

    // Oracle
    private static final String ORACLE_JDK6_CPU_LOAD_METRIC = "com.navercorp.pinpoint.profiler.monitor.metric.cpu.oracle.Java6CpuLoadMetric";
    private static final String ORACLE_CPU_LOAD_METRIC = "com.navercorp.pinpoint.profiler.monitor.metric.cpu.oracle.DefaultCpuLoadMetric";
    // IBM
    private static final String IBM_JDK6_CPU_LOAD_METRIC = "com.navercorp.pinpoint.profiler.monitor.metric.cpu.ibm.Java6CpuLoadMetric";
    private static final String IBM_CPU_LOAD_METRIC = "com.navercorp.pinpoint.profiler.monitor.metric.cpu.ibm.DefaultCpuLoadMetric";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String vendorName;

    @Inject
    public CpuLoadMetricProvider(ProfilerConfig profilerConfig) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        vendorName = profilerConfig.getProfilerJvmVendorName();
    }

    @Override
    public CpuLoadMetric get() {
        String classToLoad = null;
        JvmType jvmType = JvmType.fromVendor(vendorName);
        if (jvmType == JvmType.UNKNOWN) {
            jvmType = JvmUtils.getType();
        }
        JvmVersion jvmVersion = JvmUtils.getVersion();
        if (jvmType == JvmType.ORACLE || jvmType == JvmType.OPENJDK) {
            if (jvmVersion.onOrAfter(JvmVersion.JAVA_7)) {
                classToLoad = ORACLE_CPU_LOAD_METRIC;
            } else if (jvmVersion.onOrAfter(JvmVersion.JAVA_5)) {
                classToLoad = ORACLE_JDK6_CPU_LOAD_METRIC;
            }
        } else if (jvmType == JvmType.IBM) {
            if (jvmVersion.onOrAfter(JvmVersion.JAVA_7)) {
                classToLoad = IBM_CPU_LOAD_METRIC;
            } else if (jvmVersion == JvmVersion.JAVA_6) {
                classToLoad = IBM_JDK6_CPU_LOAD_METRIC;
            }
        }
        CpuLoadMetric cpuLoadMetric = createCpuLoadMetric(classToLoad);
        logger.info("loaded : {}", cpuLoadMetric);
        return cpuLoadMetric;
    }

    private CpuLoadMetric createCpuLoadMetric(String classToLoad) {
        if (classToLoad == null) {
            return CpuLoadMetric.UNSUPPORTED_CPU_LOAD_METRIC;
        }
        CpuLoadMetric cpuLoadMetric;
        try {
            @SuppressWarnings("unchecked")
            Class<CpuLoadMetric> cpuLoadMetricClass = (Class<CpuLoadMetric>) Class.forName(classToLoad);
            cpuLoadMetric = cpuLoadMetricClass.newInstance();
        } catch (Exception e) {
            logger.warn("Error creating CpuLoadMetric [" + classToLoad + "]", e);
            cpuLoadMetric = CpuLoadMetric.UNSUPPORTED_CPU_LOAD_METRIC;
        }
        return cpuLoadMetric;

    }
}
