package com.gb.apm.collector;

import com.gb.apm.model.APMModel;

public interface DataSender {
	void stop();
	boolean send(APMModel data);
}
