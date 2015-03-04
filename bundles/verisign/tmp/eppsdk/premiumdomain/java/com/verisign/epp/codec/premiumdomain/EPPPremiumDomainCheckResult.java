/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2007 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */

package com.verisign.epp.codec.premiumdomain;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EqualityUtil;

/**
 * <code>EPPPremiumDomainCheckResult</code> represents the result of an
 * individual premium domain name check. 
 * 
 * The attributes of EPPPremiumDomainCheckResult</code> include the domain name 
 * and a boolean value indicating if the domain name is premium.
 * The <code>domain price</code> must be set before invoking <code>encode</code>
 * if the isPremium flag is set to <code>true</code>.
 */

public class EPPPremiumDomainCheckResult implements EPPCodecComponent {


	private static final long serialVersionUID = 8455818254352957139L;
	
	/**
	 * The constant value for USD
	 */
	public static final String PRICE_UNIT_USD = "USD";

	/**
	 * XML tag name of <code>EPPPremiumDomainCheckResult</code> root element
	 */
	public static final String ELM_NAME = EPPPremiumDomainExtFactory.NS_PREFIX + ":cd";

	/**
	 * XML tag name for <code>name</code> element
	 */
	private static final String ELM_DOMAIN_NAME = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":name";

	/**
	 * XML tag name for <code>price</code> element
	 */
	private static final String ELM_DOMAIN_PRICE = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":price";
	
	/**
	 * XML tag name for <code>renewalPrice</code> element
	 */
	private static final String ELM_DOMAIN_RENEWAL_PRICE = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":renewalPrice";
	
	/**
	 * XML Attribute Name for <code>premium</code> attribute
	 */
	private static final String ATTR_PREMIUM = "premium";
	
	/**
	 * XML Attribute Name for <code>unit</code> attribute
	 */
	private static final String ATTR_PRICE_UNIT = "unit";

	/**
	 * Domain Name associated with result.
	 */
	private String name;

	/**
	 * Is the Domain Name (name) premium?
	 */
	private boolean isPremium = true;

	/**
	 * premium domain price
	 */
	private BigDecimal price = null;

	/**
	 * premium domain renewal price
	 */
	private BigDecimal renewalPrice = null;
	
    /**
     * The price unit
     */
    private String priceUnit = PRICE_UNIT_USD;

	/**
	 * Create an <code>EPPPremiumDomainCheckResp</code> instance
	 */
	public EPPPremiumDomainCheckResult () {
		this.name = null;
		this.isPremium = true;
	}

	/**
	 * Create a <code>EPPPremiumDomainCheckResp</code> instance that will set
	 * the attribute.
	 * 
	 * @param aIsPremium
	 *        isPremium flag
	 */
	public EPPPremiumDomainCheckResult ( String aName, boolean aIsPremium ) {
		this.name = aName;
		this.isPremium = aIsPremium;
	}
	
