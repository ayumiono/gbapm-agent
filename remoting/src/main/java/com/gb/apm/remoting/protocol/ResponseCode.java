package com.gb.apm.remoting.protocol;

import com.gb.apm.remoting.protocol.RemotingSysResponseCode;

public class ResponseCode extends RemotingSysResponseCode {
	/**
	 * 数据库版本过期,客户端》服务端
	 */
	public static final int DB_LOG_OVERDUE = 1009;
	
	/**
	 * 数据库同步成功,客户端》服务端
	 */
	public static final int DB_LOG_SUCCESS = 1000;
	
	public static final int DB_ALTER_TABLE_SUCCESS = 1001;
	
	/**
	 * 数据库同步失败,客户端》服务端
	 */
	public static final int DB_LOG_FAIL = 1002;
	
	public static final int DB_ALTER_TABLE_FAIL = 1003;
	
	public static final int AUTH_FAIL = 9999;
}
