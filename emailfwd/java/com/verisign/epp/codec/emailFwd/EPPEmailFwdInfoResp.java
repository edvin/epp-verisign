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

import java.util.Date;

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
 * Represents an EPP EmailFwd &lt;emailFwd:infData&gt; response to an
 * <code>EPPEmailFwdInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;emailFwd:infData&gt; element that identifies the emailFwd
 * namespace and the location of the emailFwd schema. The
 * &lt;emailFwd:infData&gt; element contains the following child elements:
 * <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified name of
 * the emailFwd.  Use <code>getName</code> and <code>setName</code> to get and
 * set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:fwdTo&gt; element that contains the emailFwd forwardTo
 * addresses.. Use <code>getForwardTo</code> and <code>setForwardTo</code> to
 * get and     set the forwardTo addresses.
 * </li>
 * <li>
 * A &lt;emailFwd:roid&gt; element that contains the Repository Object
 * IDentifier assigned to the emailFwd object when the object was created. Use
 * <code>getRoid</code> and <code>setRoid</code> to get and set the element.
 * </li>
 * <li>
 * One or more &lt;emailFwd:status&gt; elements that contain the current status
 * descriptors associated with the emailFwd. See the
 * <code>EPPEmailFwdStatus</code> description for a list of valid status
 * values.   Use <code>getStatus</code> and     <code>setStatus</code> to get
 * and set the elements.
 * </li>
 * <li>
 * If supported by the server, one &lt;emailFwd:registrant&gt; element and one
 * or more &lt;emailFwd:contact&gt; elements that contain identifiers for the
 * human or organizational social information objects associated with the
 * emailFwd object. Use <code>getContacts</code> and <code>setContacts</code>
 * to get and set the elements.  Contacts should only be     specified if the
 * Contact Mapping is supported.
 * </li>
 * <li>
 * A &lt;emailFwd:clID&gt; element that contains the identifier of the
 * sponsoring client.  Use <code>getClientId</code> and
 * <code>setClientId</code> to get and     set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:crID&gt; element that contains the identifier of the client
 * that created the emailFwd name.  Use <code>getCreatedBy</code> and
 * <code>setCreatedBy</code>     to get and set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:crDate&gt; element that contains the date and time of
 * emailFwd creation.  Use <code>getCreatedDate</code> and
 * <code>setCreatedDate</code> to     get and set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:exDate&gt; element that contains the date and time
 * identifying the end of the emailFwd's registration period.  Use
 * <code>getExpirationDate</code>     and <code>setExpirationDate</code> to
 * get and set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:upID&gt; element that contains the identifier of the client
 * that last updated the emailFwd name.  This element MUST NOT be present if
 * the emailFwd has never been modified.  Use <code>getLastUpdatedBy</code>
 * and     <code>setLastUpdatedBy</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:upDate&gt; element that contains the date and time of the
 * most recent emailFwd modification.  This element MUST NOT be present if the
 * emailFwd has never been modified.  Use <code>getLastUpdatedDate</code> and
 * <code>setLastUpdatedDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;emailFwd:trDate&gt; elements that contains the date and time of the
 * most recent successful transfer.  This element MUST NOT be provided if the
 * emailFwd has never been transferred.  Use <code>getLastTransferDate</code>
 * and     <code>setLastTransferDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:authInfo&gt; element that contains authorization
 * information associated with the emailFwd object.  This element MUST NOT be
 * provided if the querying client is not the current sponsoring client. Use
 * <code>getAuthInfo</code> and <code>setAuthInfo</code> to get and set the
 * elements.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoCmd
 */
public class EPPEmailFwdInfoResp extends EPPResponse {
	/** XML Element Name of <code>EPPEmailFwdInfoResp</code> root element. */
	final static String ELM_NAME = "emailFwd:infData";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** XML tag name for the <code>forwardTo</code> attribute. */
	private final static String ELM_EMAILFWD_TO = "emailFwd:fwdTo";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "emailFwd:status";

	/** XML tag name for the <code>contacts</code> attribute. */
	private final static String ELM_CONTACT = "emailFwd:contact";

	/** XML tag name for the <code>clientId</code> attribute. */
	private final static String ELM_CLID = "emailFwd:clID";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_CRDATE = "emailFwd:crDate";

