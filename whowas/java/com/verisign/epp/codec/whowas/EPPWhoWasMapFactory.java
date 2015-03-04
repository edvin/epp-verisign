/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

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

package com.verisign.epp.codec.whowas;

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
 * {@link EPPWhoWasMapFactory} represents the {@link EPPCommand} and
 * {@link EPPResponse} factory for the EPP WhoWas Mapping with the XML Namespace
 * URI "urn:ietf:params:xmlns:whowas". The fully qualified class name for
 * {@link EPPWhoWasMapFactory} is included in a call to
 * <code>EPPFactory.init</code> or <code>EPPCodec.init</code>.<br>
 * <br>
 * 
 * @author Deepak Deshpande
 * @version 1.0
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPWhoWasMapFactory extends EPPMapFactory {

	/** Namespace URI associated with EPPWhoWasMapFactory. */
	public static final String NS = "http://www.verisign.com/epp/whowas-1.0";

	/** Namespace prefix associated with EPPWhoWasMapFactory. */
	public static final String NS_PREFIX = "whowas";

	private static final String XSD_NAME = "whowas-1.0.xsd";

	/** EPP WhoWas XML Schema. */
	public static final String NS_SCHEMA = NS + " " + XSD_NAME;

	/** Service description for {@link EPPWhoWasMapFactory} */
	private EPPService service = null;


	/**
	 * Allocates a new {@link EPPWhoWasMapFactory}. The service attribute will be
	 * initialized with the XML namespace information defined by the
	 * {@link EPPWhoWasMapFactory} constants: <br>
	 * <ul>
	 * <li>NS_PREFIX Namespace prefix of {@link EPPWhoWasMapFactory}</li>
	 * <li>NS Namespace URI of {@link EPPWhoWasMapFactory}</li>
	 * <li>NS_SCHEMA Namespace schema reference of {@link EPPWhoWasMapFactory}</li>
	 * </ul>
	 */
	public EPPWhoWasMapFactory () {
		this.service = new EPPService( NS_PREFIX, NS, NS_SCHEMA );
		this.service.setServiceType( 0 );
	}


	/**
	 * Returns a concrete {@link EPPCommand} from the passed in XML Element tree.
	 * <code>aMapElement</code> must be the root node for the command extension.
	 * For example, &lt;whowas:info&gt; must be the element passed for a WhoWas
	 * Info Command.
	 * 
	 * @param aMapElement
	 *        Mapping Extension EPP XML Element.
	 * @return a concrete {@link EPPCommand} from the passed in XML Element tree.
	 * @exception EPPCodecException
	 *            Error creating concrete {@link EPPCommand}
	 */
	public EPPCommand createCommand ( Element aMapElement )
			throws EPPCodecException {
		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		

		if ( name.equals(EPPUtil.getLocalName(EPPWhoWasInfoCmd.ELM_NAME))) {
			return new EPPWhoWasInfoCmd();
		}
		else {
			throw new EPPCodecException( "Invalid command element " + name );
		}
	}


	/**
	 * Returns a concrete {@link EPPResponse} from the passed in XML Element tree.
	 * <code>aMapElement</code> must be the root node for the command extension.
	 * For example, &lt;whowas:info-data&gt; must be the element passed for a
	 * WhoWas Info Response.
	 * 
	 * @param aMapElement
	 *        Mapping Extension EPP XML Element.
	 * @return a concrete {@link EPPResponse} from the passed in XML Element tree.
	 * @exception EPPCodecException
	 *            Error creating concrete {@link EPPResponse}
	 */
	public EPPResponse createResponse ( Element aMapElement )
			throws EPPCodecException {

		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		

		if ( name.equals(EPPUtil.getLocalName(EPPWhoWasInfoResp.ELM_NAME))) {
			return new EPPWhoWasInfoResp();
		}
		else {
			throw new EPPCodecException( "Invalid response element " + name );
		}
	}


	/**
	 * Returns the {@link EPPService} for the WhoWas Command Mapping and
	 * associated with {@link EPPWhoWasMapFactory}. The {@link EPPService} is used
	 * by {@link com.verisign.epp.codec.gen.EPPFactory} for distributing the
	 * responsibility of creating concrete {@link EPPCommand} and
	 * {@link EPPResponse} objects by XML namespace. The XML namespace is defined
	 * in the returned {@link EPPService}.
	 * 
	 * @return the {@link EPPService} for the WhoWas Command Mapping and
	 *         associated with {@link EPPWhoWasMapFactory}.
	 */
	public EPPService getService () {
		return this.service;
	}


	/**
	 * Returns {@link Set} of {@link String} XML Schema names that should be
	 * pre-loaded in the XML Parser.
	 * 
	 * @return {@link Set} of {@link String} XML Schema names that should be
	 *         pre-loaded in the XML Parser.
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	public Set getXmlSchemas () {
		Set theSchemas = new HashSet();
		theSchemas.add( XSD_NAME );
		return theSchemas;
	}

}
