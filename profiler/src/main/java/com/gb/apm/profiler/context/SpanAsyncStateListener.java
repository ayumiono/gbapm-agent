package com.gb.apm.profiler.context;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.profiler.context.id.ListenableAsyncState;
import com.gb.apm.profiler.context.storage.Storage;

/**
 * @author Woonduk Kang(emeroad)
 */
@InterfaceAudience.LimitedPrivate("vert.x")
public class SpanAsyncStateListener implements ListenableAsyncState.AsyncStateListener {

    private final AtomicIntegerFieldUpdater<SpanAsyncStateListener> CLOSED_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(SpanAsyncStateListener.class, "closed");
    private static final int OPEN = 0;
    private static final int CLOSED = 1;

    @SuppressWarnings("unused")
    private volatile int closed = OPEN;

    private final Span span;
    private final Storage storage;

    SpanAsyncStateListener(Span span, Storage storage) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }
        if (storage == null) {
            throw new NullPointerException("storage must not be null");
        }
        this.span = span;
        this.storage = storage;
    }

    @Override
    public void finish() {
        if (CLOSED_UPDATER.compareAndSet(this, OPEN, CLOSED)) {
            if (span.isTimeRecording()) {
                span.markAfterTime();
            }
            this.storage.store(this.span);
            this.storage.close();
        }
    }
}
