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


// Log4j Imports
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP command that is sent by an EPP Client and received by an
 * EPP Server.  An <code>EPPCommand</code> can be encoded and decoded by
 * <code>EPPCodec</code>.     <br>
 * <br>
 * Every EPP command must extend <code>EPPCommand</code> and implement the
 * Template Method Design     Pattern <code>doGenEncode</code> and
 * <code>doGenDecode</code> methods.  An <code>EPPCommand</code>     client
 * will call <code>encode</code> or <code>decode</code>, which in turn will
 * call     <code>doGenEncode</code> or <code>doGenDecode</code>,
 * respectively.  There is one     derived <code>EPPCommand</code> for each
 * type of command defined in the general EPP     Specification.
 */
public abstract class EPPCommand implements EPPMessage {
	/** command type associated with the general EPP &ltlogin&gt command. */
	public final static String TYPE_LOGIN = "login";

	/** command type associated with the general EPP &ltlogout&gt command. */
	public final static String TYPE_LOGOUT = "logout";

	/** command type associated with the general EPP &ltinfo&gt command. */
	public final static String TYPE_INFO = "info";

	/** command type associated with the general EPP &ltcheck&gt command. */
	public final static String TYPE_CHECK = "check";

	/** command type associated with the general EPP &lttransfer&gt command. */
	public final static String TYPE_TRANSFER = "transfer";

	/** command type associated with the general EPP &ltcreate&gt command. */
	public final static String TYPE_CREATE = "create";

	/** command type associated with the general EPP &ltdelete&gt command. */
	public final static String TYPE_DELETE = "delete";

	/** command type associated with the general EPP &ltrenew&gt command. */
	public final static String TYPE_RENEW = "renew";

	/** command type associated with the general EPP &ltupdate&gt command. */
	public final static String TYPE_UPDATE = "update";

	/** command type associated with the general EPP &ltpoll&gt command. */
	public final static String TYPE_POLL = "poll";

	/**
	 * command approve operation currently associated with a &lttransfer&gt
	 * command.
	 */
	public final static String OP_APPROVE = "approve";

	/** command cancel operation associated with a &lttransfer&gt command. */
	public final static String OP_CANCEL = "cancel";

	/** command query operation associated with a &lttransfer&gt command. */
	public final static String OP_QUERY = "query";

	/** command reject operation associated with a &lttransfer&gt command. */
	public final static String OP_REJECT = "reject";

	/** command request operation associated with a &lttransfer&gt command. */
	public final static String OP_REQUEST = "request";

	/** XML root tag name for <code>EPPCommand</code>. */
	final static String ELM_NAME = "command";

	/** XML tag name for transId. */
	private static final String ELM_TRANS_ID = "clTRID";

	/** XML tag name for unspecified extension element. */
	private static final String ELM_EXTENSION = "extension";

	/** XML tag name for the <code>op</code> XML attribute. */
	final static String ATT_OP = "op";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPCommand.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Client Transaction id associated with the command */
	protected String transId = null;

	/**
	 * Extension objects associated with the command.  The extension object is
	 * associated with a unique XML Namespace, XML Schema, and can be any
	 * simple or complex object that implements the
	 * <code>EPPCodecComponent</code> interface.
	 */
	protected Vector extensions = null;

	/**
	 * Allocates a new <code>EPPCommand</code> with default attribute values.
	 * The defaults include the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * transaction id is set to <code>null</code>.  This attribute can be set
	 * using <code>setTransId</code> before invoking <code>encode</code>.
	 * </li>
	 * </ul>
	 * 
	 * <br>
	 */
	public EPPCommand() {
		transId		   = null;
		extensions     = null;
	} // End EPPCommand.EPPCommand()

	/**
	 * Allocates a new <code>EPPCommand</code> setting the client transaction
	 * id.
	 *
	 * @param aTransId Client Transaction id associated with the command.
	 */
	public EPPCommand(String aTransId) {
		transId		   = aTransId;
		extensions     = null;
	} // End EPPCommand.EPPCommand(String)

