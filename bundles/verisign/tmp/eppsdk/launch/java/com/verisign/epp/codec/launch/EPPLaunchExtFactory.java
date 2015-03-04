/***********************************************************
Copyright (C) 2012 VeriSign, Inc.

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
 ***********************************************************/

package com.verisign.epp.codec.launch;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPExtFactory;
import com.verisign.epp.codec.gen.EPPProtocolExtension;
import com.verisign.epp.codec.gen.EPPService;

/**
 * The EPPCodec Extension Factory that needs to be configured to encode/decode
 * LaunchPhase extensions.
 * 
 * The LaunchPhase URI is: http://www.verisign.com/epp/launchInf-1.0
 */
public class EPPLaunchExtFactory extends EPPExtFactory {

	/** Namespace URI associated with EPPLaunchExtFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:launch-1.0";

	/** Namespace prefix associated with EPPLaunchExtFactory. */
	public static final String NS_PREFIX = "launch";

	/** XML Schema definition for EPPLaunchExtFactory */
	public static final String NS_SCHEMA = "urn:ietf:params:xml:ns:launch-1.0 launch-1.0.xsd";

	/**
	 * Service object associated with EPPLaunchExtFactory. The service object is
	 * used when creating the Greeting or the Login.
	 */
	private EPPService service;

	/**
	 * Create a new instance of EPPLaunchExtFactory
	 */
	public EPPLaunchExtFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
		service.setServiceType(EPPService.EXT_SERVICE);
	}

	/**
	 * Overridden but doesn't do anything in the context of EPPLaunchExtFactory.
	 * Throws an EPPCodecException if called
	 * 
	 * @param aExtensionElm DOM <code>Element</code> to create protocol extension from
	 * @return Protocol extension
	 * @throws com.verisign.epp.codec.gen.EPPCodecException On error
	 */
	public EPPProtocolExtension createProtocolExtension(Element aExtensionElm)
			throws com.verisign.epp.codec.gen.EPPCodecException {
		throw new EPPCodecException(
				"EPPLaunchExtFactory.createProtocolExtension: Protocol extensions not supported");
	}

	/**
	 * Creates the appropriate EPPLaunchExt object based on the passed DOM
	 * element.
	 * 
	 * @param aExtensionElm
	 *            The DOM element that is a parent to the EPPLaunchExt XML
	 *            fragment
	 * @return A concrete EPPCodecComponet that knows how to decode itself from
	 *         the rest of the DOM document.
	 * @throws com.verisign.epp.codec.gen.EPPCodecException
	 *             Thrown if an unrecognized EPPLaunchExt element is found below
	 *             the passed in extension element
	 */

	public EPPCodecComponent createExtension(Element aExtensionElm)
			throws com.verisign.epp.codec.gen.EPPCodecException {

		String name = aExtensionElm.getLocalName();

		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}

		String localName = aExtensionElm.getLocalName();

		if (localName.equals(EPPLaunchCheck.ELM_LOCALNAME)) {
			return new EPPLaunchCheck();
		}
		else if (localName.equals(EPPLaunchChkData.ELM_LOCALNAME)) {
			return new EPPLaunchChkData();
		}		
		else if (localName.equals(EPPLaunchInfo.ELM_LOCALNAME)) {
			return new EPPLaunchInfo();
		}
		else if (localName.equals(EPPLaunchInfData.ELM_LOCALNAME)) {
			return new EPPLaunchInfData();
		}
		else if (localName.equals(EPPLaunchCreate.ELM_LOCALNAME)) {
			return new EPPLaunchCreate();
		}
		else if (localName.equals(EPPLaunchCreData.ELM_LOCALNAME)) {
			return new EPPLaunchCreData();
		}
		else if (localName.equals(EPPLaunchUpdate.ELM_LOCALNAME)) {
			return new EPPLaunchUpdate();
		}
		else if (localName.equals(EPPLaunchDelete.ELM_LOCALNAME)) {
			return new EPPLaunchDelete();
		}
		else {
			throw new EPPCodecException("Invalid extension element " + name);
		}
	}

	/**
	 * Returns the EPPService instance associated with this ExtFactory. The
	 * EPPService instance contains the XML Namespace and XML Schema location
	 * 
	 * @return the EPPService instance associated with this ExtFactory
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
	 * 
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	public Set getXmlSchemas() {
		Set theSchemas = new HashSet();
		theSchemas.add("launch-1.0.xsd");
		theSchemas.add("signedMark-1.0.xsd");
		theSchemas.add("mark-1.0.xsd");
		theSchemas.add("xmldsig-core-schema.xsd");
		return theSchemas;
	}

}