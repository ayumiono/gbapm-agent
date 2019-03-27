package com.gb.apm.plugins.dubbo;

import com.alibaba.dubbo.rpc.Invocation;

public class RpcUtil {
	/**
	 * 最好能够缓存起来
	 * @param dubboInterface
	 * @param parameterTypes
	 * @param methodName
	 * @return
	 */
	public static String parseInterface(Invocation invocation) {
		Class<?> dubboInterface = invocation.getInvoker().getInterface();
		String methodName = invocation.getMethodName();
		StringBuilder sbuilder = new StringBuilder(dubboInterface.getName()+":"+methodName);
		return sbuilder.toString();
	}
}	
