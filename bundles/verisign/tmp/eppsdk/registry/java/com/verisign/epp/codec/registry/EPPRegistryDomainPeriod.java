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

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines the supported registration periods and default periods by command
 * type. The required "command" attribute defines the command type with sample
 * values of "create", "renew", and "transfer". The &lt;registry:period&gt;
 * element contains ONE of the following elements: <br>
 * <br>
 * <ul>
 * <li>
 * &lt;registry:length&gt; - The default, minimum, and maximum period length for
 * the command type. Use {@link #getLength()} and
 * {@link #setLength(EPPRegistryMinMaxPeriod)} to get and set this element.</li>
 * <li>
 * &lt;registry:serverDecided&gt; - The registration period is decided by the
 * server based on the relationship to a related object that MUST have the same
 * expiration date. Use {@link #getServerDecided()} and
 * {@link #setServerDecided(Boolean)} to get and set this element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryMinMaxPeriod
 */
public class EPPRegistryDomainPeriod implements EPPCodecComponent {
	private static final long serialVersionUID = -7241851592546211256L;

	/** XML Element Name of <code>EPPRegistryDomainPeriod</code> root element. */
	public static final String ELM_NAME = "registry:period";

	/** XML Element Name of <code>serverDecided</code> attribute. */
	public static final String ELM_SERVER_DECIDED = "registry:serverDecided";

	/** XML attribute name for the <code>command</code> attribute. */
	public static final String ATTR_COMMAND = "command";

	/** Maximum period in years as defined in RFC5731 */
	public static final int MAX_PERIOD = 99;

	/** Maximum period in years as defined in RFC5731 */
	public static final int MIN_PERIOD = 1;

	/**
	 * Instance of {@link EPPRegistryMinMaxPeriod} that defines min/max/default
	 * period for a given command. If this attribute is not {@code null}, then
	 * "serverDecided" attribute must be {@code false}, and vice versa.
	 */
	private EPPRegistryMinMaxPeriod length = null;

	/**
	 * Whether no not to have server decided expiration date. If this is set to
	 * {@code true}, then "length" attribute must be set to null, and vice
	 * verse.
	 */
	private Boolean serverDecided = Boolean.FALSE;

	/** Command type. Valid values are "create", "renew" or "transfer". */
	private String command = null;

	/**
	 * Default constructor. Attributes are set default values:
	 * <ul>
	 * <li>{@code command} - {@code null}</li>
	 * <li>{@code length} - {@code null}</li>
	 * <li>{@code serverDecided} - {@code Boolean.FALSE}</li>
	 * </ul>
	 */
	public EPPRegistryDomainPeriod() {
	}

	/**
	 * Construct an instance of {@code EPPRegistryDomainPeriod} with the
	 * following inputs. {@code serverDecided} is set to {@code Boolean.FALSE}:
	 * 
	 * @param command
	 *            command type
	 * @param min
	 *            minimum length number
	 * @param minUnit
	 *            minimum length unit
	 * @param max
	 *            maximum length number
	 * @param maxUnit
	 *            maximum length unit
	 * @param defaultLength
	 *            default length number
	 * @param defaultLengthUnit
	 *            length unit
	 */
	public EPPRegistryDomainPeriod(String command, Integer min, String minUnit,
			Integer max, String maxUnit, Integer defaultLength,
			String defaultLengthUnit) {
		this.command = command;
		this.length = new EPPRegistryMinMaxPeriod(min, minUnit, max, maxUnit,
				defaultLength, defaultLengthUnit);
	}

	/**
	 * Construct an instance of {@code EPPRegistryDomainPeriod} with the
	 * following inputs. {@code serverDecided} is set to {@code Boolean.FALSE}:
	 * 
	 * @param command
	 *            command type
	 * @param min
	 *            minimum length number
	 * @param minUnit
	 *            minimum length unit
	 * @param max
	 *            maximum length number
	 * @param maxUnit
	 *            maximum length unit
	 * @param defaultLength
	 *            default length number
	 * @param defaultLengthUnit
	 *            length unit
	 */
	public EPPRegistryDomainPeriod(String command, int min, String minUnit,
			int max, String maxUnit, int defaultLength, String defaultLengthUnit) {
		this.command = command;
		this.length = new EPPRegistryMinMaxPeriod(min, minUnit, max, maxUnit,
				defaultLength, defaultLengthUnit);
	}

	/**
	 * Construct an instance of {@code EPPRegistryDomainPeriod} with the
	 * following inputs. {@code length} is set to {@code null}:
	 * 
	 * @param command
	 *            command type
	 * @param serverDecided
	 *            whether no not to have server decided expiration date
	 */
	public EPPRegistryDomainPeriod(String command, Boolean serverDecided) {
		this.command = command;
		this.serverDecided = serverDecided;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryDomainPeriod} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryDomainPeriod} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryDomainPeriod}
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryDomainPeriod.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		root.setAttribute(ATTR_COMMAND, command);
		if (length != null) {
			EPPUtil.encodeComp(aDocument, root, length);
		}
		if (serverDecided.booleanValue()) {
			Element child = aDocument.createElementNS(EPPRegistryMapFactory.NS,
					ELM_SERVER_DECIDED);
			root.appendChild(child);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryDomainPeriod} attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryDomainPeriod}
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		command = aElement.getAttribute(ATTR_COMMAND);
		Element child = EPPUtil.getElementByTagNameNS(aElement,
				EPPRegistryMapFactory.NS, ELM_SERVER_DECIDED);
		serverDecided = (child == null) ? Boolean.FALSE : Boolean.TRUE;
		if (Boolean.FALSE == serverDecided) {
			this.setLength((EPPRegistryMinMaxPeriod) EPPUtil.decodeComp(
					aElement, EPPRegistryMapFactory.NS,
					EPPRegistryMinMaxPeriod.ELM_NAME,
					EPPRegistryMinMaxPeriod.class));
		}
	}

	/**
	 * Validate the state of the <code>EPPRegistryDomainPeriod</code> instance.
	 * A valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if ((length == null && !serverDecided.booleanValue())
				|| (length != null && serverDecided.booleanValue())) {
			throw new EPPCodecException(
					"Either specify length or make it server decided");
		}
		if (command == null || command.trim().length() == 0) {
			throw new EPPEncodeException("command is required");
		}
		if (length != null) {
			validPeriod(length.getMin(), "min");
			validPeriod(length.getMax(), "max");
			validPeriod(length.getDefaultLength(), "default");
			if (comparePeriod(length.getMin(), length.getDefaultLength()) > 0) {
				throw new EPPCodecException(
						"min period cannot be greater than default period");
			}
			if (comparePeriod(length.getDefaultLength(), length.getMax()) > 0) {
				throw new EPPCodecException(
						"default period cannot be greater than max period");
			}
		}
	}

	private void validPeriod(EPPRegistryPeriodType period, String type)
			throws EPPCodecException {
		if (period == null) {
			throw new EPPCodecException(type + " period is required");
		}
		period.validateState();
	}

	private int comparePeriod(EPPRegistryPeriodType left,
			EPPRegistryPeriodType right) {
		int l = 0;
		int r = 0;
		if ("y".equals(left.getUnit())) {
			l = left.getNumber().intValue() * 12;
		} else if ("m".equals(left.getUnit())) {
			l = left.getNumber().intValue();
		}
		if ("y".equals(right.getUnit())) {
			r = right.getNumber().intValue() * 12;
		} else if ("m".equals(right.getUnit())) {
			r = right.getNumber().intValue();
		}

		return l - r;
	}

	/**
	 * Clone <code>EPPRegistryDomainPeriod</code>.
	 * 
	 * @return clone of <code>EPPRegistryDomainPeriod</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryDomainPeriod clone = (EPPRegistryDomainPeriod) super.clone();

		if (length != null) {
			clone.length = (EPPRegistryMinMaxPeriod) length.clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryDomainPeriod</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryDomainPeriod</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryDomainPeriod)) {
			return false;
		}

		EPPRegistryDomainPeriod theComp = (EPPRegistryDomainPeriod) aObject;

		if (!((length == null) ? (theComp.length == null) : length
				.equals(theComp.length))) {
			return false;
		}
		if (!((serverDecided == null) ? (theComp.serverDecided == null)
				: serverDecided.equals(theComp.serverDecided))) {
			return false;
		}
		if (!((command == null) ? (theComp.command == null) : command
				.equals(theComp.command))) {
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Get the length for an EPP domain transform command.
	 * 
	 * @return instance of {@link EPPRegistryMinMaxPeriod} that defines
	 *         min/max/default period for a given command
	 */
	public EPPRegistryMinMaxPeriod getLength() {
		return length;
	}

	/**
	 * Set the length for an EPP domain transform command.
	 * 
	 * @param length
	 *            instance of {@link EPPRegistryMinMaxPeriod} that defines
	 *            min/max/default period for a given command
	 */
	public void setLength(EPPRegistryMinMaxPeriod length) {
		this.length = length;
	}

	/**
	 * Get whether to have server decided expiration date.
	 * 
	 * @return {@code true} - the registration period is decided by the server
	 *         based on the relationship to a related object that MUST have the
	 *         same expiration date. {@code false} - the registration period is
	 *         specified in the {@code length} attribute.
	 */
	public Boolean getServerDecided() {
		return serverDecided;
	}

	/**
	 * Set whether to have server decided expiration date.
	 * 
	 * @param serverDecided
	 *            {@code true} - the registration period is decided by the
	 *            server based on the relationship to a related object that MUST
	 *            have the same expiration date. {@code false} - the
	 *            registration period is specified in the {@code length}
	 *            attribute.
	 */
	public void setServerDecided(Boolean serverDecided) {
		this.serverDecided = serverDecided;
	}

	/**
	 * Get the command type.
	 * 
	 * @return command type in {@code String}. Valid values are "create",
	 *         "renew" and "transfer".
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Set the command type.
	 * 
	 * @param command
	 *            command type in {@code String}. Valid values are "create",
	 *            "renew" and "transfer".
	 */
	public void setCommand(String command) {
		this.command = command;
	}
}