	/** XML tag name for the <code>createdBy</code> attribute. */
	private final static String ELM_CRID = "emailFwd:crID";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXDATE = "emailFwd:exDate";

	/*
	 *    XML tag name for the <code>registrant</code> attribute.
	 */

	/** DOCUMENT ME! */
	private final static String ELM_REGISTRANT = "emailFwd:registrant";

	/** XML tag name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "emailFwd:roid";

	/** XML tag name for the <code>lastTransferDate</code> attribute. */
	private final static String ELM_TRDATE = "emailFwd:trDate";

	/** XML tag name for the <code>lastUpdatedDate</code> attribute. */
	private final static String ELM_UPDATE = "emailFwd:upDate";

	/** XML tag name for the <code>lastUpdatedBy</code> attribute. */
	private final static String ELM_UPID = "emailFwd:upID";

	/** fully qualified name of the emailFwd */
	private String name = null;

	/** fully qualified forwardTo address of the emailFwd */
	private String forwardTo = null;

	/** identifier of sponsoring client */
	private String clientId = null;

	/**
	 * <code>Vector</code> of <code>EPPEmailFwdContact</code> instances
	 * associated with emailFwd
	 */
	private Vector contacts = null;

	/** identifier of the client that created the emailFwd name */
	private String createdBy = null;

	/** date and time of emailFwd creation */
	private Date createdDate = null;

	/**
	 * date and time identifying the end of the emailFwd's registration period
	 */
	private Date expirationDate = null;

	/** identifier of the client that last updated the emailFwd name */
	private String lastUpdatedBy = null;

	/** date and time of the most recent emailFwd modification */
	private Date lastUpdatedDate = null;

	/** date and time of the most recent successful transfer */
	private Date lastTransferDate = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/** registrant. */
	private String registrant = null;

	/** one or more current status descriptors. */
	private java.util.Vector statuses = new Vector();

	/** roid. */
	private java.lang.String roid = null;

	/**
	 * <code>EPPEmailFwdInfoResp</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * name - <code>setName</code>
	 * </li>
	 * <li>
	 * roid - <code>setRoid</code>
	 * </li>
	 * <li>
	 * client id - <code>setClientId</code>
	 * </li>
	 * </ul>
	 */
	public EPPEmailFwdInfoResp() {
		// Default values set in attribute definitions.
	}

	// End EPPEmailFwdInfoResp.EPPEmailFwdInfoResp()

	/**
	 * <code>EPPEmailFwdInfoResp</code> constuctor that takes the required attribute 
	 * values as paramters.
     * The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aRoid roid
	 * @param aName EmailFwd name
	 * @param aClientId Owning Client Id
	 */
	public EPPEmailFwdInfoResp(
							   EPPTransId aTransId, String aRoid, String aName,
							   String aClientId) {
		super(aTransId);

		name		    = aName;
		roid		    = aRoid;
		clientId	    = aClientId;
	}
	
	
	/**
	 * <code>EPPEmailFwdInfoResp</code> constuctor that takes the required and 
	 * the most common optional attribute values as paramters.
     * The setter methods of the additional optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aRoid roid
	 * @param aName EmailFwd name
	 * @param aForwardTo EmailFwdTo address
	 * @param aClientId Owning Client Id
	 * @param someStatuses Current status descriptors associated with the
	 * 		  emailFwd.
	 * @param aCreatedBy Client Id of Registrar that created the emailFwd
	 * @param aCreatedDate Date the emailFwd was created
	 * @param aAuthInfo Expirate date of the emailFwd
	 */
	public EPPEmailFwdInfoResp(
							   EPPTransId aTransId, String aRoid, String aName,
							   String aForwardTo, String aClientId,
							   Vector someStatuses, String aCreatedBy,
							   Date aCreatedDate, EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		    = aName;
		forwardTo	    = aForwardTo;
		roid		    = aRoid;
		clientId	    = aClientId;
		statuses	    = someStatuses;
		createdBy	    = aCreatedBy;
		createdDate     = aCreatedDate;
		authInfo	    = aAuthInfo;
		authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
	}

	// End EPPEmailFwdInfoResp.EPPEmailFwdInfoResp(EPPTransId, String, String, String, Vector, String, Date, EPPAuthInfo)

