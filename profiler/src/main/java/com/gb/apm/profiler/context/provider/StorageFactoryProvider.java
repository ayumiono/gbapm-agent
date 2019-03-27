package com.gb.apm.profiler.context.provider;

import com.gb.apm.collector.DataSender;
import com.gb.apm.profiler.context.module.SpanDataSender;
import com.gb.apm.profiler.context.storage.SpanStorageFactory;
import com.gb.apm.profiler.context.storage.StorageFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class StorageFactoryProvider implements Provider<StorageFactory> {

    private final DataSender spanDataSender;

    @Inject
    public StorageFactoryProvider(@SpanDataSender DataSender spanDataSender) {
        if (spanDataSender == null) {
            throw new NullPointerException("spanDataSender must not be null");
        }
        this.spanDataSender = spanDataSender;
    }

    @Override
    public StorageFactory get() {
    	return new SpanStorageFactory(spanDataSender);
    }

    @Override
    public String toString() {
        return "StorageFactoryProvider{" +
                ", spanDataSender=" + spanDataSender +
                '}';
    }

}
