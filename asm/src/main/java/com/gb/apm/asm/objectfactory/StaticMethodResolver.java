package com.gb.apm.asm.objectfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jongho Moon
 *
 */
public class StaticMethodResolver {
    private final Class<?> type;
    private final String methodName;
    private final ArgumentsResolver argumentsResolver;
    
    private Method resolvedMethod;
    private Object[] resolvedArguments;
    
    public StaticMethodResolver(Class<?> type, String methodName, ArgumentsResolver argumentsResolver) {
        this.type = type;
        this.methodName = methodName;
        this.argumentsResolver = argumentsResolver;
    }
    
    private List<Method> getCandidates() {
        List<Method> result = new ArrayList<Method>();
        
        for (Method method : type.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            
            if (!method.getName().equals(methodName)) {
                continue;
            }
            
            result.add(method);
        }
        
        Collections.sort(result, COMPARATOR);
        return result;
    }

    public boolean resolve() {
        List<Method> candidates = getCandidates();
        
        for (Method method : candidates) {
            Class<?>[] types = method.getParameterTypes();
            Annotation[][] annotations = method.getParameterAnnotations();

            Object[] arguments = argumentsResolver.resolve(types, annotations);
            
            if (arguments != null) {
                this.resolvedMethod = method;
                this.resolvedArguments = arguments;

                return true;
            }
        }
        
        return false;
    }

    public Method getResolvedMethod() {
        return resolvedMethod;
    }

    public Object[] getResolvedArguments() {
        return resolvedArguments;
    }

    private static final Comparator<Method> COMPARATOR = new Comparator<Method>() {

        @Override
        public int compare(Method o1, Method o2) {
            int p1 = o1.getParameterTypes().length;
            int p2 = o2.getParameterTypes().length;
            
            return (p1 < p2) ? 1 : ((p1 == p2) ? 0 : -1);
        }
        
    };
}
