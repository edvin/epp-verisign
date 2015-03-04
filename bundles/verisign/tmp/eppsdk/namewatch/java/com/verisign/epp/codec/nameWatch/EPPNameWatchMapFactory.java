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
package com.verisign.epp.codec.nameWatch;


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
 * <code>EPPNameWatchMapFactory</code> represents the <code>EPPCommand</code>
 * and     <code>EPPResponseMap</code> factory for the EPP NameWatch Mapping
 * with the XML     Namespace URI "urn:ietf:params:xmlns:nameWatch".  The
 * fully qualified class name for     <code>EPPNameWatchMapFactory</code> is
 * included in a call to <code>EPPFactory.init</code>     or
 * <code>EPPCodec.init</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPNameWatchMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPNameWatchMapFactory. */
	public static final String NS = "http://www.nic.name/epp/nameWatch-1.0";

	/** Namespace prefix associated with EPPNameWatchMapFactory. */
	public static final String NS_PREFIX = "nameWatch";

	/** Namesapce URI associated with the Contact Command Mapping. */
	public static final String NS_CONTACT =
		"http://www.nic.name/epp/contact-1.0";

	/** EPP NameWatch XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.nic.name/epp/nameWatch-1.0 nameWatch-1.0.xsd";

	/**
	 * XML tag name associated with domain authorization information. This
	 * value will be passed to the authInfo object when it is initialized in
	 * domain command mappings.
	 */
	public static final String ELM_NAMEWATCH_AUTHINFO = "nameWatch:authInfo";

	/** Service description for <code>EPPNameWatchMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPNameWatchMapFactory</code>.  The service
	 * attribute     will be initialized with the XML namespace information
	 * defined by the     <code>EPPNameWatchMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPNameWatchMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPNameWatchMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPNameWatchMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPNameWatchMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
		service.setServiceType(0);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;domain:create&gt; must be
	 * the element passed for     a NameWatch Create Command.
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

		if (name.equals(EPPUtil.getLocalName(EPPNameWatchInfoCmd.ELM_NAME))) {
			return new EPPNameWatchInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchCreateCmd.ELM_NAME))) {
			return new EPPNameWatchCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchDeleteCmd.ELM_NAME))) {
			return new EPPNameWatchDeleteCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchRenewCmd.ELM_NAME))) {
			return new EPPNameWatchRenewCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchUpdateCmd.ELM_NAME))) {
			return new EPPNameWatchUpdateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchTransferCmd.ELM_NAME))) {
			return new EPPNameWatchTransferCmd();
		}
		else {
			throw new EPPCodecException("Invalid command type " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;domain:info-data&gt; must
	 * be the element passed for a     NameWatch Info Response.
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

		if (name.equals(EPPUtil.getLocalName(EPPNameWatchInfoResp.ELM_NAME))) {
			return new EPPNameWatchInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchCreateResp.ELM_NAME))) {
			return new EPPNameWatchCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchRenewResp.ELM_NAME))) {
			return new EPPNameWatchRenewResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPNameWatchTransferResp.ELM_NAME))) {
			return new EPPNameWatchTransferResp();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPNameWatchMapFactory</code>.  The     <code>EPPService</code>
	 * is used by <code>EPPFactory</code> for distributing the responsibility
	 * of creating concrete <code>EPPCommand</code> and
	 * <code>EPPResponse</code>     objects by XML namespace.  The XML
	 * namespace is defined in the returned <code>EPPService</code>.
	 *
	 * @return service description for the NameWatch Command Mapping.
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
		theSchemas.add("nameWatch-1.0.xsd");
		return theSchemas;
	}
	
}
