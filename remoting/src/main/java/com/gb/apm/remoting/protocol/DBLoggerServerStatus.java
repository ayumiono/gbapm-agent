package com.gb.apm.remoting.protocol;

import java.util.Map;

public class DBLoggerServerStatus extends RemotingSerializable {
	private int sessions;
	private Map<String, Long> versions;
	public int getSessions() {
		return sessions;
	}
	public void setSessions(int sessions) {
		this.sessions = sessions;
	}
	public Map<String, Long> getVersions() {
		return versions;
	}
	public void setVersions(Map<String, Long> versions) {
		this.versions = versions;
	}
}
