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
 * The EPP &ltpoll&gt command is used to discover and retrieve client service
 * messages from a server.  Information that MAY be made available to a client
 * using service messages includes notification of object transfer requests
 * and anticipated server outages; other messages types MAY be defined as a
 * matter of server policy. <br>
 * <br>
 * Service messages are queued by the server for client retrieval.  A
 * &ltpoll&gt command MUST return the first message from the message queue.
 * Each message returned from the server includes a server-unique message
 * identifier that MUST be provided to acknowledge receipt of the message, and
 * a counter that indicates the number of messages in the queue.  As a message
 * is received by the client, the client MUST respond to the message with an
 * explicit acknowledgement to confirm that the message has been received.  A
 * server MUST dequeue a message and decrement the queue counter after
 * receiving acknowledgement from the client, making the next message in the
 * queue (if any) available for retrieval. <br>
 * <br>
 * Some of the information returned in response to a &ltpoll&gt command MAY be
 * object-specific, so some child elements of the &ltpoll&gt response MAY be
 * specified using the EPP extension framework.  In addition to the standard
 * EPP command elements, the &ltpoll&gt command SHALL contain no child
 * elements and an <code>op</code> attribute with value <code>req</code> to
 * retrieve the first message from the server message queue, or both an
 * <code>op</code> attribute with value <code>ack</code> and a
 * <code>msgID</code> attribute whose value corresponds to the value of
 * <code>id</code> attribute copied from the &ltmsg&gt element in the response
 * that is being acknowledged. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPPollCmd extends EPPCommand {
	/**
	 * Poll operation type indicating that the client is requesting information
	 * from the server.
	 */
	public final static String OP_REQ = "req";

	/**
	 * Poll operation type indicating that the client has received  a message
	 * and that the server can remove the message.
	 */
	public final static String OP_ACK = "ack";

	/** XML Element Name of <code>EPPPollCmd</code> root element. */
	final static String ELM_NAME = "poll";

	/** XML Attribute Name for the op attribute. */
	private final static String ATTR_OP = "op";

	/** XML Attribute Name for the msgId attribute. */
	private final static String ATTR_MSGID = "msgID";

	/**
	 * Operation "op" associated with the <code>EPPPollCmd</code>.  This should
	 * be equal to     one of the <code>EPPPollCmd.OP_</code> constants.
	 */
	protected String op = null;

	/**
	 * msgID attribute whose value corresponds to the value of <code>id</code>
	 * attribute copied from  the <code>msg</code> element in the response
	 * that is being acknowledged.
	 */
	protected java.lang.String msgID = null;

	/**
	 * Default constructor.  Will set both op and msgId to <code>null</code>.
	 */
	public EPPPollCmd() {
		op		  = null;
		msgID     = null;
	}

	// End EPPPollCmd.EPPPollCmd()

	/**
	 * <code>EPPPollCmd</code> that takes all required attributes as arguments
	 * for a <code>EPPPollCmd.OP_REQ</code> poll command.  If <code>aOp</code>
	 * is <code>EPPPollCmd.OP_ACK</code>, the message id must be set  with
	 * <code>setMsgId</code> before calling <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aOp One of the <code>EPPPollCmd.OP_</code> constants.
	 */
	public EPPPollCmd(String aTransId, String aOp) {
		super(aTransId);

		op		  = aOp;
		msgID     = null;
	}

	// End EPPPollCmd.EPPPollCmd(String, String)

	/**
	 * <code>EPPPollCmd</code> that takes attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aOp One of the <code>EPPPollCmd.OP_</code> constants.
	 * @param aMsgID Message Id when operation
	 * 		  is<code>EPPPollCmd.OP_ACK</code>.
	 */
	public EPPPollCmd(String aTransId, String aOp, String aMsgID) {
		super(aTransId);

		op		  = aOp;
		msgID     = aMsgID;
	}

	// End EPPPollCmd.EPPPollCmd(String, String, String)

	/**
	 * Gets the EPP command type associated with EPPPollCmd.
	 *
	 * @return <code>EPPCommand.TYPE_POLL</code>
	 */
	public String getType() {
		return EPPCommand.TYPE_POLL;
	}

	// End EPPPollCmd.getType()

	/**
	 * Gets the EPP command operation attribute associated with
	 * <code>EPPPollCmd</code>.
	 *
	 * @return One of the <code>EPPCommand.OP_</code> constants associated with
	 * 		   the poll command.
	 */
	public String getOp() {
		return op;
	}

	// End EPPPollCmd.getOp()

	/**
	 * Compares an instance of EPPPollCmd with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		EPPPollCmd thePoll = (EPPPollCmd) aObject;

		// EPPCommand
		if (!super.equals(aObject)) {
			return false;
		}

		// Operation
		if (!((op == null) ? (thePoll.op == null) : op.equals(thePoll.op))) {
			return false;
		}

		// Message Id
		if (
			!(
					(msgID == null) ? (thePoll.msgID == null)
										: msgID.equals(thePoll.msgID)
				)) {
			return false;
		}

		return true;
	}

	// End EPPPollCmd.equals(Object)

	/**
	 * Encodes a DOM Element tree from the attributes of the
	 * <code>EPPPollCmd</code> instance.  This method is a member of the
	 * Template Design Pattern. <code>EPPCommand.encode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPPollCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode <code>EPPPollCmd</code>
	 * 			  instance.
	 */
	protected Element doGenEncode(Document aDocument) throws EPPEncodeException {
		// Check preconditions
		if (op == null) {
			throw new EPPEncodeException("EPPPollCmd op attribute is null");
		}

		if (!op.equals(OP_ACK) && !op.equals(OP_REQ)) {
			throw new EPPEncodeException("EPPPollCmd invalid operation type of "
										 + op);
		}

		if (op.equals(OP_ACK) && (msgID == null)) {
			throw new EPPEncodeException("EPPPollCmd msgId attribute is null for ACK operation");
		}

		// <poll> Element
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		root.setAttribute(ATTR_OP, op);

		// Command Mapping
		Element mapElement = doEncode(aDocument);

		if (mapElement != null) {
			root.appendChild(mapElement);
		}

		// Message Id
		if (msgID != null) {
			root.setAttribute(ATTR_MSGID, msgID);
		}

		return root;
	}

	// End EPPPollCmd.doGenEncode(Document)

	/**
	 * Decodes the <code>EPPPollCmd</code> attributes from the
	 * <code>aElement</code> DOM Element tree.      This method is a member of
	 * the     Template Design Pattern. <code>EPPCommand.decode</code> is a
	 * <i>Template Method</i>     and this method is a <i>Primitive
	 * Operation</i> within the Template Method Design Pattern.
	 *
	 * @param aElement - Root DOM Element to decode <code>EPPPollCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		op = aElement.getAttribute(ATTR_OP);

		if (op == null) {
			throw new EPPDecodeException("EPPPollCmd requires an \"" + ATTR_OP
										 + "\" attribute");
		}

		// Command Mapping
		doDecode(aElement);

		// Message Id
		// getAttribute() return empty string even though the attribute do NOT existed!!!
		if (aElement.getAttribute(ATTR_MSGID).equals("")) {
			msgID = null;
		}
		else {
			msgID = aElement.getAttribute(ATTR_MSGID);
		}
	}

	// End EPPPollCmd.doGenDecode(Node)

	/**
	 * Clone <code>EPPPollCmd</code>.
	 *
	 * @return clone of <code>EPPPollCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPPollCmd clone = null;

		clone = (EPPPollCmd) super.clone();

		return clone;
	}

	// End EPPPollCmd.clone()

	/**
	 * Must be defined by <code>EPPPollCmd</code> extensions (Command Mappings)
	 * to encode     the attributes to a DOM Element tree.
	 * <code>doGenEncode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPPollCmd extension instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPPollCmd extension
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		return null;
	}

	/**
	 * Must be defined by <code>EPPPollCmd</code> extensions (Command Mappings)
	 * to decode     the attributes to a DOM Element tree.
	 * <code>doGenDecode</code> is a <i>Template Method</i>     and this
	 * method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aElement Root DOM Element representing the
	 * 		  <code>EPPPollCmd</code> extension instance.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Do nothing for now
	}

	/**
	 * Gets the poll Message Id.
	 *
	 * @return Message Id if defined; null otherwise.
	 */
	public String getMsgID() {
		return msgID;
	}

	// End EPPPollCmd.getMsgId()

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
	 * Sets the poll Message Id.
	 *
	 * @param aMsgID Message Id
	 */
	public void setMsgID(String aMsgID) {
		msgID = aMsgID;
	}

	// End EPPPollCmd.setOp(String)
}
