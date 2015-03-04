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
package com.verisign.epp.codec.emailFwd;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.*;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a emailFwd Status. A emailFwd object MUST always have at least
 * one associated status value. Status values MAY be set only by the client
 * that sponsors a emailFwd object and by the server on which the object
 * resides.  A client MAY change the status of a emailFwd object using the EPP
 * &lt;update&gt; command. Each status value MAY be accompanied by a string of
 * human-readable text that describes the rationale for the status applied to
 * the object. <br>
 * <br>
 * A client MUST NOT alter status values set by the server.  A server MAY
 * alter or override status values set by a client subject to local server
 * policies. <br>
 * <br>
 * Status values that may be added or removed by a client are prefixed with
 * "client".  Corresponding status values that may be added or removed by a
 * server are prefixed with "server".  Status values that do not begin with
 * either "client" or "server" are server-managed. <br>
 * <br>
 * Status Value Descriptions: <br>
 * 
 * <ul>
 * <li>
 * clientDeleteProhibited, serverDeleteProhibited: Requests to delete the
 * object MUST be rejected.
 * </li>
 * <li>
 * clientHold, serverHold: Delegation information MUST be withheld from
 * publication in the object's nominal zone.
 * </li>
 * <li>
 * clientRenewProhibited, serverRenewProhibited: Requests to renew the object
 * MUST be rejected.
 * </li>
 * <li>
 * clientTransferProhibited, serverTransferProhibited: Requests to transfer the
 * object MUST be rejected.
 * </li>
 * <li>
 * clientUpdateProhibited, serverUpdateProhibited: Requests to update the
 * object (other than to remove this status) MUST be rejected.
 * </li>
 * <li>
 * inactive: Delegation information has not been associated with the object.
 * </li>
 * <li>
 * ok: This is the nominal status value for an object that has no pending
 * perations or prohibitions.
 * </li>
 * <li>
 * pendingDelete: A delete request has been received for the object, but the
 * object has not yet been purged from the server database.
 * </li>
 * <li>
 * pendingTransfer: A transfer request has been received for the object, and
 * completion of the request is pending.  Transform commands other than
 * &lt;transfer&gt; MUST be rejected while an object is in this state.
 * </li>
 * <li>
 * pendingVerification: A create request has been received for the object, and
 * completion of the request is pending.
 * </li>
 * <li>
 * <code>ok</code> status MUST NOT be combined with any other status.
 * </li>
 * <li>
 * <code>pendingDelete</code> status MUST NOT be combined with either
 * </li>
 * <li>
 * <code>clientDeleteProhibited</code> or <code>serverDeleteProhibited</code>
 * status.
 * </li>
 * <li>
 * <code>pendingTransfer</code> status MUST NOT be combined with either
 * <code>clientTransferProhibited</code> or
 * <code>serverTransferProhibited</code> status.
 * </li>
 * <li>
 * Allother status value combinations are valid.
 * </li>
 * </ul>
 */
