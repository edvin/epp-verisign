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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Contact &lt;transfer&gt; command. The EPP &lt;transfer&gt;
 * command provides a query operation that allows a client to determine
 * real-time status of pending and completed transfer requests.  In addition
 * to the standard EPP command elements, the &lt;transfer&gt; command MUST
 * contain an "op"; attribute with value "query", and a
 * &lt;contact:transfer&gt; element that identifies the contact namespace and
 * the location of the contact schema. The &lt;contact:transfer&gt; element
 * MUST contain the following child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier of
 * the contact object to be queried. Use <code>getId</code>     and
 * <code>setId</code> to get and set the element.
 * </li>
 * <li>
 * An authorization information as described in [EPP]. Use
 * <code>getAuthInfo</code>     and <code>setAuthInfo</code> to get and set
 * the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 * <code>EPPContactTransferResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPContactTransferCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactTransferResp
 */
public class EPPContactTransferCmd extends EPPTransferCmd {
	/** XML Element Name of <code>EPPContactTransferCmd</code> root element. */
	final static String ELM_NAME = "contact:transfer";

	/** XML Element Name for the <code>id</code> attribute. */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** XML Element Name for the <code>id</code> attribute. */
	private final static String ELM_CONTACT_AUTHINFO = "contact:authInfo";

	/** XML Element Name for the <code>id</code> attribute. */
	private final static String ELM_CONTACT_TRANSFER_OP = "request";

	/** Contact Id of contact to query. */
	private String id = null;

	/** DOCUMENT ME! */
	private EPPAuthInfo authInfo = null;

	/**
	 * Allocates a new <code>EPPContactTransferCmd</code> with default
	 * attribute values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * id is set to <code>null</code>
	 * </li>
	 * <li>
	 * operation is set to to <code>null</code>
	 * </li>
	 * <li>
	 * authorization information  is set to to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The id, operation, and auth id must be set before invoking
	 * <code>encode</code>.
	 */
	public EPPContactTransferCmd() {
		id			 = null;
		authInfo     = null;
	}

	// End EPPContactTransferCmd.EPPContactTransferCmd()

	/**
	 * <code>EPPContactTransferCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aId Contact id to create.
	 */
	public EPPContactTransferCmd(String aTransId, String aOp, String aId) {
		super(aTransId, aOp);
		id = aId;
	}

	// End EPPContactTransferCmd.EPPContactTransferCmd(String, String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactTransferCmd</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactTransferCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPContactTransferCmd</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the id of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (super.getOp() == null) {
			throw new EPPCodecException("op required attribute is not set");
		}

		if (id == null) {
			throw new EPPCodecException("id required attribute is not set");
		}

		if (super.getOp().equals(EPPCommand.OP_REQUEST) && (authInfo == null)) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPContactTransferCmd.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactTransferCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPContactTransferCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactTransferCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPContactTransferCmd invalid state: "
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

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPContactTransferCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPContactTransferCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactTransferCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		String tempVal;

		// id
		id     = EPPUtil.decodeString(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CONTACT_ID);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPContactMapFactory.NS,
											 ELM_CONTACT_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPContactTransferCmd.doDecode(Element)

	/**
	 * Gets the contact id to query.
	 *
	 * @return Contact Id if defined; <code>null</code> otherwise.
	 */
	public String getId() {
		return id;
	}

	// End EPPContactTransferCmd.getId()

	/**
	 * Sets the contact id to query.
	 *
	 * @param aId Contact Id
	 */
	public void setId(String aId) {
		id     = aId;
	}

	// End EPPContactTransferCmd.setId(String)

	/**
	 * Compare an instance of <code>EPPContactTransferCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactTransferCmd)) {
			return false;
		}

		// EPPTransferCmd
		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactTransferCmd theMap = (EPPContactTransferCmd) aObject;

		// id
		if (!((id == null) ? (theMap.id == null) : id.equals(theMap.id))) {
			return false;
		}

		// authInfo
		if (
			!(
					(authInfo == null) ? (theMap.authInfo == null)
										   : authInfo.equals(theMap.authInfo)
				)) {
			return false;
		}

		return true;
	}

	// End EPPContactTransferCmd.equals(Object)

	/**
	 * Clone <code>EPPContactTransferCmd</code>.
	 *
	 * @return clone of <code>EPPContactTransferCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactTransferCmd clone = (EPPContactTransferCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
	}

	// End EPPContactTransferCmd.clone()

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

	// End EPPContactTransferCmd.toString()

	/**
	 * Get authorization information.
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPContactTransferCmd.getAuthInfo()

	/**
	 * Set authorization information.
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPContactMapFactory.NS, EPPContactMapFactory.ELM_CONTACT_AUTHINFO);
		}
	}

	// End EPPContactTransferCmd.setAuthInfo(EPPAuthInfo)
}
