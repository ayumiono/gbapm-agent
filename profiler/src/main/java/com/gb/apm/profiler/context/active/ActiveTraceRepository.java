package com.gb.apm.profiler.context.active;

import java.util.List;

/**
 * @author Taejin Koo
 */
public interface ActiveTraceRepository {

    void put(ActiveTrace activeTrace);

    ActiveTrace remove(Long key);

    List<ActiveTraceInfo> collect();

}
