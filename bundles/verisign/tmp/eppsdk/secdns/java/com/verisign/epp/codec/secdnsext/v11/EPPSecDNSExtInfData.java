/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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

package com.verisign.epp.codec.secdnsext.v11;

// Log4j Imports
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtFactory;
import com.verisign.epp.util.EPPCatFactory;

/**
 *
 * The EPPSecDNSExtInfData is the EPPCodecComponent that knows how to encode and
 * decode secDNS infData elements from/to XML and object instance.
 *
 *
 *
 * <p>Title: EPP 1.0 secDNS </p>
 * <p>Description: secDNS Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: VeriSign</p>
 * @version 1.1
 */

public class EPPSecDNSExtInfData implements EPPCodecComponent {

	/**
	 * Used for serialization
	 */
	private static final long serialVersionUID = -2754621455867222676L;

	/** Unspecified maxSigLife value */
	public static final int UNSPEC_MAX_SIG_LIFE = -1;

	/** Minimum maxSigLife value */
	public static final int MIN_MAX_SIG_LIFE = 0;

	/** Maximum maxSigLife value */
	public static final int MAX_MAX_SIG_LIFE = Integer.MAX_VALUE;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPSecDNSExtInfData.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * The element tag name for maxSigLife
	 */
	public static final String ELM_MAX_SIG_LIFE = EPPSecDNSExtFactory.NS_PREFIX
			+ ":maxSigLife";

    /**
     * Element tag name for the infData
     */
   public static final String ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":infData";

	/**
	 * The maxSigLife value
	 */
	private int maxSigLife = UNSPEC_MAX_SIG_LIFE;

	/**
	 * <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances 
	 * as part of the DS Data Interface.
	 */
	private List dsData = null;

	/**
	 * <code>List</code> of Key Data <code>EPPSecDNSExtKeyData</code> instances
	 * as part of the Key Data Interface.
	 */
	private List keyData = null;

	/**
	 * The namespace associated with this secDNS infData.
	 * 
	 * @return The namespace associated with secDNS component
	 */
	public String getNamespace() {
		return EPPSecDNSExtFactory.NS;
	}

	/**
	 * Instantiate a new instance of <code>EPPSecDNSExtInfData</code>. Set either
	 * <code>dsData</code> or <code>keyData</code> prior to calling {@link #encode(Document)}.
	 */
	public EPPSecDNSExtInfData() {
	}

	/**
	 * Creates a new instance of the <code>EPPSecDNSExtInfData</code> with the DS
	 * DATA element.
	 * 
	 * @param aDsData
	 *            <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code>
	 *            instances if using DS Data Interface.
	 * @param aKeyData
	 *            <code>List</code> of Key Data {@link EPPSecDNSExtKeyData}
	 *            instance if using Key Data Interface.
	 * @param aMaxSigLife
	 *            Child's preference for the number of seconds after signature
	 *            generation when the parent's signature on the DS information
	 *            provided by the child will expire. Set to
	 *            <code>UNSPEC_MAX_SIG_LIFE</code> if there is no preference.
	 */
	public EPPSecDNSExtInfData(List aDsData, List aKeyData, int aMaxSigLife) {
		this.dsData = aDsData;
		this.keyData = aKeyData;
		this.maxSigLife = aMaxSigLife;
	}

