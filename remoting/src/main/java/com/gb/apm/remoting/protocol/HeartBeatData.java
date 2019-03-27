package com.gb.apm.remoting.protocol;

import com.gb.apm.remoting.protocol.RemotingSerializable;

public class HeartBeatData extends RemotingSerializable {
	private String clientId;
	private String clientVersion;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
}
