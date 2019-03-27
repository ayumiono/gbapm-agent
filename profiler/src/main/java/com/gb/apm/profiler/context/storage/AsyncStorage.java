package com.gb.apm.profiler.context.storage;

import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.Span._SpanEvent;

/**
 * 
 * @author jaehong.kim
 *
 */
public class AsyncStorage implements Storage {

    private Storage storage;
    
    public AsyncStorage(final Storage storage) {
        this.storage = storage;
    }
    
    @Override
    public void store(_SpanEvent spanEvent) {
        storage.store(spanEvent);
    }

    @Override
    public void store(Span span) {
        storage.flush();
    }

    @Override
    public void flush() {
        storage.flush();
    }

    @Override
    public void close() {
        storage.close();
    }
}
