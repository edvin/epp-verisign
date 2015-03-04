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
package com.verisign.epp.codec.contact;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPMapFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPService;
import com.verisign.epp.codec.gen.EPPUtil;


/**
 * <code>EPPContactMapFactory</code> represents the <code>EPPCommand</code> and
 * <code>EPPResponse</code> factory for the EPP Contact Mapping with the XML
 * Namespace URI "urn:iana:xmlns:contact".  The fully qualified class name for
 * <code>EPPContactMapFactory</code> is included in a call to
 * <code>EPPFactory.init</code>     or <code>EPPCodec.init</code>. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPContactMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPContactMapFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:contact-1.0";

	/** Namespace prefix associated with EPPContactMapFactory. */
	public static final String NS_PREFIX = "contact";

	/** EPP Contact XML Schema. */
	public static final String NS_SCHEMA =
		"urn:ietf:params:xml:ns:contact-1.0 contact-1.0.xsd";

	/**
	 * XML tag name associated with contact authorization information. This
	 * value will be passed to the authInfo object when it is initialized in
	 * contact command mappings.
	 */
	public static final String ELM_CONTACT_AUTHINFO = "contact:authInfo";

	/** Service description for <code>EPPContactMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPContactMapFactory</code>.  The service
	 * attribute     will be initialized with the XML namespace information
	 * defined by the     <code>EPPContactMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPContactMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPContactMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPContactMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPContactMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;contact:create&gt; must be
	 * the element passed for     a Contact Create Command.
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

		if (name.equals(EPPUtil.getLocalName(EPPContactCheckCmd.ELM_NAME))) {
			return new EPPContactCheckCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactInfoCmd.ELM_NAME))) {
			return new EPPContactInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactCreateCmd.ELM_NAME))) {
			return new EPPContactCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactDeleteCmd.ELM_NAME))) {
			return new EPPContactDeleteCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactUpdateCmd.ELM_NAME))) {
			return new EPPContactUpdateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactTransferCmd.ELM_NAME))) {
			return new EPPContactTransferCmd();
		}
		else {
			throw new EPPCodecException("Invalid command element " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;contact:info-data&gt; must
	 * be the element passed for a     Contact Info Response.
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

		if (name.equals(EPPUtil.getLocalName(EPPContactCheckResp.ELM_NAME))) {
			return new EPPContactCheckResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactInfoResp.ELM_NAME))) {
			return new EPPContactInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactTransferResp.ELM_NAME))) {
			return new EPPContactTransferResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactCreateResp.ELM_NAME))) {
			return new EPPContactCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPContactPendActionMsg.ELM_NAME))) {
			return new EPPContactPendActionMsg();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPContactMapFactory</code>.  The     <code>EPPService</code> is
	 * used by <code>EPPFactory</code> for distributing the     responsibility
	 * of creating concrete <code>EPPCommand</code> and
	 * <code>EPPResponse</code>     objects by XML namespace.  The XML
	 * namespace is defined in the returned <code>EPPService</code>.
	 *
	 * @return service description for the Contact Command Mapping.
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
		theSchemas.add("contact-1.0.xsd");
		return theSchemas;
	}
	
}
