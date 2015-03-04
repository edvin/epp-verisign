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
package com.verisign.epp.codec.defReg;


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
 * Represents an EPP DefReg &lt;defReg:infData&gt; response to an
 * <code>EPPDefRegInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;defReg:infData&gt; element that identifies the defReg namespace
 * and the location of the defReg schema. The &lt;defReg:infData&gt; element
 * contains the following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:name&gt; element that contains name information associated with
 * the defReg object.   Use <code>getName</code> and <code>setName</code> to
 * get and     set the defReg object.
 * </li>
 * <li>
 * A &lt;defReg:roid&gt; element that contains the Repository Object
 * IDentifier. Use <code>getRoid</code> and <code>setRoid</code> to get and
 * set the Roid.
 * </li>
 * <li>
 * An  &lt;defReg:registrant&gt; element that contains the identifier for the
 * human or organizational social information (contact) object to be
 * associated with the defReg object as the object registrant. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get     and
 * set the elements.
 * </li>
 * <li>
 * A &lt;defReg:tm&gt; OPTIONAL element that contains trademark information to
 * be associated with the defReg object. Use <code>getTm</code> and
 * <code>setTm</code> to get     and set the trademark elements.
 * </li>
 * <li>
 * A &lt;defReg:tmcountry&gt; OPTIONAL element that contains trademark country
 * information to be associated with the defReg object. Use
 * <code>getTmCountry</code> and <code>setTmCountry</code> to get     and set
 * the elements.
 * </li>
 * <li>
 * A &lt;defReg:tmdate&gt; OPTIONAL element that contains tradeamark date
 * information to be associated with the defReg object. Use
 * <code>getTmDate</code> and <code>setTmDate</code> to get     and set the
 * elements.
 * </li>
 * <li>
 * A &lt;defReg:admincontact&gt; element that contains admincontact information
 * to be associated with the defReg object. Use <code>getAdminContact</code>
 * and <code>setAdminContact</code> to get     and set the elements.
 * </li>
 * <li>
 * A &lt;defReg:authInfo&gt; element that contains authorization information to
 * be associated with the defReg object.
 * </li>
 * <li>
 * One or more &lt;defReg:status&gt; elements that contain the current status
 * descriptors associated with the defReg. See the
 * <code>EPPDefRegStatus</code> description for a list of valid status values.
 * Use <code>getStatus</code> and     <code>setStatus</code> to get and set
 * the elements.
 * </li>
 * <li>
 * A &lt;defReg:crID&gt; element that contains the identifier of the client
 * that created the defReg name.  Use <code>getCreatedBy</code> and
 * <code>setCreatedBy</code>     to get and set the element.
 * </li>
 * <li>
 * A &lt;defReg:crDate&gt; element that contains the date and time of defReg
 * creation.  Use <code>getCreatedDate</code> and <code>setCreatedDate</code>
 * to     get and set the element.
 * </li>
 * <li>
 * A &lt;defReg:exDate&gt; element that contains the date and time identifying
 * the end of the defReg's registration period.  Use
 * <code>getExpirationDate</code>     and <code>setExpirationDate</code> to
 * get and set the element.
 * </li>
 * <li>
 * A &lt;defReg:upID&gt; element that contains the identifier of the client
 * that last updated the defReg name.  This element MUST NOT be present if the
 * defReg has never been modified.  Use <code>getLastUpdatedBy</code> and
 * <code>setLastUpdatedBy</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;defReg:upDate&gt; element that contains the date and time of the most
 * recent defReg modification.  This element MUST NOT be present if the defReg
 * has never been modified.  Use <code>getLastUpdatedDate</code> and
 * <code>setLastUpdatedDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;defReg:trDate&gt; elements that contains the date and time of the most
 * recent successful transfer.  This element MUST NOT be provided if the
 * defReg has never been transferred.  Use <code>getLastTransferDate</code>
 * and     <code>setLastTransferDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:authInfo&gt; element that contains authorization
 * information associated with the defReg object.  This element MUST NOT be
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
 * @see com.verisign.epp.codec.defReg.EPPDefRegInfoCmd
 */
public class EPPDefRegInfoResp extends EPPResponse {
	/** XML Element Name of <code>EPPDefRegDefRegInfoResp</code> root element. */
	final static String ELM_NAME = "defReg:infData";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_DEFREG_NAME = "defReg:name";

	/** XML tag name for the <code>roid</code> attribute. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/*
	 *    XML tag name for the <code>registrant</code> attribute.
	 */

	/** DOCUMENT ME! */
	private final static String ELM_DEFREG_REGISTRANT = "defReg:registrant";

