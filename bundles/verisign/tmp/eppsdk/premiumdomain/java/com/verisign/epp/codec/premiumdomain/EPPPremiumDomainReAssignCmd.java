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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * EPPCodecComponent that encodes and decodes a Premium Domain reassign extension.
 */
public class EPPPremiumDomainReAssignCmd implements EPPCodecComponent {

	private static final long serialVersionUID = 1240018126045066112L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPPremiumDomainReAssignCmd.class.getName(), EPPCatFactory
					.getInstance().getFactory() );

	/**
	 * XML tag name of <code>EPPPremiumDomainReAssignCmd</code> root element
	 */
	public static final String ELM_NAME = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":reassign";

	/**
	 * XML tag name for registrar <code>shortname</code> element
	 */
	private static final String ELM_REGISTRAR_SHORTNAME = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":shortName";

	/**
	 * registarar shortName
	 */
	private String shortName = null;


	/**
	 * Create an <code>EPPPremiumDomainReAssignCmd</code> instance
	 */
	public EPPPremiumDomainReAssignCmd () {
	}


	/**
	 * Create a <code>EPPPremiumDomainReAssignCmd</code> instance that will
	 * set the shortName attribute.
	 * 
	 * @param aShortName
	 *        Registrar shortName
	 */
	public EPPPremiumDomainReAssignCmd ( String aShortName ) {
		this.shortName = aShortName;
	}


	/**
	 * Clone <code>EPPPremiumDomainReAssignCmd</code>.
	 * 
	 * @return clone of <code>EPPPremiumDomainReAssignCmd</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPPremiumDomainReAssignCmd clone = (EPPPremiumDomainReAssignCmd) super
				.clone();

		return clone;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPPremiumDomainReAssignCmd</code> instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built
	 * @return Element Root DOM Element representing the
	 *         <code>EPPPremiumDomainReAssignCmd</code> instance.
	 * @exception EPPEncodeException
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPPremiumDomainReAssignCmd.encode(Document)" );
		}

		// Validate Attributes
		if ( this.shortName == null ) {
			cat
					.info( "EPPPremiumDomainReAssignCmd.encode(): shortName is null" );
			throw new EPPEncodeException( "shortName is not set" );
		}

		Element root = aDocument.createElementNS(
				EPPPremiumDomainExtFactory.NS, ELM_NAME );
		root
				.setAttribute( "xmlns:premiumdomain",
						EPPPremiumDomainExtFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPPremiumDomainExtFactory.NS_SCHEMA );

		// Short Name
		EPPUtil.encodeString( aDocument, root, this.shortName,
				EPPPremiumDomainExtFactory.NS, ELM_REGISTRAR_SHORTNAME );

		return root;
	}


	/**
	 * Decode the <code>EPPPremiumDomainReAssignCmd</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode
	 *        <code>EPPPremiumDomainReAssignCmd</code> from.
	 * @exception EPPDecodeException
	 *            Unable to decode <code>aElement</code>
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		// Short Name
		this.shortName = EPPUtil.decodeString( aElement,
				EPPPremiumDomainExtFactory.NS, ELM_REGISTRAR_SHORTNAME );

		// Validate Attributes
		if ( this.shortName == null || shortName.length() == 0) {
			cat.info( "EPPPremiumDomainCreateResp.decode(): shortName is null" );
			throw new EPPDecodeException( "shortName is not set" );
		}

	}


	/**
	 * Compare an instance of <code>EPPPremiumDomainReAssignCmd</code> with
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

		if ( !(aObject instanceof EPPPremiumDomainReAssignCmd) ) {
			return false;
		}

		EPPPremiumDomainReAssignCmd theComp = (EPPPremiumDomainReAssignCmd) aObject;

		if ( this.shortName == null ) {
			if ( theComp.shortName != null ) {
				return false;
			}
		}
		else if ( !this.shortName.equals( theComp.shortName ) ) {
			return false;
		}
		
		return true;
	}


	/**
	 * @return the shortName
	 */
	public String getShortName () {
		return shortName;
	}


	/**
	 * @param aShortName
	 *        the shortName to set
	 */
	public void setShortName ( String aShortName ) {
		shortName = aShortName;
	}

}
