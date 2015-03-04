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
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines one key, value pair used in {@link EPPRegistryCustomData}. The
 * &lt;registry:value&gt; element contains a required "key" attribute for the
 * value in the element. Use {@link #getKey()} and {@link #getKey()} to get and
 * set the key. Use {@link #getValue()} and {@link #setValue(String)} to get and
 * set the value.
 * 
 * @author ljia
 * @version 1.4
 * 
 */
public class EPPRegistryKeyValue implements EPPCodecComponent {
	private static final long serialVersionUID = -6074576944169908426L;

	/** XML Element Name of <code>EPPRegistryKeyValue</code> root element. */
	public static final String ELM_NAME = "registry:value";

	/** XML attribute name for the <code>key</code> attribute. */
	public static final String ATTR_KEY = "key";

	private String key = null;
	private String value = null;

	private String rootName = ELM_NAME;

	/**
	 * Default constructor. All attributes are set to default values. Must call
	 * {@link #setKey(String)} and {@link #setValue(String)} before calling
	 * {@link #encode(Document)}.
	 */
	public EPPRegistryKeyValue() {
		super();
	}

	/**
	 * Constructs a new instance with given key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public EPPRegistryKeyValue(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	// public EPPRegistryKeyValue(String rootName, String key, String value) {
	// this(key, value);
	// this.rootName = rootName;
	// }

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryKeyValue} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryKeyValue} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryKeyValue} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryKeyValue.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				getRootName());
		Text currVal = aDocument.createTextNode(value);
		root.setAttribute(ATTR_KEY, key);
		root.appendChild(currVal);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryKeyValue} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryKeyValue} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		key = aElement.getAttribute(ATTR_KEY);
		value = aElement.getFirstChild().getNodeValue();
	}

	/**
	 * Validate the state of the <code>EPPRegistryKeyValue</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (rootName == null || rootName.trim().length() == 0) {
			throw new EPPCodecException("rootName is not set");
		}
		if (key == null || key.trim().length() == 0) {
			throw new EPPCodecException("key is required");
		}
		if (value == null || value.trim().length() == 0) {
			throw new EPPCodecException("value is required");
		}
	}

	/**
	 * Clone <code>EPPRegistryKeyValue</code>.
	 * 
	 * @return clone of <code>EPPRegistryKeyValue</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryKeyValue) super.clone();
	}

	/**
	 * implements a deep <code>EPPRegistryKeyValue</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryKeyValue</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryKeyValue)) {
			return false;
		}

		EPPRegistryKeyValue theComp = (EPPRegistryKeyValue) aObject;

		if (!((key == null) ? (theComp.key == null) : key.equals(theComp.key))) {
			return false;
		}
		if (!((value == null) ? (theComp.value == null) : value
				.equals(theComp.value))) {
			return false;
		}
		if (!((rootName == null) ? (theComp.rootName == null) : rootName
				.equals(theComp.rootName))) {
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
	 * Get the key.
	 * 
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the key.
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get the value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Get the root name.
	 * 
	 * @return root name of the xml element. Always return {@code ELM_NAME}
	 */
	public String getRootName() {
		return rootName;
	}

	// public void setRootName(String rootName) {
	// this.rootName = rootName;
	// }
}
