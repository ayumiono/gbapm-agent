package com.gb.apm.collector;

public interface DataSenderFactory {
	public DataSender create(String typeName);
}
