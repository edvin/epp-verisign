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

http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/

package com.verisign.epp.codec.relateddomainext;

import java.util.Date;

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
 * EPPCodecComponent that encodes and decodes a <relDom:domain> tag sent in a
 * command. This element contains a number of child elements. Not all of them
 * are required for all the commands listed below in which this element is used.
 * <ul>
 * <li>Domain Create
 * <li>Domain Delete
 * <li>Domain Renew
 * <li>Domain Transfer
 * <li>Domain Update
 * </ul>
 * <p>
 * Title: EPP 1.0 Related Domain - domain tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtDomain object represents the collection
 * of domains that must be processed atomically. As XML, it is represented by a
 * <relDom:domain> element.
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
public class EPPRelatedDomainExtDomain implements EPPCodecComponent {
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtDomain.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * 
	 */
	private static final long serialVersionUID = 3151298616180707607L;

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "relDom:name";

	/** XML Element Name of <code>EPPRelatedDomainExtDomain</code> root element. */
	final static String ELM_NAME = "relDom:domain";

	/** XML tag name for the <code>currentExpirationDate</code> attribute. */
	private final static String ELM_CURRENT_EXPIRATION_DATE = "relDom:curExpDate";

	/** XML Element Name of <code>language</code> attribute. */
	final static String ELM_LANG = "relDom:lang";

	/** The fully qualified domain name. */
	private String name = null;

	/**
	 * The auth info associated with the domain
	 */
	private EPPRelatedDomainExtAuthInfo authInfo = null;

	/**
	 * The registration period of the domain
	 */
	private EPPRelatedDomainExtPeriod period = null;

	/**
	 * The current expiration date of the domain in yyyy-MM-dd format.
	 */
	private Date currentExpirationDate = null;

	/**
	 * The language associated with the domain if it is an IDN (International
	 * Domain Name).
	 */
	private String language = null;


	/**
	 * <code>EPPRelatedDomainExtDomain</code> default constructor. Must call
	 * required setter methods before invoking <code>encode</code>, which
	 * <i>may</i> include:<br>
	 * <br>
	 * <ul>
	 * <li>name - <code>setName</code></li>
	 * <li>auth info - <code>setAuthInfo</code></li>
	 * <li>period - <code>setPeriod</code></li>
	 * <li>current expiration date - <code>setCurrentExpirationDate</code></li>
	 * <li>language - <code>setLanguage</code></li>
	 * </ul>
	 */
	public EPPRelatedDomainExtDomain () {
		// Default constructor
	}


	/**
	 * Constructor which takes the name of domain.
	 * 
	 * @param aName
	 *        the domain name
	 */
	public EPPRelatedDomainExtDomain ( final String aName ) {
		this.name = aName;
	}


	/**
	 * Constructor which takes the name of domain and authInfo
	 * 
	 * @param name
	 * @param authInfo
	 */
	public EPPRelatedDomainExtDomain ( final String name,
			final EPPRelatedDomainExtAuthInfo authInfo ) {
		this.name = name;
		this.authInfo = authInfo;
	}


	/**
	 * Constructor which takes the name of domain, authInfo and a language.
	 * 
	 * @param name
	 * @param authInfo
	 * @param aLanguage
	 */
	public EPPRelatedDomainExtDomain ( final String name,
			final EPPRelatedDomainExtAuthInfo authInfo, final String aLanguage ) {
		this.name = name;
		this.authInfo = authInfo;
		this.language = aLanguage;
	}


	/**
	 * Constructor which takes the name of domain, authInfo and registration
	 * period.
	 * 
	 * @param name
	 * @param authInfo
	 * @param period
	 */
	public EPPRelatedDomainExtDomain ( final String name,
			final EPPRelatedDomainExtAuthInfo authInfo,
			final EPPRelatedDomainExtPeriod period ) {
		this.name = name;
		this.authInfo = authInfo;
		this.period = period;
	}


	/**
	 * Constructor which takes the name of domain, authInfo, registration period
	 * and a language.
	 * 
	 * @param name
	 * @param authInfo
	 * @param period
	 * @param aLanguage
	 */
	public EPPRelatedDomainExtDomain ( final String name,
			final EPPRelatedDomainExtAuthInfo authInfo,
			final EPPRelatedDomainExtPeriod period, final String aLanguage ) {
		this.name = name;
		this.authInfo = authInfo;
		this.period = period;
		this.language = aLanguage;
	}


