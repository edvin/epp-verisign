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
// Java Core Imports
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * <code>EPPContactCheckResult</code> represents the result of an individual
 * contact id check.  The attributes of <code>EPPContactCheckResult</code>
 * include     the contact id and a boolean value indicating if the contact id
 * is available by the server.  <code>contact reason</code> must be set before
 * invoking <code>encode</code> if the available flag is set to
 * <code>false</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPContactCheckResult implements EPPCodecComponent {
	/** XML root tag id for <code>EPPContactCheckResult</code>. */
	final static String ELM_NAME = "contact:cd";

	/** XML Element name of <code>EPPContactCheckResult</code> root element. */
	final static String ELM_CONTACT_ID = "contact:id";

	/** XML Element Name for the <code>reason</code> attribute. */
	private final static String ELM_CONTACT_REASON = "contact:reason";

	/** XML attribute id for the <code>available</code> attribute. */
	private final static String ATTR_AVAIL = "avail";
	
	/** XML attribute name for the <code>domain:reason</code> attribute. */
	private final static String ATTR_LANG = "lang";

	/** Default XML attribute value for domain reason language. */
	private final static String VALUE_LANG = "en";

	/** XML attribute value for the <code>lang</code> attribute. */
	private String language = "en";

	/** Contact Id associated with result. */
	private String id = null;

	/** Is the Contact Id (id) available? */
	private boolean available;

	/** Contact Reason to check.  This is a <code>String</code>. */
	private String reason;

	/**
	 * Default constructor for <code>EPPContactCheckResult</code>.     the
	 * defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * id is set to <code>null</code>
	 * </li>
	 * <li>
	 * available is set to <code>true</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The id must be set before invoking <code>encode</code>.
	 */
	public EPPContactCheckResult() {
		id		  = null;
		available     = true;
	}

	// End EPPContactCheckResult()

	/**
	 * Constructor for <code>EPPContactCheckResult</code> that includes     the
	 * contact id and the is available flag.
	 *
	 * @param aId Contact id associated with result
	 * @param aIsAvailable Is the contact id available?
	 */
	public EPPContactCheckResult(String aId, boolean aIsAvailable) {
		id		  = aId;
		available     = aIsAvailable;
	}

	// EPPContactCheckResult(String, boolean)

	/**
	 * Gets the contact id associated with the result.
	 *
	 * @return Contact id associated with the result if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getId() {
		return id;
	}

	// End EPPContactCheckResult.getId()

	/**
	 * Sets the contact id associated with the result.
	 *
	 * @param aId Contact Id associated with the result.
	 */
	public void setId(String aId) {
		id = aId;
	}

	// End EPPContactCheckResult.setId(String)

	/**
	 * Gets if the contact associated with <code>EPPContactCheckResult</code>
	 * is available.
	 *
	 * @return Is the contact available?
	 */
	public boolean isAvailable() {
		return available;
	}

	// End EPPContactCheckResult.isAvailable()

	/**
	 * Sets if the contact associated with <code>EPPContactCheckResult</code>
	 * is available.
	 *
	 * @param aIsAvailable Is the contact available?
	 */
	public void setIsAvailable(boolean aIsAvailable) {
		available = aIsAvailable;
	}

	// End EPPContactCheckResult.setIsAvailable(boolean)

	/**
	 * Sets language attribute.
	 *
	 * @param aLang Sets contact reason language attribute.
	 */
	public void setLanguage(String aLang) {
		language = aLang;
	}

	// End EPPContactCheckResult.setLanguage(String)

	/**
	 * Sets contact reason to check.
	 *
	 * @return String of domain reason language.
	 */
	public String getLanguage() {
		return language;
	}

	// End EPPContactCheckResult.getLanguage()

	/**
	 * Sets domain reason.
	 *
	 * @param aReason Contact Reason to check.
	 */
	public void setContactReason(String aReason) {
		reason = aReason;
	}

	// End EPPContactCheckResult.setContactReason(String)

	/**
	 * Gets domain reason to check.
	 *
	 * @return String of contact reason <code>String</code>'s.
	 */
	public String getContactReason() {
		return reason;
	}

	// End EPPContactCheckResult.getContactReason()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactCheckResult</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPContactCheckResult</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactCheckResult</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (id == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		// Name
		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		// Contact id
		Element nameElm =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_CONTACT_ID);
		root.appendChild(nameElm);

		// Available
		EPPUtil.encodeBooleanAttr(nameElm, ATTR_AVAIL, this.available);

		// Id
		Text textNode = aDocument.createTextNode(id);
		nameElm.appendChild(textNode);

		// Contact Reason
		if (reason != null) {
			Element reasonElm =
				aDocument.createElementNS(EPPContactMapFactory.NS, ELM_CONTACT_REASON);
			root.appendChild(reasonElm);

			// Language
			if (!language.equals(VALUE_LANG)) {
				reasonElm.setAttribute(ATTR_LANG, language);
			}

			// Contact Reason
			Text aReason = aDocument.createTextNode(reason);
			reasonElm.appendChild(aReason);
		}

		return root;
	}

	// End EPPContactCheckResult.encode(Document)

	/**
	 * Decode the <code>EPPContactCheckResult</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactPingResult</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Available
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPContactMapFactory.NS, ELM_CONTACT_ID);
		this.available = EPPUtil.decodeBooleanAttr(currElm, ATTR_AVAIL);

		// Id
		id = currElm.getFirstChild().getNodeValue();

		// Contact Reason
		currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPContactMapFactory.NS, ELM_CONTACT_REASON);

		if (currElm != null) {
			reason = currElm.getFirstChild().getNodeValue();

			// Language
			String lang = currElm.getAttribute(ATTR_LANG);

			if (lang.length() > 0) {
				if (!lang.equals(VALUE_LANG)) {
					setLanguage(lang);
				}
			}
		}
	}

	// End EPPContactCheckResult.decode(Element)

	/**
	 * Compare an instance of <code>EPPContactCheckResult</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactCheckResult)) {
			return false;
		}

		EPPContactCheckResult theComp = (EPPContactCheckResult) aObject;

		// Id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			return false;
		}

		// Available
		if (available != theComp.available) {
			return false;
		}

		return true;
	}

	// End EPPContactCheckResult.equals(Object)

	/**
	 * Clone <code>EPPContactCheckResult</code>.
	 *
	 * @return clone of <code>EPPContactCheckResult</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactCheckResult clone = null;

		clone = (EPPContactCheckResult) super.clone();

		return clone;
	}

	// End EPPContactCheckResult.clone()

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

	// End EPPContactCheckResult.toString()
}