public class EPPEmailFwdStatus
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Value of the OK status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_OK = "ok";

	/** Value of the server hold status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_SERVER_HOLD = "serverHold";

	/** Value of the server renew prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_SERVER_RENEW_PROHIBITED =
		"serverRenewProhibited";

	/** Value of the server transfer prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_SERVER_TRANSFER_PROHIBITED =
		"serverTransferProhibited";

	/** Value of the server update prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_SERVER_UPDATE_PROHIBITED =
		"serverUpdateProhibited";

	/** Value of the server delete prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_SERVER_DELETE_PROHIBITED =
		"serverDeleteProhibited";

	/** Value of the pending create status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_PENDING_CREATE =
		"pendingCreate";

	/** Value of the pending delete status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_PENDING_DELETE =
		"pendingDelete";

	/** Value of the pending renew status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_PENDING_RENEW =
		"pendingRenew";

	/** Value of the pending transfer status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_PENDING_TRANSFER =
		"pendingTransfer";

	/** Value of the pending update status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_PENDING_UPDATE =
		"pendingUpdate";

	/** Value of the client hold status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_HOLD = "clientHold";

	/** Default Language -- English "en" */
	public final static java.lang.String ELM_DEFAULT_LANG = "en";

	/** Value of the client transfer prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_TRANSFER_PROHIBITED =
		"clientTransferProhibited";

	/** Value of the client update prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_UPDATE_PROHIBITED =
		"clientUpdateProhibited";

	/** Value of the client renew prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_RENEW_PROHIBITED =
		"clientRenewProhibited";

	/** Value of the client delete prohibited status in emailFwd mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_DELETE_PROHIBITED =
		"clientDeleteProhibited";

	/** XML Element Name of <code>EPPEmailFwdStatus</code> root element. */
	final static java.lang.String ELM_NAME = "emailFwd:status";

	/**
	 * XML Element Status Attribute Name of <code>EPPEmailFwdStatus</code> root
	 * element.
	 */
	final static java.lang.String ELM_STATUS = "s";

	/**
	 * XML Element Language Attribute Name of <code>EPPEmailFwdStatus</code>
	 * root element.
	 */
	final static java.lang.String ELM_LANG = "lang";

	/** EmailFwd status. */
	private java.lang.String status = ELM_STATUS_OK;

	/** Language of the emailFwdemailFwd status. */
	private java.lang.String lang = ELM_DEFAULT_LANG;

	/**
	 * <code>EPPEmailFwdStatus</code> default constructor. The status is
	 * initialized to <code>ELM_STATUS_OK</code>.     The lang is initialized
	 * to <code>ELM_DEFAULT_LANG</code>.
	 */
	public EPPEmailFwdStatus() {
		status     = ELM_STATUS_OK;
		lang	   = ELM_DEFAULT_LANG;
	}

	// End EPPEmailFwdStatus.EPPEmailFwdStatus()

	/**
	 * <code>EPPEmailFwdStatus</code> constructor that takes the emailFwd
	 * status as argument. The language will default to
	 * <code>ELM_DEFAULT_LANG</code>.
	 *
	 * @param aStatus String    EmailFwd staus
	 */
	public EPPEmailFwdStatus(String aStatus) {
		status     = aStatus;
		lang	   = ELM_DEFAULT_LANG;
	}

	// End EPPEmailFwdStatus.EPPEmailFwdStatus(String)

	/**
	 * <code>EPPEmailFwdStatus</code> constructor that takes the emailFwd
	 * status and the language as arguments.
	 *
	 * @param aStatus String    EmailFwd status
	 * @param aLang String  Language of the status
	 */
	public EPPEmailFwdStatus(String aStatus, String aLang) {
		status     = aStatus;
		lang	   = aLang;
	}

	// End EPPEmailFwdStatus.EPPEmailFwdStatus(String, String)

	/**
	 * Clone <code>EPPEmailFwdStatus</code>.
	 *
	 * @return clone of <code>EPPEmailFwdStatus</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdStatus clone = null;

		clone = (EPPEmailFwdStatus) super.clone();

		return clone;
	}

	// End EPPEmailFwdStatus.clone()

	/**
	 * Decode the EPPEmailFwdStatus attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPEmailFwdStatus from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Status
		status     = aElement.getAttribute(ELM_STATUS);
		lang	   = aElement.getAttribute(ELM_LANG);
	}

	// End EPPEmailFwdStatus.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPEmailFwdStatus
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPEmailFwdStatus
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPEmailFwdStatus
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Status with Attributes
		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		// add attribute status
		root.setAttribute(ELM_STATUS, status);

		// add attribute lang
		if (!lang.equals(ELM_DEFAULT_LANG)) {
			root.setAttribute(ELM_LANG, lang);
		}

		return root;
	}

	// End EPPEmailFwdStatus.encode(Document)

	/**
	 * implements a deep <code>EPPEmailFwdStatus</code> compare.
	 *
	 * @param aObject <code>EPPEmailFwdStatus</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdStatus)) {
			return false;
		}

		EPPEmailFwdStatus theComp = (EPPEmailFwdStatus) aObject;

		// status
		if (!status.equals(theComp.status)) {
			return false;
		}

		// lang
		if (!lang.equals(theComp.lang)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdStatus.equals(Object)

	/**
	 * Get language of the status.
	 *
	 * @return String  Language
	 */
	public String getLang() {
		return lang;
	}

	// End EPPEmailFwdStatus.getLang()

	/**
	 * Get emailFwd status.
	 *
	 * @return String  EmailFwd Status
	 */
	public java.lang.String getStatus() {
		return status;
	}

	// End EPPEmailFwdStatus.getStatus()

	/**
	 * Set language of emailFwd status.
	 *
	 * @param newLang String
	 */
	public void setLang(String newLang) {
		lang = newLang;
	}

	// End EPPEmailFwdStatus.setLang(String)

	/**
	 * Set emailFwd status.
	 *
	 * @param newStatus String
	 */
	public void setStatus(java.lang.String newStatus) {
		status = newStatus;
	}

	// End EPPEmailFwdStatus.setLang(String)

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

	// End EPPEmailFwdStatus.toString()
}
