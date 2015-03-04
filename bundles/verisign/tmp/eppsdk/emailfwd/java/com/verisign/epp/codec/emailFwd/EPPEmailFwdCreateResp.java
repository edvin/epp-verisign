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
 * Represents an EPP EmailFwd &lt;emailFwd:creData&gt; response to a
 * <code>EPPEmailFwdCreateCmd</code>.     When a &lt;crate&gt; command has
 * been processed successfully, the EPP &lt;resData&gt; element MUSt contain a
 * child &lt;emailFwd:creData&gt; element that identifies the emailFwd
 * namespace and the location of the emailFwd schema.  The
 * &lt;emailFwd:creData&gt; element contains the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name that has been created or whose validity period has been extended. Use
 * <code>getName</code> and <code>setName</code>     to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;emailFwd.crDate&gt; element that contains the date and time of
 * emailFwd object creation.  Use <code>getCreationDate</code> and
 * <code>setCreationDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:exDate&gt; element that contains the end of the
 * emailFwd's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateCmd
 */
public class EPPEmailFwdCreateResp extends EPPResponse {
	/** XML Element Name of <code>EPPEmailFwdCreateResp</code> root element. */
	final static String ELM_NAME = "emailFwd:creData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "emailFwd:crDate";

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

	/** Creation Date. */
	private java.util.Date creationDate = null;

	/**
	 * <code>EPPEmailFwdCreateResp</code> default constructor.  Must call
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
	public EPPEmailFwdCreateResp() {
	}

	// End EPPEmailFwdCreateResp()

	/**
	 * <code>EPPEmailFwdCreateResp</code> constructor that takes the required
	 * attribute values as parameters. <br>
	 * The creation date must be set before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName EmailFwd name
	 */
	public EPPEmailFwdCreateResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPEmailFwdCreateResp.EPPEmailFwdCreateResp(EPPTransId, String)

	/**
	 * <code>EPPEmailFwdCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName emailFwd name
	 * @param aCreationDate creation date of the emailFwd
	 */
	public EPPEmailFwdCreateResp(
								 EPPTransId aTransId, String aName,
								 Date aCreationDate) {
		super(aTransId);

		name			 = aName;
		creationDate     = aCreationDate;
	}

	// End EPPEmailFwdCreateResp.EPPEmailFwdCreateResp(EPPTransId, String, String, Date)

	/**
	 * <code>EPPEmailFwdCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName emailFwd name
	 * @param aCreationDate creation date of the emailFwd
	 * @param aExpirationDate expiration date of the emailFwd
	 */
	public EPPEmailFwdCreateResp(
								 EPPTransId aTransId, String aName,
								 Date aCreationDate, Date aExpirationDate) {
		super(aTransId);

		name			   = aName;
		creationDate	   = aCreationDate;
		expirationDate     = aExpirationDate;
	}

	// End EPPEmailFwdCreateResp.EPPEmailFwdCreateResp(EPPTransId, String, String, Date, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPEmailFwdCreateResp</code>.
	 *
	 * @return EPPEmailFwdCreateResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPEmailFwdCreateResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdCreateResp</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdCreateResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPEmailFwdCreateResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdCreateResp theComp = (EPPEmailFwdCreateResp) aObject;

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

	// End EPPEmailFwdCreateResp.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdCreateResp</code>.
	 *
	 * @return clone of <code>EPPEmailFwdCreateResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdCreateResp clone = (EPPEmailFwdCreateResp) super.clone();

		return clone;
	}

	// End EPPEmailFwdCreateResp.clone()

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

	// End EPPEmailFwdCreateResp.toString()

	/**
	 * Gets the emailFwd name
	 *
	 * @return EmailFwd Name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdCreateResp.getName()

	/**
	 * Sets the emailFwd name.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdCreateResp.setName(String)

	/**
	 * Get creation date.
	 *
	 * @return Creation date and time of the emailFwd
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// End EPPEmailFwdCreateResp.getCreationDate()

	/**
	 * Set creation date.
	 *
	 * @param newCrDate Creation date and time of the emailFwd
	 */
	public void setCreationDate(Date newCrDate) {
		creationDate = newCrDate;
	}

	// End EPPEmailFwdCreateResp.setCreationDate(Date)

	/**
	 * Gets the expiration date and time of the emailFwd.
	 *
	 * @return Expiration date and time of the emailFwd if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPEmailFwdCreateResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the emailFwd.
	 *
	 * @param aExpirationDate Expiration date and time of the emailFwd.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPEmailFwdCreateResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPEmailFwdCreateResp</code> instance. A
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

	// End EPPEmailFwdCreateResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdCreateResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPEmailFwdCreateResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdCreateResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPEmailFwdCreateResp.encode: "
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

		// Creation Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, creationDate,
								  EPPEmailFwdMapFactory.NS, ELM_CREATION_DATE);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPEmailFwdMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPEmailFwdCreateResp.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdCreateResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdCreateResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		// Creation Date
		creationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPEmailFwdMapFactory.NS,
									  ELM_CREATION_DATE);

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
			throw new EPPDecodeException("Invalid state on EPPEmailFwdCreateResp.decode: "
										 + e);
		}
	}

	// End EPPEmailFwdCreateResp.doDecode(Element)
}
