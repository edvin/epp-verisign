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

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.codec.persreg;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
// Imports
//----------------------------------------------
// SDK Imports
import com.verisign.epp.codec.gen.*;


/**
 * Personal Registration &ltcreate&gt extension, which  allows for a client to
 * provide a consent identifier to override a Defensive Registration. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPPersRegCreate implements EPPCodecComponent {
	/** XML root tag for <code>EPPPersRegCreate</code>. */
	public static final String ELM_NAME = "persReg:create";

	/** XML tag name for the <code>_consentID</code> attribute. */
	private static final String ELM_CONSENT_ID = "persReg:consentID";

	/** Personal Registration Consent Identifier. */
	private String _consentID;

	/**
	 * Default constructor for <code>EPPPersRegCreate</code>.
	 */
	public EPPPersRegCreate() {
		_consentID = null;
	}

	// End EPPPersRegCreate.EPPPersRegCreate()

	/**
	 * Constructor for <code>EPPPersRegCreate</code>  that takes the consent
	 * identifier.
	 *
	 * @param aConsentID Consent identifier
	 */
	public EPPPersRegCreate(String aConsentID) {
		_consentID = aConsentID;
	}

	// End EPPPersRegCreate.EPPPersRegCreate(String)

	/**
	 * Gets the Personal Registration Consent Identifier.
	 *
	 * @return Consent identifier is defined; <code>null</code> otherwise.
	 */
	public String getConsentID() {
		return _consentID;
	}

	// End EPPPersRegCreate.getConsentID()

	/**
	 * Sets the Personal Registration Consent Identifier.
	 *
	 * @param aConsentID Consent Identifier
	 */
	public void setConsentID(String aConsentID) {
		_consentID = aConsentID;
	}

	// End EPPPersRegCreate.setConsentID(String)

	/**
	 * encode instance into a DOM element tree.    A DOM Document is passed as
	 * an argument and functions  as a factory for DOM objects.  The root
	 * element associated  with the instance is created and each instance
	 * attributeis  appended as a child node.
	 *
	 * @param aDocument DOM Document, which acts is an Element factory
	 *
	 * @return Element Root element associated with the object
	 *
	 * @exception EPPEncodeException Error encoding
	 * 			  <code>EPPPersRegCreate</code>
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (_consentID == null) {
			throw new EPPEncodeException("consentID required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPPersRegExtFactory.NS, ELM_NAME);
		root.setAttribute("xmlns:persReg", EPPPersRegExtFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPPersRegExtFactory.NS_SCHEMA);

		EPPUtil.encodeString(
							 aDocument, root, _consentID,
							 EPPPersRegExtFactory.NS, ELM_CONSENT_ID);

		return root;
	}

	// End implements encode(Document)

	/**
	 * decode a DOM element tree to initialize the  instance attributes.  The
	 * <code>aElement</code> argument  represents the root DOM element and is
	 * used to traverse  the DOM nodes for instance attribute values.
	 *
	 * @param aElement <code>Element</code> to decode
	 *
	 * @exception EPPDecodeException Error decoding <code>Element</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Consent ID
		_consentID =
			EPPUtil.decodeString(
								 aElement, EPPPersRegExtFactory.NS,
								 ELM_CONSENT_ID);
	}

	// End implements decode(Element)

	/**
	 * clone an <code>EPPCodecComponent</code>.
	 *
	 * @return clone of concrete <code>EPPPersRegCreate</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPPersRegCreate clone = (EPPPersRegCreate) super.clone();

		return clone;
	}

	// End implements clone()

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

	// End EPPPersRegCreate.toString()

	/**
	 * Compare an instance of <code>EPPPersRegCreate</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPPersRegCreate)) {
			return false;
		}

		EPPPersRegCreate theComp = (EPPPersRegCreate) aObject;

		if (
			!(
					(_consentID == null) ? (theComp._consentID == null)
											 : _consentID.equals(theComp._consentID)
				)) {
			return false;
		}

		return true;
	}

	// End EPPPersRegCreate.equals(Object)
}


// End class EPPPersRegCreate
