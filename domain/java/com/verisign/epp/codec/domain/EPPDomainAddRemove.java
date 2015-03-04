/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.codec.domain;


// Log4j Imports
import org.apache.log4j.Logger;

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
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents attributes to add, remove or change with a
 * <code>EPPDomainUpdateCmd</code>.     In <code>EPPDomainUpdateCmd</code>, an
 * instance of <code>EPPDomainAddRemove</code> is used     to specify the
 * attributes to add; an instance of <code>EPPDomainAddRemove</code> is used
 * to specify the attributes to remove, and an instance of
 * <code>EPPDomainAddRemove</code> is used     to specify the attributes to change<br>
 * <br>
 * The Domain Mapping Specification describes the following attributes:<br>
 * 
 * <ul>
 * <li>
 * Zero or more &lt;domain:ns&gt; elements that contain the fully qualified
 * host name of a known host object.  Use <code>getServers</code> and
 * <code>setServers</code> to get and set the element.
 * </li>
 * <li>
 * Zero or more &lt;domain:contact&gt; elements that contain the registrant,
 * administrative, technical, and billing contact identifiers to be associated
 * with the domain.  Use <code>getContacts</code> and <code>setContacts</code>
 * to get and set the element.  This attribute will only be allowed if the
 * Contact Mapping is supported.
 * </li>
 * <li>
 * One or two &lt;domain:status&gt; elements that contain status values to be
 * applied to or removed from the domain object.  Use <code>getStatuses</code>
 * and <code>setStatuses</code> to get and set the element.
 * </li>
 * <li>
 * For <code>change</code> only, A &lt;domain:registrant&gt; element that
 * contains the identifier for the human or organizational social information
 * (contact) object to be associated with the domain object as the object
 * registrant.  This object identifier MUST be known to the server before the
 * contact object can be associated with the domain object. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get and set
 * the element.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the maximum number of domain attribute
 * elements is subject to the number of values currently associated with the
 * domain object.  <code>EPPDomainAddRemove</code> will delegate the
 * validation of     the cardinality of the domain attributes elements to the
 * EPP Server.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainUpdateCmd
 */
public class EPPDomainAddRemove implements EPPCodecComponent {

	/** mode of <code>EPPDomainAddRemove</code> is not specified. */
	final static short MODE_NONE = 0;

	/** mode of <code>EPPDomainAddRemove</code> is to add attributes. */
	final static short MODE_ADD = 1;

	/** mode of <code>EPPDomainAddRemove</code> is to remove attributes. */
	final static short MODE_REMOVE = 2;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPDomainAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_ADD = "domain:add";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPDomainAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_REMOVE = "domain:rem";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPDomainAddRemove.MODE_CHANGE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_CHANGE = "domain:chg";

	/** mode of <code>EPPDomainAddRemove</code> is to change attributes. */
	final static short MODE_CHANGE = 3;

	/** XML tag name for the <code>servers</code> attribute. */
	private final static String ELM_SERVER = "domain:ns";

	/** XML tag name for host object reference */
	private final static String ELM_HOST_OBJ = "domain:hostObj";

	/** XML tag name for host attribute */
	private final static String ELM_HOST_ATTR = EPPHostAttr.ELM_NAME;

	/** XML tag name for the <code>contacts</code> attribute. */
	private final static String ELM_CONTACT = "domain:contact";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "domain:status";

	/** XML tag name for the <code>servers</code> attribute. */
	private final static String ELM_REGISTRANT = "domain:registrant";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPDomainAddRemove.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Mode of EPPDomainAddRemove.  Must be <code>MODE_ADD</code> or
	 * <code>MODE_REMOVE</code> to be valid.  This     attribute will be set
	 * by the parent container <code>EPPCodecComponent</code>.  For example,
	 * <code>EPPDomainUpdateCmd</code> will set the mode for its
	 * <code>EPPDomainAddRemove</code> instances.
	 */
	private short mode = MODE_NONE;

	/** Name Servers to add or remove. */
	private Vector servers = null;

	/** Contacts to add or remove. */
	private Vector contacts = null;

	/** Status to add or remove. */
	private Vector statuses = null;

	/** authorization information to change. */
	private EPPAuthInfo authInfo = null;

	/** registrant to change. */
	private java.lang.String registrant = null;