	/** XML Element Name for the <code>tm</code> attribute. */
	private final static String ELM_DEFREG_TM = "defReg:tm";

	/** XML Element Name for the <code>tmcountry</code> attribute. */
	private final static String ELM_DEFREG_TMCOUNTRY = "defReg:tmCountry";

	/** XML Element Name for the <code>tmdate</code> attribute. */
	private final static String ELM_DEFREG_TMDATE = "defReg:tmDate";

	/** XML tag name for the <code>admincontact</code> attribute. */
	private final static String ELM_DEFREG_ADMINCONTACT = "defReg:adminContact";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_DEFREG_STATUS = "defReg:status";

	/** DOCUMENT ME! */
	private final static String ELM_DEFREG_CLID = "defReg:clID";

	/** XML tag name for the <code>createdBy</code> attribute. */
	private final static String ELM_DEFREG_CRID = "defReg:crID";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_DEFREG_CRDATE = "defReg:crDate";

	/** XML tag name for the <code>lastUpdatedBy</code> attribute. */
	private final static String ELM_DEFREG_UPID = "defReg:upID";

	/** XML tag name for the <code>lastUpdatedDate</code> attribute. */
	private final static String ELM_DEFREG_UPDATE = "defReg:upDate";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_DEFREG_EXDATE = "defReg:exDate";

	/** XML tag name for the <code>lastTransferDate</code> attribute. */
	private final static String ELM_DEFREG_TRDATE = "defReg:trDate";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPDefRegInfoResp.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** fully qualified name of the defReg */
	private EPPDefRegName name = null;

	/** roid. */
	private java.lang.String roid = null;

	/** registrant. */
	private String registrant = null;

	/** DefReg TradeMark of defReg to create . */
	private String tm = null;

	/** DefReg TradeMarkCountry of defReg to create . */
	private String tmcountry = null;

	/** DefReg TradeMarkDate of defReg to create . */
	private Date tmdate = null;

	/** DefReg AdminContact of defReg to create . */
	private String admincontact = null;

	/** one or more current status descriptors. */
	private java.util.Vector statuses = new Vector();

	/**
	 * <code>Vector</code> of <code>EPPDefRegFwdContact</code> instances
	 * associated with defReg
	 */
	private Vector contacts = null;

	/** identifier of the client that created the defReg name */
	private String clientId = null;

	/** identifier of the creator that created the defReg name */
	private String createdBy = null;

	/** date and time of defReg creation */
	private Date createdDate = null;

	/** date and time identifying the end of the defReg's registration period */
	private Date expirationDate = null;

	/** identifier of the client that last updated the defReg name */
	private String lastUpdatedBy = null;

	/** date and time of the most recent defReg modification */
	private Date lastUpdatedDate = null;

	/** date and time of the most recent successful transfer */
	private Date lastTransferDate = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/**
	 * <code>EPPDefRegInfoResp</code> default constructor.  Must call required
	 * setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * name - <code>setName</code>
	 * </li>
	 * <li>
	 * registrant is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>UNSPEC_PERIOD</code>
	 * </li>
	 * <li>
	 * tm is set to<code>null</code>.
	 * </li>
	 * <li>
	 * tmcountry is set to<code>null</code>.
	 * </li>
	 * <li>
	 * tmdate is set to<code>null</code>.
	 * </li>
	 * <li>
	 * admincontact is set to<code>null</code>.
	 * </li>
	 * <li>
	 * roid - <code>setRoid</code>
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
	public EPPDefRegInfoResp() {
		// Default values set in attribute definitions.
	}

	// End EPPDefRegInfoResp.EPPDefRegInfoResp()

	/**
	 * <code>EPPDefRegInfoResp</code> constuctor that takes the required
	 * attribute values as parameters.     
	 * The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName DefReg name
	 * @param aRoid roid
	 * @param aClientId Expirate date of the defReg
	 */
	public EPPDefRegInfoResp(
							 EPPTransId aTransId, EPPDefRegName aName,
							 String aRoid, String aClientId) {
		super(aTransId);

		name			 = aName;
		roid			 = aRoid;
		clientId		 = aClientId;
	}
	
	
	/**
	 * <code>EPPDefRegInfoResp</code> constuctor that takes the required
	 * attribute and most used optional values as parameters.     
	 * The setter methods of the optional
	 * attributes can be called before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName DefReg name
	 * @param aRoid roid
	 * @param aRegistrant DefReg Registrant
	 * @param aTm DefReg TradeMark
	 * @param aTmCountry DefReg TradeMarkCountry
	 * @param aTmDate DefReg TradeMarkDate
	 * @param aAdminContact DefReg AdminContact
	 * @param aSomeStatuses Current status descriptors associated with the
	 * 		  defReg.
	 * @param aClientId Expirate date of the defReg
	 * @param aCreatedBy Client Id of Registrar that created the defReg
	 * @param aCreatedDate Date the defReg was created
	 * @param aAuthInfo DOCUMENT ME!
	 */
	public EPPDefRegInfoResp(
							 EPPTransId aTransId, EPPDefRegName aName,
							 String aRoid, String aRegistrant, String aTm,
							 String aTmCountry, Date aTmDate,
							 String aAdminContact, Vector aSomeStatuses,
							 String aClientId, String aCreatedBy,
							 Date aCreatedDate, EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name			 = aName;
		roid			 = aRoid;
		registrant		 = aRegistrant;
		tm				 = aTm;
		tmcountry		 = aTmCountry;
		tmdate			 = aTmDate;
		admincontact     = aAdminContact;
		statuses		 = aSomeStatuses;
		clientId		 = aClientId;
		createdBy		 = aCreatedBy;
		createdDate		 = aCreatedDate;
		authInfo		 = aAuthInfo;
		authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
	}

