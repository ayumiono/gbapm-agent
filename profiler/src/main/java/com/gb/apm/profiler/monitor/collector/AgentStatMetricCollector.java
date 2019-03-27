package com.gb.apm.profiler.monitor.collector;

/**
 * @author HyunGil Jeong
 */
public interface AgentStatMetricCollector<T> {
    T collect();
}
