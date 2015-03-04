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
package com.verisign.epp.codec.domain;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Domain &lt;domain:creData&gt; response to a
 * <code>EPPDomainCreateCmd</code>.     When a &lt;crate&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUSt contain a
 * child &lt;domain:creData&gt; element that identifies the domain namespace
 * and the location of the domain schema.  The &lt;domain:creData&gt; element
 * contains the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * that has been created or whose validity period has been extended. Use
 * <code>getName</code> and <code>setName</code>     to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;domain.crDate&gt; element that contains the date and time of domain
 * object creation.  Use <code>getCreationDate</code> and
 * <code>setCreationDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:exDate&gt; element that contains the end of the
 * domain's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainCreateCmd
 */
public class EPPDomainCreateResp extends EPPResponse {
	/** XML Element Name of <code>EPPDomainCreateResp</code> root element. */
	final static String ELM_NAME = "domain:creData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "domain:crDate";

	/** XML Element Name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "domain:exDate";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_ROID = "domain:roid";

	/**
	 * fully qualified domain name that has been created or whose validity
	 * period has been extended.
	 */
	private String name = null;

	/** the end of the domain's validity period. */
	private Date expirationDate = null;

	/** Creation Date. */
	private java.util.Date creationDate = null;

	/**
	 * <code>EPPDomainCreateResp</code> default constructor.  Must call
	 * required setter methods before encode.     the defaults include the
	 * following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * expiration date is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPDomainCreateResp() {
	}

	// End EPPDomainCreateResp()

	/**
	 * <code>EPPDomainCreateResp</code> constructor that takes the required
	 * attribute values as parameters. <br>
	 * The creation date must be set before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName Domain name
	 */
	public EPPDomainCreateResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPDomainCreateResp.EPPDomainCreateResp(EPPTransId, String)

	/**
	 * <code>EPPDomainCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName domain name
	 * @param aCreationDate creation date of the domain
	 */
	public EPPDomainCreateResp(
							   EPPTransId aTransId, String aName,
							   Date aCreationDate) {
		super(aTransId);

		name			 = aName;
		creationDate     = aCreationDate;
	}

	// End EPPDomainCreateResp.EPPDomainCreateResp(EPPTransId, String, String, Date)

	/**
	 * <code>EPPDomainCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName domain name
	 * @param aCreationDate creation date of the domain
	 * @param aExpirationDate expiration date of the domain
	 */
	public EPPDomainCreateResp(
							   EPPTransId aTransId, String aName,
							   Date aCreationDate, Date aExpirationDate) {
		super(aTransId);

		name			   = aName;
		creationDate	   = aCreationDate;
		expirationDate     = aExpirationDate;
	}

	// End EPPDomainCreateResp.EPPDomainCreateResp(EPPTransId, String, String, Date, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPDomainCreateResp</code>.
	 *
	 * @return EPPDomainCreateResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDomainCreateResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainCreateResp</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainCreateResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDomainCreateResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainCreateResp theComp = (EPPDomainCreateResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Creation Date
		if (
			!(
					(creationDate == null) ? (theComp.creationDate == null)
											   : creationDate.equals(theComp.creationDate)
				)) {
			return false;
		}

		// Expiration Date
		if (
			!(
					(expirationDate == null) ? (
													 theComp.expirationDate == null
												 )
												 : expirationDate.equals(theComp.expirationDate)
				)) {
			return false;
		}

		return true;
	}

	// End EPPDomainCreateResp.equals(Object)

	/**
	 * Clone <code>EPPDomainCreateResp</code>.
	 *
	 * @return clone of <code>EPPDomainCreateResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainCreateResp clone = (EPPDomainCreateResp) super.clone();

		return clone;
	}

	// End EPPDomainCreateResp.clone()

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

	// End EPPDomainCreateResp.toString()

	/**
	 * Gets the domain name
	 *
	 * @return Domain Name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainCreateResp.getName()

	/**
	 * Sets the domain name.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainCreateResp.setName(String)

	/**
	 * Get creation date.
	 *
	 * @return Creation date and time of the domain
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// End EPPDomainCreateResp.getCreationDate()

	/**
	 * Set creation date.
	 *
	 * @param newCrDate Creation date and time of the domain
	 */
	public void setCreationDate(Date newCrDate) {
		creationDate = newCrDate;
	}

	// End EPPDomainCreateResp.setCreationDate(Date)

	/**
	 * Gets the expiration date and time of the domain.
	 *
	 * @return Expiration date and time of the domain if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDomainCreateResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the domain.
	 *
	 * @param aExpirationDate Expiration date and time of the domain.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDomainCreateResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPDomainCreateResp</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the name of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (creationDate == null) {
			throw new EPPCodecException("required attribute creationDate is not set");
		}
	}

	// End EPPDomainCreateResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainCreateResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDomainCreateResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainCreateResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPDomainCreateResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);

		// Creation Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, creationDate,
								  EPPDomainMapFactory.NS, ELM_CREATION_DATE);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDomainMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPDomainCreateResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainCreateResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainCreateResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Creation Date
		creationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_CREATION_DATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDomainMapFactory.NS,
									  ELM_EXPIRATION_DATE);

		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPDomainCreateResp.decode: "
										 + e);
		}
	}

	// End EPPDomainCreateResp.doDecode(Element)
}