	// End EPPDefRegInfoResp.EPPDefRegInfoResp(EPPTransId, EPPDefRegName, String, String, String, String, Date,String,Vector,String,String,Date,EPPAuthInfo)

	/**
	 * Get the current associated statuses
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getStatuses() {
		return statuses;
	}

	// End EPPDefRegInfoResp.getStatuses()

	/**
	 * Set associated statuses.
	 *
	 * @param newStatuses java.util.Vector
	 */
	public void setStatuses(Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPDefRegInfoResp.setStatuses(Vector)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPDefRegInfoResp</code>.
	 *
	 * @return <code>EPPDefRegInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDefRegInfoResp.getType()

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPDefRegInfoResp</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegInfoResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDefRegInfoResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegInfoResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegInfoResp theInfoData = (EPPDefRegInfoResp) aObject;

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

		// Registrant
		if (
			!(
					(registrant == null) ? (theInfoData.registrant == null)
											 : registrant.equals(theInfoData.registrant)
				)) {
			return false;
		}

		// tm
		if (
			!(
					(tm == null) ? (theInfoData.tm == null)
									 : tm.equals(theInfoData.tm)
				)) {
			return false;
		}

		// tmcountry
		if (
			!(
					(tmcountry == null) ? (theInfoData.tmcountry == null)
											: tmcountry.equals(theInfoData.tmcountry)
				)) {
			return false;
		}

		// tmdate
		if (
			!(
					(tmdate == null) ? (theInfoData.tmdate == null)
										 : tmdate.equals(theInfoData.tmdate)
				)) {
			return false;
		}

		//admincontact
		if (
			!(
					(admincontact == null) ? (
												   theInfoData.admincontact == null
											   )
											   : admincontact.equals(theInfoData.admincontact)
				)) {
			return false;
		}

		//clientId
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

	// End EPPDefRegInfoResp.equals(Object)

	/**
	 * Clone <code>EPPDefRegInfoResp</code>.
	 *
	 * @return clone of <code>EPPDefRegInfoResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegInfoResp clone = (EPPDefRegInfoResp) super.clone();

		clone.statuses = (Vector) statuses.clone();

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPDefRegInfoResp.clone()

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

	// End EPPDefRegInfoResp.toString()

	/**
	 * Gets the defReg name
	 *
	 * @return DefReg Name if defined; <code>null</code> otherwise.
	 */
	public EPPDefRegName getName() {
		return name;
	}

	// End EPPDefRegInfoResp.getName()

	/**
	 * Sets the defReg name.
	 *
	 * @param aName DefReg Name
	 */
	public void setName(EPPDefRegName aName) {
		name = aName;
	}

	// End EPPDefRegInfoResp.setName(String)

	/**
	 * Get the registrant informnation.
	 *
	 * @return DefReg registrant
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPDefRegInfoCmd.getRegistrant()

	/**
	 * Set the registrant informnation.
	 *
	 * @param aRegistrant DefReg registrant
	 */
	public void setRegistrant(String aRegistrant) {
		registrant = aRegistrant;
	}

	// End EPPDefRegInfoCmd.setForwardTo(String)

	/**
	 * Get the trademark informnation.
	 *
	 * @return DefReg tradeMark
	 */
	public String getTm() {
		return tm;
	}

	// End EPPDefRegInfoCmd.getTm()

	/**
	 * Set the trademark informnation.
	 *
	 * @param aTm DefReg registrant
	 */
	public void setTm(String aTm) {
		tm = aTm;
	}

	// End EPPDefRegInfoCmd.setTm(String)

	/**
	 * Get the trademark country informnation.
	 *
	 * @return DefReg trademark country
	 */
	public String getTmCountry() {
		return tmcountry;
	}

	// End EPPDefRegInfoCmd.getTmCountry()

	/**
	 * Set the trademark country informnation.
	 *
	 * @param aTmCountry DefReg    trademark country
	 */
	public void setTmCountry(String aTmCountry) {
		tmcountry = aTmCountry;
	}

	// End EPPDefRegInfoCmd.setTmCountry(String)

	/**
	 * Get the trademark date informnation.
	 *
	 * @return DefReg trademark date
	 */
	public Date getTmDate() {
		return tmdate;
	}

	// End EPPDefRegInfoCmd.getTmDate()

	/**
	 * Set the trademark date informnation.
	 *
	 * @param aTmDate DefReg    trademark date
	 */
	public void setTmDate(Date aTmDate) {
		tmdate = aTmDate;
	}

	// End EPPDefRegInfoCmd.setTmDate(String)

	/**
	 * Get the AdminContact informnation.
	 *
	 * @return DefReg AdminContact
	 */
	public String getAdminContact() {
		return admincontact;
	}

	// End EPPDefRegInfoCmd.getAdminContact()

	/**
	 * Set the AdminContact informnation.
	 *
	 * @param aAdminContact DefReg AdminContact
	 */
	public void setAdminContact(String aAdminContact) {
		admincontact = aAdminContact;
	}

	// End EPPDefRegInfoCmd.setAdminContact(String)

	/**
	 * Get authorization information
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDefRegInfoCmd.getAuthInfo()

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo java.lang.String
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
		}
	}

	// End EPPDefRegInfoCmd.setAuthInfo(EPPAuthInfo)

	/**
	 * Sets Client Id that created the defReg.
	 *
	 * @param aClientId Client Id that associated with  the defReg.
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPDefRegInfoResp.setClientId(String)

	/**
	 * Gets Client Id that created the defReg.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPDefRegInfoResp.getClientId()

	/**
	 * Gets Create Id that created the defReg.
	 *
	 * @return Create Id if defined; <code>null</code> otherwise.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	// End EPPDefRegInfoResp.getCreatedBy()

	/**
	 * Sets Client Id that created the defReg.
	 *
	 * @param aCreatedBy Client Id that created the defReg.
	 */
	public void setCreatedBy(String aCreatedBy) {
		createdBy = aCreatedBy;
	}

	// End EPPDefRegInfoResp.setCreatedBy(Date)

	/**
	 * Gets the date and time the defReg was created.
	 *
	 * @return Date and time the defReg was created if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	// End EPPDefRegInfoResp.getCreatedDate()

	/**
	 * Sets the date and time the defReg was created.
	 *
	 * @param aDate Date and time the defReg was created.
	 */
	public void setCreatedDate(Date aDate) {
		createdDate = aDate;
	}

	// End EPPDefRegInfoResp.setCreatedDate(Date)

	/**
	 * Gets the expiration date and time of the defReg.
	 *
	 * @return Expiration date and time of the defReg if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDefRegInfoResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the defReg.
	 *
	 * @param aExpirationDate Expiration date and time of the defReg.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDefRegInfoResp.setExpirationDate(Date)

	/**
	 * Gets the Client Id that last updated the defReg.  This will be null if
	 * the defReg has not been updated since creation.
	 *
	 * @return Client Id that last updated the defReg has been updated;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	// End EPPDefRegInfoResp.getLastUpdatedBy()

	/**
	 * Sets the Client Id that last updated the defReg.
	 *
	 * @param aLastUpdatedBy Client Id String that last updated the defReg.
	 */
	public void setLastUpdatedBy(String aLastUpdatedBy) {
		lastUpdatedBy = aLastUpdatedBy;
	}

	// End EPPDefRegInfoResp.setLastUpdatedBy(String)

	/**
	 * Gets the date and time of the last defReg update.  This will be
	 * <code>null</code>     if the defReg has not been updated since
	 * creation.
	 *
	 * @return date and time of the last defReg update if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	// End EPPDefRegInfoResp.getLastUpdatedDate()

	/**
	 * Sets the last date and time the defReg was updated.
	 *
	 * @param aLastUpdatedDate Date and time of the last defReg update.
	 */
	public void setLastUpdatedDate(Date aLastUpdatedDate) {
		lastUpdatedDate = aLastUpdatedDate;
	}

	// End EPPDefRegInfoResp.setLastUpdatedDate(Date)

	/**
	 * Gets the date and time of the last successful defReg transfer.  This
	 * will be <code>null</code>     if the defReg has not been successfully
	 * transferred since creation.
	 *
	 * @return date and time of the last successful transfer if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	// End EPPDefRegInfoResp.getLastTransferDate()

	/**
	 * Sets the last date and time the defReg was successfully transferred.
	 *
	 * @param aLastTransferDate Date and time of the last succesful transfer
	 */
	public void setLastTransferDate(Date aLastTransferDate) {
		lastTransferDate = aLastTransferDate;
	}

	// End EPPDefRegInfoResp.setLastTransferDate(Date)

	/**
	 * Get roid.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRoid() {
		return roid;
	}

	// End EPPDefRegInfoResp.getRoid()

	/**
	 * Set roid.
	 *
	 * @param newRoid java.lang.String
	 */
	public void setRoid(String newRoid) {
		roid = newRoid;
	}

	// End EPPDefRegInfoResp.setRoid(String)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegInfoResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the EPPDefRegInfoResp
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPDefRegInfoResp
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPDefRegInfoResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_ROID);

