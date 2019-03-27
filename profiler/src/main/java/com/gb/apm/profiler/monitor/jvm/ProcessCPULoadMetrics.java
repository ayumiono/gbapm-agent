package com.gb.apm.profiler.monitor.jvm;

import java.lang.management.ManagementFactory;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.jvm.JmxAttributeGauge;

public class ProcessCPULoadMetrics implements Gauge<Double>{
	
	private JmxAttributeGauge processCpuTimeJmxAttr;
	
	public ProcessCPULoadMetrics() {
		try {
			processCpuTimeJmxAttr = new JmxAttributeGauge(new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME), "ProcessCpuLoad");
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Double getValue() {
		double value = (double) processCpuTimeJmxAttr.getValue();
		return ((int)(value * 1000) / 10.0);
	}

}
