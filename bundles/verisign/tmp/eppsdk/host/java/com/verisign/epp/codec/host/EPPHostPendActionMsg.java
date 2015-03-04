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


// Log4j Imports
import org.apache.log4j.Logger;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Text;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Poll message used to indicate the result of a pending  action.  Pending
 * action can included host create,  update, and delete.
 *
 * @see com.verisign.epp.codec.host.EPPHostCreateCmd
 * @see com.verisign.epp.codec.host.EPPHostUpdateCmd
 * @see com.verisign.epp.codec.host.EPPHostDeleteCmd
 */
public class EPPHostPendActionMsg extends EPPResponse {
	/** XML Element Name of <code>EPPHostPendActionMsg</code> root element. */
	final static String ELM_NAME = "host:panData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "host:name";

	/** XML Element Name for the <code>paTRID</code> attribute. */
	private final static String ELM_PATRID = "host:paTRID";

	/** XML Element Name for the <code>paDate</code> attribute. */
	private final static String ELM_PENDING_DATE = "host:paDate";

	/** XML Attribute Name for the pending action result */
	private final static String ATTR_RESULT = "paResult";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPHostPendActionMsg.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** fully qualified host name of pending action. */
	private String name = null;

	/**
	 * Was the pending request successful?  A value of <code>true</code>
	 * indicates that the request has been approved and completed.  A  value
	 * of <code>false</code> indicates that the request has been  denied and
	 * the requested action has not been taken.  Default  value is
	 * <code>false</code>.
	 */
	private boolean success = false;

	/**
	 * Pending transaction id contains the client transaction identifier and
	 * server transaction identifier returned with the original response to
	 * process the command.
	 */
	EPPTransId pendingTransId = null;

	/**
	 * Pending date and time describing when review of the requested action was
	 * completed.
	 */
	private Date pendingDate = null;

	/**
	 * <code>EPPHostPendActionMsg</code> default constructor. Must call
	 * required setter methods before encode.
	 */
	public EPPHostPendActionMsg() {
	}

	// End EPPHostPendActionMsg()

	/**
	 * <code>EPPHostPendActionMsg</code> constructor that takes all of  the
	 * required attributes.
	 *
	 * @param aTransId Poll command transaction id
	 * @param aName Host name of pending action
	 * @param aSuccess Was the pending action successful
	 * @param aPendingTransId Pending action transaction id
	 * @param aPendingDate Date of pending action completion
	 */
	public EPPHostPendActionMsg(
								EPPTransId aTransId, String aName,
								boolean aSuccess, EPPTransId aPendingTransId,
								Date aPendingDate) {
		super(aTransId);

		this.name		 = aName;
		this.success     = aSuccess;
		this.setPendingTransId(aPendingTransId);
		this.pendingTransId     = aPendingTransId;
		this.pendingDate	    = aPendingDate;
	}

	// End EPPHostPendActionMsg.EPPHostPendActionMsg(EPPTransId, String, boolean, EPPTransId, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPHostPendActionMsg</code>.
	 *
	 * @return EPPHostPendActionMsg.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPHostPendActionMsg.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPHostPendActionMsg</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostPendActionMsg.getNamespace()

	/**
	 * Gets the host name
	 *
	 * @return Host Name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPHostPendActionMsg.getName()

	/**
	 * Sets the host name.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostPendActionMsg.setName(String)

	/**
	 * Gets the date that the pending action was completed.
	 *
	 * @return Returns the pendingDate.
	 */
	public Date getPendingDate() {
		return pendingDate;
	}

	/**
	 * Sets the date that the pending action was completed.
	 *
	 * @param pendingDate The pendingDate to set.
	 */
	public void setPendingDate(Date pendingDate) {
		this.pendingDate = pendingDate;
	}

	/**
	 * Gets the pending transaction id.
	 *
	 * @return Returns the pendingTransId.
	 */
	public EPPTransId getPendingTransId() {
		return pendingTransId;
	}

	/**
	 * Sets the pending transaction id.
	 *
	 * @param pendingTransId The pendingTransId to set.
	 */
	public void setPendingTransId(EPPTransId pendingTransId) {
		this.pendingTransId = pendingTransId;
		this.pendingTransId.setRootName(EPPHostMapFactory.NS, ELM_PATRID);
	}

