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
package com.verisign.epp.codec.domain;

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
 * Represents an EPP Domain &lt;delete&gt; command that allows a client to
 * delete a domain object. The EPP &lt;delete&gt; command provides a transform
 * operation that allows a client to delete a domain object. In addition to
 * the standard EPP command elements, the &lt;delete&gt; command MUST contain
 * a "domain:delete" element that identifies the domain namespace and the
 * location of the domain schema. A domain object SHOULD NOT be deleted if
 * subordinate host objects are associated with the domain object.  For
 * example, if domain "example.com" exists, and host object "ns1.example.com"
 * also exists, then domain "example.com" SHOULD NOT be deleted until host
 * "ns1.example.com" has been either deleted or renamed to exist in a
 * different superordinate domain.     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * of the object to be deleted.  Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPDomainDeleteCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPDomainDeleteCmd extends EPPDeleteCmd {
	/** XML Element Name of <code>EPPDomainDeleteCmd</code> root element. */
	final static String ELM_NAME = "domain:delete";

	/**
	 * XML Element Name of a domain name in a <code>EPPDomainDeleteCmd</code>.
	 */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** Domain Name of domain to delete. */
	private String name;

	/**
	 * <code>EPPDomainDeleteCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDomainDeleteCmd() {
		name = null;
	}

	// End EPPDomainDeleteCmd.EPPDomainDeleteCmd()

	/**
	 * <code>EPPDomainDeleteCmd</code> constructor that takes the domain name
	 * as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name to delete.
	 */
	public EPPDomainDeleteCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPDomainDeleteCmd.EPPDomainDeleteCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainDeleteCmd</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainDeleteCmd.getNamespace()

	/**
	 * Gets the domain name to delete.
	 *
	 * @return Domain Name    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainDeleteCmd.getName()

	/**
	 * Sets the domain name to delete.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainDeleteCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPDomainDeleteCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainDeleteCmd theComp = (EPPDomainDeleteCmd) aObject;

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

	// End EPPDomainDeleteCmd.equals(Object)

	/**
	 * Clone <code>EPPDomainDeleteCmd</code>.
	 *
	 * @return clone of <code>EPPDomainDeleteCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainDeleteCmd clone = (EPPDomainDeleteCmd) super.clone();

		return clone;
	}

	// End EPPDomainDeleteCmd.clone()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainDeleteCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDomainDeleteCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);

		return root;
	}

	// End EPPDomainDeleteCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainDeleteCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainDeleteCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Domain Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);
	}

	// End EPPDomainDeleteCmd.doDecode(Element)
}
