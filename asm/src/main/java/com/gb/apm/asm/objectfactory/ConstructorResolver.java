package com.gb.apm.asm.objectfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Jongho Moon
 *
 */
public class ConstructorResolver {
    private final Class<?> type;
    private final ArgumentsResolver argumentsResolver;
    
    private Constructor<?> resolvedConstructor;
    private Object[] resolvedArguments;
    
    public ConstructorResolver(Class<?> type, ArgumentsResolver argumentsResolver) {
        this.type = type;
        this.argumentsResolver = argumentsResolver;
    }

    public boolean resolve() {
        Constructor<?>[] constructors = (Constructor<?>[]) type.getConstructors();
        Arrays.sort(constructors, CONSTRUCTOR_COMPARATOR);
        
        for (Constructor<?> constructor : constructors) {
            Class<?>[] types = constructor.getParameterTypes();
            Annotation[][] annotations = constructor.getParameterAnnotations();

            Object[] resolvedArguments = argumentsResolver.resolve(types, annotations);
            
            if (resolvedArguments != null) {
                this.resolvedConstructor = constructor;
                this.resolvedArguments = resolvedArguments;

                return true;
            }
        }
        
        return false;
    }

    public Constructor<?> getResolvedConstructor() {
        return resolvedConstructor;
    }

    public Object[] getResolvedArguments() {
        return resolvedArguments;
    }

    private static final Comparator<Constructor<?>> CONSTRUCTOR_COMPARATOR = new Comparator<Constructor<?>>() {

        @Override
        public int compare(Constructor<?> o1, Constructor<?> o2) {
            int p1 = o1.getParameterTypes().length;
            int p2 = o2.getParameterTypes().length;
            
            return (p1 < p2) ? 1 : ((p1 == p2) ? 0 : -1);
        }
        
    };
}