	/**
	 * Constructor which takes the name of domain, current expiration date and
	 * registration period.
	 * 
	 * @param name
	 * @param period
	 * @param currentExpirationDate
	 */
	public EPPRelatedDomainExtDomain ( final String name,
			final Date currentExpirationDate, final EPPRelatedDomainExtPeriod period ) {
		this.name = name;
		this.currentExpirationDate = currentExpirationDate;
		this.period = period;
	}


	/**
	 * Validate the state of this object when it is part of the Domain-Create
	 * command. The name of domain and the auth info are required elements when
	 * used with the Domain-Create command.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForCreate () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}
		if ( !hasAuthInfo() ) {
			cat.error( "authInfo required attribute is not set" );
			throw new EPPCodecException( "authInfo required attribute is not set" );
		}
		return true;

	}


	/**
	 * Validate the state of this object when it is part of the Domain-Transfer
	 * command. The name of domain is required when used with the Domain-Transfer
	 * command.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForTransfer () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}

		return true;

	}


	/**
	 * Validate the state of this object when it is part of the Domain-Renew
	 * command. The name of domain and the current expiration date are required
	 * elements when used with the Domain-Renew command.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForRenew () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}
		if ( !hasCurrentExpirationDate() ) {
			cat.error( "currentExpirationDate required attribute is not set" );
			throw new EPPCodecException(
					"currentExpirationDate required attribute is not set" );
		}
		return true;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRelatedDomainExtDomain</code> instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built. Used as an Element factory.
	 * @return Root DOM Element representing the
	 *         <code>EPPRelatedDomainExtDomain</code> instance.
	 * @exception EPPEncodeException
	 *            Unable to encode <code>EPPRelatedDomainExtDomain</code>
	 *            instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		// Name
		if ( hasName() ) {
			EPPUtil.encodeString( aDocument, root, this.name,
					EPPRelatedDomainExtFactory.NS, ELM_DOMAIN_NAME );
		}

		if ( hasAuthInfo() ) {
			EPPUtil.encodeComp( aDocument, root, this.authInfo );
		}

		if ( hasCurrentExpirationDate() ) {
			EPPUtil.encodeDate( aDocument, root, this.currentExpirationDate,
					EPPRelatedDomainExtFactory.NS, ELM_CURRENT_EXPIRATION_DATE );
		}

		if ( hasPeriod() ) {
			EPPUtil.encodeComp( aDocument, root, this.period );
		}

		if ( hasLanguage() ) {
			EPPUtil.encodeString( aDocument, root, this.language,
					EPPRelatedDomainExtFactory.NS, ELM_LANG );
		}

		return root;
	}


	/**
	 * Decode the <code>EPPRelatedDomainExtDomain</code> attributes from the
	 * aElement DOM Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode <code>EPPRelatedDomainExtDomain</code>
	 *        from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {
		// Name
		this.name =
				EPPUtil.decodeString( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_DOMAIN_NAME );

		this.authInfo =
				(EPPRelatedDomainExtAuthInfo) EPPUtil.decodeComp( aElement,
						EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtAuthInfo.ELM_NAME,
						EPPRelatedDomainExtAuthInfo.class );

		this.period =
				(EPPRelatedDomainExtPeriod) EPPUtil.decodeComp( aElement,
						EPPRelatedDomainExtFactory.NS, EPPRelatedDomainExtPeriod.ELM_NAME,
						EPPRelatedDomainExtPeriod.class );

		this.currentExpirationDate =
				EPPUtil.decodeDate( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_CURRENT_EXPIRATION_DATE );

		this.language =
				EPPUtil
						.decodeString( aElement, EPPRelatedDomainExtFactory.NS, ELM_LANG );

	}


	/**
	 * Compare an instance of <code>EPPRelatedDomainExtDomain</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *        Object to compare with.
	 * @return DOCUMENT ME!
	 */
	@Override
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPRelatedDomainExtDomain) ) {
			return false;
		}

		final EPPRelatedDomainExtDomain theComp =
				(EPPRelatedDomainExtDomain) aObject;

		// Name
		if ( !((this.name == null) ? (theComp.name == null) : this.name
				.equals( theComp.name )) ) {
			cat.error( "name attribute is not equal: this.name = " + this.name
					+ "; theComp.name = " + theComp.name );
			return false;
		}

		if ( !((this.authInfo == null) ? (theComp.authInfo == null) : this.authInfo
				.equals( theComp.authInfo )) ) {
			cat.error( "authInfo attribute is not equal: this.authInfo = "
					+ this.authInfo + "; theComp.authInfo = " + theComp.authInfo );
			return false;
		}

		if ( !((this.period == null) ? (theComp.period == null) : this.period
				.equals( theComp.period )) ) {
			cat.error( "period attribute is not equal: this.period = " + this.period
					+ "; theComp.period = " + theComp.period );
			return false;
		}

		if ( !((this.currentExpirationDate == null) ? (theComp.currentExpirationDate == null)
				: this.currentExpirationDate.equals( theComp.currentExpirationDate )) ) {
			cat
					.error( "currentExpirationDate attribute is not equal: this.currentExpirationDate = "
							+ this.currentExpirationDate.getTime()
							+ "; theComp.currentExpirationDate = "
							+ theComp.currentExpirationDate.getTime() );
			return false;
		}

		if ( !((this.language == null) ? (theComp.language == null) : this.language
				.equals( theComp.language )) ) {
			cat.error( "language attribute is not equal: this.language = "
					+ this.language + "; theComp.language = " + theComp.language );
			return false;
		}
		return true;
	}


	/**
	 * Clone <code>EPPRelatedDomainExtDomain</code>.
	 * 
	 * @return clone of <code>EPPRelatedDomainExtDomain</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	@Override
	public Object clone () throws CloneNotSupportedException {
		final EPPRelatedDomainExtDomain clone =
				(EPPRelatedDomainExtDomain) super.clone();

		return clone;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString( this );
	}


	// End EPPDomainTransferResp.toString()

	/**
	 * Gets the domain name
	 * 
	 * @return Domain Name if set; <code>null</code> otherwise.
	 */
	public String getName () {
		return this.name;
	}


	// End EPPDomainTransferResp.getName()

	/**
	 * Sets the domain name.
	 * 
	 * @param aName
	 *        Domain Name
	 */
	public void setName ( final String aName ) {
		this.name = aName;
	}


	/**
	 * Returns the authInfo
	 * 
	 * @return the authInfo
	 */
	public EPPRelatedDomainExtAuthInfo getAuthInfo () {
		return this.authInfo;
	}


	/**
	 * Sets authInfo value to authInfo
	 * 
	 * @param aAuthInfo
	 *        the authInfo to set
	 */
	public void setAuthInfo ( final EPPRelatedDomainExtAuthInfo aAuthInfo ) {
		this.authInfo = aAuthInfo;
	}


	/**
	 * Returns the period
	 * 
	 * @return the period
	 */
	public EPPRelatedDomainExtPeriod getPeriod () {
		return this.period;
	}


	/**
	 * Sets period value to period
	 * 
	 * @param aPeriod
	 *        the period to set
	 */
	public void setPeriod ( final EPPRelatedDomainExtPeriod aPeriod ) {
		this.period = aPeriod;
	}


	/**
	 * Returns the currentExpirationDate
	 * 
	 * @return the currentExpirationDate
	 */
	public Date getCurrentExpirationDate () {
		return this.currentExpirationDate;
	}


	/**
	 * Sets currentExpirationDate value to currentExpirationDate
	 * 
	 * @param aCurrentExpirationDate
	 *        the currentExpirationDate to set
	 */
	public void setCurrentExpirationDate ( final Date aCurrentExpirationDate ) {
		this.currentExpirationDate = aCurrentExpirationDate;
	}


	/**
	 * Returns the language
	 * 
	 * @return the language
	 */
	public String getLanguage () {
		return this.language;
	}


	/**
	 * Sets language value to language
	 * 
	 * @param aLanguage
	 *        the language to set
	 */
	public void setLanguage ( final String aLanguage ) {
		this.language = aLanguage;
	}


	/**
	 * @return <code>true</code> if {@linkplain #name} is not null.
	 */
	public boolean hasName () {
		return this.name != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #authInfo} is not null.
	 */
	public boolean hasAuthInfo () {
		return this.authInfo != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #period} is not null.
	 */
	public boolean hasPeriod () {
		return this.period != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #currentExpirationDate} is not
	 *         null.
	 */
	public boolean hasCurrentExpirationDate () {
		return this.currentExpirationDate != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #language} is not null.
	 */
	public boolean hasLanguage () {
		return this.language != null;
	}
}
