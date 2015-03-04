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
 * Represents an EPP Domain &lt;domain:transfer-data&gt; response to an
 * <code>EPPDomainTransferCmd</code>.     When a &lt;transfer&gt; query
 * command has been processed successfully, the EPP &lt;resData&gt; element
 * MUST contain a child &lt;domain:trnData&gt; element that identifies the
 * domain namespace and the location of the domain schema. The
 * &lt;domain:trnData&gt; element SHALL contain the following child elements:
 * <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * used in the query.  Use <code>getName</code> and <code>setName</code> to
 * get and     set the element.
 * </li>
 * <li>
 * A &lt;domain:reID&gt; element that contains the identifier of     the client
 * that initiated the transfer request.  Use <code>getRequestClient</code> and
 * <code>setRequestClient</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;domain:acID&gt; element that contains the identifier of the     client
 * that SHOULD respond to the transfer request.  Use
 * <code>getActionClient</code>     and <code>setActionClient</code> to get
 * and set the element.
 * </li>
 * <li>
 * A &lt;domain:trStatus&gt; element that contains the state of the most recent
 * transfer request.  Valid values are "PENDING", "APPROVED", "REJECTED",
 * "AUTO-APPROVED", "AUTO-REJECTED", and "CANCELLED".  Use
 * <code>getStatus</code> and <code>setStatus</code> with the
 * <code>EPPDomainTransferResp.STATUS_</code> constants to get and     set the
 * element.
 * </li>
 * <li>
 * A &lt;domain:reDate&gt; element that contains the date and time that the
 * transfer was requested.  Use <code>getRequestDate</code> and
 * <code>setRequestDate</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;domain:acDate&gt; element that contains the date and time of a
 * required or completed response.  For a PENDING request, the value
 * identifies the date and time by which a response is required before an
 * automated response action MUST be taken by the server.  For all other
 * status types, the value identifies the date and time when the request was
 * completed.  Use <code>getActionDate</code> and <code>setActionDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:exDate&gt; element that contains the end     of the
 * domain's validity period if the &lt;transfer&gt; command caused or causes a
 * change in the validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainTransferCmd
 */
public class EPPDomainTransferResp extends EPPResponse {
	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML Element Name of <code>EPPDomainTransferResp</code> root element. */
	final static String ELM_NAME = "domain:trnData";

	/** XML tag name for the <code>requestClient</code> attribute. */
	private final static String ELM_REQUEST_CLIENT = "domain:reID";

	/** XML tag name for the <code>actionClient</code> attribute. */
	private final static String ELM_ACTION_CLIENT = "domain:acID";

	/** XML tag name for the <code>transferStatus</code> attribute. */
	private final static String ELM_TRANSFER_STATUS = "domain:trStatus";

	/** XML tag name for the <code>requestDate</code> attribute. */
	private final static String ELM_REQUEST_DATE = "domain:reDate";

	/** XML tag name for the <code>actionDate</code> attribute. */
	private final static String ELM_ACTION_DATE = "domain:acDate";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "domain:exDate";

	/** The fully qualified domain name. */
	private String name = null;

	/** The identifier of the client that initiated the transfer request. */
	private String requestClient = null;

	/**
	 * The identifier of the client that SHOULD respond to the transfer
	 * request.
	 */
	private String actionClient = null;

	/**
	 * The state of the most recent transfer request.  This should be one of
	 * the     <code>EPPDomainTransferResp.STATUS</code> constants.
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
	 * An optional attribute that contains the end of the domain's validity
	 * period     if the transfer command caused or causes a change in the
	 * validity period.
	 */
	private Date expirationDate = null;

	/**
	 * <code>EPPDomainTransferResp</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * name - <code>setName</code>
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
	public EPPDomainTransferResp() {
		// Values set in attribute definition.
	}

	// End EPPDomainTransferResp.EPPDomainTransferResp()

	/**
	 * <code>EPPDomainTransferResp</code> which takes the name of domain.  All
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
	 * @param aName Domain name
	 */
	public EPPDomainTransferResp(EPPTransId aTransId, String aName) {
		super(aTransId);
		name = aName;
	}

	// End EPPDomainTransferResp.EPPDomainTransferResp(EPPTransId, String)

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPDomainTransferResp</code>.
	 *
	 * @return <code>EPPDomainTransferResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDomainTransferResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainTransferResp</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainTransferResp.getNamespace()

