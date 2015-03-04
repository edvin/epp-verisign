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
 * Represents attributes to add, remove or change with a
 * <code>EPPDefRegUpdateCmd</code>.     In <code>EPPDefRegUpdateCmd</code>, an
 * instance of <code>EPPDefRegAddRemove</code> is used     to specify the
 * attributes to add; an instance of <code>EPPDefRegAddRemove</code> is used
 * to specify the attributes to remove, and an instance of
 * <code>EPPDefRegAddRemove</code> is used     to specify the attributes to change<br>
 * <br>
 * The DefRegReg Mapping Specification describes the following attributes:<br>
 * 
 * <ul>
 * <li>
 * For <code>change</code> only, A optional &lt;defReg:registrant&gt; element
 * that contains the identifier for the human or organizational social
 * information (contact) object to be associated with the defReg object as the
 * object registrant.  This object identifier MUST be known to the server
 * before the contact object can be associated with the defReg object. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get and set
 * the element.
 * </li>
 * <li>
 * For <code>change</code> only, A optional &lt;defReg:tm&gt; element that
 * contains trademark information to be associated with the defReg object. Use
 * <code>getTm</code> and <code>setTm</code> to get and set the element.
 * </li>
 * <li>
 * For <code>change</code> only, A optional &lt;defReg:tmcountry&gt; element
 * that contains trademark country information to be associated with the
 * defReg object.  Use <code>getTmCountry</code> and <code>setTmCountry</code>
 * to get and set the element.
 * </li>
 * <li>
 * For <code>change</code> only, A optional &lt;defReg:tmdate&gt; element that
 * contains tradeamark date information to be associated with the defReg
 * object.  Use <code>getTmDate</code> and <code>setTmDate</code> to get and
 * set the element.
 * </li>
 * <li>
 * For <code>change</code> only, An OPTIONAL &lt;defReg:admincontact&gt;
 * element that contains the defreg forwardTo addresses. Use
 * <code>getAdminContact</code>  and <code>setAdminContact</code> to get and
 * set the Admin Contacts.
 * </li>
 * <li>
 * For <code>change</code> only, An OPTIONAL &lt;defReg:authInfo&gt; element
 * that contains authorization information to be associated with the defReg
 * object.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the maximum number of defReg attribute
 * elements is subject to the number of values currently associated with the
 * defReg object.  <code>EPPDefRegAddRemove</code> will delegate the
 * validation of     the cardinality of the defReg attributes elements to the
 * EPP Server.
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegUpdateCmd
 */
public class EPPDefRegAddRemove implements EPPCodecComponent {
	/** mode of <code>EPPDefRegAddRemove</code> is not specified. */
	final static short MODE_NONE = 0;

	/** mode of <code>EPPDefRegAddRemove</code> is to add attributes. */
	final static short MODE_ADD = 1;

	/** mode of <code>EPPDefRegAddRemove</code> is to remove attributes. */
	final static short MODE_REMOVE = 2;

	/** mode of <code>EPPDefRegAddRemove</code> is to change attributes. */
	final static short MODE_CHANGE = 3;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPDefRegAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_ADD = "defReg:add";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPDefRegAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_REMOVE = "defReg:rem";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPDefRegAddRemove.MODE_CHANGE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_CHANGE = "defReg:chg";

	/** XML tag name for the <code>servers</code> attribute. */
	private final static String ELM_DEFREG_REGISTRANT = "defReg:registrant";

	/** XML Element Name for the <code>tm</code> attribute. */
	private final static String ELM_DEFREG_TM = "defReg:tm";

	/** XML Element Name for the <code>tmcountry</code> attribute. */
	private final static String ELM_DEFREG_TMCOUNTRY = "defReg:tmCountry";

	/** XML Element Name for the <code>tmdate</code> attribute. */
	private final static String ELM_DEFREG_TMDATE = "defReg:tmDate";