	/**
	 * Clone <code>EPPPremiumDomainCheckResp</code>.
	 * 
	 * @return clone of <code>EPPPremiumDomainCheckResp</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPPremiumDomainCheckResult clone = (EPPPremiumDomainCheckResult) super
				.clone();

		return clone;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPPremiumDomainCheckResp</code> instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built
	 * @return Element Root DOM Element representing the
	 *         <code>EPPPremiumDomainCheckResp</code> instance.
	 * @exception EPPEncodeException
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPPremiumDomainCheckResp.encode(Document)" );
		}

		Element root = aDocument.createElementNS(
				EPPPremiumDomainExtFactory.NS, ELM_NAME );

		// Domain Name
		Element nameElm = aDocument.createElementNS(
				EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_NAME );
		root.appendChild( nameElm );

		// premium flag
		EPPUtil.encodeBooleanAttr( nameElm, ATTR_PREMIUM, this.isPremium );

		// Name
		Text textNode = aDocument.createTextNode( name );
		nameElm.appendChild( textNode );

		// price
		if ( price != null ) {

			Element priceElm = aDocument.createElementNS(
					EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_PRICE );
			root.appendChild( priceElm );
			
			DecimalFormat decFormat = new DecimalFormat( "#0.00",
					new DecimalFormatSymbols( Locale.US ) );

			Text priceTextNode = aDocument.createTextNode( decFormat
					.format( this.price ) );
			priceElm.appendChild( priceTextNode );

			// price unit
			priceElm.setAttribute( ATTR_PRICE_UNIT, this.priceUnit );		
			
			// renewal price
			Element renewalPriceElm = aDocument.createElementNS(
					EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_RENEWAL_PRICE );
			root.appendChild( renewalPriceElm );
			
			Text renewalPriceTextNode = aDocument.createTextNode( decFormat
					.format( this.renewalPrice ) );
			renewalPriceElm.appendChild( renewalPriceTextNode );

			// price unit
			renewalPriceElm.setAttribute( ATTR_PRICE_UNIT, this.priceUnit );
		}

		return root;
	}


	/**
	 * Decode the <code>EPPPremiumDomainCheckResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode <code>EPPPremiumDomainCheckResp</code>
	 *        from.
	 * @exception EPPDecodeException
	 *            Unable to decode <code>aElement</code>
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		Element theNameElm = EPPUtil.getElementByTagNameNS( aElement,
				EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_NAME );

		// domain name
		this.name = theNameElm.getFirstChild().getNodeValue();

		// premium flag
		this.isPremium = EPPUtil.decodeBooleanAttr( theNameElm, ATTR_PREMIUM );

		// price	
		this.price = EPPUtil.decodeBigDecimal( aElement,
				EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_PRICE );			

		// price unit		
		Element thePriceElm = EPPUtil.getElementByTagNameNS( aElement,
				EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_PRICE );
 
		if ( thePriceElm != null) {
			this.priceUnit = thePriceElm.getAttribute( ATTR_PRICE_UNIT );
		}
		
		// renewal price	
		this.renewalPrice = EPPUtil.decodeBigDecimal( aElement,
				EPPPremiumDomainExtFactory.NS, ELM_DOMAIN_RENEWAL_PRICE );
				
		
	}


	/**
	 * Compare an instance of <code>EPPPremiumDomainCheckResp</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *        Object to compare with.
	 * @return true if equal false otherwise
	 */
	public boolean equals ( Object aObject ) {

		if ( !(aObject instanceof EPPPremiumDomainCheckResult) ) {
			return false;
		}

		EPPPremiumDomainCheckResult theComp = (EPPPremiumDomainCheckResult) aObject;

		// Name
		if ( !(EqualityUtil.equals( this.name, theComp.name )) ) {
			return false;
		}

		// premium flag
		if ( this.isPremium != theComp.isPremium ) {
			return false;
		}

		// price
		if ( !(EqualityUtil.equals( this.price, theComp.price )) ) {			
			return false;
		}

		// renewal price
		if ( !(EqualityUtil.equals( this.renewalPrice, theComp.renewalPrice )) ) {			
			return false;
		}
		
		// price unit
		if ( !(EqualityUtil.equals( this.priceUnit, theComp.priceUnit )) ) {
			return false;
		}
		
		return true;
	}


	/**
	 * Gets the domain name associated with the result.
	 * 
	 * @return Domain name associated with the result if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Sets the domain name associated with the result.
	 * 
	 * @param aName
	 *        Domain Name associated with the result.
	 */
	public void setName ( String aName ) {
		this.name = aName;
	}


	/**
	 * @return the isPremium flag
	 */
	public boolean isPremium () {
		return isPremium;
	}


	/**
	 * @param aIsPremium
	 *        the isPremium to set
	 */
	public void setIsPremium ( boolean aIsPremium ) {
		isPremium = aIsPremium;
	}


	/**
	 * @return the price
	 */
	public BigDecimal getPrice () {
		return price;
	}


	/**
	 * @param aPrice
	 *        the price to set
	 */
	public void setPrice ( BigDecimal aPrice ) {
		price = aPrice;
	}
	

	/**
	 * @return the renewalPrice
	 */
	public BigDecimal getRenewalPrice () {
		return renewalPrice;
	}

	/**
	 * @param aRenewalPrice the renewalPrice to set
	 */
	public void setRenewalPrice ( BigDecimal aRenewalPrice ) {
		renewalPrice = aRenewalPrice;
	}

	/**
	 * @return Unit associated with the price;
	 *         <code>null</code> otherwise.
	 */
	public String getPriceUnit () {
		return this.priceUnit;
	}
	
	/**
	 * @param aPriceUnit
	 *        Unit associated with the price.
	 */
	public void setPriceUnit ( String aPriceUnit ) {
		this.priceUnit = aPriceUnit;
	}

}
