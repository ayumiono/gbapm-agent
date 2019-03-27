package com.gb.apm.profiler.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ClassNameFilterChain implements ClassNameFilter {

    private final List<ClassNameFilter> filterChain;

    public ClassNameFilterChain(List<ClassNameFilter> filterChain) {
        if (filterChain == null) {
            throw new NullPointerException("filterChain must not be null");
        }
        this.filterChain = new ArrayList<ClassNameFilter>(filterChain);
    }


    @Override
    public boolean accept(String className) {
        for (ClassNameFilter classNameFilter : this.filterChain) {
            if (!classNameFilter.accept(className)) {
                return REJECT;
            }
        }
        return ACCEPT;
    }
}
