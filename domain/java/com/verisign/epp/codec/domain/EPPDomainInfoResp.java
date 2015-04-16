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
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP Domain &lt;domain:infData&gt; response to an
 * <code>EPPDomainInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;domain:infData&gt; element that identifies the domain namespace
 * and the location of the domain schema. The &lt;domain:infData&gt; element
 * contains the following child elements: <br><br>
 *
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified name of the
 * domain.  Use <code>getName</code> and <code>setName</code> to get and set
 * the element.
 * </li>
 * <li>
 * A &lt;domain:roid&gt; element that contains the Repository Object IDentifier
 * assigned to the domain object when the object was created.  Use
 * <code>getRoid</code> and <code>setRoid</code> to get and set the element.
 * </li>
 * <li>
 * One or more &lt;domain:status&gt; elements that contain the current status
 * descriptors associated with the domain. See the
 * <code>EPPDomainStatus</code> description for a list of valid status values.
 * Use <code>getStatus</code> and     <code>setStatus</code> to get and set
 * the elements.
 * </li>
 * <li>
 * If supported by the server, one &lt;domain:registrant&gt; element and one or
 * more &lt;domain:contact&gt; elements that contain identifiers for the human
 * or organizational social information objects associated with the domain
 * object. Use <code>getContacts</code> and     <code>setContacts</code> to
 * get and set the elements.  Contacts should only be     specified if the
 * Contact Mapping is supported.
 * </li>
 * <li>
 * Zero or more &lt;domain:ns&gt; elements that contain the fully qualified
 * names of the name server objects associated with the domain object. Use
 * <code>getNs</code> and <code>setNs</code> to get and set the elements.
 * </li>
 * <li>
 * Zero or more &lt;domain:host&gt; elements that contain the fully qualified
 * names of the host objects created under this superordinate domain object.
 * Use <code>getHost</code> and <code>setHost</code> to get and set the
 * elements.
 * </li>
 * <li>
 * A &lt;domain:clID&gt; element that contains the identifier of the sponsoring
 * client.  Use <code>getClientId</code> and <code>setClientId</code> to get
 * and     set the element.
 * </li>
 * <li>
 * A &lt;domain:crID&gt; element that contains the identifier of the client
 * that created the domain name.  Use <code>getCreatedBy</code> and
 * <code>setCreatedBy</code>     to get and set the element.
 * </li>
 * <li>
 * A &lt;domain:crDate&gt; element that contains the date and time of domain
 * creation.  Use <code>getCreatedDate</code> and <code>setCreatedDate</code>
 * to     get and set the element.
 * </li>
 * <li>
 * A &lt;domain:exDate&gt; element that contains the date and time identifying
 * the end of the domain's registration period.  Use
 * <code>getExpirationDate</code>     and <code>setExpirationDate</code> to
 * get and set the element.
 * </li>
 * <li>
 * A &lt;domain:upID&gt; element that contains the identifier of the client
 * that last updated the domain name.  This element MUST NOT be present if the
 * domain has never been modified.  Use <code>getLastUpdatedBy</code> and
 * <code>setLastUpdatedBy</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;domain:upDate&gt; element that contains the date and time of the most
 * recent domain modification.  This element MUST NOT be present if the domain
 * has never been modified.  Use <code>getLastUpdatedDate</code> and
 * <code>setLastUpdatedDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;domain:trDate&gt; elements that contains the date and time of the most
 * recent successful transfer.  This element MUST NOT be provided if the
 * domain has never been transferred.  Use <code>getLastTransferDate</code>
 * and     <code>setLastTransferDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:authInfo&gt; element that contains authorization
 * information associated with the domain object.  This element MUST NOT be
 * provided if the querying client is not the current sponsoring client. Use
 * <code>getAuthInfo</code> and <code>setAuthInfo</code> to get and set the
 * elements.
 * </li>
 * </ul>
 *
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainInfoCmd
 */
public class EPPDomainInfoResp extends EPPResponse {
	/** XML Element Name of <code>EPPDomainInfoResp</code> root element. */
	final static String ELM_NAME = "domain:infData";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "domain:status";

	/** XML tag name for the <code>contacts</code> attribute. */
	private final static String ELM_CONTACT = "domain:contact";

	/** XML tag name for the <code>clientId</code> attribute. */
	private final static String ELM_CLID = "domain:clID";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_CRDATE = "domain:crDate";

