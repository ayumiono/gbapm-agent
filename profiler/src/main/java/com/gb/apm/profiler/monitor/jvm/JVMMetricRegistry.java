package com.gb.apm.profiler.monitor.jvm;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;

public class JVMMetricRegistry {
	
	private static String SQLLOG_FILE = "/var/log/asm/jvm.log";
	private static String SQLLOG_FILE_PATTERN;
	
	private static final String LAYOUT_PATTERN = "%msg%n";
	private static final String MAX_LOG_FILE_SIZE = "256MB";
	private static final String TOTAL_FILE_SIZE_CAP = "2GB";
	private static final int MAX_HISTORY = 2;
	
	private static final Logger jvmMetricsLogger = LoggerFactory.getLogger("jvmMetricsLogger");
	private static final String APPENDER_NAME = "babySitterAppender";
	
	public static JVMMetricRegistryBuilder config() {
		return new JVMMetricRegistryBuilder();
	}
	
	public static class JVMMetricRegistryBuilder {
		
		boolean cpuload;
		boolean thread;
		boolean memory;
		boolean gc;
		int duration;
		int delay;
		
		/**
		 * 每隔多少秒输出一次统计信息
		 * @param duration
		 * @return
		 */
		public JVMMetricRegistryBuilder duration(int duration) {
			this.duration = duration;
			return this;
		}
		
		public JVMMetricRegistryBuilder delay(int delay) {
			this.delay = delay;
			return this;
		}
		
		public JVMMetricRegistryBuilder output(String logpath) {
			SQLLOG_FILE = logpath;
			if(SQLLOG_FILE.indexOf(".")>-1) {
				String path = StringUtils.substringBeforeLast(SQLLOG_FILE, ".");
				String sufix = StringUtils.substringAfterLast(SQLLOG_FILE, ".");
				SQLLOG_FILE_PATTERN = path+"."+"%d{yyyy-MM-dd}_%i"+"."+sufix;
			}else {
				SQLLOG_FILE_PATTERN = SQLLOG_FILE + "_%d{yyyy-MM-dd}_%i";
			}
			return this;
		}
		
		public JVMMetricRegistryBuilder cpuload() {
			this.cpuload = true;
			return this;
		}
		
		public JVMMetricRegistryBuilder thread() {
			this.thread = true;
			return this;
		}
		
		public JVMMetricRegistryBuilder memory() {
			this.memory = true;
			return this;
		}
		
		public JVMMetricRegistryBuilder gc() {
			this.gc = true;
			return this;
		}
		
		public Slf4jJVMMetricsReporter build(JVMMetricsFactory factory) {
			MetricRegistry registry = new MetricRegistry();
			if(cpuload) {
				registry.register("jvm.cpuload", new ProcessCPULoadMetrics());
			}
			if(thread) {
				registry.register("jvm.thread", new ThreadingMXBeanMetrics());
			}
			if(memory) {
				registry.register("jvm", new MemoryPoolMXbeanMetrics());
			}
			if(gc) {
				registry.register("jvm.gc", new JVMGCMXBeanMetrics());
			}
			Slf4jJVMMetricsReporter reporter = Slf4jJVMMetricsReporter.forRegistry(registry,factory).outputTo(jvmMetricsLogger).build();
			reporter.start(delay, duration, TimeUnit.SECONDS);
			return reporter;
		}
	}
	
	public static void initAppender() {
		try {
			if(jvmMetricsLogger instanceof ch.qos.logback.classic.Logger) {
				ch.qos.logback.classic.Logger _logger = (ch.qos.logback.classic.Logger)jvmMetricsLogger;
				if(_logger.getAppender(APPENDER_NAME) == null) {
					_logger.setAdditive(false);
					_logger.setLevel(Level.INFO);
					RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
					appender.setContext(_logger.getLoggerContext());
					appender.setName(APPENDER_NAME);
					appender.setFile(SQLLOG_FILE);
					appender.setAppend(true);
					TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
					rollingPolicy.setContext(_logger.getLoggerContext());
					rollingPolicy.setFileNamePattern(SQLLOG_FILE_PATTERN);
					rollingPolicy.setMaxHistory(MAX_HISTORY);
					rollingPolicy.setTotalSizeCap(FileSize.valueOf(TOTAL_FILE_SIZE_CAP));
					rollingPolicy.setCleanHistoryOnStart(true);
					rollingPolicy.setParent(appender);
					SizeAndTimeBasedFNATP<ILoggingEvent> timeBasedTriggering = new SizeAndTimeBasedFNATP<>();
					timeBasedTriggering.setMaxFileSize(FileSize.valueOf(MAX_LOG_FILE_SIZE));
					rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(timeBasedTriggering);
					rollingPolicy.start();
					timeBasedTriggering.start();
					appender.setRollingPolicy(rollingPolicy);
					PatternLayoutEncoder encoder = new PatternLayoutEncoder();
					encoder.setPattern(LAYOUT_PATTERN);
					encoder.setContext(_logger.getLoggerContext());
					encoder.start();
					appender.setEncoder(encoder);
					appender.start();
					_logger.addAppender(appender);
				}
			}
		} catch (Exception e) {
		}
	}
}
