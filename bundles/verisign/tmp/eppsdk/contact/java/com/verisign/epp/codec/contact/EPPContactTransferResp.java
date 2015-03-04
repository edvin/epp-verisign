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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a &lt;contact:trnData&gt; response to
 * <code>EPPContactTransferCmd</code>. When a &lt;transfer&gt; query command
 * has been processed successfully, the EPP &lt;resData&gt; element MUST
 * contain a child &lt;contact:trnData&gt; element that identifies the contact
 * namespace and the location of the contact schema. The
 * &lt;contact:trnData&gt; element SHALL contain the following child elements:
 * <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier for
 * the queried contact.  Use <code>getId</code> and <code>setId</code> to get
 * and     set the element.
 * </li>
 * <li>
 * A &lt;contact:trStatus&gt; element that contains the state of the most
 * recent transfer request. Valid values are "PENDING", "APPROVED",
 * "REJECTED", "AUTO-APPROVED", "AUTO-REJECTED", and "CANCELLED". Use
 * <code>getTrStatus</code> and <code>setTrStatus</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;contact:reID&gt; element that contains the identifier of     the
 * client that initiated the transfer request.  Use
 * <code>getRequestClient</code>     and <code>setRequestClient</code> to get
 * and set the element.
 * </li>
 * <li>
 * A &lt;contact:acID&gt; element that contains the identifier of the client
 * that SHOULD respond to the transfer request.  Use
 * <code>getActionClient</code>     and <code>setActionClient</code> to get
 * and set the element.
 * </li>
 * <li>
 * A &lt;contact:reDate&gt; element that contains the date and time that the
 * transfer was requested.  Use <code>getRequestDate</code> and
 * <code>setRequestDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:acDate&gt; element that contains the date and time of a
 * required or completed response.  For a PENDING request, the value
 * identifies the date and time by which a response is required before an
 * automated response action MUST be taken by the server.  For all other
 * status types, the value identifies the date and time when the request was
 * completed.  Use <code>getActionDate</code> and <code>setActionDate</code>
 * to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactTransferCmd
 */
public class EPPContactTransferResp extends EPPResponse {
	/** XML Element Name of <code>EPPContactTransferResp</code> root element. */
	final static String ELM_NAME = "contact:trnData";

	/** XML tag name for the <code>requestClient</code> attribute. */
	private final static String ELM_REQUEST_CLIENT = "contact:reID";

	/** XML tag name for the <code>actionClient</code> attribute. */
	private final static String ELM_ACTION_CLIENT = "contact:acID";

	/** XML tag name for the <code>transferStatus</code> attribute. */
	private final static String ELM_TRANSFER_STATUS = "contact:trStatus";

	/** XML tag name for the <code>requestDate</code> attribute. */
	private final static String ELM_REQUEST_DATE = "contact:reDate";

	/** XML tag name for the <code>actionDate</code> attribute. */
	private final static String ELM_ACTION_DATE = "contact:acDate";

	/** XML tag name for the <code>id</code> attribute. */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** The fully qualified contact id. */
	private String id = null;

	/** The identifier of the client that initiated the transfer request. */
	private String requestClient = null;

	/**
	 * The identifier of the client that SHOULD respond to the transfer
	 * request.
	 */
	private String actionClient = null;

	/** The date and time that the transfer was requested. */
	private Date requestDate = null;

	/**
	 * The date and time of a required or completed response.  For a
	 * STATUS_PENDING     request, the value identifies the date and time by
	 * which a response is required     before an automated response action
	 * MUST be taken by the server.  For all other     status types, the value
	 * identifies the date and time when the request was     completed.
	 */
	private Date actionDate = null;

	/** Transfer status. */
	private java.lang.String trStatus = null;

	/**
	 * <code>EPPContactTransferResp</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * id - <code>setId</code>
	 * </li>
	 * <li>
	 * request client - <code>setRequestClient</code>
	 * </li>
	 * <li>
	 * action client - <code>setActionClient</code>
	 * </li>
	 * <li>
	 * transfer status - <code>setTransferStatus</code>
	 * </li>
	 * <li>
	 * request date - <code>setReqeustDate</code>
	 * </li>
	 * <li>
	 * action date - <code>setActionDate</code>
	 * </li>
	 * <li>
	 * transaction id - <code>setTransId</code>
	 * </li>
	 * </ul>
	 */
	public EPPContactTransferResp() {
		// Values set in attribute definition.
	}

	// End EPPContactTransferResp.EPPContactTransferResp()

