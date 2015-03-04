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


import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.verisign.epp.util.EPPCatFactory;


/**
 * <code>EPPMsgQueue</code> describes messages queued for client retrieval.  A
 * &lt;msgQ&gt; element MUST NOT be present if there are no messages queued
 * for client retrieval.  A &lt;msgQ&gt; element MAY be present in responses
 * to EPP commands other than the &lt;pol&gt; command if messages are queued
 * for retrieval.  A &lt;msgQ&gt; element MUST be present in responses to the
 * EPP &lt;poll&gt; command if messages are queued for retrieval.  The
 * &lt;msgQ&gt; element contains the following attributes: <br><br>
 * 
 * <ul>
 * <li>
 * A "count" attribute that describes the number of messages that exist in the
 * queue.
 * </li>
 * <li>
 * An "id" attribute used to uniquely identify the message at the head of the
 * queue.
 * </li>
 * </ul>
 * 
 * The &lt;msgQ&gt; element contains the following OPTIONAL child elements that
 * MUST be returned in response to a &lt;poll&gt; request command and MUST NOT
 * be returned in response to any other command, including a &lt;poll&gt;
 * acknowledgement: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;qDate&gt; element that contains the date and time that the message was
 * enqueued.
 * </li>
 * <li>
 * A &lt;msg&gt; element containing a human-readable message.  The language of
 * the response is identified via an OPTIONAL "lang" attribute.  If not
 * specified, the default attribute value MUST be "en" (English). This element
 * MAY contain XML content for formatting purposes, but the XML content is not
 * specified by the protocol and will thus not be processed for validity.
 * </li>
 * </ul>
 */
public class EPPMsgQueue implements EPPCodecComponent {
	/** The default language of the reason "en". */
	public static final String DEFAULT_LANG = "en";

	/** XML root tag name for <code>EPPMsgQueue</code>. */
	final static String ELM_NAME = "msgQ";

	/** XML attribute name for the &lt;count&gt; attribute. */
	private final static String ATTR_COUNT = "count";

	/** XML attribute name for the &lt;id&gt; attribute. */
	private final static String ATTR_ID = "id";

	/** XML element name for the &lt;qDate&gt; element. */
	private final static String ELM_QDATE = "qDate";

	/** XML element name for the &lt;msg&gt; element. */
	private final static String ELM_MSG = "msg";

	/** XML attribute name for the &lt;lang&gt; attribute. */
	private final static String ATTR_LANG = "lang";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPMsgQueue.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Number of messages that exist in the queue. */
	private Long count = null;

	/** Uniquely identifies the message at the head of the queue. */
	private String id = null;

	/** Optional date and time that the message was enqueued. */
	private Date qDate = null;

	/** Language of the <code>msg</code> element. */
	private String lang = DEFAULT_LANG;

	/** Optional human-readable message. */
	private String msg = null;
	
	/** Optional msg using mixed content **/
	private NodeList msgNodeList = null; 
		
	/**
	 * Default constructor required for serialization.  The  <code>count</code>
	 * and <code>id</code> attributes must  be set before calling
	 * <code>encode</code>.
	 */
	public EPPMsgQueue() {
		// Do nothing
	}

	/**
	 * Allocates a new <code>EPPMsgQueue</code> with the  required attributes.
	 * The optional attributes are  initialized to <code>null</code>.
	 *
	 * @param aCount The number of messages that exist in the queue
	 * @param aId uniquely identify the message at the head of the queue
	 */
	public EPPMsgQueue(Long aCount, String aId) {
		this.count     = aCount;
		this.id		   = aId;
	}
	// End EPPMsgQueue.EPPMsgQueue(Long, String)

	/**
	 * Allocates a new <code>EPPMsgQueue</code> with all  of the attributes.
	 *
	 * @param aCount The number of messages that exist in the queue
	 * @param aId uniquely identify the message at the head of the queue
	 * @param aQDate date and time that the message was enqueued
	 * @param aMsg human-readable message.  The language defaults  to
	 * 		  <code>DEFAULT_LANG</code>, but can be set with
	 * 		  <code>setLang(String)</code>.
	 */
	public EPPMsgQueue(Long aCount, String aId, Date aQDate, String aMsg) {
		this.count     = aCount;
		this.id		   = aId;
		this.qDate     = aQDate;
		this.msg	   = aMsg;
	}
	// End EPPMsgQueue.EPPMsgQueue(Long, String, Date, String)

	/**
	 * Allocates a new <code>EPPMsgQueue</code> with all  of the attributes.
	 *
	 * @param aCount The number of messages that exist in the queue
	 * @param aId uniquely identify the message at the head of the queue
	 * @param aQDate date and time that the message was enqueued
	 * @param aMsgNodeList Mixed XML content for the message.
	 */
	public EPPMsgQueue(Long aCount, String aId, Date aQDate, NodeList aMsgNodeList) {
		this.count     = aCount;
		this.id		   = aId;
		this.qDate     = aQDate;
		this.msgNodeList = aMsgNodeList;
	}
	// End EPPMsgQueue.EPPMsgQueue(Long, String, Date, NodeList)
	
	
	/**
	 * Gets the number of messages that exist in the queue.
	 *
	 * @return Number of messages in the queue
	 */
	public Long getCount() {
		return this.count;
	}

