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
 * Represents an EPP Contact &lt;delete&gt; command that allows a client to
 * delete a contact object. In addition to the standard EPP command elements,
 * the &lt;delete&gt; command MUST contain a "contact:delete" element that
 * identifies the contact namespace and the location of the contact schema.
 * The "contact:delete" element SHALL contain the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier for
 * the contact to be deleted.  Use <code>getId</code> and <code>setId</code>
 * to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPContactDeleteCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPContactDeleteCmd extends EPPDeleteCmd {
	/** XML Element Name of <code>EPPContactDeleteCmd</code> root element. */
	final static String ELM_NAME = "contact:delete";

	/**
	 * XML Element Name of a contact id in a <code>EPPContactDeleteCmd</code>.
	 */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** Contact Name of contact to delete. */
	private String id;

	/**
	 * <code>EPPContactDeleteCmd</code> default constructor.  The id is
	 * initialized to <code>null</code>.     The id must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPContactDeleteCmd() {
		id = null;
	}

	// End EPPContactDeleteCmd.EPPContactDeleteCmd()

	/**
	 * <code>EPPContactDeleteCmd</code> constructor that takes the contact id
	 * as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aId Contact id to delete.
	 */
	public EPPContactDeleteCmd(String aTransId, String aId) {
		super(aTransId);

		id = aId;
	}

	// End EPPContactDeleteCmd.EPPContactDeleteCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactDeleteCmd</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactDeleteCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactDeleteCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPContactDeleteCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (id == null) {
			throw new EPPEncodeException("required attribute id is not set");
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

		return root;
	}

	// End EPPContactDeleteCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPContactDeleteCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactDeleteCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Contact Id
		id = EPPUtil.decodeString(
								  aElement, EPPContactMapFactory.NS,
								  ELM_CONTACT_ID);
	}

	// End EPPContactDeleteCmd.doDecode(Element)

	/**
	 * Gets the contact id to delete.
	 *
	 * @return Contact Id    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getId() {
		return id;
	}

	// End EPPContactDeleteCmd.getId()

	/**
	 * Sets the contact id to delete.
	 *
	 * @param aId Contact Id
	 */
	public void setId(String aId) {
		id = aId;
	}

	// End EPPContactDeleteCmd.setId(String)

	/**
	 * Compare an instance of <code>EPPContactDeleteCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactDeleteCmd theComp = (EPPContactDeleteCmd) aObject;

		// Id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			return false;
		}

		return true;
	}

	// End EPPContactDeleteCmd.equals(Object)

	/**
	 * Clone <code>EPPContactDeleteCmd</code>.
	 *
	 * @return clone of <code>EPPContactDeleteCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactDeleteCmd clone = (EPPContactDeleteCmd) super.clone();

		return clone;
	}

	// End EPPContactDeleteCmd.clone()

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

	// End EPPContactDeleteCmd.toString()
}
