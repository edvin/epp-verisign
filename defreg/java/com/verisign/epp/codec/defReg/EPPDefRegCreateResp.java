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
package com.verisign.epp.codec.defReg;

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
 * Represents an EPPDefReg&lt;defReg:creData&gt; response to a
 * <code>EPPDefRegCreateCmd</code>.     When a &lt;crate&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUSt contain a
 * child &lt;defReg:creData&gt; element that identifies the defReg namespace
 * and the location of the defReg schema.  The &lt;defReg:creData&gt; element
 * contains the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:name&gt; element that contains the fully qualified defReg name
 * that has been created or whose validity period has been extended. Use
 * <code>getName</code> and <code>setName</code>     to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;defReg.roid&gt; element that contains the roid of defReg object
 * creation.  Use <code>getRoid</code> and <code>setRoid</code> to get and set
 * the element.
 * </li>
 * <li>
 * A &lt;defReg.crDate&gt; element that contains the date and time of defReg
 * object creation.  Use <code>getCreationDate</code> and
 * <code>setCreationDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:exDate&gt; element that contains the end of the
 * defReg's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegCreateCmd
 */
public class EPPDefRegCreateResp extends EPPResponse {
	/** XML Element Name of <code>EPPDefRegCreateResp</code> root element. */
	final static String ELM_NAME = "defReg:creData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DEFREG_NAME = "defReg:name";

	/** XML Element Name for the <code>roid</code> attribute. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "defReg:crDate";

	/** XML Element Name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "defReg:exDate";

	/**
	 * fully qualified defReg name that has been created or whose validity
	 * period has been extended.
	 */
	private EPPDefRegName name = null;

	/**
	 * fully qualified defReg roid that has been created or whose validity
	 * period has been extended.
	 */
	private String roid = null;

	/** the end of the defReg's validity period. */
	private Date expirationDate = null;

	/** Creation Date. */
	private java.util.Date creationDate = null;

	/**
	 * <code>EPPDefRegCreateResp</code> default constructor.  Must call
	 * required setter methods before encode.     the defaults include the
	 * following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * roid is set to <code>null</code>
	 * </li>
	 * <li>
	 * expiration date is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>. The
	 * roid must be set before invoking <code>encode</code>.
	 */
	public EPPDefRegCreateResp() {
	}

	// End EPPDefRegCreateResp()

	/**
	 * <code>EPPDefRegCreateResp</code> constructor that takes the required
	 * attribute values as parameters. <br>
	 * The creation date must be set before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName DefReg name
	 * @param aRoid DefReg roid
	 */
	public EPPDefRegCreateResp(
							   EPPTransId aTransId, EPPDefRegName aName,
							   String aRoid) {
		super(aTransId);

		name     = aName;
		roid     = aRoid;
	}

	// End EPPDefRegCreateResp.EPPDefRegCreateResp(EPPTransId, String,String)

	/**
	 * <code>EPPDefRegCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName defReg name
	 * @param aRoid defReg roid
	 * @param aCreationDate creation date of the defReg
	 */
	public EPPDefRegCreateResp(
							   EPPTransId aTransId, EPPDefRegName aName,
							   String aRoid, Date aCreationDate) {
		super(aTransId);

		name			 = aName;
		roid			 = aRoid;
		creationDate     = aCreationDate;
	}

	// End EPPDefRegCreateResp.EPPDefRegCreateResp(EPPTransId, String, String, Date)

	/**
	 * <code>EPPDefRegCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName defReg name
	 * @param aRoid defReg roid
	 * @param aCreationDate creation date of the defReg
	 * @param aExpirationDate expiration date of the defReg
	 */
	public EPPDefRegCreateResp(
							   EPPTransId aTransId, EPPDefRegName aName,
							   String aRoid, Date aCreationDate,
							   Date aExpirationDate) {
		super(aTransId);
		name			   = aName;
		roid			   = aRoid;
		creationDate	   = aCreationDate;
		expirationDate     = aExpirationDate;
	}

	// End EPPDefRegCreateResp.EPPDefRegCreateResp(EPPTransId, String, String, Date, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPDefRegCreateResp</code>.
	 *
	 * @return EPPDefRegCreateResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDefRegCreateResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegCreateResp</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegCreateResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDefRegCreateResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegCreateResp theComp = (EPPDefRegCreateResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
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

		return true;
	}

	// End EPPDefRegCreateResp.equals(Object)

	/**
	 * Clone <code>EPPDefRegCreateResp</code>.
	 *
	 * @return clone of <code>EPPDefRegCreateResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegCreateResp clone = (EPPDefRegCreateResp) super.clone();

		return clone;
	}

	// End EPPDefRegCreateResp.clone()

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

	// End EPPDefRegCreateResp.toString()

	/**
	 * Gets the defReg name
	 *
	 * @return DefReg Name <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public EPPDefRegName getName() {
		return name;
	}

	// End EPPDefRegCreateResp.getName()

	/**
	 * Sets the defReg name.
	 *
	 * @param aName DefReg Name
	 */
	public void setName(EPPDefRegName aName) {
		name = aName;
	}

	// End EPPDefRegCreateResp.setName(String)

	/**
	 * Gets the defReg roid
	 *
	 * @return DefReg roid <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegCreateResp.getRoid()

	/**
	 * Sets the defReg roid.
	 *
	 * @param aRoid DefReg Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegCreateResp.setRoid(String)

	/**
	 * Get creation date.
	 *
	 * @return Creation date and time of the defReg
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// End EPPDefRegCreateResp.getCreationDate()

	/**
	 * Set creation date.
	 *
	 * @param newCrDate Creation date and time of the defReg
	 */
	public void setCreationDate(Date newCrDate) {
		creationDate = newCrDate;
	}

	// End EPPDefRegCreateResp.setCreationDate(Date)

	/**
	 * Gets the expiration date and time of the defReg.
	 *
	 * @return Expiration date and time of the defReg if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDefRegCreateResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the defReg.
	 *
	 * @param aExpirationDate Expiration date and time of the defReg.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDefRegCreateResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPDefRegCreateResp</code> instance.  A
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

		if (roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}

		if (creationDate == null) {
			throw new EPPCodecException("required attribute creationDate is not set");
		}
	}

	// End EPPDefRegCreateResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegCreateResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDefRegCreateResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegCreateResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPDefRegCreateResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// ROID
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_ROID);

		//Name
		EPPUtil.encodeComp(aDocument, root, name);

		// Creation Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, creationDate,
								  EPPDefRegMapFactory.NS, ELM_CREATION_DATE);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDefRegMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPDefRegCreateResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegCreateResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegCreateResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

		// Name
		name =
			(EPPDefRegName) EPPUtil.decodeComp(
											   aElement, EPPDefRegMapFactory.NS,
											   ELM_DEFREG_NAME,
											   EPPDefRegName.class);

		// Creation Date
		creationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_CREATION_DATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_EXPIRATION_DATE);
	}

	// End EPPDefRegCreateResp.doDecode(Element)
}
