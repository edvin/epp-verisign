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

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Text;

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
 * Represents a &lt;contact:infData&gt; response to an
 * <code>EPPContactInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;contact:infData&gt; element that identifies the contact namespace
 * and the location of the contact schema. The &lt;contact:infData&gt; element
 * SHALL contain the following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier of
 * the contact object. Use <code>getId</code> and <code>setId</code> to get
 * and set the elements.
 * </li>
 * <li>
 * One or more &lt;contact:status&gt; elements that describe the status of the
 * contact object. Use <code>getStatuses</code> and <code>setStatuses</code>
 * to get and set the elements.
 * </li>
 * <li>
 * A &lt;contact:postalInfo&gt; element that contains the postal contacts. Use
 * <code>getPostalInfo</code>, <code>addPostalInfo</code> and
 * <code>setPostalInfo</code> to get, add and set the elements.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:voice&gt; element that contains the contact's voice
 * telephone number. Use <code>getVoice</code> and <code>setVoice</code> to
 * get and set the elements.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:fax&gt; element that contains the contact's
 * facsimile telephone number. Use <code>getFax</code> and <code>setFax</code>
 * to get and set the elements.
 * </li>
 * <li>
 * A &lt;contact:email&gt; element that contains the contact's e-mail address.
 * Use <code>getEmail</code> and <code>setEmail</code> to get and set the
 * elements.
 * </li>
 * <li>
 * A &lt;contact:clID&gt; element that contains the identifier of the
 * sponsoring client.  The sponsoring client is the client that has
 * administrative privileges to manage the object. Use
 * <code>getClientId</code> and <code>setClientId</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;contact:crID&gt; element that contains the identifier of the client
 * that created the contact name. Use <code>getCreatedBy</code> and
 * <code>setCreatedBy</code>to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:crDate&gt; element that contains the date and time of contact
 * creation. Use <code>getCreatedDate</code> and <code>setCreatedDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:upID&gt; element that contains the identifier of the client
 * that last updated the contact name. This element MUST NOT be present if the
 * contact has never been modified. Use <code>getLastUpdatedBy</code> and
 * <code>setLastUpdatedBy</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:upDate&gt; element that contains the date and time of the most
 * recent contact modification.  This element MUST NOT be present if the
 * contact has never been modified.  Use <code>getLastUpdatedDate</code> and
 * <code>setLastUpdatedDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:trDate&gt; element that contains the date and time of the most
 * recent successful transfer.  This element MUST NOT be provided if the
 * contact has never been transferred.  Use <code>getLastTransferDate</code>
 * and <code>setLastTransferDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:authInfo&gt; element that contains authorization
 * information associated with the contact object.  This element MUST NOT be
 * provided if the querying client is not the current sponsoring client. Use
 * <code>getAuthInfo</code> and <code>setAuthInfo</code> to get and set the
 * element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:disclose&gt; element that contains disclose
 * information associated with the contact object. Use
 * <code>getDisclose</code> and <code>setDisclose</code> to get and set the
 * element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactInfoCmd
 */
public class EPPContactInfoResp extends EPPResponse {
	/** XML Element Name of <code>EPPContactInfoResp</code> root element. */
	final static String ELM_NAME = "contact:infData";

	/** XML tag name for the <code>clientId</code> element. */
	private final static String ELM_CLIENT_ID = "contact:clID";

	/** XML tag name for the <code>crID</code> element. */
	private final static String ELM_CREATED_BY = "contact:crID";

	/** XML tag name for the <code>crDate</code> element. */
	private final static String ELM_CREATED_DATE = "contact:crDate";

	/** XML tag name for the <code>upID</code> element. */
	private final static String ELM_LAST_UPDATED_BY = "contact:upID";

	/** XML tag name for the <code>upDate</code> element. */
	private final static String ELM_LAST_UPDATED_DATE = "contact:upDate";

	/** XML tag name for the <code>trDate</code> element. */
	private final static String ELM_LAST_TRANSFER_DATE = "contact:trDate";

