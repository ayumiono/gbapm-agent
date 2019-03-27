package com.gb.apm.model;

public enum TJvmGcType {
	UNKNOWN(0), SERIAL(1), PARALLEL(2), CMS(3), G1(4);

	private final int value;

	private TJvmGcType(int value) {
		this.value = value;
	}

	/**
	 * Get the integer value of this enum value, as defined in the Thrift IDL.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Find a the enum type by its integer value, as defined in the Thrift IDL.
	 * 
	 * @return null if the value is not found.
	 */
	public static TJvmGcType findByValue(int value) {
		switch (value) {
		case 0:
			return UNKNOWN;
		case 1:
			return SERIAL;
		case 2:
			return PARALLEL;
		case 3:
			return CMS;
		case 4:
			return G1;
		default:
			return null;
		}
	}
}
