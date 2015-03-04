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

import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryUpdateResp extends EPPResponse {
	private static final long serialVersionUID = -4667715413601551579L;

	/** XML Element Name of <code>EPPRegistryInfoResp</code> root element. */
	final static String ELM_NAME = "registry:updData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_REGISTRY_NAME = "registry:name";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "registry:crDate";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_UPDATE_DATE = "registry:upDate";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_ROID = "registry:roid";

	private String name = null;
	private Date createDate = null;
	private Date updateDate = null;

	public EPPRegistryUpdateResp() {
	}

	public EPPRegistryUpdateResp(EPPTransId aTransId, String name) {
		super(aTransId);
		this.name = name;
	}

	public EPPRegistryUpdateResp(EPPTransId aTransId, String name,
			Date createDate) {
		this(aTransId, name);
		this.createDate = createDate;
	}

	public EPPRegistryUpdateResp(EPPTransId aTransId, String name,
			Date createDate, Date updateDate) {
		this(aTransId, name, createDate);
		this.updateDate = updateDate;
	}

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPRegistryUpdateResp</code>.
	 * 
	 * @return EPPRegistryUpdateResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPRegistryUpdateResp</code>.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Compare an instance of <code>EPPRegistryUpdateResp</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryUpdateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPRegistryUpdateResp theComp = (EPPRegistryUpdateResp) aObject;

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

		// Update Date
		if (!((updateDate == null) ? (theComp.updateDate == null) : updateDate
				.equals(theComp.updateDate))) {
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryUpdateResp</code>.
	 * 
	 * @return clone of <code>EPPRegistryUpdateResp</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryUpdateResp clone = (EPPRegistryUpdateResp) super.clone();

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
	 * Validate the state of the <code>EPPRegistryUpdateResp</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *             DOCUMENT ME!
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
	 * <code>EPPRegistryUpdateResp</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         <code>EPPRegistryUpdateResp</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryUpdateResp</code>
	 *                instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryUpdateResp.encode: " + e);
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

		// Update Date
		if (updateDate != null) {
			EPPUtil.encodeTimeInstant(aDocument, root, updateDate,
					EPPRegistryMapFactory.NS, ELM_UPDATE_DATE);
		}

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryUpdateResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryUpdateResp</code>
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

		// Update Date
		updateDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_UPDATE_DATE);

		// Validate Attributes
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPDecodeException(
					"Invalid state on EPPRegistryUpdateResp.decode: " + e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