	/** XML tag name for the <code>postalInfo</code> element. */
	private final static String ELM_CONTACT_POSTAL_INFO = "contact:postalInfo";

	/** XML tag name for the <code>authInfo</code> element. */
	private final static String ELM_CONTACT_AUTHINFO = "contact:authInfo";

	/** XML tag name for the <code>email</code> element. */
	private final static String ELM_CONTACT_EMAIL = "contact:email";

	/** XML tag name for the <code>fax</code> element. */
	private final static String ELM_CONTACT_FAX = "contact:fax";

	/** XML tag name for the <code>id</code> element. */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** XML tag name for the <code>status</code> element. */
	private final static String ELM_CONTACT_STATUSES = "contact:status";

	/** XML tag name for the <code>voice</code> element. */
	private final static String ELM_CONTACT_VOICE = "contact:voice";

	/** XML tag name for the <code>roid</code> element. */
	private final static String ELM_ROID = "contact:roid";

	/** XML tag name for the <code>disclose</code> element. */
	private final static String ELM_CONTACT_DISCLOSE = "contact:disclose";

	/**
	 * XML Attribute Name for a phone extension, which applies to  fax and
	 * voice numbers.
	 */
	private final static String ATTR_EXT = "x";

	/** identifier of sponsoring client */
	private String clientId = null;

	/** identifier of the client that created the contact */
	private String createdBy = null;

	/** date and time of contact creation */
	private Date createdDate = null;

	/** identifier of the client that last updated the contact name */
	private String lastUpdatedBy = null;

	/** date and time of the most recent contact modification */
	private Date lastUpdatedDate = null;

	/** date and time of the most recent successful transfer */
	private Date lastTransferDate = null;

	/** postal contacts */
	private java.util.Vector postalContacts = new Vector();

	/** authorization information of contact */
	private com.verisign.epp.codec.gen.EPPAuthInfo authInfo = null;

	/** disclose information of contact */
	private com.verisign.epp.codec.contact.EPPContactDisclose disclose = null;

	/** contact email */
	private java.lang.String email = null;

	/** contact fax number */
	private java.lang.String fax = null;

	/** fax extension number for contact */
	private String faxExt = null;

	/** contact id */
	private java.lang.String id = null;

	/** contact statuses */
	private java.util.Vector statuses = null;

	/** contact voice number */
	private java.lang.String voice = null;

	/** voice extension number */
	private String voiceExt = null;

	/** roid */
	private java.lang.String roid = null;

	/**
	 * <code>EPPContactInfoResp</code> default constructor.  Must call required
	 * setter methods before invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * transaction id - <code>setTransId</code>
	 * </li>
	 * <li>
	 * contact - <code>setContact</code>
	 * </li>
	 * <li>
	 * client id - <code>setClientId</code>
	 * </li>
	 * <li>
	 * postalInfo - <code>setPostalInfo</code>
	 * </li>
	 * <li>
	 * postalInfo - <code>addPostalInfo</code>
	 * </li>
	 * <li>
	 * created by - <code>setCreatedBy</code>
	 * </li>
	 * <li>
	 * created date - <code>setCreatedDate</code>
	 * </li>
	 * </ul>
	 * 
	 * <br><br> The following optional attributes can be set:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * last updated by - <code>setLastUpdatedBy</code>
	 * </li>
	 * <li>
	 * last updated date    - <code>setLastUpdatedDate</code>
	 * </li>
	 * <li>
	 * last transfer by - <code>setLastTransferBy</code>
	 * </li>
	 * <li>
	 * last transfer date    - <code>setLastTransferDate</code>
	 * </li>
	 * <li>
	 * authorization id - <code>setAuthId</code>
	 * </li>
	 * <li>
	 * disclose - <code>setDisclose</code>
	 * </li>
	 * </ul>
	 */
	public EPPContactInfoResp() {
		// Default values set in attribute definitions.
	}