	/**
	 * Default constructor for <code>EPPDomainAddRemove</code>.  All of the
	 * attribute     default to <code>null</code> to indicate no modification.
	 */
	public EPPDomainAddRemove() {
		servers		   = null;
		contacts	   = null;
		statuses	   = null;
		registrant     = null;
		authInfo	   = null;
	}

	// End EPPDomainAddRemove()

	
	/**
	 * Constructor for <code>EPPDomainAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param someServers Vector of Name Server <code>String</code>'s.  Is
	 * 		  <code>null</code> or empty for no modifications.
	 * @param someContacts Vector of <code>EPPDomainContact</code> instances.
	 * 		  Is     <code>null</code> or empty for no modifications.  If the
	 * 		  Contact Mapping is not supported, this value should be
	 * 		  <code>null</code>.
	 * @param someStatuses Vector of status <code>String</code>'s.  One of the
	 * 		  <code>EPPDomainInfoResp.STATUS_</code> contants can be used for
	 * 		  each of the status values.    Is <code>null</code> or empty for
	 * 		  no modifications.
	 */
	public EPPDomainAddRemove(
							  Vector someServers, Vector someContacts,
							  Vector someStatuses) {
		servers		 = someServers;
		contacts     = someContacts;
		statuses     = someStatuses;
	}

	// EPPDomainAddRemove(Vector, Vector, Vector)

	/**
	 * Constructor for <code>EPPDomainAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aRegistrant <code>String</code>            registrant for the
	 * 		  change mode
	 * @param aAuthInfo <code>EPPAuthInfo</code>    authorization information
	 * 		  for the change mode
	 */
	public EPPDomainAddRemove(String aRegistrant, EPPAuthInfo aAuthInfo) {
		registrant = aRegistrant;
		setAuthInfo(aAuthInfo);
	}

	// End EPPDomainAddRemove(String, EPPAuthInfo)

	/**
	 * Gets the name servers. The name servers can either be
	 * <code>String</code> instances containing the fully qualified name of a
	 * known name server host object, or <code>EPPHostAttr</code> instances
	 * containing the fully qualified name of a host and optionally the host
	 * IP addresses.
	 *
	 * @return <code>Vector</code> of name server <code>String</code> instances
	 * 		   for host object references or <code>EPPHostAttr</code>
	 * 		   instances for host attribute values if exists;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getServers() {
		return servers;
	}

	// End EPPDomainAddRemove.getServers()

	/**
	 * Sets the name servers. The name servers can either be
	 * <code>String</code> instances containing the fully qualified name of a
	 * known name server host object, or <code>EPPHostAttr</code> instances
	 * containing the fully qualified name of a host and optionally the host
	 * IP addresses.
	 *
	 * @param aServers <code>Vector</code> of name server <code>String</code>
	 * 		  instances for host object references or <code>EPPHostAttr</code>
	 * 		  instances for host attribute values.
	 */
	public void setServers(Vector aServers) {
		servers = aServers;
	}

	// End EPPDomainAddRemove.setServers(Vector)

	/**
	 * Gets the contacts to add or remove.
	 *
	 * @return Vector of <code>EPPDomainContact</code> instances.
	 */
	public Vector getContacts() {
		return contacts;
	}

	// End EPPDomainAddRemove.getContacts()

	/**
	 * Sets the contacts to add or remove.
	 *
	 * @param aContacts DOCUMENT ME!
	 */
	public void setContacts(Vector aContacts) {
		contacts = aContacts;
	}

	// End EPPDomainAddRemove.setContacts(Vector)

	/**
	 * Gets the statuses to add or remove.  The
	 * <code>EPPDomainInfoResp.STATUS_</code>     constants can be used for
	 * the statuses.
	 *
	 * @return Vector of status <code>String</code> instances.
	 */
	public Vector getStatuses() {
		return statuses;
	}

	// End EPPDomainAddRemove.getStatuses()

	/**
	 * Sets the statuses to add or remove.  The
	 * <code>EPPDomainInfoResp.STATUS_</code>     constants can be used for
	 * the statuses.
	 *
	 * @param aStatuses Vector of status <code>String</code> instances.
	 */
	public void setStatuses(Vector aStatuses) {
		statuses = aStatuses;
	}

	// End EPPDomainAddRemove.setStatuses(Vector)

