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
// W3C Imports
import org.w3c.dom.Element;


/**
 * Represents an EPP CODEC Component interface that is implemented by any class
 * that     needs to participate in the encoding and decoding of EPP XML
 * documents.  All     <code>EPPCodecComponent</code> classes are
 * <code>Serializable</code>.  All subclasses     of <code>EPPMessage</code>
 * and all contained classes of <code>EPPMessage</code>     classes must
 * implement the <code>EPPCodecComponent</code> interface.
 *
 * @see com.verisign.epp.codec.gen.EPPMessage
 */
public interface EPPCodecComponent extends java.io.Serializable, Cloneable {
	/**
	 * encode instance into a DOM element tree.   A DOM Document is passed as
	 * an argument and functions as a factory for DOM objects.  The root
	 * element associated with the instance is created and each instance
	 * attribute     is appended as a child node.      <br>
	 * <br>
	 * For example, the &ltcommand&gt element of <code>EPPCommand</code> is
	 * created and is used     to append the attribute nodes of
	 * <code>EPPCommand</code>.
	 *
	 * @param aDocument DOM document used as a factory of DOM objects.
	 *
	 * @return instance root DOM element along with attribute child nodes.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	Element encode(Document aDocument) throws EPPEncodeException;

	/**
	 * decode a DOM element tree to initialize the instance attributes. The
	 * <code>aElement</code> argument represents the root DOM element and is
	 * used to traverse the DOM nodes for instance attribute values. <br>
	 * <br>
	 * For example, the &ltcommand&gt element of <code>EPPCommand</code> is
	 * passed     into <code>decode</code> of EPPCommand to be decoded and
	 * used to     set the instance attributes.
	 *
	 * @param aElement root DOM element associated with instance
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	void decode(Element aElement) throws EPPDecodeException;

	/**
	 * clone an <code>EPPCodecComponent</code>.
	 *
	 * @return clone of concrete <code>EPPCodecComponent</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	Object clone() throws CloneNotSupportedException;
}
