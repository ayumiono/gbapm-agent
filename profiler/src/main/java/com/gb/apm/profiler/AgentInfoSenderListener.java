package com.gb.apm.profiler;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.collector.Future;
import com.gb.apm.collector.FutureListener;
import com.gb.apm.collector.ResponseMessage;

public class AgentInfoSenderListener implements FutureListener<ResponseMessage> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicBoolean isSuccessful;

    public AgentInfoSenderListener(AtomicBoolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    @Override
    public void onComplete(Future<ResponseMessage> future) {
        try {
            if (future != null && future.isSuccess()) {
            }
        } catch(Exception e) {
            logger.warn("request fail. caused:{}", e.getMessage());
        }
    }
}