	/**
	 * Get the current associated statuses
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getStatuses() {
		return statuses;
	}

	// End EPPEmailFwdInfoResp.getStatuses()

	/**
	 * Set associated statuses.
	 *
	 * @param newStatuses java.util.Vector
	 */
	public void setStatuses(Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPEmailFwdInfoResp.setStatuses(Vector)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPEmailFwdInfoResp</code>.
	 *
	 * @return <code>EPPEmailFwdInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPEmailFwdInfoResp.getType()

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPEmailFwdInfoResp</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdInfoResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPEmailFwdInfoResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdInfoResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdInfoResp theInfoData = (EPPEmailFwdInfoResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theInfoData.name == null)
									   : name.equals(theInfoData.name)
				)) {
			return false;
		}

		// forwardTo
		if (
			!(
					(forwardTo == null) ? (theInfoData.forwardTo == null)
											: forwardTo.equals(theInfoData.forwardTo)
				)) {
			return false;
		}

		// roid
		if (
			!(
					(roid == null) ? (theInfoData.roid == null)
									   : roid.equals(theInfoData.roid)
				)) {
			return false;
		}

		// Client Id
		if (
			!(
					(clientId == null) ? (theInfoData.clientId == null)
										   : clientId.equals(theInfoData.clientId)
				)) {
			return false;
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theInfoData.statuses)) {
			return false;
		}

		// registrant
		if (
			!(
					(registrant == null) ? (theInfoData.registrant == null)
											 : registrant.equals(theInfoData.registrant)
				)) {
			return false;
		}

		// Contacts
		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			if (!EPPUtil.equalVectors(contacts, theInfoData.contacts)) {
				return false;
			}
		}

		// Created By
		if (
			!(
					(createdBy == null) ? (theInfoData.createdBy == null)
											: createdBy.equals(theInfoData.createdBy)
				)) {
			return false;
		}

		// Created Date
		if (
			!(
					(createdDate == null) ? (theInfoData.createdDate == null)
											  : createdDate.equals(theInfoData.createdDate)
				)) {
			return false;
		}

		// Expiration Date
		if (
			!(
					(expirationDate == null)
						? (theInfoData.expirationDate == null)
							: expirationDate.equals(theInfoData.expirationDate)
				)) {
			return false;
		}

		// Last Updated By
		if (
			!(
					(lastUpdatedBy == null) ? (
													theInfoData.lastUpdatedBy == null
												)
												: lastUpdatedBy.equals(theInfoData.lastUpdatedBy)
				)) {
			return false;
		}

		// Last Updated Date
		if (
			!(
					(lastUpdatedDate == null)
						? (theInfoData.lastUpdatedDate == null)
							: lastUpdatedDate.equals(theInfoData.lastUpdatedDate)
				)) {
			return false;
		}

		// Last Transfer Date
		if (
			!(
					(lastTransferDate == null)
						? (theInfoData.lastTransferDate == null)
							: lastTransferDate.equals(theInfoData.lastTransferDate)
				)) {
			return false;
		}

		// Authorization Info
		if (
			!(
					(authInfo == null) ? (theInfoData.authInfo == null)
										   : authInfo.equals(theInfoData.authInfo)
				)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdInfoResp.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdInfoResp</code>.
	 *
	 * @return clone of <code>EPPEmailFwdInfoResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdInfoResp clone = (EPPEmailFwdInfoResp) super.clone();

		clone.statuses = (Vector) statuses.clone();

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPEmailFwdInfoResp.clone()

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

	// End EPPEmailFwdInfoResp.toString()

	/**
	 * Gets the emailFwd name
	 *
	 * @return EmailFwd Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdInfoResp.getName()

	/**
	 * Sets the emailFwd name.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdInfoResp.setName(String)

	/**
	 * Sets the emailFwd name.
	 *
	 * @param aForwardTo EmailFwd Name
	 */
	public void setForwardTo(String aForwardTo) {
		forwardTo = aForwardTo;
	}

	// End EPPEmailFwdInfoResp.setForwardTo(String)

	/**
	 * Gets the emailFwd To
	 *
	 * @return EmailFwd forwardTo if defined; <code>null</code> otherwise.
	 */
	public String getForwardTo() {
		return forwardTo;
	}

	// End EPPEmailFwdInfoResp.getForwardTo()

	/**
	 * Gets the emailFwd owning Client Id.
	 *
	 * @return Client Id
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPEmailFwdInfoResp.getClientId()

	/**
	 * Sets the emailFwd owning Client Id.
	 *
	 * @param aClientId Client Id
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPEmailFwdInfoResp.setClientId(String)

	/**
	 * Gets the Contacts
	 *
	 * @return Vector of <code>EPPEmailFwdContact</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getContacts() {
		return contacts;
	}

	// End EPPEmailFwdInfoResp.getContacts()

	/**
	 * Sets the Contacts.  This method should only be called if the Contact
	 * Namespace     supported.
	 *
	 * @param someContacts - Vector of <code>EPPEmailFwdContact</code>
	 * 		  instances
	 */
	public void setContacts(Vector someContacts) {
		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			contacts = someContacts;
		}
	}

	// End EPPEmailFwdInfoResp.setContacts(Vector)

	/**
	 * Gets Client Id that created the emailFwd.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	// End EPPEmailFwdInfoResp.getCreatedBy()

	/**
	 * Sets Client Id that created the emailFwd.
	 *
	 * @param aCreatedBy Client Id that created the emailFwd.
	 */
	public void setCreatedBy(String aCreatedBy) {
		createdBy = aCreatedBy;
	}

	// End EPPEmailFwdInfoResp.setCreatedBy(Date)

	/**
	 * Gets the date and time the emailFwd was created.
	 *
	 * @return Date and time the emailFwd was created if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	// End EPPEmailFwdInfoResp.getCreatedDate()

	/**
	 * Sets the date and time the emailFwd was created.
	 *
	 * @param aDate Date and time the emailFwd was created.
	 */
	public void setCreatedDate(Date aDate) {
		createdDate = aDate;
	}

	// End EPPEmailFwdInfoResp.setCreatedDate(Date)

	/**
	 * Gets the expiration date and time of the emailFwd.
	 *
	 * @return Expiration date and time of the emailFwd if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPEmailFwdInfoResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the emailFwd.
	 *
	 * @param aExpirationDate Expiration date and time of the emailFwd.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPEmailFwdInfoResp.setExpirationDate(Date)

	/**
	 * Gets the Client Id that last updated the emailFwd.  This will be null if
	 * the emailFwd has not been updated since creation.
	 *
	 * @return Client Id that last updated the emailFwd has been updated;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	// End EPPEmailFwdInfoResp.getLastUpdatedBy()

	/**
	 * Sets the Client Id that last updated the emailFwd.
	 *
	 * @param aLastUpdatedBy Client Id String that last updated the emailFwd.
	 */
	public void setLastUpdatedBy(String aLastUpdatedBy) {
		lastUpdatedBy = aLastUpdatedBy;
	}

	// End EPPEmailFwdInfoResp.setLastUpdatedBy(String)

	/**
	 * Gets the date and time of the last emailFwd update.  This will be
	 * <code>null</code>     if the emailFwd has not been updated since
	 * creation.
	 *
	 * @return date and time of the last emailFwd update if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	// End EPPEmailFwdInfoResp.getLastUpdatedDate()

	/**
	 * Sets the last date and time the emailFwd was updated.
	 *
	 * @param aLastUpdatedDate Date and time of the last emailFwd update.
	 */
	public void setLastUpdatedDate(Date aLastUpdatedDate) {
		lastUpdatedDate = aLastUpdatedDate;
	}

	// End EPPEmailFwdInfoResp.setLastUpdatedDate(Date)

	/**
	 * Gets the date and time of the last successful emailFwd transfer.  This
	 * will be <code>null</code>     if the emailFwd has not been successfully
	 * transferred since creation.
	 *
	 * @return date and time of the last successful transfer if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	// End EPPEmailFwdInfoResp.getLastTransferDate()

	/**
	 * Sets the last date and time the emailFwd was successfully transferred.
	 *
	 * @param aLastTransferDate Date and time of the last succesful transfer
	 */
	public void setLastTransferDate(Date aLastTransferDate) {
		lastTransferDate = aLastTransferDate;
	}

	// End EPPEmailFwdInfoResp.setLastTransferDate(Date)

	/**
	 * Get authorization information
	 *
	 * @return Authorization information if defined; <code>null</code>
	 * 		   otherwise;
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPEmailFwdInfoResp.getAuthInfo()

	/**
	 * Get registrant
	 *
	 * @return String
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPEmailFwdInfoResp.getRegistrant()

	/**
	 * Set registrants.
	 *
	 * @param newRegistrant String
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPEmailFwdInfoResp.setRegistrant(String)

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
		}
	}

	// End EPPEmailFwdInfoResp.setAuthInfo(EPPAuthInfo)

	/**
	 * Get roid.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRoid() {
		return roid;
	}

	// End EPPEmailFwdInfoResp.getRoid()

	/**
	 * Set roid.
	 *
	 * @param newRoid java.lang.String
	 */
	public void setRoid(String newRoid) {
		roid = newRoid;
	}

	// End EPPEmailFwdInfoResp.setRoid(String)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdInfoResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the EPPEmailFwdInfoResp
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPEmailFwdInfoResp
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPEmailFwdInfoResp.encode: "
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

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPEmailFwdMapFactory.NS,
							 ELM_ROID);

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		// registrant
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

		// forwardTo
		EPPUtil.encodeString(
							 aDocument, root, forwardTo,
							 EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_TO);

		// Client Id
		EPPUtil.encodeString(
							 aDocument, root, clientId, EPPEmailFwdMapFactory.NS,
							 ELM_CLID);

		// Created By
		EPPUtil.encodeString(
							 aDocument, root, createdBy,
							 EPPEmailFwdMapFactory.NS, ELM_CRID);

		// Created Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, createdDate,
								  EPPEmailFwdMapFactory.NS, ELM_CRDATE);

		// Last Updated By
		if (lastUpdatedBy != null) {
			EPPUtil.encodeString(
								 aDocument, root, lastUpdatedBy,
								 EPPEmailFwdMapFactory.NS, ELM_UPID);
		}

		// Last Updated Date
		if (lastUpdatedDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastUpdatedDate,
									  EPPEmailFwdMapFactory.NS, ELM_UPDATE);
		}

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPEmailFwdMapFactory.NS, ELM_EXDATE);
		}

		// Last Transfer Date
		if (lastTransferDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastTransferDate,
									  EPPEmailFwdMapFactory.NS, ELM_TRDATE);
		}

		// Authorization Info
		if (authInfo != null) {
			EPPUtil.encodeComp(aDocument, root, authInfo);
		}

		return root;
	}

	// End EPPEmailFwdInfoResp.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdInfoResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdInfoResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPEmailFwdMapFactory.NS, ELM_ROID);

		// Statuses
		statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPEmailFwdMapFactory.NS,
									 ELM_STATUS, EPPEmailFwdStatus.class);

		// registant
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

		//ForwardTo
		forwardTo =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_TO);

		// Client Id
		clientId =
			EPPUtil.decodeString(aElement, EPPEmailFwdMapFactory.NS, ELM_CLID);

		// Created By
		createdBy =
			EPPUtil.decodeString(aElement, EPPEmailFwdMapFactory.NS, ELM_CRID);

		// Created Date
		createdDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPEmailFwdMapFactory.NS,
									  ELM_CRDATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPEmailFwdMapFactory.NS,
									  ELM_EXDATE);

		// Last Updated By
		lastUpdatedBy =
			EPPUtil.decodeString(aElement, EPPEmailFwdMapFactory.NS, ELM_UPID);

		// Last Updated Date
		lastUpdatedDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPEmailFwdMapFactory.NS,
									  ELM_UPDATE);

		// Last Transfer Date
		lastTransferDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPEmailFwdMapFactory.NS,
									  ELM_TRDATE);

		// Authorization Info
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPEmailFwdMapFactory.NS,
											 EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPEmailFwdInfoResp.doDecode(Element)

	/**
	 * Validate the state of the <code>EPPEmailFwdInfoResp</code> instance.  A
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
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}

		if (clientId == null) {
			throw new EPPCodecException("clientId required attribute is not set");
		}

	}

	// End EPPEmailFwdInfoResp.isValid()
}
