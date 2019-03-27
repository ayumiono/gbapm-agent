package com.gb.apm.profiler.context.recorder;

import com.alibaba.fastjson.JSON;
import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.utils.AnnotationKeyUtils;
import com.gb.apm.common.utils.StringUtils;
import com.gb.apm.dapper.context.MethodDescriptor;
import com.gb.apm.model.Annotation;
import com.gb.apm.profiler.context.KVAnnotation;
import com.gb.apm.profiler.metadata.SqlMetaDataService;
import com.gb.apm.profiler.metadata.StringMetaDataService;

/**
 * 
 * @author jaehong.kim
 *
 */
public abstract class AbstractRecorder {

    protected final StringMetaDataService stringMetaDataService;
    protected final SqlMetaDataService sqlMetaDataService;
    
    public AbstractRecorder(final StringMetaDataService stringMetaDataService, SqlMetaDataService sqlMetaDataService) {
        if (stringMetaDataService == null) {
            throw new NullPointerException("stringMetaDataService must not be null");
        }
        if (sqlMetaDataService == null) {
            throw new NullPointerException("sqlMetaDataService must not be null");
        }
        this.stringMetaDataService = stringMetaDataService;
        this.sqlMetaDataService = sqlMetaDataService;
    }
    
    public void recordException(Throwable throwable) {
        recordException(true, throwable);
    }

    public void recordException(boolean markError, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        final String drop = StringUtils.abbreviate(throwable.getMessage(), 256);
        // An exception that is an instance of a proxy class could make something wrong because the class name will vary.
        final int exceptionId = stringMetaDataService.cacheString(throwable.getClass().getName());
        setExceptionInfo(markError, exceptionId, drop);
    }

    abstract void setExceptionInfo(int exceptionClassId, String exceptionMessage);

    abstract void setExceptionInfo(boolean markError, int exceptionClassId, String exceptionMessage);
    
    public void recordApi(MethodDescriptor methodDescriptor) {
        if (methodDescriptor == null) {
            return;
        }
        if (methodDescriptor.getApiId() == 0) {
            recordAttribute(AnnotationKey.API, methodDescriptor.getFullName());
        } else {
            setApiId0(methodDescriptor.getApiId());
        }
    }
    
    public void recordApi(MethodDescriptor methodDescriptor, Object[] args) {
        recordApi(methodDescriptor);
        recordArgs(args);
    }

    public void recordApi(MethodDescriptor methodDescriptor, Object args, int index) {
        recordApi(methodDescriptor);
        recordSingleArg(args, index);
    }

    public void recordApi(MethodDescriptor methodDescriptor, Object[] args, int start, int end) {
        recordApi(methodDescriptor);
        recordArgs(args, start, end);
    }

    public void recordApiCachedString(MethodDescriptor methodDescriptor, String args, int index) {
        recordApi(methodDescriptor);
        recordSingleCachedString(args, index);
    }

    abstract void setApiId0(final int apiId);

    private void recordArgs(Object[] args, int start, int end) {
        if (args != null) {
            int max = Math.min(Math.min(args.length, AnnotationKey.MAX_ARGS_SIZE), end);
            for (int i = start; i < max; i++) {
                recordAttribute(AnnotationKeyUtils.getArgs(i), args[i]);
            }
            // TODO How to handle if args length is greater than MAX_ARGS_SIZE?
        }
    }

    private void recordSingleArg(Object args, int index) {
        if (args != null) {
            recordAttribute(AnnotationKeyUtils.getArgs(index), args);
        }
    }

    private void recordSingleCachedString(String args, int index) {
        if (args != null) {
            int cacheId = stringMetaDataService.cacheString(args);
            recordAttribute(AnnotationKeyUtils.getCachedArgs(index), cacheId);
        }
    }

    private void recordArgs(Object[] args) {
        if (args != null) {
            int max = Math.min(args.length, AnnotationKey.MAX_ARGS_SIZE);
            for (int i = 0; i < max; i++) {
                recordAttribute(AnnotationKeyUtils.getArgs(i), args[i]);
            }
         // TODO How to handle if args length is greater than MAX_ARGS_SIZE?                                                                  
        }
    }
    
    public void recordAttribute(AnnotationKey key, String value) {
        addAnnotation(new KVAnnotation(key.getCode(), value));
    }

    public void recordAttribute(AnnotationKey key, int value) {
        addAnnotation(new KVAnnotation(key.getCode(), value+""));
    }

    public void recordAttribute(AnnotationKey key, Object value) {
        addAnnotation(new KVAnnotation(key.getCode(), JSON.toJSONString(value)));
    }

    abstract void addAnnotation(KVAnnotation annotation);
}
