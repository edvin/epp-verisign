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
 * Represents an EPP NameWatch &lt;nameWatch:transfer-data&gt; response to an
 * <code>EPPNameWatchTransferCmd</code>.     When a &lt;transfer&gt; query
 * command has been processed successfully, the EPP &lt;resData&gt; element
 * MUST contain a child &lt;nameWatch:trnData&gt; element that identifies the
 * nameWatch namespace and the location of the nameWatch schema. The
 * &lt;nameWatch:trnData&gt; element SHALL contain the following child
 * elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the fully qualified nameWatch
 * roid used in the query.  Use <code>getRoid</code> and <code>setRoid</code>
 * to get and     set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:trStatus&gt; element that contains the state of the most
 * recent transfer request.  Valid values are "PENDING", "APPROVED",
 * "REJECTED", "AUTO-APPROVED", "AUTO-REJECTED", and "CANCELLED".  Use
 * <code>getStatus</code> and <code>setStatus</code> with the
 * <code>EPPNameWatchTransferResp.STATUS_</code> constants to get and     set
 * the element.
 * </li>
 * <li>
 * A &lt;nameWatch:reID&gt; element that contains the identifier of     the
 * client that initiated the transfer request.  Use
 * <code>getRequestClient</code>     and <code>setRequestClient</code> to get
 * and set the element.     and <code>setActionClient</code> to get and set
 * the element.
 * </li>
 * <li>
 * A &lt;nameWatch:reDate&gt; element that contains the date and time that the
 * transfer was requested.  Use <code>getRequestDate</code> and
 * <code>setRequestDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:acID&gt; element that contains the identifier of the client
 * that SHOULD respond to the transfer request.  Use
 * <code>getActionClient</code>
 * </li>
 * <li>
 * A &lt;nameWatch:acDate&gt; element that contains the date and time of a
 * required or completed response.  For a PENDING request, the value
 * identifies the date and time by which a response is required before an
 * automated response action MUST be taken by the server.  For all other
 * status types, the value identifies the date and time when the request was
 * completed.  Use <code>getActionDate</code> and <code>setActionDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:exDate&gt; element that contains the end     of
 * the nameWatch's validity period if the &lt;transfer&gt; command caused or
 * causes a change in the validity period.  Use <code>getExpirationDate</code>
 * and <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchTransferCmd
 */
public class EPPNameWatchTransferResp extends EPPResponse {
	/**
	 * XML Element Name of <code>EPPNameWatchTransferResp</code> root element.
	 */
	final static String ELM_NAME = "nameWatch:trnData";

	/** XML tag name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** XML tag name for the <code>transferStatus</code> attribute. */
	private final static String ELM_TRANSFER_STATUS = "nameWatch:trStatus";

	/** XML tag name for the <code>requestClient</code> attribute. */
	private final static String ELM_REQUEST_CLIENT = "nameWatch:reID";

	/** XML tag name for the <code>requestDate</code> attribute. */
	private final static String ELM_REQUEST_DATE = "nameWatch:reDate";

	/** XML tag name for the <code>actionClient</code> attribute. */
	private final static String ELM_ACTION_CLIENT = "nameWatch:acID";

	/** XML tag name for the <code>actionDate</code> attribute. */
	private final static String ELM_ACTION_DATE = "nameWatch:acDate";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "nameWatch:exDate";

	/** The fully qualified nameWatch name. */
	private String roid = null;

	/** The identifier of the client that initiated the transfer request. */
	private String requestClient = null;

	/**
	 * The identifier of the client that SHOULD respond to the transfer
	 * request.
	 */
	private String actionClient = null;

	/**
	 * The state of the most recent transfer request.  This should be one of
	 * the     <code>EPPNameWatchTransferResp.STATUS</code> constants.
	 */
	private String transferStatus = null;

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

	/**
	 * An optional attribute that contains the end of the nameWatch's validity
	 * period     if the transfer command caused or causes a change in the
	 * validity period.
	 */
	private Date expirationDate = null;

	/**
	 * <code>EPPNameWatchTransferResp</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * transaction id - <code>setTransId</code>
	 * </li>
	 * <li>
	 * roid - <code>setRoid</code>
	 * </li>
	 * <li>
	 * transfer status - <code>setTransferStatus</code>
	 * </li>
	 * <li>
	 * request client - <code>setRequestClient</code>
	 * </li>
	 * <li>
	 * request date - <code>setReqeustDate</code>
	 * </li>
	 * <li>
	 * action client - <code>setActionClient</code>
	 * </li>
	 * <li>
	 * action date - <code>setActionDate</code>
	 * </li>
	 * </ul>
	 */
	public EPPNameWatchTransferResp() {
		// Values set in attribute definition.
	}