	/**
	 * Append all data from this secDNS create to the given DOM Document
	 * 
	 * @param aDocument
	 *            The DOM Document to append data to
     * @return Encoded DOM <code>Element</code>
	 * @throws EPPEncodeException
	 *             Thrown when errors occur during the encode attempt or if the
	 *             instance is invalid.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		try {
			// Validate States
			validateState();
		}
		catch (EPPCodecException e) {
			cat.error("EPPSecDNSExtInfData.encode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException("EPPSecDNSExtInfData invalid state: "
					+ e);
		}

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " in EPPSecDNSExtInfData.encode(Document)");
		}

		Element root = aDocument.createElementNS(EPPSecDNSExtFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:" + EPPSecDNSExtFactory.NS_PREFIX,
				EPPSecDNSExtFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPSecDNSExtFactory.NS_SCHEMA);

		// maxSigLife
		if (this.maxSigLife != UNSPEC_MAX_SIG_LIFE) {
			EPPUtil.encodeString(aDocument, root, this.maxSigLife + "",
					EPPSecDNSExtFactory.NS, ELM_MAX_SIG_LIFE);
		}

		// dsData
		EPPUtil.encodeCompList(aDocument, root, this.dsData);

		// keyData
		EPPUtil.encodeCompList(aDocument, root, this.keyData);

		return root;
	}

	/**
	 * Populate the data of this instance with the data stored in the given
	 * Element of the DOM tree
	 * 
	 * @param aElement
	 *            The root element of the report fragment of XML
	 * @throws EPPDecodeException
	 *             Thrown if any errors occur during decoding.
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// maxSigLife
		Integer maxSigLifeInt = EPPUtil.decodeInteger(aElement,
				EPPSecDNSExtFactory.NS, ELM_MAX_SIG_LIFE);
		if (maxSigLifeInt == null) {
			this.maxSigLife = UNSPEC_MAX_SIG_LIFE;
		}
		else {
			this.maxSigLife = maxSigLifeInt.intValue();
		}

		// dsData
		this.dsData = EPPUtil.decodeCompList(aElement, EPPSecDNSExtFactory.NS,
				EPPSecDNSExtDsData.ELM_NAME, EPPSecDNSExtDsData.class);
		if (this.dsData.isEmpty()) {
			this.dsData = null;
		}
				
		// keyData
		this.keyData = EPPUtil.decodeCompList(aElement, EPPSecDNSExtFactory.NS,
				EPPSecDNSExtKeyData.ELM_NAME, EPPSecDNSExtKeyData.class);
		if (this.keyData.isEmpty()) {
			this.keyData = null;
		}
	}

	/**
	 * implements a deep <code>EPPSecDNSExtInfData</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPSecDNSExtInfData</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPSecDNSExtInfData)) {
			return false;
		}
		EPPSecDNSExtInfData theComp = (EPPSecDNSExtInfData) aObject;

		// maxSigLife
		if (this.maxSigLife != theComp.maxSigLife) {
			return false;
		}

		// dsData
		if (!EPPUtil.equalLists(this.dsData, theComp.dsData)) {
			cat.error("EPPSecDNSExtInfData.equals(): dsData not equal");

			return false;
		}

		// keyData
		if (!EPPUtil.equalLists(this.keyData, theComp.keyData)) {
			cat.error("EPPSecDNSExtInfData.equals(): keyData not equal");

			return false;
		}
		
		return true;
	}

	/**
	 * Validate the state of the <code>EPPSecDNSExtInfData</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *             Thrown if the instance is in an invalid state
	 */
	void validateState() throws EPPCodecException {
		// maxSigLife: optional positive int
		if (this.maxSigLife == UNSPEC_MAX_SIG_LIFE) {
			// optional
		}
		else if (this.maxSigLife < MIN_MAX_SIG_LIFE
				&& this.maxSigLife > MAX_MAX_SIG_LIFE) {
			throw new EPPCodecException("EPPSecDNSExtDsData maxSigLife of "
					+ this.maxSigLife + " is out of range, must be between "
					+ MIN_MAX_SIG_LIFE + " and " + MAX_MAX_SIG_LIFE);

		}

		// Both dsData and keyData set?
		if (hasDsData() && hasKeyData()) {
			throw new EPPCodecException("EPPSecDNSExtInfData can not have both dsData and keyData set.");
		}
		
		// Both dsData and keyData NOT set?
		if (!hasDsData() && !hasKeyData()) {
			throw new EPPCodecException("EPPSecDNSExtInfData must set either dsData or keyData.");
		}
	}

