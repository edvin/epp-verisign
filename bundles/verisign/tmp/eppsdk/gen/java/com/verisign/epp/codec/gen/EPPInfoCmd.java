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
 * The EPP &ltinfo&gt command is used to retrieve information associated with
 * an existing object.  The elements needed to identify an object and the type
 * of information associated with an object are both object- specific,  so the
 * child elements of the &ltinfo&gt command are specified using the EPP
 * extension framework.  In addition to the standard EPP command elements, the
 * &ltinfo&gt command SHALL contain the following child elements: An
 * object-specific &ltobj:info&gt element that identifies the object to be
 * queried. <br>
 * <br>
 * <code>EPPInfoCmd</code> is an abtract EPP command class that represents a
 * info operation.      A command mapping info command extends
 * <code>EPPInfoCmd</code>.      For example, <code>EPPDomainInfoCmd</code> is
 * a <code>EPPInfoCmd</code> that implements     the Domain Info Command
 * Mapping.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public abstract class EPPInfoCmd extends EPPCommand {
	/** XML Element Name of <code>EPPInfoCmd</code> root element. */
	final static String ELM_NAME = "info";

	/**
	 * Default constructor for <code>EPPInfoCmd</code>.
	 */
	public EPPInfoCmd() {
	}

	// End EPPInfoCmd.EPPInfoCmd()

	/**
	 * <code>EPPInfoCmd</code> that takes all required attributes as arguments.
	 * This will     call the super <code>EPPCommand(String)</code> method to
	 * set the transaction id     for the command.
	 *
	 * @param aTransId Transaction Id associated with command.
	 */
	public EPPInfoCmd(String aTransId) {
		super(aTransId);
	}

	// End EPPInfoCmd.EPPInfoCmd(String)

	/**
	 * Gets the EPP command type associated with <code>EPPInfoCmd</code>.
	 *
	 * @return EPPCommand.TYPE_INFO
	 */
	public String getType() {
		return EPPCommand.TYPE_INFO;
	}

	// End EPPInfoCmd.getType()

	/**
	 * Compares an instance of <code>EPPInfoCmd</code> with this instance.
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

	// End EPPInfoCmd.equals(Object)

	/**
	 * Encodes a DOM Element tree from the attributes of the
	 * <code>EPPInfoCmd</code> instance.  This method is a member of the
	 * Template Design Pattern. <code>EPPCommand.encode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPInfoCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode <code>EPPInfoCmd</code>
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

	// End EPPInfoCmd.doGenEncode(Document)

	/**
	 * Decodes the <code>EPPInfoCmd</code> attributes from the
	 * <code>aElement</code> DOM Element tree.      This method is a member of
	 * the     Template Design Pattern. <code>EPPCommand.decode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aElement - Root DOM Element to decode <code>EPPInfoCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		// Command Mapping
		doDecode(EPPUtil.getFirstElementChild(aElement));
	}

	// End EPPInfoCmd.doGenDecode(Node)

	/**
	 * Must be defined by <code>EPPInfoCmd</code> extensions (Command Mappings)
	 * to encode     the attributes to a DOM Element tree.
	 * <code>doGenEncode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPInfoCmd extension instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPInfoCmd extension
	 * 			  instance.
	 */
	protected abstract Element doEncode(Document aDocument)
								 throws EPPEncodeException;

	/**
	 * Must be defined by <code>EPPInfoCmd</code> extensions (Command Mappings)
	 * to decode     the attributes to a DOM Element tree.
	 * <code>doGenDecode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aElement Root DOM Element representing the
	 * 		  <code>EPPInfoCmd</code> extension instance.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	protected abstract void doDecode(Element aElement)
							  throws EPPDecodeException;
}
