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
 * Represents an EPP Host &lt;delete&gt; command that allows a client to delete
 * a host object. The EPP &lt;delete&gt; command provides a transform
 * operation that allows a client to delete a host object. In addition to the
 * standard EPP command elements, the &lt;delete&gt; command MUST contain a
 * &lt;host:delete&gt; element that identifies the host namespace and the
 * location of the host schema.  The &lt;host:delete&gt; element SHALL contain
 * the following child elements: <br>
 * A &lt;host:name&gt; element that contains the fully qualified name of the
 * host object to be deleted. Use <code>getName</code> and
 * <code>setName</code>     to get and set the element. <br>
 * <br>
 * A host name object MUST NOT be deleted if the host object is associated
 * with any other object.  For example, if the host object is associated with
 * a domain object, the host object MUST NOT be deleted until the existing
 * association has been broken.     <br>
 * <code>EPPReponse</code> is the response associated     with
 * <code>EPPHostDeleteCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPHostDeleteCmd extends EPPDeleteCmd {
	/** XML Element Name of <code>EPPHostDeleteCmd</code> root element. */
	final static String ELM_NAME = "host:delete";

	/** XML Element Name of a host name in a <code>EPPHostDeleteCmd</code>. */
	private final static String ELM_HOST_NAME = "host:name";

	/** Host Name of host to delete. */
	private String name;

	/**
	 * <code>EPPHostDeleteCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPHostDeleteCmd() {
		name = null;
	}

	// End EPPHostDeleteCmd.EPPHostDeleteCmd()

	/**
	 * <code>EPPHostDeleteCmd</code> constructor that takes the host name as an
	 * argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Host name to delete.
	 */
	public EPPHostDeleteCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPHostDeleteCmd.EPPHostDeleteCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPHostDeleteCmd</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostDeleteCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostDeleteCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPHostDeleteCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("required attribute name is not set");
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPHostMapFactory.NS,
							 ELM_HOST_NAME);

		return root;
	}

	// End EPPHostDeleteCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPHostDeleteCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostDeleteCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Host Name
		name =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);
	}

	// End EPPHostDeleteCmd.doDecode(Element)

	/**
	 * Gets the host name to delete.
	 *
	 * @return Host Name    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPHostDeleteCmd.getName()

	/**
	 * Sets the host name to delete.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostDeleteCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPHostDeleteCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostDeleteCmd theComp = (EPPHostDeleteCmd) aObject;

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

	// End EPPHostDeleteCmd.equals(Object)

	/**
	 * Clone <code>EPPHostDeleteCmd</code>.
	 *
	 * @return clone of <code>EPPHostDeleteCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostDeleteCmd clone = (EPPHostDeleteCmd) super.clone();

		return clone;
	}

	// End EPPHostDeleteCmd.clone()

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

	// End EPPHostDeleteCmd.toString()
}