	/**
	 * Return if Domain Contacts is supported.
	 *
	 * @return <code>true</code> if contacts are supported; <code>false</code>
	 * 		   otherwise.
	 */
	public boolean contactsSupported() {
		return EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT);
	}

	// End EPPDomainAddRemove.contactsSupported()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainAddRemove</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDomainAddRemove</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainAddRemove</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root;

		// Change mode
		if (mode == MODE_CHANGE) {
			root =
				aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_CHANGE);

			if (registrant != null) {
				EPPUtil.encodeString(
									 aDocument, root, registrant,
									 EPPDomainMapFactory.NS, ELM_REGISTRANT);
			}

			if (authInfo != null) {
				EPPUtil.encodeComp(aDocument, root, authInfo);
			}

			return root;
		}

		// Add or Remove mode
		if (mode == MODE_ADD) {
			root = aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_ADD);
		}
		else if (mode == MODE_REMOVE) {
			root =
				aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_REMOVE);
		}
		else {
			throw new EPPEncodeException("Invalid EPPDomainAddRemove mode of "
										 + mode);
		}

		// Domain Name Servers
		if ((this.servers != null) && (this.servers.size() > 0)) {
			Element theServersElm =
				aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_SERVER);
			root.appendChild(theServersElm);

			Object theNS = this.servers.get(0);

			// Name Server Host objects?
			if (theNS instanceof String) {
				EPPUtil.encodeVector(
									 aDocument, theServersElm, this.servers,
									 EPPDomainMapFactory.NS, ELM_HOST_OBJ);
			}

			// Name Server Host attributes?
			else if (theNS instanceof EPPHostAttr) {
				EPPUtil.encodeCompVector(
										 aDocument, theServersElm, this.servers);
			}
			else {
				throw new EPPEncodeException("EPPDomainAddRemove.encode: Invalid NS server class "
											 + theNS.getClass().getName());
			}
		}

		// end if (this.servers != null) && (this.servers.size()) > 0)
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

	// End EPPDomainAddRemove.encode(Document)

	/**
	 * Decode the <code>EPPDomainAddRemove</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainAddRemove</code> from.
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
			throw new EPPDecodeException("Invalid EPPDomainAddRemove mode of "
										 + aElement.getLocalName());
		}

		// Change Mode
		if (mode == MODE_CHANGE) {
			// Registrant
			registrant =
				EPPUtil.decodeString(
									 aElement, EPPDomainMapFactory.NS,
									 ELM_REGISTRANT);

			// AuthInfo
			authInfo =
				(EPPAuthInfo) EPPUtil.decodeComp(
												 aElement,
												 EPPDomainMapFactory.NS,
												 EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO,
												 EPPAuthInfo.class);
		}
		else { // Add & Remove Mode

			// Servers
			Element theServersElm =
				EPPUtil.getElementByTagNameNS(aElement, EPPDomainMapFactory.NS, ELM_SERVER);

			if (theServersElm != null) {
				Element theServerElm =
					EPPUtil.getFirstElementChild(theServersElm);

				if (theServerElm != null) {
					if (theServerElm.getLocalName().equals(EPPUtil.getLocalName(ELM_HOST_OBJ))) {
						servers =
							EPPUtil.decodeVector(
												 theServersElm,
												 EPPDomainMapFactory.NS,
												 ELM_HOST_OBJ);
					}
					else if (theServerElm.getLocalName().equals(EPPUtil.getLocalName(ELM_HOST_ATTR))) {
						servers =
							EPPUtil.decodeCompVector(
													 theServersElm,
													 EPPDomainMapFactory.NS,
													 ELM_HOST_ATTR,
													 EPPHostAttr.class);
					}
					else {
						throw new EPPDecodeException("EPPDomainAddRemove.doDecode: Invalid host child element "
													 + theServersElm.getLocalName());
					}

					if (servers.size() == 0) {
						servers = null;
					}
				}

				// end if (theServerElm != null) 
			}

			// end if (theServersElm != null)
			// Contacts
			contacts =
				EPPUtil.decodeCompVector(
										 aElement, EPPDomainMapFactory.NS,
										 ELM_CONTACT, EPPDomainContact.class);

			if (contacts.size() == 0) {
				contacts = null;
			}

			// Statuses
			statuses =
				EPPUtil.decodeCompVector(
										 aElement, EPPDomainMapFactory.NS,
										 ELM_STATUS, EPPDomainStatus.class);
			if (statuses.size() == 0) {
				statuses = null;
			}
		}
	}

	// End EPPDomainAddRemove.decode(Element)

	/**
	 * implements a deep <code>EPPDomainAddRemove</code> compare.
	 *
	 * @param aObject <code>EPPDomainAddRemove</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainAddRemove)) {
			cat.error("EPPDomainAddRemove.equals(): "
					  + aObject.getClass().getName()
					  + " not EPPDomainAddRemove instance");

			return false;
		}

		EPPDomainAddRemove theComp = (EPPDomainAddRemove) aObject;

		// Mode
		if (mode != theComp.mode) {
			cat.error("EPPDomainAddRemove.equals(): mode not equal");

			return false;
		}

		// Servers
		if (!EPPUtil.equalVectors(servers, theComp.servers)) {
			cat.error("EPPDomainAddRemove.equals(): servers not equal");

			return false;
		}

		// Contacts
		if (contactsSupported()) {
			if (!EPPUtil.equalVectors(contacts, theComp.contacts)) {
				cat.error("EPPDomainAddRemove.equals(): contacts not equal");

				return false;
			}
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
			cat.error("EPPDomainAddRemove.equals(): statuses not equal");

			return false;
		}

		// Registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
				)) {
			cat.error("EPPDomainAddRemove.equals(): registrant not equal");

			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			cat.error("EPPDomainAddRemove.equals(): authInfo not equal");

			return false;
		}

		return true;
	}

	// End EPPDomainAddRemove.equals(Object)

	/**
	 * Clone <code>EPPDomainAddRemove</code>.
	 *
	 * @return clone of <code>EPPDomainAddRemove</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainAddRemove clone = null;

		clone = (EPPDomainAddRemove) super.clone();

		if (servers != null) {
			clone.servers = (Vector) servers.clone();
		}

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();

			for (int i = 0; i < contacts.size(); i++)
				clone.contacts.setElementAt(
											((EPPDomainContact) contacts
											 .elementAt(i)).clone(), i);
		}

		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();
		}

		if (registrant != null) {
			clone.registrant = registrant;
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPDomainAddRemove.clone()

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

	// End EPPDomainAddRemove.toString()

	/**
	 * Get authorization information for the change mode
	 *
	 * @return com.verisign.epp.codec.domain.EPPDomainAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDomainAddRemove.getAuthInfo()

	/**
	 * Get registrant for the change mode
	 *
	 * @return java.lang.String
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPDomainAddRemove.getRegistrant()

	/**
	 * Set authorization information for the change mode
	 *
	 * @param newAuthInfo com.verisign.epp.codec.domain.EPPDomainAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
		}
	}

	// End EPPDomainAddRemove.setAuthInfo(EPPAuthInfo)

	/**
	 * Set registrant for the change mode
	 *
	 * @param newRegistrant java.lang.String
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPDomainAddRemove.setRegistrant(String)

	/**
	 * Is the <code>EPPDomainAddRemove</code> empty?  
	 * 
	 * @return <code>true</code> if all of the attributes are null; <code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return (this.servers == null) &&
			   (this.contacts == null) &&
			   (this.statuses == null) &&
			   (this.registrant == null) &&
			   (this.authInfo == null);
	}
	
	
	/**
	 * Gets the mode of <code>EPPDomainAddRemove</code>.  There are two valid
	 * modes <code>EPPDomainAddRemove.MODE_ADD</code> and
	 * <code>EPPDomainAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPDomainAddRemove.MODE_NONE</code>.
	 *
	 * @return One of the <code>EPPDomainAddRemove_MODE</code> constants.
	 */
	short getMode() {
		return mode;
	}

	// End EPPDomainAddRemove.getMode()

	/**
	 * Sets the mode of <code>EPPDomainAddRemove</code>.  There are two valid
	 * modes <code>EPPDomainAddRemove.MODE_ADD</code> and
	 * <code>EPPDomainAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPDomainAddRemove.MODE_NONE</code>
	 *
	 * @param aMode <code>EPPDomainAddRemove.MODE_ADD</code> or
	 * 		  <code>EPPDomainAddRemove.MODE_REMOVE</code>.
	 */
	void setMode(short aMode) {
		mode = aMode;
	}

	// End EPPDomainAddRemove.setMode(short)
	

}