	/**
	 * Validate the state of the <code>EPPDomainTransferResp</code> instance. A
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

	// End EPPDomainTransferResp.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainTransferResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDomainTransferResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainTransferResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPDomainTransferResp.encode: "
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

		// Transfer Status
		EPPUtil.encodeString(
							 aDocument, root, transferStatus,
							 EPPDomainMapFactory.NS, ELM_TRANSFER_STATUS);

		// Request Client
		EPPUtil.encodeString(
							 aDocument, root, requestClient,
							 EPPDomainMapFactory.NS, ELM_REQUEST_CLIENT);

		// Request Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, requestDate,
								  EPPDomainMapFactory.NS, ELM_REQUEST_DATE);

		// Action Client
		EPPUtil.encodeString(
							 aDocument, root, actionClient,
							 EPPDomainMapFactory.NS, ELM_ACTION_CLIENT);

		// Action Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, actionDate,
								  EPPDomainMapFactory.NS, ELM_ACTION_DATE);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDomainMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPDomainTransferResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainTransferResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainTransferResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Transfer Status
		transferStatus =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_TRANSFER_STATUS);

		// Request Client
		requestClient =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_REQUEST_CLIENT);

		// Request Date
		requestDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_REQUEST_DATE);

		// Action Client
		actionClient =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_ACTION_CLIENT);

		// Action Date
		actionDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_ACTION_DATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_EXPIRATION_DATE);
	}

	// End EPPDomainTransferResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPDomainTransferResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainTransferResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainTransferResp theComp = (EPPDomainTransferResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
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

	// End EPPDomainTransferResp.equals(Object)

	/**
	 * Clone <code>EPPDomainTransferResp</code>.
	 *
	 * @return clone of <code>EPPDomainTransferResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainTransferResp clone = (EPPDomainTransferResp) super.clone();

		return clone;
	}

	// End EPPDomainTransferResp.clone()

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

	// End EPPDomainTransferResp.toString()

	/**
	 * Gets the domain name
	 *
	 * @return Domain Name if set; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainTransferResp.getName()

	/**
	 * Sets the domain name.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainTransferResp.setName(String)

	/**
	 * Gets the identifier of the client that initiated the transfer request.
	 *
	 * @return The Request Client Id <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRequestClient() {
		return requestClient;
	}

	// End EPPDomainTransferResp.getRequestClient()

	/**
	 * Sets the identifier of the client that initiated the transfer request.
	 *
	 * @param aRequestClient The Request Client Id <code>String</code>
	 */
	public void setRequestClient(String aRequestClient) {
		requestClient = aRequestClient;
	}

	// End EPPDomainTransferResp.setRequestClient(String)

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

	// End EPPDomainTransferResp.getActionClient()

	/**
	 * Sets the identifier of the client that SHOULD respond to the transfer
	 * request.
	 *
	 * @param aActionClient The Action Client Id <code>String</code>
	 */
	public void setActionClient(String aActionClient) {
		actionClient = aActionClient;
	}

	// End EPPDomainTransferResp.setActionClient(String)

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

	// End EPPDomainTransferResp.getTransferStatus()

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

	// End EPPDomainTransferResp.setTransferStatus(String)

	/**
	 * Gets the date and time that the transfer was requested.
	 *
	 * @return The request date and time if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	// End EPPDomainTransferResp.getRequestDate()

	/**
	 * Sets the date and time that the transfer was requested.
	 *
	 * @param aRequestDate The request date and time
	 */
	public void setRequestDate(Date aRequestDate) {
		requestDate = aRequestDate;
	}

	// End EPPDomainTransferResp.setRequestDate(Date)

	/**
	 * Gets the date and time of a required or completed response.
	 *
	 * @return The required or complete response data and time if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getActionDate() {
		return actionDate;
	}

	// End EPPDomainTransferResp.getActionDate()

	/**
	 * Sets the date and time of a required or completed response.
	 *
	 * @param aActionDate The required or complete response data and time.
	 */
	public void setActionDate(Date aActionDate) {
		actionDate = aActionDate;
	}

	// End EPPDomainTransferResp.setActionDate(Date)

	/**
	 * Gets the optional attribute that contains the end of the domain's
	 * validity period     if the transfer command caused or causes a change
	 * in the validity period.
	 *
	 * @return Transfer expiration data and time if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDomainTransferResp.getExpirationDate()

	/**
	 * Sets the optional attribute that contains the end of the domain's
	 * validity period     if the transfer command caused or causes a change
	 * in the validity period.
	 *
	 * @param aExpirationDate Transfer expiration data and time.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDomainTransferResp.setExpirationDate(Date)
}
