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
 * The EPP &ltrenew&gt command is used to extend the validity period of an
 * existing object.  The elements needed to identify and extend the validity
 * period  of an object are object-specific, so the child elements of the
 * &ltrenew&gt command are specified using the EPP extension framework.  In
 * addition to the standard EPP command elements, the &ltrenew&gt command
 * SHALL contain the following child elements: An object-specific
 * &ltobj:renew&gt element that identifies the object to be renewed and the
 * elements that are required to extend the validity period of the object. <br>
 * <br>
 * <code>EPPRenewCmd</code> is an abtract EPP command class that represents a
 * renew operation.      A command mapping renew command extends
 * <code>EPPRenewCmd</code>.      For example, <code>EPPDomainRenewCmd</code>
 * is a <code>EPPRenewCmd</code> that implements     the Domain Renew Command
 * Mapping.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public abstract class EPPRenewCmd extends EPPCommand {
	/** XML Element Name of <code>EPPRenewCmd</code> root element. */
	final static String ELM_NAME = "renew";

	/**
	 * Default constructor for <code>EPPRenewCmd</code>.
	 */
	public EPPRenewCmd() {
	}

	// End EPPRenewCmd.EPPRenewCmd()

	/**
	 * <code>EPPRenewCmd</code> that takes all required attributes as
	 * arguments.  This will     call the super
	 * <code>EPPCommand(String)</code> method to set the transaction id for
	 * the command.
	 *
	 * @param aTransId Transaction Id associated with command.
	 */
	public EPPRenewCmd(String aTransId) {
		super(aTransId);
	}

	// End EPPRenewCmd.EPPRenewCmd(String)

	/**
	 * Gets the EPP command type associated with <code>EPPRenewCmd</code>.
	 *
	 * @return EPPCommand.TYPE_RENEW
	 */
	public String getType() {
		return EPPCommand.TYPE_RENEW;
	}

	// End EPPRenewCmd.getType()

	/**
	 * Compares an instance of <code>EPPRenewCmd</code> with this instance.
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

	// End EPPRenewCmd.equals(Object)

	/**
	 * Encodes a DOM Element tree from the attributes of the
	 * <code>EPPRenewCmd</code> instance.  This method is a member of the
	 * Template Design Pattern. <code>EPPCommand.encode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPRenewCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode <code>EPPRenewCmd</code>
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

	// End EPPRenewCmd.doGenEncode(Document)

	/**
	 * Decodes the <code>EPPRenewCmd</code> attributes from the
	 * <code>aElement</code> DOM Element tree.      This method is a member of
	 * the     Template Design Pattern. <code>EPPCommand.decode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aElement - Root DOM Element to decode <code>EPPRenewCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		// Command Mapping
		doDecode(EPPUtil.getFirstElementChild(aElement));
	}

	// End EPPRenewCmd.doGenDecode(Node)

	/**
	 * Must be defined by <code>EPPRenewCmd</code> extensions (Command
	 * Mappings) to encode     the attributes to a DOM Element tree.
	 * <code>doGenEncode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPRenewCmd extension
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPRenewCmd extension
	 * 			  instance.
	 */
	protected abstract Element doEncode(Document aDocument)
								 throws EPPEncodeException;

	/**
	 * Must be defined by <code>EPPRenewCmd</code> extensions (Command
	 * Mappings) to decode     the attributes to a DOM Element tree.
	 * <code>doGenDecode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aElement Root DOM Element representing the
	 * 		  <code>EPPRenewCmd</code> extension instance.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	protected abstract void doDecode(Element aElement)
							  throws EPPDecodeException;
}
