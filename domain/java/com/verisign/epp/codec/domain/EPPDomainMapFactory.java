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
package com.verisign.epp.codec.domain;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * <code>EPPDomainMapFactory</code> represents the <code>EPPCommand</code> and
 * <code>EPPResponseMap</code> factory for the EPP Domain Mapping with the XML
 * Namespace URI "urn:ietf:params:xmlns:domain".  The fully qualified class
 * name for     <code>EPPDomainMapFactory</code> is included in a call to
 * <code>EPPFactory.init</code>     or <code>EPPCodec.init</code>. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPDomainMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPDomainMapFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:domain-1.0";

	/** Namespace prefix associated with EPPDomainMapFactory. */
	public static final String NS_PREFIX = "domain";

	/** Namesapce URI associated with the Contact Command Mapping. */
	public static final String NS_CONTACT =
		"urn:ietf:params:xml:ns:contact-1.0";

	/** EPP Domain XML Schema. */
	public static final String NS_SCHEMA =
		"urn:ietf:params:xml:ns:domain-1.0 domain-1.0.xsd";

	/**
	 * XML tag name associated with domain authorization information. This
	 * value will be passed to the authInfo object when it is initialized in
	 * domain command mappings.
	 */
	public static final String ELM_DOMAIN_AUTHINFO = "domain:authInfo";

	/** Service description for <code>EPPDomainMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPDomainMapFactory</code>.  The service attribute
	 * will be initialized with the XML namespace information defined by the
	 * <code>EPPDomainMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPDomainMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPDomainMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPDomainMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPDomainMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;domain:create&gt; must be
	 * the element passed for     a Domain Create Command.
	 *
	 * @param aMapElement Mapping Extension EPP XML Element.
	 *
	 * @return Concrete <code>EPPCommand</code> instance associated with
	 * 		   <code>aMapElement</code>.
	 *
	 * @exception EPPCodecException Error creating concrete
	 * 			  <code>EPPCommand</code>
	 */
	public EPPCommand createCommand(Element aMapElement)
							 throws EPPCodecException {
		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		
		
		if (name.equals(EPPUtil.getLocalName(EPPDomainCheckCmd.ELM_NAME))) {
			return new EPPDomainCheckCmd();
		}

		else if (name.equals(EPPUtil.getLocalName(EPPDomainInfoCmd.ELM_NAME))) {
			return new EPPDomainInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainCreateCmd.ELM_NAME))) {
			return new EPPDomainCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainDeleteCmd.ELM_NAME))) {
			return new EPPDomainDeleteCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainRenewCmd.ELM_NAME))) {
			return new EPPDomainRenewCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainUpdateCmd.ELM_NAME))) {
			return new EPPDomainUpdateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainTransferCmd.ELM_NAME))) {
			return new EPPDomainTransferCmd();
		}
		else {
			throw new EPPCodecException("Invalid command type " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;domain:info-data&gt; must
	 * be the element passed for a     Domain Info Response.
	 *
	 * @param aMapElement Mapping Extension EPP XML Element.
	 *
	 * @return Concrete <code>EPPResponse</code> instance associated with
	 * 		   <code>aMapElement</code>.
	 *
	 * @exception EPPCodecException Error creating concrete
	 * 			  <code>EPPResponse</code>
	 */
	public EPPResponse createResponse(Element aMapElement)
							   throws EPPCodecException {
		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		

		if (name.equals(EPPUtil.getLocalName(EPPDomainCheckResp.ELM_NAME))) {
			return new EPPDomainCheckResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainInfoResp.ELM_NAME))) {
			return new EPPDomainInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainCreateResp.ELM_NAME))) {
			return new EPPDomainCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainRenewResp.ELM_NAME))) {
			return new EPPDomainRenewResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainTransferResp.ELM_NAME))) {
			return new EPPDomainTransferResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDomainPendActionMsg.ELM_NAME))) {
			return new EPPDomainPendActionMsg();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPDomainMapFactory</code>.  The     <code>EPPService</code> is
	 * used by <code>EPPFactory</code> for distributing the     responsibility
	 * of creating concrete <code>EPPCommand</code> and
	 * <code>EPPResponse</code>     objects by XML namespace.  The XML
	 * namespace is defined in the returned <code>EPPService</code>.
	 *
	 * @return service description for the Domain Command Mapping.
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
		theSchemas.add("domain-1.0.xsd");
		return theSchemas;
	}
	
	
}
