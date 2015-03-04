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
package com.verisign.epp.codec.coaext;

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
 * Client Object Attribute extensions. The COA URI is:
 * urn:ietf:params:xml:ns:coa-1.0
 * <p>
 * Title: EPP 1.0 COA
 * </p>
 * <p>
 * Description: COA Extension to the EPP SDK
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @author jfaust
 * @version 1.0
 */

public class EPPCoaExtFactory extends EPPExtFactory {

	/** Namespace URI associated with EPPCoaExtFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:coa-1.0";

	/** Namespace prefix associated with EPPCoaExtFactory. */
	public static final String NS_PREFIX = "coa";

	/** EPP COA XML Schema. */
	public static final String NS_SCHEMA = NS + " coa-1.0.xsd";

	/**
	 * Service object associated with EPPCoaExtFactory. The service object is used
	 * when creating the Greeting or the Login.
	 */
	private EPPService service;


	/**
	 * Create a new instance of EPPCoaExtFactory
	 */
	public EPPCoaExtFactory () {
		service = new EPPService( NS_PREFIX, NS, NS_SCHEMA );
		service.setServiceType( EPPService.EXT_SERVICE );
	}


	/**
	 * Overridden but doesn't do anything in the context of Client Object
	 * Attribute. Throws an EPPCodecException if called
	 * 
	 * @param aExtensionElm
	 * @return nothing
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 */
	public EPPProtocolExtension createProtocolExtension ( Element aExtensionElm )
			throws com.verisign.epp.codec.gen.EPPCodecException {
		throw new EPPCodecException(
				"EPPCoaExtFactory.createProtocolExtension: Protocol extensions not supported" );
	}


	/**
	 * Creates the concrete Client Object Attribute ext instance when decoding XML
	 * that contains a Coa Extension. This is only EPPCoaIdCreate as of 01/28/11
	 * 
	 * @param aExtensionElm
	 *        The DOM element that is a parent to the Coa XML fragment
	 * @return A concrete EPPCodecComponet that knows how to decode itself from
	 *         the rest of the DOM document.
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 *         Thrown if an unrecognized coa element is found below the
	 *         passed in extension element
	 */

	public EPPCodecComponent createExtension ( Element aExtensionElm )
			throws com.verisign.epp.codec.gen.EPPCodecException {

		String name = aExtensionElm.getLocalName();

		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}		

		if ( name.equals(EPPUtil
				.getLocalName(EPPCoaExtCreate.ELM_NAME))) {
			return new EPPCoaExtCreate();
		}
		else if ( name.equals(EPPUtil
				.getLocalName(EPPCoaExtUpdate.ELM_NAME))) {
			return new EPPCoaExtUpdate();
		}
		else if ( name.equals(EPPUtil
				.getLocalName(EPPCoaExtInfData.ELM_NAME))) {
			return new EPPCoaExtInfData();
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
		theSchemas.add( "coa-1.0.xsd" );
		return theSchemas;
	}

}