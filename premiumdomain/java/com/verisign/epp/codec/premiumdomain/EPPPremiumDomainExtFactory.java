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

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPExtFactory;
import com.verisign.epp.codec.gen.EPPProtocolExtension;
import com.verisign.epp.codec.gen.EPPService;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * The EPPCodec Extension Factory that needs to be configured to encode/decode
 * Premium Domain extensions. 
 * 
 * The Premium Domain URI is: http://www.verisign.com/epp/premiumdomain-1.0
 */

public class EPPPremiumDomainExtFactory extends EPPExtFactory {

	/** Namespace URI associated with EPPPremiumDomainExtFactory. */
	public static final String NS = "http://www.verisign.com/epp/premiumdomain-1.0";

	/** Namespace prefix associated with EPPPremiumDomainExtFactory. */
	public static final String NS_PREFIX = "premiumdomain";

	/** EPP IDN Lang XML Schema. */
	public static final String NS_SCHEMA = "http://www.verisign.com/epp/premiumdomain-1.0 premiumdomain-1.0.xsd";

	/**
	 * Service object associated with EPPPremiumDomainExtFactory. The service
	 * object is used when creating the Greeting or the Login.
	 */
	private EPPService service;


	/**
	 * Create a new instance of EPPPremiumDomainExtFactory
	 */
	public EPPPremiumDomainExtFactory () {
		service = new EPPService( NS_PREFIX, NS, NS_SCHEMA );
		service.setServiceType( EPPService.EXT_SERVICE );
	}


	/**
	 * Overridden but doesn't do anything in the context of Premium domain.
	 * Throws an EPPCodecException if called
	 * 
	 * @param aExtensionElm
	 * @return EPPProtocolExtension
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 */
	public EPPProtocolExtension createProtocolExtension ( Element aExtensionElm )
			throws com.verisign.epp.codec.gen.EPPCodecException {
		throw new EPPCodecException(
				"EPPPremiumDomainExtFactory.createProtocolExtension: Protocol extensions not supported" );
	}


	/**
	 * Creates the concrete Premium Domain extension instance when decoding XML
	 * that contains a Premium Domain Extension.
	 * 
	 * @param aExtensionElm
	 *        The DOM element that is a parent to the Premium Domain XML
	 *        fragment
	 * @return A concrete EPPCodecComponet that knows how to decode itself from
	 *         the rest of the DOM document.
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 *         Thrown if an unrecognized Premium Domain element is found below
	 *         the passed in extension element.
	 */
	public EPPCodecComponent createExtension ( Element aExtensionElm )
			throws com.verisign.epp.codec.gen.EPPCodecException {

		String name = aExtensionElm.getLocalName();

		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}		

		if ( name.equals(EPPUtil.getLocalName(EPPPremiumDomainReAssignCmd.ELM_NAME))) {
			return new EPPPremiumDomainReAssignCmd();
		}
		else if ( name.equals(EPPUtil.getLocalName(EPPPremiumDomainCheck.ELM_NAME))) {
			return new EPPPremiumDomainCheck();
		}
		else if ( name.equals(EPPUtil.getLocalName(EPPPremiumDomainCheckResp.ELM_NAME))) {
			return new EPPPremiumDomainCheckResp();
		}
		else {
			throw new EPPCodecException( "Invalid extension element " + name );
		}
	}


	/**
	 * Returns the EPPService instance associated with this ExtFactory. The
	 * EPPService instance contains the XML Namespace and XML Schema location
	 * 
	 * @return the EPPService instance associated with this ExtFactory
	 */
	public EPPService getService () {
		return service;
	}


	/**
	 * Gets the list of XML schemas that need to be pre-loaded into the XML
	 * Parser.
	 * 
	 * @return <code>Set</code> of <code>String</code> XML Schema names that
	 *         should be pre-loaded in the XML Parser.
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	public Set getXmlSchemas () {
		Set theSchemas = new HashSet();
		theSchemas.add( "premiumdomain-1.0.xsd" );
		return theSchemas;
	}

}