	/** XML Element Name for the <code>fwdTo</code> attribute. */
	private final static String ELM_DEFREG_ADMINCONTACT = "defReg:adminContact";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "defReg:status";

	/**
	 * Mode of EPPDefRegAddRemove.  Must be <code>MODE_ADD</code> or
	 * <code>MODE_REMOVE</code> to be valid.  This     attribute will be set
	 * by the parent container <code>EPPCodecComponent</code>.  For example,
	 * <code>EPPDefRegUpdateCmd</code> will set the mode for its
	 * <code>EPPDefRegAddRemove</code> instances.
	 */
	private short mode = MODE_NONE;

	/** registrant to change. */
	private java.lang.String registrant = null;

	/** DefReg TradeMark of defReg to create . */
	private String tm = null;

	/** DefReg TradeMarkCountry of defReg to create . */
	private String tmcountry = null;

	/** DefReg TradeMarkDate of defReg to create . */
	private Date tmdate = null;

	/** adminContact to change. */
	private java.lang.String adminContact = null;

	/** authorization information to change. */
	private EPPAuthInfo authInfo = null;

	/** Status to add or remove. */
	private Vector statuses = null;

	/**
	 * Default constructor for <code>EPPDefRegAddRemove</code>.  All of the
	 * attribute     default to <code>null</code> to indicate no modification.
	 */
	public EPPDefRegAddRemove() {
		registrant     = null;

		tm     = null;

		tmcountry     = null;

		tmdate     = null;

		adminContact     = null;

		authInfo     = null;

		statuses = null;
	}

	// End EPPDefRegAddRemove()

	/**
	 * Constructor for <code>EPPDefRegAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aStatuses <code>Vector</code> statuses
	 */
	public EPPDefRegAddRemove(Vector aStatuses) {
		statuses = aStatuses;
	}

	// End EPPDefRegAddRemove(Vector)

	/**
	 * Constructor for <code>EPPDefRegAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aRegistrant <code>String</code>         registrant for the change
	 * 		  mode
	 * @param aTm DefReg TradeMark
	 * @param aTmCountry DefReg TradeMarkCountry
	 * @param aTmDate DefReg TradeMarkDate
	 * @param aAdminContact <code>String</code>         registrant AdminContact
	 * @param aAuthInfo <code>EPPAuthInfo</code>    authorization information
	 * 		  for the change mode
	 */
	public EPPDefRegAddRemove(
							  String aRegistrant, String aTm, String aTmCountry,
							  Date aTmDate, String aAdminContact,
							  EPPAuthInfo aAuthInfo) {
		registrant     = aRegistrant;

		tm     = aTm;

		tmcountry     = aTmCountry;

		tmdate     = aTmDate;

		adminContact     = aAdminContact;

		authInfo = aAuthInfo;

		if (authInfo != null) {
			authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
		}
	}

	// End EPPDefRegAddRemove(String,String,EPPAuthInfo)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegAddRemove</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDefRegAddRemove</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegAddRemove</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = null;

		// Change mode
		if (mode == MODE_CHANGE) {
			root =
				aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_CHANGE);

			//registrant
			if (registrant != null) {
				EPPUtil.encodeString(
									 aDocument, root, registrant,
									 EPPDefRegMapFactory.NS,
									 ELM_DEFREG_REGISTRANT);
			}

			//TradeMark
			EPPUtil.encodeString(
								 aDocument, root, tm, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_TM);

			//TrademMark Country
			EPPUtil.encodeString(
								 aDocument, root, tmcountry,
								 EPPDefRegMapFactory.NS, ELM_DEFREG_TMCOUNTRY);

			//TradeMarkDate
			EPPUtil.encodeDate(
							   aDocument, root, tmdate, EPPDefRegMapFactory.NS,
							   ELM_DEFREG_TMDATE);

