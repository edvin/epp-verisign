/***********************************************************
Copyright (C) 2011 VeriSign, Inc.

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

package com.verisign.epp.codec.balance;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>EPPCreditThreshold</code> is the <code>EPPCodecComponent</code>
 * that knows how to encode and decode Credit Threshold Type elements from/to
 * XML and object instance.
 */
public class EPPCreditThreshold implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPCreditThreshold.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * The constant value for FIXED
	 */
	public static final String FIXED = "FIXED";

	/**
	 * The constant value for PERCENT
	 */
	public static final String PERCENT = "PERCENT";

	/**
	 * XML Element Name of <code>EPPCreditThreshold</code> root element.
	 */
	final static java.lang.String ELM_NAME = "balance:creditThreshold";
	
	/**
	 * XML Element Name of fixed <code>EPPCreditThreshold</code> element.
	 */
	final static java.lang.String ELM_FIXED = "balance:fixed";
	
	/**
	 * XML Element Name of percent <code>EPPCreditThreshold</code> element.
	 */
	final static java.lang.String ELM_PERCENT = "balance:percent";
	

	/**
	 * XML Element Type Attribute Name of <code>EPPCreditThreshold</code> root
	 * element.
	 */
	final static java.lang.String ATTR_NAME = "type";

	/**
	 * The type value of this Type component
	 */
	private String type = FIXED;

	/**
	 * The creditThresholdValue value of this Type component
	 */
	private BigDecimal value = null;

	/**
	 * Create a new instance of EPPCreditThreshold. The <code>type</code>
	 * defaults to <code>EPPCreditThreshold.FIXED</code> and the
	 * <code>creditThresholdValue</code> value needs to be set via
	 * {@link #setValue(BigDecimal)} prior to called
	 * {@link #encode(Document)}.
	 */
	public EPPCreditThreshold() {
	}

	/**
	 * Create a new instance of EPPCreditThreshold with the given threshold type
	 * and threshold amount
	 * 
	 * @param aType
	 *            the type value to use for this instance. Should use one of the
	 *            static constants defined for this class as a value.
	 * @param aCreditThresholdValue
	 *            The threshold amount in a fixed dollar amount if
	 *            <code>aType</code> is <code>EPPCreditThreshold.FIXED</code> or
	 *            in a percentage value if <code>aType</code> is
	 *            <code>EPPCreditThreshold.PERCENT</code>
	 */

	public EPPCreditThreshold(String aType, BigDecimal aCreditThresholdValue) {
		type = aType;
		this.value = aCreditThresholdValue;
	}

	/**
	 * Append all attributes frm the <code>EPPCreditThreshold</code> to the
	 * given DOM Document
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
			cat
					.error("EPPCreditThreshold.doEncode(): Invalid state on encode: "
							+ e);
			throw new EPPEncodeException("EPPCreditThreshold invalid state: "
					+ e);
		}

		// creditThreshold with Attributes
		Element root = aDocument.createElementNS(EPPBalanceMapFactory.NS,
				ELM_NAME);
		
		Element valueElm;
		
		// Fixed threshold?
		if (this.type.equals(FIXED)) {
			valueElm = aDocument.createElementNS(EPPBalanceMapFactory.NS,
					ELM_FIXED);
		}
		else { // Percent threshold
			valueElm = aDocument.createElementNS(EPPBalanceMapFactory.NS,
					ELM_PERCENT);	
		}
		
		Text descVal = aDocument.createTextNode(this.value
				.toString());
		
		valueElm.appendChild(descVal);
		
		root.appendChild(valueElm);
		
		return root;
	}

	/**
	 * Populate the data of this instance with the data stored in the given
	 * Element of the DOM tree.
	 * 
	 * @param aElement
	 *            The root element of the report fragment of XML
	 * @throws EPPDecodeException
	 *             Thrown if any errors occur during decoding.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		
		Element valueElm = EPPUtil.getFirstElementChild(aElement);
		
		// Fixed threshold?
		if (valueElm.getLocalName().equals(EPPUtil.getLocalName(ELM_FIXED))) {
			this.type = FIXED;
		}
		else { // Percent threshold
			this.type = PERCENT;
		}
		
		// creditThresholdValue
		this.value = new BigDecimal(valueElm.getFirstChild().getNodeValue());
	}

	/**
	 * implements a deep <code>EPPCreditThreshold</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPCreditThreshold</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPCreditThreshold)) {
			return false;
		}

		EPPCreditThreshold theComp = (EPPCreditThreshold) aObject;

		// type
		if (!this.type.equals(theComp.type)) {
			return false;
		}

		// creditThresholdValue
		if (!this.value.equals(theComp.value)) {
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPCreditThreshold</code> instance. A
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
		// type
		if (this.type == null) {
			throw new EPPCodecException(
					"EPPCreditThreshold1 required attribute is not set");
		}

		// creditThresholdValue
		if (this.value == null) {
			throw new EPPCodecException(
					"EPPCreditThreshold2 required attribute is not set");
		}
	}

	/**
	 * Clone <code>EPPCreditThreshold</code>.
	 * 
	 * @return clone of <code>EPPCreditThreshold</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPCreditThreshold clone = null;

		clone = (EPPCreditThreshold) super.clone();

		clone.type = type;

		// creditThresholdValue
		if (this.value != null) {
			clone.value = new BigDecimal(
					this.value.toString());
		}
		else {
			clone.value = null;
		}

		return clone;
	}

	/**
	 * Gets the type of the credit threshold, which is either
	 * <code>EPPCreditThreshold.FIXED</code> or
	 * <code>EPPCreditThreshold.PERCENT</code>.
	 * <code>EPPCreditThreshold.FIXED</code> is the default value.
	 * 
	 * @return Return either <code>EPPCreditThreshold.FIXED</code> or
	 *         <code>EPPCreditThreshold.PERCENT</code>
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Sets the type of the credit threshold, which is either
	 * <code>EPPCreditThreshold.FIXED</code> or
	 * <code>EPPCreditThreshold.PERCENT</code>.
	 * 
	 * @param aType
	 *            Either <code>EPPCreditThreshold.FIXED</code> or
	 *            <code>EPPCreditThreshold.PERCENT</code>
	 */
	public void setType(String aType) {
		if (aType.equals(EPPCreditThreshold.FIXED)
				|| aType.equals(EPPCreditThreshold.PERCENT))
			this.type = aType;
	}

	/**
	 * Gets the credit threshold value.
	 * 
	 * @return Credit threshold value if set; <code>null</code> otherwise.
	 */
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * Sets the credit threshold value. The value is a fixed dollar amount if
	 * the <code>type</code> is <code>EPPCreditThreshold.FIXED</code> and is a
	 * percentage of the value if the <code>type</code> is
	 * <code>EPPCreditThreshold.PERCENT</code>.
	 * 
	 * @param aCreditThresholdValue
	 *            Credit threshold value.
	 */
	public void setValue(BigDecimal aCreditThresholdValue) {
		this.value = aCreditThresholdValue;
	}

}
