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
 * The EPP &lttransfer&gt command provides a query operation that allows a
 * client to determine real-time status of pending and completed transfer
 * requests.  The elements needed to identify an object that is the subject of
 * a transfer request are object-specific, so the child elements of the
 * &lttransfer&gt query command are specified using the EPP extension
 * framework.  In addition to the standard EPP command elements, the
 * &lttransfer&gt command SHALL contain an <code>op</code> attribute with
 * value <code>query</code>, and the following child elements: An
 * object-specific &ltobj:transfer&gt element that identifies the object whose
 * transfer status is requested. <br>
 * <br>
 * <code>EPPTransferCmd</code> is an abtract EPP command class that represents
 * a transfer operation.   A command mapping transfer command extends
 * <code>EPPTransferCmd</code>.   For example,
 * <code>EPPDomainTransferCmd</code> is a <code>EPPTransferCmd</code> that
 * implements the Domain Transfer Command Mapping.  The different types of
 * transfer operations can  be one of the <code>EPPCommand.OP_</code>
 * constants.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public abstract class EPPTransferCmd extends EPPCommand {
	/** XML Element Name of <code>EPPTransferCmd</code> root element. */
	final static String ELM_NAME = "transfer";

	/**
	 * XML Attribute Name for the <code>getOp()</code> extension value.   For
	 * example, <code>EPPCommand.OP_APPROVE</code>.
	 */
	private final static String ATTR_OP = "op";

	/**
	 * Operation "op" associated with the <code>EPPTransferCmd</code>.  This
	 * should be equal to  one of the <code>EPPCommand.OP_</code> constants.
	 */
	protected String op = null;

	/**
	 * Default constructor.  Will set Authorization Id to null.
	 */
	public EPPTransferCmd() {
		op = null;
	}

	// End EPPTransferCmd.EPPTransferCmd()

	/**
	 * <code>EPPTransferCmd</code> that takes all required attributes as
	 * arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 */
	public EPPTransferCmd(String aTransId, String aOp) {
		super(aTransId);

		op = aOp;
	}

	// End EPPTransferCmd.EPPTransferCmd(String, EPPTransId)

	/**
	 * Gets the EPP command type associated with EPPTransferCmd.
	 *
	 * @return <code>EPPCommand.TYPE_TRANSFER</code>
	 */
	public String getType() {
		return EPPCommand.TYPE_TRANSFER;
	}

	// End EPPTransferCmd.getType()

	/**
	 * Gets the EPP command operation attribute associated with
	 * <code>EPPTransferCmd</code>.
	 *
	 * @return One of the <code>EPPCommand.OP_</code> constants associated with
	 * 		   the transfer command.
	 */
	public String getOp() {
		return op;
	}

	// End EPPTransferCmd.getOp()

	/**
	 * Sets the EPP command operation attribute associated with
	 * <code>EPPTransferCmd</code>.
	 *
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 */
	public void setOp(String aOp) {
		op = aOp;
	}

	// End EPPTransferCmd.setOp(String)

	/**
	 * Compares an instance of EPPTransferCmd with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		EPPTransferCmd theTransfer = (EPPTransferCmd) aObject;

		// EPPCommand
		if (!super.equals(aObject)) {
			return false;
		}

		// Operation
		if (
			!(
					(op == null) ? (theTransfer.op == null)
									 : op.equals(theTransfer.op)
				)) {
			return false;
		}

		return true;
	}

	// End EPPTransferCmd.equals(Object)

	/**
	 * Encodes a DOM Element tree from the attributes of the
	 * <code>EPPTransferCmd</code> instance.  This method is a member of the
	 * Template Design Pattern. <code>EPPCommand.encode</code> is a
	 * <i>Template Method</i> and this method is a <i>Primitive Operation</i>
	 * within the Template Method Design Pattern.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPTransferCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPTransferCmd</code> instance.
	 */
	protected Element doGenEncode(Document aDocument) throws EPPEncodeException {
		// <transfer> Element
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		if (op == null) {
			throw new EPPEncodeException("EPPTransferCmd op attribute is null");
		}

		root.setAttribute(ATTR_OP, op);

		// Command Mapping
		Element mapElement = doEncode(aDocument);

		if (mapElement != null) {
			root.appendChild(mapElement);
		}

		return root;
	}

	// End EPPTransferCmd.doGenEncode(Document)

	/**
	 * Decodes the <code>EPPTransferCmd</code> attributes from the
	 * <code>aElement</code> DOM Element tree.   This method is a member of
	 * the Template Design Pattern. <code>EPPCommand.decode</code> is a
	 * <i>Template Method</i> and this method is a <i>Primitive Operation</i>
	 * within the Template Method Design Pattern.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPTransferCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		op = aElement.getAttribute(ATTR_OP);

		if (op == null) {
			throw new EPPDecodeException("EPPTransferCmd requires an \""
										 + ATTR_OP + "\" attribute");
		}

		// Command Mapping
		doDecode(EPPUtil.getFirstElementChild(aElement));
	}

	// End EPPTransferCmd.doGenDecode(Node)

	/**
	 * Clone <code>EPPTransferCmd</code>.
	 *
	 * @return clone of <code>EPPTransferCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPTransferCmd clone = null;

		clone = (EPPTransferCmd) super.clone();

		return clone;
	}

	// End EPPTransferCmd.clone()

	/**
	 * Must be defined by <code>EPPTransferCmd</code> extensions (Command
	 * Mappings) to encode the attributes to a DOM Element tree.
	 * <code>doGenEncode</code> is a <i>Template Method</i> and this method is
	 * a <i>Primitive Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPTransferCmd extension
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPTransferCmd extension
	 * 			  instance.
	 */
	protected abstract Element doEncode(Document aDocument)
								 throws EPPEncodeException;

	/**
	 * Must be defined by <code>EPPTransferCmd</code> extensions (Command
	 * Mappings) to decode     the attributes to a DOM Element tree.
	 * <code>doGenDecode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aElement Root DOM Element representing the
	 * 		  <code>EPPTransferCmd</code> extension instance.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	protected abstract void doDecode(Element aElement)
							  throws EPPDecodeException;
}
