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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;

/**
 * Defines the grace periods by operation type. The required "command" attribute
 * defines the operation type with the sample values of "create", "renew",
 * "transfer", and "autoRenew". The &lt;registry:gracePeriod&gt; element
 * requires the "unit" attribute with the possible values of "d" for day, "h"
 * for hour, and "m" for minute.
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 */
public class EPPRegistryGracePeriod extends EPPRegistryPeriodType {
	private static final long serialVersionUID = 7887886408519098870L;

	/** XML Element Name of <code>EPPRegistryGracePeriod</code> root element. */
	public final static String ELM_NAME = "registry:gracePeriod";

	/** XML attribute name for the <code>command</code> attribute. */
	public static final String ATTR_COMMAND = "command";

	/** EPP transform command to which this period applies */
	protected String command = null;

	/**
	 * Default constructor. Must call {@link #setNumber(Integer)},
	 * {@link #setUnit(String)} and {@link #setCommand(String)} before calling
	 * {@link #encode(Document)} method.
	 */
	public EPPRegistryGracePeriod() {
		super();
		this.rootName = ELM_NAME;
	}

	/**
	 * Constructs an instance of {@code EPPRegistryGracePeriod} with
	 * {@code command}, {@code number} and {@code unit}.
	 * 
	 * @param command
	 *            EPP command to which this period applies
	 * @param number
	 *            number must be > 0
	 * @param unit
	 *            unit must be one of "d", "h" or "m"
	 */
	public EPPRegistryGracePeriod(String command, Integer number, String unit) {
		this();
		this.command = command;
		this.number = number;
		this.unit = unit;
	}

	/**
	 * Constructs an instance of {@code EPPRegistryGracePeriod} with
	 * {@code command}, {@code number} and {@code unit}.
	 * 
	 * @param command
	 *            EPP command to which this period applies
	 * @param number
	 *            number must be > 0
	 * @param unit
	 *            unit must be one of "d", "h" or "m"
	 */
	public EPPRegistryGracePeriod(String command, int number, String unit) {
		this(command, new Integer(number), unit);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryGracePeriod} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryGracePeriod} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryGracePeriod}
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = super.encode(aDocument);
		root.setAttribute(ATTR_COMMAND, command);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryGracePeriod} attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryGracePeriod}
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		this.command = aElement.getAttribute(ATTR_COMMAND);
		super.decode(aElement);
	}

	/*
	 * (non-Javadoc)
	 * @see com.verisign.epp.codec.registry.EPPRegistryPeriodType#validateState()
	 */
	void validateState() throws EPPCodecException {
		super.validateState();
		if (command == null || command.trim().length() == 0) {
			throw new EPPCodecException(getRootName() + ": command is empty");
		}
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
	 * Get the EPP transform command to which this period applies.
	 * 
	 * @return EPP transform command to which this period applies
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Set the EPP transform command to which this period applies.
	 * 
	 * @param command
	 *            EPP transform command to which this period applies
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Extra validation rules on top of {@link EPPRegistryPeriodType}:
	 * {@code number} must be greater than "0"; {@code unit} must be one of "h",
	 * "m" or "d".
	 */
	void extraValidate() throws EPPCodecException {
		int n = number.intValue();
		if (n <= 0) {
			throw new EPPCodecException(getRootName()
					+ ": number should be greater than 0");
		}
		if (!"h".equals(unit) && !"m".equals(unit) && !"d".equals(unit)) {
			throw new EPPCodecException(getRootName()
					+ ": invalid unit. Valid values: d/h/m");
		}
	}
}
