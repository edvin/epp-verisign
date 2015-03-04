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
 * Represents an EPP Host &lt;info&gt; command that is used to retrieve
 * information associated with  a host.  The &lt;host:info&gt; element MUST
 * contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains the fully qualified host     name
 * for which information is requested.  Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 * <code>EPPHostInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPHostInfoCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.host.EPPHostInfoResp
 */
public class EPPHostInfoCmd extends EPPInfoCmd {
	/** XML Element Name of <code>EPPHostInfoCmd</code> root element. */
	final static String ELM_NAME = "host:info";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_HOST_NAME = "host:name";

	/** Host Name to get information on. */
	private String name;

	/**
	 * <code>EPPHostInfoCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPHostInfoCmd() {
		name = null;
	}

	// End EPPHostInfoCmd.EPPHostInfoCmd()

	/**
	 * <code>EPPHostInfoCmd</code> constructor that takes the host name as an
	 * argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Host name to get information on.
	 */
	public EPPHostInfoCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPHostInfoCmd.EPPHostInfoCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPHostInfoCmd</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostInfoCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostInfoCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPHostInfoCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostInfoCmd</code> instance.
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

	// End EPPHostInfoCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPHostInfoCmd</code> attributes from the aElement DOM
	 * Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostInfoCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Host Name
		name =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);
	}

	// End EPPHostInfoCmd.doDecode(Node)

	/**
	 * Gets the host name to get information on.
	 *
	 * @return Host Name    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPHostInfoCmd.getName()

	/**
	 * Sets the host name to get information on.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostInfoCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPHostInfoCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostInfoCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostInfoCmd theComp = (EPPHostInfoCmd) aObject;

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

	// End EPPHostInfoCmd.equals(Object)

	/**
	 * Clone <code>EPPHostInfoCmd</code>.
	 *
	 * @return clone of <code>EPPHostInfoCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostInfoCmd clone = (EPPHostInfoCmd) super.clone();

		return clone;
	}

	// End EPPHostInfoCmd.clone()

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

	// End EPPHostInfoCmd.toString()
}
