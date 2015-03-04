/***********************************************************
 Copyright (C) 2012 VeriSign, Inc.

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
 ***********************************************************/
package com.verisign.epp.codec.mark;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class for an address within an {@link EPPMarkContact}. 
 */
public class EPPMarkAddress implements EPPCodecComponent {
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPMarkAddress.class.getName(),
			EPPCatFactory.getInstance().getFactory());
	
	/**
	 * Constant for the local name
	 */
	public static final String ELM_LOCALNAME = "addr";

	/**
	 * Constant for the tag name
	 */
	public static final String ELM_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for the contact's street address.
	 */
	private static final String ELM_STREET = "street";

	/**
	 * Element local name for the contact's city.
	 */
	private static final String ELM_CITY = "city";

	/**
	 * Element local name for the contact's contact's state or province.
	 */
	private static final String ELM_SP = "sp";
	
	/**
	 * Element local name for the contact's contact's postal code.
	 */
	private static final String ELM_PC = "pc";

	/**
	 * Element local name for the contact's contact's contact's country code.
	 */
	private static final String ELM_CC = "cc";

	/**
	 * Zero to three street lines for the contact's street address.
	 */
	List<String> streets = new ArrayList<String>();
	
	/**
	 * Contact's city
	 */
	String city;
	
	/**
	 * Contact's state or province
	 */
	String sp;

	/**
	 * Contact's postal code
	 */
	String pc;
	
	/**
	 * Contact's country code
	 */
	String cc;
	
	/**
	 * Default constructor for <code>EPPMarkAddress</code>.
	 */
	public EPPMarkAddress() {
	}
	
	/**
	 * Constructor that takes all of the <code>EPPMarkAddress</code> 
	 * attributes.
	 * 
	 * @param aStreets Streets of the contact.  
	 * @param aCity City of the contact
	 * @param aSp State or Province of the contact
	 * @param aPc Postal Code of the contact
	 * @param aCc Country code of the contact
	 */
	public EPPMarkAddress(List<String> aStreets, String aCity, String aSp, String aPc, String aCc) {
		this.setStreets(aStreets);
		this.city = aCity;
		this.sp = aSp;
		this.pc = aPc;
		this.cc = aCc;
	}
	
	/**
	 * Clone <code>EPPMarkAddress</code>.
	 * 
	 * @return clone of <code>EPPMarkAddress</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPMarkAddress clone = (EPPMarkAddress) super.clone();

		return clone;
	}

	/**
	 * Sets all this instance's data in the given XML document
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPMarkAddress.encode(Document)");
		}
		
		// Validate streets
		if (this.streets != null && this.streets.size() > 3) {
			throw new EPPEncodeException("street lines exceed the maximum of 3");
		}

		Element root = aDocument.createElementNS(EPPMark.NS, ELM_NAME);

		// Streets
		EPPUtil.encodeList(aDocument, root, this.streets, EPPMark.NS, EPPMark.NS_PREFIX + ":"
				+ ELM_STREET);

		// City
		EPPUtil.encodeString(aDocument, root, this.city, EPPMark.NS, EPPMark.NS_PREFIX + ":"
				+ ELM_CITY);

		// State or Province
		EPPUtil.encodeString(aDocument, root, this.sp, EPPMark.NS, EPPMark.NS_PREFIX + ":"
				+ ELM_SP);

		// Postal Code
		EPPUtil.encodeString(aDocument, root, this.pc, EPPMark.NS, EPPMark.NS_PREFIX + ":"
				+ ELM_PC);
		
		// Country Code
		EPPUtil.encodeString(aDocument, root, this.cc, EPPMark.NS, EPPMark.NS_PREFIX + ":"
				+ ELM_CC);
		
		return root;
	}

	/**
	 * Decode the <code>EPPMark</code> component
	 * 
	 * @param aElement
	 *            Root element of the <code>EPPMark</code>
	 * @throws EPPDecodeException
	 *             Error decoding the <code>EPPMark</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		
		// Streets
		this.streets = EPPUtil.decodeList(aElement, EPPMark.NS, ELM_STREET);

		// City
		this.city = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_CITY);

		// State or Province
		this.sp = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_SP);

		// Postal Code
		this.pc = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_PC);

		// Country Code
		this.cc = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_CC);
	}
	
	/**
	 * implements a deep <code>EPPMarkAddress</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPMarkAddress</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPMarkAddress)) {
			cat.error("EPPMarkAddress.equals(): aObject is not an EPPMarkAddress");
			return false;
		}

		EPPMarkAddress other = (EPPMarkAddress) aObject;

		// Streets
		if (!EqualityUtil.equals(this.streets, other.streets)) {
			cat.error("EPPMarkAddress.equals(): streets not equal");
			return false;
		}

		// City
		if (!EqualityUtil.equals(this.city, other.city)) {
			cat.error("EPPMarkAddress.equals(): city not equal");
			return false;
		}

		// State or Province
		if (!EqualityUtil.equals(this.sp, other.sp)) {
			cat.error("EPPMarkAddress.equals(): sp not equal");
			return false;
		}

		// Postal Code
		if (!EqualityUtil.equals(this.pc, other.pc)) {
			cat.error("EPPMarkAddress.equals(): pc not equal");
			return false;
		}
		
		// Country Code
		if (!EqualityUtil.equals(this.cc, other.cc)) {
			cat.error("EPPMarkAddress.equals(): cc not equal");
			return false;
		}

		return true;
	}

	/**
	 * Gets the contact's street address.
	 * 
	 * @return <code>List</code> of street lines if set; Empty list otherwise.
	 */
	public List<String> getStreets() {
		return this.streets;
	}

	/**
	 * Sets the contact's street address.  There can be update to three lines of the streets address.
	 * @param aStreets Zero to three street lines.  
	 */
	public void setStreets(List<String> aStreets) {
		if (aStreets == null) {
			this.streets = new ArrayList<String>();
		}
		else {
			this.streets = aStreets;
		}
	}
	
	/**
	 * Add a street line to the contact street address. This will add a street to the 
	 * end of the list of street lines.  
	 * 
	 * @param aStreet A line of the contact street address.
	 */
	public void addStreet(String aStreet) {
		if (this.streets == null) {
			this.streets = new ArrayList<String>();
		}
		this.streets.add(aStreet);
	}

	/**
	 * Gets the city of the contact.
	 * 
	 * @return The city of the contact if set; <code>null</code> otherwise.
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Sets the city of the contact.
	 * 
	 * @param aCity City of the contact.
	 */
	public void setCity(String aCity) {
		this.city = aCity;
	}

	/**
	 * Gets the City or Province of the contact.
	 * 
	 * @return City or Province of the contact if set; <code>null</code> otherwise.
	 */
	public String getSp() {
		return this.sp;
	}

	/**
	 * Sets the City or Province of the contact.
	 * 
	 * @param aSp City or Province of the contact.
	 */
	public void setSp(String aSp) {
		this.sp = aSp;
	}

	/**
	 * Gets the Postal Code of the contact.
	 * 
	 * @return Postal Code of the contact if set; <code>null</code> otherwise.
	 */
	public String getPc() {
		return this.pc;
	}

	/**
	 * Sets the Postal Code of the contact.
	 * 
	 * @param aPc Postal Code of the contact.
	 */
	public void setPc(String aPc) {
		this.pc = aPc;
	}
	
	
	/**
	 * Gets the country code of the contact.
	 * 
	 * @return Country code of the contact if set; <code>null</code> otherwise.
	 */
	public String getCc() {
		return this.cc;
	}

	/**
	 * Sets the country code of the contact.
	 * 
	 * @param aCc Country code of the contact.
	 */
	public void setCc(String aCc) {
		this.cc = aCc;
	}
	
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
}
