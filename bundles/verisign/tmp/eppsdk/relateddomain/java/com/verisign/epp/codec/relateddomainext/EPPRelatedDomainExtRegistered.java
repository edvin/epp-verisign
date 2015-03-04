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
import java.util.Iterator;
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
 * EPPCodecComponent that encodes and decodes a <relDom:registered> tag
 * <p>
 * Title: EPP 1.0 Related Domain - registered tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtRegistered object represents the
 * collection of domains that are registered in a family of related domains. As
 * such it is composed of a collection of {@link EPPRelatedDomainExtName}
 * objects. <br/>
 * As XML, it is represented by a <relDom:registered> element containing a
 * number of <relDom:name> elements.
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
public class EPPRelatedDomainExtRegistered implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPRelatedDomainExtRegistered.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Element tag name for registered
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":registered";

	/**
	 * List of domain names represented by the {@link EPPRelatedDomainExtName} to
	 * be associated with the <relDom:registered> element
	 */
	private List<EPPRelatedDomainExtName> registeredDomains = null;


	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtRegistered () {
	}


	/**
	 * Constructor with a list of registered domain names.
	 * 
	 * @param aRegisteredDomains
	 */
	public EPPRelatedDomainExtRegistered(
			final List<EPPRelatedDomainExtName> aRegisteredDomains) {
		this.registeredDomains = aRegisteredDomains;

	}

	/**
	 * A deep clone of the EPPRelatedDomainExtRegistered
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		final EPPRelatedDomainExtRegistered theClone = new EPPRelatedDomainExtRegistered();

		if (this.registeredDomains != null) {
			theClone.registeredDomains = new ArrayList<EPPRelatedDomainExtName>();

			for (EPPRelatedDomainExtName registeredDomain : this.registeredDomains) {

				if (registeredDomain != null) {
					theClone.registeredDomains
							.add((EPPRelatedDomainExtName) registeredDomain
									.clone());
				}
				else {
					theClone.registeredDomains.add(null);
				}

			}

		}
		else {
			theClone.registeredDomains = null;
		}

		return theClone;
	}

	/**
	 * Populate the data of this instance with the data stored in the given
	 * Element of the DOM tree
	 * 
	 * @param aElement
	 *        The root element of the report fragment of XML
	 * @throws EPPDecodeException
	 *         Thrown if any errors occur during decoding.
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {

		this.registeredDomains =
				EPPUtil.decodeCompList( aElement, EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtName.ELM_DOMAIN_NAME,
						EPPRelatedDomainExtName.class );
	}


	/**
	 * Append all data from the list of the list of registered related domain names
	 * represented by {@link EPPRelatedDomainExtName} to given DOM Document
	 * 
	 * @param aDocument
	 *        The DOM Document to append data to
	 * @return Encoded DOM <code>Element</code>
	 * @throws EPPEncodeException
	 *         Thrown when errors occur during the encode attempt or if the
	 *         instance is invalid.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " in EPPRelatedDomainExtRegistered.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( final EPPCodecException e ) {
			cat
					.error( "EPPRelatedDomainExtRegistered.encode(): Invalid state on encode: "
							+ e );
			throw new EPPEncodeException(
					"EPPRelatedDomainExtRegistered invalid state: " + e );
		}

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		// dsData
		EPPUtil.encodeCompList( aDocument, root, this.registeredDomains );
		return root;
	}


	/**
	 * A deep comparison of this with another EPPRelatedDomainExtRegistered.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals ( final Object aObj ) {

		if ( !(aObj instanceof EPPRelatedDomainExtRegistered) ) {
			return false;
		}

		final EPPRelatedDomainExtRegistered theComp =
				(EPPRelatedDomainExtRegistered) aObj;

		if ( !EPPUtil
				.equalLists( this.registeredDomains, theComp.registeredDomains ) ) {
			cat
					.error( "EPPRelatedDomainExtRegistered.equals(): registeredDomains not equal" );
			return false;
		}

		return true;
	}

	
	/**
	 * Adds a registered domain represented by
	 * {@link EPPRelatedDomainExtName} to the list of registered domains.
	 * 
	 * @param aRegisteredDomain Registered domain to add to the list.
	 */
	public void addRegisteredDomain(EPPRelatedDomainExtName aRegisteredDomain) {
		if (this.registeredDomains == null) {
			this.registeredDomains = new ArrayList<EPPRelatedDomainExtName>();
		}
		
		this.registeredDomains.add(aRegisteredDomain);
	}
	
	/**
	 * Are there any registered domains included in the list of registered domains?
	 * 
	 * @return <code>true</code> if the registered domain list is not <code>null</code> and
	 *         there is at least one registered domain in the list; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasRegisteredDomains() {
		if (this.registeredDomains != null && this.registeredDomains.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Returns the registeredDomains
	 * 
	 * @return the registeredDomains
	 */
	public List<EPPRelatedDomainExtName> getRegisteredDomains () {
		return this.registeredDomains;
	}


	/**
	 * Sets registeredDomains value to aRegisteredDomains
	 * 
	 * @param aRegisteredDomains
	 *            the registered domains to set
	 */
	public void setRegisteredDomains(
			final List<EPPRelatedDomainExtName> aRegisteredDomains) {
		this.registeredDomains = aRegisteredDomains;
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
	 * Validate the state of the <code>EPPRelatedDomainExtRegistered</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( (this.registeredDomains == null)
				|| (this.registeredDomains.size() == 0) ) {
			throw new EPPCodecException(
					"EPPRelatedDomainExtRegistered contains no elements." );
		}

	}

}
