package com.gb.apm.model;

public class TIntStringValue extends APMModel {
	private int intValue; // required
	private String stringValue; // optional
	
	public TIntStringValue() {}

	public TIntStringValue(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}
