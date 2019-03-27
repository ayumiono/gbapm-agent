package com.gb.apm.common.trace;

import com.gb.apm.common.utils.AnnotationKeyUtils;

/**
 * @author Jongho Moon
 *
 */
public final class AnnotationKeyMatchers {
    
    private static class ExactMatcher implements AnnotationKeyMatcher {
        private final int code;
        
        public ExactMatcher(AnnotationKey key) {
            this.code = key.getCode();
        }
    
        @Override
        public boolean matches(int code) {
            return this.code == code;
        }
    
        @Override
        public String toString() {
            return "ExactMatcher(" + code + ")";
        }
    }

    public static final AnnotationKeyMatcher NOTHING_MATCHER = new AnnotationKeyMatcher() {
        @Override
        public boolean matches(int code) {
            return false;
        }
    
        @Override
        public String toString() {
            return "NOTHING_MATCHER";
        }
    };
    public static final AnnotationKeyMatcher ARGS_MATCHER = new AnnotationKeyMatcher() {
        @Override
        public boolean matches(int code) {
            return AnnotationKeyUtils.isArgsKey(code);
        }
    
        @Override
        public String toString() {
            return "ARGS_MATCHER";
        }
    };

    private AnnotationKeyMatchers() { }

    public static AnnotationKeyMatcher exact(AnnotationKey key) {
        return new AnnotationKeyMatchers.ExactMatcher(key);
    }


}
