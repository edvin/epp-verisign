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


// W3C Imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;


/**
 * Represents a contact disclose name definition.<br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPContactDiscloseAddress implements EPPCodecComponent {
	/**
	 * XML Element Name of <code>EPPContactDiscloseAddress</code> root element.
	 */
	final static java.lang.String ELM_NAME = "contact:addr";

	/** XML Attribute Name for disclose type. */
	private final static String ATTR_TYPE = "type";

	/** Value of the LOC in contact disclose type mapping */
	public final static java.lang.String ATTR_TYPE_LOC = "loc";

	/** Value of the INT in contact disclose type mapping */
	public final static java.lang.String ATTR_TYPE_INT = "int";

	/** The type value of this Type component */
	private String type = ATTR_TYPE_LOC;

	/**
	 * Create a new instance of EPPContactDiscloseAddress
	 */
	public EPPContactDiscloseAddress() {
	}

	/**
	 * Create a new instance of EPPContactDiscloseAddress with the given type
	 *
	 * @param aType the type value to use for this instance.  Should use one of
	 * 		  the static constants defined for this class as a value.
	 */
	public EPPContactDiscloseAddress(String aType) {
		type = aType;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactDiscloseAddress</code> instance.
	 *
	 * @param aDocument The DOM Document to append data to
	 *
	 * @return The encoded DOM <code>Element</code>
	 *
	 * @throws EPPEncodeException Error encoding 
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			//Validate States
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPContactDiscloseAddress invalid state: "
										 + e);
		}

		// creditThreshold with Attributes
		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		// add attribute type
		root.setAttribute(ATTR_TYPE, type);

		return root;
	}

	/**
	 * Decode the <code>EPPContactDisclose</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement The root element of the report fragment of XML
	 *
	 * @throws EPPDecodeException
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Type
		type = aElement.getAttribute(ATTR_TYPE);
	}

	/**
	 * implements a deep <code>EPPContactDiscloseAddress</code> compare.
	 *
	 * @param aObject <code>EPPContactDiscloseAddress</code> instance to
	 * 		  compare with
	 *
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactDiscloseAddress)) {
			return false;
		}

		EPPContactDiscloseAddress theComp = (EPPContactDiscloseAddress) aObject;

		// type
		if (!type.equals(theComp.type)) {
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPContactDiscloseAddress</code>
	 * instance. A valid state means that all of the required attributes have
	 * been set. If validateState returns without an exception, the state is
	 * valid. If the state is not valid, the <code>EPPCodecException</code>
	 * will contain a description of the error.  throws EPPCodecException
	 * State error. This will contain the name of the attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException Thrown if the instance is in an invalid state
	 */
	void validateState() throws EPPCodecException {
		//type
		if (type == null) {
			throw new EPPCodecException("EPPContactDiscloseAddress1 required attribute is not set");
		}
	}

	/**
	 * Clone <code>EPPContactDiscloseAddress</code>.
	 *
	 * @return clone of <code>EPPContactDiscloseAddress</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactDiscloseAddress clone = null;

		clone     = (EPPContactDiscloseAddress) super.clone();

		clone.type = type;

		return clone;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getType() {
		return type;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param aType DOCUMENT ME!
	 */
	public void setType(String aType) {
		this.type = aType;
	}
}
