package com.gb.apm.bootstrap.core.instrument.matcher;

/**
 * @author emeroad
 */
public class DefaultClassNameMatcher implements ClassNameMatcher {
    private final String className;

    DefaultClassNameMatcher(String className) {
        if (className == null) {
            throw new NullPointerException("className must not be null");
        }
        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultClassNameMatcher that = (DefaultClassNameMatcher) o;

        return className.equals(that.className);

    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultClassNameMatcher{");
        sb.append(className);
        sb.append('}');
        return sb.toString();
    }
}
