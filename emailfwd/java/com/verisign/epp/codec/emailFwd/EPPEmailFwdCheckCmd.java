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
 * Represents an EPP EmailFwd &lt;check&gt; command, which is used to determine
 * if a emailFwd name is known to the server.  The &lt;emailFwd:check&gt;
 * element     MUST contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * One or more (up to a maximum of sixteen) &lt;emailFwd:name&gt; elements that
 * contain the fully qualified name of the queried emailFwds.  Use
 * <code>getNames</code>     and <code>setNames</code> to get and set the
 * elements.  Use <code>setName</code>     to set an individual name.
 * </li>
 * </ul>
 * 
 * <br><code>EPPEmailFwdCheckResp</code> is the concrete
 * <code>EPPReponse</code> associated     with
 * <code>EPPEmailFwdCheckCmd</code>.
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResp
 */
public class EPPEmailFwdCheckCmd extends EPPCheckCmd {
	/** Maximum number of emailFwds to check at once. */
	public static final int MAX_EMAILFWDS = 99;

	/** XML Element Name of <code>EPPEmailFwdCheckCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:check";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/**
	 * EmailFwd Names to check.  This is a <code>Vector</code> of
	 * <code>String</code> instances.
	 */
	private Vector names;

	/**
	 * <code>EPPEmailFwdCheckCmd</code> default constructor.  It will set the
	 * names attribute     to an empty <code>Vector</code>.
	 */
	public EPPEmailFwdCheckCmd() {
		names = new Vector();
	}

	// End EPPEmailFwdCheckCmd.EPPEmailFwdCheckCmd()

	/**
	 * <code>EPPEmailFwdCheckCmd</code> constructor that will check an
	 * individual emailFwd name.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name to check
	 */
	public EPPEmailFwdCheckCmd(String aTransId, String aName) {
		super(aTransId);

		names = new Vector();
		names.addElement(aName);
	}

	// End EPPEmailFwdCheckCmd.EPPEmailFwdCheckCmd(String, String)

	/**
	 * <code>EPPEmailFwdCheckCmd</code> constructor that will check a list of
	 * emailFwd names.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param someNames <code>Vector</code> of emailFwd name
	 * 		  <code>String</code> instances.
	 */
	public EPPEmailFwdCheckCmd(String aTransId, Vector someNames) {
		super(aTransId);

		names = someNames;
	}

	// End EPPEmailFwdCheckCmd.EPPEmailFwdCheckCmd(String, Vector)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdCheckCmd</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdCheckCmd.getNamespace()

	/**
	 * Compare an instance of <code>EPPEmailFwdCheckCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdCheckCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdCheckCmd theMap = (EPPEmailFwdCheckCmd) aObject;

		// EmailFwd Names
		if (!EPPUtil.equalVectors(names, theMap.names)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdCheckCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdCheckCmd</code>.
	 *
	 * @return Deep copy clone of <code>EPPEmailFwdCheckCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdCheckCmd clone = (EPPEmailFwdCheckCmd) super.clone();

		clone.names = (Vector) names.clone();

		return clone;
	}

	// End EPPEmailFwdCheckCmd.clone()

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

	// End EPPEmailFwdCheckCmd.toString()

	/**
	 * Sets emailFwd name to check.
	 *
	 * @param aName Name to check.
	 */
	public void setName(String aName) {
		names = new Vector();
		names.addElement(aName);
	}

	// End EPPEmailFwdCheckCmd.setNames(Vector)

	/**
	 * Gets emailFwd names to check.
	 *
	 * @return Vector of emailFwd name <code>String</code>'s.
	 */
	public Vector getNames() {
		return names;
	}

	// End EPPEmailFwdCheckCmd.getNames()

	/**
	 * Sets emailFwd names to check.
	 *
	 * @param someNames Names to check.
	 */
	public void setNames(Vector someNames) {
		names = someNames;
	}

	// End EPPEmailFwdCheckCmd.setNames(Vector)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdCheckCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPEmailFwdCheckCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdCheckCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (names.size() == 0) {
			throw new EPPEncodeException("No emailFwds names specified in EPPEmailFwdCheckCmd");
		}

		if (names.size() > MAX_EMAILFWDS) {
			throw new EPPEncodeException(names.size()
										 + " emailFwd names is greater than the maximum of "
										 + MAX_EMAILFWDS);
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Names
		EPPUtil.encodeVector(
							 aDocument, root, names, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);

		return root;
	}

	// End EPPEmailFwdCheckCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdCheckCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdCheckCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// EmailFwd Names
		names =
			EPPUtil.decodeVector(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		if (names == null) {
			names = new Vector();
		}
	}

	// End EPPEmailFwdCheckCmd.doDecode(Node)
}
