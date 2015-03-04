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
package com.verisign.epp.codec.nameWatch;

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
 * Represents an EPP NameWatch &lt;nameWatch:creData&gt; response to a
 * <code>EPPNameWatchCreateCmd</code>.     When a &lt;crate&gt; command has
 * been processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;nameWatch:creData&gt; element that identifies the NameWatch
 * namespace and the location of the nameWatch schema.  The
 * &lt;nameWatch:creData&gt; element contains the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:name&gt; element that contains the fully qualified nameWatch
 * name that has been created or whose validity period has been extended. Use
 * <code>getName</code> and <code>setName</code>     to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the roid of NameWatch object.
 * Use <code>getRoid</code> and <code>setRoid</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;nameWatch.crDate&gt; element that contains the date and time of
 * NameWatch object creation.  Use <code>getCreationDate</code> and
 * <code>setCreationDate</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:exDate&gt; element that contains the end of the
 * NameWatch's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchCreateCmd
 */
public class EPPNameWatchCreateResp extends EPPResponse {
	/** XML Element Name of <code>EPPNameWatchCreateResp</code> root element. */
	final static String ELM_NAME = "nameWatch:creData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_NAMEWATCH_NAME = "nameWatch:name";

	/** XML Element Name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "nameWatch:crDate";

	/** XML Element Name for the <code>exDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "nameWatch:exDate";

	/**
	 * fully qualified nameWatch name that has been created or whose validity
	 * period has been extended.
	 */
	private String name = null;

	/** roid */
	private String roid = null;

	/** Expiration Date. */
	private java.util.Date expirationDate = null;

	/** Creation Date. */
	private java.util.Date creationDate = null;

	/**
	 * <code>EPPNameWatchCreateResp</code> default constructor.  Must call
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
	 * creation date is set to <code>null</code>
	 * </li>
	 * <li>
	 * expiration date is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name, roid, and creation date must be set before invoking
	 * <code>encode</code>.
	 */
	public EPPNameWatchCreateResp() {
	}

	// End EPPNameWatchCreateResp()

	/**
	 * <code>EPPNameWatchCreateResp</code> constructor that takes the required
	 * attribute values as parameters. <br>
	 * The roid and creation date must be set before invoking
	 * <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName NameWatch name
	 */
	public EPPNameWatchCreateResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPNameWatchCreateResp.EPPNameWatchCreateResp(EPPTransId, String)

	/**
	 * <code>EPPNameWatchCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName NameWatch name
	 * @param aRoid NameWatch roid
	 * @param aCreationDate creation date of the NameWatch
	 */
	public EPPNameWatchCreateResp(
								  EPPTransId aTransId, String aName,
								  String aRoid, Date aCreationDate) {
		super(aTransId);

		name			 = aName;
		roid			 = aRoid;
		creationDate     = aCreationDate;
	}

	// End EPPNameWatchCreateResp.EPPNameWatchCreateResp(EPPTransId, String, String, Date)

	/**
	 * <code>EPPNameWatchCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aName NameWatch name
	 * @param aRoid NameWatch roid
	 * @param aCreationDate creation date of the NameWatch
	 * @param aExpirationDate expiration date of the NameWatch
	 */
	public EPPNameWatchCreateResp(
								  EPPTransId aTransId, String aName,
								  String aRoid, Date aCreationDate,
								  Date aExpirationDate) {
		super(aTransId);

		name			   = aName;
		roid			   = aRoid;
		creationDate	   = aCreationDate;
		expirationDate     = aExpirationDate;
	}

	// End EPPNameWatchCreateResp.EPPNameWatchCreateResp(EPPTransId, String, String, Date, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPNameWatchCreateResp</code>.
	 *
	 * @return EPPNameWatchCreateResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPNameWatchCreateResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchCreateResp</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchCreateResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPNameWatchCreateResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchCreateResp theComp = (EPPNameWatchCreateResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// roid
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

	// End EPPNameWatchCreateResp.equals(Object)

	/**
	 * Clone <code>EPPNameWatchCreateResp</code>.
	 *
	 * @return clone of <code>EPPNameWatchCreateResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchCreateResp clone = (EPPNameWatchCreateResp) super.clone();

		return clone;
	}

	// End EPPNameWatchCreateResp.clone()

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

	// End EPPNameWatchCreateResp.toString()

	/**
	 * Gets the nameWatch name
	 *
	 * @return NameWatch Name
	 */
	public String getName() {
		return name;
	}

	// End EPPNameWatchCreateResp.getName()

	/**
	 * Sets the nameWatch name.
	 *
	 * @param aName NameWatch Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPNameWatchCreateResp.setName(String)

	/**
	 * Gets the nameWatch roid
	 *
	 * @return NameWatch roid.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchCreateResp.getRoid()

	/**
	 * Sets the nameWatch roid.
	 *
	 * @param aRoid NameWatch Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchCreateResp.setRoid(String)

	/**
	 * Get creation date.
	 *
	 * @return Creation date and time of the nameWatch
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// End EPPNameWatchCreateResp.getCreationDate()

	/**
	 * Set creation date.
	 *
	 * @param newCrDate Creation date and time of the nameWatch
	 */
	public void setCreationDate(Date newCrDate) {
		creationDate = newCrDate;
	}

	// End EPPNameWatchCreateResp.setCreationDate(Date)

	/**
	 * Gets the expiration date and time of the nameWatch.
	 *
	 * @return Expiration date and time of the nameWatch if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPNameWatchCreateResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the nameWatch.
	 *
	 * @param aExpirationDate Expiration date and time of the nameWatch.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPNameWatchCreateResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPNameWatchCreateResp</code> instance.
	 * A valid state means that all of the required attributes have been set.
	 * If validateState     returns without an exception, the state is valid.
	 * If the state is not     valid, the EPPCodecException will contain a
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
			throw new EPPCodecException("crDate required attribute is not set");
		}
	}

	// End EPPNameWatchCreateResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchCreateResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPNameWatchCreateResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchCreateResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPNameWatchCreateResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPNameWatchMapFactory.NS,
							 ELM_NAMEWATCH_NAME);

		// Creation Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, creationDate,
								  EPPNameWatchMapFactory.NS, ELM_CREATION_DATE);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPNameWatchMapFactory.NS,
									  ELM_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPNameWatchCreateResp.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchCreateResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchCreateResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_NAMEWATCH_NAME);

		// Creation Date
		creationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_CREATION_DATE);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPNameWatchMapFactory.NS,
									  ELM_EXPIRATION_DATE);

		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPNameWatchCreateResp.decode: "
										 + e);
		}
	}

	// End EPPNameWatchCreateResp.doDecode(Element)
}
