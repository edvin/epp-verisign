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
 * Represents attributes to add, remove or change with a
 * <code>EPPEmailFwdUpdateCmd</code>.     In
 * <code>EPPEmailFwdUpdateCmd</code>, an instance of
 * <code>EPPEmailFwdAddRemove</code> is used     to specify the attributes to
 * add; an instance of <code>EPPEmailFwdAddRemove</code> is used     to
 * specify the attributes to remove, and an instance of
 * <code>EPPEmailFwdAddRemove</code> is used     to specify the attributes to change<br>
 * <br>
 * The EmailFwdEmailFwd Mapping Specification describes the following
 * attributes:<br>
 * 
 * <ul>
 * <li>
 * Zero or more &lt;emailFwd:contact&gt; elements that contain the registrant,
 * administrative, technical, and billing contact identiEmailFwdfiers to be
 * associated with the emailFwd.  Use <code>getContacts</code> and
 * <code>setContacts</code> to get and set the element.  This attribute will
 * only be allowed if the Contact Mapping is supported.
 * </li>
 * <li>
 * One or two &lt;emailFwd:status&gt; elements that contain status values to be
 * applied to or removed from the emailFwd object.  Use
 * <code>getStatuses</code> and <code>setStatuses</code> to get and set the
 * element.
 * </li>
 * <li>
 * For <code>change</code> only, A &lt;emailFwd:registrant&gt; element that
 * contains the identifier for the human or organizational social information
 * (contact) object to be associated with the emailFwd object as the object
 * registrant.  This object identifier MUST be known to the server before the
 * contact object can be associated with the emailFwd object. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get and set
 * the element.
 * </li>
 * <li>
 * For <code>change</code> only, An OPTIONAL &lt;emailFwd:fwdTo&gt; element
 * that contains the email forwardTo addresses. Use <code>getForwardTo</code>
 * and <code>setForwardTo</code> to get and set the forwardTo addresses.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the maximum number of emailFwd attribute
 * elements is subject to the number of values currently associated with the
 * emailFwd object.  <code>EPPEmailFwdAddRemove</code> will delegate the
 * validation of     the cardinality of the emailFwd attributes elements to
 * the EPP Server.     <br><br>
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdUpdateCmd
 */
public class EPPEmailFwdAddRemove implements EPPCodecComponent {
	/** mode of <code>EPPEmailFwdAddRemove</code> is not specified. */
	final static short MODE_NONE = 0;

	/** mode of <code>EPPEmailFwdAddRemove</code> is to add attributes. */
	final static short MODE_ADD = 1;

	/** mode of <code>EPPEmailFwdAddRemove</code> is to remove attributes. */
	final static short MODE_REMOVE = 2;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPEmailFwdAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_ADD = "emailFwd:add";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPEmailFwdAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_REMOVE = "emailFwd:rem";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPEmailFwdAddRemove.MODE_CHANGE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_CHANGE = "emailFwd:chg";

	/** mode of <code>EPPEmailFwdAddRemove</code> is to change attributes. */
	final static short MODE_CHANGE = 3;

	/** XML tag name for the <code>contacts</code> attribute. */
	private final static String ELM_CONTACT = "emailFwd:contact";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "emailFwd:status";

	/** XML tag name for the <code>servers</code> attribute. */
	private final static String ELM_REGISTRANT = "emailFwd:registrant";

	/** XML Element Name for the <code>fwdTo</code> attribute. */
	private final static String ELM_EMAILFWD_TO = "emailFwd:fwdTo";

	/**
	 * Mode of EPPEmailFwdAddRemove.  Must be <code>MODE_ADD</code> or
	 * <code>MODE_REMOVE</code> to be valid.  This     attribute will be set
	 * by the parent container <code>EPPCodecComponent</code>.  For example,
	 * <code>EPPEmailFwdUpdateCmd</code> will set the mode for its
	 * <code>EPPEmailFwdAddRemove</code> instances.
	 */
	private short mode = MODE_NONE;

	/** Contacts to add or remove. */
	private Vector contacts = null;

	/** Status to add or remove. */
	private Vector statuses = null;

	/** authorization information to change. */
	private EPPAuthInfo authInfo = null;

	/** registrant to change. */
	private java.lang.String registrant = null;

	/** forwardTo to change. */
	private java.lang.String forwardTo = null;

	/**
	 * Default constructor for <code>EPPEmailFwdAddRemove</code>.  All of the
	 * attribute     default to <code>null</code> to indicate no modification.
	 */
	public EPPEmailFwdAddRemove() {
		contacts	   = null;
		statuses	   = null;
		registrant     = null;
		forwardTo	   = null;
		authInfo	   = null;
	}

	// End EPPEmailFwdAddRemove()

