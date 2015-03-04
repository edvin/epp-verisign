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
 * EPPCodecComponent that encodes and decodes a <relDom:delete> tag associated
 * with a Domain-Delete command.
 * <p>
 * Title: EPP 1.0 Related Domain - delete tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtDelete object represents the collection
 * of domains that must be deleted atomically. As such it is composed of a
 * collection of {@link String} objects. <br/>
 * As XML, it is represented by a <relDom:delete> element containing a number of
 * <relDom:name> elements.
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
public class EPPRelatedDomainExtDelete implements EPPCodecComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7097759695930056421L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtDelete.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * Element tag name for create
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":delete";

	public static final String ELM_DOMAIN_NAME =
			EPPRelatedDomainExtFactory.NS_PREFIX + ":name";

	/**
	 * List of domain names represented by the {@link String} to be associated
	 * with the <relDom:delete> element
	 */
	private List<String> domainNames = null;


	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtDelete () {
	}


	/**
	 * Constructor with a list of domain names to be deleted.
	 * 
	 * @param aDomains
	 */
	public EPPRelatedDomainExtDelete ( final List<String> aDomains ) {
		this.domainNames = aDomains;

	}


	/**
	 * A deep clone of the EPPRelatedDomainExtDelete
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone () throws CloneNotSupportedException {
		final EPPRelatedDomainExtDelete theClone = new EPPRelatedDomainExtDelete();

		
		if ( this.domainNames != null ) {
			theClone.domainNames = new ArrayList<String>();
			
			for (String domainName : this.domainNames) {
				
				if (domainName != null) {
					theClone.domainNames.add((String) domainName);
				}
				else {
					theClone.domainNames.add(null);
				}
				
			}
		}
		else {
			theClone.domainNames = null;
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

		this.domainNames =
				EPPUtil.decodeList( aElement, EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtDelete.ELM_DOMAIN_NAME );
		try {
			validateState();
		}
		catch ( final EPPCodecException e ) {
			throw new EPPDecodeException(
					"Invalid state on EPPRelatedDomainExtDelete.decode: " + e );
		}
	}


	/**
	 * Append all data from the list related domain names to be deleted to given
	 * DOM Document
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
					+ " in EPPRelatedDomainExtDelete.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( final EPPCodecException e ) {
			cat
					.error( "EPPRelatedDomainExtDelete.encode(): Invalid state on encode: "
							+ e );
			throw new EPPEncodeException( "EPPRelatedDomainExtDelete invalid state: "
					+ e );
		}

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );
		EPPUtil.encodeList( aDocument, root, this.domainNames,
				EPPRelatedDomainExtFactory.NS, ELM_DOMAIN_NAME );
		return root;
	}


	/**
	 * A deep comparison of this with another EPPRelatedDomainExtDelete.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals ( final Object aObj ) {

		if ( !(aObj instanceof EPPRelatedDomainExtDelete) ) {
			return false;
		}

		final EPPRelatedDomainExtDelete theComp = (EPPRelatedDomainExtDelete) aObj;

		if ( !EPPUtil.equalLists( this.domainNames, theComp.domainNames ) ) {
			cat
					.error( "EPPRelatedDomainExtDelete.equals(): related domains not equal" );
			return false;
		}

		return true;
	}

	/**
	 * Adds a domain name to be deleted to the list.
	 * 
	 * @param aDomainName Related domain name to add to the list.
	 */
	public void addDomain(String aDomainName) {
		if (this.domainNames == null) {
			this.domainNames = new ArrayList<String>();
		}
		
		this.domainNames.add(aDomainName);

	}

	/**
	 * Are there any domains included in the list of domains?
	 * 
	 * @return <code>true</code> if the domain list is not <code>null</code> and
	 *         there is at least one domain in the list; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasDomains() {
		if (this.domainNames != null && this.domainNames.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Returns the list of related domain names to be deleted
	 * 
	 * @return the relatedDomains
	 */
	public List<String> getDomains () {
		return this.domainNames;
	}


	/**
	 * Sets the list of related domain names to be deleted
	 * 
	 * @param aDomains
	 *        the relatedDomains to set
	 */
	public void setDomains ( final List<String> aDomains ) {
		this.domainNames = aDomains;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString(this);
	}


	/**
	 * Validate the state of the <code>EPPRelatedDomainExtDelete</code> instance.
	 * A valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( (this.domainNames == null) || (this.domainNames.size() == 0) ) {
			throw new EPPCodecException(
					"EPPRelatedDomainExtDelete contains no  elements." );
		}

		for (String domainName : this.domainNames) {
			if ( domainName == null ) {
				throw new EPPCodecException(
						"EPPRelatedDomainExtDelete: name element cannot be null." );
			}
		}
		
	}

}
