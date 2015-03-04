package com.verisign.epp.codec.registry;

import com.verisign.epp.codec.gen.EPPCodecException;

public class EPPRegistryDefaultPeriodType extends EPPRegistryPeriodType {
	private static final long serialVersionUID = 6372101315523033369L;

	public static final String ELM_NAME = "registry:default";

	public EPPRegistryDefaultPeriodType() {
		super();
		this.rootName = ELM_NAME;
	}

	public EPPRegistryDefaultPeriodType(Integer n, String unit) {
		this();
		this.number = n;
		this.unit = unit;
	}

	public EPPRegistryDefaultPeriodType(int number, String unit) {
		this();
		this.number = new Integer(number);
		this.unit = unit;
	}

	public boolean equals(Object aObject) {
		return super.equals(aObject);
	}

	void extraValidate() throws EPPCodecException {
		int n = number.intValue();
		if (n < 1 || n > 99) {
			throw new EPPCodecException(getRootName()
					+ ": number should be between 1 - 99.");
		}
		if (!"y".equals(unit) && !"m".equals(unit)) {
			throw new EPPCodecException(getRootName()
					+ ": invalid unit. Valid values: y/m");
		}
	}
}