	/**
	 * Does the command have a client transaction id?  If so, the transaction
	 * id can be retrieved with a call to <code>getTransId</code>.
	 *
	 * @return <code>true</code> if this is a transaction id;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasTransId() {
		if (transId != null) {
			return true;
		}
		else {
			return false;
		}
	} // End EPPCommand.hasTransId()

	/**
	 * Gets the Client Transaction Id associated with the
	 * <code>EPPCommand</code>.
	 *
	 * @return <code>String</code> instance if defined; null otherwise.
	 */
	public String getTransId() {
		return transId;
	} // End EPPCommand.getTransId()

	/**
	 * Sets the Client Transaction Id associated with the EPPCommand.
	 *
	 * @param aTransId Client Transaction Id <code>String</code>
	 */
	public void setTransId(String aTransId) {
		transId = aTransId;
	} // End EPPCommand.setTransId(String)

	/**
	 * Gets the EPP namespace associated with the <code>EPPCommand</code>.
	 *
	 * @return Namespace URI associated with the <code>EPPCommand</code>.
	 */
	abstract public String getNamespace();

	/**
	 * Gets command type of the <code>EPPCommand</code>.  Each command     is
	 * associated with a single command type equal to one of the
	 * <code>EPPCommand.TYPE_</code> constants and optionally a     command
	 * operation equal to one of the <code>EPPCommand.OP_</code> constants.
	 *
	 * @return Command type <code>String</code> (<code>EPPCommand.TYPE_</code>)
	 */
	public abstract String getType();

	/**
	 * Gets the string operation of the concrete <code>EPPCommand</code>.  The
	 * type should     be equal to one of the <code>EPPCommand.OP_</code>
	 * constants, or null if there     is no operation.
	 *
	 * @return Operation of concrete EPPCommand if exists; null otherwise.
	 */
	public String getOp() {
		return null;
	} // End EPPCommand.getOp()

	/**
	 * Does the command have a command extension object of a specified class?
	 * If so, the command extension object can be retrieved with a call to
	 * <code>getExtensions(Class)</code>.
	 *
	 * @param aExtensionClass DOCUMENT ME!
	 *
	 * @return <code>true</code> if the extension object exists;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasExtension(Class aExtensionClass) {
		if (getExtension(aExtensionClass) != null) {
			return true;
		}
		else {
			return false;
		}
	} // End EPPCommand.hasExtension(Class)

	/**
	 * Gets the command extension object with the specified class. The
	 * extension object is an unspecified element in the EPP Specifications.
	 * To create an extension object, an XML Schema for the extension object
	 * must exist with a unique XML Namespace.  A custom
	 * <code>EPPExtensionFactory</code> must be created for the extension,
	 * which returns an instance of <code>EPPCodecComponent</code> for an
	 * instance of an extension object in the EPP Command.
	 *
	 * @param aExtensionClass of desired extension
	 *
	 * @return Concrete <code>EPPCodecComponent</code> associated with the
	 * 		   command if exists; <code>null</code> otherwise.
	 */
	public EPPCodecComponent getExtension(Class aExtensionClass) {
		if (extensions == null) {
			return null;
		}

		Iterator theIter = extensions.iterator();

		while (theIter.hasNext()) {
			EPPCodecComponent theExtension = (EPPCodecComponent) theIter.next();

			if (aExtensionClass.isInstance(theExtension)) {
				return theExtension;
			}
		}

		return null;
	} // End EPPCommand.getExtension(Class)
	

	/**
	 * Gets the command extension object with the specified class with the
	 * option to fail when a duplicate extension is found. The extension object
	 * is an unspecified element in the EPP Specifications. To create an
	 * extension object, an XML Schema for the extension object must exist with
	 * a unique XML Namespace. A custom <code>EPPExtensionFactory</code> must be
	 * created for the extension, which returns an instance of
	 * <code>EPPCodecComponent</code> for an instance of an extension object in
	 * the <code>EPPCommand</code>.
	 * 
	 * @param aExtensionClass
	 *            <code>Class</code> of desired extension
	 * @param aFailOnDuplicate
	 *            Throw {@link EPPDuplicateExtensionException} if <code>true</code> and a
	 *            duplicate extension is found
	 * 
	 * @return Concrete <code>EPPCodecComponent</code> associated with the
	 *         command if exists; <code>null</code> otherwise.
	 * 
	 * @exception EPPDuplicateExtensionException
	 *                If a duplicate extension is found with the extension
	 *                included in the extension
	 */
	public EPPCodecComponent getExtension(Class aExtensionClass,
			boolean aFailOnDuplicate) throws EPPDuplicateExtensionException {
		EPPCodecComponent theExtension = null;

		if (extensions == null) {
			return null;
		}

		Iterator theIter = extensions.iterator();

		while (theIter.hasNext()) {
			EPPCodecComponent currExtension = (EPPCodecComponent) theIter
					.next();

			if (aExtensionClass.isInstance(currExtension)) {
				
				// Duplicate found?
				if ((theExtension != null) && (aFailOnDuplicate)) {
					throw new EPPDuplicateExtensionException(currExtension);
				}

				theExtension = currExtension;
				
				// Done if not looking for duplicates
				if (!aFailOnDuplicate) {
					return theExtension;
				}
			}
		}

		return theExtension;
	} // End EPPCommand.getExtension(Class, boolean)
	

