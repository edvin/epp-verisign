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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a contact disclose definition.  <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPContactDisclose implements EPPCodecComponent {
	/** XML tag name for the <code>org</code> attribute. */
	public final static String ELM_NAME_DISCLOSE = "contact:disclose";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_CONTACT_NAME = "contact:name";

	/** XML tag name for the <code>org</code> attribute. */
	private final static String ELM_CONTACT_ORG = "contact:org";

	/** XML tag name for the <code>addr</code> attribute. */
	private final static String ELM_CONTACT_ADDR = "contact:addr";

	/** XML tag name for the <code>voice</code> attribute. */
	private final static String ELM_CONTACT_VOICE = "contact:voice";

	/** XML tag name for the <code>fax</code> attribute. */
	private final static String ELM_CONTACT_FAX = "contact:fax";

	/** XML tag name for the <code>email</code> attribute. */
	private final static String ELM_CONTACT_EMAIL = "contact:email";

	/** XML Attribute Name for disclose flag. */
	private final static String ATTR_FLAG = "flag";

	/** Value of the FALSE in contact disclose flag mapping */
	public final static java.lang.String ATTR_FLAG_FALSE = "0";

	/** Value of the TRUE in contact disclose flag mapping */
	public final static java.lang.String ATTR_FLAG_TRUE = "1";

	/** XML Attribute Name for disclose type. */
	private final static String ATTR_TYPE = "type";

	/** Value of the LOC in contact disclose type mapping */
	public final static java.lang.String ATTR_TYPE_LOC = "loc";

	/** Value of the INT in contact disclose type mapping */
	public final static java.lang.String ATTR_TYPE_INT = "int";

	/** DOCUMENT ME! */
	private final static int MAX_NAMES = 2;

	/** DOCUMENT ME! */
	private final static int MAX_ORGS = 2;

	/** DOCUMENT ME! */
	private final static int MAX_ADDRS = 2;

	/**
	 * XML root element tag name for contact disclose definition The value
	 * needs to be set before calling encode(Document) and default value is
	 * set to <code>ELM_NAME_DISCLOSE</code>.
	 */
	private java.lang.String rootName = ELM_NAME_DISCLOSE;

	/** Attribute Name of <code>EPPContactDisclose</code> root element. */
	private java.lang.String flag = ATTR_FLAG_FALSE;

	/** Contact names, which is a <code>Vector</code>. */
	private Vector names = null;

	/** Contact orgs, which is a <code>Vector</code>. */
	private Vector orgs = null;

	/** Contact addresses, which is a <code>Vector</code>. */
	private Vector addresses = null;

	/** contact voice */
	private String voice = null;

	/** contact fax */
	private String fax = null;

	/** contact email */
	private String email = null;

	/**
	 * A flag to show whether validateState() been called before calling
	 * encode(Document)
	 */
	private boolean validatedFlag = true;

	/**
	 * <code>EPPContactDisclose</code> default constructor.  Must call required
	 * setter methods before  invoking <code>encode</code>.
	 */
	public EPPContactDisclose() {
		// Default values set in attribute definitions.
	}

	// End EPPContactDisclose.EPPContactDisclose()

	/**
	 * Get contact disclose names.
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getNames() {
		return names;
	}

	// End EPPContactDisclose.getNames()

	/**
	 * Set contact disclose names.
	 *
	 * @param newNames java.util.Vector
	 */
	public void setNames(java.util.Vector newNames) {
		names = newNames;
	}

	// End EPPContactDisclose.setNames(Vector)

	/**
	 * Get contact disclose orgs.
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getOrgs() {
		return orgs;
	}

	// End EPPContactDisclose.getOrgs()

	/**
	 * Set contact disclose orgs.
	 *
	 * @param newOrgs java.util.Vector
	 */
	public void setOrgs(java.util.Vector newOrgs) {
		orgs = newOrgs;
	}

	// End EPPContactDisclose.setOrgs(Vector)

	/**
	 * Get contact disclose addresses.
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getAddresses() {
		return addresses;
	}

	// End EPPContactDisclose.getAddresses()

	/**
	 * Set contact disclose addresses.
	 *
	 * @param newAddresses java.util.Vector
	 */
	public void setAddresses(java.util.Vector newAddresses) {
		addresses = newAddresses;
	}

	// End EPPContactDisclose.setAddresses(Vector)

	/**
	 * Get contact disclose flag.
	 *
	 * @return String Contact disclose flag
	 */
	public String getFlag() {
		return flag;
	}

	// End EPPContactDisclose.getFlag()

	/**
	 * Set contact disclose flag.
	 *
	 * @param newFlag String
	 */
	public void setFlag(String newFlag) {
		flag = newFlag;
	}

	// End EPPContactDisclose.setFlag()	

	/**
	 * Gets the contact disclose voice
	 *
	 * @return Client disclose voice if defined; <code>null</code> otherwise.
	 */
	public String getVoice() {
		return voice;
	}

	// End EPPContactDisclose.getVoice()

	/**
	 * Sets the contact disclose voice
	 *
	 * @param aVoice Client disclose voice
	 */
	public void setVoice(String aVoice) {
		voice = aVoice;
	}

	// End EPPContactDisclose.setVoice()

	/**
	 * Gets the contact disclose fax
	 *
	 * @return Client disclose fax if defined; <code>null</code> otherwise.
	 */
	public String getFax() {
		return fax;
	}

	// End EPPContactDisclose.getFax()

	/**
	 * Sets the contact disclose fax
	 *
	 * @param aFax Client disclose fax
	 */
	public void setFax(String aFax) {
		fax = aFax;
	}

	// End EPPContactDisclose.setFax()

	/**
	 * Gets the contact disclose email
	 *
	 * @return Client disclose email if defined; <code>null</code> otherwise.
	 */
	public String getEmail() {
		return email;
	}

	// End EPPContactDisclose.getEmail()

	/**
	 * Sets the contact disclose email
	 *
	 * @param aEmail Client disclose email
	 */
	public void setEmail(String aEmail) {
		email = aEmail;
	}

	// End EPPContactDisclose.setEmail()	

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactDisclose</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return DOCUMENT ME!
	 *
	 * @exception EPPEncodeException
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element currElm = null;
		Text    currVal = null;

		// validate states if validatedFlag been set
		if (validatedFlag) {
			try {
				validateState();
			}
			 catch (EPPCodecException e) {
				throw new EPPEncodeException("Invalid state on EPPContactPostalDefination.encode: "
											 + e);
			}
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, rootName);

		// add attribute type
		root.setAttribute(ATTR_FLAG, flag);

		// names
		EPPUtil.encodeCompVector(aDocument, root, names);

		// orgs
		EPPUtil.encodeCompVector(aDocument, root, orgs);

		// addresses
		EPPUtil.encodeCompVector(aDocument, root, addresses);

		// Voice 
		if (voice != null) {
			EPPUtil.encodeString(
								 aDocument, root, voice, EPPContactMapFactory.NS,
								 ELM_CONTACT_VOICE);
		}

		// Fax 
		if (fax != null) {
			EPPUtil.encodeString(
								 aDocument, root, fax, EPPContactMapFactory.NS,
								 ELM_CONTACT_FAX);
		}

		// Email 
		if (email != null) {
			EPPUtil.encodeString(
								 aDocument, root, email, EPPContactMapFactory.NS,
								 ELM_CONTACT_EMAIL);
		}

		return root;
	}

	// End EPPContactDisclose.encode(Document)

	/**
	 * Decode the <code>EPPContactDisclose</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactDisclose</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		Element currElm = null;

		// flag
		flag     = aElement.getAttribute(ATTR_FLAG);

		// names
		names =
			EPPUtil.decodeCompVector(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_NAME,
									 EPPContactDiscloseName.class);

		// orgs
		orgs =
			EPPUtil.decodeCompVector(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_ORG,
									 EPPContactDiscloseOrg.class);

		// addresses
		addresses =
			EPPUtil.decodeCompVector(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_ADDR,
									 EPPContactDiscloseAddress.class);

		// voice
		voice =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CONTACT_VOICE);

		// fax
		fax     = EPPUtil.decodeString(
									   aElement, EPPContactMapFactory.NS,
									   ELM_CONTACT_FAX);

		// email
		email =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CONTACT_EMAIL);
	}

	// End EPPContactDisclose.decode(Element)

	/**
	 * implements a deep <code>EPPContactDisclose</code> compare.
	 *
	 * @param aObject <code>EPPContactDisclose</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactDisclose)) {
			return false;
		}

		EPPContactDisclose theComp = (EPPContactDisclose) aObject;

		// flag
		if (!flag.equals(theComp.flag)) {
			return false;
		}

		// names
		if (names != null) {
			if (!EPPUtil.equalVectors(names, theComp.names)) {
				return false;
			}
		}

		// orgs
		if (orgs != null) {
			if (!EPPUtil.equalVectors(orgs, theComp.orgs)) {
				return false;
			}
		}

		// addresses
		if (addresses != null) {
			if (!EPPUtil.equalVectors(addresses, theComp.addresses)) {
				return false;
			}
		}

		// voice
		if (
			!(
					(voice == null) ? (theComp.voice == null)
										: voice.equals(theComp.voice)
				)) {
			return false;
		}

		// fax
		if (!((fax == null) ? (theComp.fax == null) : fax.equals(theComp.fax))) {
			return false;
		}

		// email
		if (
			!(
					(email == null) ? (theComp.email == null)
										: email.equals(theComp.email)
				)) {
			return false;
		}

		return true;
	}

	// End EPPContactDisclose.equals(Object)

	/**
	 * Clone <code>EPPContactDisclose</code>.
	 *
	 * @return clone of <code>EPPContactDisclose</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactDisclose clone = (EPPContactDisclose) super.clone();

		if (names != null) {
			clone.names = (Vector) names.clone();

			for (int i = 0; i < names.size(); i++)
				clone.names.setElementAt(
										 ((EPPContactDiscloseName) names
										  .elementAt(i)).clone(), i);
		}

		if (orgs != null) {
			clone.orgs = (Vector) orgs.clone();

			for (int i = 0; i < orgs.size(); i++)
				clone.orgs.setElementAt(
										((EPPContactDiscloseOrg) orgs.elementAt(i))
										.clone(), i);
		}

		if (addresses != null) {
			clone.addresses = (Vector) addresses.clone();

			for (int i = 0; i < addresses.size(); i++)
				clone.addresses.setElementAt(
											 ((EPPContactDiscloseAddress) addresses
											  .elementAt(i)).clone(), i);
		}

		return clone;
	}

	// End EPPContactDisclose.clone()

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

	// End EPPContactDefinition.toString()

	/**
	 * Validate the state of the <code>EPPContactPostalDefination</code>
	 * instance.  A valid state means that all of the required attributes have
	 * been set.  If validateState     returns without an exception, the state
	 * is valid.  If the state is not     valid, the EPPCodecException will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	protected void validateState() throws EPPCodecException {
		boolean bFound = false;

		if (rootName == null) {
			throw new EPPCodecException("root element name is not set");
		}

		if (!rootName.equals(ELM_NAME_DISCLOSE)) {
			throw new EPPCodecException("root element name is not recognized");
		}

		if ((names != null) && names.elements().hasMoreElements()) {
			bFound = true;

			if (names.size() > MAX_NAMES) {
				throw new EPPCodecException("name lines exceed the maximum");
			}
		}

		if ((orgs != null) && orgs.elements().hasMoreElements()) {
			bFound = true;

			if (orgs.size() > MAX_ORGS) {
				throw new EPPCodecException("org lines exceed the maximum");
			}
		}

		if ((addresses != null) && addresses.elements().hasMoreElements()) {
			bFound = true;

			if (addresses.size() > MAX_ADDRS) {
				throw new EPPCodecException("address lines exceed the maximum");
			}
		}

		if ((voice != null) || (fax != null) || (email != null)) {
			bFound = true;
		}

		if (!bFound) {
			throw new EPPCodecException("disclose element is empty");
		}
	}

	// End EPPContactPostalDefination.validateState()

	/**
	 * Get root tag name for contact postal definition.
	 *
	 * @return String  root tag name
	 */
	public String getRootName() {
		return rootName;
	}

	// End EPPContactDisclose.getRootName()

	/**
	 * Set root tag name for contact postal definition.
	 *
	 * @param newRootName String
	 */
	public void setRootName(String newRootName) {
		rootName = newRootName;
	}

	// End EPPContactDisclose.setRootName(String)

	/**
	 * Show whether needs to call validateState()
	 *
	 * @return boolean
	 */
	public boolean isValidated() {
		return validatedFlag;
	}

	// End EPPContactDisclose.isValidated()

	/**
	 * Set validated flag.
	 *
	 * @param newValidatedFlag boolean
	 */
	public void setValidatedFlag(boolean newValidatedFlag) {
		validatedFlag = newValidatedFlag;
	}

	// End EPPContactDisclose.setValidatedFlag(boolean)
}
