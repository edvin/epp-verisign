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
 * Related Domain extensions. The RelatedDomain URI is:
 * http://www.verisign.com/epp/relatedDomain-1.0
 * <p>
 * Title: EPP 1.0 RelatedDomain
 * </p>
 * <p>
 * Description: RelatedDomain Extension to the EPP SDK
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

public class EPPRelatedDomainExtFactory extends EPPExtFactory {

	/** Namespace URI associated with EPPRelatedDomainExtFactory. */
	public static final String NS =
			"http://www.verisign.com/epp/relatedDomain-1.0";

	/** Namespace prefix associated with EPPRelatedDomainExtFactory. */
	public static final String NS_PREFIX = "relDom";

	/** EPP Related Domain XML Schema. */
	public static final String NS_SCHEMA = NS + " relatedDomain-1.0.xsd";

	/**
	 * Service object associated with EPPRelatedDomainExtFactory. The service
	 * object is used when creating the Greeting or the Login.
	 */
	private final EPPService service;


	/**
	 * Create a new instance of EPPRelatedDomainExtFactory
	 */
	public EPPRelatedDomainExtFactory () {
		this.service = new EPPService( NS_PREFIX, NS, NS_SCHEMA );
		this.service.setServiceType( EPPService.EXT_SERVICE );
	}


	/**
	 * Creates the concrete Related Domain ext instance when decoding XML that
	 * contains a Related Domain Extension.
	 * 
	 * @param aExtensionElm
	 *        The DOM element that is a parent to the Related Domain XML fragment
	 * @return A concrete EPPCodecComponet that knows how to decode itself from
	 *         the rest of the DOM document.
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 *         Thrown if an unrecognized related domain element is found below the
	 *         passed in extension element
	 */

	@Override
	public EPPCodecComponent createExtension ( final Element aExtensionElm )
			throws com.verisign.epp.codec.gen.EPPCodecException {

		final String name = aExtensionElm.getLocalName();

		if ( !aExtensionElm.getNamespaceURI().equals( NS ) ) {
			throw new EPPCodecException( "Invalid extension type " + name );
		}

		if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtInfData.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtInfData();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtInfo.ELM_INFO ) ) ) {
			return new EPPRelatedDomainExtInfo();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtCreate.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtCreate();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtCreateResp.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtCreateResp();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtDelete.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtDelete();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtDeleteResp.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtDeleteResp();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtUpdate.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtUpdate();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtRenew.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtRenew();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtRenewResp.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtRenewResp();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtTransfer.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtTransfer();
		}
		else if ( name.equals( EPPUtil
				.getLocalName( EPPRelatedDomainExtTransferResp.ELM_NAME ) ) ) {
			return new EPPRelatedDomainExtTransferResp();
		}
		else {
			throw new EPPCodecException( "Invalid extension element " + name );
		}

	}


	/**
	 * Overridden but doesn't do anything in the context of Related Domain. Throws
	 * an EPPCodecException if called
	 * 
	 * @param aExtensionElm
	 * @return nothing
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 */
	@Override
	public EPPProtocolExtension createProtocolExtension (
			final Element aExtensionElm )
			throws com.verisign.epp.codec.gen.EPPCodecException {
		throw new EPPCodecException(
				"EPPRelatedDomainExtFactory.createProtocolExtension: Protocol extensions not supported" );
	}


	/**
	 * Returns the EPPService instance associated with this ExtFactory. The
	 * EPPService instance contains the XML Namespace and XML Schema location
	 * 
	 * @return the EPPService instance associated with this ExtFactory
	 */
	@Override
	public EPPService getService () {
		return this.service;
	}


	/**
	 * Gets the list of XML schemas that need to be pre-loaded into the XML
	 * Parser.
	 * 
	 * @return <code>Set</code> of <code>String</code> XML Schema names that
	 *         should be pre-loaded in the XML Parser.
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	@Override
	public Set getXmlSchemas () {
		final Set theSchemas = new HashSet();
		theSchemas.add( "relatedDomain-1.0.xsd" );
		return theSchemas;
	}

}