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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents custom data using key/value pairs. The
 * &lt;registry:customeData&gt; element contains the following child elements:
 * 
 * <ul>
 * <li>&lt;registry:value&gt; - one or more elements with a required "key"
 * attribute, which defines the key for the value. Use {@link #getKeyValues()}
 * and {@link #setKeyValues(List)} to get and set the elements. Use
 * {@link #addKeyValue(EPPRegistryKeyValue)} to add one key/value pair to
 * existing {@code List}.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryHost
 * @see com.verisign.epp.codec.registry.EPPRegistryContact
 * @see com.verisign.epp.codec.registry.EPPRegistryKeyValue
 */
public class EPPRegistryCustomData implements EPPCodecComponent {
	private static final long serialVersionUID = 1009798850579739560L;

	/** XML Element Name of <code>EPPRegistryCustomData</code> root element. */
	public static final String ELM_NAME = "registry:customData";

	/** {@code List} of {@code EPPRegistryKeyValue} */
	private List keyValues = new ArrayList();

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryCustomData} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryCustomData} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryCustomData} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		if ((keyValues == null) || (keyValues.size() == 0)) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryCustomData.encode: keyValues is required");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		for (Iterator it = keyValues.iterator(); it.hasNext();) {
			EPPRegistryKeyValue elem = (EPPRegistryKeyValue) it.next();
			root.appendChild(elem.encode(aDocument));
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryCustomData} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryCustomData} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		keyValues = EPPUtil.decodeCompList(aElement, EPPRegistryMapFactory.NS,
				EPPRegistryKeyValue.ELM_NAME, EPPRegistryKeyValue.class);
	}

	/**
	 * Clone <code>EPPRegistryCustomData</code>.
	 * 
	 * @return clone of <code>EPPRegistryCustomData</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryCustomData clone = (EPPRegistryCustomData) super.clone();

		if (keyValues != null) {
			clone.keyValues = (List) ((ArrayList) keyValues).clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryCustomData</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryCustomData</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryCustomData)) {
			return false;
		}

		EPPRegistryCustomData theComp = (EPPRegistryCustomData) aObject;

		if (!((keyValues == null) ? (theComp.keyValues == null) : EPPUtil
				.equalLists(keyValues, theComp.keyValues))) {
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
	 * Get the custom key/value pairs.
	 * 
	 * @return {@code List} of key/value pair, instance of
	 *         {@code EPPRegistryKeyValue}
	 */
	public List getKeyValues() {
		return keyValues;
	}

	/**
	 * Set the custom key/value pairs.
	 * 
	 * @param keyValues
	 *            {@code List} of key/value pair, instance of
	 *            {@code EPPRegistryKeyValue}
	 */
	public void setKeyValues(List keyValues) {
		this.keyValues = keyValues;
	}

	/**
	 * Add one key/value pair to existing key/value pairs.
	 * 
	 * @param keyValue
	 *            key/value pair
	 */
	public void addKeyValue(EPPRegistryKeyValue keyValue) {
		if (keyValue == null) {
			return;
		}

		if (keyValues == null) {
			keyValues = new ArrayList();
		}

		keyValues.add(keyValue);
	}
}
