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
package com.verisign.epp.codec.host;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Date;
import java.util.Enumeration;

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
 * Represents an EPP Host &lt;host:infData&gt; response to an
 * <code>EPPHostInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;host:infData&gt; element that identifies the host namespace and
 * the location of the host schema. The &lt;host:infData&gt; element contains
 * the following child elements:     <br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains the fully qualified name of the
 * host.  Use <code>getName</code> and <code>setName</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;host:roid&gt; element that contains the Respoitory Object IDentifier
 * assigned to the host object when the object was created.
 * </li>
 * <li>
 * One or more &lt;host:status&gt; elements that describe the status of the
 * host object. Use <code>getStatuses</code> and <code>setStatuses</code> to
 * get and set the     element.
 * </li>
 * <li>
 * Zero or more &lt;host:addr&gt; elements that contain the IP addresses
 * associated with the host object. Use <code>getAddresses</code> and
 * <code>setAddresses</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;host:clID&gt; element that contains the identifier of the sponsoring
 * client.  Use <code>getClientId</code> and <code>setClientId</code>     to
 * get and set the element.
 * </li>
 * <li>
 * A &lt;host:crID&gt; element that contains the identifier of the     client
 * that created the host name.  Use <code>getCreatedBy</code> and
 * <code>setCreatedBy</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;host:crDate&gt; element that contains the date and time of     host
 * creation.  Use <code>getCreatedDate</code> and <code>setCreatedDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * A &lt;host:upID&gt; element that contains the identifier of the     client
 * that last updated the host name.  This element MUST NOT be     present if
 * the host has never been modified.  Use <code>getLastUpdatedBy</code> and
 * <code>setLastUpdatedBy</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;host:upDate&gt; element that contains the date and time     of the
 * most recent host modification.  This element MUST NOT be     present if the
 * host has never been modified.  Use <code>getUpdatedDate</code>     and
 * <code>setUpdatedDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;host:trDate&gt; element that contains the date and time     of the
 * most recent successful transfer.  This element MUST NOT be     provided if
 * the host has never been transferred. Note that host objects MUST NOT be
 * transferred directly; host objects MUST be transferred implicitly when the
 * host object's superordinate domain object is transferred.  Host objects
 * that are subject to transfer when transferring a domain object are listed
 * in the response to an EPP &lt;info&gt; command performed on the domain
 * object. Use <code>getTrDate</code>     and <code>setTrDate</code> to get
 * and set the element.
 * </li>
 * <li>
 * A &lt;host:authInfo&gt; element derived from either the original creation
 * transaction or the most recent successful parent domain transfer
 * transaction.  This element MUST NOT be provided if the querying client is
 * not the current sponsoring client.  Use <code>getAuthInfo</code> and
 * <code>setAuthInfo</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.host.EPPHostInfoCmd
 */
public class EPPHostInfoResp extends EPPResponse {
	/** XML Element Name of <code>EPPHostInfoResp</code> root element. */
	final static String ELM_NAME = "host:infData";

	/** XML Element Name of <code>EPPHostInfoResp</code> root element. */
	final static String ELM_LINKED = "host:linked";

	/** XML Element Name of <code>EPPHostInfoResp</code> root element. */
	final static String ELM_ROID = "host:roid";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_HOST_NAME = "host:name";

	/** XML tag name for the <code>clientId</code> attribute. */
	private final static String ELM_CLIENT_ID = "host:clID";

	/** XML tag name for the <code>createdBy</code> attribute. */
	private final static String ELM_CREATED_BY = "host:crID";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_CREATED_DATE = "host:crDate";

	/** XML tag name for the <code>lastUpdatedBy</code> attribute. */
	private final static String ELM_LAST_UPDATED_BY = "host:upID";

	/** XML tag name for the <code>lastUpdatedDate</code> attribute. */
	private final static String ELM_LAST_UPDATED_DATE = "host:upDate";

	/** XML tag name for the <code>clientId</code> attribute. */
	private final static String ELM_TRDATE = "host:trDate";

	/** fully qualified name of the host */
	private String name = null;

