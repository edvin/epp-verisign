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

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * 
 * A concrete EPPResponse that knows how to encode/decode Balance Info response
 * from/to XML and object instance.
 * 
 * <p>
 * Title: EPP 1.0 Balance
 * </p>
 * <p>
 * Description: Balance Mapping for the EPP SDK
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @author jgould
 * @version 1.0
 */

public class EPPBalanceInfoResp extends EPPResponse {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPBalanceInfoResp.class.getName(), EPPCatFactory.getInstance()
					.getFactory());

	/**
	 * The poll data XML tag name
	 */
	public static final String ELM_NAME = "balance:infData";

	/**
	 * The creditLimit XML tag name
	 */
	public static final String ELM_CREDIT_LIMIT = "balance:creditLimit";

	/**
	 * The balance XML tag name
	 */
	public static final String ELM_BALANCE = "balance:balance";

	/**
	 * The availableCredit XML tag name
	 */
	public static final String ELM_AVAILABLE_CREDIT = "balance:availableCredit";

	/**
	 * The creditLimit
	 */
	private BigDecimal creditLimit;

	/**
	 * The balance
	 */
	private BigDecimal balance;

	/**
	 * The availableCredit
	 */
	private BigDecimal availableCredit;

	/**
	 * creditThreshold
	 */
	private EPPCreditThreshold creditThreshold;

	/**
	 * Default constructor
	 */
	public EPPBalanceInfoResp() {
	}

	/**
	 * <code>EPPBalanceInfoResp</code> that takes all of the required attributes.
	 * 
	 * @param aCreditLimit Credit limit to set
	 * @param aBalance Balance to set
	 * @param aAvailableCredit Available credit to set
	 * @param aCreditThreshold Credit threshold to set
	 */
	public EPPBalanceInfoResp(EPPTransId aTransId, BigDecimal aCreditLimit, BigDecimal aBalance, BigDecimal aAvailableCredit, EPPCreditThreshold aCreditThreshold) {
		super(aTransId);
		
		this.creditLimit = aCreditLimit;
		this.balance = aBalance;
		this.availableCredit = aAvailableCredit;
		this.creditThreshold = aCreditThreshold;  
	}
	

	/**
	 * Put all data contained in this poll response into the given XML document
	 * 
	 * @param aDocument
	 *            the DOM Document to attach data to.
	 * @return the element that is a parent of the poll data
	 * @throws EPPEncodeException
	 *             Thrown if any errors occur during the encode process
	 */
	public Element doEncode(Document aDocument) throws EPPEncodeException {

		try {
			// Validate State
			validateState();
		}
		catch (EPPCodecException e) {
			cat
					.error("EPPBalanceInfoResponse.doEncode(): Invalid state on encode: "
							+ e);
			throw new EPPEncodeException("EPPBalanceInfoResponse invalid state: "
					+ e);
		}

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPBalanceInfoResponse.doEncode(Document)");
		}

		Element root = aDocument.createElementNS(EPPBalanceMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:balance", EPPBalanceMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPBalanceMapFactory.NS_SCHEMA);

		// creditLimit
		EPPUtil.encodeString(aDocument, root, this.creditLimit.toString(),
				EPPBalanceMapFactory.NS, ELM_CREDIT_LIMIT);

		// balance
		EPPUtil.encodeString(aDocument, root, this.balance.toString(),
				EPPBalanceMapFactory.NS, ELM_BALANCE);

		// availableCredit
		EPPUtil.encodeString(aDocument, root, this.availableCredit.toString(),
				EPPBalanceMapFactory.NS, ELM_AVAILABLE_CREDIT);

		// creditThreshold
		EPPUtil.encodeComp(aDocument, root, creditThreshold);

		return root;

	}

	/**
	 * Populates the data of this instance from the given XML Element which is
	 * part of a DOM Document
	 * 
	 * @param aElement
	 *            the element that is a parent of the poll data
	 * @throws EPPDecodeException
	 *             thrown if any errors occur during the decode operation
	 */
	public void doDecode(Element aElement) throws EPPDecodeException {

		// creditLimit
		this.creditLimit = new BigDecimal(EPPUtil.decodeString(aElement,
				EPPBalanceMapFactory.NS, ELM_CREDIT_LIMIT));

		// balance
		this.balance = new BigDecimal(EPPUtil.decodeString(aElement,
				EPPBalanceMapFactory.NS, ELM_BALANCE));

		// availableCredit
		this.availableCredit = new BigDecimal(EPPUtil.decodeString(aElement,
				EPPBalanceMapFactory.NS, ELM_AVAILABLE_CREDIT));

		// creditThreshold
		this.creditThreshold = (EPPCreditThreshold) EPPUtil.decodeComp(
				aElement, EPPBalanceMapFactory.NS,
				EPPCreditThreshold.ELM_NAME, EPPCreditThreshold.class);
	}

	/**
	 * implements a deep <code>EPPBalanceInfoResp</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPBalanceInfoResp</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPBalanceInfoResp)) {
			cat.error("EPPBalanceInfoResp.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPBalanceInfoResp instance");
			return false;
		}

		EPPBalanceInfoResp theComp = (EPPBalanceInfoResp) aObject;

		// creditLimit
		if (!((this.creditLimit == null) ? (theComp.creditLimit == null)
				: this.creditLimit.equals(theComp.creditLimit))) {
			cat.error("EPPBalanceInfoResp.equals(): creditLimit not equal");
			return false;
		}

		// balance
		if (!((this.balance == null) ? (theComp.balance == null) : this.balance
				.equals(theComp.balance))) {
			cat.error("EPPBalanceInfoResp.equals(): balance not equal");
			return false;
		}

		// availableCredit
		if (!((this.availableCredit == null) ? (theComp.availableCredit == null)
				: this.availableCredit.equals(theComp.availableCredit))) {
			cat.error("EPPBalanceInfoResp.equals(): availableCredit not equal");
			return false;
		}

		// creditThreshold
		if (!((this.creditThreshold == null) ? (theComp.creditThreshold == null)
				: this.creditThreshold.equals(theComp.creditThreshold))) {
			cat.error("EPPBalanceInfoResp.equals(): creditThreshold not equal");
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPLowBalancePollResponse</code>.
	 * 
	 * @return clone of <code>EPPLowBalancePollResponse</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPBalanceInfoResp clone = (EPPBalanceInfoResp) super.clone();

		// creditLimit
		if (this.creditLimit != null) {
			clone.creditLimit = new BigDecimal(this.creditLimit.toString());
		}
		else {
			clone.creditLimit = null;
		}

		// balance
		if (this.balance != null) {
			clone.balance = new BigDecimal(this.balance.toString());
		}
		else {
			clone.balance = null;
		}
		
		// availableCredit
		if (this.availableCredit != null) {
			clone.availableCredit = new BigDecimal(this.availableCredit.toString());
		}
		else {
			clone.availableCredit = null;
		}
		
		// creditThreshold
		if (creditThreshold != null) {
			clone.creditThreshold = (EPPCreditThreshold) creditThreshold
					.clone();
		}
		else {
			clone.creditThreshold = null;
		}

		return clone;
	}

	/**
	 * Validate the state of the <code>EPPBalanceInfoResp</code> instance. A
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

		// creditLimit
		if (creditLimit == null) {
			throw new EPPCodecException(
					"EPPBalanceInfoResp required creditLimit attribute is not set");
		}

		// balance
		if (balance == null) {
			throw new EPPCodecException(
					"EPPBalanceInfoResp required balance attribute is not set");
		}		
		
		// availableCredit
		if (availableCredit == null) {
			throw new EPPCodecException(
					"EPPBalanceInfoResp required availableCredit attribute is not set");
		}
		
		
		// creditThreshold
		if (creditThreshold == null) {
			throw new EPPCodecException(
					"EPPBalanceInfoResp required creditThreshold attribute is not set");
		}

	}

	/**
	 * Gets the credit limit
	 * 
	 * @return credit limit if set; <code>null</code> otherwise.
	 */
	public BigDecimal getCreditLimit() {
		return this.creditLimit;
	}

	/**
	 * Sets the credit Limit
	 * 
	 * @param aCreditLimit Credit limit to set
	 */
	public void setCreditLimit(BigDecimal aCreditLimit) {
		this.creditLimit = aCreditLimit;
	}

	/**
	 * Gets the balance
	 * 
	 * @return balance if set; <code>null</code> otherwise.
	 */
	public BigDecimal getBalance() {
		return this.balance;
	}

	/**
	 * Sets the balance
	 * 
	 * @param aBalance Balance to set
	 */
	public void setBalance(BigDecimal aBalance) {
		this.balance = aBalance;
	}
	
	/**
	 * Gets the available credit
	 * 
	 * @return available credit if set; <code>null</code> otherwise.
	 */
	public BigDecimal getAvailableCredit() {
		return this.availableCredit;
	}

	/**
	 * Sets the available credit
	 * 
	 * @param aAvailableCredit Available credit to set
	 */
	public void setAvailableCredit(BigDecimal aAvailableCredit) {
		this.availableCredit = aAvailableCredit;
	}
	
	/**
	 * Gets the credit threshold
	 * 	
	 * @return Credit threshold if set; <code>null</code> otherwise.
	 */
	public EPPCreditThreshold getCreditThreshold() {
		return this.creditThreshold;
	}

	/**
	 * Sets the credit threshold
	 * 
	 * @param aCreditThreshold Credit threshold to set
	 */
	public void setCreditThreshold(EPPCreditThreshold aCreditThreshold) {
		this.creditThreshold = aCreditThreshold;
	}

}