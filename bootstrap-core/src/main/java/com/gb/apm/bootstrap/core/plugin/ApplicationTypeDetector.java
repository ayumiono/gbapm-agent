package com.gb.apm.bootstrap.core.plugin;

import com.gb.apm.bootstrap.core.resolver.ConditionProvider;
import com.gb.apm.common.trace.ServiceType;


/**
 * @author Jongho Moon
 *
 */
public interface ApplicationTypeDetector {
    
    /**
     * Returns the {@link ServiceType} representing the current plugin, 
     * with code in a range corresponding to {@link com.navercorp.pinpoint.common.trace.ServiceTypeCategory#SERVER}
     * 
     * @return the {@link ServiceType} representing the current plugin
     * @see ServiceType#isWas()
     * @see com.navercorp.pinpoint.common.trace.ServiceTypeCategory#SERVER
     */
    ServiceType getApplicationType();
    
    /**
     * Checks whether the provided conditions satisfy the requirements given by the plugins implementing this class.
     * 
     * <p>This method allows the agent to go through each of the registered plugins with classes implementing this interface,
     * checking whether the execution environment satisfies the requirements specified in them, returning <tt>true</tt> if the
     * requirements are satisfied.
     * 
     * @param provider conditions provided by the current application
     * @return <tt>true</tt> if the provided conditions satisfy the requirements, <tt>false</tt> if otherwise
     * @see ConditionProvider
     */
    boolean detect(ConditionProvider provider);
}
