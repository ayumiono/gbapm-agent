package com.gb.apm.common.trace;

/**
 * 
 * @author netspider
 * @author emeroad
 * @author Jongho Moon
 */
public class DefaultAnnotationKey implements AnnotationKey {

    private final int code;
    private final String name;
    private final boolean viewInRecordSet;
    private final boolean errorApiMetadata;

    DefaultAnnotationKey(int code, String name, AnnotationKeyProperty... properties) {
        this.code = code;
        this.name = name;
        
        boolean viewInRecordSet = false;
        boolean errorApiMetadata = false;
        
        for (AnnotationKeyProperty property : properties) {
            switch (property) {
            case VIEW_IN_RECORD_SET:
                viewInRecordSet = true;
                break;
            case ERROR_API_METADATA:
                errorApiMetadata = true;
                break;
            }
        }
        
        this.viewInRecordSet = viewInRecordSet;
        this.errorApiMetadata = errorApiMetadata;
    }
    

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
    
    public boolean isErrorApiMetadata() {
        return errorApiMetadata;
    }

    public boolean isViewInRecordSet() {
        return viewInRecordSet;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnnotationKey{");
        sb.append("code=").append(code);
        sb.append(", name='").append(name);
        sb.append('}');
        return sb.toString();
    }
}