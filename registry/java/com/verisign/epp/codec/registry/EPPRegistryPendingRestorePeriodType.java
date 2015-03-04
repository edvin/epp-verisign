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
 * Defines the length of time that the domain object will remain in the
 * pendingRestore status unless the restore report command is received..
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryRGP
 */
public class EPPRegistryPendingRestorePeriodType extends EPPRegistryPeriodType {
	private static final long serialVersionUID = -6666426449555551984L;

	/**
	 * XML Element Name of <code>EPPRegistryPendingRestorePeriodType</code> root
	 * element.
	 */
	public static final String ELM_NAME = "registry:pendingRestore";

	/**
	 * Default constructor. Must call {@link #setNumber(Integer)} and
	 * {@link #setUnit(String)} before calling
	 * {@link #encode(org.w3c.dom.Document)} method.
	 */
	public EPPRegistryPendingRestorePeriodType() {
		super();
		this.rootName = ELM_NAME;
	}

	/**
	 * Constructs an instance of {@code EPPRegistryPendingRestorePeriodType}
	 * with {@code number} and {@code unit}.
	 * 
	 * @param number
	 *            number must be > 0
	 * @param unit
	 *            unit must be one of "y", "m", "d", or "h"
	 */
	public EPPRegistryPendingRestorePeriodType(Integer number, String unit) {
		this();
		this.number = number;
		this.unit = unit;
	}

	/**
	 * Constructs an instance of {@code EPPRegistryPendingRestorePeriodType}
	 * with {@code number} and {@code unit}.
	 * 
	 * @param number
	 *            number must be > 0
	 * @param unit
	 *            unit must be one of "y", "m", "d", or "h"
	 */
	public EPPRegistryPendingRestorePeriodType(int number, String unit) {
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
	 * Extra validation rules on top of
	 * {@link EPPRegistryPendingRestorePeriodType}: {@code number} must be
	 * greater than "0"; {@code unit} must be one of "y", "m", "d" or "h".
	 */
	void extraValidate() throws EPPCodecException {
		int n = number.intValue();
		if (n <= 0) {
			throw new EPPCodecException(getRootName()
					+ ": number should be greater than 0");
		}
		if (!VALID_UNITS.contains(unit)) {
			throw new EPPCodecException(getRootName()
					+ ": invalid unit. Valid values: y/m/d/h");
		}
	}
}
