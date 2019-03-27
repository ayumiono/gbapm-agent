package com.gb.apm.dapper.context;

import java.util.List;

/**
 * @author hyungil.jeong
 */
public interface ServiceInfo {
	
	String getServiceName();

	List<String> getServiceLibs();
}
