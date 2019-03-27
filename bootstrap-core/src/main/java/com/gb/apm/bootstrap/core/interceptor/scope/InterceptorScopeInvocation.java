package com.gb.apm.bootstrap.core.interceptor.scope;


/**
 * 
 * 
 * @author emeroad
 * @author Jongho Moon
 */
public interface InterceptorScopeInvocation {
    String getName();

    boolean tryEnter(ExecutionPolicy policy);
    boolean canLeave(ExecutionPolicy policy);
    void leave(ExecutionPolicy policy);
    
    boolean isActive();
    
    Object setAttachment(Object attachment);
    Object getAttachment();
    Object getOrCreateAttachment(AttachmentFactory factory);
    Object removeAttachment();
}
