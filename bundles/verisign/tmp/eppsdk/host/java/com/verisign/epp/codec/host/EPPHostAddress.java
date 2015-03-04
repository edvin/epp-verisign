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
package com.verisign.epp.codec.host;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
// W3C Imports
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a host address specified in an <code>EPPHostCreateCmd</code> or
 * in an <code>EPPHostAddRemove</code> object of an
 * <code>EPPHostUpdateCmd</code>.  An address     consists of a name and a
 * type, where type is either     <code>EPPHostAddress.IPV4</code> or
 * <code>EPPHostAddress.IPV6</code>.     The default type is
 * <code>EPPHostAddress.IPV6</code>.       <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 *
 * @see com.verisign.epp.codec.host.EPPHostCreateCmd
 * @see com.verisign.epp.codec.host.EPPHostUpdateCmd
 * @see com.verisign.epp.codec.host.EPPHostAddRemove
 */
public class EPPHostAddress implements EPPCodecComponent {
	/** IPV4 IP address constant.  This is the default type. */
	public final static short IPV4 = 0;

	/** IPV6 IP address constant. */
	public final static short IPV6 = 1;

	/** XML Element Name of <code>EPPHostAddress</code> root element. */
	final static String ELM_NAME = "host:addr";
	
	/** XML Element Local Name of <code>EPPHostAddress</code> root element. */
	final static String ELM_LOCALNAME = "addr";

	/** XML Element Name of <code>EPPHostAddress</code> root element. */
	private final static String ELM_ADDRESS_IP = "ip";

	/** IPV4 IP address constant. */
	private final static String ATTR_IPV4 = "v4";

	/** IPV6 IP address constant. */
	public final static String ATTR_IPV6 = "v6";

	/** IP address name */
	private String name = null;

	/** Type of the IP address (<code>IPV4</code> or <code>IPV6</code> */
	private short type = IPV4;

	/**
	 * Root fully qualified tag name
	 */
	private String rootName = ELM_NAME;
	
	/**
	 * XML namespace URI for the root element.
	 */
	private String rootNS = EPPHostMapFactory.NS;

	/**
	 * Default constructor for <code>EPPHostAddress</code>.  The name attribute
	 * defaults to <code>null</code> and must be set using
	 * <code>setName</code>     before invoking <code>encode</code>.  The type
	 * defaults to IPV4.
	 */
	public EPPHostAddress() {
		name     = null;
		type     = IPV4;
	}

	/**
	 * Constructor for <code>EPPHostAddress</code> that the takes the string
	 * name of the IP address with the type set to <code>IPV4</code>.
	 *
	 * @param aName IP address name.
	 */
	public EPPHostAddress(String aName) {
		this.name     = aName;
		this.type     = IPV4;
	}


	/**
	 * Constructor for <code>EPPHostAddress</code> that the takes the string
	 * name of the IP address along with the type of the IP address using
	 * either the constant <code>EPPHostAddress.IPV4</code> or
	 * <code>EPPHostAddress.IPV6</code>.
	 *
	 * @param aName IP address name.
	 * @param aType <code>EPPHostAddress.IPV4</code> or
	 * 		  <code>EPPHostAddress.IPV6</code> constant.
	 */
	public EPPHostAddress(String aName, short aType) {
		this.name     = aName;
		this.type     = aType;
	}


	/**
	 * Constructor for <code>EPPHostAddress</code> that the takes the string
	 * name of an IPV4 IP address and the root tag to use.  This constructor
	 * is useful for other mappings like domain.
	 *
	 * @param aRootNS Root element namespace URI
	 * @param aRootName Root tag for the element
	 * @param aName IP address name in IPV4 format.
	 */
	public EPPHostAddress(String aRootNS, String aRootName, String aName) {
		
		this.setRootName(aRootNS, aRootName);
		this.name		  = aName;
	}

	// EPPHostAddress.EPPHostAddress(String)

	/**
	 * Constructor for <code>EPPHostAddress</code> that the takes the string
	 * name of an IPV4 or IPV6 IP address and the root tag to use.  This
	 * constructor  is useful for other mappings like domain.
	 *
	 * @param aRootNS Root element namespace URI
	 * @param aRootName Root tag for the element
	 * @param aName IP address name in IPV4 format.
	 * @param aType <code>EPPHostAddress.IPV4</code> or
	 * 		  <code>EPPHostAddress.IPV6</code> constant.
	 */
	public EPPHostAddress(String aRootNS, String aRootName, String aName, short aType) {
		this.setRootName(aRootNS, aRootName);
		this.name		  = aName;
		this.type		  = aType;
	}

