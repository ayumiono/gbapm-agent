package com.gb.apm.collector.log;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gb.apm.bootstrap.core.config.ProfilerConfig;
import com.gb.apm.collector.QueueDataSender;
import com.gb.apm.model.APMModel;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;

public class LogQueueDataSender extends QueueDataSender {
	
	private final Logger finishListenerLogger = LoggerFactory.getLogger("finishListenerLogger");

	private AtomicBoolean initFlag = new AtomicBoolean(false);
	private ProfilerConfig config;
	private String SQLLOG_FILE;
	private String SQLLOG_FILE_PATTERN;

	private static final String LAYOUT_PATTERN = "%msg%n";
	private static final String MAX_LOG_FILE_SIZE = "256MB";
	private static final String TOTAL_FILE_SIZE_CAP = "2GB";
	private static final int MAX_HISTORY = 2;
	private static final String APPENDER_NAME = "babySitterAppender";

	private void initAppender() {
		try {
			SQLLOG_FILE = config.readString("profiler.asm.log", "/tmp/asm.log");
			if (SQLLOG_FILE.indexOf(".") > -1) {
				String path = StringUtils.substringBeforeLast(SQLLOG_FILE, ".");
				String sufix = StringUtils.substringAfterLast(SQLLOG_FILE, ".");
				SQLLOG_FILE_PATTERN = path + "." + "%d{yyyy-MM-dd}_%i" + "." + sufix;
			} else {
				SQLLOG_FILE_PATTERN = SQLLOG_FILE + "_%d{yyyy-MM-dd}_%i";
			}
			if (finishListenerLogger instanceof ch.qos.logback.classic.Logger) {
				ch.qos.logback.classic.Logger _logger = (ch.qos.logback.classic.Logger) finishListenerLogger;
				if (_logger.getAppender(APPENDER_NAME) == null) {
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
			} else {
			}
		} catch (Exception e) {
			System.err.println("finishListenerLogger init failed! log only support logback implements");
		}
	}

	public LogQueueDataSender(InvokeStackEvent interest,ProfilerConfig config) {
		super(interest);
		this.config = config;
	}

	@Override
	public boolean deal(APMModel frame) {
		try {
			String json = JSON.toJSONString(frame);
			if(!initFlag.get()) {
				initAppender();
				initFlag.compareAndSet(false, true);
			}
			finishListenerLogger.info(json);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("finishListener处理失败{}",e.getMessage());
			return false;
		}
	}
}
