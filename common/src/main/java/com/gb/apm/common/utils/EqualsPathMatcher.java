package com.gb.apm.common.utils;

/**
 * @author emeroad
 */
public class EqualsPathMatcher implements PathMatcher {

    private final String pattern;

    public EqualsPathMatcher(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern must not be null");
        }
        this.pattern = pattern;
    }

    @Override
    public boolean isMatched(String path) {
        return pattern.equals(path);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EqualsPathMatcher{");
        sb.append("pattern='").append(pattern).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
