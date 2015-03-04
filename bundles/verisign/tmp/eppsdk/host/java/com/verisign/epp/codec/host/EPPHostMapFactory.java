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
package com.verisign.epp.codec.host;


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
 * <code>EPPHostMapFactory</code> represents the <code>EPPCommand</code> and
 * <code>EPPResponse</code> factory for the EPP Host Mapping with the XML
 * Namespace URI "urn:ietf:params:xmlns:host".  The fully qualified class name
 * for     <code>EPPHostMapFactory</code> is included in a call to
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
public class EPPHostMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPHostMapFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:host-1.0";

	/** Namespace prefix associated with EPPHostMapFactory. */
	public static final String NS_PREFIX = "host";

	/** EPP Host XML Schema. */
	public static final String NS_SCHEMA =
		"urn:ietf:params:xml:ns:host-1.0 host-1.0.xsd";

	/** Service description for <code>EPPHostMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPHostMapFactory</code>.  The service attribute
	 * will be initialized with the XML namespace information defined by the
	 * <code>EPPHostMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPHostMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPHostMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPHostMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPHostMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;host:create&gt; must be the
	 * element passed for     a Host Create Command.
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


		if (name.equals(EPPUtil.getLocalName(EPPHostCheckCmd.ELM_NAME))) {
			return new EPPHostCheckCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostInfoCmd.ELM_NAME))) {
			return new EPPHostInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostCreateCmd.ELM_NAME))) {
			return new EPPHostCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostDeleteCmd.ELM_NAME))) {
			return new EPPHostDeleteCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostUpdateCmd.ELM_NAME))) {
			return new EPPHostUpdateCmd();
		}
		else {
			throw new EPPCodecException("Invalid command element " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &lt;host:info-data&gt; must be
	 * the element passed for a     Host Info Response.
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

		if (name.equals(EPPUtil.getLocalName(EPPHostCheckResp.ELM_NAME))) {
			return new EPPHostCheckResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostInfoResp.ELM_NAME))) {
			return new EPPHostInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostCreateResp.ELM_NAME))) {
			return new EPPHostCreateResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPHostPendActionMsg.ELM_NAME))) {
			return new EPPHostPendActionMsg();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPHostMapFactory</code>.  The     <code>EPPService</code> is
	 * used by <code>EPPFactory</code> for distributing the     responsibility
	 * of creating concrete <code>EPPCommand</code> and
	 * <code>EPPResponse</code>     objects by XML namespace.  The XML
	 * namespace is defined in the returned <code>EPPService</code>.
	 *
	 * @return service description for the Host Command Mapping.
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
		theSchemas.add("host-1.0.xsd");
		return theSchemas;
	}
	
}