	/**
	 * Clone <code>EPPSecDNSExtInfData</code>.
	 * 
	 * @return clone of <code>EPPSecDNSExtInfData</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPSecDNSExtInfData clone = null;

		clone = (EPPSecDNSExtInfData) super.clone();

		// maxSigLife
		clone.maxSigLife = this.maxSigLife;

		// dsData
		if (this.dsData != null) {
			clone.dsData = new ArrayList(this.dsData);
		}
		
		// keyData
		if (this.keyData != null) {
			clone.keyData = new ArrayList(this.keyData);
		}
		return clone;
	}

	/**
	 * Is there dsData contained in <code>EPPSecDNSExtInfData</code>?
	 * 
	 * @return <code>true</code> if list of <code>EPPSecDNSExtDsData</code> is
	 *         not <code>null</code> and not empty; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasDsData() {
		return (this.dsData != null ? !this.dsData.isEmpty() : false);
	}
	
	/**
	 * Gets the dsData.
	 * 
	 * @return <code>List</code> of dsData <code>EPPSecDNSExtDsData</code>
	 *         instances if defined; <code>null</code> otherwise.
	 */
	public List getDsData() {
		return this.dsData;
	}

	/**
	 * Sets the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code>
	 * instances.
	 * 
	 * @param aDsData
	 *            <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
	public void setDsData(List aDsData) {
		this.dsData = aDsData;
	}

	/**
	 * Appends to the <code>List</code> of DS Data
	 * <code>EPPSecDNSExtDsData</code> instances.
	 * 
	 * @param aDsData
	 *            <code>EPPSecDNSExtDsData</code> instance
	 */
	public void appendDsData(EPPSecDNSExtDsData aDsData) {
		if (this.dsData == null) {
			this.dsData = new ArrayList();
		}
		this.dsData.add(aDsData);
	}
	
	
	/**
	 * Is there keyData contained in <code>EPPSecDNSExtInfData</code>?
	 * 
	 * @return <code>true</code> if list of <code>EPPSecDNSExtKeyData</code> is
	 *         not <code>null</code> and not empty; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasKeyData() {
		return (this.keyData != null ? !this.keyData.isEmpty() : false);
	}
	
	/**
	 * Gets the keyData.
	 * 
	 * @return <code>List</code> of keyData <code>EPPSecDNSExtKeyData</code>
	 *         instances if defined; <code>null</code> otherwise.
	 */
	public List getKeyData() {
		return this.keyData;
	}

	/**
	 * Sets the <code>List</code> of DS Data <code>EPPSecDNSExtKeyData</code>
	 * instances to create
	 * 
	 * @param aKeyData
	 *            <code>List</code> of <code>EPPSecDNSExtKeyData</code> instances
	 */
	public void setKeyData(List aKeyData) {
		this.keyData = aKeyData;
	}

	/**
	 * Appends to the <code>List</code> of DS Data
	 * <code>EPPSecDNSExtKeyData</code> instances to create.
	 * 
	 * @param aKeyData
	 *            <code>EPPSecDNSExtKeyData</code> instance
	 */
	public void appendKeyData(EPPSecDNSExtKeyData aKeyData) {
		if (this.keyData == null) {
			this.keyData = new ArrayList();
		}
		this.keyData.add(aKeyData);
	}
	

	/**
	 * Get secDNS:maxSigLife value
	 * 
	 * @return an <code>int</code> value representing secDNS:maxSigLife
	 * @see <code>hasMaxSigLife()</code>
	 * @see <code>UNSPEC_MAX_SIG_LIFE</code>
	 */
	public int getMaxSigLife() {
		return this.maxSigLife;
	}

	/**
	 * Set secDNS:maxSigLife value
	 * 
	 * @param maxSigLife
	 *            an <code>int</code> value representing secDNS:maxSigLife
	 */
	public void setMaxSigLife(int maxSigLife) {
		this.maxSigLife = maxSigLife;
	}

	/**
	 * Does secDNS:infData include optional secDNS:maxSigLife?
	 * 
	 * @return true if secDNS:maxSigLife is specified, otherwise false
	 * @see <code>UNSPEC_MAX_SIG_LIFE</code>
	 */
	public boolean hasMaxSigLife() {
		if (this.maxSigLife == UNSPEC_MAX_SIG_LIFE) {
			return false;
		}
		else {
			return true;
		}
	}
}