	/**
	 * <code>EPPContactTransferResp</code> which takes the id of contact.  All
	 * other     required attributes need to be set using the setter methods,
	 * which include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * request client - <code>setRequestClient</code>
	 * </li>
	 * <li>
	 * action client - <code>setActionClient</code>
	 * </li>
	 * <li>
	 * transfer status - <code>setTransferStatus</code>
	 * </li>
	 * <li>
	 * request date - <code>setReqeustDate</code>
	 * </li>
	 * <li>
	 * action date - <code>setActionDate</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aId Contact id
	 */
	public EPPContactTransferResp(EPPTransId aTransId, String aId) {
		super(aTransId);
		id = aId;
	}

	// End EPPContactTransferResp.EPPContactTransferResp(EPPTransId, String)

	/**
	 * <code>EPPContactTransferResp</code> which takes the id of contact.  All
	 * other     required attributes need to be set using the setter methods,
	 * which include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * request client - <code>setRequestClient</code>
	 * </li>
	 * <li>
	 * action client - <code>setActionClient</code>
	 * </li>
	 * <li>
	 * transfer status - <code>setTransferStatus</code>
	 * </li>
	 * <li>
	 * request date - <code>setReqeustDate</code>
	 * </li>
	 * <li>
	 * action date - <code>setActionDate</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aId Contact
	 * @param aStatus Transfer status
	 */
	public EPPContactTransferResp(
								  EPPTransId aTransId, String aId,
								  String aStatus) {
		super(aTransId);

		id			 = aId;
		trStatus     = aStatus;
	}

	// End EPPContactTransferResp.EPPContactTransferResp(EPPTransId, String, String)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPContactTransferResp</code>.
	 *
	 * @return <code>EPPContactTransferResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPContactTransferResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactTransferResp</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactTransferResp.getNamespace()

	/**
	 * Validate the state of the <code>EPPContactTransferResp</code> instance.
	 * A valid state means that all of the required attributes have been set.
	 * If validateState     returns without an exception, the state is valid.
	 * If the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the id of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (id == null) {
			throw new EPPCodecException("id required attribute is not set");
		}

		if (requestClient == null) {
			throw new EPPCodecException("requestClient required attribute is not set");
		}

		if (actionClient == null) {
			throw new EPPCodecException("actionClient required attribute is not set");
		}

		if (trStatus == null) {
			throw new EPPCodecException("trStatus required attribute is not set");
		}

		if (requestDate == null) {
			throw new EPPCodecException("requestDate required attribute is not set");
		}

		if (actionDate == null) {
			throw new EPPCodecException("actionDate required attribute is not set");
		}
	}

	// End EPPContactTransferResp.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactTransferResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPContactTransferResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactTransferResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPContactTransferResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// Id
		EPPUtil.encodeString(
							 aDocument, root, id, EPPContactMapFactory.NS,
							 ELM_CONTACT_ID);

		// Transfer Status
		EPPUtil.encodeString(
							 aDocument, root, trStatus, EPPContactMapFactory.NS,
							 ELM_TRANSFER_STATUS);

		// Request Client
		EPPUtil.encodeString(
							 aDocument, root, requestClient,
							 EPPContactMapFactory.NS, ELM_REQUEST_CLIENT);

		// Request Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, requestDate,
								  EPPContactMapFactory.NS, ELM_REQUEST_DATE);

		// Action Client
		EPPUtil.encodeString(
							 aDocument, root, actionClient,
							 EPPContactMapFactory.NS, ELM_ACTION_CLIENT);

		// Action Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, actionDate,
								  EPPContactMapFactory.NS, ELM_ACTION_DATE);

		return root;
	}

	// End EPPContactTransferResp.doEncode(Document)

	/**
	 * Decode the <code>EPPContactTransferResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactTransferResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Id
		id     = EPPUtil.decodeString(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CONTACT_ID);

		// Transfer Status
		trStatus =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_TRANSFER_STATUS);

		// Request Client
		requestClient =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_REQUEST_CLIENT);

		// Request Date
		requestDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPContactMapFactory.NS,
									  ELM_REQUEST_DATE);

		// Action Client
		actionClient =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_ACTION_CLIENT);

		// Action Date
		actionDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPContactMapFactory.NS,
									  ELM_ACTION_DATE);
	}

	// End EPPContactTransferResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPContactTransferResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactTransferResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactTransferResp theComp = (EPPContactTransferResp) aObject;

		// Id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			return false;
		}

		// Transfer Status
		if (
			!(
					(trStatus == null) ? (theComp.trStatus == null)
										   : trStatus.equals(theComp.trStatus)
				)) {
			return false;
		}

		// Request Client
		if (
			!(
					(requestClient == null) ? (theComp.requestClient == null)
												: requestClient.equals(theComp.requestClient)
				)) {
			return false;
		}

		// Request Date
		if (
			!(
					(requestDate == null) ? (theComp.requestDate == null)
											  : requestDate.equals(theComp.requestDate)
				)) {
			return false;
		}

		// Action Client
		if (
			!(
					(actionClient == null) ? (theComp.actionClient == null)
											   : actionClient.equals(theComp.actionClient)
				)) {
			return false;
		}

		// Action Date
		if (
			!(
					(actionDate == null) ? (theComp.actionDate == null)
											 : actionDate.equals(theComp.actionDate)
				)) {
			return false;
		}

		return true;
	}

	// End EPPContactTransferResp.equals(Object)

	/**
	 * Clone <code>EPPContactTransferResp</code>.
	 *
	 * @return clone of <code>EPPContactTransferResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactTransferResp clone = (EPPContactTransferResp) super.clone();

		return clone;
	}

	// End EPPContactTransferResp.clone()

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

	// End EPPContactTransferResp.toString()

	/**
	 * Gets the contact id
	 *
	 * @return Contact Id if set; <code>null</code> otherwise.
	 */
	public String getId() {
		return id;
	}

