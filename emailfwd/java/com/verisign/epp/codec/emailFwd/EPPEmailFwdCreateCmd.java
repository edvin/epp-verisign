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
package com.verisign.epp.codec.emailFwd;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP EmailFwd &lt;create&gt; command, which provides a
 * transform     operation that allows a client to create a emailFwd object.
 * In addition to the standard EPP command elements, the &lt;create&gt;
 * command MUST contain a &lt;emailFwd:create&gt; element that identifies the
 * emailFwd namespace and the location of the emailFwd schema.     The
 * &lt;emailFwd:create&gt; element MUST contain the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name of the object to be created.  Use <code>getName</code> and
 * <code>setName</code> to get and set the element.
 * </li>
 * <li>
 * An   &lt;emailFwd:fwdTo&gt; element that contain the forwardTo Addresses/
 * .Use <code>getEmailForwardTo</code> and <code>setEmailForwardTo</code> to
 * get and set the forwardTo addresses.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:period&gt; element that contains the initial
 * registration period of the emailFwd object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:registrant&gt; element that contains the identifier
 * for the human or organizational social information (contact) object to be
 * associated with the emailFwd object as the object registrant.  This object
 * identifier MUST be known to the server before the contact object can be
 * associated with the emailFwd object. Use <code>getRegistrant</code> and
 * <code>setRegistrant</code> to get     and set the elements.
 * </li>
 * <li>
 * Zero or more &lt;emailFwd:contact&gt; elements that contain the registrant,
 * administrative, technical, and billing contact identifiers to be associated
 * with the emailFwd.  A contact identifier MUST be known to the server before
 * the contact can be associated with the emailFwd.  Only one contact
 * identifier of each type MAY be specified.  A server MAY provide contact
 * object services when providing emailFwd name object services.  The EPP
 * mapping for contact objects is described in [EPP-C].     Use
 * <code>getContacts</code> and <code>setContacts</code> to get     and set
 * the elements.  Contacts should only be specified if the     Contact Mapping
 * is supported.
 * </li>
 * <li>
 * A &lt;emailFwd:authInfo&gt; element that contains authorization information
 * to be associated with the emailFwd object.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the transaction identifier associated with
 * successful creation of a emailFwd object becomes the authorization
 * identifier required to transfer sponsorship of the emailFwd object.  A
 * client MUST retain all transaction identifiers associated with emailFwd
 * object creation and protect them from disclosure.  A client MUST also
 * provide a copy of the transaction identifier information to the emailFwd
 * registrant, who will need this information to request a emailFwd transfer
 * through a different client.     <br>
 * <br>
 * <code>EPPEmailFwdCreateResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPEmailFwdCreateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateResp
 */
public class EPPEmailFwdCreateCmd extends EPPCreateCmd {
	/** XML Element Name of <code>EPPEmailFwdCreateCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:create";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** XML Element Name for the <code>fwdTo</code> attribute. */
	private final static String ELM_EMAILFWD_TO = "emailFwd:fwdTo";

	/** XML Element Name of <code>registrant</code> root element. */
	private final static String ELM_REGISTRANT = "emailFwd:registrant";

	/** XML tag name for the <code>contacts</code> attribute. */
	private final static String ELM_CONTACT = "emailFwd:contact";

	/** EmailFwd Name of emailFwd to create. */
	private String name = null;

	/** DOCUMENT ME! */
	private String forwardTo = null;

	/** Registration Period. */
	private EPPEmailFwdPeriod period = null;

	/** EmailFwd Contacts */
	private Vector contacts = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/** registrant. */
	private java.lang.String registrant = null;

