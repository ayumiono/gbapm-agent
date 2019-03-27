package com.gb.apm.profiler.metadata;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.gb.apm.common.utils.BytesUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author emeroad
 */
public class SimpleCache<T> {
    // zero means not exist.
    private final AtomicInteger idGen;
    private final ConcurrentMap<T, Result> cache;


    public SimpleCache() {
        this(1024, 1);
    }

    public SimpleCache(int cacheSize) {
        this(cacheSize, 1);
    }

    public SimpleCache(int cacheSize, int startValue) {
        idGen = new AtomicInteger(startValue);
        cache = createCache(cacheSize);
    }

    private ConcurrentMap<T, Result> createCache(int maxCacheSize) {
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.concurrencyLevel(64);
        cacheBuilder.initialCapacity(maxCacheSize);
        cacheBuilder.maximumSize(maxCacheSize);
        Cache<T, Result> localCache = cacheBuilder.build();
        ConcurrentMap<T, Result> cache = localCache.asMap();
        return cache;
    }

    public Result put(T value) {
        final Result find = this.cache.get(value);
        if (find != null) {
            return find;
        }
        
        // Use negative values too to reduce data size
        final int newId = BytesUtils.zigzagToInt(idGen.getAndIncrement());
        final Result result = new Result(false, newId);
        final Result before = this.cache.putIfAbsent(value, result);
        if (before != null) {
            return before;
        }
        return new Result(true, newId);
    }

}
