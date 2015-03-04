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
package com.verisign.epp.codec.gen;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;


/**
 * <code>EPPTestGenMapFactory</code> is a test map factory used to test the
 * general (gen) EPP package.  The fully qualified class name for
 * <code>EPPTestGenMapFactory</code> is included in a call to
 * <code>EPPFactory.init</code> or <code>EPPCodec.init</code>. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPTestGenMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPTestGenMapFactory. */
	public static final String NS = "http://www.verisign.com/epp/gen/test";

	/** Namespace prefix associated with EPPTestGenMapFactory. */
	public static final String NS_PREFIX = "test";

	/** EPP Domain XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.verisign.com/epp/gen/test test-1.0.xsd";

	/** Service description for <code>EPPTestGenMapFactory</code> */
	private EPPService service = null;

	/**
	 * Allocates a new <code>EPPTestGenMapFactory</code>.  The service
	 * attribute     will be initialized with the XML namespace information
	 * defined by the     <code>EPPTestGenMapFactory</code> constants:<br>
	 * 
	 * <ul>
	 * <li>
	 * NS_PREFIX    Namespace prefix of <code>EPPTestGenMapFactory</code>
	 * </li>
	 * <li>
	 * NS            Namespace URI of <code>EPPTestGenMapFactory</code>
	 * </li>
	 * <li>
	 * NS_SCHEMA    Namespace schema reference of
	 * <code>EPPTestGenMapFactory</code>
	 * </li>
	 * </ul>
	 */
	public EPPTestGenMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
		service.setServiceType(EPPService.OBJ_SERVICE);
	}

	// End EPPTestGenMapFactory.EPPTestGenMapFactory()

	/**
	 * creates a concrete <code>EPPCommand</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &ltdomain:create&gt must be the
	 * element passed for     a Domain Create Command.
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
		throw new EPPCodecException("EPPTestGenMapFactory does not support any commands!");
	}

	// End EPPTestGenMapFactory.createCommand(Element)

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree.     <code>aMapElement</code> must be the root node for
	 * the command extension.     For example, &ltdomain:info-data&gt must be
	 * the element passed for a     Domain Info Response.
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
		throw new EPPCodecException("EPPTestGenMapFactory does not support any responses!");
	}

	// End EPPTestGenMapFactory.createResponse(Element)

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPTestGenMapFactory</code>.  The     <code>EPPService</code> is
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

	// End EPPTestGenMapFactory.getService()
}
