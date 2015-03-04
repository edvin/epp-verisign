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
package com.verisign.epp.codec.emailFwd;


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
 * <code>EPPEmailFwdMapFactory</code> represents the <code>EPPCommand</code>
 * and     <code>EPPResponseMap</code> factory for the EPP EmailFwd Mapping
 * with the XML     Namespace URI "http://www.nic.name/epp/emailFwd-1.0".  The
 * fully qualified class name for     <code>EPPEmailFwdMapFactory</code> is
 * included in a call to <code>EPPFactory.init</code>     or
 * <code>EPPCodec.init</code>.
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPEmailFwdMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPEmailFwdMapFactory. */
	public static final String NS = "http://www.nic.name/epp/emailFwd-1.0";

	/** Namespace prefix associated with EPPEmailFwdMapFactory. */
	public static final String NS_PREFIX = "emailFwd";

	/** Namesapce URI associated with the Contact Command Mapping. */
	public static final String NS_CONTACT =
		"urn:ietf:params:xml:ns:contact-1.0";

	/** EPP EmailFwd XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.nic.name/epp/emailFwd-1.0 emailFwd-1.0.xsd";

	/**
	 * XML tag name associated with emailFwd authorization information. This
	 * value will be passed to the authInfo object when it is initialized in
	 * emailFwd command mappings.
	 */
	public static final String ELM_EMAILFWD_AUTHINFO = "emailFwd:authInfo";

	/** Service description for <code>EPPEmailFwdMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPEmailFwdMapFactory</code>.  The service
	 * attribute     will be initialized with the XML namespace information
	 * defined by the     <code>EPPEmailFwdMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPEmailFwdMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPEmailFwdMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPEmailFwdMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPEmailFwdMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);

		service.setServiceType(0);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;emailFwd:create&gt; must be
	 * the element passed for     a EmailFwd Create Command.
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

		if (name.equals(EPPUtil.getLocalName(EPPEmailFwdCreateCmd.ELM_NAME))) {
			return new EPPEmailFwdCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdCheckCmd.ELM_NAME))) {
			return new EPPEmailFwdCheckCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdInfoCmd.ELM_NAME))) {
			return new EPPEmailFwdInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdCreateCmd.ELM_NAME))) {
			return new EPPEmailFwdCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdDeleteCmd.ELM_NAME))) {
			return new EPPEmailFwdDeleteCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdRenewCmd.ELM_NAME))) {
			return new EPPEmailFwdRenewCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdUpdateCmd.ELM_NAME))) {
			return new EPPEmailFwdUpdateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdTransferCmd.ELM_NAME))) {
			return new EPPEmailFwdTransferCmd();
		}
		else {
			throw new EPPCodecException("Invalid command element " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;emailFwd:info-data&gt; must
	 * be the element passed for a     EmailFwd Info Response.
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
		
		if (name.equals(EPPUtil.getLocalName(EPPEmailFwdCreateResp.ELM_NAME))) {
			return new EPPEmailFwdCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdCheckResp.ELM_NAME))) {
			return new EPPEmailFwdCheckResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdInfoResp.ELM_NAME))) {
			return new EPPEmailFwdInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdCreateResp.ELM_NAME))) {
			return new EPPEmailFwdCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdRenewResp.ELM_NAME))) {
			return new EPPEmailFwdRenewResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPEmailFwdTransferResp.ELM_NAME))) {
			return new EPPEmailFwdTransferResp();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPEmailFwdMapFactory</code>.  The     <code>EPPService</code> is
	 * used by <code>EPPFactory</code> for distributing the     responsibility
	 * of creating concrete <code>EPPCommand</code> and
	 * <code>EPPResponse</code>     objects by XML namespace.  The XML
	 * namespace is defined in the returned <code>EPPService</code>.
	 *
	 * @return service description for the EmailFwd Command Mapping.
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
		theSchemas.add("emailFwd-1.0.xsd");
		return theSchemas;
	}
	
}
