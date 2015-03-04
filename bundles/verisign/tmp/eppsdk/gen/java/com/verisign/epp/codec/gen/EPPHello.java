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


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Represents an EPP hello request that is sent by an EPP Client and received
 * by an EPP Server. EPP MAY be carried over both connection-oriented and
 * connection-less transport protocols.  An EPP client MAY request a
 * &ltgreeting&gt from an EPP server at any time by sending a &lthello&gt to a
 * server.  Use of this element is essential in a connection-less environment
 * where a server can not return a &ltgreeting&gt in response to a
 * client-initiated connection.  An EPP &lthello&gt SHALL be an empty element
 * with no child elements. <br>
 * <br>
 * An <code>EPPHello</code> can be encoded and decoded by
 * <code>EPPCodec</code>. <br>
 * <br>
 * An <code>EPPHello</code> request should result in the EPP Server sending
 * back an <code>EPPGreeting</code>.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPGreeting
 */
public class EPPHello implements EPPMessage {
	/** XML root tag name for <code>EPPHello</code>. */
	final static String ELM_NAME = "hello";

	/**
	 * Allocates a new <code>EPPHello</code>.  <code>EPPHello</code> contains
	 * no attributes.
	 */
	public EPPHello() {
	}

	// End EPPHello.EPPHello()

	/**
	 * Gets the associated EPP namespace.  The general EPP namespace is
	 * returned,     which is defined as <code>EPPCodec.NS</code>.
	 *
	 * @return namespace URI
	 */
	public String getNamespace() {
		return EPPCodec.NS;
	}

	// End EPPGreeting.getNamespace()

	/**
	 * encode <code>EPPHello</code> into a DOM element tree.  The &lthello&gt
	 * element     is created and returned.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &lthello&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// <command>
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		return root;
	}

	// End EPPHello.encode(Document)

	/**
	 * decode <code>EPPHello</code> from a DOM element tree.  The "hello"
	 * element needs to be the value of the <code>aElement</code> argument.
	 * Since <code>EPPHello</code> contains no attribute, no attributes need
	 * to be set the <code>decode</code>.
	 *
	 * @param aElement &lthello&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Do nothing since there are no attributes.
	}

	// End EPPHello.decode(Element)

	/**
	 * implements a deep <code>EPPHello</code> compare.
	 *
	 * @param aObject <code>EPPHello</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHello)) {
			return false;
		}

		return true;
	}

	// End EPPHello.equals(Object)

	/**
	 * Clone <code>EPPHello</code>.
	 *
	 * @return clone of <code>EPPHello</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHello clone = null;

		clone = (EPPHello) super.clone();

		return clone;
	}

	// End EPPHello.clone()

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

	// End EPPHello.toString()
}
