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
import com.verisign.epp.codec.gen.EPPEncodeException;

/**
 * Represents the minimum and maximum length of &lt;contact:name&gt; element
 * defined RFC 5733 using the &lt;registry:minLength&gt; and
 * &lt;registry:maxLength&gt; child elements, respectively.
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryPostal
 */
public class EPPRegistryContactName extends EPPRegistryMinMaxLength {
	private static final long serialVersionUID = -6709593710281976641L;

	/** XML Element Name of <code>EPPRegistryContactName</code> root element. */
	public static final String ELM_NAME = "registry:name";

	/**
	 * Creates a new EPPRegistryContactName object.
	 */
	public EPPRegistryContactName() {
		super();
		this.rootName = ELM_NAME;
	}

	/**
	 * Creates a new EPPRegistryContactName object with min length and max
	 * length.
	 * 
	 * @param min
	 *            minimum length of the contact name
	 * @param max
	 *            maximum length of the contact name
	 */
	public EPPRegistryContactName(Integer min, Integer max) {
		this();
		this.setMin(min);
		this.setMax(max);
	}

	/**
	 * Creates a new EPPRegistryContactName object with min length and max
	 * length.
	 * 
	 * @param min
	 *            minimum length of the contact name
	 * @param max
	 *            maximum length of the contact name
	 */
	public EPPRegistryContactName(int min, int max) {
		this(new Integer(min), new Integer(max));
	}

	/**
	 * implements a deep <code>EPPRegistryContactName</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryContactName</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		return super.equals(aObject);
	}

	/**
	 * Validate the state of the <code>EPPRegistryContactName</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	protected void validateState() throws EPPEncodeException {
		if (min == null || min.intValue() <= 0) {
			throw new EPPEncodeException("Invalid state on "
					+ getClass().getName()
					+ ".encode: min is required and should be greater than 0");
		}
		if (max == null || max.intValue() < min.intValue()) {
			throw new EPPEncodeException(
					"Invalid state on "
							+ getClass().getName()
							+ ".encode: max is required and should be greater than or equal to min");
		}
		if (max.intValue() > 255) {
			throw new EPPEncodeException("Invalid state on "
					+ getClass().getName()
					+ ".encode: max should be less than or equal to 255");
		}
	}
}
