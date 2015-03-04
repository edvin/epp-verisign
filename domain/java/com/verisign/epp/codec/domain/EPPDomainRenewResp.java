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
 * Represents an EPP Domain &lt;domain:renData&gt; response to a <code>
 * EPPDomainRenewCmd</code>. When a &lt;renew&gt; command has been processed
 * successfully, the EPP &lt;resData&gt; element MUST contain a child
 * &ltdomain:renData&gt; element that identifies the domain namespace and the
 * location of the domain schema.  The &lt;domain:name&gt; element contains
 * the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * that has been created or whose validity period has been extended. Use
 * <code>getName</code> and <code>setName</code>     to get and set the
 * element.
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
 * @see com.verisign.epp.codec.domain.EPPDomainRenewCmd
 */
public class EPPDomainRenewResp extends EPPResponse {
	/** XML Element Name of <code>EPPDomainRenewResp</code> root element. */
	final static String ELM_NAME = "domain:renData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

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

	/** DOCUMENT ME! */
	private boolean cmdType = false;

	/**
	 * <code>EPPDomainRenewResp</code> default constructor.  Must call required
	 * setter methods before encode.     the defaults include the following:
	 * <br><br>
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
	public EPPDomainRenewResp() {
	}

	// End EPPDomainRenewResp()

	/**
	 * <code>EPPDomainRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName Domain name
	 */
	public EPPDomainRenewResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPDomainRenewResp.EPPDomainRenewResp(EPPTransId, String)

	/**
	 * <code>EPPDomainRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName domain name
	 * @param aExpirationDate expiration date of the domain
	 */
	public EPPDomainRenewResp(
							  EPPTransId aTransId, String aName,
							  Date aExpirationDate) {
		super(aTransId);

		name			   = aName;
		expirationDate     = aExpirationDate;
	}

	// End EPPDomainRenewResp.EPPDomainRenewResp(EPPTransId, String, String, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPDomainRenewResp</code>.
	 *
	 * @return EPPDomainRenewResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDomainRenewResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainRenewResp</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainRenewResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDomainRenewResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainRenewResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainRenewResp theComp = (EPPDomainRenewResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
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

	// End EPPDomainRenewResp.equals(Object)

	/**
	 * Clone <code>EPPDomainRenewResp</code>.
	 *
	 * @return clone of <code>EPPDomainRenewResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainRenewResp clone = (EPPDomainRenewResp) super.clone();

		return clone;
	}

	// End EPPDomainRenewResp.clone()

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

	// End EPPDomainRenewResp.toString()

	/**
	 * Gets the domain name
	 *
	 * @return Domain Name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainRenewResp.getName()

	/**
	 * Sets the domain name.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainRenewResp.setName(String)

	/**
	 * Gets the expiration date and time of the domain.
	 *
	 * @return Expiration date and time of the domain if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDomainRenewResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the domain.
	 *
	 * @param aExpirationDate Expiration date and time of the domain.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDomainRenewResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPDomainRenewResp</code> instance.  A
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
	}

	// End EPPDomainRenewResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainRenewResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDomainRenewResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainRenewResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPDomainRenewResp.encode: "
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

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDomainMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPDomainRenewResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainRenewResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainRenewResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

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
			throw new EPPDecodeException("Invalid state on EPPDomainRenewResp.decode: "
										 + e);
		}
	}

	// End EPPDomainRenewResp.doDecode(Element)
}
