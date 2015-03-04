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

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.codec.persreg;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
// Imports
//----------------------------------------------
// SDK Imports
import com.verisign.epp.codec.gen.*;


/**
 * Personal Registration &ltgenDataType&gt complexType, which is reused
 * by:<br><br>
 * 
 * <ul>
 * <li>
 * &ltcreData&gt - <code>EPPPersRegCreateData</code>
 * </li>
 * <li>
 * &ltrenData&gt - <code>EPPPersRegRenewData</code>
 * </li>
 * <li>
 * &lttrnData&gt - <code>EPPPersRegTransferData</code>
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public abstract class EPPPersRegGenData implements EPPCodecComponent {
	/** XML tag name for the <code>_bundledRate</code> attribute. */
	private static final String ELM_BUNDLED_RATE = "persReg:bundledRate";

	/** Bundled rate flag. */
	private boolean _bundledRate;

	/**
	 * Default constructor.  Bundled flag defaults to <code>false</code>.
	 */
	public EPPPersRegGenData() {
		_bundledRate = false;
	}

	// End EPPPersRegGenData.EPPPersRegGenData()

	/**
	 * Constructor that sets the bundled flag.
	 *
	 * @param aIsBundledRate Does the bundled rate apply?
	 */
	public EPPPersRegGenData(boolean aIsBundledRate) {
		_bundledRate = aIsBundledRate;
	}

	// End EPPPersRegGenData.EPPPersRegGenData(boolean)

	/**
	 * Does the bundled rate apply?
	 *
	 * @return <code>true</code> if bundled rate applies; <code>false</code>
	 * 		   otherwise.
	 */
	public boolean isBundledRate() {
		return _bundledRate;
	}

	// End EPPPersRegGenData.isBundledRate()

	/**
	 * Sets the bundled rate flag.
	 *
	 * @param aIsBundledRate Does the bundled rate apply?
	 */
	public void setIsBundedRate(boolean aIsBundledRate) {
		_bundledRate = aIsBundledRate;
	}

	// End EPPPersRegGenData.setIsBundedRate(boolean)

	/**
	 * Compare an instance of <code>EPPPersGenData</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPPersRegGenData)) {
			return false;
		}

		EPPPersRegGenData theComp = (EPPPersRegGenData) aObject;

		if (_bundledRate != theComp._bundledRate) {
			return false;
		}

		return true;
	}

	// End EPPPersRegGenData.equals(Object)

	/**
	 * encode instance into a DOM element tree. A DOM Document is passed as an
	 * argument and functions as a factory for DOM objects.  The root element
	 * associated with the instance is created and each instance attribute is
	 * appended as a child node.
	 *
	 * @param aDocument DOM Document, which acts is an Element factory
	 *
	 * @return Element Root element associated with the object
	 *
	 * @exception EPPEncodeException Error encoding
	 * 			  <code>EPPPersRegGenData</code>
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPPersRegExtFactory.NS, getRootElm());
		root.setAttribute("xmlns:persReg", EPPPersRegExtFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPPersRegExtFactory.NS_SCHEMA);

		EPPUtil.encodeBoolean(
							  aDocument, root, new Boolean(_bundledRate),
							  EPPPersRegExtFactory.NS, ELM_BUNDLED_RATE);

		return root;
	}

	// End EPPPersRegGenData.encode(Document)

	/**
	 * decode a DOM element tree to initialize the instance attributes.  The
	 * <code>aElement</code> argument represents the root DOM element and is
	 * used to traverse the DOM nodes for instance attribute values.
	 *
	 * @param aElement <code>Element</code> to decode
	 *
	 * @exception EPPDecodeException Error decoding <code>Element</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Bundled Rate
		Boolean val =
			EPPUtil.decodeBoolean(
								  aElement, EPPPersRegExtFactory.NS,
								  ELM_BUNDLED_RATE);

		if (val != null) {
			_bundledRate = val.booleanValue();
		}
		else {
			throw new EPPDecodeException("bundledRate required attribute is not decoded");
		}
	}

	// End EPPPersRegGenData.decode(Element)

	/**
	 * clone an <code>EPPCodecComponent</code>.
	 *
	 * @return clone of concrete <code>EPPPersRegGenData</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPPersRegGenData clone = (EPPPersRegGenData) super.clone();

		return clone;
	}

	// End EPPPersRegGenData.clone()

	/**
	 * Gets the root element name to use.  The derived classes must define what
	 * the root element name is.
	 *
	 * @return Root element name
	 */
	protected abstract String getRootElm();
}


// End class EPPPersRegGenData
