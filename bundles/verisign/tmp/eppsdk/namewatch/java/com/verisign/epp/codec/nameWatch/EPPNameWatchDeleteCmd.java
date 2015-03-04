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
package com.verisign.epp.codec.nameWatch;

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
 * Represents an EPP NameWatch &lt;delete&gt; command that allows a client to
 * delete a nameWatch object. The EPP &lt;delete&gt; command provides a
 * transform operation that allows a client to delete a nameWatch object. In
 * addition to the standard EPP command elements, the &lt;delete&gt; command
 * MUST contain a "nameWatch:delete" element that identifies the nameWatch
 * namespace and the location of the nameWatch schema.     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the fully qualified nameWatch
 * name of the object to be deleted.  Use <code>getRoid</code> and
 * <code>setRoid</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPNameWatchDeleteCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPNameWatchDeleteCmd extends EPPDeleteCmd {
	/** XML Element Name of <code>EPPNameWatchDeleteCmd</code> root element. */
	final static String ELM_NAME = "nameWatch:delete";

	/**
	 * XML Element Name of a nameWatch roid in a
	 * <code>EPPNameWatchDeleteCmd</code>.
	 */
	private final static String ELM_ROID = "nameWatch:roid";

	/** Roid of nameWatch to delete. */
	private String roid;

	/**
	 * <code>EPPNameWatchDeleteCmd</code> default constructor.  The roid is
	 * initialized to <code>null</code>.     The roid must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPNameWatchDeleteCmd() {
		roid = null;
	}

	// End EPPNameWatchDeleteCmd.EPPNameWatchDeleteCmd()

	/**
	 * <code>EPPNameWatchDeleteCmd</code> constructor that takes the nameWatch
	 * roid as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid NameWatch roid to delete.
	 */
	public EPPNameWatchDeleteCmd(String aTransId, String aRoid) {
		super(aTransId);

		roid = aRoid;
	}

	// End EPPNameWatchDeleteCmd.EPPNameWatchDeleteCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchDeleteCmd</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchDeleteCmd.getNamespace()

	/**
	 * Gets the nameWatch roid to delete.
	 *
	 * @return NameWatch roid
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchDeleteCmd.getRoid()

	/**
	 * Sets the nameWatch roid to delete.
	 *
	 * @param aRoid NameWatch roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchDeleteCmd.setRoid(String)

	/**
	 * Compare an instance of <code>EPPNameWatchDeleteCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchDeleteCmd theComp = (EPPNameWatchDeleteCmd) aObject;

		// roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
				)) {
			return false;
		}

		return true;
	}

	// End EPPNameWatchDeleteCmd.equals(Object)

	/**
	 * Clone <code>EPPNameWatchDeleteCmd</code>.
	 *
	 * @return clone of <code>EPPNameWatchDeleteCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchDeleteCmd clone = (EPPNameWatchDeleteCmd) super.clone();

		return clone;
	}

	// End EPPNameWatchDeleteCmd.clone()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchDeleteCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchDeleteCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (roid == null) {
			throw new EPPEncodeException("roid required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		return root;
	}

	// End EPPNameWatchDeleteCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchDeleteCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchDeleteCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// NameWatch roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		if (roid == null) {
			throw new EPPDecodeException("roid required attribute is not set");
		}
	}

	// End EPPNameWatchDeleteCmd.doDecode(Element)
}
