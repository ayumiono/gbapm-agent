package com.gb.apm.bootstrap.core.instrument;

/**
 * @author emeroad
 */
public class DefaultInterceptorScopeDefinition implements InterceptorScopeDefinition {

    private final String name;

    public DefaultInterceptorScopeDefinition(String name) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultInterceptorScopeDefinition that = (DefaultInterceptorScopeDefinition) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return result;
    }
}
