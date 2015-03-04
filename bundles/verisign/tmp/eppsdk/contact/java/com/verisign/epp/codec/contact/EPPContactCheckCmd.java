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
package com.verisign.epp.codec.contact;

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
 * Represents an EPP Contact &lt;check&gt; command, which is used to determine
 * if a contact id is known to the server. In addition to the standard EPP
 * command elements, the &lt;check&gt; command MUST contain a
 * &lt;contact:check&gt; element that identifies the contact namespace and the
 * location of the contact schema. The &lt;contact:check&gt; element MUST
 * contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * One or more &lt;contact:id&gt; elements.  The     contact id is the the
 * server-unique identifier for the contact.  Use <code>getIds</code>     and
 * <code>setIds</code> to get and set the elements.  Use <code>setId</code> to
 * set an individual id.
 * </li>
 * </ul>
 * 
 * <br><code>EPPContactCheckResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPContactCheckCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactCheckResp
 */
public class EPPContactCheckCmd extends EPPCheckCmd {
	/** XML Element Name of <code>EPPContactCheckCmd</code> root element. */
	final static String ELM_NAME = "contact:check";

	/** XML Element Name for the <code>ids</code> attribute. */
	private final static String ELM_CONTACT_NAME = "contact:id";

	/**
	 * Contact Ids to check.  This is a <code>Vector</code> of
	 * <code>String</code> instances.
	 */
	private Vector ids;

	/**
	 * <code>EPPContactCheckCmd</code> default constructor.  It will set the
	 * ids attribute     to an empty <code>Vector</code>.
	 */
	public EPPContactCheckCmd() {
		ids = new Vector();
	}

	// End EPPContactCheckCmd.EPPContactCheckCmd()

	/**
	 * <code>EPPContactCheckCmd</code> constructor that will check an
	 * individual contact id.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aId Contact id to check
	 */
	public EPPContactCheckCmd(String aTransId, String aId) {
		super(aTransId);

		ids = new Vector();
		ids.addElement(aId);
	}

	// End EPPContactCheckCmd.EPPContactCheckCmd(String, String)

	/**
	 * <code>EPPContactCheckCmd</code> constructor that will check a list of
	 * contact ids.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param someIds <code>Vector</code> of contact id <code>String</code>'s.
	 */
	public EPPContactCheckCmd(String aTransId, Vector someIds) {
		super(aTransId);

		ids = someIds;
	}

	// End EPPContactCheckCmd.EPPContactCheckCmd(String, Vector)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactCheckCmd</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactCheckCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactCheckCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPContactCheckCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactCheckCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (ids.size() == 0) {
			throw new EPPEncodeException("No contact ids specified in EPPContactCheckCmd");
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// Ids
		EPPUtil.encodeVector(
							 aDocument, root, ids, EPPContactMapFactory.NS,
							 ELM_CONTACT_NAME);

		return root;
	}

	// End EPPContactCheckCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPContactCheckCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactCheckCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Contact Ids
		ids = EPPUtil.decodeVector(
								   aElement, EPPContactMapFactory.NS,
								   ELM_CONTACT_NAME);

		if (ids == null) {
			ids = new Vector();
		}
	}

	// End EPPContactCheckCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPContactCheckCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactCheckCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactCheckCmd theComp = (EPPContactCheckCmd) aObject;

		// Contact Ids
		if (!EPPUtil.equalVectors(ids, theComp.ids)) {
			return false;
		}

		return true;
	}

	// End EPPContactCheckCmd.equals(Object)

	/**
	 * Clone <code>EPPContactCheckCmd</code>.
	 *
	 * @return clone of <code>EPPContactCheckCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactCheckCmd clone = (EPPContactCheckCmd) super.clone();

		clone.ids = (Vector) ids.clone();

		return clone;
	}

	// End EPPContactCheckCmd.clone()

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

	// End EPPContactCheckCmd.toString()

	/**
	 * Sets contact id to check.
	 *
	 * @param aId Id to check.
	 */
	public void setId(String aId) {
		ids = new Vector();
		ids.addElement(aId);
	}

	// End EPPContactCheckCmd.setIds(Vector)

	/**
	 * Gets contact ids to check.
	 *
	 * @return <code>Vector</code> of contact id <code>String</code>'s.
	 */
	public Vector getIds() {
		return ids;
	}

	// End EPPContactCheckCmd.getIds()

	/**
	 * Sets contact ids to check.
	 *
	 * @param someIds Ids to check.
	 */
	public void setIds(Vector someIds) {
		ids = someIds;
	}

	// End EPPContactCheckCmd.setIds(Vector)
}
