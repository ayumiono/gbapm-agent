package com.gb.apm.collector;

import com.gb.apm.model.APMModel;
import com.gb.apm.remoting.protocol.RemotingCommand;

/**
 * @author emeroad
 */
public interface EnhancedDataSender extends DataSender {

	boolean request(APMModel data);

	RemotingCommand request(APMModel data, int retry);

	// void request(APMModel data, InvokeCallback callback);
	void request(APMModel data, FutureListener<ResponseMessage> listener);

	void requestAysn(APMModel data);

}
