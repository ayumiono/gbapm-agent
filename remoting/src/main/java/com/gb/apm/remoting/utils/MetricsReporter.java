package com.gb.apm.remoting.utils;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jmx.JmxReporter;

public class MetricsReporter {
	
	final static MetricRegistry metrics = new MetricRegistry();
	
	final static JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
	
	static {
		reporter.start();
	}

	public static Timer timer(String name) {
		return metrics.timer(name);
	}
	
	public static <T extends Metric> T register(String name, T metric) {
		return metrics.register(name, metric);
	}
}
