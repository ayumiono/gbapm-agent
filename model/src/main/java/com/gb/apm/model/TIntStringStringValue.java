package com.gb.apm.model;

public class TIntStringStringValue extends APMModel {
	private int intValue; // required
	private String stringValue1; // optional
	private String stringValue2; // optional
	
	public TIntStringStringValue() {}

	public TIntStringStringValue(int intValue) {
		this.intValue = intValue;
	} 
	
	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getStringValue1() {
		return stringValue1;
	}

	public void setStringValue1(String stringValue1) {
		this.stringValue1 = stringValue1;
	}

	public String getStringValue2() {
		return stringValue2;
	}

	public void setStringValue2(String stringValue2) {
		this.stringValue2 = stringValue2;
	}
}
