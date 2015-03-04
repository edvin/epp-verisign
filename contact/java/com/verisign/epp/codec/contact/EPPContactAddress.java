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

// W3C Imports
import org.w3c.dom.Element;

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
 * Represents a contact address specified in <code>EPPContactCreateCmd</code>,
 * <code>EPPContactUpdateCmd</code>, or <code>EPPContactInfoResp</code>. Every
 * contact has associated postal address information.  A postal address
 * contains OPTIONAL street information, city information, OPTIONAL
 * state/province information, an OPTIONAL postal code, and a country
 * identifier as described in [ISO11180].  Address information MAY be provided
 * in both a subset of UTF-8 [RFC2279] that can be represented in 7-bit ASCII
 * [US-ASCII] and unrestricted UTF-8.     A contact address is defined as the
 * following in the EPP Contact Mapping Specification:<br>
 * <br>
 * A &lt;contact:addr&gt; element that contains address information associated
 * with the contact.  A &lt;contact:addr&gt; element     contains the
 * following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * OPTIONAL &lt;contact:street&gt; elements (up to a maximum of three) that
 * contain the contact's street address.  Use <code>getStreets</code> and
 * <code>setStreets</code> to get and set the elements.
 * </li>
 * <li>
 * A &lt;contact:city&gt; element that contains the contact's city.  Use
 * <code>getCity</code> and <code>setCity</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;contact:sp&gt; element that contains the contact's     state or
 * province.  This element is OPTIONAL for addressing schemes     that do not
 * require a state or province name.  Use <code>getStateProvince</code> and
 * <code>setStateProvince</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:pc&gt; element that contains the contact's postal
 * code.  Use <code>getPostalCode</code> and <code>setPostalCode</code> to get
 * and set the element.
 * </li>
 * <li>
 * A &lt;contact:cc&gt; element that contains the two-character     identifier
 * representing with the contact's country.  Use <code>getCountry</code> and
 * <code>setCountry</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactCreateCmd
 * @see com.verisign.epp.codec.contact.EPPContactUpdateCmd
 * @see com.verisign.epp.codec.contact.EPPContactInfoResp
 */
public class EPPContactAddress implements EPPCodecComponent {
	/** XML Element Name of <code>EPPContactAddress</code> root element. */
	final static String ELM_NAME = "contact:addr";

	/** XML tag name for an streets attribute. */
	private final static String ELM_STREET = "contact:street";

	/** XML tag name for an city attribute. */
	private final static String ELM_CITY = "contact:city";

	/** XML tag name for an stateProvince attribute. */
	private final static String ELM_STATE_PROVINCE = "contact:sp";

	/** XML tag name for an postalCode attribute. */
	private final static String ELM_POSTAL_CODE = "contact:pc";

	/** XML tag name for an country attribute. */
	private final static String ELM_COUNTRY = "contact:cc";

	/** XML tag name for an streets attribute. */
	private final static int MAX_STREET = 3;

	/**
	 * Contact street, which is a <code>Vector</code> of 1 or 2
	 * <code>String</code>'s representing     street line 1 and street line 2.
	 */
	private Vector streets = null;

	/** Contact city. */
	private String city = null;

	/** Contact state/province. */
	private String stateProvince = null;

	/** Contact postal code */
	private String postalCode = null;

	/** Contact country */
	private String country = null;

	/**
	 * Default constructor for <code>EPPContactAddress</code>.  All the     the
	 * attributes default to <code>null</code>.   Must call required setter
	 * methods before invoking <code>encode</code>, which include:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * city - <code>setCity</code>
	 * </li>
	 * <li>
	 * country - <code>setCountry</code>
	 * </li>
	 * </ul>
	 */
	public EPPContactAddress() {
		// Default set in attribute definition.
	}

	// End EPPContactAddress.EPPContactAddress()

	/**
	 * Constructor for <code>EPPContactAddress</code> all of the required
	 * attributes as parameters.
	 *
	 * @param aCity contact street
	 * @param aCountry contract country
	 */
	public EPPContactAddress(String aCity, String aCountry) {
		streets			  = null;
		city			  = aCity;
		stateProvince     = null;
		postalCode		  = null;
		country			  = aCountry;
	}

	// EPPContactAddress.EPPContactAddress(Vector, String, String, String)

