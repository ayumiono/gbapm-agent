package com.gb.apm.bootstrap.core.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gb.apm.common.utils.AntPathMatcher;
import com.gb.apm.common.utils.EqualsPathMatcher;
import com.gb.apm.common.utils.PathMatcher;
import com.gb.apm.common.utils.StringUtils;

/**
 * @author emeroad
 * @author HyunGil Jeong
 */
public class ExcludePathFilter implements Filter<String> {

    public static final String DEFAULT_PATH_SEAPARATOR = "/";
    public static final String DEFAULT_FORMAT_SEPARATOR = ",";

    protected final List<PathMatcher> excludePathMatchers;

    public ExcludePathFilter(String excludePathFormatString) {
        this(excludePathFormatString, DEFAULT_PATH_SEAPARATOR);
    }

    public ExcludePathFilter(String excludePathFormatString, String pathSeparator) {
        this(excludePathFormatString, pathSeparator, DEFAULT_FORMAT_SEPARATOR);
    }

    public ExcludePathFilter(String excludePathFormatString, String pathSeparator, String formatSeparator) {
        if (StringUtils.isEmpty(pathSeparator)) {
            throw new IllegalArgumentException("pathSeparator must not be empty");
        }
        if (StringUtils.isEmpty(excludePathFormatString)) {
            this.excludePathMatchers = Collections.emptyList();
            return;
        }
        final List<String> excludePathFormats = StringUtils.splitAndTrim(excludePathFormatString, formatSeparator);
        final List<PathMatcher> excludePathMatchers = new ArrayList<PathMatcher>(excludePathFormats.size());
        for (String excludePathFormat : excludePathFormats) {
            final PathMatcher pathMatcher = createPathMatcher(excludePathFormat, pathSeparator);
            excludePathMatchers.add(pathMatcher);
        }
        this.excludePathMatchers = excludePathMatchers;
    }

    protected PathMatcher createPathMatcher(String pattern, String pathSeparator) {
        if (AntPathMatcher.isAntStylePattern(pattern)) {
            return new AntPathMatcher(pattern, pathSeparator);
        }
        return new EqualsPathMatcher(pattern);
    }

    @Override
    public boolean filter(String value) {
        for (PathMatcher excludePathMatcher : this.excludePathMatchers) {
            if (excludePathMatcher.isMatched(value)) {
                return FILTERED;
            }
        }
        return NOT_FILTERED;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcludePathFilter{");
        sb.append("excludePathMatchers=").append(excludePathMatchers);
        sb.append('}');
        return sb.toString();
    }
}
