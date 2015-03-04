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


// W3C Imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Date;
import java.util.Iterator;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;


/**
 * Represents an EPP response that is send by an EPP Server and received by an
 * EPP Client.  An <code>EPPResponse</code> can be encoded and decoded by
 * <code>EPPCodec</code>.     Every EPP response contain the following:<br>
 * 
 * <ul>
 * <li>
 * transaction id - Mirror of the transaction id set in the associated
 * <code>EPPCommand</code>
 * </li>
 * <li>
 * results    - One or more <code>EPPResult</code> instances.
 * </li>
 * </ul>
 * 
 * <br><br>
 * <code>EPPResponse</code> is a concrete class that can be extended by the
 * Command Mapping     responses.  A Command Mapping response must override
 * the Template Design Pattern     <code>doEncode</code> and
 * <code>doDecode</code> methods.  <code>EPPResponse</code> provides a default
 * <code>doEncode</code> and <code>doDecode</code> implementation that does
 * nothing.     An <code>EPPResponse</code> client will call
 * <code>encode</code> or <code>decode</code>,     which in turn will call
 * <code>doEncode</code> or <code>doDecode</code>, respectively.
 */
public class EPPResponse implements EPPMessage {
	/** Transfer status constant - clientApproved */
	public static final String TRANSFER_CLIENT_APPROVED = "clientApproved";

	/** Transfer status constant - clientCancelled */
	public static final String TRANSFER_CLIENT_CANCELLED = "clientCancelled";

	/** Transfer status constant - clientRejected */
	public static final String TRANSFER_CLIENT_REJECTED = "clientRejected";

	/** Transfer status constant - pending */
	public static final String TRANSFER_PENDING = "pending";

	/** Transfer status constant - serverApproved */
	public static final String TRANSFER_SERVER_APPROVED = "serverApproved";

	/** Transfer status constant - serverCancelled */
	public static final String TRANSFER_SERVER_CANCELLED = "serverCancelled";

	/** XML root tag name for <code>EPPReponse</code>. */
	final static String ELM_NAME = "response";

	/**
	 * XML tag name response extension data, which is defined by
	 * <code>EPPResponse</code> derived classes.
	 */
	final static String ELM_MESSAGE_QUEUE = "msgQ";

	/**
	 * XML tag name response extension data, which is defined by
	 * <code>EPPResponse</code> derived classes.
	 */
	final static String ELM_RESPONSE_DATA = "resData";

	/** XML tag name for the "qDate" child element to the &lt;msgQ&gt; element */
	final static String ELM_MESSAGE_QUEUE_QDATE = "qDate";

	/** XML tag name for unspecified extension element. */
	private static final String ELM_EXTENSION = "extension";

	/** Message Queue count attribute */
	private static final String ATTR_COUNT = "count";

	/**
	 * Extension objects associated with the response.  The extension object is
	 * associated with a unique XML Namespace, XML Schema, and can be any
	 * simple or complex object that implements the
	 * <code>EPPCodecComponent</code> interface.
	 */
	protected Vector extensions = null;

	/**
	 * Vector of <code>EPPResult</code> instances representing the results of
	 * the response.  There must be at least on result defined.
	 */
	private Vector results;

	/** Size of Message Queue. */
	private EPPMsgQueue msgQueue = null;

	/** Transaction id associated with the response */
	private EPPTransId transId;

	/** the "qDate" child element (value) to the &lt;msgQ&gt; element. */
	private java.util.Date qDate = null;

	/**
	 * Allocates a new <code>EPPResponse</code> with default attribute values.
	 * The defaults include the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * transaction id is set to <code>null</code>.  This attribute must be set
	 * using <code>setTransId</code> before invoking <code>encode</code>.
	 * </li>
	 * <li>
	 * extensions is set to <code>null</code>.  This attribute is optional.
	 * </li>
	 * <li>
	 * message queue is set to <code>null</code>.  This attribute is optional.
	 * </li>
	 * <li>
	 * results - Set to an individual successful result
	 * </li>
	 * </ul>
	 */
	public EPPResponse() {
		transId		   = null;
		extensions     = null;
		msgQueue	   = null;
		setResult(EPPResult.SUCCESS);
	} // End EPPResponse.EPPResponse()