	/**
	 * Constructor for <code>EPPContactAddress</code> all of the required
	 * attributes as parameters.
	 *
	 * @param someStreets <code>Vector</code> of street (up to maximum three)
	 * 		  <code>String</code>'s
	 * @param aCity contact street
	 * @param aStateProvince contact state/province
	 * @param aPostalCode contact postal code
	 * @param aCountry contract country
	 */
	public EPPContactAddress(
							 Vector someStreets, String aCity,
							 String aStateProvince, String aPostalCode,
							 String aCountry) {
		streets			  = someStreets;
		city			  = aCity;
		stateProvince     = aStateProvince;
		postalCode		  = aPostalCode;
		country			  = aCountry;
	}

	// EPPContactAddress.EPPContactAddress(Vector, String, String, String, String, String)

	/**
	 * Gets the contact street(s).
	 *
	 * @return street(s) as a <code>Vector</code> of street (up to maximum
	 * 		   three) <code>String</code> if defined;     <code>null</code>
	 * 		   otherwise.
	 */
	public Vector getStreets() {
		return streets;
	}

	// End EPPContactAddress.getCity()

	/**
	 * Sets the contact street with only one <code>String</code> parameter.
	 * Only a one element <code>Vector</code> will be returned on     a call
	 * to <code>getStreets</code> when originally set with     this method.
	 *
	 * @param aStreet contact street.
	 */
	public void setStreet(String aStreet) {
		streets = new Vector();

		streets.addElement(aStreet);
	}

	// End EPPContactAddress.setStreet(String)

	/**
	 * Sets the contact street attribute with a <code>Vector</code> of
	 * <code>String</code>'s.
	 *
	 * @param someStreets <code>Vector</code> of one or two street
	 * 		  <code>String</code>'s.
	 */
	public void setStreets(Vector someStreets) {
		streets = someStreets;
	}

	// End EPPContactAddress.setStreet(String, String)

	/**
	 * Sets the contact street with two sub-street <code>String</code>'s.
	 *
	 * @param aStreet1 First part/line of contact street
	 * @param aStreet2 Second part/line of contact street
	 */
	public void setStreets(String aStreet1, String aStreet2) {
		streets = new Vector();

		streets.addElement(aStreet1);
		streets.addElement(aStreet2);
	}

	// End EPPContactAddress.setStreet(String, String)

	/**
	 * Sets the contact street with three sub-street <code>String</code>'s.
	 *
	 * @param aStreet1 First part/line of contact street
	 * @param aStreet2 Second part/line of contact street
	 * @param aStreet3 Third part/line of contact street
	 */
	public void setStreets(String aStreet1, String aStreet2, String aStreet3) {
		streets = new Vector();

		streets.addElement(aStreet1);
		streets.addElement(aStreet2);
		streets.addElement(aStreet3);
	}

	// End EPPContactAddress.setStreet(String, String, String)

	/**
	 * Gets the contact city.
	 *
	 * @return city. <code>String</code> if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getCity() {
		return city;
	}

	// End EPPContactAddress.getCity()

	/**
	 * Sets the contact city.
	 *
	 * @param aCity contact city
	 */
	public void setCity(String aCity) {
		city = aCity;
	}

	// End EPPContactAddress.setCity(String)

	/**
	 * Gets the contact state/province.
	 *
	 * @return state/province. <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getStateProvince() {
		return stateProvince;
	}

	// End EPPContactAddress.getStateProvince()

	/**
	 * Sets the contact state/province.
	 *
	 * @param aStateProvince contact state/province
	 */
	public void setStateProvince(String aStateProvince) {
		stateProvince = aStateProvince;
	}

	// End EPPContactAddress.setStateProvince(String)

	/**
	 * Gets the contact postal code
	 *
	 * @return postal code <code>String</code> if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getPostalCode() {
		return postalCode;
	}

	// End EPPContactAddress.getPostalCode()

	/**
	 * Sets the contact postal code
	 *
	 * @param aPostalCode contact postal code
	 */
	public void setPostalCode(String aPostalCode) {
		postalCode = aPostalCode;
	}

	// End EPPContactAddress.setPostalCode(String)

	/**
	 * Gets the contact country.
	 *
	 * @return contact country <code>String</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getCountry() {
		return country;
	}

	// End EPPContactAddress.getCountry()

	/**
	 * Sets the contact country.
	 *
	 * @param aCountry contact country
	 */
	public void setCountry(String aCountry) {
		country = aCountry;
	}