	/** identifier of sponsoring client */
	private String clientId = null;

	/**
	 * <code>Vector</code> of <code>EPPHostAddress</code> instances asssociated
	 * with host.
	 */
	private Vector addresses = new Vector();

	/** identifier of the client that created the host name */
	private String createdBy = null;

	/** date and time of host creation */
	private Date createdDate = null;

	/** identifier of the client that last updated the host name */
	private String lastUpdatedBy = null;

	/** date and time of the most recent host modification */
	private Date lastUpdatedDate = null;

	/** Host statuses */
	private Vector statuses = null;

	/** Host transfer date */
	private java.util.Date trDate = null;

	/** roid */
	private java.lang.String roid = null;

	/**
	 * <code>EPPHostInfoResp</code> default constructor.  Must call required
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
	 * status - <code>setStatuses</code>
	 * </li>
	 * <li>
	 * client id - <code>setClientId</code>
	 * </li>
	 * <li>
	 * created by - <code>setCreatedBy</code>
	 * </li>
	 * <li>
	 * created date - <code>setCreatedDate</code>
	 * </li>
	 * </ul>
	 */
	public EPPHostInfoResp() {
		// Default values set in attribute definitions.
	}

	// End EPPHostInfoResp.EPPHostInfoResp()

	/**
	 * <code>EPPHostInfoResp</code> constuctor that takes the required
	 * attribute values as parameters.     The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName Host name
	 * @param aRoid roid
	 * @param aHostStatus EPP Host Status
	 * @param aClientId Owning Client Id
	 * @param aCreatedBy Client Id of Registrar that created the host
	 * @param aCreatedDate Date the host was created
	 */
	public EPPHostInfoResp(
						   EPPTransId aTransId, String aName, String aRoid,
						   EPPHostStatus aHostStatus, String aClientId,
						   String aCreatedBy, Date aCreatedDate) {
		super(aTransId);

		name		 = aName;
		roid		 = aRoid;
		statuses     = new Vector();
		statuses.addElement(aHostStatus);
		clientId	    = aClientId;
		createdBy	    = aCreatedBy;
		createdDate     = aCreatedDate;
	}

	// End EPPHostInfoResp.EPPHostInfoResp(EPPTransId, String, String, EPPHostStatus, String, String, Date)