	/**
	 * Sets a command extension object.  The extension object is an unspecified
	 * element in the EPP Specifications.  The unspecified element will be
	 * encoded under the &ltunspec&gt element of the EPP Command.
	 *
	 * @param aExtension command extension object associated with the command
	 *
	 * @deprecated Replaced by {@link #addExtension(EPPCodecComponent)}.  This
	 * 			   method will add the extension as is done in {@link
	 * 			   #addExtension(EPPCodecComponent)}.
	 */
	public void setExtension(EPPCodecComponent aExtension) {
		this.addExtension(aExtension);
	} // End EPPCommand.setExtension(EPPCodecComponent)

	/**
	 * Adds a command extension object.  The extension object is an unspecified
	 * element in the EPP Specifications.  The unspecified element will be
	 * encoded under the &ltunspec&gt element of the EPP Command.
	 *
	 * @param aExtension command extension object associated with the command
	 */
	public void addExtension(EPPCodecComponent aExtension) {
		if (extensions == null) {
			extensions = new Vector();
		}

		extensions.addElement(aExtension);
	} // End EPPCommand.addExtension(EPPCodecComponent)

	/**
	 * Does the command have a command extension objects?  If so, the command
	 * extension objects can be retrieved with a call to
	 * <code>getExtensions</code>.
	 *
	 * @return <code>true</code> if there are extension objects;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasExtensions() {
		if (extensions != null) {
			return true;
		}
		else {
			return false;
		}
	} // End EPPCommand.hasExtensions()

	/**
	 * Gets the command extensions.  The extension objects are an unspecified
	 * elements in the EPP Specification.  To create an extension object, an
	 * XML Schema for the extension object must exist with a unique XML
	 * Namespace.  A custom <code>EPPExtensionFactory</code> must be created
	 * for the extension, which returns an instance of
	 * <code>EPPCodecComponent</code> for an instance of an extension object
	 * in the EPP Command.
	 *
	 * @return <code>Vector</code> of concrete <code>EPPCodecComponent</code>
	 * 		   associated with the command if exists; <code>null</code>
	 * 		   otherwise.
	 */
	public Vector getExtensions() {
		return extensions;
	} // End EPPCommand.getExtensions()

