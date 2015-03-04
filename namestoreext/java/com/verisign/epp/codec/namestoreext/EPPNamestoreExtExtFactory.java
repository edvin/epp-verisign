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

package com.verisign.epp.codec.namestoreext;

// W3C Imports
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

//----------------------------------------------
// Imports
//----------------------------------------------
// SDK Imports
import com.verisign.epp.codec.gen.*;


/**
 * Factory for the Namestore Extension objects.  The Namestore Extension only
 * supports command, response extensions with <code>createExtension</code>,
 * but currently does not support protocol extensions with
 * <code>createProtocolExtension</code>.  Calling
 * <code>createProtocolExtension</code> will result in an exception. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 */
public class EPPNamestoreExtExtFactory extends EPPExtFactory {
	/** XML Namespace */
	public static final String NS =
		"http://www.verisign-grs.com/epp/namestoreExt-1.1";

	/** XML Namespace Prefix */
	public static final String NS_PREFIX = "namestoreExt";

	/** EPP Namestore Extension XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.verisign-grs.com/epp/namestoreExt-1.1 namestoreExt-1.1.xsd";

	/**
	 * Service object associated with EPPNamestoreExtExtFactory.  The service
	 * object is used when creating the Greeting or the Login.
	 */
	private EPPService _service;

	/**
	 * Default constructor for <code>EPPNamestoreExtExtFactory</code>.
	 */
	public EPPNamestoreExtExtFactory() {
		_service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
		_service.setServiceType(EPPService.EXT_SERVICE);
	}

	/**
	 * Creates a concrete <code>EPPCodecComponent</code> from an XML element
	 * that represents an EPP extension.
	 *
	 * @param aExtensionElm extension XML element.
	 *
	 * @return Concrete <code>EPPCodecComponent</code> associated with the
	 * 		   extension XML element.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPCodecComponent</code>
	 */
	public EPPCodecComponent createExtension(Element aExtensionElm)
									  throws EPPCodecException {
		String name = aExtensionElm.getLocalName();
		
		if (!aExtensionElm.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid extension type " + name);
		}		
		
		if (name.equals(EPPUtil
				.getLocalName(EPPNamestoreExtNamestoreExt.ELM_NAME))) {
			return new EPPNamestoreExtNamestoreExt();
		}
		else if (name.equals(EPPUtil
				.getLocalName(EPPNamestoreExtNSExtErrData.ELM_NAME))) {
			return new EPPNamestoreExtNSExtErrData();
		}
		else {
			throw new EPPCodecException("Invalid extension element " + name);
		}
	}

	/**
	 * Creates a concrete <code>EPPProtocolExtension</code> from an XML element
	 * that represents an EPP protocol extension.
	 *
	 * @param aExtensionElm extension XML element.
	 *
	 * @return Concrete <code>EPPProtocolExtension</code> associated with the
	 * 		   extension XML element.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPProtocolExtension</code>
	 */
	public EPPProtocolExtension createProtocolExtension(Element aExtensionElm)
												 throws EPPCodecException {
		throw new EPPCodecException("EPPNamestoreExtExtFactory.createProtocolExtension: Protocol extensions not supported");
	}

	/**
	 * Gets the service information associated with the concrete
	 * <code>EPPExtFactory</code>.  The service information is used
	 * by<code>EPPFactory</code> for extracting the XML namespace associated
	 * with the extension factory.
	 *
	 * @return service description associated with the concrete
	 * 		   <code>EPPExtFactory</code>.
	 */
	public EPPService getService() {
		return _service;
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
		theSchemas.add("namestoreExt-1.1.xsd");
		return theSchemas;
	}
	
}