	// End EPPNameWatchTransferResp.EPPNameWatchTransferResp()

	/**
	 * <code>EPPNameWatchTransferResp</code> which takes the name of nameWatch.
	 * All other     required attributes need to be set using the setter
	 * methods, which include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * transfer status - <code>setTransferStatus</code>
	 * </li>
	 * <li>
	 * request client - <code>setRequestClient</code>
	 * </li>
	 * <li>
	 * request date - <code>setReqeustDate</code>
	 * </li>
	 * <li>
	 * action client - <code>setActionClient</code>
	 * </li>
	 * <li>
	 * action date - <code>setActionDate</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aRoid NameWatch roid
	 */
	public EPPNameWatchTransferResp(EPPTransId aTransId, String aRoid) {
		super(aTransId);
		roid = aRoid;
	}

	// End EPPNameWatchTransferResp.EPPNameWatchTransferResp(EPPTransId, String)

	/**
	 * <code>EPPNameWatchTransferResp</code> which takes the name of nameWatch.
	 *
	 * @param aTransId DOCUMENT ME!
	 * @param aRoid DOCUMENT ME!
	 * @param aTransferStatus DOCUMENT ME!
	 * @param aRequestClient DOCUMENT ME!
	 * @param aRequestDate DOCUMENT ME!
	 * @param aActionClient DOCUMENT ME!
	 * @param aActionDate DOCUMENT ME!
	 */
	public EPPNameWatchTransferResp(
									EPPTransId aTransId, String aRoid,
									String aTransferStatus,
									String aRequestClient, Date aRequestDate,
									String aActionClient, Date aActionDate) {
		super(aTransId);

		roid			   = aRoid;
		transferStatus     = aTransferStatus;
		requestClient	   = aRequestClient;
		requestDate		   = aRequestDate;
		actionClient	   = aActionClient;
		actionDate		   = aActionDate;
	}

	// End EPPNameWatchTransferResp.EPPNameWatchTransferResp(EPPTransId, String, String, String, Date, String, Date)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPNameWatchTransferResp</code>.
	 *
	 * @return <code>EPPNameWatchTransferResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPNameWatchTransferResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchTransferResp</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchTransferResp.getNamespace()

	/**
	 * Validate the state of the <code>EPPNameWatchTransferResp</code>
	 * instance.  A valid state means that all of the required attributes have
	 * been set.  If validateState     returns without an exception, the state
	 * is valid.  If the state is not     valid, the EPPCodecException will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}

		if (transferStatus == null) {
			throw new EPPCodecException("transferStatus required attribute is not set");
		}

		if (requestClient == null) {
			throw new EPPCodecException("requestClient required attribute is not set");
		}

		if (requestDate == null) {
			throw new EPPCodecException("requestDate required attribute is not set");
		}

		if (actionClient == null) {
			throw new EPPCodecException("actionClient required attribute is not set");
		}

		if (actionDate == null) {
			throw new EPPCodecException("actionDate required attribute is not set");
		}
	}

	// End EPPNameWatchTransferResp.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchTransferResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchTransferResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchTransferResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPNameWatchTransferResp.encode: "
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

		// Transfer Status
		EPPUtil.encodeString(
							 aDocument, root, transferStatus,
							 EPPNameWatchMapFactory.NS, ELM_TRANSFER_STATUS);

		// Request Client
		EPPUtil.encodeString(
							 aDocument, root, requestClient,
							 EPPNameWatchMapFactory.NS, ELM_REQUEST_CLIENT);

		// Request Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, requestDate,
								  EPPNameWatchMapFactory.NS, ELM_REQUEST_DATE);

		// Action Client
		EPPUtil.encodeString(
							 aDocument, root, actionClient,
							 EPPNameWatchMapFactory.NS, ELM_ACTION_CLIENT);

		// Action Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, actionDate,
								  EPPNameWatchMapFactory.NS, ELM_ACTION_DATE);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPNameWatchMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPNameWatchTransferResp.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchTransferResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchTransferResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		// Transfer Status
		transferStatus =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_TRANSFER_STATUS);

		// Request Client
		requestClient =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_REQUEST_CLIENT);

		// Request Date
		requestDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_REQUEST_DATE);

		// Action Client
		actionClient =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_ACTION_CLIENT);

		// Action Date
		actionDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_ACTION_DATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_EXPIRATION_DATE);

		//Validate States
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPNameWatchCreateCmd.decode: "
										 + e);
		}
	}

	// End EPPNameWatchTransferResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPNameWatchTransferResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchTransferResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchTransferResp theComp = (EPPNameWatchTransferResp) aObject;

		// roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
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

		// Action Client
		if (
			!(
					(actionClient == null) ? (theComp.actionClient == null)
											   : actionClient.equals(theComp.actionClient)
				)) {
			return false;
		}

		// Transfer Status
		if (
			!(
					(transferStatus == null) ? (
													 theComp.transferStatus == null
												 )
												 : transferStatus.equals(theComp.transferStatus)
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

		// Action Date
		if (
			!(
					(actionDate == null) ? (theComp.actionDate == null)
											 : actionDate.equals(theComp.actionDate)
				)) {
			return false;
		}

		// Expiration Date
		if (
			!(
					(expirationDate == null) ? (
													 theComp.expirationDate == null
												 )
												 : expirationDate.equals(theComp.expirationDate)
				)) {
			return false;
		}

		return true;
	}

	// End EPPNameWatchTransferResp.equals(Object)

	/**
	 * Clone <code>EPPNameWatchTransferResp</code>.
	 *
	 * @return clone of <code>EPPNameWatchTransferResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchTransferResp clone =
			(EPPNameWatchTransferResp) super.clone();

		return clone;
	}

	// End EPPNameWatchTransferResp.clone()

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

	// End EPPNameWatchTransferResp.toString()

	/**
	 * Gets the nameWatch roid
	 *
	 * @return NameWatch Roid if set; <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchTransferResp.getName()

	/**
	 * Sets the nameWatch roid.
	 *
	 * @param aRoid NameWatch Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchTransferResp.setName(String)

	/**
	 * Gets the identifier of the client that initiated the transfer request.
	 *
	 * @return The Request Client Id <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRequestClient() {
		return requestClient;
	}

	// End EPPNameWatchTransferResp.getRequestClient()

	/**
	 * Sets the identifier of the client that initiated the transfer request.
	 *
	 * @param aRequestClient The Request Client Id <code>String</code>
	 */
	public void setRequestClient(String aRequestClient) {
		requestClient = aRequestClient;
	}