	/**
	 * Sets the command extension objects.  The extension objects are an
	 * unspecified element in the EPP Specifications.  The unspecified element
	 * will be encoded under the &ltunspec&gt element of the EPP Command.
	 *
	 * @param aExtensions command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		extensions = aExtensions;
	} // End EPPCommand.setExtensions(Vector)

	/**
	 * encode <code>EPPCommand</code> into a DOM element tree.  The
	 * &ltcommand&gt element     is created and the attribute nodes are
	 * appending as children.  This method is     a <i>Template Method</i> in
	 * the Template Method Design Pattern.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &ltcommand&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Check pre-conditions
		if (
			(transId != null)
				&& (
					(transId.length() < EPPTransId.MIN_TRANSID_LEN)
					|| (transId.length() > EPPTransId.MAX_TRANSID_LEN)
				)) {
			throw new EPPEncodeException("EPPCommand transaction id length of "
										 + transId.length()
										 + "is out of range, must be between "
										 + EPPTransId.MIN_TRANSID_LEN + " and "
										 + EPPTransId.MAX_TRANSID_LEN);
		}

		// <command>
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// EPP General Command (e.g. EPPCreateCmd).
		Element mapping = doGenEncode(aDocument);

		if (mapping != null) {
			root.appendChild(mapping);
		}

		// Extension Element
		if (extensions != null) {
			Element extensionElm =
				aDocument.createElementNS(EPPCodec.NS, ELM_EXTENSION);
			root.appendChild(extensionElm);

			EPPUtil.encodeCompVector(aDocument, extensionElm, extensions);
		}

		// Transaction ID
		EPPUtil.encodeString(
							 aDocument, root, transId, EPPCodec.NS, ELM_TRANS_ID);

		return root;
	} // End EPPCommand.encode(Document)

	/**
	 * decode <code>EPPCommand</code> from a DOM element tree.  The "command"
	 * element needs to be the value of the <code>aElement</code> argument.
	 * This method is     a <i>Template Method</i> in the Template Method
	 * Design Pattern.
	 *
	 * @param aElement &ltcommand&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 * @exception EPPComponentNotFoundException An extension component could not be found
	 */
	public void decode(Element aElement) throws EPPDecodeException, EPPComponentNotFoundException {
		Element theCurrElement = null;

		theCurrElement = EPPUtil.getFirstElementChild(aElement);

		if (theCurrElement == null) {
			throw new EPPDecodeException("No child Element found");
		}

		// Command Mapping
		doGenDecode(theCurrElement);

		// Extension Element
		Element extensionElm = EPPUtil.getElementByTagNameNS(aElement, EPPCodec.NS, ELM_EXTENSION);
		
		// Extension Element exists?
		if (extensionElm != null) {
			Element currExtension = EPPUtil.getFirstElementChild(extensionElm);
			
			// While there is an extension to process
			while (currExtension != null) {
				
				// Decode the extension
				EPPCodecComponent theExtension = null;

				try {
					theExtension =
						EPPFactory.getInstance().createExtension(currExtension);
				}
				 catch (EPPCodecException e) {
					throw new EPPComponentNotFoundException(
							EPPComponentNotFoundException.EXTENSION, 
								"EPPCommand.decode unable to create extension object: "
												 + e);
				}
				theExtension.decode(currExtension);

				this.addExtension(theExtension);
				
				currExtension = EPPUtil.getNextElementSibling(currExtension);
			} // End while (currExtension != null)
			
		} // End if (extensionElm != null)

		// Client Transaction ID
		transId = EPPUtil.decodeString(aElement, EPPCodec.NS, ELM_TRANS_ID);
	} // End EPPCommand.decode(Element)

	/**
	 * implements a deep <code>EPPCommand</code> compare.
	 *
	 * @param aObject <code>EPPCommand</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		EPPCommand theCommand = (EPPCommand) aObject;

		// Transaction Id
		if (
			!(
					(transId == null) ? (theCommand.transId == null)
										  : transId.equals(theCommand.transId)
				)) {
			return false;
		}

		// Extensions
		if (!EPPUtil.equalVectors(extensions, theCommand.extensions)) {
			return false;
		}

		return true;
	} // End EPPCommand.equals(Object)

	/**
	 * Clone <code>EPPCommand</code>.
	 *
	 * @return clone of <code>EPPCommand</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPCommand clone = null;

		clone = (EPPCommand) super.clone();

		// Extensions
		if (extensions != null) {
			clone.extensions = (Vector) extensions.clone();

			for (int i = 0; i < extensions.size(); i++) {
				clone.extensions.setElementAt(
											  ((EPPCodecComponent) extensions
											   .elementAt(i)).clone(), i);
			}
		}

		return clone;
	} // End EPPCommand.clone()

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
	} // End EPPCommand.toString()

	/**
	 * Encodes the atributes of a general extension of <code>EPPCommand</code>.
	 * An example of a     general extension is <code>EPPCreateCmd</code>.
	 * <code>encode</code> is a <i>Template Method</i>    and this method is a
	 * <i>Primitive Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument DOM document used as a factory of DOM objects.
	 *
	 * @return instance root DOM element along with attribute child nodes.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	protected abstract Element doGenEncode(Document aDocument)
									throws EPPEncodeException;

	/**
	 * Decodes the atributes of a general extension of <code>EPPCommand</code>.
	 * An example of a     general extension is <code>EPPCreateCmd</code>.
	 * <code>decode</code> is a <i>Template Method</i>    and this method is a
	 * <i>Primitive Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aElement root DOM element associated with instance
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	protected abstract void doGenDecode(Element aElement)
								 throws EPPDecodeException;
}
