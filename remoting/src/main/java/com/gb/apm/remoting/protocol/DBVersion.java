package com.gb.apm.remoting.protocol;

import com.gb.apm.remoting.CommandCustomHeader;
import com.gb.apm.remoting.exception.RemotingCommandException;

public class DBVersion implements CommandCustomHeader {
	
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public void checkFields() throws RemotingCommandException {
		
	}

}