	/**
	 * Allocates a new <code>EPPEmailFwdCreateCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * forwardTo is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>UNSPEC_PERIOD</code>
	 * </li>
	 * <li>
	 * contacts is set to to <code>null</code>
	 * </li>
	 * <li>
	 * transaction id is set to <code>null</code>.
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPEmailFwdCreateCmd() {
		name		  = null;
		forwardTo     = null;
		period		  = null;
		contacts	  = null;
	}

	// End EPPEmailFwdCreateCmd.EPPEmailFwdCreateCmd()

	/**
	 * Allocates a new <code>EPPEmailFwdCreateCmd</code> with a emailFwd name.
	 * The other attributes are initialized as follows:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * period is set to <code>UNSPEC_PERIOD</code>
	 * </li>
	 * <li>
	 * servers is set to <code>null</code>
	 * </li>
	 * <li>
	 * contacts is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name
	 * @param aForwardTo EmailFwdTo address
	 * @param aAuthInfo EPPAuthInfo    authorization information
	 */
	public EPPEmailFwdCreateCmd(
								String aTransId, String aName, String aForwardTo,
								EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		  = aName;
		forwardTo     = aForwardTo;
		authInfo	  = aAuthInfo;
		authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
		period		 = null;
		contacts     = null;
	}

	// End EPPEmailFwdCreateCmd.EPPEmailFwdCreateCmd(String,String,String, EPPAuthInfo)

	/**
	 * Allocates a new <code>EPPEmailFwdCreateCmd</code> with all attributes
	 * specified     by the arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name
	 * @param aForwardTo EmailFwdTo name
	 * @param someContacts EmailFwd contacts.  Should be <code>null</code> if
	 * 		  the Contact Mapping is not supported.
	 * @param aPeriod Value greater than or equal to <code>MIN_PERIOD</code> or
	 * 		  less than or equal to <code>MAX_PERIOD</code>.
	 * @param aAuthInfo EPPAuthInfo    authorization information
	 */
	public EPPEmailFwdCreateCmd(
								String aTransId, String aName, String aForwardTo,
								Vector someContacts, EPPEmailFwdPeriod aPeriod,
								EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		  = aName;
		forwardTo     = aForwardTo;
		period		  = aPeriod;
		contacts	  = someContacts;
		authInfo	  = aAuthInfo;
		authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
	}

	// End EPPEmailFwdCreateCmd.EPPEmailFwdCreateCmd(String, String, String, Vector, EPPEmailFwdPeriod, EPPAuthInfo)

	/**
	 * Get the EPP command Namespace associated with EPPEmailFwdCreateCmd.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdCreateCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPEmailFwdCreateCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the <code>EPPCodecException</code> will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		//EmailFwd name
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (forwardTo == null) {
			throw new EPPCodecException("forwardTo required attribute is not set");
		}

		//AuthInfo
		if (authInfo == null) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPEmailFwdCreateCmd.isValid()

	/**
	 * Get the emailFwd name to create.
	 *
	 * @return EmailFwd Name
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdCreateCmd.getName()

	/**
	 * Get the emailFwd  to.
	 *
	 * @return EmailFwdTo
	 */
	public String getForwardTo() {
		return forwardTo;
	}

	// End EPPEmailFwdCreateCmd.getName()

	/**
	 * Set the emailFwd name to create.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdCreateCmd.setName(String)

	/**
	 * Set the emailFwd name to create.
	 *
	 * @param aForwardTo EmailFwd Name
	 */
	public void setForwardTo(String aForwardTo) {
		forwardTo = aForwardTo;
	}

	// End EPPEmailFwdCreateCmd.setForwardTo(String)

	/**
	 * Gets the contacts.
	 *
	 * @return Vector of <code>EPPEmailFwdContact</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getContacts() {
		return contacts;
	}

	// End EPPEmailFwdCreateCmd.getContacts()

	/**
	 * Sets the contacts.
	 *
	 * @param aContacts DOCUMENT ME!
	 */
	public void setContacts(Vector aContacts) {
		contacts = aContacts;
	}

	// End EPPEmailFwdCreateCmd.setContacts(Vector)

