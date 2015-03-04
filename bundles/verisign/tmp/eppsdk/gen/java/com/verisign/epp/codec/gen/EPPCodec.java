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
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.verisign.epp.util.EPPCatFactory;


/**
 * Singleton class to encode and decode <code>EPPMessage</code> instances to
 * and from DOM Documents.  <code>EPPCodec</code> encapsulates the details of
 * the EPP and is capable of handling new EPP Command Mappings through the use
 * of <code>EPPFactory</code>.     <br>
 * <br>
 * Utility methods are provided for decoding specific concrete
 * <code>EPPMessages</code> including:     <br>
 * 
 * <ul>
 * <li>
 * <code>EPPCommand</code> - EPP Command encoded by client and decoded by
 * server
 * </li>
 * <li>
 * <code>EPPResponse</code> - EPP Response encoded by server and decoded by
 * client
 * </li>
 * <li>
 * <code>EPPGreeting</code> - EPP Greeting encoded by server and decoded by
 * client
 * </li>
 * <li>
 * <code>EPPHello</code> - EPP Hello encoded by client and decoded by server
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPMessage
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPHello
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPCodec {
	/**
	 * version of the EPP.  This is mapped to the version of the     general
	 * EPP Specification.
	 */
	public static final String VERSION = "1.0";

	/** EPP general XML namespace URI. */

	//public static final String NS 	 		= "urn:iana:xml:ns:epp-1.0";
	//new epp version-05 protocol namespace
	public static final String NS = "urn:ietf:params:xml:ns:epp-1.0";

	/** EPP general XML namespace prefix. */
	public static final String NS_PREFIX = "epp";

	/** EPP general XML schema location. */

	//public static final String NS_SCHEMA	= "urn:iana:xml:ns:epp-1.0 epp-1.0.xsd";
	//new epp version -05 schema location
	public static final String NS_SCHEMA =
		"urn:ietf:params:xml:ns:epp-1.0 epp-1.0.xsd";

	/** XML Schema instance used by the EPP XML Schemas. */
	public static final String NS_XSI =
		"http://www.w3.org/2001/XMLSchema-instance";

	/** Singleton instance of EPPCodec. */
	private static EPPCodec instance = new EPPCodec();

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPCodec.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Name for the EPP root element. */
	private final String ELM_EPP = "epp";


	/**
	 * allocates the Singleton <code>EPPCodec</code> instance.  The XML Parser
	 * used to construct     the DOM Document is initialized to be namespace
	 * aware and to not validate.  JAXP is     used to instantiate the
	 * concrete XML Parser, and the XML Parser is only used during     an
	 * <code>encode</code> operation.
	 */
	protected EPPCodec() {
	}


	/**
	 * gets the Singleton instance of <code>EPPCodec</code>.
	 * <code>EPPCodec</code> follows     the Singleton Design Pattern.
	 *
	 * @return Singleton instance of <code>EPPCodec</code>.
	 */
	public static EPPCodec getInstance() {
		return instance;
	}


	/**
	 * Initialize the Singleton instance.  This method initializes all of the
	 * components     used by <code>EPPCodec</code>.     <br>
	 * A <code>Vector</code> of concrete <code>EPPMapFactory</code> fully
	 * qualified class names are     specified to initialize the
	 * <code>EPPCodec</code> with the supported     EPP Command Mappings.  For
	 * example, support for the Domain Command Mapping is added     to
	 * <code>EPPCodec</code> by included the class name
	 * "com.verisign.epp.codec.domain.EPPDomainMapFactory"     in the
	 * <code>someFactories</code> argument.  The following code shows the
	 * steps to     include the Domain Command Mapping and the Host Command
	 * Mapping in the <code>EPPCodec</code>.     <br>
	 * <pre>
	 * 	Vector theFactories = new Vector();
	 * 	theFactories.addElement("com.verisign.epp.codec.domain.EPPDomainMapFactory");
	 * 	theFactories.addElement("com.verisign.epp.codec.host.HostMapFactory");
	 * 	EPPCodec.getInstance().init(theFactories);
	 * 	</pre>
	 *
	 * @param someFactories a Vector of concrete <code>EPPMapFactory</code>
	 * 		  fully qualified class names.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	public void init(Vector someFactories) throws EPPCodecException {
		cat.debug("init(Vector): enter");

		EPPFactory.getInstance().init(someFactories);

		cat.debug("init(Vector): exit");
	}


	/**
	 * Initialize the Singleton instance.  This method initializes all of the
	 * components     used by <code>EPPCodec</code>. A<code>Vector</code> of
	 * concrete <code>EPPCommandResponseExtension</code> of fully qualified
	 * class names are specified to initialize the <code>EPPCodec</code> with
	 * the supported Command Response Extensions. For example support for the
	 * Protocol Level Extensiosn and  CommanResponse Extensions is added to
	 * <code>EPPCodec</code> by including the class names namely
	 * "com.verisign.epp.codec.gen.EPPProtocolExtensions",
	 * "com.verisign.epp.codec.gen.EPPCommandResponseExtensions" in the
	 * <code>extensionFactories</code> argument.The follwoign code shows the
	 * steps to the ProtcolExtensions and CommandResponse Extensions in the
	 * <code>EPPCodec</code>. <br>
	 * <pre>
	 *  Vector protocolExts = EPPEnv.getProtocolExtensions();
	 * 	Vector commandExts=EPPEnv.getCmdResponseExtensions();
	 * 	Vector extensionsVector=new Vector()
	 *  extensionsVector.addElement((String)protocolExts.elementAt(i));
	 * 	extensionsVector.addElement((String)commandExts.elementAt(j));
	 * 	Now instantiate the Codec instance with both the mapfactories and extension factories
	 * 	EPPCodec.getInstance().init(EPPEnv.getMapFactories(),extensionsVector);
	 * 	</pre>
	 *
	 * @param someFactories a Vector of concrete <code>EPPMapFactory</code>
	 * 		  fully qualified class names.
	 * @param extensionFactories a Vector of concrete
	 * 		  <code>EPPProtocolExtension</code> and
	 * 		  <code>EPPCommandResponseExtesnsions</code>
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	public void init(Vector someFactories, Vector extensionFactories)
			  throws EPPCodecException {
		cat.debug("init(Vector, Vector): enter");

		EPPFactory.getInstance().init(someFactories, extensionFactories);

		cat.debug("init(Vector, Vector): exit");
	}


	/**
	 * encodes a concrete <code>EPPMessage</code> into a DOM Document.
	 *
	 * @param aMessage Concrete <code>EPPMessage</code> to encode.
	 *
	 * @return Encoded DOM Document of representing the <code>EPPMessage</code>
	 *
	 * @exception EPPEncodeException Error encoding the
	 * 			  <code>EPPMessage</code>.
	 */
	public Document encode(EPPMessage aMessage) throws EPPEncodeException {
		
		// Using Xerces specific class in place of JAXP for speed
		Document document = new DocumentImpl();

		// Create root EPP element
		Element root = document.createElementNS(EPPCodec.NS, ELM_EPP);

		root.setAttribute("xmlns:xsi", NS_XSI);
		root.setAttributeNS(NS_XSI, "xsi:schemaLocation", NS_SCHEMA);

		//create the extension tag by calling the protocol extension class
		// Append the child EPPMessage DOM tree.
		root.appendChild(aMessage.encode(document));

		document.appendChild(root);

		return document;
	}


	/**
	 * decodes a DOM Document into a concrete <code>EPPMessage</code>.
	 *
	 * @param aDocument DOM Document to decode into a concrete
	 * 		  <code>EPPMessage</code>.
	 *
	 * @return Encoded concrete <code>EPPMessage</code>.
	 *
	 * @exception EPPDecodeException Error decoding the DOM Document.
	 */
	public EPPMessage decode(Document aDocument) throws EPPDecodeException {
		return decode(aDocument.getDocumentElement());
	}


	/**
	 * decodes a DOM Element tree into a concrete <code>EPPMessage</code>.
	 *
	 * @param root Root EPP element to decode from
	 *
	 * @return Encoded concrete <code>EPPMessage</code>.
	 *
	 * @exception EPPDecodeException Error decoding the DOM Document.
	 * @exception EPPComponentNotFoundException A component could not be found which could be 
	 * a command, response, or extension component.
	 */
	public EPPMessage decode(Element root) throws EPPDecodeException, EPPComponentNotFoundException {
		EPPMessage retVal = null;
		

		// Validate root element attribute values
		if (!EPPCodec.NS.equals(root.getNamespaceURI())
				|| !root.getLocalName().equals("epp")) {
			throw new EPPDecodeException("Invalid root element NS = "
					+ root.getNamespaceURI() + ", name = "
					+ root.getLocalName());
		}

		// Handle Message Type
		Element messageType = EPPUtil.getFirstElementChild(root);

		if (messageType == null) {
			throw new EPPDecodeException("No element child was found from the root node");
		}

		// Greeting?
		if (EPPCodec.NS.equals(root.getNamespaceURI())
				&& messageType.getLocalName().equals(
						EPPUtil.getLocalName(EPPGreeting.ELM_NAME))) {
			retVal = new EPPGreeting();
		}

		// Hello?
		else if (EPPCodec.NS.equals(root.getNamespaceURI())
				&& messageType.getLocalName().equals(
						EPPUtil.getLocalName(EPPHello.ELM_NAME))) {
			retVal = new EPPHello();
		}

		// Command?
		else if (EPPCodec.NS.equals(root.getNamespaceURI())
				&& messageType.getLocalName().equals(
						EPPUtil.getLocalName(EPPCommand.ELM_NAME))) {
			
			Element commandType = EPPUtil.getFirstElementChild(messageType);

			if (commandType == null) {
				throw new EPPDecodeException("Command Type Element could not be found");
			}

			String  commandTypeName = commandType.getLocalName();

			Element commandMap = EPPUtil.getFirstElementChild(commandType);

			// Create Concrete Command
			if ((commandMap != null)
					&& (commandMap.getLocalName().startsWith(commandTypeName))) {
				try {
					retVal = EPPFactory.getInstance().createCommand(commandMap);
				}
				catch (EPPCodecException e) {
					throw new EPPComponentNotFoundException(
							EPPComponentNotFoundException.COMMAND,
							"Unable to create concrete command for type "
									+ commandTypeName + ": " + e);
				}
			}
			else {
				String op = commandType.getAttribute(EPPCommand.ATT_OP);

				try {
					retVal = EPPFactory.getInstance().createCommand(
							commandTypeName, op);
				}
				catch (EPPCodecException e) {
					throw new EPPComponentNotFoundException(
							EPPComponentNotFoundException.COMMAND,
							"Unable to create concrete command for type "
									+ commandTypeName + " and op " + op + " : "
									+ e);
				}
			}
		}

		// Response?
		else if (EPPCodec.NS.equals(root.getNamespaceURI())
				&& messageType.getLocalName().equals(
						EPPUtil.getLocalName(EPPResponse.ELM_NAME))) {
			
			NodeList responseDataElm = messageType.getElementsByTagNameNS(
					EPPCodec.NS, EPPUtil
							.getLocalName(EPPResponse.ELM_RESPONSE_DATA));

			switch (responseDataElm.getLength()) {
				// No Response Extension?
				case 0:
					retVal = new EPPResponse();

					break;

				// Response Extension?
				case 1:

					// Create Concrete Response
					Element responseMap =
						EPPUtil.getFirstElementChild((Element) responseDataElm
													 .item(0));

					if (responseMap == null) {
						throw new EPPDecodeException("No child element found for "
													 + EPPResponse.ELM_RESPONSE_DATA);
					}

					try {
						retVal =
							EPPFactory.getInstance().createResponse(responseMap);
					}
					 catch (EPPCodecException e) {
						throw new EPPComponentNotFoundException(
								EPPComponentNotFoundException.RESPONSE, 
								"Unable to create concrete response: "
													 + e);
					}

					break;

				default:
					throw new EPPDecodeException("Invalid number of "
												 + EPPResponse.ELM_RESPONSE_DATA
												 + " elements of "
												 + responseDataElm.getLength());
			} // end switch (responseDataElm.getLength())
		}

		// Protocol Extension
		else if (EPPCodec.NS.equals(root.getNamespaceURI())
				&& messageType.getLocalName().equals(
						EPPUtil.getLocalName(EPPProtocolExtension.ELM_NAME))) // extension
		{
			Element extensionElm = EPPUtil.getFirstElementChild(messageType); //ext element

			// Is there a protocol extension Element?
			if (extensionElm != null) {
				try {
					retVal =
						EPPFactory.getInstance().createProtocolExtension(extensionElm);
				}
				 catch (EPPCodecException e) {
					throw new EPPComponentNotFoundException(
							EPPComponentNotFoundException.EXTENSION, 
							"EPPCommand.decode unable to create protocol extension object: "
												 + e);
				}
			}
			else // No protocol extension Element
			 {
				throw new EPPDecodeException("No child element found for the protocol extension");
			}
		}
		else {
			throw new EPPDecodeException("Invalid message tag name of "
										 + messageType.getTagName());
		}

		// Decode the message.
		retVal.decode(messageType);

		return retVal;
	}


	/**
	 * utility method that will decode a DOM Document and return an
	 * <code>EPPCommand</code> instance.     An
	 * <code>EPPDecodeException</code> will be thrown if the decoded
	 * <code>EPPMessage</code>     is not an <code>EPPCommand</code>.
	 *
	 * @param aDocument DOM Document to decode into an <code>EPPCommand</code>.
	 *
	 * @return Encoded concrete <code>EPPCommand</code>.
	 *
	 * @exception EPPDecodeException Error decoding the DOM Document or
	 * 			  <code>EPPMessage</code> is not an <code>EPPCommand</code>
	 */
	public EPPCommand decodeCommand(Document aDocument)
							 throws EPPDecodeException {
		EPPMessage theMessage = decode(aDocument);

		if (!(theMessage instanceof EPPCommand)) {
			throw new EPPDecodeException("Decoded message is not an EPPCommand on call to decodeCommand");
		}

		return (EPPCommand) theMessage;
	}


	/**
	 * utility method that will decode a DOM Document and return an
	 * <code>EPPResponse</code> instance.     An
	 * <code>EPPDecodeException</code> will be thrown if the decoded
	 * <code>EPPMessage</code> is     not an <code>EPPResponse</code>.
	 *
	 * @param aDocument DOM Document to decode into an
	 * 		  <code>EPPResponse</code>.
	 *
	 * @return Encoded concrete <code>EPPResponse</code>.
	 *
	 * @exception EPPDecodeException Error decoding the DOM Document or
	 * 			  <code>EPPMessage</code> is not an <code>EPPResponse</code>
	 */
	public EPPResponse decodeResponse(Document aDocument)
							   throws EPPDecodeException {
		EPPMessage theMessage = decode(aDocument);

		if (!(theMessage instanceof EPPResponse)) {
			throw new EPPDecodeException("Decoded message is not an EPPResponse on call to decodeResponse");
		}

		return (EPPResponse) theMessage;
	}


	/**
	 * utility method that will decode a DOM Document and return an
	 * <code>EPPGreeting</code> instance.     An
	 * <code>EPPDecodeException</code> will be thrown if the decoded
	 * <code>EPPMessage</code>     is not an <code>EPPGreeting</code>.
	 *
	 * @param aDocument DOM Document to decode into an
	 * 		  <code>EPPGreeting</code>.
	 *
	 * @return Encoded concrete <code>EPPGreeting</code>.
	 *
	 * @exception EPPDecodeException Error decoding the DOM Document or
	 * 			  <code>EPPMessage</code> is not an <code>EPPGreeting</code>
	 */
	public EPPGreeting decodeGreeting(Document aDocument)
							   throws EPPDecodeException {
		EPPMessage theMessage = decode(aDocument);

		if (!(theMessage instanceof EPPGreeting)) {
			throw new EPPDecodeException("Decoded message is not an EPPGreeting on call to decodeGreeting");
		}

		return (EPPGreeting) theMessage;
	}


	/**
	 * utility method that will decode a DOM Document and return an
	 * <code>EPPHello</code> instance.     An <code>EPPDecodeException</code>
	 * will be thrown if the decoded <code>EPPMessage</code>     is not an
	 * <code>EPPHello</code>.
	 *
	 * @param aDocument DOM Document to decode into an <code>EPPHello</code>.
	 *
	 * @return Encoded concrete <code>EPPHello</code>.
	 *
	 * @exception EPPDecodeException Error decoding the DOM Document or
	 * 			  <code>EPPMessage</code> is not an <code>EPPHello</code>
	 */
	public EPPHello decodeHello(Document aDocument) throws EPPDecodeException {
		EPPMessage theMessage = decode(aDocument);

		if (!(theMessage instanceof EPPHello)) {
			throw new EPPDecodeException("Decoded message is not an EPPHello on call to decodeHello");
		}

		return (EPPHello) theMessage;
	}

}
