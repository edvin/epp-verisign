/***********************************************************
 Copyright (C) 2013 VeriSign, Inc.

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
 * Class that contains the countries and region of the country where the mark is
 * protected and the OPTIONAL ruling, in case of statute protected marks, to
 * identify the country where the statute was enacted. A list of
 * <code>EPPProtection</code> instances is contained in a
 * {@link EPPTreatyOrStatute}.
 */
public class EPPProtection implements EPPCodecComponent {
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPProtection.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the local name
	 */
	public static final String ELM_LOCALNAME = "protection";

	/**
	 * Constant for the tag name
	 */
	public static final String ELM_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for the two-character code of the country in which the
	 * mark is protected.
	 */
	private static final String ELM_CC = "cc";

	/**
	 * Element local name for the name of the city, state, province or other
	 * geographic region in which the mark is protected
	 */
	private static final String ELM_REGION = "region";

	/**
	 * Element local name for the two-character code of the country of the
	 * ruling.
	 */
	private static final String ELM_RULING = "ruling";

	/**
	 * The two-character code of the country in which the mark is protected.
	 */
	private String cc;

	/**
	 * Name of the city, state, province or other geographic region in which the
	 * mark is protected.
	 */
	private String region;

	/**
	 * Zero or more OPTIONAL &lt;mark:ruling&gt; elements that contains
	 * the two-character code of the countries of the ruling.  This 
	 * is a two-character code from [ISO3166-2].
	 */
	private List<String> rulings = new ArrayList<String>();

	/**
	 * Default constructor for <code>EPPProtection</code>.
	 */
	public EPPProtection() {
	}

	/**
	 * Constructor that takes all of the <code>EPPProtection</code> required
	 * attributes.
	 * 
	 * @param aCC
	 *            Two-character code of the country in which the mark is
	 *            protected.
	 * @param aRegion
	 *            Name of the city, state, province or other geographic region
	 *            in which the mark is protected.
	 */
	public EPPProtection(String aCC, String aRegion) {
		this.cc = aCC;
		this.region = aRegion;
	}

	/**
	 * Constructor that takes all of the <code>EPPProtection</code> attributes with 
	 * a single ruling country.
	 * 
	 * @param aCC
	 *            Two-character code of the country in which the mark is
	 *            protected.
	 * @param aRegion
	 *            Name of the city, state, province or other geographic region
	 *            in which the mark is protected.
	 * @param aRuling
	 *            Identify the country where the statute was enacted
	 */
	public EPPProtection(String aCC, String aRegion, String aRuling) {
		this.cc = aCC;
		this.region = aRegion;
		this.addRuling(aRuling);
	}

	/**
	 * Constructor that takes all of the <code>EPPProtection</code> attributes.
	 * 
	 * @param aCC
	 *            Two-character code of the country in which the mark is
	 *            protected.
	 * @param aRegion
	 *            Name of the city, state, province or other geographic region
	 *            in which the mark is protected.
	 * @param aRulings
	 *            List of two-character code of countries of the ruling.
	 */
	public EPPProtection(String aCC, String aRegion, List<String> aRulings) {
		this.cc = aCC;
		this.region = aRegion;
		this.setRulings(aRulings);
	}
	
	/**
	 * Clone <code>EPPProtection</code>.
	 * 
	 * @return clone of <code>EPPProtection</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPProtection clone = (EPPProtection) super.clone();

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
					+ " on in EPPProtection.encode(Document)");
		}

		// Validate required attributes
		if (this.cc == null) {
			throw new EPPEncodeException("cc is required for protection");
		}
		if (this.region == null) {
			throw new EPPEncodeException("region is required for protection");
		}

		Element root = aDocument.createElementNS(EPPMark.NS, ELM_NAME);

		// Country Code
		EPPUtil.encodeString(aDocument, root, this.cc, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_CC);

		// Region
		EPPUtil.encodeString(aDocument, root, this.region, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_REGION);

		// Rulings
		EPPUtil.encodeList(aDocument, root, this.rulings, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_RULING);

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

		// Country Code
		this.cc = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_CC);

		// Region
		this.region = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_REGION);

		// Rulings
		this.rulings = EPPUtil.decodeList(aElement, EPPMark.NS, ELM_RULING);
	}

	/**
	 * implements a deep <code>EPPProtection</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPProtection</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPProtection)) {
			cat.error("EPPProtection.equals(): aObject is not an EPPProtection");
			return false;
		}

		EPPProtection other = (EPPProtection) aObject;

		// Country Code
		if (!EqualityUtil.equals(this.cc, other.cc)) {
			cat.error("EPPProtection.equals(): cc not equal");
			return false;
		}

		// Region
		if (!EqualityUtil.equals(this.region, other.region)) {
			cat.error("EPPProtection.equals(): region not equal");
			return false;
		}

		// Ruling
		if (!EqualityUtil.equals(this.rulings, other.rulings)) {
			cat.error("EPPProtection.equals(): rulings not equal");
			return false;
		}

		return true;
	}

	/**
	 * Gets two-character code of the country in which the mark is protected
	 * from [ISO3166-2].
	 * 
	 * @return Two-character code of the country in which the mark is protected
	 *         if set; <code>null</code> otherwise.
	 */
	public String getCc() {
		return this.cc;
	}

	/**
	 * Sets two-character code of the country in which the mark is protected.
	 * This is a two-character code from [ISO3166-2].
	 * 
	 * @param aCc
	 *            Two-character code of the country in which the mark is
	 *            protected from [ISO3166-2].
	 */
	public void setCc(String aCc) {
		this.cc = aCc;
	}

	/**
	 * Gets the name of the city, state, province or other geographic region in
	 * which the mark is protected.
	 * 
	 * @return the name of the city, state, province or other geographic region
	 *         in which the mark is protected if set; <code>null</code>
	 *         otherwise.
	 */
	public String getRegion() {
		return this.region;
	}

	/**
	 * Sets the name of the city, state, province or other geographic region in
	 * which the mark is protected.
	 * 
	 * @param aRegion
	 *            Name of the city, state, province or other geographic region
	 *            in which the mark is protected.
	 */
	public void setRegion(String aRegion) {
		this.region = aRegion;
	}

	/**
	 * Gets the list of two-character country codes of the countries of the ruling.
	 * 
	 * @return The countries of the rulings if set; Empty <code>List</code> otherwise.
	 */
	public List<String> getRulings() {
		return this.rulings;
	}

	/**
	 * Sets the list of two-character country codes of the countries of the ruling.
	 * 
	 * @param aRulings
	 *            The countries of the ruling 
	 */
	public void setRulings(List<String> aRulings) {
		this.rulings = aRulings;
		if (this.rulings == null) {
			this.rulings = new ArrayList<String>();
		}
	}
	
	/**
	 * Adds a country for to the list of countries for the ruling.
	 * 
	 * @param aRuling
	 *            Two-character code of the countries of the ruling.  This 
	 * is a two-character code from [ISO3166-2].
	 */
	public void addRuling(String aRuling) {
		if (this.rulings == null) {
			this.rulings = new ArrayList<String>();
		}

		this.rulings.add(aRuling);
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
}