	/**
	 * Allocates a new <code>EPPResponse</code> setting the transaction id. The
	 * results attribute will default to an individual successful result. Both
	 * the extensions and the message queue will be set to <code>null</code>.
	 *
	 * @param aTransId Transaction id associated with the response.
	 */
	public EPPResponse(EPPTransId aTransId) {
		transId		   = aTransId;
		extensions     = null;
		msgQueue	   = null;
		setResult(EPPResult.SUCCESS);
	} // End EPPResponse.EPPResponse(EPPTransId)

	/**
	 * Allocates a new <code>EPPResponse</code> setting the transaction id and
	 * an individual result.  Both the extensions and the message queue will
	 * be set to <code>null</code>.
	 *
	 * @param aTransId Transaction id associated with the response.
	 * @param aResult Result to associate with the response.
	 */
	public EPPResponse(EPPTransId aTransId, EPPResult aResult) {
		transId		   = aTransId;
		extensions     = null;
		msgQueue	   = null;
		results		   = new Vector();
		results.addElement(aResult);
	} // End EPPResponse.EPPResponse(EPPTransId, EPPResult)

	/**
	 * Gets the EPP response extension type name or <code>null</code> if there
	 * is no extension.
	 *
	 * @return Extension type name if defined; <code>null</code> otherwise. For
	 * 		   example, <code>getType</code>     will return
	 * 		   EPPDomainCheckResp.ELM_NAME for a EPPDomainCheckResp instance.
	 */
	public String getType() {
		return null;
	} // End EPPResponse.getType()

	/**
	 * Gets the Transaction Id associated with the <code>EPPResponse</code>.
	 * This should include the <code>EPPCommand</code> client specified
	 * transaction id, and a server generated transaction id.
	 *
	 * @return EPPTransId instance if defined; <code>null</code> otherwise.
	 */
	public EPPTransId getTransId() {
		return transId;
	} // End EPPCommand.getTransId()

	/**
	 * Sets the Transaction Id associated with the <code>EPPResponse</code>.
	 * This should include the <code>EPPCommand</code> client specified
	 * transaction id, and a server generated transaction id.
	 *
	 * @param aTransId Response transaction id
	 */
	public void setTransId(EPPTransId aTransId) {
		transId = aTransId;
	} // End EPPCommand.setTransId(EPPTransId)

	/**
	 * Gets the EPP namespace associated with the <code>EPPResponse</code>. A
	 * base <code>EPPResponse</code> instance will have the EPP namespace
	 * equal to EPPCodec.NS.
	 *
	 * @return Namespace URI associated with the EPPResponse.
	 */
	public String getNamespace() {
		return EPPCodec.NS;
	} // End EPPResponse.getNamespace()

	/**
	 * Gets the Vector of results associated with the <code>EPPResponse</code>.
	 *
	 * @return Vector of <code>EPPResult</code> instances.
	 */
	public Vector getResults() {
		return results;
	} // End EPPResponse.getResults()
	
	/**
	 * Gets the first result in the response.  Use {@link #getResults()} to 
	 * get all results associated with the response.
	 * 
	 * @return The first result if defined; <code>null</code> otherwise
	 */
	public EPPResult getResult() {
		if (results.size() == 0)
			return null;
		
		return (EPPResult) results.elementAt(0);
	} // End EPPResponse.getResult()
	

	/**
	 * Does the response have a success status?  <code>isSuccess</code> will
	 * check that there is only one <code>EPPResult</code> defined and the
	 * result code is set to a value of 10??.
	 *
	 * @return <code>true</code> if is success; <code>false</code> otherwise.
	 */
	public boolean isSuccess() {
		if (results.size() != 1) {
			return false;
		}

		return ((EPPResult) results.elementAt(0)).isSuccess();
	} // End EPPReponse.isSuccess()

