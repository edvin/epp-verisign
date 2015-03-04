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

import java.util.Date;

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
 * Represents an EPP Host &lt;host:resData&gt; response to a
 * <code>EPPHostCreateCmd</code>.     When a &lt;create&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;host:creData&gt; element that identifies the host namespace and
 * the location of the host schema. The &lt;host:creData&gt; element SHALL
 * contain the following child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains the fully qualified name of the
 * host object. Use <code>getName</code> and <code>setName</code>     to get
 * and set the element.
 * </li>
 * <li>
 * A &lt;host:crDate&gt; element that contains the date and time of host
 * object. Use <code>getCreationDate</code> and <code>setCreationDate</code>
 * to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.host.EPPHostCreateCmd
 */
public class EPPHostCreateResp extends EPPResponse {
	/** XML Element Name of <code>EPPHostCreateResp</code> root element. */
	final static String ELM_NAME = "host:creData";

	/** XML Element Name of <code>EPPHostCreateResp</code> root element. */
	final static String ELM_HOST_NAME = "host:name";

	/** XML Element Name of <code>EPPHostCreateResp</code> root element. */
	final static String ELM_ROID = "host:roid";

	/** XML Element Name for the <code>crDate</code> attribute. */
	private final static String ELM_CREATION_DATE = "host:crDate";

	/** Host Name of host created. */
	private java.lang.String name = null;

	/** Creation Date. */
	private java.util.Date creationDate = null;

	/**
	 * <code>EPPHostCreateResp</code> default constructor. <br>
	 * It will set results attribute to an empty <code>Vector</code>. The name
	 * is initialized to null. The creation date must be set before invoking
	 * <code>encode</code>.
	 */
	public EPPHostCreateResp() {
		name = null;
	}

	// End EPPHostCreateResp()

	/**
	 * <code>EPPHostCreateResp</code> constructor that will set the result of
	 * an individual host. <br>
	 * The creation date must be set before invoking <code>encode</code>.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName Host name
	 */
	public EPPHostCreateResp(EPPTransId aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPHostCreateResp(EPPTransId, EPPHostPingResult)

	/**
	 * <code>EPPHostCreateResp</code> constructor that takes the required
	 * attribute values as parameters.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aName Host name
	 * @param aCreationDate Creation date of the host
	 */
	public EPPHostCreateResp(
							 EPPTransId aTransId, String aName,
							 Date aCreationDate) {
		super(aTransId);

		name			 = aName;
		creationDate     = aCreationDate;
	}

	// End EPPHostCreateResp.EPPHostCreateResp(EPPTransId, String, String, Date)

	/**
	 * Get the EPP response type associated with
	 * <code>EPPHostCreateResp</code>.
	 *
	 * @return EPPHostCreateResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPHostCreateResp.getType()

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPHostCreateResp</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostCreateResp.getNamespace()

	/**
	 * Get creation date.
	 *
	 * @return creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// End EPPHostCreateResp.getCreationDate()

	/**
	 * Set creation date.
	 *
	 * @param newCrDate creation date
	 */
	public void setCreationDate(Date newCrDate) {
		creationDate = newCrDate;
	}

	// End EPPHostCreateResp.setCreationDate(Date)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostCreateResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPHostCreateResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostCreateResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPHostCreateResp.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPHostMapFactory.NS,
							 ELM_HOST_NAME);

		// Creation Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, creationDate,
								  EPPHostMapFactory.NS, ELM_CREATION_DATE);

		return root;
	}

	// End EPPHostCreateResp.doEncode(Document)

	/**
	 * Decode the <code>EPPHostCreateResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPHostCreateResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// name
		name =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);

		// Creation Date
		creationDate =
			EPPUtil.decodeTimeInstant(
									  aElement, EPPHostMapFactory.NS,
									  ELM_CREATION_DATE);
	}

	// End EPPHostCreateResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPHostCreateResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostCreateResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostCreateResp theCreateData = (EPPHostCreateResp) aObject;

		// Name
		if (
			!(
					(name == null) ? (theCreateData.name == null)
									   : name.equals(theCreateData.name)
				)) {
			return false;
		}

		// Creation Date
		if (
			!(
					(creationDate == null) ? (
												   theCreateData.creationDate == null
											   )
											   : creationDate.equals(theCreateData.creationDate)
				)) {
			return false;
		}

		return true;
	}

	// End EPPHostCreateResp.equals(Object)

	/**
	 * Clone <code>EPPHostCreateResp</code>.
	 *
	 * @return clone of <code>EPPHostCreateResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostCreateResp clone = (EPPHostCreateResp) super.clone();

		return clone;
	}

	// End EPPHostCreateResp.clone()

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

	// End EPPHostCreateResp.toString()

	/**
	 * Get host name.
	 *
	 * @return String  Host Name
	 */
	public java.lang.String getName() {
		return name;
	}

	// End EPPHostCreateResp.getName()

	/**
	 * Set host name.
	 *
	 * @param newName String
	 */
	public void setName(String newName) {
		name = newName;
	}

	// End EPPHostCreateResp.setName(String)

	/**
	 * Validate the state of the <code>EPPHostCreateResp</code> instance.  A
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
			throw new EPPCodecException("required attribute name is not set");
		}

		if (creationDate == null) {
			throw new EPPCodecException("required attribute creationDate is not set");
		}
	}

	//End of validateState()
}
