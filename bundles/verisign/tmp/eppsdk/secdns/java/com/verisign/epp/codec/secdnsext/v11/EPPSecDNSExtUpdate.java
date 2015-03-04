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
import com.verisign.epp.util.EPPCatFactory;

/**
 * 
 * The EPPSecDNSExtUpdate is the EPPCodecComponent that knows how to encode and
 * decode secDNS update elements from/to XML and object instance.
 * 
 * 
 * 
 * <p>
 * Title: EPP 1.0 secDNS
 * </p>
 * <p>
 * Description: secDNS Extension to the EPP SDK
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @version 1.1
 */

public class EPPSecDNSExtUpdate implements EPPCodecComponent {

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPSecDNSExtUpdate.class
			.getName(), EPPCatFactory.getInstance().getFactory());

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

	/**
	 * Element tag name for the update
	 */
	public static final String ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX
			+ ":update";

	/**
	 * Element tag name for the add
	 */
	public static final String ELM_ADD = EPPSecDNSExtFactory.NS_PREFIX + ":add";

	/**
	 * Element tag name for the chg
	 */
	public static final String ELM_CHG = EPPSecDNSExtFactory.NS_PREFIX + ":chg";

	/**
	 * Element tag name for the rem
	 */
	public static final String ELM_REM = EPPSecDNSExtFactory.NS_PREFIX + ":rem";

	/**
	 * The element tag name for maxSigLife
	 */
	public static final String ELM_MAX_SIG_LIFE = EPPSecDNSExtFactory.NS_PREFIX
			+ ":maxSigLife";

	/** XML Attribute Name for the urgent attribute */
	private final static String ATTR_URGENT = "urgent";

	private static final String ELM_ALL = EPPSecDNSExtFactory.NS_PREFIX
	+ ":all";

	/**
	 * The maxSigLife value as part of the chg
	 */
	private int maxSigLife = UNSPEC_MAX_SIG_LIFE;

	/**
	 * <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to
	 * add as part of the DS Data Interface.
	 */
	private List addDsData = null;

	/**
	 * <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to
	 * remove as part of the DS Data Interface.
	 */
	private List remDsData = null;

	/**
	 * <code>List</code> of Key Data <code>EPPSecDNSExtKeyData</code> instances
	 * to add as part of the Key Data Interface.
	 */
	private List addKeyData = null;

	/**
	 * <code>List</code> of DS Data <code>EPPSecDNSExtKeyData</code> instances
	 * to remove as part of the Key Data Interface.
	 */
	private List remKeyData = null;

	/**
	 * High priority request? A value of <code>true</code> indicates that the
	 * client has asked the server operator to process the update command with a
	 * high priority. Default value is <code>false</code>.
	 */
	private boolean urgent = false;

	/**
	 * Remove all DS / Key data flag as an alternative to explicitly specifying
	 * all of the <code>EPPSecDNSExtDsData</code> or
	 * <code>EPPSecDNSExtKeyData</code> instances to remove.
	 */
	private boolean remAllData = false;

	/**
	 * The namespace associated with this secDNS update.
	 * 
	 * @return The namespace associated with secDNS component
	 */
	public String getNamespace() {
		return EPPSecDNSExtFactory.NS;
	}

	/**
	 * Instantiate a new instance of EPPSecDNSExtUpdate
	 */
	public EPPSecDNSExtUpdate() {
	}

	/**
	 * Append all data from this secDNS update to the given DOM Document
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
			cat.error("EPPSecDNSExtUpdate.encode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException("EPPSecDNSExtUpdate invalid state: "
					+ e);
		}

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " in EPPSecDNSExtUpdate.encode(Document)");
		}

		Element root = aDocument.createElementNS(EPPSecDNSExtFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:" + EPPSecDNSExtFactory.NS_PREFIX,
				EPPSecDNSExtFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPSecDNSExtFactory.NS_SCHEMA);

		// rem
		if (isRemAllData() || hasRemDsData() || hasRemKeyData()) {
			Element rem = aDocument.createElementNS(EPPSecDNSExtFactory.NS,
					ELM_REM);
			root.appendChild(rem);

			if (isRemAllData()) {
				EPPUtil.encodeBoolean(aDocument, rem, new Boolean(
						this.remAllData), EPPSecDNSExtFactory.NS, ELM_ALL);
			}
			else if (hasRemDsData()) {
				EPPUtil.encodeCompList(aDocument, rem, this.remDsData);
			}
			else { // hasRemKeyData()
				EPPUtil.encodeCompList(aDocument, rem, this.remKeyData);
			}
		}

		// add
		if (hasAddDsData() || hasAddKeyData()) {
			Element add = aDocument.createElementNS(EPPSecDNSExtFactory.NS,
					ELM_ADD);
			root.appendChild(add);

			if (hasAddDsData()) {
				EPPUtil.encodeCompList(aDocument, add, this.addDsData);
			}
			else { // hasAddKeyData()
				EPPUtil.encodeCompList(aDocument, add, this.addKeyData);
			}
		}

		// chg
		if (hasChg()) {
			Element chg = aDocument.createElementNS(EPPSecDNSExtFactory.NS,
					ELM_CHG);
			root.appendChild(chg);

			// maxSigLife
			if (this.maxSigLife != UNSPEC_MAX_SIG_LIFE) {
				EPPUtil.encodeString(aDocument, chg, this.maxSigLife + "",
						EPPSecDNSExtFactory.NS, ELM_MAX_SIG_LIFE);
			}

		}

		// urgent
		if (this.urgent) {
			EPPUtil.encodeBooleanAttr(root, ATTR_URGENT, this.urgent);
		}

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

		// rem
		this.remAllData = false;
		this.remDsData = null;
		this.remKeyData = null;

		Element remElm = EPPUtil.getElementByTagNameNS(aElement, EPPSecDNSExtFactory.NS, ELM_REM);

		if (remElm != null) {

			// all
			Boolean remAll = EPPUtil.decodeBoolean(remElm,
					EPPSecDNSExtFactory.NS, ELM_ALL);
			if (remAll != null && remAll.booleanValue()) {
				this.remAllData = remAll.booleanValue();
			}

			// dsData
			if (remAll == null) {
				this.remDsData = EPPUtil.decodeCompList(remElm,
						EPPSecDNSExtFactory.NS, EPPSecDNSExtDsData.ELM_NAME,
						EPPSecDNSExtDsData.class);

				if (this.remDsData != null && this.remDsData.isEmpty()) {
					this.remDsData = null;
				}
			}

			// keyData
			if (this.remDsData == null) {
				this.remKeyData = EPPUtil.decodeCompList(remElm,
						EPPSecDNSExtFactory.NS, EPPSecDNSExtKeyData.ELM_NAME,
						EPPSecDNSExtKeyData.class);

				if (this.remKeyData != null && this.remKeyData.isEmpty()) {
					this.remKeyData = null;
				}

			} // end if (remElm != null)
		} // if (remElm != null)

		// add
		this.addDsData = null;
		this.addKeyData = null;

		Element addElm = EPPUtil.getElementByTagNameNS(aElement, EPPSecDNSExtFactory.NS, ELM_ADD);

		if (addElm != null) {

			// dsData
			this.addDsData = EPPUtil.decodeCompList(addElm,
					EPPSecDNSExtFactory.NS, EPPSecDNSExtDsData.ELM_NAME,
					EPPSecDNSExtDsData.class);

			if (this.addDsData != null && this.addDsData.isEmpty()) {
				this.addDsData = null;
			}

			// keyData
			this.addKeyData = EPPUtil.decodeCompList(addElm,
					EPPSecDNSExtFactory.NS, EPPSecDNSExtKeyData.ELM_NAME,
					EPPSecDNSExtKeyData.class);

			if (this.addKeyData != null && this.addKeyData.isEmpty()) {
				this.addKeyData = null;
			}

		}

		// chg
		this.maxSigLife = UNSPEC_MAX_SIG_LIFE;
		Element chgElm = EPPUtil.getElementByTagNameNS(aElement, EPPSecDNSExtFactory.NS, ELM_CHG);
		if (chgElm != null) {

			// maxSigLife
			Integer maxSigLifeInt = EPPUtil.decodeInteger(chgElm,
					EPPSecDNSExtFactory.NS, ELM_MAX_SIG_LIFE);
			if (maxSigLifeInt == null) {
				this.maxSigLife = UNSPEC_MAX_SIG_LIFE;
			}
			else {
				this.maxSigLife = maxSigLifeInt.intValue();
			}

		} // if (chgElm != null)

		// urgent
		this.urgent = false;
		if (aElement.getAttribute(ATTR_URGENT) != null) {
			this.urgent = EPPUtil.decodeBooleanAttr(aElement, ATTR_URGENT);
		}
	}

	/**
	 * implements a deep <code>EPPSecDNSExtUpdate</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPSecDNSExtUpdate</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPSecDNSExtUpdate)) {
			return false;
		}
		EPPSecDNSExtUpdate theComp = (EPPSecDNSExtUpdate) aObject;

		// remDsData
		if (!EPPUtil.equalLists(this.remDsData, theComp.remDsData)) {
			cat.error("EPPSecDNSExtUpdate.equals(): remDsData not equal");
			return false;
		}

		// remAllData
		if (this.remAllData != theComp.remAllData) {
			return false;
		}

		// addDsData
		if (!EPPUtil.equalLists(this.addDsData, theComp.addDsData)) {
			cat.error("EPPSecDNSExtUpdate.equals(): addDsData not equal");
			return false;
		}

		// remKeyData
		if (!EPPUtil.equalLists(this.remKeyData, theComp.remKeyData)) {
			cat.error("EPPSecDNSExtUpdate.equals(): remKeyData not equal");
			return false;
		}

		// addKeyData
		if (!EPPUtil.equalLists(this.addKeyData, theComp.addKeyData)) {
			cat.error("EPPSecDNSExtUpdate.equals(): addKeyData not equal");
			return false;
		}

		// maxSigLife
		if (this.maxSigLife != theComp.maxSigLife) {
			return false;
		}

		// urgent
		if (!this.urgent == theComp.urgent) {
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPSecDNSExtUpdate</code> instance. A
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

		// Both dsData and keyData set?
		if ((hasAddDsData() || hasRemDsData())
				&& (hasAddKeyData() || hasRemKeyData())) {
			throw new EPPCodecException(
					"EPPSecDNSExtUpdate can not have both dsData and keyData set.");
		}

		// Remove all along with remove dsData or keyData?
		if (isRemAllData() && (hasRemKeyData() || hasRemDsData())) {
			throw new EPPCodecException(
					"EPPSecDNSExtUpdate can not have both remove all along with remove dsData or keyData.");
		}

		// Is nothing being updated?
		if (!hasAddDsData() && !hasRemDsData() && !hasAddKeyData()
				&& !hasRemKeyData() && !isRemAllData() && !hasChg()) {
			throw new EPPCodecException(
					"EPPSecDNSExtUpdate cannot have no adds, removes, or changes.");
		}

	}

	/**
	 * Clone <code>EPPSecDNSExtUpdate</code>.
	 * 
	 * @return clone of <code>EPPSecDNSExtUpdate</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPSecDNSExtUpdate clone = null;

		clone = (EPPSecDNSExtUpdate) super.clone();

		// remAllDsData
		clone.remAllData = this.remAllData;

		// remDSData
		if (this.remDsData != null) {
			clone.remDsData = new ArrayList(this.remDsData);
		}

		// remKeyData
		if (this.remKeyData != null) {
			clone.remKeyData = new ArrayList(this.remKeyData);
		}

		// addDSData
		if (this.addDsData != null) {
			clone.addDsData = new ArrayList(this.addDsData);
		}

		// addKeyData
		if (this.addKeyData != null) {
			clone.addKeyData = new ArrayList(this.addKeyData);
		}

		// maxSigLife
		clone.maxSigLife = this.maxSigLife;

		// Urgent
		clone.urgent = this.urgent;

		return clone;
	}

	/**
	 * Is there dsData contained in the dsData add list?
	 * 
	 * @return <code>true</code> if add list of <code>EPPSecDNSExtDsData</code>
	 *         is not <code>null</code> and not empty; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasAddDsData() {
		return this.addDsData != null ? !this.addDsData.isEmpty() : false;
	}

	/**
	 * Gets the dsData add list.
	 * 
	 * @return <code>List</code> of dsData <code>EPPSecDNSExtDsData</code>
	 *         instances if defined; <code>null</code> otherwise.
	 */
	public List getAddDsData() {
		return this.addDsData;
	}

	/**
	 * Sets the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code>
	 * instances to add.
	 * 
	 * @param aAddDsData
	 *            <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
	public void setAddDsData(List aAddDsData) {
		this.addDsData = aAddDsData;
	}

	/**
	 * Appends to the <code>List</code> of DS Data
	 * <code>EPPSecDNSExtDsData</code> instances to add.
	 * 
	 * @param aDsData
	 *            <code>EPPSecDNSExtDsData</code> instance
	 */
	public void appendAddDsData(EPPSecDNSExtDsData aDsData) {
		if (this.addDsData == null) {
			this.addDsData = new ArrayList();
		}
		this.addDsData.add(aDsData);
	}

	/**
	 * Is there dsData contained in the dsData remove list?
	 * 
	 * @return <code>true</code> if remove list of
	 *         <code>EPPSecDNSExtDsData</code> is not <code>null</code> and not
	 *         empty; <code>false</code> otherwise.
	 */
	public boolean hasRemDsData() {
		return this.remDsData != null ? !this.remDsData.isEmpty() : false;
	}

	/**
	 * Gets the dsData remove list.
	 * 
	 * @return <code>List</code> of dsData <code>EPPSecDNSExtDsData</code>
	 *         instances if defined; <code>null</code> otherwise.
	 */
	public List getRemDsData() {
		return this.remDsData;
	}

	/**
	 * Sets the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code>
	 * instances to remove.
	 * 
	 * @param aRemDsData
	 *            <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
	public void setRemDsData(List aRemDsData) {
		this.remDsData = aRemDsData;
	}

	/**
	 * Appends to the <code>List</code> of DS Data
	 * <code>EPPSecDNSExtDsData</code> instances to remove.
	 * 
	 * @param aDsData
	 *            <code>EPPSecDNSExtDsData</code> instance
	 */
	public void appendRemDsData(EPPSecDNSExtDsData aDsData) {
		if (this.remDsData == null) {
			this.remDsData = new ArrayList();
		}
		this.remDsData.add(aDsData);
	}

	/**
	 * Sets the flag for removing all DS / Key Data.
	 * 
	 * @param aRemAllData
	 *            <code>true</code> to remove all DS / Key Data;
	 *            <code>false</code> otherwise.
	 */
	public void setRemAllData(boolean aRemAllData) {
		this.remAllData = aRemAllData;
	}

	/**
	 * Remove all DS / Key Data?
	 * 
	 * @return <code>true</code> to remove all DS / Key Data; <code>false</code>
	 *         otherwise.
	 */
	public boolean isRemAllData() {
		return this.remAllData;
	}

	/**
	 * Is there keyData contained in the keyData add list?
	 * 
	 * @return <code>true</code> if add list of <code>EPPSecDNSExtKeyData</code>
	 *         is not <code>null</code> and not empty; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasAddKeyData() {
		return this.addKeyData != null ? !this.addKeyData.isEmpty() : false;
	}

	/**
	 * Gets the keyData add list.
	 * 
	 * @return <code>List</code> of keyData <code>EPPSecDNSExtKeyData</code>
	 *         instances if defined; <code>null</code> otherwise.
	 */
	public List getAddKeyData() {
		return this.addKeyData;
	}

	/**
	 * Sets the <code>List</code> of Key Data <code>EPPSecDNSExtKeyData</code>
	 * instances to add.
	 * 
	 * @param aAddKeyData
	 *            <code>List</code> of <code>EPPSecDNSExtKeyData</code>
	 *            instances
	 */
	public void setAddKeyData(List aAddKeyData) {
		this.addKeyData = aAddKeyData;
	}

	/**
	 * Appends to the <code>List</code> of Key Data
	 * <code>EPPSecDNSExtKeyData</code> instances to add.
	 * 
	 * @param aKeyData
	 *            <code>EPPSecDNSExtKeyData</code> instance
	 */
	public void appendAddKeyData(EPPSecDNSExtKeyData aKeyData) {
		if (this.addKeyData == null) {
			this.addKeyData = new ArrayList();
		}
		this.addKeyData.add(aKeyData);
	}

	/**
	 * Is there keyData contained in the keyData remove list?
	 * 
	 * @return <code>true</code> if remove list of
	 *         <code>EPPSecDNSExtKeyData</code> is not <code>null</code> and not
	 *         empty; <code>false</code> otherwise.
	 */
	public boolean hasRemKeyData() {
		return this.remKeyData != null ? !this.remKeyData.isEmpty() : false;
	}

	/**
	 * Gets the keyData remove list.
	 * 
	 * @return <code>List</code> of dsData <code>EPPSecDNSExtKeyData</code>
	 *         instances if defined; <code>null</code> otherwise.
	 */
	public List getRemKeyData() {
		return this.remKeyData;
	}

	/**
	 * Sets the <code>List</code> of Key Data <code>EPPSecDNSExtKeyData</code>
	 * instances to remove.
	 * 
	 * @param aRemKeyData
	 *            <code>List</code> of <code>EPPSecDNSExtKeyData</code>
	 *            instances
	 */
	public void setRemKeyData(List aRemKeyData) {
		this.remKeyData = aRemKeyData;
	}

	/**
	 * Appends to the <code>List</code> of Key Data
	 * <code>EPPSecDNSExtKeyData</code> instances to remove.
	 * 
	 * @param aKeyData
	 *            <code>EPPSecDNSExtKeyData</code> instance
	 */
	public void appendRemKeyData(EPPSecDNSExtKeyData aKeyData) {
		if (this.remKeyData == null) {
			this.remKeyData = new ArrayList();
		}
		this.remKeyData.add(aKeyData);
	}

	/**
	 * Is the update request urgent?
	 * 
	 * @return Returns <code>true</code> if the client has asked the server
	 *         operator to process the update command with a high priority;
	 *         <code>false</code> otherwise.
	 */
	public boolean isUrgent() {
		return this.urgent;
	}

	/**
	 * Sets the urgent attribute.
	 * 
	 * @param urgent
	 *            The urgent value to set.
	 */
	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
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
	 * Does secDNS:chg include optional secDNS:maxSigLife?
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

	/**
	 * Is there are change (chg) element included in the update. Currently the
	 * only change element is secDNS:maxSigLife.
	 * 
	 * @return <code>true</code> if a change element is defined;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasChg() {
		return hasMaxSigLife();
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