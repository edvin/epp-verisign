/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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
package com.verisign.epp.codec.secdnsext.v10;

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
 * secDNS Extensions.
 *
 * The secDNS URN is: "urn:ietf:params:xml:ns:secDNS-1.0"
 *
 * <p>Title: EPP 1.0 secDNS </p>
 * <p>Description: secDNS Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: VeriSign</p>
 * @version 1.0
 */

public class EPPSecDNSExtFactory extends EPPExtFactory {

    /** Namespace URI associated with EPPSecDNSExtFactory. */
    public static final String NS = "urn:ietf:params:xml:ns:secDNS-1.0";

    /** Namespace prefix associated with EPPSecDNSExtFactory. */
    public static final String NS_PREFIX = "secDNS";

    /** EPP secDNS XML Schema. */
    public static final String NS_SCHEMA =
        NS + " secDNS-1.0.xsd";

    /**
     *  Service object associated with EPPSecDNSExtFactory.  The service
     * object is used when creating the Greeting or the Login.
     */
    private EPPService service;

    /**
     * Create a new instance of EPPSecDNSExtFactory
     */
    public EPPSecDNSExtFactory() {
        service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
        service.setServiceType(EPPService.EXT_SERVICE);
    }


    /**
     * Overridden but doesn't do anything in the context of secDNS.  Throws
     * an EPPCodecException if called
     *
     * @param aExtensionElm
     * @return <code>EPPProtocolExtension</code> instance if supported
     * @throws com.verisign.epp.codec.gen.EPPCodecException
     */
    public EPPProtocolExtension createProtocolExtension(Element aExtensionElm)
        throws com.verisign.epp.codec.gen.EPPCodecException {
       throw new EPPCodecException("EPPSecDNSExtFactory.createProtocolExtension: Protocol extensions not supported");
    }


    /**
     * Creates the concrete SecDNSExt instance when decoding XML that contains
     * a secDNS Extension.  This is either a EPPSecDNSExtInfData, 
     * EPPSecDNSExtCreate, or EPPSecDNSExtUpdate instance
     *
     * @param aExtensionElm The DOM element that is a parent to the secDNS XML fragment
     * @return A concrete EPPCodecComponet that knows how to decode itself from the
     * rest of the DOM document.
     * @throws com.verisign.epp.codec.gen.EPPCodecException Thrown if an unrecognized
     * secDNS element is found below the passed in extension element
     */
    public EPPCodecComponent createExtension(Element aExtensionElm) throws com.verisign.epp.codec.gen.EPPCodecException {

		String name = aExtensionElm.getLocalName();

		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}		

        if (name.equals(EPPUtil.getLocalName(EPPSecDNSExtInfData.ELM_NAME))) {
			return new EPPSecDNSExtInfData();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPSecDNSExtCreate.ELM_NAME))) {
			return new EPPSecDNSExtCreate();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPSecDNSExtUpdate.ELM_NAME))) {
			return new EPPSecDNSExtUpdate();
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
	 * Gets the list of XML schemas that need to be pre-loaded into the XML
	 * Parser.
	 * 
	 * @return <code>Set</code> of <code>String</code> XML Schema names that
	 *         should be pre-loaded in the XML Parser.
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	public Set getXmlSchemas () {
		Set theSchemas = new HashSet();
		theSchemas.add( "secDNS-1.0.xsd" );
		return theSchemas;
	}
    
}