/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.codec.persreg;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
// Imports
//----------------------------------------------
// SDK Imports
import com.verisign.epp.codec.gen.*;


/**
 * Factory for the Personal Registration Extension objects.  The Personal
 * Registration Extension only supports command, response extensions with
 * <code>createExtension</code>, but currently does not support protocol
 * extensions with <code>createProtocolExtension</code>.  Calling
 * <code>createProtocolExtension</code> will result in an exception. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPPersRegExtFactory extends EPPExtFactory {
	/** XML Namespace */
	public static final String NS = "http://www.nic.name/epp/persReg-1.0";

	/** XML Namespace Prefix */
	public static final String NS_PREFIX = "persReg";

	/** EPP Personal Registration Extension XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.nic.name/epp/persReg-1.0 persReg-1.0.xsd";

	/**
	 * Service object associated with EPPPersRegExtFactory.  The  service
	 * object is used when creating the Greeting or the Login.
	 */
	private EPPService _service;

	/**
	 * Default constructor for <code>EPPPersRegExtFactory</code>.
	 */
	public EPPPersRegExtFactory() {
		_service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
		_service.setServiceType(EPPService.EXT_SERVICE);
	}

	/**
	 * Creates a concrete <code>EPPCodecComponent</code> from an XML element
	 * that represents an EPP extension.
	 *
	 * @param aExtensionElm extension XML element.
	 *
	 * @return Concrete <code>EPPCodecComponent</code> associated  with the
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

		if (name.equals(EPPUtil.getLocalName(EPPPersRegCreate.ELM_NAME))) {
			return new EPPPersRegCreate();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPPersRegCreateData.ELM_NAME))) {
			return new EPPPersRegCreateData();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPPersRegCreateErrData.ELM_NAME))) {
			return new EPPPersRegCreateErrData();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPPersRegInfoData.ELM_NAME))) {
			return new EPPPersRegInfoData();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPPersRegRenewData.ELM_NAME))) {
			return new EPPPersRegRenewData();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPPersRegTransferData.ELM_NAME))) {
			return new EPPPersRegTransferData();
		}
		else {
			throw new EPPCodecException("Invalid extension element " + name);
		}
	}

	/**
	 * Creates a concrete <code>EPPProtocolExtension</code> from an  XML
	 * element that represents an EPP protocol extension.
	 *
	 * @param aExtensionElm extension XML element.
	 *
	 * @return Concrete <code>EPPProtocolExtension</code> associated with  the
	 * 		   extension XML element.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPProtocolExtension</code>
	 */
	public EPPProtocolExtension createProtocolExtension(Element aExtensionElm)
												 throws EPPCodecException {
		throw new EPPCodecException("EPPPersRegExtFactory.createProtocolExtension: Protocol extensions not supported");
	}

	/**
	 * Gets the service information associated with the concrete
	 * <code>EPPExtFactory</code>.  The service information is  used
	 * by<code>EPPFactory</code> for extracting the XML namespace  associated
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
		theSchemas.add("persReg-1.0.xsd");
		return theSchemas;
	}
	
}
