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
import java.util.Set;

import org.w3c.dom.Element;


/**
 * Represents an interface, used by <code>EPPFactory</code>, and implemented by
 * concrete EPP Command Extensions for the creation of concrete
 * <code>EPPCodecComponent</code> and <code>EPPProtocolExtension</code>
 * instances from a command extension XML element.  Each
 * <code>EPPExtFactory</code> is associated with an XML namespace, and is
 * delegated the responsibility by <code>EPPFactory</code> of creating
 * concrete <code>EPPCodecComponent</code> objects representing an EPP extension.<br>
 * <br>
 * For example, a Domain Create Command might include a price extension
 * element, referencing an XML namespace of "urn:verisign:xmlns:pricing" and
 * has a concrete <code>EPPExtFactory</code> named
 * <code>EPPPricingExtFactory</code> that will create all of the pricing
 * extension objects used by <code>EPPCommand</code>'s and
 * <code>EPPResponse</code>'s.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 */
public abstract class EPPExtFactory {
	/**
	 * Creates a concrete <code>EPPCodecComponent</code> from an XML element
	 * that represents an EPP extension. For example, a
	 * <code>EPPPricingQueryExt</code> could be created by
	 * <code>EPPPricingExtFactory</code> given the &ltpricing:query&gt XML
	 * element.
	 *
	 * @param aExtensionElm extension XML element.  For example
	 * 		  &ltpricing:query&gt.
	 *
	 * @return Concrete <code>EPPCodecComponent</code> associated with the
	 * 		   extension XML element. For example,
	 * 		   <code>EPPPricingQueryExt</code> might be associated with an
	 * 		   &ltpricing:query&gt element.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPCodecComponent</code>
	 */
	public abstract EPPCodecComponent createExtension(Element aExtensionElm)
											   throws EPPCodecException;

	/**
	 * Creates a concrete <code>EPPProtocolExtension</code> from an XML element
	 * that represents an EPP protocol extension.
	 *
	 * @param aExtensionElm extension XML element.
	 *
	 * @return Concrete <code>EPPProtocolExtension</code> associated with the
	 * 		   extension XML element.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPProtocolExtension</code>
	 */
	public abstract EPPProtocolExtension createProtocolExtension(Element aExtensionElm)
		throws EPPCodecException;

	/**
	 * Gets the service information associated with the     concrete
	 * <code>EPPExtFactory</code>.  The service information is used by
	 * <code>EPPFactory</code> for extracting the XML namespace     associated
	 * with the extension factory.
	 *
	 * @return service description associated with the concret
	 * 		   <code>EPPExtFactory</code>.
	 */
	public abstract EPPService getService();
	
	/**
	 * Gets the list of XML schemas that need to be pre-loaded into the 
	 * XML Parser.	 
	 * 
	 * @return <code>Set</code> of <code>String</code> XML Schema names that 
	 * should be pre-loaded in the XML Parser.  Defaults to returning <code>null</code> 
	 * to indicate that no XML schemas need to be loaded.
	 */
	public Set getXmlSchemas() {
		return null;
	}
	
}
