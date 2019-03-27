package com.gb.apm.asm.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.interceptor.ApiIdAwareAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor0;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor1;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor2;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor3;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor4;
import com.gb.apm.bootstrap.core.interceptor.AroundInterceptor5;
import com.gb.apm.bootstrap.core.interceptor.Interceptor;
import com.gb.apm.bootstrap.core.interceptor.StaticAroundInterceptor;
import com.gb.apm.bootstrap.core.interceptor.annotation.IgnoreMethod;

/**
 * @author Woonduk Kang(emeroad)
 */
public class InterceptorDefinitionFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<TypeHandler> detectHandlers;

    public InterceptorDefinitionFactory() {
        this.detectHandlers = register();
    }

    public InterceptorDefinition createInterceptorDefinition(Class<?> interceptorClazz) {
        if (interceptorClazz == null) {
            throw new NullPointerException("targetInterceptorClazz must not be null");
        }

        for (TypeHandler typeHandler : detectHandlers) {
            final InterceptorDefinition interceptorDefinition = typeHandler.resolveType(interceptorClazz);
            if (interceptorDefinition != null) {
                return interceptorDefinition;
            }
        }
        throw new RuntimeException("unsupported Interceptor Type. " + interceptorClazz.getName());
    }


    private List<TypeHandler> register() {
        final List<TypeHandler> typeHandlerList = new ArrayList<TypeHandler>();

        addTypeHandler(typeHandlerList, AroundInterceptor.class, InterceptorType.ARRAY_ARGS);

        addTypeHandler(typeHandlerList, AroundInterceptor0.class, InterceptorType.BASIC);
        addTypeHandler(typeHandlerList, AroundInterceptor1.class, InterceptorType.BASIC);
        addTypeHandler(typeHandlerList, AroundInterceptor2.class, InterceptorType.BASIC);
        addTypeHandler(typeHandlerList, AroundInterceptor3.class, InterceptorType.BASIC);
        addTypeHandler(typeHandlerList, AroundInterceptor4.class, InterceptorType.BASIC);
        addTypeHandler(typeHandlerList, AroundInterceptor5.class, InterceptorType.BASIC);


        addTypeHandler(typeHandlerList, StaticAroundInterceptor.class, InterceptorType.STATIC);

        addTypeHandler(typeHandlerList, ApiIdAwareAroundInterceptor.class, InterceptorType.API_ID_AWARE);

        return typeHandlerList;
    }

    private void addTypeHandler(List<TypeHandler> typeHandlerList, Class<? extends Interceptor> interceptorClazz, InterceptorType arrayArgs) {
        final TypeHandler typeHandler = createInterceptorTypeHandler(interceptorClazz, arrayArgs);
        typeHandlerList.add(typeHandler);
    }

    private TypeHandler createInterceptorTypeHandler(Class<? extends Interceptor> interceptorClazz, InterceptorType interceptorType) {
        if (interceptorClazz == null) {
            throw new NullPointerException("targetInterceptorClazz must not be null");
        }
        if (interceptorType == null) {
            throw new NullPointerException("interceptorType must not be null");
        }

        final Method[] declaredMethods = interceptorClazz.getDeclaredMethods();
        if (declaredMethods.length != 2) {
            throw new RuntimeException("invalid Type");
        }
        final String before = "before";
        final Method beforeMethod = findMethodByName(declaredMethods, before);
        final Class<?>[] beforeParamList = beforeMethod.getParameterTypes();

        final String after = "after";
        final Method afterMethod = findMethodByName(declaredMethods, after);
        final Class<?>[] afterParamList = afterMethod.getParameterTypes();

        return new TypeHandler(interceptorClazz, interceptorType, before, beforeParamList, after, afterParamList);
    }


    private Method findMethodByName(Method[] declaredMethods, String methodName) {
        Method findMethod = null;
        int count = 0;
        for (Method method : declaredMethods) {
            if (method.getName().equals(methodName)) {
                count++;
                findMethod = method;
            }
        }
        if (findMethod == null) {
            throw new RuntimeException(methodName + " not found");
        }
        if (count > 1 ) {
            throw new RuntimeException("duplicated method exist. methodName:" + methodName);
        }
        return findMethod;
    }


    private class TypeHandler {
        private final Class<? extends Interceptor> interceptorClazz;
        private final InterceptorType interceptorType;
        private final String before;
        private final Class<?>[] beforeParamList;
        private final String after;
        private final Class<?>[] afterParamList;

        public TypeHandler(Class<? extends Interceptor> interceptorClazz, InterceptorType interceptorType, String before, final Class<?>[] beforeParamList, final String after, final Class<?>[] afterParamList) {
            if (interceptorClazz == null) {
                throw new NullPointerException("targetInterceptorClazz must not be null");
            }
            if (interceptorType == null) {
                throw new NullPointerException("interceptorType must not be null");
            }
            if (before == null) {
                throw new NullPointerException("before must not be null");
            }
            if (beforeParamList == null) {
                throw new NullPointerException("beforeParamList must not be null");
            }
            if (after == null) {
                throw new NullPointerException("after must not be null");
            }
            if (afterParamList == null) {
                throw new NullPointerException("afterParamList must not be null");
            }
            this.interceptorClazz = interceptorClazz;
            this.interceptorType = interceptorType;
            this.before = before;
            this.beforeParamList = beforeParamList;
            this.after = after;
            this.afterParamList = afterParamList;
        }


        public InterceptorDefinition resolveType(Class<?> targetClazz) {
            if(!this.interceptorClazz.isAssignableFrom(targetClazz)) {
                return null;
            }
            @SuppressWarnings("unchecked")
            final Class<? extends Interceptor> casting = (Class<? extends Interceptor>) targetClazz;
            return createInterceptorDefinition(casting);
        }

        private InterceptorDefinition createInterceptorDefinition(Class<? extends Interceptor> targetInterceptorClazz) {

            final Method beforeMethod = searchMethod(targetInterceptorClazz, before, beforeParamList);
            if (beforeMethod == null) {
                throw new RuntimeException(before + " method not found. " + Arrays.toString(beforeParamList));
            }
            final boolean beforeIgnoreMethod = beforeMethod.isAnnotationPresent(IgnoreMethod.class);


            final Method afterMethod = searchMethod(targetInterceptorClazz, after, afterParamList);
            if (afterMethod == null) {
                throw new RuntimeException(after + " method not found. " + Arrays.toString(afterParamList));
            }
            final boolean afterIgnoreMethod = afterMethod.isAnnotationPresent(IgnoreMethod.class);


            if (beforeIgnoreMethod == true && afterIgnoreMethod == true) {
                return new DefaultInterceptorDefinition(interceptorClazz, targetInterceptorClazz, interceptorType, CaptureType.NON, null, null);
            }
            if (beforeIgnoreMethod == true) {
                return new DefaultInterceptorDefinition(interceptorClazz, targetInterceptorClazz, interceptorType, CaptureType.AFTER, null, afterMethod);
            }
            if (afterIgnoreMethod == true) {
                return new DefaultInterceptorDefinition(interceptorClazz, targetInterceptorClazz, interceptorType, CaptureType.BEFORE, beforeMethod, null);
            }
            return new DefaultInterceptorDefinition(interceptorClazz, targetInterceptorClazz, interceptorType, CaptureType.AROUND, beforeMethod, afterMethod);
        }

        private Method searchMethod(Class<?> interceptorClazz, String searchMethodName, Class<?>[] searchMethodParameter) {
            if (searchMethodName == null) {
                throw new NullPointerException("methodList must not be null");
            }
//          only DeclaredMethod search ?
//            try {
//                return targetInterceptorClazz.getDeclaredMethod(searchMethodName, searchMethodParameter);
//            } catch (NoSuchMethodException ex) {
//                logger.debug(searchMethodName + " DeclaredMethod not found. search parent class");
//            }
            // search all class
            try {
                return interceptorClazz.getMethod(searchMethodName, searchMethodParameter);
            } catch (NoSuchMethodException ex) {
                logger.debug(searchMethodName +" DeclaredMethod not found.");
            }
            return null;
        }
    }



}