	/** XML tag name for the <code>createdBy</code> attribute. */
	private final static String ELM_CRID = "domain:crID";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXDATE = "domain:exDate";

	/** XML tag name for the <code>servers</code> attribute. */
	private final static String ELM_HOST = "domain:host";

	/** XML tag name for the <code>childServers</code> attribute. */
	private final static String ELM_NS = "domain:ns";

	/** XML tag name for host object reference */
	private final static String ELM_HOST_OBJ = "domain:hostObj";

	/** XML tag name for host attribute */
	private final static String ELM_HOST_ATTR = EPPHostAttr.ELM_NAME;

	/** XML tag name for the <code>registrant</code> attribute. */
	private final static String ELM_REGISTRANT = "domain:registrant";

	/** XML tag name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "domain:roid";

	/** XML tag name for the <code>lastTransferDate</code> attribute. */
	private final static String ELM_TRDATE = "domain:trDate";

	/** XML tag name for the <code>lastUpdatedDate</code> attribute. */
	private final static String ELM_UPDATE = "domain:upDate";

	/** XML tag name for the <code>lastUpdatedBy</code> attribute. */
	private final static String ELM_UPID = "domain:upID";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPDomainInfoResp.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** fully qualified name of the domain */
	private String name = null;

	/** identifier of sponsoring client */
	private String clientId = null;

	/**
	 * <code>Vector</code> of <code>EPPDomainContact</code> instances
	 * associated with domain
	 */
	private Vector<EPPDomainContact> contacts = null;

	/** identifier of the client that created the domain name */
	private String createdBy = null;

	/** date and time of domain creation */
	private Date createdDate = null;

	/** date and time identifying the end of the domain's registration period */
	private Date expirationDate = null;

	/** identifier of the client that last updated the domain name */
	private String lastUpdatedBy = null;

	/** date and time of the most recent domain modification */
	private Date lastUpdatedDate = null;

	/** date and time of the most recent successful transfer */
	private Date lastTransferDate = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/** registrant. */
	private String registrant = null;

	/** names of host objects. */
	private java.util.Vector hosts = new Vector();

	/** names of name server objects. */
	private java.util.Vector<String> nses = new Vector();

	/** one or more current status descriptors. */
	private java.util.Vector statuses = new Vector();

	/** roid. */
	private java.lang.String roid = null;

	/**
	 * <code>EPPDomainInfoResp</code> default constructor.  Must call required
	 * setter methods before     invoking <code>encode</code>, which
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
	 * <li>
	 * statuses - <code>setStatuses</code>
	 * </li>
	 * <li>
	 * created by - <code>setCreatedBy</code>
	 * </li>
	 * <li>
	 * created date - <code>setCreatedDate</code>
	 * </li>
	 * <li>
	 * transaction id - <code>setTransId</code>
	 * </li>
	 * </ul>
	 */
	public EPPDomainInfoResp() {
		// Default values set in attribute definitions.
	}

	// End EPPDomainInfoResp.EPPDomainInfoResp()

	/**
	 * <code>EPPDomainInfoResp</code> constuctor that takes the required
	 * attribute values as parameters.     The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aRoid roid
	 * @param aName Domain name
	 * @param aClientId Owning Client Id
	 * @param someStatuses Current status descriptors associated with the
	 * 		  domain.
	 * @param aCreatedBy Client Id of Registrar that created the domain
	 * @param aCreatedDate Date the domain was created
	 * @param aAuthInfo Expirate date of the domain
	 */
	public EPPDomainInfoResp(
							 EPPTransId aTransId, String aRoid, String aName,
							 String aClientId, Vector someStatuses,
							 String aCreatedBy, Date aCreatedDate,
							 EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		    = aName;
		roid		    = aRoid;
		clientId	    = aClientId;
		statuses	    = someStatuses;
		createdBy	    = aCreatedBy;
		createdDate     = aCreatedDate;
		authInfo	    = aAuthInfo;
		authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
	}

	// End EPPDomainInfoResp.EPPDomainInfoResp(EPPTransId, String, String, String, Vector, String, Date, EPPAuthInfo)

	/**
	 * Get host names
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getHosts() {
		return hosts;
	}

	// End EPPDomainInfoResp.getHosts()

	/**
	 * Gets the name servers. The name servers can either be
	 * <code>String</code> instances containing the fully qualified name of a
	 * known name server host object, or <code>EPPHostAttr</code> instances
	 * containing the fully qualified name of a host and optionally the host
	 * IP addresses.
	 *
	 * @return <code>Vector</code> of name server <code>String</code> instances
	 * 		   for host object references or <code>EPPHostAttr</code>
	 * 		   instances for host attribute values if exists.
	 */
	public java.util.Vector<String> getNses() {
		return nses;
	}

