/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

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
package com.verisign.epp.codec.registry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents the address information policy information. The
 * &lt;registry:address&gt; element contains the following child elements: <br>
 * <ul>
 * <li>
 * &lt;registry:street&gt; - The minimum and maximum length and the minimum and
 * maximum number of the &lt;contact:street&gt; elements defined in RFC 5733</li>
 * <li>
 * &lt;registry:city&gt; - The minimum and maximum length of the
 * &lt;contact:city&gt; element defined in RFC 5733 using the
 * &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child elements,
 * respectively.</li>
 * <li>
 * &lt;registry:sp&gt; - The minimum and maximum length of the
 * &lt;contact:sp&gt; element defined in RFC 5733 using the
 * &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child elements,
 * respectively.</li>
 * <li>
 * &lt;registry:pc&gt; - The minimum and maximum length of the
 * &lt;contact:pc&gt; element defined in RFC 5733 using the
 * &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child elements,
 * respectively</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryPostal
 * @see com.verisign.epp.codec.registry.EPPRegistryContactStreet
 * @see com.verisign.epp.codec.registry.EPPRegistryContactCity
 * @see com.verisign.epp.codec.registry.EPPRegistryContactStateProvince
 * @see com.verisign.epp.codec.registry.EPPRegistryContactPostalCode
 */
public class EPPRegistryContactAddress implements EPPCodecComponent {
	private static final long serialVersionUID = -8821366021541599063L;

	/** XML Element Name of <code>EPPRegistryContactAddress</code> root element. */
	public static final String ELM_NAME = "registry:address";

	/** DOCUMENT ME! */
	public static final String ELM_COUNTRY_CODE = "registry:defaultCC";

	/** Street attributes */
	private EPPRegistryContactStreet street = null;

	/** City attributes */
	private EPPRegistryContactCity city = null;

	/** State/Province attributes */
	private EPPRegistryContactStateProvince stateProvince = null;

