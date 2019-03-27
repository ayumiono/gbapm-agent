package com.gb.apm.profiler.context.active;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author Taejin Koo
 */
public class DefaultActiveTraceRepository implements ActiveTraceRepository {

    // memory leak defense threshold
    private static final int DEFAULT_MAX_ACTIVE_TRACE_SIZE = 1024 * 10;
    // oom safe cache
    private final ConcurrentMap<Long, ActiveTrace> activeTraceInfoMap;

    public DefaultActiveTraceRepository() {
        this(DEFAULT_MAX_ACTIVE_TRACE_SIZE);
    }

    public DefaultActiveTraceRepository(int maxActiveTraceSize) {
        this.activeTraceInfoMap = createCache(maxActiveTraceSize);
    }

    private ConcurrentMap<Long, ActiveTrace> createCache(int maxActiveTraceSize) {
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.concurrencyLevel(64);
        cacheBuilder.initialCapacity(maxActiveTraceSize);
        cacheBuilder.maximumSize(maxActiveTraceSize);
        // OOM defense
        cacheBuilder.weakValues();

        final Cache<Long, ActiveTrace> localCache = cacheBuilder.build();
        return localCache.asMap();
    }

    @Override
    public void put(ActiveTrace activeTrace) {
        this.activeTraceInfoMap.put(activeTrace.getId(), activeTrace);
    }

    @Override
    public ActiveTrace remove(Long key) {
        return this.activeTraceInfoMap.remove(key);
    }

    // @ThreadSafe
    @Override
    public List<ActiveTraceInfo> collect() {
        final Collection<ActiveTrace> copied = this.activeTraceInfoMap.values();
        List<ActiveTraceInfo> collectData = new ArrayList<ActiveTraceInfo>(copied.size());
        for (ActiveTrace trace : copied) {
            final long startTime = trace.getStartTime();
            // not started
            if (startTime > 0) {
                if (trace.isSampled()) {
                    ActiveTraceInfo activeTraceInfo = new ActiveTraceInfo(trace.getId(), startTime, trace.getBindThread(), true, trace.getTransactionId(), trace.getEntryPoint());
                    collectData.add(activeTraceInfo);
                } else {
                    // clear Trace reference
                    ActiveTraceInfo activeTraceInfo = new ActiveTraceInfo(trace.getId(), startTime, trace.getBindThread());
                    collectData.add(activeTraceInfo);
                }
            }
        }
        return collectData;
    }

}
