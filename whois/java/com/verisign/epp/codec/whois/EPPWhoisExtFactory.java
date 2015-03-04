/***********************************************************
 Copyright (C) 2004 VeriSign, Inc.
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


package com.verisign.epp.codec.whois;

import java.util.HashSet;
import java.util.Set;

import com.verisign.epp.codec.gen.*;

import org.w3c.dom.*;

/**
 * The EPPCodec Extension Factory that needs to be configured to encode/decode
 * Whois extensions.
 *
 * The Whois URI is: http://www.verisign.com/epp/whoisInf-1.0
 */
public class EPPWhoisExtFactory
    extends EPPExtFactory {

    /** Namespace URI associated with EPPWhoisExtFactory. */
    public static final String NS = "http://www.verisign.com/epp/whoisInf-1.0";

    /** Namespace prefix associated with EPPWhoisExtFactory. */
    public static final String NS_PREFIX = "whoisInf";

    /** EPP IDN Lang XML Schema. */
    public static final String NS_SCHEMA =
        "http://www.verisign.com/epp/whoisInf-1.0 whoisInf-1.0.xsd";

    /**
     *  Service object associated with EPPWhoisExtFactory.  The service
     * object is used when creating the Greeting or the Login.
     */
    private EPPService service;

    /**
     * Create a new instance of EPPWhoisExtFactory
     */
    public EPPWhoisExtFactory() {
        service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
        service.setServiceType(EPPService.EXT_SERVICE);
    }

    /**
     * Overridden but doesn't do anything in the context of IDN.  Throws
     * an EPPCodecException if called
     *
     * @param aExtensionElm DOM <code>Element</code> to create protocol extension from
     * @return <code>EPPProtocolExtension</code> if applicable
     * @throws com.verisign.epp.codec.gen.EPPCodecException If protocol extension not supported
     */
    public EPPProtocolExtension createProtocolExtension(Element aExtensionElm) throws
        com.verisign.epp.codec.gen.EPPCodecException {
        throw new EPPCodecException(
            "EPPWhoisExtFactory.createProtocolExtension: Protocol extensions not supported");
    }

    /**
     * Creates the concrete Whois ext instance when decoding XML that contains
     * a Whois Extension.  This is only EPPWhoisLangCreate as of 05/11/04
     *
     * @param aExtensionElm The DOM element that is a parent to the IDN Lang XML fragment
     * @return A concrete EPPCodecComponet that knows how to decode itself from the
     * rest of the DOM document.
     * @throws com.verisign.epp.codec.gen.EPPCodecException Thrown if an unrecognized
     * idn lang element is found below the passed in extension element
     */

    public EPPCodecComponent createExtension(Element aExtensionElm) throws com.
        verisign.epp.codec.gen.EPPCodecException {

		String name = aExtensionElm.getLocalName();

		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}		

        if (name.equals(EPPUtil.getLocalName(EPPWhoisInf.ELM_NAME))) {
            return new EPPWhoisInf();
        }
        else if (name.equals(EPPUtil.getLocalName(EPPWhoisInfData.ELM_NAME))) {
        		return new EPPWhoisInfData();
        }
        else {
            throw new EPPCodecException("Invalid extension element " + name);
        }
    }

    /**
     * Returns the EPPService instance associated with this ExtFactory. The
     * EPPService instance contains the XML Namespace and XML Schema location
     *
     * @return  the EPPService instance associated with this ExtFactory
     */
    public EPPService getService() {
        return service;
    }
    
	/**
	 * Gets the list of XML schemas that need to be pre-loaded into the 
	 * XML Parser.
	 *
	 * @return <code>Set</code> of <code>String</code> XML Schema names that 
	 * should be pre-loaded in the XML Parser.
	 *   
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	public Set getXmlSchemas() {
		Set theSchemas = new HashSet();
		theSchemas.add("whoisInf-1.0.xsd");
		return theSchemas;
	}
    
}