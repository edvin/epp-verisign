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

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Host &lt;create&gt; command, which provides a transform
 * operation that allows a client to create a host object.     The
 * &lt;host:create&gt; element MUST contain the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains the fully qualified host name of
 * the object to be created.  Use <code>getName</code> and
 * <code>setName</code> to get and set the element.
 * </li>
 * <li>
 * Zero or more &lt;host:addr&gt; elements that contain the IP addresses to be
 * associated with the host.  If the host name exists in a name space for
 * which the server is not authoritative, then the superordinate domain of the
 * host MUST be known to the server before the host object can be created. For
 * example, if the server is authoritative for the ".com" name space and the
 * name of the     server is "ns1.example.com.au", the server is not required
 * to produce DNS glue records for the name server and IP addresses for the
 * server are not required by the DNS.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the transaction identifier associated with
 * successful creation of a host object becomes the authorization identifier
 * return in the &lt;info-data&gt; of a EPP Host &lt;info&gt;     response and
 * most likely will be required for future transform     operations.  A client
 * MUST retain all transaction identifiers associated with host object
 * creation and protect them from disclosure.  A client MUST also provide a
 * copy of the transaction identifier information to the host registrant, who
 * will need this information to request a host transfer through a different
 * client.     <br>
 * <br>
 * <code>EPPReponse</code> is the response associated     with
 * <code>EPPHostCreateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPHostCreateCmd extends EPPCreateCmd {
	/** XML Element Name of <code>EPPHostCreateCmd</code> root element. */
	final static String ELM_NAME = "host:create";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_HOST_NAME = "host:name";

	/** Host Name of host to create. */
	private String name = null;

	/** Host Name Addresses */
	private Vector addresses = null;

	/**
	 * Allocates a new <code>EPPHostCreateCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * addresses is set to to <code>null</code>
	 * </li>
	 * <li>
	 * transaction id is set to <code>null</code>.
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPHostCreateCmd() {
		name		  = null;
		addresses     = null;
	}

	// End EPPHostCreateCmd.EPPHostCreateCmd()

	/**
	 * Allocates a new <code>EPPHostCreateCmd</code> with a host name.     The
	 * other attributes are initialized as follows:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * addresses is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Host name
	 */
	public EPPHostCreateCmd(String aTransId, String aName) {
		super(aTransId);

		name		  = aName;
		addresses     = null;
	}

	// End EPPHostCreateCmd.EPPHostCreateCmd(String, String)

	/**
	 * Allocates a new <code>EPPHostCreateCmd</code> with all attributes
	 * specified     by the arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Host name
	 * @param someAddresses <code>Vector</code> of <code>EPPHostAddress</code>
	 * 		  instances.
	 */
	public EPPHostCreateCmd(
							String aTransId, String aName, Vector someAddresses) {
		super(aTransId);

		name		  = aName;
		addresses     = someAddresses;
	}

	// End EPPHostCreateCmd.EPPHostCreateCmd(String, String, Vector)

	/**
	 * Get the EPP command Namespace associated with EPPHostCreateCmd.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostCreateCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPHostCreateCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the <code>EPPCodecException</code> will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (name == null) {
			throw new EPPCodecException("required attribute name is not set");
		}
	}

	// End EPPHostCreateCmd.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the EPPHostCreateCmd
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPHostCreateCmd instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPHostCreateCmd
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPHostCreateCmd invalid state: " + e);
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPHostMapFactory.NS,
							 ELM_HOST_NAME);

		// Addresses
		EPPUtil.encodeCompVector(aDocument, root, addresses);

		return root;
	}

	// End EPPHostCreateCmd.doEncode(Document)

	/**
	 * Decode the EPPHostCreateCmd attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPHostCreateCmd from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Host Name
		name =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);

		// Addresses
		addresses =
			EPPUtil.decodeCompVector(
									 aElement, EPPHostMapFactory.NS,
									 EPPHostAddress.ELM_NAME,
									 EPPHostAddress.class);

		if (addresses.size() == 0) {
			addresses = null;
		}
	}

	// End EPPHostCreateCmd.doDecode(Element)

	/**
	 * Get the host name to create.
	 *
	 * @return Host Name
	 */
	public String getName() {
		return name;
	}

	// End EPPHostCreateCmd.getName()

	/**
	 * Set the host name to create.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostCreateCmd.setName(String)

	/**
	 * Gets the host addresses.
	 *
	 * @return Vector of <code>EPPHostAddress</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getAddresses() {
		return addresses;
	}

	// End EPPHostCreateCmd.getAddresses()

	/**
	 * Sets the host addresses.
	 *
	 * @param aAddresses Vector of <code>EPPHostAddress</code> instances.
	 */
	public void setAddresses(Vector aAddresses) {
		addresses = aAddresses;
	}

	// End EPPHostCreateCmd.setAddresses(Vector)

	/**
	 * Compare an instance of <code>EPPHostCreateCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostCreateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostCreateCmd theComp = (EPPHostCreateCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Addresses
		if (!EPPUtil.equalVectors(addresses, theComp.addresses)) {
			return false;
		}

		return true;
	}

	// End EPPHostCreateCmd.equals(Object)

	/**
	 * Clone <code>EPPHostCreateCmd</code>.
	 *
	 * @return clone of <code>EPPHostCreateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostCreateCmd clone = (EPPHostCreateCmd) super.clone();

		if (addresses != null) {
			clone.addresses = (Vector) addresses.clone();

			for (int i = 0; i < addresses.size(); i++)
				clone.addresses.setElementAt(
											 ((EPPHostAddress) addresses
											  .elementAt(i)).clone(), i);
		}

		return clone;
	}

	// End EPPHostCreateCmd.clone()

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

	// End EPPHostCreateCmd.toString()
}
