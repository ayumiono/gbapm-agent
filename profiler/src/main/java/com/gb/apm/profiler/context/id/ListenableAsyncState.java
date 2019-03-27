package com.gb.apm.profiler.context.id;

import com.gb.apm.annotations.InterfaceAudience;
import com.gb.apm.dapper.context.AsyncState;

/**
 * @author jaehong.kim
 */
@InterfaceAudience.LimitedPrivate("vert.x")
public class ListenableAsyncState implements AsyncState {

    private final AsyncStateListener asyncStateListener;

    private boolean setup = false;
    private boolean await = false;
    private boolean finish = false;

    public ListenableAsyncState(AsyncStateListener asyncStateListener) {
        if (asyncStateListener == null) {
            throw new NullPointerException("asyncStateListener must not be null");
        }
        this.asyncStateListener = asyncStateListener;
    }

    @Override
    public void finish() {
        boolean finished = false;
        synchronized (this) {
            if (this.await && !this.finish) {
                finished = true;
            }
            this.finish = true;
        }
        if (finished) {
            this.asyncStateListener.finish();
        }
    }

    @Override
    public void setup() {
        synchronized (this) {
            this.setup = true;
        }
    }

    @Override
    public boolean await() {
        synchronized (this) {
            if (!this.setup || this.finish) {
                return false;
            }
            this.await = true;
            return true;
        }
    }

    @InterfaceAudience.LimitedPrivate("LocalTraceContext")
    public interface AsyncStateListener {
        void finish();
    }

    @Override
    public String toString() {
        return "ListenableAsyncState{" +
                "asyncStateListener=" + asyncStateListener +
                ", setup=" + setup +
                ", await=" + await +
                ", finish=" + finish +
                '}';
    }
}