	/**
	 * Constructor for <code>EPPEmailFwdAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param someContacts Vector of <code>EPPEmailFwdContact</code> instances.
	 * 		  Is     <code>null</code> or empty for no modifications.  If the
	 * 		  Contact Mapping is not supported, this value should be
	 * 		  <code>null</code>.
	 * @param someStatuses Vector of status <code>String</code>'s.  One of the
	 * 		  <code>EPPEmailFwdInfoResp.STATUS_</code> contants can be used
	 * 		  for     each of the status values.    Is <code>null</code> or
	 * 		  empty     for no modifications.
	 */
	public EPPEmailFwdAddRemove(Vector someContacts, Vector someStatuses) {
		contacts     = someContacts;
		statuses     = someStatuses;
	}

	// EPPEmailFwdAddRemove(Vector, Vector, Vector)

	/**
	 * Constructor for <code>EPPEmailFwdAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aRegistrant <code>String</code>            registrant for the
	 * 		  change mode
	 * @param aForwardTo <code>String</code>         forwardTo address of the
	 * 		  email.
	 * @param aAuthInfo <code>EPPAuthInfo</code>    authorization information
	 * 		  for the change mode
	 */
	public EPPEmailFwdAddRemove(
								String aRegistrant, String aForwardTo,
								EPPAuthInfo aAuthInfo) {
		registrant     = aRegistrant;
		forwardTo	   = aForwardTo;
		authInfo	   = aAuthInfo;
		authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
	}

	// End EPPEmailFwdAddRemove(String, EPPAuthInfo)

	/**
	 * Gets the contacts to add or remove.
	 *
	 * @return Vector of <code>EPPEmailFwdContact</code> instances.
	 */
	public Vector getContacts() {
		return contacts;
	}

	// End EPPEmailFwdAddRemove.getContacts()

	/**
	 * Sets the contacts to add or remove.
	 *
	 * @param aContacts DOCUMENT ME!
	 */
	public void setContacts(Vector aContacts) {
		contacts = aContacts;
	}

	// End EPPEmailFwdAddRemove.setContacts(Vector)

	/**
	 * Gets the statuses to add or remove.  The
	 * <code>EPPEmailFwdInfoResp.STATUS_</code>     constants can be used for
	 * the statuses.
	 *
	 * @return Vector of status <code>String</code> instances.
	 */
	public Vector getStatuses() {
		return statuses;
	}

	// End EPPEmailFwdAddRemove.getStatuses()

	/**
	 * Sets the statuses to add or remove.  The
	 * <code>EPPEmailFwdInfoResp.STATUS_</code>     constants can be used for
	 * the statuses.
	 *
	 * @param aStatuses Vector of status <code>String</code> instances.
	 */
	public void setStatuses(Vector aStatuses) {
		statuses = aStatuses;
	}

	// End EPPEmailFwdAddRemove.setStatuses(Vector)