	// End EPPContactInfoResp.EPPContactInfoResp()

	/**
	 * <code>EPPContactInfoResp</code> constructor that sets the required
	 * attributes with the parameters. The following optional attributes can
	 * be set:<br>
	 *
	 * @param aTransId command transaction id
	 * @param aRoid roid
	 * @param aId contact ID
	 * @param newStatuses contact statuses
	 * @param aPostalInfo postal element of contact
	 * @param aEmail contact email
	 * @param aClientId contact sponsering client identifier
	 * @param aCreatedBy identifier of the client that created the contact name
	 * @param aCreatedDate Date and time of contact creation
	 * @param aAuthInfo authorization information
	 */
	public EPPContactInfoResp(
							  EPPTransId aTransId, String aRoid, String aId,
							  Vector newStatuses,
							  EPPContactPostalDefinition aPostalInfo,
							  String aEmail, String aClientId, String aCreatedBy,
							  Date aCreatedDate, EPPAuthInfo aAuthInfo) {
		super(aTransId);

		id			 = aId;
		roid		 = aRoid;
		statuses     = newStatuses;
		postalContacts.add(aPostalInfo);
		email		    = aEmail;
		clientId	    = aClientId;
		createdBy	    = aCreatedBy;
		createdDate     = aCreatedDate;
		authInfo	    = aAuthInfo;
		authInfo.setRootName(EPPContactMapFactory.NS, EPPContactMapFactory.ELM_CONTACT_AUTHINFO);
	}

	// End EPPContactInfoResp.EPPContactInfoResp(EPPTransId, String, String, Vector, EPPContactDefinition, String, String, String, Date, EPPAuthInfo)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPContactInfoResp</code>.
	 *
	 * @return <code>EPPContactInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPContactInfoResp.getType()

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPContactInfoResp</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactInfoResp.getNamespace()

	/**
	 * Gets the contact owning Client Id.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPContactInfoResp.getClientId()

	/**
	 * Sets the contact owning Client Id.
	 *
	 * @param aClientId Client Id
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPContactInfoResp.setClientId(String)

	/**
	 * Gets Client Id that created the contact.
	 *
	 * @return Client Id if defined; <code>null</code> otherwise.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	// End EPPContactInfoResp.getCreatedBy()

	/**
	 * Sets Client Id that created the contact.
	 *
	 * @param aCreatedBy Client Id that created the contact if defined;
	 * 		  <code>null</code> otherwise.
	 */
	public void setCreatedBy(String aCreatedBy) {
		createdBy = aCreatedBy;
	}

	// End EPPContactInfoResp.setCreatedBy(Date)

	/**
	 * Gets the date and time the contact was created.
	 *
	 * @return Date and time the contact was created if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	// End EPPContactInfoResp.getCreatedDate()

	/**
	 * Sets the date and time the contact was created.
	 *
	 * @param aDate Date and time the contact was created.
	 */
	public void setCreatedDate(Date aDate) {
		createdDate = aDate;
	}

	// End EPPContactInfoResp.setCreatedDate()

	/**
	 * Gets the Client Id that last updated the contact.  This will be null if
	 * the contact has not been updated since creation.
	 *
	 * @return Client Id that last updated the contact has been updated;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	// End EPPContactInfoResp.getLastUpdatedBy()

	/**
	 * Sets the Client Id that last updated the contact.
	 *
	 * @param aLastUpdatedBy Client Id String that last updated the contact.
	 */
	public void setLastUpdatedBy(String aLastUpdatedBy) {
		lastUpdatedBy = aLastUpdatedBy;
	}

	// End EPPContactInfoResp.setLastUpdatedBy(String)

