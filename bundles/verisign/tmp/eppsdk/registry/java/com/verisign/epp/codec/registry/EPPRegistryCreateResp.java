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

import java.util.Date;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents an EPP Registry &lt;registry:creData&gt; response to a
 * <code>EPPRegistryCreateCmd</code>. When a &lt;crate&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUSt contain a child
 * &lt;registry:creData&gt; element that identifies the registry namespace and
 * the location of the registry schema. The &lt;registry:creData&gt; element
 * contains the following child elements:<br>
 * <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:name&gt; element that contains the fully qualified name of
 * zone object that has been created. Use <code>getName</code> and
 * <code>setName</code> to get and set the element.</li>
 * <li>
 * A &lt;registry.crDate&gt; element that contains the date and time of zone
 * object creation. Use <code>getCreateDate</code> and
 * <code>setCreateDate</code> to get and set the element.</li>
 * </ul>
 * 
 * <br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryCreateCmd
 */
public class EPPRegistryCreateResp extends EPPResponse {
	private static final long serialVersionUID = 4209738019189546774L;

	/** XML Element Name of <code>EPPRegistryCreateResp</code> root element. */
	final static String ELM_NAME = "registry:creData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_REGISTRY_NAME = "registry:name";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "registry:crDate";

	/** Name {@code String} of the zone object created. */
	private String name = null;

	/** {@code Date} of zone object creation. */
	private Date createDate = null;

	/**
	 * <code>EPPRegistryCreateResp</code> default constructor. Must call
	 * required setter methods before encode. the defaults include the
	 * following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * {@code name} is set to <code>null</code></li>
	 * <li>
	 * {@code createDate} date is set to <code>null</code></li>
	 * </ul>
	 * 
	 * <br>
	 * {@code name} must be set before invoking <code>encode</code>.
	 */
	public EPPRegistryCreateResp() {
	}

	/**
	 * <code>EPPRegistryCreateResp</code> constructor that takes {@code name}
	 * values as parameters. <br>
	 * The creation date must be set before invoking <code>encode</code>.
	 * 
	 * @param aTransId
	 *            transaction Id associated with response
	 * @param name
	 *            name of zone object
	 */
	public EPPRegistryCreateResp(EPPTransId aTransId, String name) {
		super(aTransId);
		this.name = name;
	}

	/**
	 * <code>EPPRegistryCreateResp</code> constructor that takes required values
	 * as parameters. <br>
	 * 
	 * @param aTransId
	 *            transaction Id associated with response
	 * @param name
	 *            name of zone object
	 * @param createDate
	 *            creation date of the zone object
	 * 
	 */
	public EPPRegistryCreateResp(EPPTransId aTransId, String name,
			Date createDate) {
		this(aTransId, name);
		this.createDate = createDate;
	}

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPRegistryCreateResp</code>.
	 * 
	 * @return {@code EPPRegistryCreateResp.ELM_NAME}
	 */
	public String getType() {
		return ELM_NAME;
	}

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPRegistryCreateResp</code>.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Compare an instance of <code>EPPRegistryCreateResp</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPRegistryCreateResp theComp = (EPPRegistryCreateResp) aObject;

		// Name
		if (!((name == null) ? (theComp.name == null) : name
				.equals(theComp.name))) {
			return false;
		}

		// Create Date
		if (!((createDate == null) ? (theComp.createDate == null) : createDate
				.equals(theComp.createDate))) {
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryCreateResp</code>.
	 * 
	 * @return clone of <code>EPPRegistryCreateResp</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryCreateResp clone = (EPPRegistryCreateResp) super.clone();

		return clone;
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
	 * Validate the state of the <code>EPPRegistryCreateResp</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (createDate == null) {
			throw new EPPCodecException(
					"required attribute createDate is not set");
		}
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryCreateResp</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         <code>EPPRegistryCreateResp</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryCreateResp</code>
	 *                instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryCreateResp.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(aDocument, root, name, EPPRegistryMapFactory.NS,
				ELM_REGISTRY_NAME);

		// Creation Date
		EPPUtil.encodeTimeInstant(aDocument, root, createDate,
				EPPRegistryMapFactory.NS, ELM_CREATION_DATE);

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryCreateResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryCreateResp</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_REGISTRY_NAME);

		// Creation Date
		createDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_CREATION_DATE);

		// Validate Attributes
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPDecodeException(
					"Invalid state on EPPRegistryCreateResp.decode: " + e);
		}
	}

	/**
	 * Get the name of the zone object created.
	 * 
	 * @return Name {@code String} of zone object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the zone object created.
	 * 
	 * @param name Name {@code String} of zone object
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the zone object creation date.
	 * 
	 * @return zone object creation date
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Set the zone object creation date.
	 * 
	 * @param createDate zone object creation date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
