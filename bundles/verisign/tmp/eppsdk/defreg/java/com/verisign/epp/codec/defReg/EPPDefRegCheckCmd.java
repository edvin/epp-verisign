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

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP DefReg &lt;check&gt; command, which is used to determine
 * if a defReg name is known to the server.  The &lt;defReg:check&gt; element
 * MUST contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * One or more (up to a maximum of sixteen) &lt;defReg:name&gt; elements that
 * contain the fully qualified name of the queried defRegs.  Use
 * <code>getNames</code>     and <code>setNames</code> to get and set the
 * elements.  Use <code>setName</code>     to set an individual name.
 * </li>
 * </ul>
 * 
 * <br><code>EPPDefRegCheckResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPDefRegCheckCmd</code>.
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegCheckResp
 */
public class EPPDefRegCheckCmd extends EPPCheckCmd {
	/** Maximum number of defRegs to check at once. */
	public static final int MAX_DEFREGS = 99;

	/** XML Element Name of <code>EPPDefRegCheckCmd</code> root element. */
	final static String ELM_NAME = "defReg:check";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_DEFREG_NAME = "defReg:name";

	/**
	 * DefReg Names to check.  This is a <code>Vector</code> of
	 * <code>String</code> instances.
	 */
	private Vector names;

	/**
	 * <code>EPPDefRegCheckCmd</code> default constructor.  It will set the
	 * names attribute     to an empty <code>Vector</code>.
	 */
	public EPPDefRegCheckCmd() {
		names = new Vector();
	}

	// End EPPDefRegCheckCmd.EPPDefRegCheckCmd()

	/**
	 * <code>EPPDefRegCheckCmd</code> constructor that will check an individual
	 * defReg name.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName DefReg name to check
	 */
	public EPPDefRegCheckCmd(String aTransId, EPPDefRegName aName) {
		super(aTransId);

		names = new Vector();
		names.addElement(aName);
	}

	// End EPPDefRegCheckCmd.EPPDefRegCheckCmd(String, String)

	/**
	 * <code>EPPDefRegCheckCmd</code> constructor that will check a list of
	 * defReg names.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param someNames <code>Vector</code> of defReg name <code>String</code>
	 * 		  instances.
	 */
	public EPPDefRegCheckCmd(String aTransId, Vector someNames) {
		super(aTransId);

		names = someNames;
	}

	// End EPPDefRegCheckCmd.EPPDefRegCheckCmd(String, Vector)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegCheckCmd</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegCheckCmd.getNamespace()

	/**
	 * Compare an instance of <code>EPPDefRegCheckCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegCheckCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegCheckCmd theMap = (EPPDefRegCheckCmd) aObject;

		// DefReg Names
		if (!EPPUtil.equalVectors(names, theMap.names)) {
			return false;
		}

		return true;
	}

	// End EPPDefRegCheckCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegCheckCmd</code>.
	 *
	 * @return Deep copy clone of <code>EPPDefRegCheckCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegCheckCmd clone = (EPPDefRegCheckCmd) super.clone();

		clone.names = (Vector) names.clone();

		return clone;
	}

	// End EPPDefRegCheckCmd.clone()

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

	// End EPPDefRegCheckCmd.toString()

	/**
	 * Sets defReg name to check.
	 *
	 * @param aName Name to check.
	 */
	public void setName(EPPDefRegName aName) {
		names = new Vector();
		names.addElement(aName);
	}

	// End EPPDefRegCheckCmd.setNames(Vector)

	/**
	 * Gets defReg names to check.
	 *
	 * @return Vector of defReg name <code>String</code>'s.
	 */
	public Vector getNames() {
		return names;
	}

	// End EPPDefRegCheckCmd.getNames()

	/**
	 * Sets defReg names to check.
	 *
	 * @param someNames Names to check.
	 */
	public void setNames(Vector someNames) {
		names = someNames;
	}

	// End EPPDefRegCheckCmd.setNames(Vector)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegCheckCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDefRegCheckCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegCheckCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (names.size() == 0) {
			throw new EPPEncodeException("No defRegs names specified in EPPHostCheckCmd");
		}

		if (names.size() > MAX_DEFREGS) {
			throw new EPPEncodeException(names.size()
										 + " defReg names is greater than the maximum of "
										 + MAX_DEFREGS);
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Names
		EPPUtil.encodeCompVector(aDocument, root, names);

		return root;
	}

	// End EPPDefRegCheckCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegCheckCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegCheckCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// DefReg Names
		names =
			EPPUtil.decodeCompVector(
									 aElement, EPPDefRegMapFactory.NS,
									 ELM_DEFREG_NAME, EPPDefRegName.class);

		if (names == null) {
			names = new Vector();
		}
	}

	// End EPPDefRegCheckCmd.doDecode(Node)
}
