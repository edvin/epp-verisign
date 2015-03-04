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
package com.verisign.epp.codec.nameWatch;

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
 * Represents an EPP NameWatch &lt;nameWatch:infData&gt; response to an
 * <code>EPPNameWatchInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;nameWatch:infData&gt; element that identifies the nameWatch
 * namespace and the location of the nameWatch schema. The
 * &lt;nameWatch:infData&gt; element contains the following child elements:
 * <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:name&gt; element that contains the fully qualified name of
 * the nameWatch.  Use <code>getName</code> and <code>setName</code> to get
 * and     set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the Repository Object
 * IDentifier assigned to the nameWatch object when the object was created.
 * Use <code>getRoid</code> and <code>setRoid</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;nameWatch:registrant&gt; elements that contain identifiers for the
 * human or organizational social information objects associated with the
 * nameWatch object. Use <code>getRegistrant</code> and
 * <code>setRegistrant</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;nameWatch&gt; element that contains the report to descriptors
 * associated with the nameWatch.  See the <code>EPPNameWatchRptTo</code>
 * description for detail.  Use <code>getRptTo</code> and
 * <code>setRptTo</code> to get and set the element.
 * </li>
 * <li>
 * One or more &lt;nameWatch:status&gt; elements that contain the current
 * status descriptors associated with the nameWatch. See the
 * <code>EPPNameWatchStatus</code> description for a list of valid status
 * values.   Use <code>getStatus</code> and     <code>setStatus</code> to get
 * and set the elements.
 * </li>
 * <li>
 * A &lt;nameWatch:clID&gt; element that contains the identifier of the
 * sponsoring client.  Use <code>getClientId</code> and
 * <code>setClientId</code> to get and     set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:crID&gt; element that contains the identifier of the client
 * that created the nameWatch name.  Use <code>getCreatedBy</code> and
 * <code>setCreatedBy</code>     to get and set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:crDate&gt; element that contains the date and time of
 * nameWatch creation.  Use <code>getCreatedDate</code> and
 * <code>setCreatedDate</code> to     get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:upID&gt; element that contains the identifier of
 * the client that last updated the nameWatch name.  This element MUST NOT be
 * present if the nameWatch has never been modified.  Use
 * <code>getLastUpdatedBy</code> and     <code>setLastUpdatedBy</code> to get
 * and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:upDate&gt; element that contains the date and time
 * of the most recent nameWatch modification.  This element MUST NOT be
 * present if the nameWatch has never been modified.  Use
 * <code>getLastUpdatedDate</code> and     <code>setLastUpdatedDate</code> to
 * get and set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:exDate&gt; element that contains the date and time
 * identifying the end of the nameWatch's registration period.  Use
 * <code>getExpirationDate</code>     and <code>setExpirationDate</code> to
 * get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:trDate&gt; elements that contains the date and
 * time of the most recent successful transfer.  This element MUST NOT be
 * provided if the nameWatch has never been transferred.  Use
 * <code>getLastTransferDate</code> and     <code>setLastTransferDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:authInfo&gt; element that contains authorization
 * information associated with the nameWatch object.  This element MUST NOT be
 * provided if the querying client is not the current sponsoring client. Use
 * <code>getAuthInfo</code> and <code>setAuthInfo</code> to get and set the
 * elements.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchInfoCmd
 */
public class EPPNameWatchInfoResp extends EPPResponse {
	/** XML Element Name of <code>EPPNameWatchInfoResp</code> root element. */
	final static String ELM_NAME = "nameWatch:infData";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_NAMEWATCH_NAME = "nameWatch:name";

	/** XML tag name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** XML tag name for the <code>registrant</code> attribute. */
	private final static String ELM_REGISTRANT = "nameWatch:registrant";

	/** XML tag name for the <code>rptTo</code> attribute. */
	private final static String ELM_RPTTO = "nameWatch:rptTo";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "nameWatch:status";

	/** XML tag name for the <code>clientId</code> attribute. */
	private final static String ELM_CLID = "nameWatch:clID";

	/** XML tag name for the <code>createdBy</code> attribute. */
	private final static String ELM_CRID = "nameWatch:crID";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_CRDATE = "nameWatch:crDate";

	/** XML tag name for the <code>lastUpdatedBy</code> attribute. */
	private final static String ELM_UPID = "nameWatch:upID";

	/** XML tag name for the <code>lastUpdatedDate</code> attribute. */
	private final static String ELM_UPDATE = "nameWatch:upDate";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXDATE = "nameWatch:exDate";

	/** XML tag name for the <code>lastTransferDate</code> attribute. */
	private final static String ELM_TRDATE = "nameWatch:trDate";

	/** fully qualified name of the nameWatch */
	private String name = null;

	/** roid. */
	private String roid = null;

	/** registrant. */
	private String registrant = null;