	/**
	 * Gets the EPP response type associated with <code>EPPHostInfoResp</code>.
	 *
	 * @return <code>EPPHostInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPHostInfoResp.getType()

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPHostInfoResp</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostInfoResp.getNamespace()

	/**
	 * Validate the state of the <code>EPPHostInfoResp</code> instance.  A
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
			throw new EPPCodecException("required attribute name is not set");
		}

		if (roid == null) {
			throw new EPPCodecException("required attribute roid is not set");
		}

		if (statuses == null) {
			throw new EPPCodecException("required attribute statuses is not set");
		}

		if (clientId == null) {
			throw new EPPCodecException("clientId required attribute is not set");
		}

		if (createdBy == null) {
			throw new EPPCodecException("createBy required attribute is not set");
		}

		if (createdDate == null) {
			throw new EPPCodecException("createdDate required attribute is not set");
		}
	}

	// End EPPHostInfoResp.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostInfoResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the EPPHostPingMap
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPHostPingMap instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPHostInfoResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPHostMapFactory.NS,
							 ELM_HOST_NAME);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPHostMapFactory.NS,
							 ELM_ROID);

		// statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		// Addresses
		EPPUtil.encodeCompVector(aDocument, root, addresses);

		// Client Id
		EPPUtil.encodeString(
							 aDocument, root, clientId, EPPHostMapFactory.NS,
							 ELM_CLIENT_ID);

		// Created By
		EPPUtil.encodeString(
							 aDocument, root, createdBy, EPPHostMapFactory.NS,
							 ELM_CREATED_BY);

		// Created Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, createdDate,
								  EPPHostMapFactory.NS, ELM_CREATED_DATE);

		// Last Updated By
		if (lastUpdatedBy != null) {
			EPPUtil.encodeString(
								 aDocument, root, lastUpdatedBy,
								 EPPHostMapFactory.NS, ELM_LAST_UPDATED_BY);
		}

		// Last Updated Date
		if (lastUpdatedDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastUpdatedDate,
									  EPPHostMapFactory.NS,
									  ELM_LAST_UPDATED_DATE);
		}

		// trDate
		if (trDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, trDate,
									  EPPHostMapFactory.NS, ELM_TRDATE);
		}

		return root;
	}

	// End EPPHostInfoResp.doEncode(Document)

	/**
	 * Decode the <code>EPPHostInfoResp</code> attributes from the aElement DOM
	 * Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostInfoResp</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);

		// roid
		roid     = EPPUtil.decodeString(
										aElement, EPPHostMapFactory.NS, ELM_ROID);

		// statuses
		statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPHostMapFactory.NS,
									 EPPHostStatus.ELM_NAME, EPPHostStatus.class);

		// Addresses
		addresses =
			EPPUtil.decodeCompVector(
									 aElement, EPPHostMapFactory.NS,
									 EPPHostAddress.ELM_NAME,
									 EPPHostAddress.class);

		// Client Id
		clientId =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_CLIENT_ID);

		// Created By
		createdBy =
			EPPUtil.decodeString(
								 aElement, EPPHostMapFactory.NS, ELM_CREATED_BY);

		// Created Date
		createdDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPHostMapFactory.NS,
									  ELM_CREATED_DATE);

		// Last Updated By
		lastUpdatedBy =
			EPPUtil.decodeString(
								 aElement, EPPHostMapFactory.NS,
								 ELM_LAST_UPDATED_BY);

		// Last Updated Date
		lastUpdatedDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPHostMapFactory.NS,
									  ELM_LAST_UPDATED_DATE);

		// trDate
		trDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPHostMapFactory.NS, ELM_TRDATE);
	}

	// End EPPHostInfoResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPHostInfoResp</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostInfoResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostInfoResp theInfoData = (EPPHostInfoResp) aObject;

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

		// statuses
		if (!EPPUtil.equalVectors(statuses, theInfoData.statuses)) {
			return false;
		}

		// Addresses
		if (!EPPUtil.equalVectors(addresses, theInfoData.addresses)) {
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

		// trDate
		if (
			!(
					(trDate == null) ? (theInfoData.trDate == null)
										 : trDate.equals(theInfoData.trDate)
				)) {
			return false;
		}

		return true;
	}

	// End EPPHostInfoResp.equals(Object)

	/**
	 * Clone <code>EPPHostInfoResp</code>.
	 *
	 * @return clone of <code>EPPHostInfoResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostInfoResp clone = (EPPHostInfoResp) super.clone();

		clone.addresses = (Vector) addresses.clone();

		for (int i = 0; i < addresses.size(); i++)
			clone.addresses.setElementAt(
										 ((EPPHostAddress) addresses.elementAt(i))
										 .clone(), i);

		clone.statuses = (Vector) statuses.clone();

		for (int i = 0; i < statuses.size(); i++)
			clone.statuses.setElementAt(
										((EPPHostStatus) statuses.elementAt(i))
										.clone(), i);

		return clone;
	}

	// End EPPHostInfoResp.clone()

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

	// End EPPHostInfoResp.toString()

	/**
	 * Gets the host name
	 *
	 * @return Host Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPHostInfoResp.getName()

	/**
	 * Sets the host name.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostInfoResp.setName(String)

	/**
	 * Gets the host owning Client Id.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPHostInfoResp.getClientId()

	/**
	 * Sets the host owning Client Id.
	 *
	 * @param aClientId Client Id
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPHostInfoResp.setClientId(String)

	/**
	 * Gets the list (Vector) of host addresses.  Each host address is an
	 * instance of <code>EPPHostAddress</code>.
	 *
	 * @return <code>Vector</code> of <code>EPPHostAddress</code> instances.
	 */
	public Vector getAddresses() {
		return addresses;
	}

	// End EPPHostInfoResp.getAddresses()