	/**
	 * Return if EmailFwd Contacts is supported.
	 *
	 * @return <code>true</code> if contacts are supported; <code>false</code>
	 * 		   otherwise.
	 */
	public boolean contactsSupported() {
		return EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT);
	}

	// End EPPEmailFwdAddRemove.contactsSupported()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdAddRemove</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPEmailFwdAddRemove</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdAddRemove</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root;

		// Change mode
		if (mode == MODE_CHANGE) {
			root =
				aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_CHANGE);

			if (forwardTo != null) {
				EPPUtil.encodeString(
									 aDocument, root, forwardTo,
									 EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_TO);
			}

			if (registrant != null) {
				EPPUtil.encodeString(
									 aDocument, root, registrant,
									 EPPEmailFwdMapFactory.NS, ELM_REGISTRANT);
			}

			if (authInfo != null) {
				EPPUtil.encodeComp(aDocument, root, authInfo);
			}

			return root;
		}

		// Add or Remove mode
		if (mode == MODE_ADD) {
			root = aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_ADD);
		}
		else if (mode == MODE_REMOVE) {
			root =
				aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_REMOVE);
		}
		else {
			throw new EPPEncodeException("Invalid EPPEmailFwdAddRemove mode of "
										 + mode);
		}

		// Contacts
		if (contacts != null) {
			if (contactsSupported()) {
				EPPUtil.encodeCompVector(aDocument, root, contacts);
			}
			else {
				throw new EPPEncodeException("Contacts specified when the Contact Mapping is not supported");
			}
		}

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		return root;
	}

	// End EPPEmailFwdAddRemove.encode(Document)

	/**
	 * Decode the <code>EPPEmailFwdAddRemove</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdAddRemove</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		if (aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_ADD))) {
			mode = MODE_ADD;
		}
		else if (aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_REMOVE))) {
			mode = MODE_REMOVE;
		}
		else if (aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_CHANGE))) {
			mode = MODE_CHANGE;
		}
		else {
			throw new EPPDecodeException("Invalid EPPEmailFwdAddRemove mode of "
										 + aElement.getLocalName());
		}

		// Change Mode
		if (mode == MODE_CHANGE) {
			//ForwardTo Address
			forwardTo =
				EPPUtil.decodeString(
									 aElement, EPPEmailFwdMapFactory.NS,
									 ELM_EMAILFWD_TO);

			// Registrant
			registrant =
				EPPUtil.decodeString(
									 aElement, EPPEmailFwdMapFactory.NS,
									 ELM_REGISTRANT);

			// AuthInfo
			authInfo =
				(EPPAuthInfo) EPPUtil.decodeComp(
												 aElement,
												 EPPEmailFwdMapFactory.NS,
												 EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO,
												 EPPAuthInfo.class);
		}
		else {
			// Add & Remove Mode
			// Contacts
			contacts =
				EPPUtil.decodeCompVector(
										 aElement, EPPEmailFwdMapFactory.NS,
										 ELM_CONTACT, EPPEmailFwdContact.class);

			if (contacts.size() == 0) {
				contacts = null;
			}

			// Statuses
			statuses =
				EPPUtil.decodeCompVector(
										 aElement, EPPEmailFwdMapFactory.NS,
										 ELM_STATUS, EPPEmailFwdStatus.class);
		}
	}

	// End EPPEmailFwdAddRemove.decode(Element)

	/**
	 * implements a deep <code>EPPEmailFwdAddRemove</code> compare.
	 *
	 * @param aObject <code>EPPEmailFwdAddRemove</code> instance to compare
	 * 		  with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdAddRemove)) {
			return false;
		}

		EPPEmailFwdAddRemove theComp = (EPPEmailFwdAddRemove) aObject;

		// Mode
		if (mode != theComp.mode) {
			return false;
		}

		// Contacts
		if (contactsSupported()) {
			if (!EPPUtil.equalVectors(contacts, theComp.contacts)) {
				return false;
			}
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
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

		// Registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
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

		return true;
	}

	// End EPPEmailFwdAddRemove.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdAddRemove</code>.
	 *
	 * @return clone of <code>EPPEmailFwdAddRemove</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdAddRemove clone = null;

		clone = (EPPEmailFwdAddRemove) super.clone();

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();

			for (int i = 0; i < contacts.size(); i++)
				clone.contacts.setElementAt(
											((EPPEmailFwdContact) contacts
											 .elementAt(i)).clone(), i);
		}

		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();
		}

		if (forwardTo != null) {
			clone.forwardTo = forwardTo;
		}

		if (registrant != null) {
			clone.registrant = registrant;
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPEmailFwdAddRemove.clone()

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

	// End EPPEmailFwdAddRemove.toString()

	/**
	 * Get authorization information for the change mode
	 *
	 * @return com.verisign.epp.codec.emailFwd.EPPEmailFwdAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPEmailFwdAddRemove.getAuthInfo()

	/**
	 * Get registrant for the change mode
	 *
	 * @return java.lang.String
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPEmailFwdAddRemove.getRegistrant()

	/**
	 * Get forwardTo Address
	 *
	 * @return java.lang.String
	 */
	public String getForwardTo() {
		return forwardTo;
	}

	// End EPPEmailFwdAddRemove.getForwardTo()

	/**
	 * Set authorization information for the change mode
	 *
	 * @param newAuthInfo com.verisign.epp.codec.emailFwd.EPPEmailFwdAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
		}
	}

	// End EPPEmailFwdAddRemove.setAuthInfo(EPPAuthInfo)

	/**
	 * Set registrant for the change mode
	 *
	 * @param newRegistrant java.lang.String
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPEmailFwdAddRemove.setRegistrant(String)

	/**
	 * Set forwardTo Address  for the change mode
	 *
	 * @param newForwardTo java.lang.String
	 */
	public void setForwardTo(String newForwardTo) {
		forwardTo = newForwardTo;
	}

	// End EPPEmailFwdAddRemove.setForwardTo(String)

	/**
	 * Gets the mode of <code>EPPEmailFwdAddRemove</code>.  There are two valid
	 * modes <code>EPPEmailFwdAddRemove.MODE_ADD</code> and
	 * <code>EPPEmailFwdAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPEmailFwdAddRemove.MODE_NONE</code>.
	 *
	 * @return One of the <code>EPPEmailFwdAddRemove_MODE</code> constants.
	 */
	short getMode() {
		return mode;
	}

	// End EPPEmailFwdAddRemove.getMode()

	/**
	 * Sets the mode of <code>EPPEmailFwdAddRemove</code>.  There are two valid
	 * modes <code>EPPEmailFwdAddRemove.MODE_ADD</code> and
	 * <code>EPPEmailFwdAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPEmailFwdAddRemove.MODE_NONE</code>
	 *
	 * @param aMode <code>EPPEmailFwdAddRemove.MODE_ADD</code> or
	 * 		  <code>EPPEmailFwdAddRemove.MODE_REMOVE</code>.
	 */
	void setMode(short aMode) {
		mode = aMode;
	}

	// End EPPEmailFwdAddRemove.setMode(short)
}
