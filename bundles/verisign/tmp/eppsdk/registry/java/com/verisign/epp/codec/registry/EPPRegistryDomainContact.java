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

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines the miniumum and maximum numbers of contacts by contact type. The
 * contact type is defined with the required "type" attribute with the possible
 * values of "admin", "tech", and "billing". The &lt;registry:contact&gt;
 * element contains the following child elements<br>
 * <br>
 * <ul>
 * <li>
 * &lt;registry:min&gt; - The minimum number of contacts for the contact type.
 * Use {@link #getMin()} and {@link #setMin(Integer)} to get and set this
 * element.</li>
 * <li>
 * &lt;registry:max&gt; - The OPTIONAL maximum number of contacts for the
 * contact type. If this element is not defined, the maximum number is
 * unbounded. Use {@link #getMax()} and {@link #setMax(Integer)} to get and set
 * this element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 */
public class EPPRegistryDomainContact extends EPPRegistryMinMax {
	private static final long serialVersionUID = 2085087247348705827L;

	/** XML Element Name of <code>EPPRegistryDomainContact</code> root element. */
	public static final String ELM_NAME = "registry:contact";

	/** XML attribute name for the <code>type</code> attribute. */
	public static final String ATTR_TYPE = "type";

	/** "admin" contact type */
	public static final String TYPE_ADMIN = "admin";

	/** "billing" contact type */
	public static final String TYPE_BILLING = "billing";

	/** "tech" contact type */
	public static final String TYPE_TECH = "tech";

	/** "type" attribute */
	private String type = null;

	public static Set VALID_TYPES = null;

	static {
		VALID_TYPES = new HashSet();
		VALID_TYPES.add(TYPE_ADMIN);
		VALID_TYPES.add(TYPE_BILLING);
		VALID_TYPES.add(TYPE_TECH);
	}

	/**
	 * Default constructor of EPPRegistryDomainContact. All attributes are set
	 * to null. Must call {@link #setType(String)} and {@link #setMin(Integer)}
	 * before calling {@link #encode(Document)}.
	 */
	public EPPRegistryDomainContact() {
		super();
		rootName = ELM_NAME;
	}

	/**
	 * Constructor an EPPRegistryDomainContact with type, min and max.
	 * 
	 * @param type
	 *            type of contact
	 * @param min
	 *            minimum number of contact entries.
	 * @param max
	 *            maximum number of contact entries.
	 */
	public EPPRegistryDomainContact(String type, Integer min, Integer max) {
		this();
		this.type = type;
		this.min = min;
		this.max = max;
	}

	/**
	 * Constructor an EPPRegistryDomainContact with type, min and max.
	 * 
	 * @param type
	 *            type of contact
	 * @param min
	 *            minimum number of contact entries.
	 * @param max
	 *            maximum number of contact entries.
	 */
	public EPPRegistryDomainContact(String type, int min, int max) {
		this(type, new Integer(min), new Integer(max));
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryDomainContact} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryDomainContact} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryDomainContact}
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		if (!VALID_TYPES.contains(type)) {
			throw new EPPEncodeException(
					"Invalide state on EPPRegistryDomainContact.encode: "
							+ "type is required. Valid values: admin/billing/tech");
		}

		Element root = super.encode(aDocument);

		root.setAttribute(ATTR_TYPE, type);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryDomainContact} attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryDomainContact}
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		super.decode(aElement);
		type = aElement.getAttribute(ATTR_TYPE);
	}

	/**
	 * Clone <code>EPPRegistryDomainContact</code>.
	 * 
	 * @return clone of <code>EPPRegistryDomainContact</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryDomainContact) super.clone();
	}

	/**
	 * implements a deep <code>EPPRegistryDomainContact</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryDomainContact</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!super.equals(aObject)) {
			return false;
		}

		if (!(aObject instanceof EPPRegistryDomainContact)) {
			return false;
		}

		EPPRegistryDomainContact theComp = (EPPRegistryDomainContact) aObject;

		if (!((type == null) ? (theComp.type == null) : type
				.equals(theComp.type))) {
			return false;
		}

		return true;
	}

	/**
	 * Return the root name of the XML element.
	 * 
	 * @return {@code EPPRegistryDomainContact.ELM_NAME}
	 */
	public String getRootName() {
		return this.rootName;
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
	 * Get type of contact.
	 * 
	 * @return type of contact
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set type of contact.
	 * 
	 * @param type
	 *            type of contact
	 */
	public void setType(String type) {
		this.type = type;
	}
}
