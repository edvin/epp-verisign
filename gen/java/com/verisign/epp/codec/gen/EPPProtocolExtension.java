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
 * Represents an EPPProtocol extension that is sent by an EPP Client and
 * received by an     EPP Server.  An <code>EPPProtocolExtension</code> can be
 * encoded and decoded by <code>EPPCodec</code>.     <br>
 * <br>
 * Every EPPProtocol extension must extend <code>EPPProtocolExtension</code>
 * and implement the Template Method Design Pattern <code>doGenEncode</code>
 * and <code>doGenDecode</code> methods. An <code>EPPProtocolExtension</code>
 * client will call <code>encode</code> or <code>decode</code>, which in turn
 * will call <code>doGenEncode</code> or <code>doGenDecode</code>,
 * respectively.  There is one     derived <code>EPPProtocolExtension</code>
 * for each type of extension defined in the general EPP     Specification.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public abstract class EPPProtocolExtension implements EPPMessage {
	/** XML root tag name for <code>EPPProtocolExtension</code>. */
	final static String ELM_NAME = "extension";

	/**
	 * Allocates a new <code>EPPProtocolExtension</code> with default attribute
	 * values.
	 */
	public EPPProtocolExtension() {
	}

	// End EPPProtocolExtension.EPPProtocolExtension()

	/**
	 * Gets the EPP namespace associated with the
	 * <code>EPPProtocolExtension</code>.
	 *
	 * @return Namespace URI associated with the
	 * 		   <code>EPPProtocolExtension</code>.
	 */
	abstract public String getNamespace();

	/**
	 * encode <code>EPPProtocolExtension</code> into a DOM element tree.  The
	 * &ltextension&gt element     is created and the attribute nodes are
	 * appending as children.  This method is     a <i>Template Method</i> in
	 * the Template Method Design Pattern.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &ltextension&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// <extension>
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Encode the extension element (derived class).
		Element extElm = doEncode(aDocument);

		if (extElm != null) {
			root.appendChild(extElm);
		}

		return root;
	}

	// End EPPProtocolExtension.encode(Document)

	/**
	 * decode <code>EPPProtocolExtension</code> from a DOM element tree.  The
	 * "extension"     element needs to be the value of the
	 * <code>aElement</code> argument.  This method is     a <i>Template
	 * Method</i> in the Template Method Design Pattern.
	 *
	 * @param aElement &ltextension&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		Element extensionTypeElm = EPPUtil.getFirstElementChild(aElement);

		if (extensionTypeElm == null) {
			throw new EPPDecodeException("No child protocol extension Element found");
		}

		//do the necessary decode in the child class for the ext element
		doDecode(extensionTypeElm);

		// Extension Element
	}

	// End EPPProtocolExtension.decode(Element)

	/**
	 * implements a deep <code>EPPProtocolExtension</code> compare.
	 *
	 * @param aObject <code>EPPProtocolExtension</code> instance to compare
	 * 		  with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		return true;
	}

	// End EPPProtocolExtension.equals(Object)

	/**
	 * Clone <code>EPPProtocolExtension</code>.
	 *
	 * @return clone of <code>EPPProtocolExtension</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPProtocolExtension clone = null;

		clone = (EPPProtocolExtension) super.clone();

		return clone;
	}

	// End EPPProtocolExtension.clone()

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

	// End EPPProtocolExtension.toString()

	/**
	 * Encodes the atributes of a protocol extension of
	 * <code>EPPProtocolExtension</code>.  An example of a     protocol
	 * extension is <code>EPPAlert</code>.     <code>encode</code> is a
	 * <i>Template Method</i>    and this method is a     <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument DOM document used as a factory of DOM objects.
	 *
	 * @return instance root DOM element along with attribute child nodes.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	protected abstract Element doEncode(Document aDocument)
								 throws EPPEncodeException;

	/**
	 * Decodes the atributes of a general extension of
	 * <code>EPPProtocolExtension</code>.  An example of a     protocol
	 * extension is <code>EPPAlert</code>.     <code>decode</code> is a
	 * <i>Template Method</i>    and this method is a     <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aElement root DOM element associated with instance
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	protected abstract void doDecode(Element aElement)
							  throws EPPDecodeException;
}