			//admincontact
			if (adminContact != null) {
				EPPUtil.encodeString(
									 aDocument, root, adminContact,
									 EPPDefRegMapFactory.NS,
									 ELM_DEFREG_ADMINCONTACT);
			}

			//authinfo
			if (authInfo != null) {
				EPPUtil.encodeComp(aDocument, root, authInfo);
			}

			return root;
		}

		// Add or Remove mode
		if (mode == MODE_ADD) {
			root = aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_ADD);
		}

		else if (mode == MODE_REMOVE) {
			root =
				aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_REMOVE);
		}

		else {
			throw new EPPEncodeException("Invalid EPPDefRegAddRemove mode of "
										 + mode);
		}

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		return root;
	}

	// End EPPDefRegAddRemove.encode(Document)

	/**
	 * Decode the <code>EPPDefRegAddRemove</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegAddRemove</code> from.
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
			throw new EPPDecodeException("Invalid EPPDefRegAddRemove mode of "
										 + aElement.getLocalName());
		}

		// Change Mode
		if (mode == MODE_CHANGE) {
			// Registrant
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

			//AdminContact
			adminContact =
				EPPUtil.decodeString(
									 aElement, EPPDefRegMapFactory.NS,
									 ELM_DEFREG_ADMINCONTACT);

			// AuthInfo
			authInfo =
				(EPPAuthInfo) EPPUtil.decodeComp(
												 aElement,
												 EPPDefRegMapFactory.NS,
												 EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO,
												 EPPAuthInfo.class);
		}

		else {
			// Statuses
			statuses =
				EPPUtil.decodeCompVector(
										 aElement, EPPDefRegMapFactory.NS,
										 ELM_STATUS, EPPDefRegStatus.class);
		}
	}

	// End EPPDefRegAddRemove.decode(Element)

	/**
	 * implements a deep <code>EPPDefRegAddRemove</code> compare.
	 *
	 * @param aObject <code>EPPDefRegAddRemove</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegAddRemove)) {
			return false;
		}

		EPPDefRegAddRemove theComp = (EPPDefRegAddRemove) aObject;

		// Mode
		if (mode != theComp.mode) {
			return false;
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
			return false;
		}

		// adminContact
		if (
			!(
					(adminContact == null) ? (theComp.adminContact == null)
											   : adminContact.equals(theComp.adminContact)
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

		// tm
		if (!((tm == null) ? (theComp.tm == null) : tm.equals(theComp.tm))) {
			return false;
		}

		// tmcountry
		if (
			!(
					(tmcountry == null) ? (theComp.tmcountry == null)
											: tmcountry.equals(theComp.tmcountry)
				)) {
			return false;
		}

		// tmdate
		if (
			!(
					(tmdate == null) ? (theComp.tmdate == null)
										 : tmdate.equals(theComp.tmdate)
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

	// End EPPDefRegAddRemove.equals(Object)

	/**
	 * Clone <code>EPPDefRegAddRemove</code>.
	 *
	 * @return clone of <code>EPPDefRegAddRemove</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegAddRemove clone = null;

		clone = (EPPDefRegAddRemove) super.clone();

		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();
		}

		if (adminContact != null) {
			clone.adminContact = adminContact;
		}

		if (registrant != null) {
			clone.registrant = registrant;
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPDefRegAddRemove.clone()

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

	// End EPPDefRegAddRemove.toString()

	/**
	 * Get authorization information for the change mode
	 *
	 * @return com.verisign.epp.codec.defReg.EPPDefRegAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDefRegAddRemove.getAuthInfo()

	/**
	 * Get registrant for the change mode
	 *
	 * @return java.lang.String
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPDefRegAddRemove.getRegistrant()

	/**
	 * Get the trademark informnation.
	 *
	 * @return DefReg tradeMark
	 */
	public String getTm() {
		return tm;
	}

	// End EPPDefRegAddRemove.getTm()

	/**
	 * Set the trademark informnation.
	 *
	 * @param aTm DefReg registrant
	 */
	public void setTm(String aTm) {
		tm = aTm;
	}

	// End EPPDefRegAddRemove.setTm(String)

	/**
	 * Get the trademark country informnation.
	 *
	 * @return DefReg trademark country
	 */
	public String getTmCountry() {
		return tmcountry;
	}

	// End EPPDefRegAddRemove.getTmCountry()

	/**
	 * Set the trademark country informnation.
	 *
	 * @param aTmCountry DefReg    trademark country
	 */
	public void setTmCountry(String aTmCountry) {
		tmcountry = aTmCountry;
	}

	// End EPPDefRegAddRemove.setTmCountry(String)

	/**
	 * Get the trademark date informnation.
	 *
	 * @return DefReg trademark date
	 */
	public Date getTmDate() {
		return tmdate;
	}

	// End EPPDefRegAddRemove.getTmDate()

	/**
	 * Set the trademark date informnation.
	 *
	 * @param aTmDate DefReg    trademark date
	 */
	public void setTmDate(Date aTmDate) {
		tmdate = aTmDate;
	}

	// End EPPDefRegAddRemove.setTmDate(String)

	/**
	 * Get AdminContact Address
	 *
	 * @return java.lang.String
	 */
	public String getAdminContact() {
		return adminContact;
	}

	// End EPPDefRegAddRemove.getAdminContact()

	/**
	 * Set authorization information for the change mode
	 *
	 * @param newAuthInfo com.verisign.epp.codec.defReg.EPPDefRegAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;

			authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
		}
	}

	// End EPPDefRegAddRemove.setAuthInfo(EPPAuthInfo)

	/**
	 * Set registrant for the change mode
	 *
	 * @param newRegistrant java.lang.String
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPDefRegAddRemove.setRegistrant(String)

	/**
	 * Set forwardTo Address  for the change mode
	 *
	 * @param newAdminContact java.lang.String
	 */
	public void setAdminContact(String newAdminContact) {
		adminContact = newAdminContact;
	}

	// End EPPDefRegAddRemove.setAdminContact(String)

	/**
	 * Gets the mode of <code>EPPDefRegAddRemove</code>.  There are two valid
	 * modes <code>EPPDefRegAddRemove.MODE_ADD</code> and
	 * <code>EPPDefRegAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPDefRegAddRemove.MODE_NONE</code>.
	 *
	 * @return One of the <code>EPPDefRegAddRemove_MODE</code> constants.
	 */
	short getMode() {
		return mode;
	}

	// End EPPDefRegAddRemove.getMode()

	/**
	 * Sets the mode of <code>EPPDefRegAddRemove</code>.  There are two valid
	 * modes <code>EPPDefRegAddRemove.MODE_ADD</code> and
	 * <code>EPPDefRegAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPDefRegAddRemove.MODE_NONE</code>
	 *
	 * @param aMode <code>EPPDefRegAddRemove.MODE_ADD</code> or
	 * 		  <code>EPPDefRegAddRemove.MODE_REMOVE</code>.
	 */
	void setMode(short aMode) {
		mode = aMode;
	}

	// End EPPDefRegAddRemove.setMode(short)

	/**
	 * Gets the statuses to add or remove.  The
	 * <code>EPPDefRegStatus.STATUS_</code>     constants can be used for the
	 * statuses.
	 *
	 * @return Vector of status <code>String</code> instances.
	 */
	public Vector getStatuses() {
		return statuses;
	}

	// End EPPDefRegAddRemove.getStatuses()

	/**
	 * Sets the statuses to add or remove.  The
	 * <code>EPPDefRegStatus.STATUS_</code>     constants can be used for the
	 * statuses.
	 *
	 * @param aStatuses Vector of status <code>String</code> instances.
	 */
	public void setStatuses(Vector aStatuses) {
		statuses = aStatuses;
	}
}