	/** Report To. */
	private EPPNameWatchRptTo rptTo = null;

	/** one or more current status descriptors. */
	private Vector statuses = new Vector();

	/** identifier of sponsoring client */
	private String clientId = null;

	/** identifier of the client that created the nameWatch name */
	private String createdBy = null;

	/** date and time of nameWatch creation */
	private Date createdDate = null;

	/** identifier of the client that last updated the nameWatch name */
	private String lastUpdatedBy = null;

	/** date and time of the most recent nameWatch modification */
	private Date lastUpdatedDate = null;

	/**
	 * date and time identifying the end of the nameWatch's registration period
	 */
	private Date expirationDate = null;

	/** date and time of the most recent successful transfer */
	private Date lastTransferDate = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/**
	 * <code>EPPNameWatchInfoResp</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * transaction id - <code>setTransId</code>
	 * </li>
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
	public EPPNameWatchInfoResp() {
		// Default values set in attribute definitions.
	}

	// End EPPNameWatchInfoResp.EPPNameWatchInfoResp()

	/**
	 * <code>EPPNameWatchInfoResp</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * roid - <code>setRoid</code>
	 * </li>
	 * <li>
	 * client id - <code>setClientId</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId DOCUMENT ME!
	 * @param aName DOCUMENT ME!
	 */
	public EPPNameWatchInfoResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPNameWatchInfoResp.EPPNameWatchInfoResp(EPPTransId, String)

	
	/**
	 * <code>EPPNameWatchInfoResp</code> constuctor that takes the required
	 * attribute values as parameters.     
	 * The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName NameWatch name
	 * @param aRoid roid
	 * @param aClientId Owning Client Id
	 */
	public EPPNameWatchInfoResp(EPPTransId aTransId, String aName, String aRoid,
								String aClientId) {
		super(aTransId);

		name			   = aName;
		roid			   = aRoid;
		clientId		   = aClientId;
	}
	
	
	/**
	 * <code>EPPNameWatchInfoResp</code> constuctor that takes the required
	 * and most used optional attribute values as parameters.     
	 * The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName NameWatch name
	 * @param aRoid roid
	 * @param aRegistrant NameWatch registrant
	 * @param aRptTo EPPNameWatchRptTo rptTo
	 * @param someStatuses Current status descriptors associated with the
	 * 		  nameWatch.
	 * @param aClientId Owning Client Id
	 * @param aCreatedBy Client Id of Registrar that created the nameWatch
	 * @param aCreatedDate Date the nameWatch was created
	 * @param aExpirationDate Expirate date of the nameWatch
	 */
	public EPPNameWatchInfoResp(
								EPPTransId aTransId, String aName, String aRoid,
								String aRegistrant, EPPNameWatchRptTo aRptTo,
								Vector someStatuses, String aClientId,
								String aCreatedBy, Date aCreatedDate,
								Date aExpirationDate) {
		super(aTransId);

		name			   = aName;
		roid			   = aRoid;
		registrant		   = aRegistrant;
		rptTo			   = aRptTo;
		statuses		   = someStatuses;
		clientId		   = aClientId;
		createdBy		   = aCreatedBy;
		createdDate		   = aCreatedDate;
		expirationDate     = aExpirationDate;
	}

	// End EPPNameWatchInfoResp.EPPNameWatchInfoResp(EPPTransId, String, String, EPPNameWatchRptTo, Vector, String, String, Date, Date)

	/**
	 * Get the current associated statuses
	 *
	 * @return NameWatch status
	 */
	public Vector getStatuses() {
		return statuses;
	}

	// End EPPNameWatchInfoResp.getStatuses()

