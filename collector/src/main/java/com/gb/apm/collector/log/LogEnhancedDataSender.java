package com.gb.apm.collector.log;

import com.gb.apm.collector.EnhancedDataSender;
import com.gb.apm.collector.FutureListener;
import com.gb.apm.collector.ResponseMessage;
import com.gb.apm.model.APMModel;
import com.gb.apm.remoting.protocol.RemotingCommand;

public class LogEnhancedDataSender implements EnhancedDataSender {

	@Override
	public boolean send(APMModel data) {
		return false;
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean request(APMModel data) {
		return false;
	}

	@Override
	public RemotingCommand request(APMModel data, int retry) {
		return null;
	}

//	@Override
//	public void request(APMModel data, InvokeCallback callback) {
//	}

	@Override
	public void requestAysn(APMModel data) {
	}

	@Override
	public void request(APMModel data, FutureListener<ResponseMessage> listener) {
		// TODO Auto-generated method stub
		
	}

}
