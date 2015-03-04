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
 * EPPCodecComponent that encodes and decodes a <relDom:available> tag
 * <p>
 * Title: EPP 1.0 Related Domain - available tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtAvailable object represents the
 * collection of domains that are available for registration in a family of
 * related domains. As such it is composed of a collection of
 * {@link EPPRelatedDomainExtName} objects. <br/>
 * As XML, it is represented by a <relDom:available> element containing a number
 * of <relDom:name> elements.
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
public class EPPRelatedDomainExtAvailable implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPRelatedDomainExtAvailable.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Element tag name for available
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":available";

	/**
	 * List of domain names represented by the {@link EPPRelatedDomainExtName} to
	 * be associated with the <relDom:available> element
	 */
	private List<EPPRelatedDomainExtName> availableDomains = null;


	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtAvailable () {
	}


	/**
	 * Constructor with a list of available domain names.
	 * 
	 * @param aAvailableDomains
	 */
	public EPPRelatedDomainExtAvailable(
			final List<EPPRelatedDomainExtName> aAvailableDomains) {
		this.availableDomains = aAvailableDomains;

	}

	/**
	 * A deep clone of the EPPRelatedDomainAvailable
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		final EPPRelatedDomainExtAvailable theClone = new EPPRelatedDomainExtAvailable();

		if (this.availableDomains != null) {
			theClone.availableDomains = new ArrayList();

			for (EPPRelatedDomainExtName availableDomain : this.availableDomains) {

				if (availableDomain != null) {
					theClone.availableDomains
							.add((EPPRelatedDomainExtName) availableDomain
									.clone());
				}
				else {
					theClone.availableDomains.add(null);
				}

			}

		}
		else {
			theClone.availableDomains = null;
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

		this.availableDomains =
				EPPUtil.decodeCompList( aElement, EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtName.ELM_DOMAIN_NAME,
						EPPRelatedDomainExtName.class );
	}


	/**
	 * Append all data from the list of the list of available related domain names
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
					+ " in EPPRelatedDomainExtAvailable.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( final EPPCodecException e ) {
			cat
					.error( "EPPRelatedDomainExtAvailable.encode(): Invalid state on encode: "
							+ e );
			throw new EPPEncodeException(
					"EPPRelatedDomainExtAvailable invalid state: " + e );
		}

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		// dsData
		EPPUtil.encodeCompList( aDocument, root, this.availableDomains );
		return root;
	}


	/**
	 * A deep comparison of this with another EPPRelatedDomainExtAvailable.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals ( final Object aObj ) {

		if ( !(aObj instanceof EPPRelatedDomainExtAvailable) ) {
			return false;
		}

		final EPPRelatedDomainExtAvailable theComp =
				(EPPRelatedDomainExtAvailable) aObj;

		if ( !EPPUtil.equalLists( this.availableDomains, theComp.availableDomains ) ) {
			cat
					.error( "EPPRelatedDomainExtAvailable.equals(): availableDomains not equal" );
			return false;
		}

		return true;
	}

	/**
	 * Adds a available domain represented by
	 * {@link EPPRelatedDomainExtName} to the list of available domains.
	 * 
	 * @param aAvailableDomain Available domain to add to the list.
	 */
	public void addAvailableDomain(EPPRelatedDomainExtName aAvailableDomain) {
		if (this.availableDomains == null) {
			this.availableDomains = new ArrayList<EPPRelatedDomainExtName>();
		}
		
		this.availableDomains.add(aAvailableDomain);
	}
	
	/**
	 * Are there any available domains included in the list of available domains?
	 * 
	 * @return <code>true</code> if the available domain list is not <code>null</code> and
	 *         there is at least one available domain in the list; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasAvailableDomains() {
		if (this.availableDomains != null && this.availableDomains.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the list of available related domain names represented by
	 * {@link EPPRelatedDomainExtName}
	 * 
	 * @return the relatedDomains
	 */
	public List<EPPRelatedDomainExtName> getAvailableDomains () {
		return this.availableDomains;
	}


	/**
	 * Sets the list of available related domain names represented by
	 * {@link EPPRelatedDomainExtName}
	 * 
	 * @param relatedDomains
	 *        the relatedDomains to set
	 */
	public void setAvailableDomains ( final List<EPPRelatedDomainExtName> relatedDomains ) {
		this.availableDomains = relatedDomains;
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
	 * Validate the state of the <code>EPPRelatedDomainExtAvailable</code>
	 * instance. A valid state means that all of the required attributes have been
	 * set. If validateState returns without an exception, the state is valid. If
	 * the state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( (this.availableDomains == null) || (this.availableDomains.size() == 0) ) {
			throw new EPPCodecException(
					"EPPRelatedDomainExtAvailable contains no elements." );
		}

	}

}
