/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.codec.contact;

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
 * Represents a contact postal address definition that is used in
 * <code>EPPContactCreateCmd</code>, <code>EPPContactUpdateCmd</code>, and
 * <code>EPPContactInfoResp</code>.  The child elements associated with an
 * <code>EPPContactPostalDefinition</code> include:     <br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:name&gt; element that contains the name of the individual or
 * role represented by the contact. Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:org&gt; element that contains the name of the
 * organization with which the contact is affiliated. Use <code>getOrg</code>
 * and <code>setOrg</code>     to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:addr&gt; element that contains address information associated
 * with the contact. Use <code>getAddress</code> and <code>setAdress</code> to
 * get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPContactPostalDefinition implements EPPCodecComponent {
	/** XML tag name for the <code>org</code> attribute. */
	public final static String ELM_NAME_POSTAL_INFO = "contact:postalInfo";

	/** XML tag name for the <code>org</code> attribute. */
	private final static String ELM_ORG = "contact:org";

	/** XML tag name for the <code>org</code> attribute. */
	private final static String ELM_CONTACT_NAME = "contact:name";

	/** XML Attribute Name for postalInfo type. */
	private final static String ATTR_TYPE = "type";

	/** Value of the LOC in contact postal info type mapping */
	public final static java.lang.String ATTR_TYPE_LOC = "loc";

	/** Value of the INT in contact postal info type mapping */
	public final static java.lang.String ATTR_TYPE_INT = "int";

	/**
	 * XML root element tag name for contact postal definition The value needs
	 * to be set before calling encode(Document) and default value is set to
	 * <code>ELM_NAME_POSTAL_INFO</code>.
	 */
	private java.lang.String rootName = ELM_NAME_POSTAL_INFO;

	/**
	 * Attribute Name of <code>EPPContactPostalDefinition</code> root element.
	 */
	private java.lang.String type = ATTR_TYPE_INT;

	/** contact name */
	private String name = null;

	/** contact organization */
	private String org = null;

	/** contact address */
	private EPPContactAddress address = null;

	/**
	 * A flag to show whether validateState() been called before calling
	 * encode(Document)
	 */
	private boolean validatedFlag = true;

	/**
	 * <code>EPPContactPostalDefinition</code> default constructor.  Must call
	 * required setter methods before     invoking <code>encode</code>, which
	 * include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * id - <code>setId</code>
	 * </li>
	 * <li>
	 * name - <code>setName</code>
	 * </li>
	 * <li>
	 * type - <code>setType</code>
	 * </li>
	 * <li>
	 * address - <code>setAddress</code>
	 * </li>
	 * <li>
	 * voice - <code>setVoice</code>
	 * </li>
	 * <li>
	 * email - <code>setEmail</code>
	 * </li>
	 * </ul>
	 * 
	 * <br><br>     The following optional attributes can be set:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * org - <code>setOrg</code>
	 * </li>
	 * <li>
	 * fax    - <code>setFax</code>
	 * </li>
	 * </ul>
	 */
	public EPPContactPostalDefinition() {
		// Default values set in attribute definitions.
	}

	// End EPPContactPostalDefinition.EPPContactPostalDefinition()

	/**
	 * <code>EPPContactPostalDefinition</code> constructor that takes the
	 * contact address type as argument.
	 *
	 * @param aType address type which should be one of the
	 * 		  <code>ATTR_TYPE</code> constants.
	 */
	public EPPContactPostalDefinition(String aType) {
		type = aType;
	}

	/**
	 * <code>EPPContactPostalDefinition</code> constructor that sets the
	 * required     attributes with the parameters.  The following optional
	 * attribute can be set:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * org - <code>setOrg</code>
	 * </li>
	 * <li>
	 * fax    - <code>setFax</code>
	 * </li>
	 * <li>
	 * name - <code>setName</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aType DOCUMENT ME!
	 * @param aAddress contact address
	 */
	public EPPContactPostalDefinition(String aType, EPPContactAddress aAddress) {
		type	    = aType;
		address     = aAddress;
	}

	// End EPPContactPostalDefinition.EPPContactPostalDefinition(EPPContactAddress)

	/**
	 * <code>EPPContactPostalDefinition</code> constructor that sets the
	 * required     attributes with the parameters.  The following optional
	 * attribute can be set:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * org - <code>setOrg</code>
	 * </li>
	 * <li>
	 * fax    - <code>setFax</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aName contact name
	 * @param aType DOCUMENT ME!
	 * @param aAddress contact address
	 */
	public EPPContactPostalDefinition(
									  String aName, String aType,
									  EPPContactAddress aAddress) {
		name	    = aName;
		type	    = aType;
		address     = aAddress;
	}

	// End EPPContactPostalDefinition.EPPContactPostalDefinition(String, EPPContactAddress)

	/**
	 * <code>EPPContactPostalDefinition</code> constructor that sets all of the
	 * attribute     with the parameter values.
	 *
	 * @param aName contact name
	 * @param aOrg contact organization if defined; <code>null</code> otherwise
	 * @param aType DOCUMENT ME!
	 * @param aAddress contact address
	 */
	public EPPContactPostalDefinition(
									  String aName, String aOrg, String aType,
									  EPPContactAddress aAddress) {
		name	    = aName;
		org		    = aOrg;
		type	    = aType;
		address     = aAddress;
	}

	// End EPPContactPostalDefinition.EPPContactDPostalefinition(String, String, EPPContactAddress)

	/**
	 * Gets the contact organization
	 *
	 * @return Client organization if defined; <code>null</code> otherwise.
	 */
	public String getOrg() {
		return org;
	}

	// End EPPContactPostalDefinition.getOrg()

	/**
	 * Sets the contact organization
	 *
	 * @param aOrg Client organization
	 */
	public void setOrg(String aOrg) {
		org = aOrg;
	}

	// End EPPContactPostalDefinition.setOrg(String)

	/**
	 * Gets the contact address
	 *
	 * @return Contact address if defined; <code>null</code> otherwise.
	 */
	public EPPContactAddress getAddress() {
		return address;
	}

	// End EPPContactPostalDefinition.getAddress()

	/**
	 * Sets the contact address
	 *
	 * @param aAddress Contact address
	 */
	public void setAddress(EPPContactAddress aAddress) {
		address = aAddress;
	}

	// End EPPContactPostalDefinition.setAddress(EPPContactAddress)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactPostalDefinition</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return DOCUMENT ME!
	 *
	 * @exception EPPEncodeException
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// validate states if validatedFlag been set
		if (validatedFlag) {
			try {
				validateState();
			}
			 catch (EPPCodecException e) {
				throw new EPPEncodeException("Invalid state on EPPContactPostalDefination.encode: "
											 + e);
			}
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, rootName);

		// add attribute type
		root.setAttribute(ATTR_TYPE, type);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPContactMapFactory.NS,
							 ELM_CONTACT_NAME);

		// Organization
		EPPUtil.encodeString(
							 aDocument, root, org, EPPContactMapFactory.NS,
							 ELM_ORG);

		// Address //address element is optional
		if (address != null) {
			EPPUtil.encodeComp(aDocument, root, address);
		}

		return root;
	}

	// End EPPContactPostalDefinition.encode(Document)

	/**
	 * Decode the <code>EPPContactPostalDefinition</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactPostalDefinition</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_CONTACT_NAME);

		// Organization
		org     = EPPUtil.decodeString(
									   aElement, EPPContactMapFactory.NS,
									   ELM_ORG);

		//Type
		type     = aElement.getAttribute(ATTR_TYPE);

		// Address
		address =
			(EPPContactAddress) EPPUtil.decodeComp(
												   aElement,
												   EPPContactMapFactory.NS,
												   EPPContactAddress.ELM_NAME,
												   EPPContactAddress.class);
	}

	// End EPPContactPostalDefinition.decode(Element)

	/**
	 * implements a deep <code>EPPContactPostalDefinition</code> compare.
	 *
	 * @param aObject <code>EPPContactPostalDefinition</code> instance to
	 * 		  compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactPostalDefinition)) {
			return false;
		}

		EPPContactPostalDefinition theComp =
			(EPPContactPostalDefinition) aObject;

		// name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// org
		if (!((org == null) ? (theComp.org == null) : org.equals(theComp.org))) {
			return false;
		}

		// type
		if (!type.equals(theComp.type)) {
			return false;
		}

		// address
		if (
			!(
					(address == null) ? (theComp.address == null)
										  : address.equals(theComp.address)
				)) {
			return false;
		}

		return true;
	}

	// End EPPContactPostalDefinition.equals(Object)

	/**
	 * Clone <code>EPPContactPostalDefinition</code>.
	 *
	 * @return clone of <code>EPPContactPostalDefinition</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactPostalDefinition clone =
			(EPPContactPostalDefinition) super.clone();

		if (address != null) {
			clone.address = (EPPContactAddress) address.clone();
		}

		return clone;
	}

	// End EPPContactPostalDefinition.clone()

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

	// End EPPContactDefinition.toString()

	/**
	 * Validate the state of the <code>EPPContactPostalDefination</code>
	 * instance.  A valid state means that all of the required attributes have
	 * been set.  If validateState     returns without an exception, the state
	 * is valid.  If the state is not     valid, the EPPCodecException will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	protected void validateState() throws EPPCodecException {
		if (rootName == null) {
			throw new EPPCodecException("root element name is not set");
		}

		if (!rootName.equals(ELM_NAME_POSTAL_INFO)) {
			throw new EPPCodecException("root element name is not recognized");
		}

		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (type == null) {
			throw new EPPCodecException("required attribute type is not set");
		}

		//due to address element is optional i think we got to comment this$ashwin
		if (address == null) {
			throw new EPPCodecException("address required attribute is not set");
		}
	}

	// End EPPContactPostalDefination.validateState()

	/**
	 * Gets the contact name
	 *
	 * @return Contact Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPContactPostalDefinition.getName()

	/**
	 * Get root tag name for contact postal definition.
	 *
	 * @return String  root tag name
	 */
	public String getRootName() {
		return rootName;
	}

	// End EPPContactPostalDefinition.getRootName()

	/**
	 * Sets the contact name.
	 *
	 * @param aName Contact Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPContactPostalDefinition.setName(EPPContactName)

	/**
	 * Set root tag name for contact postal definition.
	 *
	 * @param newRootName String
	 */
	public void setRootName(String newRootName) {
		rootName = newRootName;
	}

	// End EPPContactPostalDefinition.setRootName(String)

	/**
	 * Show whether needs to call validateState()
	 *
	 * @return boolean
	 */
	public boolean isValidated() {
		return validatedFlag;
	}

	// End EPPContactPostalDefinition.isValidated()

	/**
	 * Set validated flag.
	 *
	 * @param newValidatedFlag boolean
	 */
	public void setValidatedFlag(boolean newValidatedFlag) {
		validatedFlag = newValidatedFlag;
	}

	// End EPPContactPostalDefinition.setValidatedFlag(boolean)

	/**
	 * Get contact address type.
	 *
	 * @return String  Contact type
	 */
	public String getType() {
		return type;
	}

	// End EPPContactPostalDefinition.getType()

	/**
	 * Set contact type.
	 *
	 * @param newType String
	 */
	public void setType(String newType) {
		type = newType;
	}

	// End EPPContactPostalDefinition.setType()	
}