	/** Postal code attributes */
	private EPPRegistryContactPostalCode postalCode = null;

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryContactAddress} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryContactAddress} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryContactAddress}
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryContactAddress.encode: " + e);
		}

		Element aRoot = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		EPPUtil.encodeComp(aDocument, aRoot, street);
		EPPUtil.encodeComp(aDocument, aRoot, city);
		EPPUtil.encodeComp(aDocument, aRoot, stateProvince);
		EPPUtil.encodeComp(aDocument, aRoot, postalCode);

		return aRoot;
	}

	/**
	 * Decode the {@code EPPRegistryContactAddress} attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryContactAddress}
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		this.setStreet((EPPRegistryContactStreet) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryContactStreet.ELM_NAME,
				EPPRegistryContactStreet.class));
		this.setCity((EPPRegistryContactCity) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryContactCity.ELM_NAME,
				EPPRegistryContactCity.class));
		this.setStateProvince((EPPRegistryContactStateProvince) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryContactStateProvince.ELM_NAME,
						EPPRegistryContactStateProvince.class));
		this.setPostalCode((EPPRegistryContactPostalCode) EPPUtil.decodeComp(
				aElement, EPPRegistryMapFactory.NS,
				EPPRegistryContactPostalCode.ELM_NAME,
				EPPRegistryContactPostalCode.class));
	}

	/**
	 * Validate the state of the <code>EPPRegistryContactAddress</code>
	 * instance. A valid state means that all of the required attributes have
	 * been set. If validateState returns without an exception, the state is
	 * valid. If the state is not valid, the EPPCodecException will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (street == null) {
			throw new EPPCodecException("street required element is not set");
		}

		if (city == null) {
			throw new EPPCodecException("city required element is not set");
		}

		if (stateProvince == null) {
			throw new EPPCodecException(
					"stateProvince required element is not set");
		}

		if (postalCode == null) {
			throw new EPPCodecException(
					"postalCode required element is not set");
		}
	}

	/**
	 * Clone <code>EPPRegistryContactAddress</code>.
	 * 
	 * @return clone of <code>EPPRegistryContactAddress</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryContactAddress clone = (EPPRegistryContactAddress) super
				.clone();

		if (street != null) {
			clone.street = (EPPRegistryContactStreet) street.clone();
		}

		if (city != null) {
			clone.city = (EPPRegistryContactCity) city.clone();
		}

		if (stateProvince != null) {
			clone.stateProvince = (EPPRegistryContactStateProvince) stateProvince
					.clone();
		}

		if (postalCode != null) {
			clone.postalCode = (EPPRegistryContactPostalCode) postalCode
					.clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryContactAddress</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryContactAddress</code> instance to compare
	 *            with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryContactAddress)) {
			return false;
		}

		EPPRegistryContactAddress theComp = (EPPRegistryContactAddress) aObject;

		if (!((street == null) ? (theComp.street == null) : street
				.equals(theComp.street))) {
			return false;
		}

		if (!((city == null) ? (theComp.city == null) : city
				.equals(theComp.city))) {
			return false;
		}

		if (!((stateProvince == null) ? (theComp.stateProvince == null)
				: stateProvince.equals(theComp.stateProvince))) {
			return false;
		}

		if (!((postalCode == null) ? (theComp.postalCode == null) : postalCode
				.equals(theComp.postalCode))) {
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Gets the contact city attributes.
	 * 
	 * @return instance of {@code EPPRegistryContactCity} that specifies the
	 *         minimum and maximum length of the &lt;contact:city&gt; element
	 *         defined in RFC 5733 using the &lt;registry:minLength&gt; and
	 *         &lt;registry:maxLength&gt; child elements, respectively
	 */
	public EPPRegistryContactCity getCity() {
		return city;
	}

	/**
	 * Sets the contact city attributes.
	 * 
	 * @param city
	 *            instance of {@code EPPRegistryContactCity} that specifies the
	 *            minimum and maximum length of the &lt;contact:city&gt; element
	 *            defined in RFC 5733 using the &lt;registry:minLength&gt; and
	 *            &lt;registry:maxLength&gt; child elements, respectively
	 */
	public void setCity(EPPRegistryContactCity city) {
		this.city = city;
	}

	/**
	 * Gets the contact state/province attributes.
	 * 
	 * @return instance of {@code EPPRegistryContactStateProvince} that
	 *         specifies the minimum and maximum length of the
	 *         &lt;contact:sp&gt; element defined in RFC 5733 using the
	 *         &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child
	 *         elements, respectively
	 */
	public EPPRegistryContactStateProvince getStateProvince() {
		return stateProvince;
	}

	/**
	 * Sets the contact state/province attributes.
	 * 
	 * @param stateProvince
	 *            instance of {@code EPPRegistryContactStateProvince} that
	 *            specifies the minimum and maximum length of the
	 *            &lt;contact:sp&gt; element defined in RFC 5733 using the
	 *            &lt;registry:minLength&gt; and &lt;registry:maxLength&gt;
	 *            child elements, respectively
	 */
	public void setStateProvince(EPPRegistryContactStateProvince stateProvince) {
		this.stateProvince = stateProvince;
	}

	/**
	 * Gets the contact postal code attributes.
	 * 
	 * @return instance of {@code EPPRegistryContactPostalCode} that specifies
	 *         the minimum and maximum length of the &lt;contact:pc&gt; element
	 *         defined in RFC 5733 using the &lt;registry:minLength&gt; and
	 *         &lt;registry:maxLength&gt; child elements, respectively
	 */
	public EPPRegistryContactPostalCode getPostalCode() {
		return postalCode;
	}

	/**
	 * Sets the contact postal code attributes.
	 * 
	 * @param postalCode
	 *            instance of {@code EPPRegistryContactPostalCode} that
	 *            specifies the minimum and maximum length of the
	 *            &lt;contact:pc&gt; element defined in RFC 5733 using the
	 *            &lt;registry:minLength&gt; and &lt;registry:maxLength&gt;
	 *            child elements, respectively
	 */
	public void setPostalCode(EPPRegistryContactPostalCode postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Gets the contact street attributes.
	 * 
	 * @return instance of {@code EPPRegistryContactStreet} that specifies the
	 *         The minimum and maximum length and the minimum and maximum number
	 *         of the &lt;contact:street&gt; elements defined in RFC 5733
	 */
	public EPPRegistryContactStreet getStreet() {
		return street;
	}

	/**
	 * Sets the contact street attributes.
	 * 
	 * @param street
	 *            instance of {@code EPPRegistryContactStreet} that specifies
	 *            the The minimum and maximum length and the minimum and maximum
	 *            number of the &lt;contact:street&gt; elements defined in RFC
	 *            5733
	 */
	public void setStreet(EPPRegistryContactStreet street) {
		this.street = street;
	}
}
