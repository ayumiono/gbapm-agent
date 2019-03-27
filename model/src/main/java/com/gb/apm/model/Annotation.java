package com.gb.apm.model;

/**
 * @author netspider
 * @author emeroad
 */
public class Annotation {
	
	private int key; // required
	
	private String value; // optional
	
	public Annotation() {}

    public Annotation(int key) {
        this.key = key;
    }

    public Annotation(int key, String value) {
    	this.key = key;
        this.value = value;
    }

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