	/**
	 * Does the response have a result with the specified result code?
	 *
	 * @param aCode Result code to scan for
	 *
	 * @return <code>true</code> if the result code exists in response;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasResultCode(int aCode) {
		Iterator theIter = results.iterator();

		while (theIter.hasNext()) {
			if (((EPPResult) theIter.next()).getCode() == aCode) {
				return true;
			}
		}

		return false;
	} // End EPPResponse.hasResultCode(int)

	/**
	 * Sets an individual result for the response with a result code.  The EPP
	 * Specification defines the range of the result codes to be 1000 - 9999. <br>
	 * <code>setResult(int)</code> will set the results to a Vector of one
	 * <code>EPPResult</code> with the result code set to <code>aCode</code>. <br>
	 * If the code is set to one of the pre-defined result codes, the result
	 * text will be set to the matching en value, as defined in the EPP
	 * Specification; otherwise the text is set to the empty string "".
	 *
	 * @param aCode Result code (1000-9999)
	 */
	public void setResult(int aCode) {
		results = new Vector();
		results.addElement(new EPPResult(aCode));
	} // End EPPResponse.setResult(int)

	/**
	 * Sets an individual result for the response with a result code and the
	 * result text.  The EPP Specification defines the range of the result
	 * codes to be 1000 - 9999. <br>
	 * <code>setResult(int)</code> will set the results to a Vector of one
	 * <code>EPPResult</code> with the result code set to <code>aCode</code>,
	 * and the result text set to <code>aText</code>.  The language of the
	 * text will default to "en". <br>
	 *
	 * @param aCode Result code (1000-9999)
	 * @param aText Result text in the default "en" language.
	 */
	public void setResult(int aCode, String aText) {
		results = new Vector();
		results.addElement(new EPPResult(aCode, aText));
	} // End EPPResponse.setResult(int, String)

	/**
	 * Sets an individual result for the response with a result code, the
	 * result text, and a Vector of <code>String</code> values.  This method
	 * should only be called if there is an error, since values are not
	 * included with successful results.  The EPP Specification defines the
	 * range of the result codes to be 1000 - 9999. <br>
	 * <code>setResult(int)</code> will set the results to a Vector of one
	 * <code>EPPResult</code> with the result code set to <code>aCode</code>,
	 * the result text set to <code>aText</code>, and the values set to
	 * <code>someValues</code>.  The language of the text will default to
	 * "en". <br>
	 *
	 * @param aCode Result code (1000-9999)
	 * @param aText Result text in the default "en" language.
	 * @param aValues Vector of <code>EPPValue</code> or
	 * 		  <code>EPPExtValue</code> instances
	 */
	public void setResult(int aCode, String aText, Vector aValues) {
		results = new Vector();
		results.addElement(new EPPResult(aCode, aText, aValues));
	} // End EPPResponse.setResult(int, String, Vector)

	/**
	 * Sets an individual result for the response with an
	 * <code>EPPResult</code> instance.  This method allows for specification
	 * of result attributes like the text language.
	 *
	 * @param aResult Individual response result.
	 */
	public void setResult(EPPResult aResult) {
		results = new Vector();
		results.addElement(aResult);
	} // End EPPResponse.setResult(Vector)

	/**
	 * Sets the result(s) of the response as a Vector of <code>EPPResult</code>
	 * instances.  This method should only be used if there is an error, since
	 * only errors can contain more than one result.
	 *
	 * @param someResults Vector of <code>EPPResult</code> instances
	 */
	public void setResults(Vector someResults) {
		results = someResults;
	} // End EPPResponse.setResults(Vector)

	/**
	 * Does the response have an extension object of a specified class?  If so,
	 * the extension object can be retrieved with a call to
	 * <code>getExtensions(Class)</code>.
	 *
	 * @param aExtensionClass Specific extension class to look for
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
	} // End EPPResponse.hasExtension(Class)

	/**
	 * Gets the extension object with the specified class. The extension object
	 * is an unspecified element in the EPP Specifications.  To create an
	 * extension object, an XML Schema for the extension object must exist
	 * with a unique XML Namespace.  A custom <code>EPPExtensionFactory</code>
	 * must be created for the extension, which returns an instance of
	 * <code>EPPCodecComponent</code> for an instance of an extension object
	 * in the EPP Response.
	 *
	 * @param aExtensionClass of desired extension
	 *
	 * @return Concrete <code>EPPCodecComponent</code> associated with the
	 * 		   response if exists; <code>null</code> otherwise.
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
	} // End EPPResponse.getExtension(Class)
	
	/**
	 * Gets the response extension object with the specified class with the
	 * option to fail when a duplicate extension is found. The extension object
	 * is an unspecified element in the EPP Specifications. To create an
	 * extension object, an XML Schema for the extension object must exist with
	 * a unique XML Namespace. A custom <code>EPPExtensionFactory</code> must be
	 * created for the extension, which returns an instance of
	 * <code>EPPCodecComponent</code> for an instance of an extension object in
	 * the <code>EPPResponse</code>.
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
	} // End EPPResponse.getExtension(Class, boolean)
	

	/**
	 * Sets a response extension object.  The extension object is an
	 * unspecified element in the EPP Specifications.  The unspecified element
	 * will be encoded under the &ltunspec&gt element of the EPP Response.
	 *
	 * @param aExtension response extension object associated with the response
	 *
	 * @deprecated Replaced by {@link #addExtension(EPPCodecComponent)}.  This
	 * 			   method will add the extension as is done in {@link
	 * 			   #addExtension(EPPCodecComponent)}.
	 */
	public void setExtension(EPPCodecComponent aExtension) {
		this.addExtension(aExtension);
	} // End EPPResponse.setExtension(EPPCodecComponent)