	/**
	 * Was the pending action successful?
	 *
	 * @return Returns <code>true</code> if the pending action was successfully
	 * 		   completed; <code>false</code> otherwise.
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets if the pending action was successful.
	 *
	 * @param success The success to set.
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Compare an instance of <code>EPPHostPendActionMsg</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostPendActionMsg)) {
			cat.error("EPPHostPendActionMsg.equals(): "
					  + aObject.getClass().getName()
					  + " not EPPHostPendActionMsg instance");

			return false;
		}

		if (!super.equals(aObject)) {
			cat.error("EPPHostPendActionMsg.equals(): super class not equal");

			return false;
		}

		EPPHostPendActionMsg theComp = (EPPHostPendActionMsg) aObject;

		// Name
		if (
			!(
					(this.name == null) ? (theComp.name == null)
											: this.name.equals(theComp.name)
				)) {
			cat.error("EPPHostPendActionMsg.equals(): name not equal");

			return false;
		}

		if (this.success != theComp.success) {
			cat.error("EPPHostPendActionMsg.equals(): success not equal");

			return false;
		}

		// Pending Transaction Id
		if (
			!(
					(this.pendingTransId == null)
						? (theComp.pendingTransId == null)
							: this.pendingTransId.equals(theComp.pendingTransId)
				)) {
			cat.error("EPPHostPendActionMsg.equals(): pendingTransId not equal");

			return false;
		}

		// Pending Date
		if (
			!(
					(this.pendingDate == null) ? (theComp.pendingDate == null)
												   : this.pendingDate.equals(theComp.pendingDate)
				)) {
			cat.error("EPPHostPendActionMsg.equals(): pendingDate not equal");

			return false;
		}

		return true;
	}

	// End EPPHostPendActionMsg.equals(Object)

	/**
	 * Clone <code>EPPHostPendActionMsg</code>.
	 *
	 * @return clone of <code>EPPHostPendActionMsg</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostPendActionMsg clone = (EPPHostPendActionMsg) super.clone();

		return clone;
	}

	// End EPPHostPendActionMsg.clone()

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

	// End EPPHostPendActionMsg.toString()

	/**
	 * Validate the state of the <code>EPPHostPendActionMsg</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error.  throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (this.name == null) {
			cat.error("EPPHostPendActionMsg.validateState(): required attribute name is not set");
			throw new EPPCodecException("required attribute name is not set");
		}

		if (this.pendingTransId == null) {
			cat.error("EPPHostPendActionMsg.validateState(): required attribute pendingTransId is not set");
			throw new EPPCodecException("required attribute pendingTransId is not set");
		}

		if (this.pendingDate == null) {
			cat.error("EPPHostPendActionMsg.validateState(): required attribute pendingDate is not set");
			throw new EPPCodecException("required attribute pendingDate is not set");
		}
	}

	// End EPPHostPendActionMsg.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostPendActionMsg</code> instance.
	 *
	 * @param aDocument DOM Document that is being built. Used as an Element
	 * 		  factory.
	 *
	 * @return Element Root DOM Element representing the
	 * 		   <code>EPPHostPendActionMsg</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostPendActionMsg</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPHostPendActionMsg.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Host Name
		Element nameElm =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_DOMAIN_NAME);
		root.appendChild(nameElm);

		// Available
		if (this.success) {
			nameElm.setAttribute(ATTR_RESULT, "1");
		}
		else {
			nameElm.setAttribute(ATTR_RESULT, "0");
		}

		Text textNode = aDocument.createTextNode(this.name);
		nameElm.appendChild(textNode);

		// Pending Transaction Id
		EPPUtil.encodeComp(aDocument, root, this.pendingTransId);

		// Pending Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, this.pendingDate,
								  EPPHostMapFactory.NS, ELM_PENDING_DATE);

		return root;
	}

	// End EPPHostPendActionMsg.doEncode(Document)

	/**
	 * Decode the <code>EPPHostPendActionMsg</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPHostPendActionMsg</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		Element theNameElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPHostMapFactory.NS, ELM_DOMAIN_NAME);
		this.name = theNameElm.getFirstChild().getNodeValue();

		String resultStr = theNameElm.getAttribute(ATTR_RESULT);

		// Success?
		if (resultStr.equals("1")) {
			this.success = true;
		}
		else {
			this.success = false;
		}

		// Pending Transaction ID
		this.pendingTransId =
			(EPPTransId) EPPUtil.decodeComp(
											aElement, EPPHostMapFactory.NS,
											ELM_PATRID, EPPTransId.class);

		// Pending Date
		this.pendingDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPHostMapFactory.NS,
									  ELM_PENDING_DATE);

		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPHostPendActionMsg.decode: "
										 + e);
		}
	}

	// End EPPHostPendActionMsg.doDecode(Element)
}


// End class EPPHostPendActionMsg
