package com.gb.apm.plugins.dubbo;

import static com.gb.apm.common.trace.ServiceTypeProperty.RECORD_STATISTICS;

import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.AnnotationKeyFactory;
import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.common.trace.ServiceTypeFactory;

/**
 * @author Jinkai.Ma
 */
public interface DubboConstants {

    ServiceType DUBBO_PROVIDER_SERVICE_TYPE = ServiceTypeFactory.of(1110, "DUBBO_PROVIDER", RECORD_STATISTICS);
    ServiceType DUBBO_CONSUMER_SERVICE_TYPE = ServiceTypeFactory.of(9110, "DUBBO_CONSUMER", RECORD_STATISTICS);
    AnnotationKey DUBBO_ARGS_ANNOTATION_KEY = AnnotationKeyFactory.of(90, "dubbo.args");
    AnnotationKey DUBBO_RESULT_ANNOTATION_KEY = AnnotationKeyFactory.of(91, "dubbo.result");
    AnnotationKey DUBBO_INTERFACE_ANNOTATION_KEY = AnnotationKeyFactory.of(92, "dubbo.interface");
    AnnotationKey DUBBO_GROUP = AnnotationKeyFactory.of(93, "dubbo.group");
    AnnotationKey DUBBO_VERSION = AnnotationKeyFactory.of(94, "dubbo.version");
    AnnotationKey DUBBO_TIMEOUT = AnnotationKeyFactory.of(95, "dubbo.timeout");//consumer中设置的超时时间
    AnnotationKey DUBBO_LOAD_LIMIT = AnnotationKeyFactory.of(96, "dubbo.load.limit");//consumer及provider中设置的流量限制
    AnnotationKey DUBBO_PROVIDER_LOAD = AnnotationKeyFactory.of(97, "provider.load");//provider当前的负载
    AnnotationKey GB_MESSAGE_PACK_CODE = AnnotationKeyFactory.of(98, "messagepack.code");
    

    String META_DO_NOT_TRACE = "_DUBBO_DO_NOT_TRACE";
    String META_TRANSACTION_ID = "_DUBBO_TRASACTION_ID";
    String META_SPAN_ID = "_DUBBO_SPAN_ID";
    String META_PARENT_SPAN_ID = "_DUBBO_PARENT_SPAN_ID";
    String TXC_XID = "xid";
    String META_PARENT_APPLICATION_NAME = "_DUBBO_PARENT_APPLICATION_NAME";
    String META_PARENT_APPLICATION_TYPE = "_DUBBO_PARENT_APPLICATION_TYPE";
    String META_FLAGS = "_DUBBO_FLAGS";

    String MONITOR_SERVICE_FQCN = "com.alibaba.dubbo.monitor.MonitorService";
}