	/**
	 * Gets the date and time of the last contact update.  This will be
	 * <code>null</code>     if the contact has not been updated since
	 * creation.
	 *
	 * @return date and time of the last contact update if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	// End EPPContactInfoResp.getLastUpdatedDate()

	/**
	 * Sets the last date and time the contact was updated.
	 *
	 * @param aLastUpdatedDate Date and time of the last contact update
	 */
	public void setLastUpdatedDate(Date aLastUpdatedDate) {
		lastUpdatedDate = aLastUpdatedDate;
	}

	// End EPPContactInfoResp.setLastUpdatedDate(Date)

	/**
	 * Gets the last date and time the contact was successfully transferred.
	 *
	 * @return Date and time of the last successful transfer if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	// End EPPContactInfoResp.getLastTransferDate()

	/**
	 * Sets the last date and time the contact was successfully transferred.
	 *
	 * @param aLastTransferDate Date and time of the last successful transfer.
	 */
	public void setLastTransferDate(Date aLastTransferDate) {
		lastTransferDate = aLastTransferDate;
	}

	// End EPPContactInfoResp.setLastTransferDate(Date)

	/**
	 * Validate the state of the <code>EPPContactInfoResp</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error, throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (id == null) {
			throw new EPPCodecException("required attribute id is not set");
		}

		if (roid == null) {
			throw new EPPCodecException("required attribute roid is not set");
		}

		if (statuses == null) {
			throw new EPPCodecException("required attribute contact statuses is not set");
		}

		if ((postalContacts == null) || (postalContacts.size() == 0)) {
			throw new EPPCodecException("required attribute postalContacts is not set");
		}

		if (email == null) {
			throw new EPPCodecException("required attribute contact email is not set");
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

	// End EPPContactInfoResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactInfoResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the EPPContactPingMap
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPContactPingMap
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		Element currElm = null;
		Text    currVal = null;

		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPContactInfoResp.doEncode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// id
		EPPUtil.encodeString(
							 aDocument, root, id, EPPContactMapFactory.NS,
							 ELM_CONTACT_ID);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPContactMapFactory.NS,
							 ELM_ROID);

		// statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		// postalContacts
		EPPUtil.encodeCompVector(aDocument, root, postalContacts);

		// voice
		if (voice != null) {
			currElm =
				aDocument.createElementNS(
										  EPPContactMapFactory.NS,
										  ELM_CONTACT_VOICE);
			currVal = aDocument.createTextNode(voice);

			// voiceExt
			if (voiceExt != null) {
				currElm.setAttribute(ATTR_EXT, voiceExt);
			}

			currElm.appendChild(currVal);
			root.appendChild(currElm);
		}

		// fax
		if (fax != null) {
			currElm =
				aDocument.createElementNS(
										  EPPContactMapFactory.NS,
										  ELM_CONTACT_FAX);
			currVal = aDocument.createTextNode(fax);

			// faxExt
			if (faxExt != null) {
				currElm.setAttribute(ATTR_EXT, faxExt);
			}

			currElm.appendChild(currVal);
			root.appendChild(currElm);
		}

		// email
		EPPUtil.encodeString(
							 aDocument, root, email, EPPContactMapFactory.NS,
							 ELM_CONTACT_EMAIL);

		// Client Id
		EPPUtil.encodeString(
							 aDocument, root, clientId, EPPContactMapFactory.NS,
							 ELM_CLIENT_ID);

		// Created By
		EPPUtil.encodeString(
							 aDocument, root, createdBy, EPPContactMapFactory.NS,
							 ELM_CREATED_BY);

		// Created Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, createdDate,
								  EPPContactMapFactory.NS, ELM_CREATED_DATE);

		// Last Updated By
		EPPUtil.encodeString(
							 aDocument, root, lastUpdatedBy,
							 EPPContactMapFactory.NS, ELM_LAST_UPDATED_BY);

		// Last Updated Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, lastUpdatedDate,
								  EPPContactMapFactory.NS, ELM_LAST_UPDATED_DATE);

		// Last Transfer Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, lastTransferDate,
								  EPPContactMapFactory.NS,
								  ELM_LAST_TRANSFER_DATE);

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		// disclose
		EPPUtil.encodeComp(aDocument, root, disclose);

		return root;
	}

	// End EPPContactInfoResp.doEncode(Document)

	/**
	 * Decode the <code>EPPContactInfoResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactInfoResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		Element currElm = null;

		// id
		id     = EPPUtil.decodeString(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CONTACT_ID);

		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPContactMapFactory.NS, ELM_ROID);

		// statuses
		statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_STATUSES,
									 EPPContactStatus.class);

		// postalInfo
		postalContacts =
			EPPUtil.decodeCompVector(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_POSTAL_INFO,
									 EPPContactPostalDefinition.class);

		// voice
		voice =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CONTACT_VOICE);

		// voiceExt
		if (voice != null) {
			currElm = EPPUtil.getElementByTagNameNS(aElement, EPPContactMapFactory.NS, ELM_CONTACT_VOICE);
			voiceExt = currElm.getAttribute(ATTR_EXT);

			if (voiceExt.length() == 0) {
				voiceExt = null;
			}
		}
		else {
			voiceExt = null;
		}

		// fax
		fax = EPPUtil.decodeString(
								   aElement, EPPContactMapFactory.NS,
								   ELM_CONTACT_FAX);

		// faxExt
		if (fax != null) {
			currElm     = EPPUtil.getElementByTagNameNS(
													  aElement, EPPContactMapFactory.NS, ELM_CONTACT_FAX);
			faxExt	    = currElm.getAttribute(ATTR_EXT);

			if (faxExt.length() == 0) {
				faxExt = null;
			}
		}
		else {
			faxExt = null;
		}

		// email
		email =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CONTACT_EMAIL);

		// Client Id
		clientId =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CLIENT_ID);

		// Created By
		createdBy =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CREATED_BY);

		// Created Date
		createdDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CREATED_DATE);

		// Last Updated By
		lastUpdatedBy =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_LAST_UPDATED_BY);

		// Last Updated Date
		lastUpdatedDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPContactMapFactory.NS,
									  ELM_LAST_UPDATED_DATE);

		// Last Transfer Date
		lastTransferDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPContactMapFactory.NS,
									  ELM_LAST_TRANSFER_DATE);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPContactMapFactory.NS,
											 ELM_CONTACT_AUTHINFO,
											 EPPAuthInfo.class);

		// disclose
		disclose =
			(EPPContactDisclose) EPPUtil.decodeComp(
													aElement,
													EPPContactMapFactory.NS,
													ELM_CONTACT_DISCLOSE,
													EPPContactDisclose.class);
	}

	// End EPPContactInfoResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPContactInfoResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactInfoResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactInfoResp theComp = (EPPContactInfoResp) aObject;

		// id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			return false;
		}

		// roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
				)) {
			return false;
		}

		// statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
			return false;
		}

		// postalContacts
		if (!EPPUtil.equalVectors(postalContacts, theComp.postalContacts)) {
			return false;
		}

		// voice
		if (
			!(
					(voice == null) ? (theComp.voice == null)
										: voice.equals(theComp.voice)
				)) {
			return false;
		}

		// voiceExt
		if (
			!(
					(voiceExt == null) ? (theComp.voiceExt == null)
										   : voiceExt.equals(theComp.voiceExt)
				)) {
			return false;
		}

		// fax
		if (!((fax == null) ? (theComp.fax == null) : fax.equals(theComp.fax))) {
			return false;
		}

		// faxExt
		if (
			!(
					(faxExt == null) ? (theComp.faxExt == null)
										 : faxExt.equals(theComp.faxExt)
				)) {
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

		// Client Id
		if (
			!(
					(clientId == null) ? (theComp.clientId == null)
										   : clientId.equals(theComp.clientId)
				)) {
			return false;
		}

		// Created By
		if (
			!(
					(createdBy == null) ? (theComp.createdBy == null)
											: createdBy.equals(theComp.createdBy)
				)) {
			return false;
		}

		// Created Date
		if (
			!(
					(createdDate == null) ? (theComp.createdDate == null)
											  : createdDate.equals(theComp.createdDate)
				)) {
			return false;
		}

		// Last Updated By
		if (
			!(
					(lastUpdatedBy == null) ? (theComp.lastUpdatedBy == null)
												: lastUpdatedBy.equals(theComp.lastUpdatedBy)
				)) {
			return false;
		}

		// Last Updated Date
		if (
			!(
					(lastUpdatedDate == null) ? (
													  theComp.lastUpdatedDate == null
												  )
												  : lastUpdatedDate.equals(theComp.lastUpdatedDate)
				)) {
			return false;
		}

		// Last Transfer Date
		if (
			!(
					(lastTransferDate == null)
						? (theComp.lastTransferDate == null)
							: lastTransferDate.equals(theComp.lastTransferDate)
				)) {
			return false;
		}

		// authInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			return false;
		}

		// disclose
		if (
			!(
					(disclose == null) ? (theComp.disclose == null)
										   : disclose.equals(theComp.disclose)
				)) {
			return false;
		}

		return true;
	}

	// End EPPContactInfoResp.equals(Object)

	/**
	 * Clone <code>EPPContactInfoResp</code>.
	 *
	 * @return clone of <code>EPPContactInfoResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactInfoResp clone = (EPPContactInfoResp) super.clone();

		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();

			for (int i = 0; i < statuses.size(); i++)
				clone.statuses.setElementAt(
											((EPPContactStatus) statuses
											 .elementAt(i)).clone(), i);
		}

		if (postalContacts != null) {
			clone.postalContacts = (Vector) postalContacts.clone();

			for (int i = 0; i < postalContacts.size(); i++)
				clone.postalContacts.setElementAt(
												  ((EPPContactPostalDefinition) postalContacts
												   .elementAt(i)).clone(), i);
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (disclose != null) {
			clone.disclose = (EPPContactDisclose) disclose.clone();
		}

		return clone;
	}

	// End EPPContactInfoResp.clone()

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

	// End EPPContactInfoResp.toString()

	/**
	 * Get contact postalInfo.
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getPostalInfo() {
		return postalContacts;
	}

	//	 End EPPContactInfoResp.getPostalInfo()

	/**
	 * Set contact postalInfo.
	 *
	 * @param newPostalContacts java.util.Vector
	 */
	public void setPostalInfo(java.util.Vector newPostalContacts) {
		postalContacts = newPostalContacts;
	}

