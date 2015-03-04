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
 * Identifies an EPP Command Mapping service supported by the
 * <code>EPPCodec</code>.  A service contains     an XML namespace prefix, an
 * XML namespace URI, and an XML Schema location.  Each concrete
 * <code>EPPMapFactory</code> is associated with an <code>EPPService</code>
 * that is used as a     descriptor of the service.  The list of supported
 * <code>EPPServices</code> can be retreived     from the
 * <code>EPPFactory</code>     An EPPService is a member of different EPP
 * Messages including the <code>EPPGreeting</code> and     the
 * <code>EPPLoginCmd</code>.  An <code>EPPService</code> is encoded into an
 * individual XML     element with a tag name of <i>XML namespace
 * prefix</i>:service and the XML attributes set to     the attribute values.
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPMapFactory
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPLoginCmd
 */
public class EPPService implements EPPCodecComponent {
	/** The service is an object service */
	public final static int OBJ_SERVICE = 0;

	/** The service is an extension service */
	public final static int EXT_SERVICE = 1;

	/** XML tag name for an object <code>EPPService</code>. */
	final static String ELM_OBJ_URI = "objURI";

	/** XML tag name for an extension <code>EPPService</code>. */
	final static String ELM_EXT_URI = "extURI";

	/**
	 * Defines the type of service as either OBJ_SERVICE or EXT_SERVICE.
	 * Default is OBJ_SERVICE.
	 */
	public int serviceType = OBJ_SERVICE;

	/** XML Namespace prefix for the service. */
	private String namespacePrefix;

	/** ML Namespace URI for the service. */
	private String namespaceURI;

	/** The location of the XML Schema. */
	private String schemaLocation;

	//Empty Constructor
	public EPPService() {
	}

	/**
	 * Allocates a new <code>EPPService</code> and sets all of the required
	 * attributes to the arguments values.
	 *
	 * @param aNamespacePrefix XML Namespace prefix for the service.  For
	 * 		  example, The Domain Mapping prefix is "domain".
	 * @param aNamespaceURI XML Namespace URI for the service.  For example,
	 * 		  The Domain Mapping URI is "urn:iana:xmlns:domain".
	 * @param aSchemaLocation The location of the XML Schema.  For example, the
	 * 		  Domain Mapping Schema Location is "urn:iana:xmlns:domain
	 * 		  domain.xsd".
	 */
	public EPPService(
					  String aNamespacePrefix, String aNamespaceURI,
					  String aSchemaLocation) {
		namespacePrefix     = aNamespacePrefix;
		namespaceURI	    = aNamespaceURI;
		schemaLocation	    = aSchemaLocation;
	}

	/// EPPService.EPPService(String, String, String)

	/**
	 * Gets the XML namespace URI associated with the service.
	 *
	 * @return XML namespace URI <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getNamespaceURI() {
		return namespaceURI;
	}

	// End EPPService.getNamespaceURI()

	/**
	 * Set the XML namespace URI associated with the service.
	 *
	 * @param aNamespaceURI XML namespace URI <code>String</code>
	 */
	public void setNamespaceURI(String aNamespaceURI) {
		namespaceURI = aNamespaceURI;
	}

	// End EPPService.setNamespaceURI(String)

	/**
	 * Gets the XML namespace prefix associated with the service.
	 *
	 * @return XML namespace prefix <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	// End EPPService.getNamespacePrefix()

	/**
	 * Set the XML namespace prefix associated with the service.
	 *
	 * @param aNamespacePrefix XML namespace prefix <code>String</code>
	 */
	public void setNamespacePrefix(String aNamespacePrefix) {
		namespacePrefix = aNamespacePrefix;
	}

	// End EPPService.setNamespacePrefix(String)

	/**
	 * Gets the XML Schema location associated with the service.
	 *
	 * @return XML Schema location <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getSchemaLocation() {
		return schemaLocation;
	}

	// End EPPService.getSchemaLocation()

	/**
	 * Set the XML Schema location associated with the service.
	 *
	 * @param aSchemaLocation XML Schema location <code>String</code>
	 */
	public void setSchemaLocation(String aSchemaLocation) {
		schemaLocation = aSchemaLocation;
	}

	// End EPPService.setSchemaLocation()

	/**
	 * Set the service type associated with this service
	 *
	 * @param aServiceType .
	 */
	public void setServiceType(int aServiceType) {
		serviceType = aServiceType;
	}

	/**
	 * Gets the service type associcted with this service
	 *
	 * @return servicetype <code>int</code> ; <code>null</code> otherwise.
	 */
	public int getServiceType() {
		return serviceType;
	}

	/**
	 * encode <code>EPPService</code> into a DOM element tree.  The
	 * "<i>prefix</i>:service"     element is created and the attribute values
	 * are appended as XML attributes.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return service root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = null;

		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPService invalid state: " + e);
		}

		if (serviceType == OBJ_SERVICE) {
			root = aDocument.createElementNS(EPPCodec.NS, ELM_OBJ_URI);

			root.appendChild(aDocument.createTextNode(namespaceURI));
		}
		else if (serviceType == EXT_SERVICE) {
			root = aDocument.createElementNS(EPPCodec.NS, ELM_EXT_URI);

			root.appendChild(aDocument.createTextNode(namespaceURI));
		}

		return root;
	}

	// End EPPService.encode(Document)

	/**
	 * decode <code>EPPService</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the "<i>prefix</i>:service"
	 * element.
	 *
	 * @param aElement The "<i>prefix</i>:service" XML element.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		if (serviceType == OBJ_SERVICE) {
			namespaceURI = aElement.getFirstChild().getNodeValue();
		}

		else if (serviceType == EXT_SERVICE) {
			namespaceURI = aElement.getFirstChild().getNodeValue();
		}
	}

	// End EPPService.decode(Element)

	/**
	 * implements a deep <code>EPPService</code> compare.
	 *
	 * @param aObject <code>EPPService</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPService)) {
			return false;
		}

		EPPService theService = (EPPService) aObject;

		if (
			!(
					(namespaceURI == null) ? (theService.namespaceURI == null)
											   : namespaceURI.equals(theService.namespaceURI)
				)) {
			return false;
		}

		return true;
	}

	// End EPPService.equals(Object)

	/**
	 * Clone <code>EPPService</code>.
	 *
	 * @return clone of <code>EPPService</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPService clone = null;

		clone = (EPPService) super.clone();

		return clone;
	}

	// End EPPService.clone()

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

	// End EPPService.toString()

	/**
	 * validate the state of the <code>EPPService</code> instance.  A valid
	 * state means that all of the required attributes are set and have valid
	 * values.     If <code>validateState</code> returns without an exception,
	 * the state is valid.     If the state is not valid, an
	 * <code>EPPCodecException</code> is thrown, which contains     a
	 * description of the attribute error.
	 *
	 * @throws EPPCodecException State error with a description of the
	 * 		   attribute error.
	 */
	private void validateState() throws EPPCodecException {
		if (namespaceURI == null) {
			throw new EPPCodecException("Required attribute \"namespaceURI\" is null");
		}
	}

	// End EPPService.validateState()
}
