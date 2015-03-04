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
package com.verisign.epp.codec.emailFwd;

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
 * Represents an EPP EmailFwd &lt;emailFwd:renData&gt; response to a <code>
 * EPPEmailFwdRenewCmd</code>. When a &lt;renew&gt; command has been processed
 * successfully, the EPP &lt;resData&gt; element MUST contain a child
 * &ltemailFwd:renData&gt; element that identifies the emailFwd namespace and
 * the location of the emailFwd schema.  The &lt;emailFwd:name&gt; element
 * contains the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name that has been created or whose validity period has been extended. Use
 * <code>getName</code> and <code>setName</code>     to get and set the
 * element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:exDate&gt; element that contains the end of the
 * emailFwd's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewCmd
 */
public class EPPEmailFwdRenewResp extends EPPResponse {
	/** XML Element Name of <code>EPPEmailFwdRenewResp</code> root element. */
	final static String ELM_NAME = "emailFwd:renData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** XML Element Name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "emailFwd:exDate";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_ROID = "emailFwd:roid";

	/**
	 * fully qualified emailFwd name that has been created or whose validity
	 * period has been extended.
	 */
	private String name = null;

	/** the end of the emailFwd's validity period. */
	private Date expirationDate = null;

	/** DOCUMENT ME! */
	private boolean cmdType = false;

	/**
	 * <code>EPPEmailFwdRenewResp</code> default constructor.  Must call
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
	public EPPEmailFwdRenewResp() {
	}

	// End EPPEmailFwdRenewResp()

	/**
	 * <code>EPPEmailFwdRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName EmailFwd name
	 */
	public EPPEmailFwdRenewResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPEmailFwdRenewResp.EPPEmailFwdRenewResp(EPPTransId, String)

	/**
	 * <code>EPPEmailFwdRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName emailFwd name
	 * @param aExpirationDate expiration date of the emailFwd
	 */
	public EPPEmailFwdRenewResp(
								EPPTransId aTransId, String aName,
								Date aExpirationDate) {
		super(aTransId);

		name			   = aName;
		expirationDate     = aExpirationDate;
	}

	// End EPPEmailFwdRenewResp.EPPEmailFwdRenewResp(EPPTransId, String, String, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPEmailFwdRenewResp</code>.
	 *
	 * @return EPPEmailFwdRenewResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPEmailFwdRenewResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdRenewResp</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdRenewResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPEmailFwdRenewResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdRenewResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdRenewResp theComp = (EPPEmailFwdRenewResp) aObject;

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

	// End EPPEmailFwdRenewResp.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdRenewResp</code>.
	 *
	 * @return clone of <code>EPPEmailFwdRenewResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdRenewResp clone = (EPPEmailFwdRenewResp) super.clone();

		return clone;
	}

	// End EPPEmailFwdRenewResp.clone()

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

	// End EPPEmailFwdRenewResp.toString()

	/**
	 * Gets the emailFwd name
	 *
	 * @return EmailFwd Name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdRenewResp.getName()

	/**
	 * Sets the emailFwd name.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdRenewResp.setName(String)

	/**
	 * Gets the expiration date and time of the emailFwd.
	 *
	 * @return Expiration date and time of the emailFwd if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPEmailFwdRenewResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the emailFwd.
	 *
	 * @param aExpirationDate Expiration date and time of the emailFwd.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPEmailFwdRenewResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPEmailFwdRenewResp</code> instance.  A
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

	// End EPPEmailFwdRenewResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdRenewResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPEmailFwdRenewResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdRenewResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPEmailFwdRenewResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPEmailFwdMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPEmailFwdRenewResp.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdRenewResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdRenewResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPEmailFwdMapFactory.NS,
									  ELM_EXPIRATION_DATE);

		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPEmailFwdRenewResp.decode: "
										 + e);
		}
	}

	// End EPPEmailFwdRenewResp.doDecode(Element)
}
