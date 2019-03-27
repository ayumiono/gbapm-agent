package com.gb.apm.dapper.context;

import java.util.List;

/**
 * @author hyungil.jeong
 */
public interface ServerMetaDataHolder {

	void setServerName(String serverName);

	void addConnector(String protocol, int port);

	void addServiceInfo(String serviceName, List<String> serviceLibs);

	void addListener(ServerMetaDataListener listener);

	void removeListener(ServerMetaDataListener listener);

	void notifyListeners();

	interface ServerMetaDataListener {

		void publishServerMetaData(ServerMetaData serverMetaData);

	}
}
