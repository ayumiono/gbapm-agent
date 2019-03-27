package com.gb.apm.bootstrap.core.plugin.jdbc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.instrument.MethodFilter;

/**
 * @author Jongho Moon
 *
 */
public class PreparedStatementBindingMethodFilter implements MethodFilter {
    private static final Map<String, List<String[]>> BIND_METHODS;

    static {
        List<Method> methods = PreparedStatementUtils.findBindVariableSetMethod();
        BIND_METHODS = new HashMap<String, List<String[]>>();
        
        for (Method method : methods) {
            List<String[]> list = BIND_METHODS.get(method.getName());
            
            if (list == null) {
                list = new ArrayList<String[]>();
                BIND_METHODS.put(method.getName(), list);
            }
            
            Class<?>[] paramTypes = method.getParameterTypes();
            int len = paramTypes.length;
            String[] paramTypeNames = new String[len];
            
            for (int i = 0; i < len; i++) {
                paramTypeNames[i] = paramTypes[i].getName();
            }
            
            list.add(paramTypeNames);
        }
    }
    
    
    public static PreparedStatementBindingMethodFilter includes(String... names) {
        Map<String, List<String[]>> targets = new HashMap<String, List<String[]>>(names.length);
        
        for (String name : names) {
            List<String[]> paramTypes = BIND_METHODS.get(name);
            
            if (paramTypes != null) {
                targets.put(name, paramTypes);
            }
        }
        
        return new PreparedStatementBindingMethodFilter(targets);
    }

    public static PreparedStatementBindingMethodFilter excludes(String... names) {
        Map<String, List<String[]>> targets = new HashMap<String, List<String[]>>(BIND_METHODS);
        
        for (String name : names) {
            targets.remove(name);
        }
        
        return new PreparedStatementBindingMethodFilter(targets);
    }

    
    private final Map<String, List<String[]>> methods;
    
    public PreparedStatementBindingMethodFilter() {
        this.methods = BIND_METHODS;
    }
    
    public PreparedStatementBindingMethodFilter(Map<String, List<String[]>> targets) {
        this.methods = targets;
    }

    @Override
    public boolean accept(InstrumentMethod method) {
        List<String[]> paramTypes = methods.get(method.getName());
        
        if (paramTypes == null) {
            return REJECT;
        }
        
        for (String[] types : paramTypes) {
            if (Arrays.equals(types, method.getParameterTypes())) {
                return ACCEPT;
            }
        }

        return REJECT;
    }

}
