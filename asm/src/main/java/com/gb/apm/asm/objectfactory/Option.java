package com.gb.apm.asm.objectfactory;

// TODO move package
public abstract class Option {
    public abstract Object getValue();
    public abstract boolean hasValue();
    
    public static Option withValue(Object value) {
        return new WithValue(value);
    }
    
    @SuppressWarnings("unchecked")
    public static Option empty() {
        return (Option)EMPTY;
    }
    
    private static final class WithValue extends Option {
        private final Object value;
        
        private WithValue(Object value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public boolean hasValue() {
            return true;
        }
        
    }
 
    private static final Option EMPTY = new Option() {

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean hasValue() {
            return false;
        }
    };
    
}
