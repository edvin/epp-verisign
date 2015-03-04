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
import org.apache.log4j.Priority;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.*;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents a host Status. <br>
 * <br>
 * A host object MUST always have at least one associated status value. Status
 * values MAY be set only by the client that sponsors a host object and by the
 * server on which the object resides. A client can change the status of a
 * host object using the EPP &lt;update&gt; command. Each status value MAY be
 * accompanied by a string of human-readable text that describes the rationale
 * for the status applied to the object. A client MUST NOT alter status values
 * set by the server.  A server MAY alter or override status values set by a
 * client subject to local server policies.  The status of an object MAY
 * change as a result of either a client-initiated transform command or an
 * action performed by a server operator. <br>
 * <br>
 * Status values that may be added or removed by a client are prefixed with
 * "client".  Corresponding status values that may be added or removed by a
 * server are prefixed with "server".  Status values that do not begin with
 * either "client" or "server" are server-managed. Status Value Descriptions:
 * <br><br>
 * 
 * <ul>
 * <li>
 * clientDeleteProhibited, serverDeleteProhibited: Requests to delete the
 * object MUST be rejected.
 * </li>
 * <li>
 * clientUpdateProhibited, serverUpdateProhibited: Requests to update the
 * object (other than to remove this status) MUST be rejected.
 * </li>
 * <li>
 * linked: The host object has at least one active association with another
 * object, such as a domain object.  Servers SHOULD provide services to
 * determine existing object associations.
 * </li>
 * <li>
 * ok: This is the nominal status value for an object that has no pending
 * operations or prohibitions.  This value is set and removed by the server as
 * other status values are added or removed.
 * </li>
 * <li>
 * pendingDelete: A delete request has been received for the object, but the
 * object has not yet been purged from the server database.
 * </li>
 * <li>
 * "ok" status MUST NOT be combined with any other status.
 * </li>
 * <li>
 * pendingCreate:  A create request has been received for a host object, and
 * completion of the request is pending.
 * </li>
 * <li>
 * pendingUpdate:  An update request has been received for a host object, and
 * completion of the request is pending.
 * </li>
 * <li>
 * pendingTransfer:  A transfer request has been received for the host object's
 * superordinate domain object, and completion of the request is pending.
 * </li>
 * <li>
 * pendingDelete  An delete request has been received for a host object or
 * superordinate domain object, and completion of the request is pending.
 * </li>
 * </ul>
 */
