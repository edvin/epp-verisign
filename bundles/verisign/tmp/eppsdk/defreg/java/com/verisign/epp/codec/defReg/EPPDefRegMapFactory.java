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
package com.verisign.epp.codec.defReg;


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
 * <code>EPPDefRegMapFactory</code> represents the <code>EPPCommand</code> and
 * <code>EPPResponseMap</code> factory for the EPP DefReg Mapping with the XML
 * Namespace URI "urn:ietf:params:xmlns:domain".  The fully qualified class
 * name for     <code>EPPDefRegMapFactory</code> is included in a call to
 * <code>EPPFactory.init</code>     or <code>EPPCodec.init</code>.
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPDefRegMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPDefRegMapFactory. */
	public static final String NS = "http://www.nic.name/epp/defReg-1.0";

	/** Namespace prefix associated with EPPDefRegMapFactory. */
	public static final String NS_PREFIX = "defReg";

	/** Namesapce URI associated with the Contact Command Mapping. */
	public static final String NS_CONTACT =
		"urn:ietf:params:xml:ns:contact-1.0";

	/** EPP DefReg XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.nic.name/epp/defReg-1.0 defReg-1.0.xsd";

	/**
	 * XML tag name associated with defReg authorization information. This
	 * value will be passed to the authInfo object when it is initialized in
	 * defReg command mappings.
	 */
	public static final String ELM_DEFREG_AUTHINFO = "defReg:authInfo";

	/** Service description for <code>EPPDefRegMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPDefRegMapFactory</code>.  The service attribute
	 * will be initialized with the XML namespace information defined by the
	 * <code>EPPDefRegMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPDefRegMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPDefRegMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPDefRegMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPDefRegMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);

		service.setServiceType(0);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;defReg:create&gt; must be
	 * the element passed for     a DefReg Create Command.
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

		if (name.equals(EPPUtil.getLocalName(EPPDefRegCheckCmd.ELM_NAME))) {
			return new EPPDefRegCheckCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegInfoCmd.ELM_NAME))) {
			return new EPPDefRegInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegCreateCmd.ELM_NAME))) {
			return new EPPDefRegCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegDeleteCmd.ELM_NAME))) {
			return new EPPDefRegDeleteCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegRenewCmd.ELM_NAME))) {
			return new EPPDefRegRenewCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegUpdateCmd.ELM_NAME))) {
			return new EPPDefRegUpdateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegTransferCmd.ELM_NAME))) {
			return new EPPDefRegTransferCmd();
		}
		else {
			throw new EPPCodecException("Invalid command element " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;defReg:info-data&gt; must
	 * be the element passed for a     DefReg Info Response.
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

		if (name.equals(EPPUtil.getLocalName(EPPDefRegCreateResp.ELM_NAME))) {
			return new EPPDefRegCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegCheckResp.ELM_NAME))) {
			return new EPPDefRegCheckResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegInfoResp.ELM_NAME))) {
			return new EPPDefRegInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegCreateResp.ELM_NAME))) {
			return new EPPDefRegCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegRenewResp.ELM_NAME))) {
			return new EPPDefRegRenewResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPDefRegTransferResp.ELM_NAME))) {
			return new EPPDefRegTransferResp();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPDefRegMapFactory</code>.  The     <code>EPPService</code> is
	 * used by <code>EPPFactory</code> for distributing the     responsibility
	 * of creating concrete <code>EPPCommand</code> and
	 * <code>EPPResponse</code>     objects by XML namespace.  The XML
	 * namespace is defined in the returned <code>EPPService</code>.
	 *
	 * @return service description for the DefReg Command Mapping.
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
		theSchemas.add("defReg-1.0.xsd");
		return theSchemas;
	}
	
}