		// Name
		EPPUtil.encodeComp(aDocument, root, name);

		// registrant
		if (registrant != null) {
			EPPUtil.encodeString(
								 aDocument, root, registrant,
								 EPPDefRegMapFactory.NS, ELM_DEFREG_REGISTRANT);
		}

		//tm
		EPPUtil.encodeString(
							 aDocument, root, tm, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_TM);

		//tmcountry
		EPPUtil.encodeString(
							 aDocument, root, tmcountry, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_TMCOUNTRY);

		//TradeMarkDate
		EPPUtil.encodeDate(
						   aDocument, root, tmdate, EPPDefRegMapFactory.NS,
						   ELM_DEFREG_TMDATE);

		//admincontact
		EPPUtil.encodeString(
							 aDocument, root, admincontact,
							 EPPDefRegMapFactory.NS, ELM_DEFREG_ADMINCONTACT);

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		//ClientId
		EPPUtil.encodeString(
							 aDocument, root, clientId, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_CLID);

		// Created By
		EPPUtil.encodeString(
							 aDocument, root, createdBy, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_CRID);

		// Created Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, createdDate,
								  EPPDefRegMapFactory.NS, ELM_DEFREG_CRDATE);

		// Last Updated By
		if (lastUpdatedBy != null) {
			EPPUtil.encodeString(
								 aDocument, root, lastUpdatedBy,
								 EPPDefRegMapFactory.NS, ELM_DEFREG_UPID);
		}

		// Last Updated Date
		if (lastUpdatedDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastUpdatedDate,
									  EPPDefRegMapFactory.NS, ELM_DEFREG_UPDATE);
		}

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDefRegMapFactory.NS, ELM_DEFREG_EXDATE);
		}

		// Last Transfer Date
		if (lastTransferDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, lastTransferDate,
									  EPPDefRegMapFactory.NS, ELM_DEFREG_TRDATE);
		}

		// Authorization Info
		if (authInfo != null) {
			EPPUtil.encodeComp(aDocument, root, authInfo);
		}

		return root;
	}

	// End EPPDefRegInfoResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegInfoResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegInfoResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

		// Name
		name =
			(EPPDefRegName) EPPUtil.decodeComp(
											   aElement, EPPDefRegMapFactory.NS,
											   ELM_DEFREG_NAME,
											   EPPDefRegName.class);

		// registant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_REGISTRANT);

		// DefReg TradeMark
		tm     = EPPUtil.decodeString(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_TM);

		// DefReg TMCountry
		tmcountry =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_TMCOUNTRY);

		// DefReg TMDate
		tmdate =
			EPPUtil.decodeDate(
							   aElement, EPPDefRegMapFactory.NS,
							   ELM_DEFREG_TMDATE);

		// DefReg AdminContact
		admincontact =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ADMINCONTACT);

		// Statuses
		statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPDefRegMapFactory.NS,
									 ELM_DEFREG_STATUS, EPPDefRegStatus.class);

		clientId =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_CLID);

		// Created By
		createdBy =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_CRID);

		// Created Date
		createdDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_CRDATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_EXDATE);

		// Last Updated By
		lastUpdatedBy =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_UPID);

		// Last Updated Date
		lastUpdatedDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_UPDATE);

		// Last Transfer Date
		lastTransferDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_TRDATE);

		// Authorization Info
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDefRegMapFactory.NS,
											 EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPDefRegInfoResp.doDecode(Element)

	/**
	 * Validate the state of the <code>EPPDefRegInfoResp</code> instance.  A
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

	// End EPPDefRegInfoResp.validateState()
}
