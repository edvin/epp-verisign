/**************************************************************************
 *                                                                        *
 * The information in this document is proprietary to VeriSign, Inc.      *
 * It may not be used, reproduced or disclosed without the written        *
 * approval of VeriSign.                                                  *
 *                                                                        *
 * VERISIGN PROPRIETARY & CONFIDENTIAL INFORMATION                        *
 *                                                                        *
 *                                                                        *
 * Copyright (c) 2011 VeriSign, Inc.  All rights reserved.                *
 *                                                                        *
 *************************************************************************/

package com.verisign.epp.codec.relateddomainext;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * EPPCodecComponent that encodes and decodes a <relDom:creData> tag associated
 * with a Domain-Create response.
 * <p>
 * Title: EPP 1.0 Related Domain - creData tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtCreateResp object represents the
 * collection of domains that have been registered in a family of related
 * domains. As such it is composed of a collection of
 * {@link EPPRelatedDomainExtDomainData} objects. <br/>
 * As XML, it is represented by a <relDom:creData> element containing a number
 * of <relDom:domain> elements.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @author nchigurupati
 * @version 1.0
 */
public class EPPRelatedDomainExtCreateResp implements EPPCodecComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7762436016536143713L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPRelatedDomainExtCreateResp.class.getName(), EPPCatFactory
					.getInstance().getFactory());

	/**
	 * Element tag name for creData
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":creData";

	/**
	 * List of domains represented by the {@link EPPRelatedDomainExtDomainData}
	 * to be associated with the <relDom:creData> element
	 */
	private List<EPPRelatedDomainExtDomainData> domains = null;

	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtCreateResp() {
	}

	/**
	 * Constructor with a list of related registered domains.
	 * 
	 * @param aDomains
	 */
	public EPPRelatedDomainExtCreateResp(
			final List<EPPRelatedDomainExtDomainData> aDomains) {
		this.domains = aDomains;

	}

	/**
	 * A deep clone of the EPPRelatedDomainExtCreateResp
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		final EPPRelatedDomainExtCreateResp theClone = new EPPRelatedDomainExtCreateResp();

		if (this.domains != null) {
			theClone.domains = new ArrayList<EPPRelatedDomainExtDomainData>();

			for (EPPRelatedDomainExtDomainData domain : this.domains) {

				if (domain != null) {
					theClone.domains.add((EPPRelatedDomainExtDomainData) domain
							.clone());
				}
				else {
					theClone.domains.add(null);
				}

			}

		}
		else {
			theClone.domains = null;
		}

		return theClone;
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
	public void decode(final Element aElement) throws EPPDecodeException {

		this.domains = EPPUtil.decodeCompList(aElement,
				EPPRelatedDomainExtFactory.NS,
				EPPRelatedDomainExtDomainData.ELM_NAME,
				EPPRelatedDomainExtDomainData.class);
		try {
			validateState();
		}
		catch (final EPPCodecException e) {
			throw new EPPDecodeException(
					"Invalid state on EPPRelatedDomainExtCreateResp.decode: "
							+ e);
		}
	}

	/**
	 * Append all data from the list registered related domain names represented
	 * by {@link EPPRelatedDomainExtDomainData} to given DOM Document
	 * 
	 * @param aDocument
	 *            The DOM Document to append data to
	 * @return Encoded DOM <code>Element</code>
	 * @throws EPPEncodeException
	 *             Thrown when errors occur during the encode attempt or if the
	 *             instance is invalid.
	 */
	public Element encode(final Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " in EPPRelatedDomainExtCreateResp.encode(Document)");
		}

		try {
			// Validate States
			validateState();
		}
		catch (final EPPCodecException e) {
			cat.error("EPPRelatedDomainExtCreateResp.encode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException(
					"EPPRelatedDomainExtCreateResp invalid state: " + e);
		}

		final Element root = aDocument.createElementNS(
				EPPRelatedDomainExtFactory.NS, ELM_NAME);

		// dsData
		EPPUtil.encodeCompList(aDocument, root, this.domains);
		return root;
	}

	/**
	 * A deep comparison of this with another EPPRelatedDomainExtCreateResp.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object aObj) {

		if (!(aObj instanceof EPPRelatedDomainExtCreateResp)) {
			return false;
		}

		final EPPRelatedDomainExtCreateResp theComp = (EPPRelatedDomainExtCreateResp) aObj;

		if (!EPPUtil.equalLists(this.domains, theComp.domains)) {
			cat.error("EPPRelatedDomainExtCreateResp.equals(): related domains not equal");
			return false;
		}

		return true;
	}

	/**
	 * Adds a related domain represented by
	 * {@link EPPRelatedDomainExtDomainData} to the list of related domains.
	 * 
	 * @param aDomain Related domain to add to the list.
	 */
	public void addDomain(EPPRelatedDomainExtDomainData aDomain) {
		if (this.domains == null) {
			this.domains = new ArrayList<EPPRelatedDomainExtDomainData>();
		}
		
		this.domains.add(aDomain);
	}
	
	/**
	 * Are there any domains included in the list of domains?
	 * 
	 * @return <code>true</code> if the domain list is not <code>null</code> and
	 *         there is at least one domain in the list; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasDomains() {
		if (this.domains != null && this.domains.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Returns the list of registered related domain names represented by
	 * {@link EPPRelatedDomainExtDomainData}
	 * 
	 * @return the relatedDomains
	 */
	public List<EPPRelatedDomainExtDomainData> getDomains() {
		return this.domains;
	}

	/**
	 * Sets the list of registered related domain names represented by
	 * {@link EPPRelatedDomainExtDomainData}
	 * 
	 * @param aRelatedDomains
	 *            the related domains to set
	 */
	public void setDomains(
			final List<EPPRelatedDomainExtDomainData> aRelatedDomains) {
		this.domains = aRelatedDomains;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	@Override
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Validate the state of the <code>EPPRelatedDomainExtCreateResp</code>
	 * instance. A valid state means that all of the required attributes have
	 * been set. If validateState returns without an exception, the state is
	 * valid. If the state is not valid, the <code>EPPCodecException</code> will
	 * contain a description of the error. throws EPPCodecException State error.
	 * This will contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *             Thrown if the instance is in an invalid state
	 */
	private void validateState() throws EPPCodecException {
		if ((this.domains == null) || (this.domains.size() == 0)) {
			throw new EPPCodecException(
					"EPPRelatedDomainExtCreateResp contains no  elements.");
		}

		for (EPPRelatedDomainExtDomainData domain : this.domains) {
			if (domain == null) {
				throw new EPPCodecException(
						"EPPRelatedDomainExtCreateResp: contains null EPPRelatedDomainExtDomainData  element.");
			}
			domain.validateStateForCreate();
		}

	}

}