	// End EPPMsgQueue.getCount()

	/**
	 * Sets the number of messages that exist in the queue.
	 *
	 * @param aCount Number of messages in the queue
	 */
	public void setCount(Long aCount) {
		this.count = aCount;
	}

	// End EPPMsgQueue.setId(Long)

	/**
	 * Gets the id that uniquely identifies the message at the head of the
	 * queue.
	 *
	 * @return id for message
	 */
	public String getId() {
		return this.id;
	}

	// End EPPMsgQueue.getId()

	/**
	 * Gets the id that uniquely identifies the message at the head of the
	 * queue.
	 *
	 * @param aId Message id
	 */
	public void setId(String aId) {
		this.id = aId;
	}

	// End EPPMsgQueue.setId(String)

	/**
	 * Gets the optional date and time that the message was enqueued.
	 *
	 * @return Date and time that the message was enqueued if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getQDate() {
		return qDate;
	}

	// End EPPMsgQueue.getQDate()

	/**
	 * Sets the optional date and time that the message was enqueued.
	 *
	 * @param aDate The date and time that the message was enqueued
	 */
	public void setQDate(Date aDate) {
		qDate = aDate;
	}

	// End EPPMsgQueue.setQDate(Date)

	/**
	 * Gets the message language.   The Language must be   structured as
	 * documented in  <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @return Language of the message, with the default of
	 * 		   <code>DEFAULT_LANG</code>.
	 */
	public String getLang() {
		return this.lang;
	}

	// End EPPMsgQueue.getLang()

	/**
	 * Sets the message language.   The Language must be   structured as
	 * documented in  <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @param aLang Language of the message.
	 */
	public void setLang(String aLang) {
		if ((aLang == null) || (aLang.equals(""))) {
			this.lang = DEFAULT_LANG;
		}
		else {
			this.lang = aLang;
		}
	}

	// End EPPMsgQueue.setLang(String)

	/**
	 * Gets the optional human-readable message.  The language defaults to
	 * <code>DEFAULT_LANG</code>, but the value can be retrieved with the
	 * <code>getLang()</code>  method.
	 *
	 * @return the human-readable message if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getMsg() {
		return this.msg;
	}

	// End EPPMsgQueue.getMsg()

	/**
	 * Sets the optional human-readable message.  The language defaults to
	 * <code>DEFAULT_LANG</code>, but can be set with the
	 * <code>setLang(String)</code>  method.
	 *
	 * @param aMsg human-readable message
	 */
	public void setMsg(String aMsg) {
		this.msg = aMsg;
	}

	// End EPPMsgQueue.setMsg(String)

	
	/**
	 * Gets the message DOM <code>NodeList</code> representing 
	 * mixed XML content for the message.  This is an alternative 
	 * to using a simple <code>String</code> for the message.
	 * 
	 * @return Message <code>NodeList</code> if defined;<code>null</code> otherwise
	 */
	public NodeList getMsgNodeList() {
		return this.msgNodeList;
	}

	/**
	 * Sets the message DOM <code>NodeList</code> representing 
	 * mixed XML content for the message.  This is an alternative 
	 * to using a simple <code>String</code> for the message.
	 * 
	 * @param aMsgNodeList <code>NodeList</code> containing mixed XML content
	 */
	public void setMsgNodeList(NodeList aMsgNodeList) {
		this.msgNodeList = aMsgNodeList;
	}

