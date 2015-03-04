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
package com.verisign.epp.codec.gen;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
import org.w3c.dom.Element;


/**
 * The EPP &ltlogout&gt command is used to end a session with an EPP server. In
 * addition to the standard EPP command elements, the &ltlogout&gt command
 * SHALL contain an empty &ltlogout&gt command element.  A server MAY also end
 * a session asynchronously due to client inactivity or excessive client
 * session longevity.  The parameters for determining excessive client
 * inactivity or session longevity are a matter of server policy and are not
 * specified by this protocol.
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPLogoutCmd extends EPPCommand {
	/** XML root tag name for <code>EPPLogoutCmd</code>. */
	final static String ELM_NAME = "logout";

	/**
	 * Default constructor for EPPLogoutCmd.
	 */
	public EPPLogoutCmd() {
	}

	// End EPPLogoutCmd.EPPLogoutCmd()

	/**
	 * Constructor that takes the transaction id required of all
	 * <code>EPPCommand</code>'s.
	 *
	 * @param aTransId DOCUMENT ME!
	 */
	public EPPLogoutCmd(String aTransId) {
		super(aTransId);
	}

	// End EPPLogoutCmd.EPPLogoutCmd(String)

	/**
	 * Get the EPP command type associated with <code>EPPLogoutCmd</code>.
	 *
	 * @return <code>EPPCommand.TYPE_LOGOUT</code>
	 */
	public String getType() {
		return EPPCommand.TYPE_LOGOUT;
	}

	// End EPPLogoutCmd.getType()

	/**
	 * Get the EPP command Namespace associated with <code>EPPLogoutCmd</code>.
	 *
	 * @return <code>EPPCodec.NS</code>
	 */
	public String getNamespace() {
		return EPPCodec.NS;
	}

	// End EPPLogoutCmd.getNamespace()

	/**
	 * encode <code>EPPLoginCmd</code> into a DOM element tree.  The
	 * &ltlogout&gt element     is created and there are currently no
	 * attribute nodes are appended as children.     This method is part of
	 * the Template Design Pattern, where <code>EPPCommand</code>     provides
	 * the public <code>encode</code> and calls the abstract
	 * <code>doGenEncode</code>.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &ltlogout&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	protected Element doGenEncode(Document aDocument) throws EPPEncodeException {
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		return root;
	}

	// End EPPLogoutCmd.doGenEncode()

	/**
	 * decode <code>EPPLogoutCmd</code> from a DOM element tree.  The "logout"
	 * element needs to be the value of the <code>aElement</code> argument.
	 * This method is part     of the Template Design Pattern, where
	 * <code>EPPCommand</code> provides the public     <code>decode</code> and
	 * calls the abstract <code>doGenDecode</code>.
	 *
	 * @param aElement &ltlogout&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		if (!aElement.getNamespaceURI().equals(EPPCodec.NS) || 
			!aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_NAME))) {
			throw new EPPDecodeException("Invalid tag " + aElement.getTagName()
										 + ", expecting logout");
		}
	}
	// End EPPLogoutCmd.doGenDecode(Element)

	/**
	 * implements a deep <code>EPPLogoutCmd</code> compare.
	 *
	 * @param aObject <code>EPPLogoutCmd</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPLogoutCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		return true;
	}

	// End EPPLogoutCmd.equals(Object)

	/**
	 * Clone <code>EPPLogoutCmd</code>.
	 *
	 * @return clone of <code>EPPLogoutCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPLogoutCmd clone = null;

		clone = (EPPLogoutCmd) super.clone();

		return clone;
	}

	// End EPPLogoutCmd.clone()

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

	// End EPPLogoutCmd.toString()
}
