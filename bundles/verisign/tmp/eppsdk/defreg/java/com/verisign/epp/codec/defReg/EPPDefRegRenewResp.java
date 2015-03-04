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
 * Represents an EPP DefReg &lt;defReg:renData&gt; response to a <code>
 * EPPDefRegRenewCmd</code>. When a &lt;renew&gt; command has been processed
 * successfully, the EPP &lt;resData&gt; element MUST contain a child
 * &ltdefReg:renData&gt; element that identifies the defReg namespace and the
 * location of the defReg schema.  The &lt;defReg:name&gt; element contains
 * the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:roid&gt; element that contains the fully qualified defReg roid
 * that has been created or whose validity period has been extended. Use
 * <code>getRoid</code> and <code>setRoid</code>     to get and set the
 * element.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:exDate&gt; element that contains the end of the
 * defReg's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegRenewCmd
 */
public class EPPDefRegRenewResp extends EPPResponse {
	/** XML Element Name of <code>EPPDefRegRenewResp</code> root element. */
	final static String ELM_NAME = "defReg:renData";

	/** XML Element Name for the <code>expirationDate</code> attribute. */
	private final static String ELM_DEFREG_EXPIRATION_DATE = "defReg:exDate";

	/** XML Element Roid for the <code>roid</code> attribute. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** the end of the defReg's validity period. */
	private Date expirationDate = null;

	/** DOCUMENT ME! */
	private String roid = null;

	/** DOCUMENT ME! */
	private boolean cmdType = false;

	/**
	 * <code>EPPDefRegRenewResp</code> default constructor.  Must call required
	 * setter methods before encode.     the defaults include the following:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * roid is set to <code>null</code>
	 * </li>
	 * <li>
	 * expiration date is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The roid must be set before invoking <code>encode</code>.
	 */
	public EPPDefRegRenewResp() {
	}

	// End EPPDefRegRenewResp()

	/**
	 * <code>EPPDefRegRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aRoid DefReg roid
	 */
	public EPPDefRegRenewResp(EPPTransId aTransId, String aRoid) {
		super(aTransId);

		roid = aRoid;
	}

	// End EPPDefRegRenewResp.EPPDefRegRenewResp(EPPTransId, String)

	/**
	 * <code>EPPDefRegRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aRoid defReg roid
	 * @param aExpirationDate expiration date of the defReg
	 */
	public EPPDefRegRenewResp(
							  EPPTransId aTransId, String aRoid,
							  Date aExpirationDate) {
		super(aTransId);

		roid			   = aRoid;
		expirationDate     = aExpirationDate;
	}

	// End EPPDefRegRenewResp.EPPDefRegRenewResp(EPPTransId, String, String, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPDefRegRenewResp</code>.
	 *
	 * @return EPPDefRegRenewResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDefRegRenewResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegRenewResp</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegRenewResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDefRegRenewResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegRenewResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegRenewResp theComp = (EPPDefRegRenewResp) aObject;

		// Roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
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

	// End EPPDefRegRenewResp.equals(Object)

	/**
	 * Clone <code>EPPDefRegRenewResp</code>.
	 *
	 * @return clone of <code>EPPDefRegRenewResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegRenewResp clone = (EPPDefRegRenewResp) super.clone();

		return clone;
	}

	// End EPPDefRegRenewResp.clone()

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

	// End EPPDefRegRenewResp.toString()

	/**
	 * Gets the defReg roid
	 *
	 * @return DefReg Roid <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegRenewResp.getRoid()

	/**
	 * Sets the defReg roid.
	 *
	 * @param aRoid DefReg Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegRenewResp.setRoid(String)

	/**
	 * Gets the expiration date and time of the defReg.
	 *
	 * @return Expiration date and time of the defReg if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPDefRegRenewResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the defReg.
	 *
	 * @param aExpirationDate Expiration date and time of the defReg.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPDefRegRenewResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPDefRegRenewResp</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the roid of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}
	}

	// End EPPDefRegRenewResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegRenewResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDefRegRenewResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegRenewResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPDefRegRenewResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_ROID);

		// Expiration Date
		if (expirationDate != null) {
			EPPUtil.encodeTimeInstant(
									  aDocument, root, expirationDate,
									  EPPDefRegMapFactory.NS,
									  ELM_DEFREG_EXPIRATION_DATE);
		}

		return root;
	}

	// End EPPDefRegRenewResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegRenewResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegRenewResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// DefReg Roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

		// Expiration Date
		expirationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_EXPIRATION_DATE);

		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPDefRegRenewResp.decode: "
										 + e);
		}
	}

	// End EPPDefRegRenewResp.doDecode(Element)
}