	// End EPPContactAddress.setCountry(String)

	/**
	 * Validate the state of the <code>EPPContactAddress</code> instance.  A
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
		//ashwin fixed the validation to the latest spec
		//Added the validation of street values by Rupert Fernando.
		if( streets != null ){
			if( streets.contains(null) )
				throw new EPPCodecException("street lines cannot be set to null");
		}
		
		if (				
			(streets != null) && streets.elements().hasMoreElements()
				&& (streets.size() > MAX_STREET)) {
			throw new EPPCodecException("street lines exceed the maximum");
		}

		if (city == null) {
			throw new EPPCodecException("city required attribute is not set");
		}

		if (country == null) {
			throw new EPPCodecException("country required attribute is not set");
		}
	}

	// End EPPContactAddress.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactAddress</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPContactAddress</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactAddress</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPContactAddress.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		// Street(s)
		if ((streets != null) && streets.elements().hasMoreElements()) {
			EPPUtil.encodeVector(
								 aDocument, root, streets,
								 EPPContactMapFactory.NS, ELM_STREET);
		}

		// City
		EPPUtil.encodeString(
							 aDocument, root, city, EPPContactMapFactory.NS,
							 ELM_CITY);

		// State/Province
		if (stateProvince != null) {
			EPPUtil.encodeString(
								 aDocument, root, stateProvince,
								 EPPContactMapFactory.NS, ELM_STATE_PROVINCE);
		}

		// Postal Code
		if (postalCode != null) {
			EPPUtil.encodeString(
								 aDocument, root, postalCode,
								 EPPContactMapFactory.NS, ELM_POSTAL_CODE);
		}

		// Country
		EPPUtil.encodeString(
							 aDocument, root, country, EPPContactMapFactory.NS,
							 ELM_COUNTRY);

		return root;
	}

	// End EPPContactAddress.encode(Document)

	/**
	 * Decode the <code>EPPContactAddress</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactAddress</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Street(s)
		streets =
			EPPUtil.decodeVector(
								 aElement, EPPContactMapFactory.NS,
								 EPPContactAddress.ELM_STREET);

		if (streets.size() == 0) {
			streets = null;
		}

		// City
		city =
			EPPUtil.decodeString(aElement, EPPContactMapFactory.NS, ELM_CITY);

		// State/Province
		stateProvince =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_STATE_PROVINCE);

		// Postal Code
		postalCode =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS,
								 ELM_POSTAL_CODE);

		// Country
		country =
			EPPUtil.decodeString(
								 aElement, EPPContactMapFactory.NS, ELM_COUNTRY);
	}

	// End EPPContactAddress.decode(Element)

	/**
	 * implements a deep <code>EPPContactAddress</code> compare.
	 *
	 * @param aObject <code>EPPContactAddress</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactAddress)) {
			return false;
		}

		EPPContactAddress theComp = (EPPContactAddress) aObject;

		// Street(s)
		if (!EPPUtil.equalVectors(streets, theComp.streets)) {
			return false;
		}

		// City
		if (
			!(
					(city == null) ? (theComp.city == null)
									   : city.equals(theComp.city)
				)) {
			return false;
		}

		// State/Province
		if (
			!(
					(stateProvince == null) ? (theComp.stateProvince == null)
												: stateProvince.equals(theComp.stateProvince)
				)) {
			return false;
		}

		// Postal Code
		if (
			!(
					(postalCode == null) ? (theComp.postalCode == null)
											 : postalCode.equals(theComp.postalCode)
				)) {
			return false;
		}

		// Country
		if (
			!(
					(country == null) ? (theComp.country == null)
										  : country.equals(theComp.country)
				)) {
			return false;
		}

		return true;
	}

	// End EPPContactAddress.equals(Object)

	/**
	 * Clone <code>EPPContactAddress</code>.
	 *
	 * @return clone of <code>EPPContactAddress</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactAddress clone = null;

		clone = (EPPContactAddress) super.clone();

		if (streets != null) {
			clone.streets = (Vector) streets.clone();
		}

		return clone;
	}

	// End EPPContactAddress.clone()

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

	// End EPPContactAddress.toString()
}