	/**
	 * Compare an instance of <code>EPPEmailFwdCreateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdCreateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdCreateCmd theComp = (EPPEmailFwdCreateCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// forwardTo
		if (
			!(
					(forwardTo == null) ? (theComp.forwardTo == null)
											: forwardTo.equals(theComp.forwardTo)
				)) {
			return false;
		}

		// Period
		if (
			!(
					(period == null) ? (theComp.period == null)
										 : period.equals(theComp.period)
				)) {
			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			return false;
		}

		// EmailFwd Contacts
		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			if (!EPPUtil.equalVectors(contacts, theComp.contacts)) {
				return false;
			}
		}

		// registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
				)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdCreateCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdCreateCmd</code>.
	 *
	 * @return clone of <code>EPPEmailFwdCreateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdCreateCmd clone = (EPPEmailFwdCreateCmd) super.clone();

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();

			for (int i = 0; i < contacts.size(); i++)
				clone.contacts.setElementAt(
											((EPPEmailFwdContact) contacts
											 .elementAt(i)).clone(), i);
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPEmailFwdPeriod) period.clone();
		}

		return clone;
	}

	// End EPPEmailFwdCreateCmd.clone()

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

	// End EPPEmailFwdCreateCmd.toString()

	/**
	 * Get authorization information
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPEmailFwdCreateCmd.getAuthInfo()

	/**
	 * Gets the registration period in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPEmailFwdPeriod getPeriod() {
		return period;
	}

	// End EPPEmailFwdCreateCmd.getPeriod()

	/**
	 * Get registrant.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRegistrant() {
		return registrant;
	}

	// End EPPEmailFwdCreateCmd.getRegistrant()

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo java.lang.String
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
		}
	}

	// End EPPEmailFwdCreateCmd.setAuthInfo(EPPAuthInfo)

	/**
	 * Sets the registration period in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPEmailFwdPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPEmailFwdCreateCmd.setPeriod(EPPEmailFwdPeriod)

	/**
	 * Set registrant.
	 *
	 * @param newRegistrant java.lang.String
	 */
	public void setRegistrant(java.lang.String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPEmailFwdCreateCmd.setPeriod(EPPEmailFwdPeriod)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPEmailFwdCreateCmd instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPEmailFwdCreateCmd instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPEmailFwdCreateCmd
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			//Validate States
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPEmailFwdCreateCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);

		//Forward Address
		EPPUtil.encodeString(
							 aDocument, root, forwardTo,
							 EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_TO);

		// Period with Attribute of Unit
		if ((period != null) && !period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// Registrant
		if (registrant != null) {
			EPPUtil.encodeString(
								 aDocument, root, registrant,
								 EPPEmailFwdMapFactory.NS, ELM_REGISTRANT);
		}

		// Contacts
		if (contacts != null) {
			if (
				EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
				EPPUtil.encodeCompVector(aDocument, root, contacts);
			}
			else {
				throw new EPPEncodeException("Contacts specified when the Contact Mapping is not supported");
			}
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPEmailFwdCreateCmd.doEncode(Document)

	/**
	 * Decode the EPPEmailFwdCreateCmd attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement Root DOM Element to decode EPPEmailFwdCreateCmd from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// EmailFwd Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		forwardTo =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_TO);

		// Period
		period =
			(EPPEmailFwdPeriod) EPPUtil.decodeComp(
												   aElement,
												   EPPEmailFwdMapFactory.NS,
												   EPPEmailFwdPeriod.ELM_NAME,
												   EPPEmailFwdPeriod.class);

		// Registrant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_REGISTRANT);

		// Contacts
		contacts =
			EPPUtil.decodeCompVector(
									 aElement, EPPEmailFwdMapFactory.NS,
									 ELM_CONTACT, EPPEmailFwdContact.class);

		if (contacts.size() == 0) {
			contacts = null;
		}

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPEmailFwdMapFactory.NS,
											 EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPEmailFwdCreateCmd.doDecode(Element)
}
