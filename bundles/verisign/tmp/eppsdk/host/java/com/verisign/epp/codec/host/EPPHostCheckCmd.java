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
package com.verisign.epp.codec.host;

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
 * Represents an EPP Host &lt;check&gt; command, which is used to determine if
 * a host name is known to the server.  The &lt;host:check&gt; element MUST
 * contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * One or more &lt;host:name&gt; elements that contain the fully qualified name
 * of the queried hosts.  Use <code>getNames</code>     and
 * <code>setNames</code> to get and set the elements.  Use
 * <code>setNames</code>     to set an individual name.
 * </li>
 * </ul>
 * 
 * <br><br>
 * <code>EPPHostCheckResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPHostCheckCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.host.EPPHostCheckResp
 */
public class EPPHostCheckCmd extends EPPCheckCmd {
	/** XML Element Name of <code>EPPHostCheckCmd</code> root element. */
	final static String ELM_NAME = "host:check";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_HOST_NAME = "host:name";

	/**
	 * Host Names to check.  This is a <code>Vector</code> of
	 * <code>String</code> instances.
	 */
	private Vector names;

	/**
	 * <code>EPPHostCheckCmd</code> default constructor.  It will set the names
	 * attribute     to an empty <code>Vector</code>.
	 */
	public EPPHostCheckCmd() {
		names = new Vector();
	}

	// End EPPHostCheckCmd.EPPHostCheckCmd()

	/**
	 * <code>EPPHostCheckCmd</code> constructor that will check an individual
	 * host name.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Host name to check
	 */
	public EPPHostCheckCmd(String aTransId, String aName) {
		super(aTransId);

		names = new Vector();
		names.addElement(aName);
	}

	// End EPPHostCheckCmd.EPPHostCheckCmd(String, String)

	/**
	 * <code>EPPHostCheckCmd</code> constructor that will check a list of host
	 * names.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param someNames <code>Vector</code> of host name <code>String</code>
	 * 		  instances.
	 */
	public EPPHostCheckCmd(String aTransId, Vector someNames) {
		super(aTransId);

		names = someNames;
	}

	// End EPPHostCheckCmd.EPPHostCheckCmd(String, Vector)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPHostCheckCmd</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostCheckCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostCheckCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPHostCheckCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostCheckCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (names.size() == 0) {
			throw new EPPEncodeException("No host names specified in EPPHostCheckCmd");
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Names
		EPPUtil.encodeVector(
							 aDocument, root, names, EPPHostMapFactory.NS,
							 ELM_HOST_NAME);

		return root;
	}

	// End EPPHostCheckCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPHostCheckCmd</code> attributes from the aElement DOM
	 * Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostCheckCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Host Names
		names =
			EPPUtil.decodeVector(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);

		if (names == null) {
			names = new Vector();
		}
	}

	// End EPPHostCheckCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPHostCheckCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostCheckCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostCheckCmd theMap = (EPPHostCheckCmd) aObject;

		// Host Names
		if (!EPPUtil.equalVectors(names, theMap.names)) {
			return false;
		}

		return true;
	}

	// End EPPHostCheckCmd.equals(Object)

	/**
	 * Clone <code>EPPHostCheckCmd</code>.
	 *
	 * @return clone of <code>EPPHostCheckCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostCheckCmd clone = (EPPHostCheckCmd) super.clone();

		clone.names = (Vector) names.clone();

		return clone;
	}

	// End EPPHostCheckCmd.clone()

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

	// End EPPHostCheckCmd.toString()

	/**
	 * Sets host name to check.
	 *
	 * @param aName Name to check.
	 */
	public void setName(String aName) {
		names = new Vector();
		names.addElement(aName);
	}

	// End EPPHostCheckCmd.setNames(Vector)

	/**
	 * Gets host names to check.
	 *
	 * @return Vector of host name <code>String</code>'s.
	 */
	public Vector getNames() {
		return names;
	}

	// End EPPHostCheckCmd.getNames()

	/**
	 * Sets host names to check.
	 *
	 * @param someNames Names to check.
	 */
	public void setNames(Vector someNames) {
		names = someNames;
	}

	// End EPPHostCheckCmd.setNames(Vector)
}