	/**
	 * Adds an extension object.  The extension object is an unspecified
	 * element in the EPP Specifications.  The unspecified element will be
	 * encoded under the &ltunspec&gt element of the EPP Response.
	 *
	 * @param aExtension extension object associated with the response
	 */
	public void addExtension(EPPCodecComponent aExtension) {
		if (extensions == null) {
			extensions = new Vector();
		}

		extensions.addElement(aExtension);
	} // End EPPResponse.addExtension(EPPCodecComponent)

	/**
	 * Does the response have an extension objects?  If so, the command
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
	} // End EPPResponse.hasExtensions()

	/**
	 * Gets the extensions.  The extension objects are an unspecified elements
	 * in the EPP Specification.  To create an extension object, an XML Schema
	 * for the extension object must exist with a unique XML Namespace.  A
	 * custom <code>EPPExtensionFactory</code> must be created for the
	 * extension, which returns an instance of <code>EPPCodecComponent</code>
	 * for an instance of an extension object in the EPP Response.
	 *
	 * @return <code>Vector</code> of concrete <code>EPPCodecComponent</code>
	 * 		   associated with the response if exists; <code>null</code>
	 * 		   otherwise.
	 */
	public Vector getExtensions() {
		return extensions;
	} // End EPPResponse.getExtensions()

	/**
	 * Sets the response extension objects.  The extension objects are an
	 * unspecified element in the EPP Specifications.  The unspecified element
	 * will be encoded under the &ltunspec&gt element of the EPP Response.
	 *
	 * @param aExtensions command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		extensions = aExtensions;
	} // End EPPResponse.setExtensions(Vector)

	/**
	 * Does the response have a message queue object?  If so, the response
	 * message queue object can be retrieved with a call to
	 * <code>getMessageQueue</code>.
	 *
	 * @return <code>true</code> if this is an message queue object;
	 * 		   <code>false</code> otherwise.
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #hasMsgQueue()}.  This
	 * 			   method will return <code>true</code> if a message queue
	 * 			   object exists that has a non-<code>null</code> message
	 * 			   count.
	 */
	public boolean hasMessageQueue() {
		if ((this.msgQueue != null) && (this.msgQueue.getCount() != null)) {
			return true;
		}
		else {
			return false;
		}
	} // End EPPResponse.hasMessageQueue()

	/**
	 * Gets the response message queue object.  Currently the message queue
	 * object is a <code>Long</code> count of the number of messages.
	 *
	 * @return Number of queue messages if defined; <code>null</code>
	 * 		   otherwise.
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #getMsgQueueCount()}
	 */
	public Long getMessageQueue() {
		if (this.msgQueue != null) {
			return this.msgQueue.getCount();
		}
		else {
			return null;
		}
	} // End EPPResponse.getMessageQueue()

	/**
	 * Get the response message queue date.
	 *
	 * @return message queue date if defined; <code>null</code> otherwise.
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #getMsgQueueDate()}
	 */
	public Date getQDate() {
		if (this.msgQueue != null) {
			return this.msgQueue.getQDate();
		}
		else {
			return null;
		}
	} // End EPPResponse.getQDate()

	/**
	 * Does the response have a message queue object?  If so, the response
	 * message queue object can be retrieved with a call to
	 * <code>getMsgQueue</code>.
	 *
	 * @return <code>true</code> if this is an message queue object;
	 * 		   <code>false</code> otherwise.
	 *
	 * @since EPP 1.0
	 */
	public boolean hasMsgQueue() {
		if (this.msgQueue != null) {
			return true;
		}
		else {
			return false;
		}
	} // End EPPResponse.hasMsgQueue()