	// End EPPContactInfoResp.setPostalInfo(Vector)

	/**
	 * Adds contact postalInfo.
	 *
	 * @param newPostalInfo
	 * 		  com.verisign.epp.codec.contact.EPPContactPostalDefinition
	 */
	public void addPostalInfo(EPPContactPostalDefinition newPostalInfo) {
		// clone necessary here
		EPPContactPostalDefinition aPostalInfo = null;

		if (newPostalInfo != null) {
			try {
				aPostalInfo =
					(EPPContactPostalDefinition) newPostalInfo.clone();
			}
			 catch (CloneNotSupportedException e) {
				// Nothing needs to be done here
			}

			postalContacts.add(newPostalInfo);
		}
	}

	// End EPPContactInfoResp.addPostalInfo(EPPContactPostalDefinition)

	/**
	 * Get authorization information.
	 *
	 * @return Authorization information if defined; <code>null</code>
	 * 		   otherwise;
	 */
	public com.verisign.epp.codec.gen.EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPContactInfoResp.getAuthInfo()

	/**
	 * Set authorization information.
	 *
	 * @param newAuthInfo com.verisign.epp.codec.gen.EPPAuthInfo
	 */
	public void setAuthInfo(com.verisign.epp.codec.gen.EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPContactMapFactory.NS, EPPContactMapFactory.ELM_CONTACT_AUTHINFO);
		}
	}

	// End EPPContactInfoResp.setAuthInfo(EPPAuthInfo)

	/**
	 * Get disclose information.
	 *
	 * @return Disclose information if defined; <code>null</code> otherwise;
	 */
	public com.verisign.epp.codec.contact.EPPContactDisclose getDisclose() {
		return disclose;
	}

	// End EPPContactInfoResp.getDisclose()

	/**
	 * Set disclose information.
	 *
	 * @param newDisclose com.verisign.epp.codec.gen.EPPContactDisclose
	 */
	public void setDisclose(com.verisign.epp.codec.contact.EPPContactDisclose newDisclose) {
		if (newDisclose != null) {
			disclose = newDisclose;
			disclose.setRootName(ELM_CONTACT_DISCLOSE);
		}
	}

	// End EPPContactInfoResp.setDisclose(EPPContactDisclose)

	/**
	 * Get contact email.
	 *
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	// End EPPContactInfoResp.getEmail()

	/**
	 * Set contact email.
	 *
	 * @param newEmail String
	 */
	public void setEmail(String newEmail) {
		email = newEmail;
	}

	// End EPPContactInfoResp.setEmail(String)

	/**
	 * Get contact fax number.
	 *
	 * @return String
	 */
	public String getFax() {
		return fax;
	}

	// End EPPContactInfoResp.getFax()

	/**
	 * Set contact fax number.
	 *
	 * @param newFax String
	 */
	public void setFax(String newFax) {
		fax = newFax;
	}

	// End EPPContactInfoResp.setFax(String)

	/**
	 * Get fax number extension.
	 *
	 * @return fax number extension if defined; <code>null</code> otherwise.
	 */
	public String getFaxExt() {
		return faxExt;
	}

	// End EPPContactInfoResp.getFaxExt()

	/**
	 * Set fax number extension.
	 *
	 * @param newFaxExt Fax number extension
	 */
	public void setFaxExt(String newFaxExt) {
		faxExt = newFaxExt;
	}

	// End EPPContactInfoResp.setFaxExt(String)

	/**
	 * Get contact id.
	 *
	 * @return String
	 */
	public String getId() {
		return id;
	}

	// End EPPContactInfoResp.getId()

	/**
	 * Set contact id.
	 *
	 * @param newId String
	 */
	public void setId(String newId) {
		id = newId;
	}

	// End EPPContactInfoResp.setId(String)

	/**
	 * Get contact statuses.
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getStatuses() {
		return statuses;
	}

	// End EPPContactInfoResp.getStatuses()

	/**
	 * Set contact statuses.
	 *
	 * @param newStatuses java.util.Vector
	 */
	public void setStatuses(java.util.Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPContactInfoResp.setStatuses(Vector)

	/**
	 * Get contact voice number.
	 *
	 * @return String
	 */
	public String getVoice() {
		return voice;
	}

	// End EPPContactInfoResp.getVoice()

	/**
	 * Set contact voice number.
	 *
	 * @param newVoice String
	 */
	public void setVoice(String newVoice) {
		voice = newVoice;
	}

	// End EPPContactInfoResp.setVoice(String)

	/**
	 * Get voice number extension.
	 *
	 * @return Voice number extension if defined; <code>null</code> otherwise.
	 */
	public String getVoiceExt() {
		return voiceExt;
	}

	// End EPPContactInfoResp.getVoiceExt()

	/**
	 * Set contact voice extension.
	 *
	 * @param newVoiceExt voice extension
	 */
	public void setVoiceExt(String newVoiceExt) {
		voiceExt = newVoiceExt;
	}

	// End EPPContactInfoResp.setVoiceExt(String)

	/**
	 * Get roid.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRoid() {
		return roid;
	}

	// End EPPContactInfoResp.getRoid()

	/**
	 * Set roid.
	 *
	 * @param newRoid java.lang.String
	 */
	public void setRoid(String newRoid) {
		roid = newRoid;
	}

	// End EPPContactInfoResp.setRoid(String)
}
