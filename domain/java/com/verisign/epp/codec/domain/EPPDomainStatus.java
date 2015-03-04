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
 * Represents a domain Status.<br>
 * <br>
 * A domain object MUST always have at least one associated status value.
 * Status values can be set only by the client that sponsors a domain object
 * and by the server on which the object resides.  A client can change the
 * status of a domain object using the EPP &lt;update&gt; command. Each status
 * value MAY be accompanied by a string of human-readable text that describes
 * the rationale for the status applied to the object.<br>
 * <br>
 * A client MUST NOT alter status values set by the server.  A server MAY
 * alter or override status values set by a client subject to local server
 * policies.  The status of an object MAY change as a result of either a
 * client-initiated transform command or an action performed by a server operator.<br>
 * <br>
 * Status values that can be added or removed by a client are prefixed with
 * "client".  Corresponding status values that can be added or removed by a
 * server are prefixed with "server".  Status values that do not begin with
 * either "client" or "server" are server-managed. <br>
 * <br>
 * Status Value Descriptions:<br><br>
 * 
 * <ul>
 * <li>
 * clientDeleteProhibited, serverDeleteProhibited Requests to delete the object
 * MUST be rejected.
 * </li>
 * <li>
 * clientHold, serverHold<br>
 * DNS delegation information MUST NOT be published for the object.
 * </li>
 * <li>
 * clientRenewProhibited, serverRenewProhibited<br>
 * Requests to renew the object MUST be rejected.
 * </li>
 * <li>
 * clientTransferProhibited, serverTransferProhibited<br>
 * Requests to transfer the object MUST be rejected.
 * </li>
 * <li>
 * clientUpdateProhibited, serverUpdateProhibited<br>
 * Requests to update the object (other than to remove this status) MUST be
 * rejected.
 * </li>
 * <li>
 * inactive<br> Delegation information has not been associated with the object.
 * </li>
 * <li>
 * ok<br> This is the normal status value for an object that has no pending
 * operations or prohibitions.  This value is set and removed by the server as
 * other status values are added or removed.
 * </li>
 * <li>
 * pendingCreate, pendingDelete, pendingRenew, pendingTransfer, pendingUpdate<br>
 * A transform command has been processed for the object, but the action has
 * not been completed by the server.  Server operators can delay action
 * completion for a variety of reasons, such as to allow for human review or
 * third-party action.  A transform command that is processed, but whose
 * requested action is pending, is noted with response code 1001.
 * </li>
 * </ul>
 */
