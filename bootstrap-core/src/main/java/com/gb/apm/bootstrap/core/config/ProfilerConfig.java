package com.gb.apm.bootstrap.core.config;

import java.util.List;
import java.util.Map;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface ProfilerConfig {

    int getInterceptorRegistrySize();

    String getCollectorSpanServerIp();

    int getCollectorSpanServerPort();

    String getCollectorStatServerIp();

    int getCollectorStatServerPort();

    String getCollectorTcpServerIp();

    int getCollectorTcpServerPort();

    int getStatDataSenderWriteQueueSize();

    int getStatDataSenderSocketSendBufferSize();

    int getStatDataSenderSocketTimeout();

    String getStatDataSenderSocketType();

    int getSpanDataSenderWriteQueueSize();

    int getSpanDataSenderSocketSendBufferSize();

    boolean isTcpDataSenderCommandAcceptEnable();

    boolean isTcpDataSenderCommandActiveThreadEnable();

    boolean isTcpDataSenderCommandActiveThreadCountEnable();

    boolean isTcpDataSenderCommandActiveThreadDumpEnable();

    boolean isTcpDataSenderCommandActiveThreadLightDumpEnable();

    boolean isTraceAgentActiveThread();

    boolean isTraceAgentDataSource();

    int getDataSourceTraceLimitSize();

    int getSpanDataSenderSocketTimeout();

    String getSpanDataSenderSocketType();

    int getSpanDataSenderChunkSize();

    int getStatDataSenderChunkSize();

    boolean isProfileEnable();

    int getJdbcSqlCacheSize();

    boolean isTraceSqlBindValue();

    int getMaxSqlBindValueSize();

    boolean isSamplingEnable();

    int getSamplingRate();

    boolean isIoBufferingEnable();

    int getIoBufferingBufferSize();

    int getProfileJvmCollectInterval();

    String getProfilerJvmVendorName();

    boolean isProfilerJvmCollectDetailedMetrics();

    long getAgentInfoSendRetryInterval();


    Filter<String> getProfilableClassFilter();

    List<String> getApplicationTypeDetectOrder();

    List<String> getDisabledPlugins();

    String getApplicationServerType();

    int getCallStackMaxDepth();

    boolean isPropagateInterceptorException();

    String getProfileInstrumentEngine();

    String readString(String propertyName, String defaultValue);

    int readInt(String propertyName, int defaultValue);

    DumpType readDumpType(String propertyName, DumpType defaultDump);

    long readLong(String propertyName, long defaultValue);

    List<String> readList(String propertyName);

    boolean readBoolean(String propertyName, boolean defaultValue);

    Map<String, String> readPattern(String propertyNamePatternRegex);

}
