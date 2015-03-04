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

package com.verisign.epp.codec.namestoreext;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
// Imports
//----------------------------------------------
// SDK Imports
import com.verisign.epp.codec.gen.*;

/**
 * Namestore &ltnamestoreExt&gt extension, which allows for a client to provide
 * a target sub-product identifier to specify the locus of operation for the
 * accompanying command. <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 */
public class EPPNamestoreExtNamestoreExt implements EPPCodecComponent {

	/**
	 * XML local name for <code>EPPNamestoreExtNamestoreExt</code>.
	 */
	public static final String ELM_LOCALNAME = "namestoreExt";

	/** XML root tag for <code>EPPNamestoreExtNamestoreExt</code>. */
	public static final String ELM_NAME = EPPNamestoreExtExtFactory.NS_PREFIX
			+ ":" + ELM_LOCALNAME;

	/** XML tag name for the <code>_subProductID</code> attribute. */
	private static final String ELM_SUB_PRODUCT_ID = "subProduct";

	/** Sub-Product Identifier. */
	private String _subProductID;

	/**
	 * Default constructor for <code>EPPNamestoreExtNamestoreExt</code>.
	 */
	public EPPNamestoreExtNamestoreExt() {
		_subProductID = null;
	}

	// End EPPNamestoreExtNamestoreExt.EPPNamestoreExtNamestoreExt()

	/**
	 * Constructor for <code>EPPNamestoreExtNamestoreExt</code> that takes the
	 * sub-product identifier.
	 * 
	 * @param aSubProductID
	 *            Sub-product identifier
	 */
	public EPPNamestoreExtNamestoreExt(String aSubProductID) {
		_subProductID = aSubProductID;
	}

	// End EPPNamestoreExtNamestoreExt.EPPNamestoreExtNamestoreExt(String)

	/**
	 * Gets the Namestore Destination Registry Identifier.
	 * 
	 * @return Registry identifier if defined; <code>null</code> otherwise.
	 */
	public String getSubProductID() {
		return _subProductID;
	}

	// End EPPNamestoreExtNamestoreExt.getSubProductID()

	/**
	 * Sets the Namestore Destination Registry Identifier.
	 * 
	 * @param aSubProductID
	 *            Registry Identifier
	 */
	public void setSubProductID(String aSubProductID) {
		_subProductID = aSubProductID;
	}

	// End EPPNamestoreExtNamestoreExt.setSubProductID(String)

	/**
	 * encode instance into a DOM element tree. A DOM Document is passed as an
	 * argument and functions as a factory for DOM objects. The root element
	 * associated with the instance is created and each instance attribute is
	 * appended as a child node.
	 * 
	 * @param aDocument
	 *            DOM Document, which acts is an Element factory
	 * 
	 * @return Element Root element associated with the object
	 * 
	 * @exception EPPEncodeException
	 *                Error encoding <code>EPPNamestoreExtNamestoreExt</code>
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (_subProductID == null) {
			throw new EPPEncodeException(
					"subProductID required attribute is not set");
		}

		Element root = aDocument.createElementNS(EPPNamestoreExtExtFactory.NS,
				ELM_NAME);
		root.setAttribute("xmlns:namestoreExt", EPPNamestoreExtExtFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPNamestoreExtExtFactory.NS_SCHEMA);

		EPPUtil.encodeString(aDocument, root, _subProductID,
				EPPNamestoreExtExtFactory.NS,
				EPPNamestoreExtExtFactory.NS_PREFIX + ":" + ELM_SUB_PRODUCT_ID);

		return root;
	}

	// End implements encode(Document)

	/**
	 * decode a DOM element tree to initialize the instance attributes. The
	 * <code>aElement</code> argument represents the root DOM element and is
	 * used to traverse the DOM nodes for instance attribute values.
	 * 
	 * @param aElement
	 *            <code>Element</code> to decode
	 * 
	 * @exception EPPDecodeException
	 *                Error decoding <code>Element</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		_subProductID = EPPUtil.decodeString(aElement,
				EPPNamestoreExtExtFactory.NS, ELM_SUB_PRODUCT_ID);
	}

	// End implements decode(Element)

	/**
	 * clone an <code>EPPCodecComponent</code>.
	 * 
	 * @return clone of concrete <code>EPPNamestoreExtNamestoreExt</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNamestoreExtNamestoreExt clone = (EPPNamestoreExtNamestoreExt) super
				.clone();

		return clone;
	}

	// End implements clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPNamestoreExtNamestoreExt.toString()

	/**
	 * Compare an instance of <code>EPPNamestoreExtNamestoreExt</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return <code>true</code> if equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNamestoreExtNamestoreExt)) {
			return false;
		}

		EPPNamestoreExtNamestoreExt theComp = (EPPNamestoreExtNamestoreExt) aObject;

		if (!((_subProductID == null) ? (theComp._subProductID == null)
				: _subProductID.equals(theComp._subProductID))) {
			return false;
		}

		return true;
	}

	// End EPPNamestoreExtNamestoreExt.equals(Object)
}

// End class EPPNamestoreExtNamestoreExt
