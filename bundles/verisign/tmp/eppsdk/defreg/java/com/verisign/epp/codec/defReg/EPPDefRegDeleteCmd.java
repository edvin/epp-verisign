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
package com.verisign.epp.codec.defReg;

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
 * Represents an EPP DefReg &lt;delete&gt; command that allows a client to
 * delete a defReg object. The EPP &lt;delete&gt; command provides a transform
 * operation that allows a client to delete a defReg object. In addition to
 * the standard EPP command elements, the &lt;delete&gt; command MUST contain
 * a "defReg:delete" element that identifies the defReg namespace and the
 * location of the defReg schema.     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:roid&gt; element that contains the Repository Object
 * IDentifier. Use <code>getRoid</code> and <code>setRoid</code> to get and
 * set the Roid.
 * </li>
 * </ul>
 * 
 * <code>EPPReponse</code> is the response associated     with
 * <code>EPPDefRegDeleteCmd</code>.
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPDefRegDeleteCmd extends EPPDeleteCmd {
	/** XML Element Name of <code>EPPDefRegDeleteCmd</code> root element. */
	final static String ELM_NAME = "defReg:delete";

	/** XML Element Roid for the <code>name</code> attribute. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** DEFREG Roid to get information on. */
	private String roid;

	/**
	 * <code>EPPDefRegDeleteCmd</code> default constructor.  The roidis
	 * initialized to <code>null</code>.     The roid must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDefRegDeleteCmd() {
		roid = null;
	}

	// End EPPDefRegDeleteCmd.EPPDefRegDeleteCmd()

	/**
	 * <code>EPPDefRegDeleteCmd</code> constructor that takes the defReg roid
	 * as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid DefReg roid to delete.
	 */
	public EPPDefRegDeleteCmd(String aTransId, String aRoid) {
		super(aTransId);

		roid = aRoid;
	}

	// End EPPDefRegDeleteCmd.EPPDefRegDeleteCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegDeleteCmd</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegDeleteCmd.getNamespace()

	/**
	 * Gets the defReg roid to delete.
	 *
	 * @return DefReg Roid    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegDeleteCmd.getRoid()

	/**
	 * Sets the defReg roid to delete.
	 *
	 * @param aRoid DefReg Roid
	 */
	public void setRoID(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegDeleteCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPDefRegDeleteCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegDeleteCmd theComp = (EPPDefRegDeleteCmd) aObject;

		// Roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
				)) {
			return false;
		}

		return true;
	}

	// End EPPDefRegDeleteCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegDeleteCmd</code>.
	 *
	 * @return clone of <code>EPPDefRegDeleteCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegDeleteCmd clone = (EPPDefRegDeleteCmd) super.clone();

		return clone;
	}

	// End EPPDefRegDeleteCmd.clone()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegDeleteCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDefRegDeleteCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (roid == null) {
			throw new EPPEncodeException("roid required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);

		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Roid DEFREG
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_ROID);
		return root;
	}

	// End EPPDefRegDeleteCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegDeleteCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegDeleteCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// DefReg Roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);
	}

	// End EPPDefRegDeleteCmd.doDecode(Element)
}
