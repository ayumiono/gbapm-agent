package com.gb.apm.common.hbase;

public class Assert {
	public static void notNull(Object o, String message) {
		if(o == null) throw new RuntimeException(message);
	}
}
