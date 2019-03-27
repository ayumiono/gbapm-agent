package com.gb.apm.profiler.plugin;

import java.lang.instrument.ClassFileTransformer;
import java.util.List;

import com.gb.apm.bootstrap.core.plugin.ApplicationTypeDetector;
import com.gb.apm.bootstrap.core.plugin.jdbc.JdbcUrlParserV2;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface PluginContextLoadResult {

    List<ClassFileTransformer> getClassFileTransformer();

    List<ApplicationTypeDetector> getApplicationTypeDetectorList();

    List<JdbcUrlParserV2> getJdbcUrlParserList();

}