public class EPPDomainStatus
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Value of the OK status in domain mapping */
	public final static java.lang.String ELM_STATUS_OK = "ok";

	/** Value of the server hold status in domain mapping */
	public final static java.lang.String ELM_STATUS_SERVER_HOLD = "serverHold";

	/** Value of the server renew prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_SERVER_RENEW_PROHIBITED =
		"serverRenewProhibited";

	/** Value of the server transfer prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_SERVER_TRANSFER_PROHIBITED =
		"serverTransferProhibited";

	/** Value of the server update prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_SERVER_UPDATE_PROHIBITED =
		"serverUpdateProhibited";

	/** Value of the server delete prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_SERVER_DELETE_PROHIBITED =
		"serverDeleteProhibited";

	/** Value of the inactive status in domain mapping */
	public final static java.lang.String ELM_STATUS_INACTIVE = "inactive";

	/** Value of the pending create status in domain mapping */
	public final static java.lang.String ELM_STATUS_PENDING_CREATE =
		"pendingCreate";

	/** Value of the pending delete status in domain mapping */
	public final static java.lang.String ELM_STATUS_PENDING_DELETE =
		"pendingDelete";

	/** Value of the pending renew status in domain mapping */
	public final static java.lang.String ELM_STATUS_PENDING_RENEW =
		"pendingRenew";

	/** Value of the pending transfer status in domain mapping */
	public final static java.lang.String ELM_STATUS_PENDING_TRANSFER =
		"pendingTransfer";

	/** Value of the pending update status in domain mapping */
	public final static java.lang.String ELM_STATUS_PENDING_UPDATE =
		"pendingUpdate";

	/** Value of the client hold status in domain mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_HOLD = "clientHold";

	/** Default Language -- English "en" */
	public final static java.lang.String ELM_DEFAULT_LANG = "en";

	/** Value of the client transfer prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_TRANSFER_PROHIBITED =
		"clientTransferProhibited";

	/** Value of the client update prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_UPDATE_PROHIBITED =
		"clientUpdateProhibited";

	/** Value of the client renew prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_RENEW_PROHIBITED =
		"clientRenewProhibited";

	/** Value of the client delete prohibited status in domain mapping */
	public final static java.lang.String ELM_STATUS_CLIENT_DELETE_PROHIBITED =
		"clientDeleteProhibited";

	/** XML Element Name of <code>EPPDomainStatus</code> root element. */
	final static java.lang.String ELM_NAME = "domain:status";

	/**
	 * XML Element Status Attribute Name of <code>EPPDomainStatus</code> root
	 * element.
	 */
	final static java.lang.String ELM_STATUS = "s";

	/**
	 * XML Element Language Attribute Name of <code>EPPDomainStatus</code> root
	 * element.
	 */
	final static java.lang.String ELM_LANG = "lang";

	/** Domain status. */
	private java.lang.String status = ELM_STATUS_OK;

	/** Language of the domain status. */
	private java.lang.String lang = ELM_DEFAULT_LANG;

	/** Description of the status rationale. */
	private String description = null;

	/**
	 * <code>EPPDomainStatus</code> default constructor.
	 */
	public EPPDomainStatus() {
	}

	// End EPPDomainStatus.EPPDomainStatus()

	/**
	 * <code>EPPDomainStatus</code> constructor that takes the domain status as
	 * argument.
	 *
	 * @param aStatus String    Domain staus
	 */
	public EPPDomainStatus(String aStatus) {
		status = aStatus;
	}

	// End EPPDomainStatus.EPPDomainStatus(String)

	/**
	 * <code>EPPDomainStatus</code> constructor that takes the domain status
	 * and a status description.
	 *
	 * @param aStatus Domain status which should be one of the
	 * 		  <code>ELM_STATUS</code> constants.
	 * @param aDesc A description of the status change
	 */
	public EPPDomainStatus(String aStatus, String aDesc) {
		status		    = aStatus;
		description     = aDesc;
	}

	// End EPPDomainStatus.EPPDomainStatus(String, String)

	/**
	 * <code>EPPDomainStatus</code> constructor that takes the domain status
	 * and the language as arguments.
	 *
	 * @param aStatus Domain status which should be one of the
	 * 		  <code>ELM_STATUS</code> constants.
	 * @param aDesc A description of the status change
	 * @param aLang Language of the status description
	 */
	public EPPDomainStatus(String aStatus, String aDesc, String aLang) {
		status		    = aStatus;
		description     = aDesc;
		lang		    = aLang;
	}

	// End EPPDomainStatus.EPPDomainStatus(String, String, String)

	/**
	 * Clone <code>EPPDomainStatus</code>.
	 *
	 * @return clone of <code>EPPDomainStatus</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainStatus clone = null;

		clone = (EPPDomainStatus) super.clone();

		return clone;
	}

	// End EPPDomainStatus.clone()

	/**
	 * Decode the EPPDomainStatus attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPDomainStatus from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Status
		status = aElement.getAttribute(ELM_STATUS);

		// Description
		Node descNode = aElement.getFirstChild();

		if (descNode != null) {
			description     = descNode.getNodeValue();

			// Description Language
			String theLang = aElement.getAttribute(ELM_LANG);
			if (theLang == null || theLang.equals(""))
				lang = ELM_DEFAULT_LANG;
			else
				lang = theLang;
		}
	}

	// End EPPDomainStatus.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPDomainStatus
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPDomainStatus
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPDomainStatus
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Status with Attributes
		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		// add attribute status
		root.setAttribute(ELM_STATUS, status);

		// Description specified?
		if (description != null) {
			// Non-default language specified?
			if (!lang.equals(ELM_DEFAULT_LANG)) {
				root.setAttribute(ELM_LANG, lang);
			}

			Text descVal = aDocument.createTextNode(description);
			root.appendChild(descVal);
		}

		return root;
	}

	// End EPPDomainStatus.encode(Document)

	/**
	 * implements a deep <code>EPPDomainStatus</code> compare.
	 *
	 * @param aObject <code>EPPDomainStatus</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainStatus)) {
			return false;
		}

		EPPDomainStatus theComp = (EPPDomainStatus) aObject;

		// status
		if (!status.equals(theComp.status)) {
			return false;
		}

		// description
		if (
			!(
					(description == null) ? (theComp.description == null)
											  : description.equals(theComp.description)
				)) {
			return false;
		}

		// lang
		if (!lang.equals(theComp.lang)) {
			return false;
		}

		return true;
	}

	// End EPPDomainStatus.equals(Object)

	/**
	 * Get language of the status.
	 *
	 * @return String  Language
	 */
	public String getLang() {
		return lang;
	}

	// End EPPDomainStatus.getLang()

	/**
	 * Get domain status.
	 *
	 * @return String  Domain Status
	 */
	public java.lang.String getStatus() {
		return status;
	}

	// End EPPDomainStatus.getStatus()

	/**
	 * Set language of domain status.
	 *
	 * @param newLang String
	 */
	public void setLang(String newLang) {
		lang = newLang;
	}

	// End EPPDomainStatus.setLang(String)

	/**
	 * Set domain status.
	 *
	 * @param newStatus String
	 */
	public void setStatus(java.lang.String newStatus) {
		status = newStatus;
	}

	// End EPPDomainStatus.setLang(String)

	/**
	 * Gets the status description, which is free form text describing the
	 * rationale for the status.
	 *
	 * @return DOCUMENT ME!
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the status description, which is free form text describing the
	 * rationale for the status.
	 *
	 * @param aDesc status description
	 */
	public void setDescription(String aDesc) {
		description = aDesc;
	}

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

	// End EPPDomainStatus.toString()
}
