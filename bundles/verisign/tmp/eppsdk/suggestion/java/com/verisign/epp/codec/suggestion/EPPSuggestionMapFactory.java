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
package com.verisign.epp.codec.suggestion;

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
 * <code>EPPSuggestionMapFactory</code> represents the <code>EPPCommand</code>
 * and <code>EPPResponseMap</code> factory for the EPP Suggestion Mapping with
 * the XML Namespace URI "urn:ietf:params:xmlns:suggestion". The fully qualified
 * class name for <code>EPPSuggestionMapFactory</code> is included in a call
 * to <code>EPPFactory.init</code> or <code>EPPCodec.init</code>.<br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPSuggestionMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPSuggestionMapFactory. */
	public static final String NS = "http://www.verisign-grs.com/epp/suggestion-1.1";

	/** Namespace prefix associated with EPPSuggestionMapFactory. */
	public static final String NS_PREFIX = "suggestion";

	/** EPP Suggestion XML Schema. */
	public static final String NS_SCHEMA = "http://www.verisign-grs.com/epp/suggestion-1.1 suggestion-1.1.xsd";

	/** Service description for <code>EPPSuggestionMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPSuggestionMapFactory</code>. The service
	 * attribute will be initialized with the XML namespace information defined
	 * by the <code>EPPSuggestionMapFactory</code> constants: <br>
	 * <ul>
	 * <li>NS_PREFIX Namespace prefix of <code>EPPSuggestionMapFactory</code>
	 * </li>
	 * <li>NS Namespace URI of <code>EPPSuggestionMapFactory</code></li>
	 * <li>NS_SCHEMA Namespace schema reference of
	 * <code>EPPSuggestionMapFactory</code></li>
	 * </ul>
	 */
	public EPPSuggestionMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
		service.setServiceType(0);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree. <code>aMapElement</code> must be the root node for the
	 * command extension. For example, &lt;domain:create&gt; must be the element
	 * passed for a Suggestion Info Command.
	 * 
	 * @param aMapElement
	 *            Mapping Extension EPP XML Element.
	 * @return Concrete <code>EPPCommand</code> instance associated with
	 *         <code>aMapElement</code>.
	 * @exception EPPCodecException
	 *                Error creating concrete <code>EPPCommand</code>
	 */
	public EPPCommand createCommand( final Element aMapElement) throws EPPCodecException {
		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		

		if (name.equals(EPPUtil.getLocalName(EPPSuggestionInfoCmd.ELM_NAME))) {
			return new EPPSuggestionInfoCmd();
		} else {
			throw new EPPCodecException("Invalid command element " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree. <code>aMapElement</code> must be the root node for the
	 * command extension. For example, &lt;domain:info-data&gt; must be the
	 * element passed for a Suggestion Info Response.
	 * 
	 * @param aMapElement
	 *            Mapping Extension EPP XML Element.
	 * @return Concrete <code>EPPResponse</code> instance associated with
	 *         <code>aMapElement</code>.
	 * @exception EPPCodecException
	 *                Error creating concrete <code>EPPResponse</code>
	 */
	public EPPResponse createResponse(final Element aMapElement) throws EPPCodecException {
		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		

		if (name.equals(EPPUtil.getLocalName(EPPSuggestionInfoResp.ELM_NAME))) {
			return new EPPSuggestionInfoResp();
		} else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPSuggestionMapFactory</code>. The <code>EPPService</code> is
	 * used by <code>EPPFactory</code> for distributing the responsibility of
	 * creating concrete <code>EPPCommand</code> and <code>EPPResponse</code>
	 * objects by XML namespace. The XML namespace is defined in the returned
	 * <code>EPPService</code>.
	 * 
	 * @return service description for the Suggestion Command Mapping.
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
	public Set<String> getXmlSchemas() {
		Set<String> theSchemas = new HashSet<String>();
		theSchemas.add("suggestion-1.1.xsd");
		return theSchemas;
	}
	
}
