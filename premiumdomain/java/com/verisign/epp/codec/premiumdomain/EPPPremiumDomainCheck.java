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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EqualityUtil;

/**
 * EPPCodecComponent that encodes and decodes a Premium Domain check extension.
 */
public class EPPPremiumDomainCheck implements EPPCodecComponent {

	private static final long serialVersionUID = -6601590245665303702L;

	/**
	 * XML tag name of <code>EPPPremiumDomainCheck</code> root element
	 */
	public static final String ELM_NAME = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":check";

	/**
	 * XML tag name for <code>flag</code> element
	 */
	private static final String ELM_FLAG = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":flag";

	/**
	 * Premium domain check flag, where a <code>true</code> value will have the
	 * <code>EPPPremiumDomainCheckResp</code> extension added to a successful response.
	 * A value of <code>false</code> will have the same result as not adding
	 * the <code>EPPPremiumDomainCheckResp</code> extension to the command.
	 */
	private Boolean flag = new Boolean(false);


	/**
	 * Create an <code>EPPPremiumDomainCheck</code> instance
	 */
	public EPPPremiumDomainCheck () {
	}


    /**
	 * Create a EPPPremiumDomainCheck instance with the flag value
	 * 
	 * @param aFlag
	 *            <code>true</code> to get the <code>EPPPremiumDomainCheckResp</code>
	 *            extension in the response;<code>false</code> otherwise
	 */
	public EPPPremiumDomainCheck ( boolean aFlag ) {
		this.flag = new Boolean(aFlag);
	}

    /**
	 * Create a EPPPremiumDomainCheck instance with the flag value
	 * 
	 * @param aFlag
	 *            <code>true</code> to get the <code>EPPPremiumDomainCheckResp</code>
	 *            extension in the response;<code>false</code> otherwise
	 */
	public EPPPremiumDomainCheck ( Boolean aFlag ) {
		this.flag = aFlag;
	}

	/**
	 * Clone <code>EPPPremiumDomainCheck</code>.
	 * 
	 * @return clone of <code>EPPPremiumDomainCheck</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPPremiumDomainCheck clone = (EPPPremiumDomainCheck) super
				.clone();

		return clone;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPPremiumDomainCheck</code> instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built
	 * @return Element Root DOM Element representing the
	 *         <code>EPPPremiumDomainCheck</code> instance.
	 * @exception EPPEncodeException
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPPremiumDomainCheck.encode(Document)" );
		}

		Element root = aDocument.createElementNS(
				EPPPremiumDomainExtFactory.NS, ELM_NAME );
		root
				.setAttribute( "xmlns:premiumdomain",
						EPPPremiumDomainExtFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPPremiumDomainExtFactory.NS_SCHEMA );

		// Flag
		EPPUtil.encodeBoolean(aDocument, root, this.flag,
				EPPPremiumDomainExtFactory.NS, ELM_FLAG);

		return root;
	}


	/**
	 * Decode the <code>EPPPremiumDomainCheck</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode
	 *        <code>EPPPremiumDomainCheck</code> from.
	 * @exception EPPDecodeException
	 *            Unable to decode <code>aElement</code>
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		// Flag
    	this.flag = EPPUtil.decodeBoolean(aElement, EPPPremiumDomainExtFactory.NS,
				ELM_FLAG);

		if (this.flag == null) {
			throw new EPPDecodeException( ELM_FLAG + " is not set" );			
		}

	}


	/**
	 * Compare an instance of <code>EPPPremiumDomainCheck</code> with
	 * this instance.
	 * 
	 * @param aObject
	 *        Object to compare with.
	 * @return true if equal false otherwise
	 */
	public boolean equals ( Object aObject ) {

		if ( this == aObject ) {
			return true;
		}

		if ( aObject == null )
			return false;

		if ( !(aObject instanceof EPPPremiumDomainCheck) ) {
			return false;
		}

		EPPPremiumDomainCheck theComp = (EPPPremiumDomainCheck) aObject;

		if ( !(EqualityUtil.equals( this.flag, theComp.flag )) ) {
			return false;
		}
		
		return true;
	}

    /**
	 * Returns the flag value
	 * 
	 * @return the flag value
	 */
	public Boolean getFlag() {
		return this.flag;
	}

	/**
	 * Sets the flag Code
	 * 
	 * @param aFlag
	 *            The flag value
	 */
	public void setFlag(Boolean aFlag) {
		this.flag = aFlag;
	}

}
