package com.gb.apm.common.utils;

/**
 * @author emeroad
 */
public class AntPathMatcher implements PathMatcher {

    private final com.gb.apm.common.utils.spring.AntPathMatcher springAntMatcher;
    private final String pattern;

    public AntPathMatcher(String pattern) {
        this(pattern, com.gb.apm.common.utils.spring.AntPathMatcher.DEFAULT_PATH_SEPARATOR);
    }

    public AntPathMatcher(String pattern, String pathSeparator) {
        if (pattern == null) {
            throw new NullPointerException("pattern must not be null");
        }
        if (pathSeparator == null) {
            throw new NullPointerException("pathSeparator must not be null");
        }
        this.pattern = pattern;
        this.springAntMatcher = new com.gb.apm.common.utils.spring.AntPathMatcher(pathSeparator);
        preCreatePatternCache();
    }

    private void preCreatePatternCache() {
        // dummy call
        this.springAntMatcher.match(pattern, "/");
    }

    @Override
    public boolean isMatched(String path) {
        if (path == null) {
            return false;
        }
        return this.springAntMatcher.match(pattern, path);
    }

    public static boolean isAntStylePattern(String pattern) {
        // copy AntPathMatcher.isPattern(String path);
        return (pattern.indexOf('*') != -1 || pattern.indexOf('?') != -1);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AntPathMatcher{");
        sb.append("pattern='").append(pattern).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
