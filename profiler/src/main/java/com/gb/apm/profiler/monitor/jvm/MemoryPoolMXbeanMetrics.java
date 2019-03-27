package com.gb.apm.profiler.monitor.jvm;

import static com.codahale.metrics.MetricRegistry.name;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.RatioGauge.Ratio;

/**
 * 堆内存分代统计信息
 * @author xuelong.chen
 *
 */
public class MemoryPoolMXbeanMetrics implements MetricSet{
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
	private final MemoryMXBean mxBean;
	private final List<MemoryPoolMXBean> memoryPools;
	
	public MemoryPoolMXbeanMetrics() {
		this(ManagementFactory.getMemoryMXBean(),ManagementFactory.getMemoryPoolMXBeans());
	}
	
	public MemoryPoolMXbeanMetrics(MemoryMXBean mxBean, Collection<MemoryPoolMXBean> memoryPools) {
		this.mxBean = mxBean;
		this.memoryPools = new ArrayList<>(memoryPools);
	}

	@Override
	public Map<String, Metric> getMetrics() {
		final Map<String, Metric> gauges = new HashMap<>();
		gauges.put("heap", (Gauge<JvmMemoryUsage>) () -> {return new JvmMemoryUsage(mxBean.getHeapMemoryUsage(), persentage(Ratio.of(mxBean.getHeapMemoryUsage().getUsed(), mxBean.getHeapMemoryUsage().getMax() == -1 ? mxBean.getHeapMemoryUsage().getCommitted() : mxBean.getHeapMemoryUsage().getMax()).getValue()));});
		gauges.put("non-heap", (Gauge<JvmMemoryUsage>)() -> {return new JvmMemoryUsage(mxBean.getNonHeapMemoryUsage(), persentage(Ratio.of(mxBean.getNonHeapMemoryUsage().getUsed(),mxBean.getNonHeapMemoryUsage().getMax() == -1 ? mxBean.getNonHeapMemoryUsage().getCommitted() : mxBean.getNonHeapMemoryUsage().getMax()).getValue()));});
		for(MemoryPoolMXBean pool : memoryPools) {
			final String poolName = name("pools", WHITESPACE.matcher(pool.getName()).replaceAll("-"));
			gauges.put(poolName, (Gauge<JvmMemoryUsage>) () -> {return new JvmMemoryUsage(pool.getUsage(), persentage(Ratio.of(pool.getUsage().getUsed(),
					pool.getUsage().getMax() == -1 ? pool.getUsage().getCommitted() : pool.getUsage().getMax()).getValue()));});
		}
		return Collections.unmodifiableMap(gauges);
	}
	
	
	static class JvmMemoryUsage {
		private MemoryUsage memoryUsage;
		private double usageRatio;
		
		public MemoryUsage getMemoryUsage() {
			return memoryUsage;
		}

		public void setMemoryUsage(MemoryUsage memoryUsage) {
			this.memoryUsage = memoryUsage;
		}

		public double getUsageRatio() {
			return usageRatio;
		}

		public void setUsageRatio(double usageRatio) {
			this.usageRatio = usageRatio;
		}

		public JvmMemoryUsage(MemoryUsage memoryUsage,double usageRatio) {
			this.memoryUsage = memoryUsage;
			this.usageRatio = usageRatio;
		}
	}
	
	private static double persentage(double ratio) {
		double f = ratio * 1000 / 10;
		BigDecimal bg = new BigDecimal(f);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
}