	/**
	 * Set associated statuses.
	 *
	 * @param newStatuses NameWatch status
	 */
	public void setStatuses(Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPNameWatchInfoResp.setStatuses(Vector)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPNameWatchInfoResp</code>.
	 *
	 * @return <code>EPPNameWatchInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPNameWatchInfoResp.getType()

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPNameWatchInfoResp</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchInfoResp.getNamespace()

	/**
	 * Gets the EPPNameWatchRptTo.
	 *
	 * @return instance of <code>EPPNameWatchRptTo</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public EPPNameWatchRptTo getRptTo() {
		return rptTo;
	}

	// End EPPNameWatchInfoResp.getRptTo()

	/**
	 * Sets the rptTo.
	 *
	 * @param aRptTo of<code>EPPNameWatchRptTo</code>.
	 */
	public void setRptTo(EPPNameWatchRptTo aRptTo) {
		if (aRptTo != null) {
			rptTo = aRptTo;
		}
	}

	// End EPPNameWatchInfoResp.setRptTo(EPPNameWatchRptTo)

	/**
	 * Compare an instance of <code>EPPNameWatchInfoResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchInfoResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchInfoResp theInfoData = (EPPNameWatchInfoResp) aObject;

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

		// registrant
		if (
			!(
					(registrant == null) ? (theInfoData.registrant == null)
											 : registrant.equals(theInfoData.registrant)
				)) {
			return false;
		}

		// rptTo
		if (
			!(
					(rptTo == null) ? (theInfoData.rptTo == null)
										: rptTo.equals(theInfoData.rptTo)
				)) {
			return false;
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theInfoData.statuses)) {
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

		// Expiration Date
		if (
			!(
					(expirationDate == null)
						? (theInfoData.expirationDate == null)
							: expirationDate.equals(theInfoData.expirationDate)
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

	// End EPPNameWatchInfoResp.equals(Object)

	/**
	 * Clone <code>EPPNameWatchInfoResp</code>.
	 *
	 * @return clone of <code>EPPNameWatchInfoResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchInfoResp clone = (EPPNameWatchInfoResp) super.clone();

		clone.statuses = (Vector) statuses.clone();

		if (rptTo != null) {
			clone.rptTo = (EPPNameWatchRptTo) rptTo.clone();
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPNameWatchInfoResp.clone()

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

	// End EPPNameWatchInfoResp.toString()

	/**
	 * Gets the nameWatch name
	 *
	 * @return NameWatch Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPNameWatchInfoResp.getName()

	/**
	 * Sets the nameWatch name.
	 *
	 * @param aName NameWatch Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPNameWatchInfoResp.setName(String)

	/**
	 * Sets the nameWatch registrant
	 *
	 * @param aRegistrant NameWatch Registrant
	 */
	public void setRegistrant(String aRegistrant) {
		registrant = aRegistrant;
	}

	// End EPPNameWatchInfoResp.setRegistrant(String)

	/**
	 * Gets the nameWatch registrant
	 *
	 * @return NameWatch Registrant if defined; <code>null</code> otherwise.
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPNameWatchInfoResp.getRegistrant()

	/**
	 * Gets the nameWatch owning Client Id.
	 *
	 * @return Client Id
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPNameWatchInfoResp.getClientId()

	/**
	 * Sets the nameWatch owning Client Id.
	 *
	 * @param aClientId Client Id
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPNameWatchInfoResp.setClientId(String)

	/**
	 * Gets Client Id that created the nameWatch.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	// End EPPNameWatchInfoResp.getCreatedBy()

	/**
	 * Sets Client Id that created the nameWatch.
	 *
	 * @param aCreatedBy Client Id that created the nameWatch.
	 */
	public void setCreatedBy(String aCreatedBy) {
		createdBy = aCreatedBy;
	}

	// End EPPNameWatchInfoResp.setCreatedBy(Date)

	/**
	 * Gets the date and time the nameWatch was created.
	 *
	 * @return Date and time the nameWatch was created if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	// End EPPNameWatchInfoResp.getCreatedDate()

	/**
	 * Sets the date and time the nameWatch was created.
	 *
	 * @param aDate Date and time the nameWatch was created.
	 */
	public void setCreatedDate(Date aDate) {
		createdDate = aDate;
	}

	// End EPPNameWatchInfoResp.setCreatedDate(Date)

	/**
	 * Gets the expiration date and time of the nameWatch.
	 *
	 * @return Expiration date and time of the nameWatch if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPNameWatchInfoResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the nameWatch.
	 *
	 * @param aExpirationDate Expiration date and time of the nameWatch.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPNameWatchInfoResp.setExpirationDate(Date)

	/**
	 * Gets the Client Id that last updated the nameWatch.  This will be null
	 * if the nameWatch has not been updated since creation.
	 *
	 * @return Client Id that last updated the nameWatch has been updated;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	// End EPPNameWatchInfoResp.getLastUpdatedBy()

	/**
	 * Sets the Client Id that last updated the nameWatch.
	 *
	 * @param aLastUpdatedBy Client Id String that last updated the nameWatch.
	 */
	public void setLastUpdatedBy(String aLastUpdatedBy) {
		lastUpdatedBy = aLastUpdatedBy;
	}

	// End EPPNameWatchInfoResp.setLastUpdatedBy(String)

	/**
	 * Gets the date and time of the last nameWatch update.  This will be
	 * <code>null</code>     if the nameWatch has not been updated since
	 * creation.
	 *
	 * @return date and time of the last nameWatch update if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	// End EPPNameWatchInfoResp.getLastUpdatedDate()

	/**
	 * Sets the last date and time the nameWatch was updated.
	 *
	 * @param aLastUpdatedDate Date and time of the last nameWatch update.
	 */
	public void setLastUpdatedDate(Date aLastUpdatedDate) {
		lastUpdatedDate = aLastUpdatedDate;
	}

	// End EPPNameWatchInfoResp.setLastUpdatedDate(Date)

	/**
	 * Gets the date and time of the last successful nameWatch transfer.  This
	 * will be <code>null</code>     if the nameWatch has not been
	 * successfully transferred since creation.
	 *
	 * @return date and time of the last successful transfer if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	// End EPPNameWatchInfoResp.getLastTransferDate()

	/**
	 * Sets the last date and time the nameWatch was successfully transferred.
	 *
	 * @param aLastTransferDate Date and time of the last succesful transfer
	 */
	public void setLastTransferDate(Date aLastTransferDate) {
		lastTransferDate = aLastTransferDate;
	}

	// End EPPNameWatchInfoResp.setLastTransferDate(Date)

	/**
	 * Gets authorization information
	 *
	 * @return Authorization information if defined; <code>null</code>
	 * 		   otherwise;
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPNameWatchInfoResp.getAuthInfo()

	/**
	 * Sets authorization information
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
		}
	}

	// End EPPNameWatchInfoResp.setAuthInfo(EPPAuthInfo)

	/**
	 * Gets roid.
	 *
	 * @return NameWatch roid
	 */
	public java.lang.String getRoid() {
		return roid;
	}

	// End EPPNameWatchInfoResp.getRoid()

	/**
	 * Sets roid.
	 *
	 * @param newRoid NameWatch roid
	 */
	public void setRoid(String newRoid) {
		roid = newRoid;
	}

	// End EPPNameWatchInfoResp.setRoid(String)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchInfoResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   EPPNameWatchInfoResp instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPNameWatchInfoResp
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPNameWatchInfoResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPNameWatchMapFactory.NS,
							 ELM_NAMEWATCH_NAME);

