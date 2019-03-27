package com.gb.apm.bootstrap.core.instrument.matcher;

import java.util.Collections;
import java.util.List;

/**
 * @author emeroad
 */
public class DefaultMultiClassNameMatcher implements MultiClassNameMatcher {

    private final List<String> classNameList;

    DefaultMultiClassNameMatcher(List<String> classNameMatcherList) {
        if (classNameMatcherList == null) {
            throw new NullPointerException("classNameMatcherList must not be null");
        }
        this.classNameList = Collections.unmodifiableList(classNameMatcherList);
    }

    @Override
    public List<String> getClassNames() {
        return classNameList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultMultiClassNameMatcher that = (DefaultMultiClassNameMatcher) o;

        return classNameList.equals(that.classNameList);

    }

    @Override
    public int hashCode() {
        return classNameList.hashCode();
    }
}
