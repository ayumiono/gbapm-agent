package com.gb.apm.asm.objectfactory;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Jongho Moon
 *
 */
public class ArgumentsResolver {

    private final List<ArgumentProvider> parameterResolvers;

    public ArgumentsResolver(List<ArgumentProvider> parameterResolvers) {
        this.parameterResolvers = parameterResolvers;
    }

    public Object[] resolve(Class<?>[] types, Annotation[][] annotations) {
        int length = types.length;
        Object[] arguments = new Object[length];
        
        for (ArgumentProvider resolver : parameterResolvers) {
            if (resolver instanceof JudgingParameterResolver) {
                ((JudgingParameterResolver)resolver).prepare();
            }
        }
        
        outer:
        for (int i = 0; i < length; i++) {
            for (ArgumentProvider resolver : parameterResolvers) {
                Option resolved = resolver.get(i, types[i], annotations[i]);
                
                if (resolved.hasValue()) {
                    arguments[i] = resolved.getValue();
                    continue outer;
                }
            }
            
            return null;
        }
        
        for (ArgumentProvider resolver : parameterResolvers) {
            if (resolver instanceof JudgingParameterResolver) {
                if (!((JudgingParameterResolver)resolver).isAcceptable()) {
                    return null;
                }
            }
        }
        
        return arguments;
    }
}