	// End EPPNameWatchTransferResp.setRequestClient(String)

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

	// End EPPNameWatchTransferResp.getActionClient()

	/**
	 * Sets the identifier of the client that SHOULD respond to the transfer
	 * request.
	 *
	 * @param aActionClient The Action Client Id <code>String</code>
	 */
	public void setActionClient(String aActionClient) {
		actionClient = aActionClient;
	}

	// End EPPNameWatchTransferResp.setActionClient(String)

	/**
	 * Gets the state of the most recent transfer request. This should be one
	 * of the     <code>EPPResponse.TRANSFER</code> constants.
	 *
	 * @return The transfer status <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getTransferStatus() {
		return transferStatus;
	}

	// End EPPNameWatchTransferResp.getTransferStatus()

	/**
	 * Sets the state of the most recent transfer request. This should be one
	 * of the     <code>EPPResponse.TRANSFER</code> constants.
	 *
	 * @param aTransferStatus The transfer status String
	 * 		  (<code>EPPResponse.TRANSFER</code>)
	 */
	public void setTransferStatus(String aTransferStatus) {
		transferStatus = aTransferStatus;
	}

	// End EPPNameWatchTransferResp.setTransferStatus(String)

	/**
	 * Gets the date and time that the transfer was requested.
	 *
	 * @return The request date and time if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	// End EPPNameWatchTransferResp.getRequestDate()

	/**
	 * Sets the date and time that the transfer was requested.
	 *
	 * @param aRequestDate The request date and time
	 */
	public void setRequestDate(Date aRequestDate) {
		requestDate = aRequestDate;
	}

	// End EPPNameWatchTransferResp.setRequestDate(Date)

	/**
	 * Gets the date and time of a required or completed response.
	 *
	 * @return The required or complete response data and time if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getActionDate() {
		return actionDate;
	}

	// End EPPNameWatchTransferResp.getActionDate()

	/**
	 * Sets the date and time of a required or completed response.
	 *
	 * @param aActionDate The required or complete response data and time.
	 */
	public void setActionDate(Date aActionDate) {
		actionDate = aActionDate;
	}

	// End EPPNameWatchTransferResp.setActionDate(Date)

	/**
	 * Gets the optional attribute that contains the end of the nameWatch's
	 * validity period     if the transfer command caused or causes a change
	 * in the validity period.
	 *
	 * @return Transfer expiration data and time if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPNameWatchTransferResp.getExpirationDate()

	/**
	 * Sets the optional attribute that contains the end of the nameWatch's
	 * validity period     if the transfer command caused or causes a change
	 * in the validity period.
	 *
	 * @param aExpirationDate Transfer expiration data and time.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPNameWatchTransferResp.setExpirationDate(Date)
}