	/**
	 * Sets the list (Vector) of host addresses.  Each host address is an
	 * instance of <code>EPPHostAddress</code>.
	 *
	 * @param someAddresses <code>Vector</code> of <code>EPPHostAddress</code>
	 * 		  instances
	 */
	public void setAddresses(Vector someAddresses) {
		addresses = someAddresses;
	}

	// End EPPHostInfoResp.setAddresses(Vector)

	/**
	 * Gets Client Id that created the host.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	// End EPPHostInfoResp.getCreatedBy()

	/**
	 * Sets Client Id that created the host.
	 *
	 * @param aCreatedBy Client Id that created the host.
	 */
	public void setCreatedBy(String aCreatedBy) {
		createdBy = aCreatedBy;
	}

	// End EPPHostInfoResp.setCreatedBy(Date)

	/**
	 * Gets the date and time the host was created.
	 *
	 * @return Date and time the host was created if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	// End EPPHostInfoResp.getCreatedDate()

	/**
	 * Sets the date and time the host was created.
	 *
	 * @param aDate Date and time the host was created.
	 */
	public void setCreatedDate(Date aDate) {
		createdDate = aDate;
	}

	// End EPPHostInfoResp.setCreatedDate()

	/**
	 * Gets the Client Id that last updated the host.  This will be null     if
	 * the host has not been updated since creation.
	 *
	 * @return Client Id that last updated the host has been updated;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	// End EPPHostInfoResp.getLastUpdatedBy()

	/**
	 * Sets the Client Id that last updated the host.
	 *
	 * @param aLastUpdatedBy Client Id String that last updated the host.
	 */
	public void setLastUpdatedBy(String aLastUpdatedBy) {
		lastUpdatedBy = aLastUpdatedBy;
	}

	// End EPPHostInfoResp.setLastUpdatedBy(String)

	/**
	 * Gets the date and time of the last host update.  This will be
	 * <code>null</code>     if the host has not been updated since creation.
	 *
	 * @return date and time of the last host update if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	// End EPPHostInfoResp.getLastUpdatedDate()

	/**
	 * Sets the last date and time the host was updated.
	 *
	 * @param aLastUpdatedDate Date and time of the last host update.
	 */
	public void setLastUpdatedDate(Date aLastUpdatedDate) {
		lastUpdatedDate = aLastUpdatedDate;
	}

	// End EPPHostInfoResp.setLastUpdatedDate(Date)

	/**
	 * Get host statuses.
	 *
	 * @return com.verisign.epp.codec.host.EPPHostStatus
	 */
	public Vector getStatuses() {
		return statuses;
	}

	// End EPPHostInfoResp.getStatuses()

	/**
	 * Get transfer date.
	 *
	 * @return java.util.Date
	 */
	public java.util.Date getTrDate() {
		return trDate;
	}

	// End EPPHostInfoResp.getTrDate()

	/**
	 * Set host statuses.
	 *
	 * @param aHostStatus com.verisign.epp.codec.host.EPPHostStatus
	 */
	public void setStatuses(EPPHostStatus aHostStatus) {
		if (statuses != null) {
			statuses.addElement(aHostStatus);
		}
		else {
			statuses = new Vector();
			statuses.addElement(aHostStatus);
		}
	}

	// End EPPHostInfoResp.setName(EPPHostStatus)

	/**
	 * Set host statuses.
	 *
	 * @param newStatuses Vector
	 */
	public void setStatuses(Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPHostInfoResp.setName(Vector)

	/**
	 * Set transfer date.
	 *
	 * @param newTrDate java.util.Date
	 */
	public void setTrDate(java.util.Date newTrDate) {
		trDate = newTrDate;
	}

	// End EPPHostInfoResp.setName(Date)

	/**
	 * Get roid.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRoid() {
		return roid;
	}

	// End EPPHostInfoResp.getRoid()

	/**
	 * Set roid.
	 *
	 * @param newRoid java.lang.String
	 */
	public void setRoid(String newRoid) {
		roid = newRoid;
	}

	// End EPPHostInfoResp.setRoid(String)
}
