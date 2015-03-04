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
 * Represents an EPP NameWatch &lt;nameWatch:renData&gt; response to a <code>
 * EPPNameWatchRenewCmd</code>. When a &lt;renew&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &ltnameWatch:renData&gt; element that identifies the nameWatch
 * namespace and the location of the nameWatch schema.  The
 * &lt;nameWatch:name&gt; element contains the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the fully qualified nameWatch
 * roid that has been created or whose validity period has been extended. Use
 * <code>getRoid</code> and <code>setRoid</code>     to get and set the
 * element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:exDate&gt; element that contains the end of the
 * nameWatch's validity period.  Use <code>getExpirationDate</code> and
 * <code>setExpirationDate</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchRenewCmd
 */
public class EPPNameWatchRenewResp extends EPPResponse {
	/** XML Element Name of <code>EPPNameWatchRenewResp</code> root element. */
	final static String ELM_NAME = "nameWatch:renData";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** XML Element Name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "nameWatch:exDate";

	/** NameWatch Roid of nameWatch to create. */
	private String roid = null;

	/** the end of the nameWatch's validity period. */
	private Date expirationDate = null;

	/**
	 * <code>EPPNameWatchRenewResp</code> default constructor.  Must call
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
	public EPPNameWatchRenewResp() {
	}

	// End EPPNameWatchRenewResp()

	/**
	 * <code>EPPNameWatchRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aRoid NameWatch roid
	 */
	public EPPNameWatchRenewResp(EPPTransId aTransId, String aRoid) {
		super(aTransId);

		roid = aRoid;
	}

	// End EPPNameWatchRenewResp.EPPNameWatchRenewResp(EPPTransId, String)

	/**
	 * <code>EPPNameWatchRenewResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId transaction Id associated with response.
	 * @param aRoid nameWatch roid
	 * @param aExpirationDate expiration date of the nameWatch
	 */
	public EPPNameWatchRenewResp(
								 EPPTransId aTransId, String aRoid,
								 Date aExpirationDate) {
		super(aTransId);

		roid			   = aRoid;
		expirationDate     = aExpirationDate;
	}

	// End EPPNameWatchRenewResp.EPPNameWatchRenewResp(EPPTransId, String, String, Date)

	/**
	 * Gets the EPP command type associated with
	 * <code>EPPNameWatchRenewResp</code>.
	 *
	 * @return EPPNameWatchRenewResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPNameWatchRenewResp.getType()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchRenewResp</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchRenewResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPNameWatchRenewResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchRenewResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchRenewResp theComp = (EPPNameWatchRenewResp) aObject;

		// roid
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

	// End EPPNameWatchRenewResp.equals(Object)

	/**
	 * Clone <code>EPPNameWatchRenewResp</code>.
	 *
	 * @return clone of <code>EPPNameWatchRenewResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchRenewResp clone = (EPPNameWatchRenewResp) super.clone();

		return clone;
	}

	// End EPPNameWatchRenewResp.clone()

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

	// End EPPNameWatchRenewResp.toString()

	/**
	 * Gets the nameWatch roid
	 *
	 * @return NameWatch Roid <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchRenewResp.getRoid()

	/**
	 * Sets the nameWatch roid.
	 *
	 * @param aRoid NameWatch Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchRenewResp.setRoid(String)

	/**
	 * Gets the expiration date and time of the nameWatch.
	 *
	 * @return Expiration date and time of the nameWatch if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	// End EPPNameWatchRenewResp.getExpirationDate()

	/**
	 * Sets the expiration date and time of the nameWatch.
	 *
	 * @param aExpirationDate Expiration date and time of the nameWatch.
	 */
	public void setExpirationDate(Date aExpirationDate) {
		expirationDate = aExpirationDate;
	}

	// End EPPNameWatchRenewResp.setExpirationDate(Date)

	/**
	 * Validate the state of the <code>EPPNameWatchRenewResp</code> instance. A
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
		if (roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}

		if (expirationDate == null) {
			throw new EPPCodecException("expirationDate required attribute is not set");
		}
	}

	// End EPPNameWatchRenewResp.validateState()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchRenewResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPNameWatchRenewResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchRenewResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate Attributes
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPNameWatchRenewResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// Roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Expiration Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, expirationDate,
								  EPPNameWatchMapFactory.NS, ELM_EXPIRATION_DATE);

		return root;
	}

	// End EPPNameWatchRenewResp.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchRenewResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchRenewResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

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
			throw new EPPDecodeException("Invalid state on EPPNameWatchRenewResp.decode: "
										 + e);
		}
	}

	// End EPPNameWatchRenewResp.doDecode(Element)
}
