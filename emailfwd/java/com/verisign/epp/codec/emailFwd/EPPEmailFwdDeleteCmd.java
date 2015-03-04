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

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP EmailFwd &lt;delete&gt; command that allows a client to
 * delete a emailFwd object. The EPP &lt;delete&gt; command provides a
 * transform operation that allows a client to delete a emailFwd object. In
 * addition to the standard EPP command elements, the &lt;delete&gt; command
 * MUST contain a "emailFwd:delete" element that identifies the emailFwd
 * namespace and the location of the emailFwd schema.     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name of the object to be deleted.  Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPEmailFwdDeleteCmd</code>.
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPEmailFwdDeleteCmd extends EPPDeleteCmd {
	/** XML Element Name of <code>EPPEmailFwdDeleteCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:delete";

	/**
	 * XML Element Name of a emailFwd name in a
	 * <code>EPPEmailFwdDeleteCmd</code>.
	 */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** EmailFwd Name of emailFwd to delete. */
	private String name;

	/**
	 * <code>EPPEmailFwdDeleteCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPEmailFwdDeleteCmd() {
		name = null;
	}

	// End EPPEmailFwdDeleteCmd.EPPEmailFwdDeleteCmd()

	/**
	 * <code>EPPEmailFwdDeleteCmd</code> constructor that takes the emailFwd
	 * name as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name to delete.
	 */
	public EPPEmailFwdDeleteCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPEmailFwdDeleteCmd.EPPEmailFwdDeleteCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdDeleteCmd</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdDeleteCmd.getNamespace()

	/**
	 * Gets the emailFwd name to delete.
	 *
	 * @return EmailFwd Name    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdDeleteCmd.getName()

	/**
	 * Sets the emailFwd name to delete.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdDeleteCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPEmailFwdDeleteCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdDeleteCmd theComp = (EPPEmailFwdDeleteCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdDeleteCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdDeleteCmd</code>.
	 *
	 * @return clone of <code>EPPEmailFwdDeleteCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdDeleteCmd clone = (EPPEmailFwdDeleteCmd) super.clone();

		return clone;
	}

	// End EPPEmailFwdDeleteCmd.clone()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdDeleteCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPEmailFwdDeleteCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);

		return root;
	}

	// End EPPEmailFwdDeleteCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdDeleteCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdDeleteCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// EmailFwd Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);
	}

	// End EPPEmailFwdDeleteCmd.doDecode(Element)
}