	/**
	 * Sets the response message queue object.
	 *
	 * @param aMsgQueue queue object
	 *
	 * @since EPP 1.0
	 */
	public void setMsgQueue(EPPMsgQueue aMsgQueue) {
		this.msgQueue = aMsgQueue;
	} // End EPPResponse.setMsgQueue(EPPMsgQueue)

	/**
	 * Gets the response message queue object.
	 *
	 * @return Message queue object if defined; <code>null</code> otherwise.
	 */
	public EPPMsgQueue getMsgQueue() {
		if (this.msgQueue != null) {
			return this.msgQueue;
		}
		else {
			return null;
		}
	} // End EPPResponse.getMsgQueue()

	/**
	 * Convenience method for getting the message queue count attribute. This
	 * is equivalent to getting the message queue object by {@link
	 * #getMsgQueue()}  and than calling {@link
	 * com.verisign.epp.codec.gen.EPPMsgQueue#getCount()}.
	 *
	 * @return Count of queue messages if defined; <code>null</code> otherwise.
	 *
	 * @since EPP 1.0
	 */
	public Long getMsgQueueCount() {
		if (this.msgQueue != null) {
			return this.msgQueue.getCount();
		}
		else {
			return null;
		}
	} // End EPPResponse.getMsgQueueCount()

	/**
	 * Convenience method for getting the message queue date attribute.
	 *
	 * @return message queue date if defined; <code>null</code> otherwise.
	 *
	 * @since EPP 1.0
	 */
	public Date getMsgQueueDate() {
		if (this.msgQueue != null) {
			return this.msgQueue.getQDate();
		}
		else {
			return null;
		}
	} // End EPPResponse.getMsgQueueDate()

	/**
	 * Convenience method for getting the message queue message attribute.
	 *
	 * @return message queue message if defined; <code>null</code> otherwise.
	 *
	 * @since EPP 1.0
	 */
	public String getMsgQueueMsg() {
		if (this.msgQueue != null) {
			return this.msgQueue.getMsg();
		}
		else {
			return null;
		}
	} // End EPPResponse.getMsgQueueMsg()

	/**
	 * encode <code>EPPResponse</code> into a DOM element tree.  The
	 * &ltresponse&gt element     is created and the attribute nodes are
	 * appending as children.  This method is     a <i>Template Method</i> in
	 * the Template Method Design Pattern.
	 *
	 * @param aDocument Used as a document factory
	 *
	 * @return &ltresponse&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// <response>
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Results
		EPPUtil.encodeCompVector(aDocument, root, results);

		// Message Queue
		EPPUtil.encodeComp(aDocument, root, msgQueue);

		// Response Mapping
		Element mapping = doEncode(aDocument);

		if (mapping != null) {
			Element elementData =
				aDocument.createElementNS(EPPCodec.NS, ELM_RESPONSE_DATA);

			elementData.appendChild(mapping);

			root.appendChild(elementData);
		}

		// Extension Element
		if (extensions != null) {
			Element extensionElm =
				aDocument.createElementNS(EPPCodec.NS, ELM_EXTENSION);

			root.appendChild(extensionElm);

			EPPUtil.encodeCompVector(aDocument, extensionElm, extensions);
		}

		// Transaction ID
		EPPUtil.encodeComp(aDocument, root, transId);

		return root;
	} // End EPPResponse.encode(Document)

	/**
	 * decode <code>EPPResponse</code> from a DOM element tree.  The "response"
	 * element needs to be the value of the <code>aElement</code> argument.
	 * This method is     a <i>Template Method</i> in the Template Method
	 * Design Pattern.
	 *
	 * @param aElement &ltresponse&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 * @exception EPPComponentNotFoundException An extension component could not be found
	 */
	public void decode(Element aElement) throws EPPDecodeException, EPPComponentNotFoundException {
		// Results
		results =
			EPPUtil.decodeCompVector(
									 aElement, EPPCodec.NS, EPPResult.ELM_NAME,
									 EPPResult.class);

		// Message Queue
		this.msgQueue =
			(EPPMsgQueue) EPPUtil.decodeComp(
											 aElement, EPPCodec.NS,
											 EPPMsgQueue.ELM_NAME,
											 EPPMsgQueue.class);

		// Response Data
		Element resDataElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPCodec.NS, ELM_RESPONSE_DATA);

