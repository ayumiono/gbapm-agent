package com.gb.apm.profiler.monitor.jvm;

import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;
import com.gb.apm.profiler.monitor.jvm.JVMGCMXBeanMetrics.JVMGCMetrics;
import com.gb.apm.profiler.monitor.jvm.MemoryPoolMXbeanMetrics.JvmMemoryUsage;
import com.gb.apm.profiler.monitor.jvm.ThreadingMXBeanMetrics.JVMThreadMetrics;

public class Slf4jJVMMetricsReporter extends ScheduledReporter {
	
	public static Builder forRegistry(MetricRegistry registry,JVMMetricsFactory factory) {
        return new Builder(registry,factory);
    }

	public static class Builder {
		private final MetricRegistry registry;
		private Logger logger;
		private TimeUnit rateUnit;
		private TimeUnit durationUnit;
		private MetricFilter filter;
		private ScheduledExecutorService executor;
		private boolean shutdownExecutorOnStop;
		private JVMMetricsFactory metricsFactory;

		private Builder(MetricRegistry registry,JVMMetricsFactory factory) {
			this.registry = registry;
			this.logger = LoggerFactory.getLogger("metrics");
			this.rateUnit = TimeUnit.SECONDS;
			this.durationUnit = TimeUnit.MILLISECONDS;
			this.filter = MetricFilter.ALL;
			this.executor = null;
			this.shutdownExecutorOnStop = true;
			this.metricsFactory = factory;
		}

		/**
		 * Specifies whether or not, the executor (used for reporting) will be stopped
		 * with same time with reporter. Default value is true. Setting this parameter
		 * to false, has the sense in combining with providing external managed executor
		 * via {@link #scheduleOn(ScheduledExecutorService)}.
		 *
		 * @param shutdownExecutorOnStop
		 *            if true, then executor will be stopped in same time with this
		 *            reporter
		 * @return {@code this}
		 */
		public Builder shutdownExecutorOnStop(boolean shutdownExecutorOnStop) {
			this.shutdownExecutorOnStop = shutdownExecutorOnStop;
			return this;
		}

		/**
		 * Specifies the executor to use while scheduling reporting of metrics. Default
		 * value is null. Null value leads to executor will be auto created on start.
		 *
		 * @param executor
		 *            the executor to use while scheduling reporting of metrics.
		 * @return {@code this}
		 */
		public Builder scheduleOn(ScheduledExecutorService executor) {
			this.executor = executor;
			return this;
		}

		/**
		 * Log metrics to the given logger.
		 *
		 * @param logger
		 *            an SLF4J {@link Logger}
		 * @return {@code this}
		 */
		public Builder outputTo(Logger logger) {
			this.logger = logger;
			return this;
		}

		/**
		 * Convert rates to the given time unit.
		 *
		 * @param rateUnit
		 *            a unit of time
		 * @return {@code this}
		 */
		public Builder convertRatesTo(TimeUnit rateUnit) {
			this.rateUnit = rateUnit;
			return this;
		}

		/**
		 * Convert durations to the given time unit.
		 *
		 * @param durationUnit
		 *            a unit of time
		 * @return {@code this}
		 */
		public Builder convertDurationsTo(TimeUnit durationUnit) {
			this.durationUnit = durationUnit;
			return this;
		}

		public Builder filter(MetricFilter filter) {
			this.filter = filter;
			return this;
		}

		public Slf4jJVMMetricsReporter build() {
			return new Slf4jJVMMetricsReporter(registry, metricsFactory, logger, rateUnit, durationUnit, filter, executor,
					shutdownExecutorOnStop);
		}
	}
	
	
	private final Logger logger;
	private final JVMMetricsFactory factory;
	
	
	static class MemoryUsageOutputter {
		private Gauge<JvmMemoryUsage> memoryUsage;
		
		public MemoryUsageOutputter(Gauge<JvmMemoryUsage> memoryUsage) {
			this.memoryUsage = memoryUsage;
		}
		
		public MemoryUsage memoryUsage() {
			return memoryUsage.getValue().getMemoryUsage();
		}
		
		public Double usage() {
			return memoryUsage.getValue().getUsageRatio();
		}
		public JvmMemoryUsage output() {
			return memoryUsage.getValue();
		}
	}
	
