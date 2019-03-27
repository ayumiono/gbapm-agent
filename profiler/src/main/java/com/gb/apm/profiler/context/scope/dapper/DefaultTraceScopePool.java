package com.gb.apm.profiler.context.scope.dapper;

import com.gb.apm.common.utils.NameValueList;
import com.gb.apm.dapper.context.scope.TraceScope;

/**
 * @author jaehong.kim
 */
public class DefaultTraceScopePool {

    private final NameValueList<TraceScope> list = new NameValueList<TraceScope>();

    public TraceScope get(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        return list.get(name);
    }

    public TraceScope add(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        final TraceScope oldScope = list.add(name, new DefaultTraceScope(name));
        return oldScope;
    }

    public void clear() {
        list.clear();
    }
}