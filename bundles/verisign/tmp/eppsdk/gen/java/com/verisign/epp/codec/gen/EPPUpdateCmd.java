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
 * The EPP update command is used to change information associated with an
 * existing object.  The elements needed to identify and modify an object are
 * object-specific, so the child elements of the update command are specified
 * using the EPP extension framework.  In addition to the standard EPP command
 * elements, the &ltupdate&gt command SHALL contain the following child
 * elements: An object-specific "obj:update" element that identifies the
 * object to be renewed and the elements that are required to modify the
 * object. Object-specific elements MUST identify values to be added, values
 * to be removed, or values to be changed. <br>
 * <br>
 * <code>EPPUpdateCmd</code> is an abtract EPP command class that represents a
 * update operation.      A command mapping update command extends
 * <code>EPPUpdateCmd</code>.      For example,
 * <code>EPPDomainUpdateCmd</code> is a <code>EPPUpdateCmd</code> that
 * implements     the Domain Update Command Mapping.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public abstract class EPPUpdateCmd extends EPPCommand {
	/** XML Element Name of <code>EPPUpdateCmd</code> root element. */
	final static String ELM_NAME = "update";

	/**
	 * Default constructor for <code>EPPUpdateCmd</code>.
	 */
	public EPPUpdateCmd() {
	}

	// End EPPUpdateCmd.EPPUpdateCmd()

	/**
	 * <code>EPPUpdateCmd</code> that takes all required attributes as
	 * arguments.  This will     call the super
	 * <code>EPPCommand(String)</code> method to set the transaction id for
	 * the command.
	 *
	 * @param aTransId Transaction Id associated with command.
	 */
	public EPPUpdateCmd(String aTransId) {
		super(aTransId);
	}

	// End EPPUpdateCmd.EPPUpdateCmd(String)

	/**
	 * Gets the EPP command type associated with <code>EPPUpdateCmd</code>.
	 *
	 * @return EPPCommand.TYPE_UPDATE
	 */
	public String getType() {
		return EPPCommand.TYPE_UPDATE;
	}

	// End EPPUpdateCmd.getType()

	/**
	 * Compares an instance of <code>EPPUpdateCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		// EPPCommand
		if (!super.equals(aObject)) {
			return false;
		}

		return true;
	}

	// End EPPUpdateCmd.equals(Object)

	/**
	 * Encodes a DOM Element tree from the attributes of the
	 * <code>EPPUpdateCmd</code> instance.  This method is a member of the
	 * Template Design Pattern. <code>EPPCommand.encode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPUpdateCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode <code>EPPUpdateCmd</code>
	 * 			  instance.
	 */
	protected Element doGenEncode(Document aDocument) throws EPPEncodeException {
		// <create> Element
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Command Mapping
		Element mapElement = doEncode(aDocument);

		if (mapElement != null) {
			root.appendChild(mapElement);
		}

		return root;
	}

	// End EPPUpdateCmd.doGenEncode(Document)

	/**
	 * Decodes the <code>EPPUpdateCmd</code> attributes from the
	 * <code>aElement</code> DOM Element tree.      This method is a member of
	 * the     Template Design Pattern. <code>EPPCommand.decode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aElement - Root DOM Element to decode <code>EPPUpdateCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		// Command Mapping
		doDecode(EPPUtil.getFirstElementChild(aElement));
	}

	// End EPPUpdateCmd.doGenDecode(Element)

	/**
	 * Must be defined by <code>EPPUpdateCmd</code> extensions (Command
	 * Mappings) to encode     the attributes to a DOM Element tree.
	 * <code>doGenEncode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPUpdateCmd extension
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPUpdateCmd extension
	 * 			  instance.
	 */
	protected abstract Element doEncode(Document aDocument)
								 throws EPPEncodeException;

	/**
	 * Must be defined by <code>EPPUpdateCmd</code> extensions (Command
	 * Mappings) to decode     the attributes to a DOM Element tree.
	 * <code>doGenDecode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aElement Root DOM Element representing the
	 * 		  <code>EPPUpdateCmd</code> extension instance.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	protected abstract void doDecode(Element aElement)
							  throws EPPDecodeException;
}
