package com.gb.apm.dapper.context;

/**
 * @author emeroad
 * @author jaehong.kim
 */
public interface MethodDescriptor {

	String getMethodName();

	String getClassName();

	String[] getParameterTypes();

	String[] getParameterVariableName();

	String getParameterDescriptor();

	int getLineNumber();

	String getFullName();

	void setApiId(int apiId);

	int getApiId();

	String getApiDescriptor();

	int getType();
}
