package com.gb.apm.profiler.context.provider;

import com.gb.apm.collector.EnhancedDataSender;
import com.gb.apm.collector.netty.TcpEnhancedDataSender;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Woonduk Kang(emeroad)
 */
public class TcpEnhancedDataSenderProvider implements Provider<EnhancedDataSender> {
	

	@Inject
    public TcpEnhancedDataSenderProvider() {
    }

    @Override
    public EnhancedDataSender get() {
        return new TcpEnhancedDataSender();
    }
    
}
