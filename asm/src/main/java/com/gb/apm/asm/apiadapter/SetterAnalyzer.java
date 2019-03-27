package com.gb.apm.asm.apiadapter;

import java.lang.reflect.Method;

import com.gb.apm.common.utils.Asserts;

/**
 * @author HyunGil Jeong
 */
public class SetterAnalyzer {

    public SetterDetails analyze(Class<?> setterType) {
        Asserts.notNull(setterType, "setterType");

        if (!setterType.isInterface()) {
            throw new IllegalArgumentException("setterType " + setterType + "is not an interface");
        }

        Method[] methods = setterType.getDeclaredMethods();

        if (methods.length != 1) {
            throw new IllegalArgumentException("Setter interface must have only one method: " + setterType.getName());
        }

        Method setter = methods[0];
        Class<?>[] arguments = setter.getParameterTypes();

        if (arguments.length != 1) {
            throw new IllegalArgumentException("Setter interface method must have exactly 1 argument: " + setterType.getName());
        }

        Class<?> fieldType = arguments[0];

        Class<?> returnType = setter.getReturnType();

        if (returnType != void.class) {
            throw new IllegalArgumentException("Setter must have return type void: " + setterType.getName());
        }

        return new SetterDetails(setter, fieldType);
    }

    public static final class SetterDetails {
        private final Method setter;
        private final Class<?> fieldType;

        public SetterDetails(Method setter, Class<?> fieldType) {
            this.setter = setter;
            this.fieldType = fieldType;
        }

        public Method getSetter() {
            return setter;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }
    }
}