		// registrant
		EPPUtil.encodeString(
							 aDocument, root, registrant,
							 EPPNameWatchMapFactory.NS, ELM_REGISTRANT);

		// rptTo
		if ((rptTo != null) && (!rptTo.isRptToUnspec())) {
			EPPUtil.encodeComp(aDocument, root, rptTo);
		}

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		// Client Id
		EPPUtil.encodeString(
							 aDocument, root, clientId,
							 EPPNameWatchMapFactory.NS, ELM_CLID);

		// Created By
		EPPUtil.encodeString(
							 aDocument, root, createdBy,
							 EPPNameWatchMapFactory.NS, ELM_CRID);

		// Created Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, createdDate,
								  EPPNameWatchMapFactory.NS, ELM_CRDATE);

		// Last Updated By
		if (lastUpdatedBy != null) {
			EPPUtil.encodeString(
								 aDocument, root, lastUpdatedBy,
								 EPPNameWatchMapFactory.NS, ELM_UPID);
		}

		// Last Updated Date
		if (lastUpdatedDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastUpdatedDate,
									  EPPNameWatchMapFactory.NS, ELM_UPDATE);
		}

		// Expiration Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, expirationDate,
								  EPPNameWatchMapFactory.NS, ELM_EXDATE);

		// Last Transfer Date
		if (lastTransferDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastTransferDate,
									  EPPNameWatchMapFactory.NS, ELM_TRDATE);
		}

		// Authorization Info
		if (authInfo != null) {
			EPPUtil.encodeComp(aDocument, root, authInfo);
		}

		return root;
	}

	// End EPPNameWatchInfoResp.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchInfoResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchInfoResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_NAMEWATCH_NAME);

		// registant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_REGISTRANT);

		// rptTo
		rptTo =
			(EPPNameWatchRptTo) EPPUtil.decodeComp(
												   aElement,
												   EPPNameWatchMapFactory.NS,
												   EPPNameWatchRptTo.ELM_NAME,
												   EPPNameWatchRptTo.class);

		// Statuses
		statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPNameWatchMapFactory.NS,
									 ELM_STATUS, EPPNameWatchStatus.class);

		// Client Id
		clientId =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_CLID);

		// Created By
		createdBy =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_CRID);

		// Created Date
		createdDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_CRDATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_EXDATE);

		// Last Updated By
		lastUpdatedBy =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_UPID);

		// Last Updated Date
		lastUpdatedDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_UPDATE);

		// Last Transfer Date
		lastTransferDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_TRDATE);

		// Authorization Info
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPNameWatchMapFactory.NS,
											 EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO,
											 EPPAuthInfo.class);

		//Validate States
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPNameWatchCreateCmd.decode: "
										 + e);
		}
	}

	// End EPPNameWatchInfoResp.doDecode(Element)

	/**
	 * Validate the state of the <code>EPPNameWatchInfoResp</code> instance.  A
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
		if (this.name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (this.roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}

		if (this.clientId == null) {
			throw new EPPCodecException("clientId required attribute is not set");
		}
	}

	// End EPPNameWatchInfoResp.validateState()
}
