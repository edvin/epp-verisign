/***********************************************************
Copyright (C) 2007 VeriSign, Inc.

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


package com.verisign.epp.codec.jobscontact;

import java.util.HashSet;
import java.util.Set;

import com.verisign.epp.codec.gen.*;

import org.w3c.dom.*;

/**
 * The EPPCodec Extension Factory that needs to be configured to encode/decode
 * DotJobs extensions.
 *
 * The DotJobs URI is: http://www.verisign.com/epp/jobsContact-1.0
 */
public class EPPJobsContactExtFactory
    extends EPPExtFactory {

    /** Namespace URI associated with EPPJobsContactExtFactory. */
    public static final String NS = "http://www.verisign.com/epp/jobsContact-1.0";

    /** Namespace prefix associated with EPPJobsContactExtFactory. */
    public static final String NS_PREFIX = "jobsContact";

    /** EPP IDN Lang XML Schema. */
    public static final String NS_SCHEMA =
        "http://www.verisign.com/epp/jobsContact-1.0 jobsContact-1.0.xsd";

    /**
     *  Service object associated with EPPDotJobsExtFactory.  The service
     * object is used when creating the Greeting or the Login.
     */
    private EPPService service;

    /**
     * Create a new instance of EPPDotJobsExtFactory
     */
    public EPPJobsContactExtFactory() {
        service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
        service.setServiceType(EPPService.EXT_SERVICE);
    }

    /**
     * Create protocol extension if applicable.
     * 
     * @param aExtensionElm Element to create protocol extension from 
     * @return Return protocol extension if applicable
     * @throws com.verisign.epp.codec.gen.EPPCodecException When protocol extension is not supported
     */
    public EPPProtocolExtension createProtocolExtension(Element aExtensionElm) throws
        com.verisign.epp.codec.gen.EPPCodecException {
        throw new EPPCodecException(
            "EPPJobsContactExtFactory.createProtocolExtension: Protocol extensions not supported");
    }

    /**
     * Creates the concrete JobsContact ext instance when decoding XML that contains
     * a JobsContact Extension.  
     *
     */

    public EPPCodecComponent createExtension(Element aExtensionElm) throws com.
        verisign.epp.codec.gen.EPPCodecException {

		String name = aExtensionElm.getLocalName();
		
		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}		

        if (name.equals(EPPUtil
				.getLocalName(EPPJobsContactCreateCmd.ELM_NAME))) {
            return new EPPJobsContactCreateCmd();
        }
        else if (name.equals(EPPUtil
				.getLocalName(EPPJobsContactUpdateCmd.ELM_NAME))) {
        		return new EPPJobsContactUpdateCmd();
        }
        else if (name.equals(EPPUtil
				.getLocalName(EPPJobsContactInfoResp.ELM_NAME))) {
    		return new EPPJobsContactInfoResp();
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
		theSchemas.add("jobsContact-1.0.xsd");
		return theSchemas;
	}
    
}