	// End EPPContactTransferResp.getId()

	/**
	 * Sets the contact id.
	 *
	 * @param aId Contact Id
	 */
	public void setId(String aId) {
		id = aId;
	}

	// End EPPContactTransferResp.setId(String)

	/**
	 * Gets the identifier of the client that initiated the transfer request.
	 *
	 * @return The Request Client Id <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRequestClient() {
		return requestClient;
	}

	// End EPPContactTransferResp.getRequestClient()

	/**
	 * Sets the identifier of the client that initiated the transfer request.
	 *
	 * @param aRequestClient The Request Client Id <code>String</code>
	 */
	public void setRequestClient(String aRequestClient) {
		requestClient = aRequestClient;
	}

	// End EPPContactTransferResp.setRequestClient(String)

	/**
	 * Gets the identifier of the client that SHOULD respond to the transfer
	 * request.
	 *
	 * @return The Request Client Id <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getActionClient() {
		return actionClient;
	}

	// End EPPContactTransferResp.getActionClient()

	/**
	 * Sets the identifier of the client that SHOULD respond to the transfer
	 * request.
	 *
	 * @param aActionClient The Action Client Id <code>String</code>
	 */
	public void setActionClient(String aActionClient) {
		actionClient = aActionClient;
	}

	// End EPPContactTransferResp.setActionClient(String)

	/**
	 * Gets the date and time that the transfer was requested.
	 *
	 * @return The request date and time if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	// End EPPContactTransferResp.getRequestDate()

	/**
	 * Sets the date and time that the transfer was requested.
	 *
	 * @param aRequestDate The request date and time
	 */
	public void setRequestDate(Date aRequestDate) {
		requestDate = aRequestDate;
	}

	// End EPPContactTransferResp.setRequestDate(Date)

	/**
	 * Gets the date and time of a required or completed response.
	 *
	 * @return The required or complete response data and time if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getActionDate() {
		return actionDate;
	}

	// End EPPContactTransferResp.getActionDate()

	/**
	 * Sets the date and time of a required or completed response.
	 *
	 * @param aActionDate The required or complete response data and time.
	 */
	public void setActionDate(Date aActionDate) {
		actionDate = aActionDate;
	}

	// End EPPContactTransferResp.setActionDate(Date)

	/**
	 * Get transfer status.  This should be one of the
	 * <code>EPPResponse.TRANSFER</code> constants.
	 *
	 * @return String
	 */
	public String getTransferStatus() {
		return trStatus;
	}

	// End EPPContactTransferResp.getTransferStatus()

	/**
	 * Set transfer status.  This should be one of the
	 * <code>EPPResponse.TRANSFER</code> constants.
	 *
	 * @param newTrStatus The transfer status String
	 * 		  (<code>EPPResponse.TRANSFER</code>)
	 */
	public void setTransferStatus(String newTrStatus) {
		trStatus = newTrStatus;
	}

	// End EPPContactTransferResp.setTransferStatus(String)
}
