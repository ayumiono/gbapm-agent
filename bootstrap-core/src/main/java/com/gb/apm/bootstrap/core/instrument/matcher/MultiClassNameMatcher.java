package com.gb.apm.bootstrap.core.instrument.matcher;

import java.util.List;

/**
 * @author emeroad
 */
public interface MultiClassNameMatcher extends ClassMatcher {
    List<String> getClassNames();
}
