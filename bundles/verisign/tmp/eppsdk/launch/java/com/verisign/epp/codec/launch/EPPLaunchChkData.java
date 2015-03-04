/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.? See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA? 02111-1307? USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.codec.launch;

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
 * The extension to a response to a claims check command.
 * 
 * @see com.verisign.epp.codec.launch.EPPLaunchCheck
 * @see com.verisign.epp.codec.launch.EPPLaunchCheckResult
 */
public class EPPLaunchChkData implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchChkData.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the launch phase check response
	 */
	public static final String ELM_LOCALNAME = "chkData";

	/**
	 * Constant for the launch phase info extension tag
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * The phase with the value of <code>EPPLaunchPhase.PHASE_CLAIMS1</code> or
	 * <code>EPPLaunchPhase.PHASE_CLAIMS2</code>.
	 */
	EPPLaunchPhase phase;

	/**
	 * List of claims check results
	 */
	private List<EPPLaunchCheckResult> results = new ArrayList<EPPLaunchCheckResult>();

	/**
	 * <code>EPPLaunchCheckResp</code> default constructor. The results list
	 * will be empty.
	 */
	public EPPLaunchChkData() {
	}

	/**
	 * <code>EPPLaunchCheckResp</code> constructor that takes only the
	 * <code>transId</code> attribute.
	 * 
	 * @param aPhase
	 *            The phase with the value of
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS1</code> or
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS2</code> to indicate the phase the
	 *            response is associated with.
	 */
	public EPPLaunchChkData(EPPLaunchPhase aPhase) {
		this.phase = aPhase;
	}

	/**
	 * <code>EPPLaunchCheckResp</code> constructor that will set the result of
	 * an individual domain.
	 * 
	 * @param aPhase
	 *            The phase with the value of
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS1</code> or
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS2</code> to indicate the phase the
	 *            response is associated with.
	 * @param aResult
	 *            Result of a single domain name.
	 */
	public EPPLaunchChkData(EPPLaunchPhase aPhase,
			EPPLaunchCheckResult aResult) {
		this.phase = aPhase;
		this.addCheckResult(aResult);
	}

	/**
	 * <code>EPPLaunchCheckResp</code> constructor that will set the result of
	 * multiple domains.
	 * 
	 * @param aPhase
	 *            The phase with the value of
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS1</code> or
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS2</code> to indicate the phase the
	 *            response is associated with.
	 * @param aResults
	 *            List of claims check results
	 */
	public EPPLaunchChkData(EPPLaunchPhase aPhase, 
			List<EPPLaunchCheckResult> aResults) {
		this.phase = aPhase;
		this.setCheckResults(aResults);
	}

	/**
	 * Get the EPP response type associated with <code>EPPLaunchCheckResp</code>
	 * .
	 * 
	 * @return EPPLaunchCheckResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPLaunchCheckResp</code>.
	 * 
	 * @return <code>EPPLaunchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPLaunchExtFactory.NS;
	}

	/**
	 * implements a deep <code>EPPLaunchCheckResp</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchCheckResp</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPLaunchChkData)) {
			cat
					.error("EPPLaunchCheckResp.equals(): aObject is not an EPPLaunchCheckResp");
			return false;
		}

		EPPLaunchChkData other = (EPPLaunchChkData) aObject;
		
		// Phase
		if (!EqualityUtil.equals(this.phase, other.phase)) {
			cat.error("EPPLaunchCheckResp.equals(): phase not equal");
			return false;
		}

		// Results
		if (!EqualityUtil.equals(this.results, other.results)) {
			cat.error("EPPLaunchCheckResp.equals(): results not equal");
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPLaunchCheckResp</code>.
	 * 
	 * @return clone of <code>EPPLaunchCheckResp</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPLaunchChkData clone = (EPPLaunchChkData) super.clone();

		clone.results = new ArrayList<EPPLaunchCheckResult>();

		for (EPPLaunchCheckResult result : this.results) {
			clone.results.add((EPPLaunchCheckResult) result.clone());
		}

		return clone;
	}

	/**
	 * Gets phase of the check response.
	 * 
	 * @return phase of the check response if set; <code>null</code> otherwise.
	 */
	public EPPLaunchPhase getPhase() {
		return this.phase;
	}

	/**
	 * Sets the phase of the check response.
	 * 
	 * @param aPhase
	 *            The phase with the value of
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS1</code> or
	 *            <code>EPPLaunchPhase.PHASE_CLAIMS2</code>"
	 */
	public void setPhase(EPPLaunchPhase aPhase) {
		this.phase = aPhase;
	}
	
	/**
	 * Sets the phase with one of the <code>EPPLaunchPhase</code>
	 * <code>PHASE</code> constants.
	 * 
	 * @param aPhaseString
	 *            One of the <code>EPPLaunchPhase</code> <code>PHASE</code>
	 *            constants.
	 */
	public void setPhase(String aPhaseString) {
		this.phase = new EPPLaunchPhase(aPhaseString);
	}
	
	
	
	/**
	 * Get the results of a <code>EPPLaunchCheckResp</code> Response. There is
	 * one <code>EPPLaunchCheckResult</code> instance in
	 * <code>this.results</code> for each domain requested in the Claims Check
	 * Command.
	 * 
	 * @return List of results if defined; empty list otherwise.
	 */
	public List<EPPLaunchCheckResult> getCheckResults() {
		return this.results;
	}

	/**
	 * Set the results of a <code>EPPLaunchCheckResp</code> Response. There is
	 * one <code>EPPLaunchCheckResult</code> instance in
	 * <code>this.results</code> for each domain requested in the Claims Check
	 * Command.
	 * 
	 * @param aResults
	 *            List of claims check results
	 */
	public void setCheckResults(List<EPPLaunchCheckResult> aResults) {
		if (aResults == null) {
			this.results = new ArrayList<EPPLaunchCheckResult>();
		}
		else {
			this.results = aResults;
		}
	}

	/**
	 * Add a claims check result to the list of results.
	 * 
	 * @param aResult
	 *            Claims check result to add to the list.
	 */
	public void addCheckResult(EPPLaunchCheckResult aResult) {
		this.results.add(aResult);
	}

	/**
	 * Sets the claims result of an individual domain name.
	 * 
	 * @param aResult
	 *            Claims check result to set
	 */
	public void setCheckResult(EPPLaunchCheckResult aResult) {
		this.results = new ArrayList<EPPLaunchCheckResult>();
		this.results.add(aResult);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPLaunchCheckResp</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         <code>EPPLaunchCheckResp</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPLaunchCheckResp</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPLaunchCheckResp.encode(Document)");
		}

		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);

		// Phase
		EPPUtil.encodeComp(aDocument, root, this.phase);		
		
		// Results
		EPPUtil.encodeCompList(aDocument, root, this.results);

		return root;
	}

	/**
	 * Decode the <code>EPPLaunchCheckResp</code> attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPLaunchCheckResp</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		
		// Phase
		this.phase = (EPPLaunchPhase) EPPUtil.decodeComp(aElement,
				EPPLaunchExtFactory.NS, EPPLaunchPhase.ELM_NAME,
				EPPLaunchPhase.class);

		// Results
		this.results = EPPUtil.decodeCompList(aElement, EPPLaunchExtFactory.NS,
				EPPLaunchCheckResult.ELM_NAME, EPPLaunchCheckResult.class);
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