	/**
	 * encode <code>EPPMsgQueue</code> into a DOM element tree.
	 *
	 * @param aDocument Factory for DOM components
	 *
	 * @return &lt;extValue&gt; root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		
		// Required attributes not set?
		if ((this.count == null) || (this.id == null)) {
			cat.error("EPPMsgQueue.encode(): required attributes count and id must be set");
			throw new EPPEncodeException("EPPMsgQueue required attributes count and id must be set");
		}

		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Count
		root.setAttribute(ATTR_COUNT, this.count.toString());

		// Id
		root.setAttribute(ATTR_ID, this.id);

		// qDate child element
		if (this.qDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, this.qDate, EPPCodec.NS,
									  ELM_QDATE);
		}

		// Msg
		Element msgElm = null;
		if (this.msg != null) {
			msgElm = aDocument.createElementNS(EPPCodec.NS, ELM_MSG);
			Text currVal     = aDocument.createTextNode(this.msg);
			msgElm.appendChild(currVal);				

		}
		else if (this.msgNodeList != null) {
			msgElm = aDocument.createElementNS(EPPCodec.NS, ELM_MSG);
			for (int i = 0; i < this.msgNodeList.getLength(); i++) {
				Node clonedNode = aDocument.importNode(this.msgNodeList.item(i), true);
				msgElm.appendChild(clonedNode);
			}
		} 
		
		if (msgElm != null) {
			root.appendChild(msgElm);
			
			// Lang
			if (!lang.equals(DEFAULT_LANG)) {
				msgElm.setAttribute(ATTR_LANG, lang);
			}
		}

		return root;
	}

	// End EPPMsgQueue.encode(Document)

	/**
	 * decode <code>EPPMsgQueue</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the "extValue" element.
	 *
	 * @param aElement The "extValue" XML element.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Count 
		this.count     = new Long(aElement.getAttribute(ATTR_COUNT));

		// Id
		this.id     = aElement.getAttribute(ATTR_ID);

		// QDate
		this.qDate =
			EPPUtil.decodeTimeInstant(aElement, EPPCodec.NS, ELM_QDATE);

		// Msg
		Element theMsgElm = EPPUtil.getElementByTagNameNS(aElement, EPPCodec.NS, ELM_MSG);
		
		if (theMsgElm != null) {
			
			NodeList theMsgNodes = theMsgElm.getChildNodes();
			
			// Is it mixed XML content?
			if (theMsgNodes.getLength() > 1 || 
					(theMsgNodes.getLength() == 1 && theMsgNodes.item(0).getNodeType() != Node.TEXT_NODE)) {
				this.msg = null;
				
				// Clear empty Text Nodes
				Node theCurrNode = theMsgNodes.item(0);
				while (theCurrNode != null) {
					Node theNextNode = theCurrNode.getNextSibling();
					if (theCurrNode.getNodeType() == Node.TEXT_NODE && 
							theCurrNode.getNodeValue().trim().length() == 0) {
						theCurrNode.getParentNode().removeChild(theCurrNode);
					}
					theCurrNode = theNextNode;
				}
				this.msgNodeList = theMsgNodes;
			}
			else if (theMsgNodes.getLength() == 1 && theMsgNodes.item(0).getNodeType() == Node.TEXT_NODE) {
				this.msgNodeList = null;
				this.msg = ((Text) theMsgNodes.item(0)).getNodeValue();	
			}
			else {
				this.msg = "";
				this.msgNodeList = null;
			}
			
			// Lang
			this.setLang(theMsgElm.getAttribute(ATTR_LANG));
		}
		else {
			this.msg	  = null;
			this.msgNodeList = null;
			this.lang  = DEFAULT_LANG;
		}
	}

	// End EPPMsgQueue.decode(Element)

	/**
	 * implements a deep <code>EPPMsgQueue</code> compare.
	 *
	 * @param aObject <code>EPPMsgQueue</code> instance to compare with
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPMsgQueue)) {
			cat.error("EPPMsgQueue.equals(): " + aObject.getClass().getName()
					  + " not EPPMsgQueue instance");

			return false;
		}

		EPPMsgQueue theMsgQueue = (EPPMsgQueue) aObject;

		// Count
		if (
			!(
					(this.count == null) ? (theMsgQueue.count == null)
											 : this.count.equals(theMsgQueue.count)
				)) {
			cat.error("EPPMsgQueue.equals(): count not equal");

			return false;
		}

		// Id
		if (
			!(
					(this.id == null) ? (theMsgQueue.id == null)
										  : this.id.equals(theMsgQueue.id)
				)) {
			cat.error("EPPMsgQueue.equals(): id not equal");

			return false;
		}

		// QDate
		if (
			!(
					(this.qDate == null) ? (theMsgQueue.qDate == null)
											 : this.qDate.equals(theMsgQueue.qDate)
				)) {
			cat.error("EPPMsgQueue.equals(): qDate not equal");

			return false;
		}

		// Msg
		if (
			!(
					(this.msg == null) ? (theMsgQueue.msg == null)
										   : this.msg.equals(theMsgQueue.msg)
				)) {
			cat.error("EPPMsgQueue.equals(): msg not equal");

			return false;
		}
		
		// MsgNodeList
		if (this.msgNodeList != null) {
			if (theMsgQueue.msgNodeList == null) {
				cat.error("EPPMsgQueue.equals(): msgNodeList not equal, one is null");		
				return false;
			}
			if (this.msgNodeList.getLength() != theMsgQueue.msgNodeList.getLength()) {
				cat.error("EPPMsgQueue.equals(): msgNodeList not equal, lengths are different (this = " + 
						this.msgNodeList.getLength() + " to = " + theMsgQueue.msgNodeList.getLength() + ")");		
				return false;				
			}
			
			for (int i = 0; i < this.msgNodeList.getLength(); i++) {
				if (this.msgNodeList.item(i).getNodeType() != theMsgQueue.msgNodeList.item(i).getNodeType()) {
					cat.error("EPPMsgQueue.equals(): msgNodeList not equal, node types not equal");		
					return false;									
				}
			}
		}

		// Lang
		if (!this.lang.equals(theMsgQueue.lang)) {
			cat.error("EPPMsgQueue.equals(): lang not equal");

			return false;
		}

		return true;
	}

	// End EPPMsgQueue.equals(Object)

	/**
	 * Clone <code>EPPMsgQueue</code>.
	 *
	 * @return Deep copy clone of <code>EPPMsgQueue</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPMsgQueue clone = null;

		clone = (EPPMsgQueue) super.clone();

		return clone;
	}

	// End EPPMsgQueue.clone()

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

	// End EPPMsgQueue.toString()
}


// End class EPPMsgQueue