	// End EPPDomainInfoResp.getNses()

	/**
	 * Get the current associated statuses
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector<EPPDomainStatus> getStatuses() {
		return statuses;
	}

	// End EPPDomainInfoResp.getStatuses()

	/**
	 * Set host names
	 *
	 * @param newHosts java.util.Vector
	 */
	public void setHosts(Vector newHosts) {
		hosts = newHosts;
	}

	// End EPPDomainInfoResp.setHosts(Vector)

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
	public void setNses(Vector<String> aServers) {
		nses = aServers;
	}

	// End EPPDomainInfoResp.setNses(Vector)

	/**
	 * Set associated statuses.
	 *
	 * @param newStatuses java.util.Vector
	 */
	public void setStatuses(Vector<EPPDomainStatus> newStatuses) {
		statuses = newStatuses;
	}

	// End EPPDomainInfoResp.setStatuses(Vector)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPDomainInfoResp</code>.
	 *
	 * @return <code>EPPDomainInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDomainInfoResp.getType()

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPDomainInfoResp</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainInfoResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDomainInfoResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainInfoResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainInfoResp theInfoData = (EPPDomainInfoResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theInfoData.name == null)
									   : name.equals(theInfoData.name)
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
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			if (!EPPUtil.equalVectors(contacts, theInfoData.contacts)) {
				return false;
			}
		}

		// name servers
		if (!EPPUtil.equalVectors(nses, theInfoData.nses)) {
			return false;
		}

		// hosts
		if (!EPPUtil.equalVectors(hosts, theInfoData.hosts)) {
			return false;
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

	// End EPPDomainInfoResp.equals(Object)

	/**
	 * Clone <code>EPPDomainInfoResp</code>.
	 *
	 * @return clone of <code>EPPDomainInfoResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainInfoResp clone = (EPPDomainInfoResp) super.clone();

		clone.statuses = (Vector) statuses.clone();

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();
		}

		if (nses != null) {
			clone.nses = (Vector) nses.clone();
		}

		if (hosts != null) {
			clone.hosts = (Vector) hosts.clone();
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPDomainInfoResp.clone()

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

	// End EPPDomainInfoResp.toString()

	/**
	 * Gets the domain name
	 *
	 * @return Domain Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainInfoResp.getName()

	/**
	 * Sets the domain name.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainInfoResp.setName(String)

	/**
	 * Gets the domain owning Client Id.
	 *
	 * @return Client Id
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPDomainInfoResp.getClientId()

	/**
	 * Sets the domain owning Client Id.
	 *
	 * @param aClientId Client Id
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPDomainInfoResp.setClientId(String)

	/**
	 * Gets the Contacts
	 *
	 * @return Vector of <code>EPPDomainContact</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector<EPPDomainContact> getContacts() {
		return contacts;
	}

	public EPPDomainContact getContactByType(String type) {
		if (contacts != null)
			for (EPPDomainContact contact : contacts)
				if (type.equals(contact.getType()))
					return contact;
		return null;
	}

	public EPPDomainContact getAdminContact() {
		return getContactByType("admin");
	}

	/**
	 * Sets the Contacts.  This method should only be called if the Contact
	 * Namespace     supported.
	 *
	 * @param someContacts - Vector of <code>EPPDomainContact</code> instances
	 */
	public void setContacts(Vector<EPPDomainContact> someContacts) {
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			contacts = someContacts;
		}
	}

	// End EPPDomainInfoResp.setContacts(Vector)

	/**
	 * Gets Client Id that created the domain.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	// End EPPDomainInfoResp.getCreatedBy()

	/**
	 * Sets Client Id that created the domain.
	 *
	 * @param aCreatedBy Client Id that created the domain.
	 */
	public void setCreatedBy(String aCreatedBy) {
		createdBy = aCreatedBy;
	}

	// End EPPDomainInfoResp.setCreatedBy(Date)

	/**
	 * Gets the date and time the domain was created.
	 *
	 * @return Date and time the domain was created if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	// End EPPDomainInfoResp.getCreatedDate()

	/**
	 * Sets the date and time the domain was created.
	 *
	 * @param aDate Date and time the domain was created.
	 */
	public void setCreatedDate(Date aDate) {
		createdDate = aDate;
	}

	// End EPPDomainInfoResp.setCreatedDate(Date)

	/**
	 * Gets the expiration date and time of the domain.
	 *
	 * @return Expiration date and time of the domain if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDomainInfoResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the domain.
	 *
	 * @param aExpirationDate Expiration date and time of the domain.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDomainInfoResp.setExpirationDate(Date)

	/**
	 * Gets the Client Id that last updated the domain.  This will be null if
	 * the domain has not been updated since creation.
	 *
	 * @return Client Id that last updated the domain has been updated;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	// End EPPDomainInfoResp.getLastUpdatedBy()

	/**
	 * Sets the Client Id that last updated the domain.
	 *
	 * @param aLastUpdatedBy Client Id String that last updated the domain.
	 */
	public void setLastUpdatedBy(String aLastUpdatedBy) {
		lastUpdatedBy = aLastUpdatedBy;
	}

	// End EPPDomainInfoResp.setLastUpdatedBy(String)

	/**
	 * Gets the date and time of the last domain update.  This will be
	 * <code>null</code>     if the domain has not been updated since
	 * creation.
	 *
	 * @return date and time of the last domain update if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	// End EPPDomainInfoResp.getLastUpdatedDate()

	/**
	 * Sets the last date and time the domain was updated.
	 *
	 * @param aLastUpdatedDate Date and time of the last domain update.
	 */
	public void setLastUpdatedDate(Date aLastUpdatedDate) {
		lastUpdatedDate = aLastUpdatedDate;
	}

	// End EPPDomainInfoResp.setLastUpdatedDate(Date)

	/**
	 * Gets the date and time of the last successful domain transfer.  This
	 * will be <code>null</code>     if the domain has not been successfully
	 * transferred since creation.
	 *
	 * @return date and time of the last successful transfer if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	// End EPPDomainInfoResp.getLastTransferDate()

	/**
	 * Sets the last date and time the domain was successfully transferred.
	 *
	 * @param aLastTransferDate Date and time of the last succesful transfer
	 */
	public void setLastTransferDate(Date aLastTransferDate) {
		lastTransferDate = aLastTransferDate;
	}

	// End EPPDomainInfoResp.setLastTransferDate(Date)

	/**
	 * Get authorization information
	 *
	 * @return Authorization information if defined; <code>null</code>
	 * 		   otherwise;
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDomainInfoResp.getAuthInfo()

	/**
	 * Get registrant
	 *
	 * @return String
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPDomainInfoResp.getRegistrant()

	/**
	 * Set registrants.
	 *
	 * @param newRegistrant String
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPDomainInfoResp.setRegistrant(String)

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
		}
	}

	// End EPPDomainInfoResp.setAuthInfo(EPPAuthInfo)

	/**
	 * Get roid.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRoid() {
		return roid;
	}

	// End EPPDomainInfoResp.getRoid()

	/**
	 * Set roid.
	 *
	 * @param newRoid java.lang.String
	 */
	public void setRoid(String newRoid) {
		roid = newRoid;
	}

	// End EPPDomainInfoResp.setRoid(String)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainInfoResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the EPPDomainInfoResp
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPDomainInfoResp
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			cat.error("EPPDomainInfoResp.doEncode(): Invalid state on encode: "
					  + e);
			throw new EPPEncodeException("Invalid state on EPPDomainInfoResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDomainMapFactory.NS,
							 ELM_ROID);

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		// registrant
		if (registrant != null) {
			EPPUtil.encodeString(
								 aDocument, root, registrant,
								 EPPDomainMapFactory.NS, ELM_REGISTRANT);
		}

		// Contacts
		if (contacts != null) {
			if (
				EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
				EPPUtil.encodeCompVector(aDocument, root, contacts);
			}
			else {
				throw new EPPEncodeException("Contacts specified when the Contact Mapping is not supported");
			}
		}

		// name servers
		if ((this.nses != null) && (this.nses.size() > 0)) {
			Element theServersElm =
				aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NS);
			root.appendChild(theServersElm);

			Object theNS = this.nses.get(0);

			// Name Server Host objects?
			if (theNS instanceof String) {
				EPPUtil.encodeVector(
									 aDocument, theServersElm, this.nses,
									 EPPDomainMapFactory.NS, ELM_HOST_OBJ);
			}

			// Name Server Host attributes?
			else if (theNS instanceof EPPHostAttr) {
				EPPUtil.encodeCompVector(aDocument, theServersElm, this.nses);
			}
			else {
				throw new EPPEncodeException("EPPDomainInfoResp.encode: Invalid NS server class "
											 + theNS.getClass().getName());
			}
		}

		// end if (this.servers != null) && (this.servers.size()) > 0)
		// hosts
		if (hosts != null) {
			EPPUtil.encodeVector(
								 aDocument, root, hosts, EPPDomainMapFactory.NS,
								 ELM_HOST);
		}

		// Client Id
		EPPUtil.encodeString(
							 aDocument, root, clientId, EPPDomainMapFactory.NS,
							 ELM_CLID);

		// Created By
		EPPUtil.encodeString(
							 aDocument, root, createdBy, EPPDomainMapFactory.NS,
							 ELM_CRID);

		// Created Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, createdDate,
								  EPPDomainMapFactory.NS, ELM_CRDATE);

		// Last Updated By
		if (lastUpdatedBy != null) {
			EPPUtil.encodeString(
								 aDocument, root, lastUpdatedBy,
								 EPPDomainMapFactory.NS, ELM_UPID);
		}

		// Last Updated Date
		if (lastUpdatedDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastUpdatedDate,
									  EPPDomainMapFactory.NS, ELM_UPDATE);
		}

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDomainMapFactory.NS, ELM_EXDATE);
		}

		// Last Transfer Date
		if (lastTransferDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastTransferDate,
									  EPPDomainMapFactory.NS, ELM_TRDATE);
		}

		// Authorization Info
		if (authInfo != null) {
			EPPUtil.encodeComp(aDocument, root, authInfo);
		}

		return root;
	}

	// End EPPDomainInfoResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainInfoResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainInfoResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// roid
		roid     = EPPUtil.decodeString(
										aElement, EPPDomainMapFactory.NS,
										ELM_ROID);

		// Statuses
		statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPDomainMapFactory.NS,
									 ELM_STATUS, EPPDomainStatus.class);

		// registant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_REGISTRANT);

		// Contacts
		contacts =
			EPPUtil.decodeCompVector(
									 aElement, EPPDomainMapFactory.NS,
									 ELM_CONTACT, EPPDomainContact.class);

		if (contacts.size() == 0) {
			contacts = null;
		}

		// name servers
		Element theServersElm = EPPUtil.getElementByTagNameNS(aElement, EPPDomainMapFactory.NS, ELM_NS);

		if (theServersElm != null) {
			Element theServerElm = EPPUtil.getFirstElementChild(theServersElm);

			if (theServerElm != null) {
				if (theServerElm.getLocalName().equals(EPPUtil.getLocalName(ELM_HOST_OBJ))) {
					nses =
						EPPUtil.decodeVector(
											 theServersElm,
											 EPPDomainMapFactory.NS,
											 ELM_HOST_OBJ);
				}
				else if (theServerElm.getLocalName().equals(EPPUtil.getLocalName(ELM_HOST_ATTR))) {
					nses =
						EPPUtil.decodeCompVector(
												 theServersElm,
												 EPPDomainMapFactory.NS,
												 ELM_HOST_ATTR,
												 EPPHostAttr.class);
				}
				else {
					throw new EPPDecodeException("EPPDomainCreateCmd.doDecode: Invalid host child element "
												 + theServersElm.getLocalName());
				}
			}

			// end if (theServerElm != null) 
		}

		// end if (theServersElm != null)
		// Child Servers
		hosts =
			EPPUtil.decodeVector(aElement, EPPDomainMapFactory.NS, ELM_HOST);

		// Client Id
		clientId =
			EPPUtil.decodeString(aElement, EPPDomainMapFactory.NS, ELM_CLID);

		// Created By
		createdBy =
			EPPUtil.decodeString(aElement, EPPDomainMapFactory.NS, ELM_CRID);

		// Created Date
		createdDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_CRDATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_EXDATE);

		// Last Updated By
		lastUpdatedBy =
			EPPUtil.decodeString(aElement, EPPDomainMapFactory.NS, ELM_UPID);

		// Last Updated Date
		lastUpdatedDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_UPDATE);

		// Last Transfer Date
		lastTransferDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_TRDATE);

		// Authorization Info
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDomainMapFactory.NS,
											 EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPDomainInfoResp.doDecode(Element)

	/**
	 * Validate the state of the <code>EPPDomainInfoResp</code> instance.  A
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

	// End EPPDomainInfoResp.isValid()
}
