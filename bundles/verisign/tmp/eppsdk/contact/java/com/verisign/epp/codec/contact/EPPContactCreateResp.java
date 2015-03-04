/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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
package com.verisign.epp.codec.contact;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a &lt;contact:creData&gt; response to an
 * <code>EPPContactCreateCmd</code>. When a &lt;create&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;contact:creData&gt; element that identifies the contact namespace
 * and the location of the contact schema. The &lt;contact:creData&gt; element
 * contains the following child elements:     <br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier for
 * the contact to be deleted.  Use <code>getId</code> and <code>setId</code>
 * to get and set the element.
 * </li>
 * <li>
 * A &lt;domain.crDate&gt; element that contains the date and time of domain
 * object creation.  Use <code>getCreationDate</code> and
 * <code>setCreationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPContactCreateResp extends EPPResponse {
	/** XML Element Name of <code>EPPContactCreateResp</code> root element. */
	final static String ELM_NAME = "contact:creData";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "contact:crDate";

	/**
	 * XML Element Name of a contact id in a <code>EPPContactCreateResp</code>.
	 */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** Contact Name of contact to delete. */
	private String id;

	/** Creation Date. */
	private java.util.Date creationDate = null;

	/**
	 * <code>EPPContactCreateResp</code> default constructor.  The id is
	 * initialized to <code>null</code>.     The id and creation date must be
	 * set before invoking <code>encode</code>.
	 */
	public EPPContactCreateResp() {
		id = null;
	}

	// End EPPContactCreateResp.EPPContactCreateResp()

	/**
	 * <code>EPPContactCreateResp</code> constructor that takes the contact id
	 * as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aId Contact id to delete.
	 * @param aCreationDate Creation date
	 */
	public EPPContactCreateResp(
								EPPTransId aTransId, String aId,
								Date aCreationDate) {
		super(aTransId);

		id				 = aId;
		creationDate     = aCreationDate;
	}

	// End EPPContactCreateResp.EPPContactCreateResp(String, String, Date)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactCreateResp</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactCreateResp.getNamespace()

	/**
	 * Get creation date.
	 *
	 * @return java.util.Date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// End EPPContactCreateResp.getCreationDate()

	/**
	 * Set creation date.
	 *
	 * @param newCrDate java.util.Date
	 */
	public void setCreationDate(Date newCrDate) {
		creationDate = newCrDate;
	}

	// End EPPContactCreateResp.setCreationDate(Date)

	/**
	 * Validate the state of the <code>EPPContactCreateResp</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the name of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (id == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (creationDate == null) {
			throw new EPPCodecException("required attribute creationDate is not set");
		}
	}

	// End EPPDomainCreateResp.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactCreateResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPContactCreateResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactCreateResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPContactCreateResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// Id
		EPPUtil.encodeString(
							 aDocument, root, id, EPPContactMapFactory.NS,
							 ELM_CONTACT_ID);

		// Creation Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, creationDate,
								  EPPContactMapFactory.NS, ELM_CREATION_DATE);

		return root;
	}

	// End EPPContactCreateResp.doEncode(Document)

	/**
	 * Decode the <code>EPPContactCreateResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactCreateResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Contact Id
		id     = EPPUtil.decodeString(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CONTACT_ID);

		// Creation Date
		creationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CREATION_DATE);

		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPContactCreateResp.decode: "
										 + e);
		}
	}

	// End EPPContactCreateResp.doDecode(Element)

	/**
	 * Gets the contact id to delete.
	 *
	 * @return Contact Id    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getId() {
		return id;
	}

	// End EPPContactCreateResp.getId()

	/**
	 * Sets the contact id to delete.
	 *
	 * @param aId Contact Id
	 */
	public void setId(String aId) {
		id     = aId;
	}

	// End EPPContactCreateResp.setId(String)

	/**
	 * Compare an instance of <code>EPPContactCreateResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactCreateResp theComp = (EPPContactCreateResp) aObject;

		// Id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			return false;
		}

		return true;
	}

	// End EPPContactCreateResp.equals(Object)

	/**
	 * Clone <code>EPPContactCreateResp</code>.
	 *
	 * @return clone of <code>EPPContactCreateResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactCreateResp clone = (EPPContactCreateResp) super.clone();

		return clone;
	}

	// End EPPContactCreateResp.clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPContactCreateResp.toString()
}
