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
 * Represents a emailFwd contact.  The valid contact types are defined by the
 * <code>EPPEmailFwdContact.TYPE_</code> constants.  A contact has a type and
 * a name     that must be known to the EPP Server.
 */
public class EPPEmailFwdContact implements EPPCodecComponent {
	/** Administrative EmailFwd Contact */
	public final static String TYPE_ADMINISTRATIVE = "admin";

	/** Billing EmailFwd Contact */
	public final static String TYPE_BILLING = "billing";

	/** Technical EmailFwd Contact */
	public final static String TYPE_TECHNICAL = "tech";

	/** XML root tag name for <code>EPPEmailFwdContact</code>. */
	final static String ELM_NAME = "emailFwd:contact";

	/** XML tag name for the <code>type</code> attribute. */
	private final static String ATTR_TYPE = "type";

	/**
	 * Type of contact defined by a <code>EPPEmailFwdContact.TYPE_</code>
	 * constant.
	 */
	private String type;

	/** Name of contact. */
	private String name;

	/**
	 * Default constructor for EPPEmailFwdContact.  The attribute default to
	 * <code>null</code> and must be set before invoking <code>encode</code>.
	 */
	public EPPEmailFwdContact() {
		name     = null;
		type     = null;
	}

	// End EPPEmailFwdContact.EPPEmailFwdContact()

	/**
	 * EPPEmailFwdContact which takes all attributes as arguments (name, type).
	 *
	 * @param aName Contact Name
	 * @param aType Contact Type, which should be a
	 * 		  <code>EPPEmailFwdContact.TYPE_</code> constant.
	 */
	public EPPEmailFwdContact(String aName, String aType) {
		name     = aName;
		type     = aType;
	}

	// End EPPEmailFwdContact.EPPEmailFwdContact()

	/**
	 * Gets the contact name
	 *
	 * @return Contact Name
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdContact.getName()

	/**
	 * Sets the contact name.
	 *
	 * @param aName Contact Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdContact.setName(String)

	/**
	 * Gets the contact type.
	 *
	 * @return DOCUMENT ME!
	 */
	public String getType() {
		return type;
	}

	// End EPPEmailFwdContact.getType()

	/**
	 * Sets the contact type to one of the
	 * <code>EPPEmailFwdContact.TYPE_</code> constants.
	 *
	 * @param aType <code>EPPEmailFwdContact.TYPE_</code> constant.
	 */
	public void setType(String aType) {
		type = aType;
	}

	// End EPPEmailFwdContact.setType(String)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPEmailFwdContact
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the
	 * 		   EPPEmailFwdContact instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPEmailFwdContact
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		// Type
		if (type == null) {
			throw new EPPEncodeException("EPPEmailFwdContact type is null on call to encode");
		}

		root.setAttribute(ATTR_TYPE, type);

		// Name
		if (name == null) {
			throw new EPPEncodeException("EPPEmailFwdContact name is null on call to encode");
		}

		Text textNode = aDocument.createTextNode(name);
		root.appendChild(textNode);

		return root;
	}

	// End EPPEmailFwdContact.encode(Document)

	/**
	 * Decode the EPPEmailFwdContact attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPEmailFwdContact from.
	 *
	 * @exception EPPDecodeException - Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Type
		type     = aElement.getAttribute(ATTR_TYPE);

		// Name
		name = aElement.getFirstChild().getNodeValue();
	}

	// End EPPEmailFwdContact.decode(Element)

	/**
	 * implements a deep <code>EPPEmailFwdContact</code> compare.
	 *
	 * @param aObject <code>EPPEmailFwdContact</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdContact)) {
			return false;
		}

		EPPEmailFwdContact theComp = (EPPEmailFwdContact) aObject;

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

	// End EPPEmailFwdContact.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdContact</code>.
	 *
	 * @return clone of <code>EPPEmailFwdContact</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdContact clone = null;

		clone = (EPPEmailFwdContact) super.clone();

		return clone;
	}

	// End EPPEmailFwdContact.clone()

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

	// End EPPEmailFwdContact.toString()
}