	static class ProcessCpuLoadOutputter {
		private final Gauge<Double> load;
		
		public ProcessCpuLoadOutputter(Gauge<Double> load) {
			this.load = load;
		}
		
		public Double output() {
			return load.getValue();
		}
	}
	
	private AtomicReference<ProcessCpuLoadOutputter> processCpuLoadGauge = new AtomicReference<>();
	private AtomicReference<MemoryUsageOutputter> heapMemGauge = new AtomicReference<>();
	private AtomicReference<MemoryUsageOutputter> nonHeapMemoGauge = new AtomicReference<>();
	private AtomicReference<Map<String, MemoryUsageOutputter>> memPools = new AtomicReference<>();
	private AtomicReference<Gauge<JVMThreadMetrics>> threadGauge = new AtomicReference<>();
	private AtomicReference<Gauge<List<JVMGCMetrics>>> gcGauge = new AtomicReference<>();
	
	
	private AtomicBoolean loggerInit = new AtomicBoolean(false);
	private AtomicBoolean cached = new AtomicBoolean(false);

	private Slf4jJVMMetricsReporter(MetricRegistry registry, JVMMetricsFactory factory, Logger logger,TimeUnit rateUnit, TimeUnit durationUnit,
			MetricFilter filter, ScheduledExecutorService executor, boolean shutdownExecutorOnStop) {
		super(registry, "jvm-metrics-logger-reporter", filter, rateUnit, durationUnit, executor,
				shutdownExecutorOnStop);
		this.logger = logger;
		this.factory = factory;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
			SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
		
		if(!cached.get()) {//结构化输出
			
			Map<String, MemoryUsageOutputter> mempools = new HashMap<>();
			for (Entry<String, Gauge> entry : gauges.entrySet()) {
				String name = entry.getKey();
				if(name.contains("jvm.cpuload")) {
					ProcessCpuLoadOutputter cupload = new ProcessCpuLoadOutputter(entry.getValue());
					processCpuLoadGauge.compareAndSet(null, cupload);
				}
				if(name.contains("jvm.thread")) {
					threadGauge.compareAndSet(null, entry.getValue());
				}
				if(name.contains("jvm.heap")) {
					MemoryUsageOutputter heapMem = new MemoryUsageOutputter(entry.getValue());
					heapMemGauge.compareAndSet(null, heapMem);
				}
				if(name.contains("jvm.non-heap")) {
					MemoryUsageOutputter nonHeapMem = new MemoryUsageOutputter(entry.getValue());
					nonHeapMemoGauge.compareAndSet(null, nonHeapMem);
				}
				if(name.contains("jvm.pools")) {
					MemoryUsageOutputter pool = new MemoryUsageOutputter(entry.getValue());
					mempools.put(entry.getKey(), pool);
				}
				if(name.contains("jvm.gc")) {
					gcGauge.compareAndSet(null, entry.getValue());
				}
			}
			memPools.compareAndSet(null, mempools);
			cached.compareAndSet(false, true);
		}
		
		JVMMetrics jvmmetrics = factory.createJVMMetrics();
		
		if(cached.get()) {
			if(processCpuLoadGauge.get() != null) {
				jvmmetrics.setProcessCpuLoad(processCpuLoadGauge.get().output());
			}
			if(threadGauge.get() != null) {
				jvmmetrics.setThread(threadGauge.get().getValue());
			}
			if(heapMemGauge.get() != null) {
				jvmmetrics.setHeapMemory(heapMemGauge.get().output());
			}
			if(nonHeapMemoGauge.get() != null) {
				jvmmetrics.setNonHeapMemory(nonHeapMemoGauge.get().output());
			}
			Map<String, JvmMemoryUsage> memoryPools = new HashMap<>();
			for(Entry<String, MemoryUsageOutputter> entry : memPools.get().entrySet()) {
				memoryPools.put(entry.getKey(), entry.getValue().output());
			}
			if(gcGauge.get() != null) {
				jvmmetrics.setGc(gcGauge.get().getValue());
			}
			jvmmetrics.setMemoryPool(memoryPools);
		}
		
		try {
			if(!loggerInit.get()) {
				JVMMetricRegistry.initAppender();
				loggerInit.compareAndSet(false, true);
			}
			logger.info(JSON.toJSONString(jvmmetrics));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
