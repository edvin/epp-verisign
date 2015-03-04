/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.codec.registry;

import com.verisign.epp.codec.gen.EPPCodecException;

/**
 * Represents the period of time a domain object is in the pending transfer
 * before the transfer is auto approved by the server. The
 * &lt;registry:transferHoldPeriod&gt; element MUST have the "unit" attribute
 * with the possible values of "y" for year, "m" for month, and "d" for day..
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 */
public class EPPRegistryTransferHoldPeriodType extends EPPRegistryPeriodType {
	private static final long serialVersionUID = 7141150544743039060L;

	/**
	 * XML Element Name of <code>EPPRegistryTransferHoldPeriodType</code> root
	 * element.
	 */
	public static final String ELM_NAME = "registry:transferHoldPeriod";

	/**
	 * Default constructor. Must call {@link #setNumber(Integer)} and
	 * {@link #setUnit(String)} before calling the
	 * {@link #encode(org.w3c.dom.Document)} method.
	 */
	public EPPRegistryTransferHoldPeriodType() {
		super();
		this.rootName = ELM_NAME;
	}

	/**
	 * Construct an instance of {@code EPPRegistryTransferHoldPeriodType} with
	 * {@code number} and {@code unit}.
	 * 
	 * @param number
	 *            number must be > 0
	 * @param unit
	 *            unit must be one of "y", "m" or "d"
	 */
	public EPPRegistryTransferHoldPeriodType(Integer number, String unit) {
		this();
		this.number = number;
		this.unit = unit;
	}

	/**
	 * Construct an instance of {@code EPPRegistryTransferHoldPeriodType} with
	 * {@code number} and {@code unit}.
	 * 
	 * @param number
	 *            number must be > 0
	 * @param unit
	 *            unit must be one of "y", "m" or "d"
	 */
	public EPPRegistryTransferHoldPeriodType(int number, String unit) {
		this();
		this.number = new Integer(number);
		this.unit = unit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.verisign.epp.codec.registry.EPPRegistryPeriodType#equals(java.lang
	 * .Object)
	 */
	public boolean equals(Object aObject) {
		return super.equals(aObject);
	}

	/**
	 * Extra validation rules on top of {@link EPPRegistryPeriodType}:
	 * {@code number} must be greater than "0"; {@code unit} must be one of "y",
	 * "m" or "d".
	 */
	void extraValidate() throws EPPCodecException {
		int n = number.intValue();
		if (n <= 0) {
			throw new EPPCodecException(getRootName()
					+ ": number should be greater than 0");
		}
		if (!"y".equals(unit) && !"m".equals(unit) && !"d".equals(unit)) {
			throw new EPPCodecException(getRootName()
					+ ": invalid unit. Valid values: y/m/d");
		}
	}
}
