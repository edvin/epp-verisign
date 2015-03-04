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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Text;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a domain contact.  The valid contact types are defined by the
 * <code>EPPDomainContact.TYPE_</code> constants.  A contact has a type and a
 * name     that must be known to the EPP Server.      <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPDomainContact implements EPPCodecComponent {
	/** Administrative Domain Contact */
	public final static String TYPE_ADMINISTRATIVE = "admin";

	/** Billing Domain Contact */
	public final static String TYPE_BILLING = "billing";

	/** Technical Domain Contact */
	public final static String TYPE_TECHNICAL = "tech";

	/** XML root tag name for <code>EPPDomainContact</code>. */
	final static String ELM_NAME = "domain:contact";

	/** XML tag name for the <code>type</code> attribute. */
	private final static String ATTR_TYPE = "type";

	/**
	 * Type of contact defined by a <code>EPPDomainContact.TYPE_</code>
	 * constant.
	 */
	private String type;

	/** Name of contact. */
	private String name;

	/**
	 * Default constructor for EPPDomainContact.  The attribute default to
	 * <code>null</code> and must be set before invoking <code>encode</code>.
	 */
	public EPPDomainContact() {
		name     = null;
		type     = null;
	}

	// End EPPDomainContact.EPPDomainContact()

	/**
	 * EPPDomainContact which takes all attributes as arguments (name, type).
	 *
	 * @param aName Contact Name
	 * @param aType Contact Type, which should be a
	 * 		  <code>EPPDomainContact.TYPE_</code> constant.
	 */
	public EPPDomainContact(String aName, String aType) {
		name     = aName;
		type     = aType;
	}

	// End EPPDomainContact.EPPDomainContact()

	/**
	 * Gets the contact name
	 *
	 * @return Contact Name
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainContact.getName()

	/**
	 * Sets the contact name.
	 *
	 * @param aName Contact Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainContact.setName(String)

	/**
	 * Gets the contact type.
	 *
	 * @return DOCUMENT ME!
	 */
	public String getType() {
		return type;
	}

	// End EPPDomainContact.getType()

	/**
	 * Sets the contact type to one of the <code>EPPDomainContact.TYPE_</code>
	 * constants.
	 *
	 * @param aType <code>EPPDomainContact.TYPE_</code> constant.
	 */
	public void setType(String aType) {
		type = aType;
	}

	// End EPPDomainContact.setType(String)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPDomainContact
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPDomainContact
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPDomainContact
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		// Type
		if (type == null) {
			throw new EPPEncodeException("EPPDomainContact type is null on call to encode");
		}

		root.setAttribute(ATTR_TYPE, type);

		// Name
		if (name == null) {
			throw new EPPEncodeException("EPPDomainContact name is null on call to encode");
		}

		Text textNode = aDocument.createTextNode(name);
		root.appendChild(textNode);

		return root;
	}

	// End EPPDomainContact.encode(Document)

	/**
	 * Decode the EPPDomainContact attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPDomainContact from.
	 *
	 * @exception EPPDecodeException - Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Type
		type     = aElement.getAttribute(ATTR_TYPE);

		// Name		
		name = aElement.getFirstChild().getNodeValue();
	}

	// End EPPDomainContact.decode(Element)

	/**
	 * implements a deep <code>EPPDomainContact</code> compare.
	 *
	 * @param aObject <code>EPPDomainContact</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainContact)) {
			return false;
		}

		EPPDomainContact theComp = (EPPDomainContact) aObject;

		// Name	
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Type
		if (
			!(
					(type == null) ? (theComp.type == null)
									   : type.equals(theComp.type)
				)) {
			return false;
		}

		return true;
	}

	// End EPPDomainContact.equals(Object)

	/**
	 * Clone <code>EPPDomainContact</code>.
	 *
	 * @return clone of <code>EPPDomainContact</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainContact clone = null;

		clone = (EPPDomainContact) super.clone();

		return clone;
	}

	// End EPPDomainContact.clone()

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

	// End EPPDomainContact.toString()
}