public class EPPHostStatus
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Value of the OK status in host mapping */
	public final static String ELM_STATUS_OK = "ok";

	/** Value of the pending delete status in host mapping */
	public final static String ELM_STATUS_PENDING_DELETE = "pendingDelete";

	/** Value of the pending create status in host mapping */
	public final static String ELM_STATUS_PENDING_CREATE = "pendingCreate";

	/** Value of the pending update status in host mapping */
	public final static String ELM_STATUS_PENDING_UPDATE = "pendingUpdate";

	/** Value of the pending transfer status in host mapping */
	public final static String ELM_STATUS_PENDING_TRANSFER = "pendingTransfer";

	/** Value of the client delete prohibited status in host mapping */
	public final static String ELM_STATUS_CLIENT_DELETE_PROHIBITED =
		"clientDeleteProhibited";

	/** Value of the client update prohibited status in host mapping */
	public final static String ELM_STATUS_CLIENT_UPDATE_PROHIBITED =
		"clientUpdateProhibited";

	/** Value of the linked status in host mapping */
	public final static String ELM_STATUS_LINKED = "linked";

	/** Value of the server delete prohibited status in host mapping */
	public final static String ELM_STATUS_SERVER_DELETE_PROHIBITED =
		"serverDeleteProhibited";

	/** Value of the server update prohibited status in host mapping */
	public final static String ELM_STATUS_SERVER_UPDATE_PROHIBITED =
		"serverUpdateProhibited";

	/** Default Language -- English "en" */
	public final static String ELM_DEFAULT_LANG = "en";

	/** XML Element Name of <code>EPPHostStatus</code> root element. */
	final static String ELM_NAME = "host:status";

	/** XML Element Name of <code>EPPHostStatus</code> root element. */
	final static String ELM_STATUS = "s";

	/**
	 * XML Element Language Attribute Name of <code>EPPHostStatus</code> root
	 * element.
	 */
	final static String ELM_LANG = "lang";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPHostStatus.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Host status, default status is OK status. */
	private String status = ELM_STATUS_OK;

	/** Language of the host status. */
	private String lang = ELM_DEFAULT_LANG;

	/** Description of the status rationale. */
	private String description = null;

	/**
	 * <code>EPPHostStatus</code> default constructor.
	 */
	public EPPHostStatus() {
	}

	// End EPPHostStatus.EPPHostStatus()

	/**
	 * <code>EPPHostStatus</code> constructor that takes the host status as
	 * argument.
	 *
	 * @param aStatus Domain status which should be one of the
	 * 		  <code>ELM_STATUS</code> constants.
	 */
	public EPPHostStatus(String aStatus) {
		status = aStatus;
	}

	// End EPPHostStatus.EPPHostStatus(String)

	/**
	 * <code>EPPHostStatus</code> constructor that takes the host status and a
	 * status description.
	 *
	 * @param aStatus Domain status which should be one of the
	 * 		  <code>ELM_STATUS</code> constants.
	 * @param aDesc A description of the status change
	 */
	public EPPHostStatus(String aStatus, String aDesc) {
		status		    = aStatus;
		description     = aDesc;
	}

	// End EPPHostStatus.EPPHostStatus(String, String)

	/**
	 * <code>EPPHostStatus</code> constructor that takes the host status and
	 * the language as arguments.
	 *
	 * @param aStatus Domain status which should be one of the
	 * 		  <code>ELM_STATUS</code> constants.
	 * @param aDesc A description of the status change
	 * @param aLang Language of the status description
	 */
	public EPPHostStatus(String aStatus, String aDesc, String aLang) {
		status		    = aStatus;
		description     = aDesc;
		lang		    = aLang;
	}

	// End EPPHostStatus.EPPHostStatus(String, String, String)

	/**
	 * Clone <code>EPPHostStatus</code>.
	 *
	 * @return clone of <code>EPPHostStatus</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostStatus clone = null;

		clone = (EPPHostStatus) super.clone();

		return clone;
	}

	// End EPPHostStatus.clone()

	/**
	 * Decode the EPPHostStatus attributes from the aElement DOM Element tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPHostStatus from.
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

	// End EPPHostStatus.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPHostStatus
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPHostStatus
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPHostStatus instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Status with Attributes
		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

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

	// End EPPHostStatus.encode(Document)

	/**
	 * implements a deep <code>EPPHostStatus</code> compare.
	 *
	 * @param aObject <code>EPPHostStatus</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostStatus)) {
			return false;
		}

		EPPHostStatus theComp = (EPPHostStatus) aObject;

		// status
		if (!status.equals(theComp.status)) {
			cat.error("Status " + status + " != " + theComp.status);

			return false;
		}

		// description
		if (
			!(
					(description == null) ? (theComp.description == null)
											  : description.equals(theComp.description)
				)) {
			cat.error("Description " + description + " != "
					  + theComp.description);

			return false;
		}

		// lang
		if (!lang.equals(theComp.lang)) {
			cat.error("Lang " + lang + " != " + theComp.lang);

			return false;
		}

		return true;
	}

	// End EPPHostStatus.equals(Object)

	/**
	 * Get status description language. The default is
	 * <code>ELM_DEFAULT_LANG</code>.
	 *
	 * @return Language
	 */
	public String getLang() {
		return lang;
	}

	// End EPPHostStatus.getLang()

	/**
	 * Gets host status.
	 *
	 * @return Host Status
	 */
	public String getStatus() {
		return status;
	}

	// End EPPHostStatus.getLang()

	/**
	 * Sets host status.
	 *
	 * @param newStatus String
	 */
	public void setStatus(String newStatus) {
		status = newStatus;
	}

	// End EPPHostStatus.setStatus(String)

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

	// End EPPHostStatus.toString()

	/**
	 * Sets status description language.
	 *
	 * @param newLang String
	 */
	public void setLang(String newLang) {
		lang = newLang;
	}

	// End EPPHostStatus.setLang(String)

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
}
