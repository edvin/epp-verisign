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
 * Represents an EPP Domain &lt;check&gt; command, which is used to determine
 * if a domain name is known to the server.  The &lt;domain:check&gt; element
 * MUST contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * One or more (up to a maximum of <code>MAX_DOMAINS</code>) &lt;domain:name&gt; elements that
 * contain the fully qualified name of the queried domains.  Use
 * <code>getNames</code>     and <code>setNames</code> to get and set the
 * elements.  Use <code>setName</code>     to set an individual name.
 * </li>
 * </ul>
 * 
 * <br><code>EPPDomainCheckResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPDomainCheckCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainCheckResp
 */
public class EPPDomainCheckCmd extends EPPCheckCmd {
	/** Maximum number of domains to check at once. */
	public static final int MAX_DOMAINS = 99;

	/** XML Element Name of <code>EPPDomainCheckCmd</code> root element. */
	final static String ELM_NAME = "domain:check";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/**
	 * Domain Names to check.  This is a <code>Vector</code> of
	 * <code>String</code> instances.
	 */
	private Vector names;

	/**
	 * <code>EPPDomainCheckCmd</code> default constructor.  It will set the
	 * names attribute     to an empty <code>Vector</code>.
	 */
	public EPPDomainCheckCmd() {
		names = new Vector();
	}

	// End EPPDomainCheckCmd.EPPDomainCheckCmd()

	/**
	 * <code>EPPDomainCheckCmd</code> constructor that will check an individual
	 * domain name.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name to check
	 */
	public EPPDomainCheckCmd(String aTransId, String aName) {
		super(aTransId);

		names = new Vector();
		names.addElement(aName);
	}

	// End EPPDomainCheckCmd.EPPDomainCheckCmd(String, String)

	/**
	 * <code>EPPDomainCheckCmd</code> constructor that will check a list of
	 * domain names.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param someNames <code>Vector</code> of domain name <code>String</code>
	 * 		  instances.
	 */
	public EPPDomainCheckCmd(String aTransId, Vector someNames) {
		super(aTransId);

		names = someNames;
	}

	// End EPPDomainCheckCmd.EPPDomainCheckCmd(String, Vector)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainCheckCmd</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainCheckCmd.getNamespace()

	/**
	 * Compare an instance of <code>EPPDomainCheckCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainCheckCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainCheckCmd theMap = (EPPDomainCheckCmd) aObject;

		// Domain Names
		if (!EPPUtil.equalVectors(names, theMap.names)) {
			return false;
		}

		return true;
	}

	// End EPPDomainCheckCmd.equals(Object)

	/**
	 * Clone <code>EPPDomainCheckCmd</code>.
	 *
	 * @return Deep copy clone of <code>EPPDomainCheckCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainCheckCmd clone = (EPPDomainCheckCmd) super.clone();

		clone.names = (Vector) names.clone();

		return clone;
	}

	// End EPPDomainCheckCmd.clone()

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

	// End EPPDomainCheckCmd.toString()

	/**
	 * Sets domain name to check.
	 *
	 * @param aName Name to check.
	 */
	public void setName(String aName) {
		names = new Vector();
		names.addElement(aName);
	}

	// End EPPDomainCheckCmd.setNames(Vector)

	/**
	 * Gets domain names to check.
	 *
	 * @return Vector of domain name <code>String</code>'s.
	 */
	public Vector getNames() {
		return names;
	}

	// End EPPDomainCheckCmd.getNames()

	/**
	 * Sets domain names to check.
	 *
	 * @param someNames Names to check.
	 */
	public void setNames(Vector someNames) {
		names = someNames;
	}

	// End EPPDomainCheckCmd.setNames(Vector)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainCheckCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDomainCheckCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainCheckCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (names.size() == 0) {
			throw new EPPEncodeException("No domains names specified in EPPDomainCheckCmd");
		}

		if (names.size() > MAX_DOMAINS) {
			throw new EPPEncodeException(names.size()
										 + " domain names is greater than the maximum of "
										 + MAX_DOMAINS);
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Names
		EPPUtil.encodeVector(
							 aDocument, root, names, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);

		return root;
	}

	// End EPPDomainCheckCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainCheckCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainCheckCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Domain Names
		names =
			EPPUtil.decodeVector(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		if (names == null) {
			names = new Vector();
		}
	}

	// End EPPDomainCheckCmd.doDecode(Node)
}