	/**
	 * Gets the root element XML namespace URI. 
	 * 
	 * @return root element XML namespace URI
	 */
	public String getRootNS() {
		return this.rootNS;
	}
	
	
	/**
	 * Get root name such as domain.
	 *
	 * @return Root element tag to use
	 */
	public String getRootName() {
		return this.rootName;
	}

	/**
	 * Set root name such as domain.
	 *
	 * @param aRootNS Root element namespace URI
	 * @param aRootName Root element tag to use
	 */
	public void setRootName(String aRootNS, String aRootName) {
		this.rootNS = aRootNS;
		this.rootName = aRootName;
	}

	/**
	 * Gets the addresss name in the format specified by <code>getType</code>.
	 *
	 * @return Address name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the address name in the format specified by <code>setType</code>.
	 *
	 * @param aName address name
	 */
	public void setName(String aName) {
		name = aName;
	}


	/**
	 * Sets the address name and address type.  <code>aType</code> should be
	 * either     <code>EPPHostAddress.IPV4</code> or
	 * <code>EPPHostAddress.IPV6</code>, and     the address name should
	 * conform to the format of the type.
	 *
	 * @param aName address name
	 * @param aType address type ((<code>EPPHostAddress.IPV4</code> or
	 * 		  <code>EPPHostAddress.IPV6</code>)
	 */
	public void setName(String aName, short aType) {
		this.name     = aName;
		this.type     = aType;
	}


	/**
	 * Gets the type of the address name, which should be either the
	 * <code>EPPHostAddress.IPV4</code> or     the
	 * <code>EPPHostAddress.IPV6</code> constant.
	 *
	 * @return Type of the address (<code>EPPHostAddress.IPV4</code> or
	 * 		   <code>EPPHostAddress.IPV6</code>)
	 */
	public short getType() {
		return this.type;
	}


	/**
	 * Sets the type of the address name to either the
	 * <code>EPPHostAddress.IPV4</code> or     the
	 * <code>EPPHostAddress.IPV6</code> constant.
	 *
	 * @param aType <code>IPV4</code> or <code>IPV6</code>
	 */
	public void setType(short aType) {
		this.type = aType;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostAddress</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPHostAddress</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostAddress</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("required attribute name is not set");
		}

		Element root = aDocument.createElementNS(this.rootNS, this.rootName);

		// Create Element by address type (IPV4 or IPV6)		
		if (type == IPV4) {
			root.setAttribute(ELM_ADDRESS_IP, ATTR_IPV4);
		}
		else if (type == IPV6) {
			root.setAttribute(ELM_ADDRESS_IP, ATTR_IPV6);
		}
		else {
			throw new EPPEncodeException("Invalid host address type of " + type);
		}

		// Add address name as text node to address type Element
		root.appendChild(aDocument.createTextNode(name));

		return root;
	}


	/**
	 * Decode the <code>EPPHostAddress</code> attributes from the aElement DOM
	 * Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostAddress</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// root name
		this.rootName     = aElement.getTagName();
		
		// root namspace URI
		this.rootNS = aElement.getNamespaceURI();

		// name	
		this.name = aElement.getFirstChild().getNodeValue();

		// type
		String typeStr = aElement.getAttribute(ELM_ADDRESS_IP);

		if ((typeStr == null) || (typeStr.equals(ATTR_IPV4))) {
			this.type = IPV4;
		}
		else if (typeStr.equals(ATTR_IPV6)) {
			this.type = IPV6;
		}
		else {
			throw new EPPDecodeException("Invalid host address type of "
										 + typeStr);
		}
	}


	/**
	 * implements a deep <code>EPPHostAddress</code> compare.
	 *
	 * @param aObject <code>EPPHostAddress</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostAddress)) {
			return false;
		}

		EPPHostAddress theComp = (EPPHostAddress) aObject;

		// Root name
		if (!EPPUtil.getLocalName(this.rootName).equals(EPPUtil.getLocalName(theComp.rootName))) {
			return false;
		}
		
		// Root NS
		if (!this.rootNS.equals(theComp.rootNS)) {
			return false;
		}

		// Type
		if (this.type != theComp.type) {
			return false;
		}

		// Name
		if (
			!(
					(this.name == null) ? (theComp.name == null)
											: this.name.equals(theComp.name)
				)) {
			return false;
		}

		return true;
	}


	/**
	 * Clone <code>EPPHostAddress</code>.
	 *
	 * @return clone of <code>EPPHostAddress</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostAddress clone = null;

		clone = (EPPHostAddress) super.clone();

		return clone;
	}


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

}