		// Is there any Response Data?
		if (resDataElm != null) {
			// Decode Concrete Response
			Element responseMap =
				EPPUtil.getFirstElementChild(resDataElm);

			if (responseMap == null) {
				throw new EPPDecodeException("No child element found for "
											 + ELM_RESPONSE_DATA);
			}

			doDecode(responseMap);
		}

		// Extension Element
		Element extensionElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPCodec.NS, ELM_EXTENSION);
		
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
							"EPPResponse.decode unable to create extension object: "
												 + e);
				}
				theExtension.decode(currExtension);

				this.addExtension(theExtension);
				
				currExtension = EPPUtil.getNextElementSibling(currExtension);
			} // End while (currExtension != null)
			
		} // End if (extensionElm != null)
		
		// Transaction ID
		transId =
			(EPPTransId) EPPUtil.decodeComp(
											aElement, EPPCodec.NS,
											EPPTransId.ELM_NAME,
											EPPTransId.class);
	} // End EPPResponse.encode(Element)

	/**
	 * implements a deep <code>EPPResponse</code> compare.
	 *
	 * @param aObject <code>EPPResponse</code> instance to compare with
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		EPPResponse theResponse = (EPPResponse) aObject;

		// Results
		if (!EPPUtil.equalVectors(results, theResponse.results)) {
			return false;
		}

		// Message Queue
		if (
			!(
					(msgQueue == null) ? (theResponse.msgQueue == null)
										   : msgQueue.equals(theResponse.msgQueue)
				)) {
			return false;
		}

		// Extensions
		if (!EPPUtil.equalVectors(extensions, theResponse.extensions)) {
			return false;
		}

		// Transaction Id
		if (
			!(
					(transId == null) ? (theResponse.transId == null)
										  : transId.equals(theResponse.transId)
				)) {
			return false;
		}

		return true;
	} // End EPPResponse.equals(Object)

	/**
	 * Clone <code>EPPResponse</code>.
	 *
	 * @return clone of <code>EPPResponse</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPResponse clone = null;
		clone     = (EPPResponse) super.clone();

		// Results
		clone.results = (Vector) results.clone();

		for (int i = 0; i < results.size(); i++) {
			clone.results.setElementAt(
									   ((EPPResult) results.elementAt(i)).clone(),
									   i);
		}

		// Message Queue
		if (msgQueue != null) {
			clone.msgQueue = (EPPMsgQueue) this.msgQueue.clone();
		}

		// Extensions
		if (extensions != null) {
			clone.extensions = (Vector) extensions.clone();

			for (int i = 0; i < extensions.size(); i++) {
				clone.extensions.setElementAt(
											  ((EPPCodecComponent) extensions
											   .elementAt(i)).clone(), i);
			}
		}

		// Transaction Id
		if (transId != null) {
			clone.transId = (EPPTransId) transId.clone();
		}

		return clone;
	} // End EPPResponse.clone()

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
	} // End EPPResponse.toString()

	/**
	 * Encodes the attributes of the concrete <code>EPPResponse</code> and must
	 * be overridden by <code>EPPResponse</code> derived classes.  The default
	 * implementation is to do nothing, since <code>EPPResponse</code> is a
	 * concrete class. <code>encode</code> is a <i>Template Method</i>    and
	 * this method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aDocument DOM document used as a factory of DOM objects.
	 *
	 * @return instance root DOM element along with attribute child nodes.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		return null;
	} // End EPPResponse.doEncode(Document)

	/**
	 * Decodes the attributes of the concrete <code>EPPResponse</code> and must
	 * be overridden by <code>EPPResponse</code> derived classes.  The default
	 * implementation is to do nothing, since <code>EPPResponse</code> is a
	 * concrete class. <code>decode</code> is a <i>Template Method</i>    and
	 * this method is a <i>Primitive Operation</i> within the Template Method
	 * Design Pattern.
	 *
	 * @param aElement root DOM element associated with instance
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Do nothing since EPPReponse can be concrete.
	} // End EPPResponse.doDecode(Element)
}
