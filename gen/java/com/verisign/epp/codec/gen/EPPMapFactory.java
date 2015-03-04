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
 * Represents an abstract class, used by <code>EPPFactory</code>, and extended
 * by concrete     EPP Command Mappings for the creation of concrte
 * <code>EPPCommand</code> and     <code>EPPResponse</code> instances from a
 * command extension XML element.  Each <code>EPPMapFactory</code>     is
 * associated with an XML namespace, and is delegated the responsibility by
 * <code>EPPFactory</code>     of creating concrete <code>EPPCommand</code>
 * and <code>EPPResponse</code> objects.<br>
 * <br>
 * For example, the Domain Command Mapping has an XML namespace of
 * "urn:iana:xmlns:domain"     and has a concrete <code>EPPMapFactory</code>
 * named <code>EPPDomainMapFactory</code> that will create     all of the
 * domain package objects.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 */
public abstract class EPPMapFactory {
	/** Command type constant associated with the &ltcreate&gt command. */
	public static final String EPP_CREATE = "create";

	/** Command type constant associated with the &ltdelete&gt command. */
	public static final String EPP_DELETE = "delete";

	/** Command type constant associated with the &ltinfo&gt command. */
	public static final String EPP_INFO = "info";

	/** Command type constant associated with the &ltlogin&gt command. */
	public static final String EPP_LOGIN = "login";

	/** Command type constant associated with the &ltlogout&gt command. */
	public static final String EPP_LOGOUT = "logout";

	/** Command type constant associated with the &ltping&gt command. */
	public static final String EPP_CHECK = "check";

	/** Command type constant associated with the &ltrenew&gt command. */
	public static final String EPP_RENEW = "renew";

	/** Command type constant associated with the &lttransfer&gt command. */
	public static final String EPP_TRANSFER = "transfer";

	/** Command type constant associated with the &ltupdate&gt command. */
	public static final String EPP_UPDATE = "update";

	/**
	 * Abstract method that creates a concrete <code>EPPCommand</code> from an
	 * XML element.      For example, a <code>EPPDomainCreateCmd</code> will
	 * be created by <code>EPPDomainMapFactory</code>     given the
	 * &ltdomain:create&gt XML element.
	 *
	 * @param aMapElement command extension XML element.  For example
	 * 		  &ltdomain:create&gt.
	 *
	 * @return Concrete <code>EPPCommand</code> associated with command
	 * 		   extension XML element.      For example,
	 * 		   <code>EPPDomainCreateCmd</code>.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPCommand</code>
	 */
	public abstract EPPCommand createCommand(Element aMapElement)
									  throws EPPCodecException;

	/**
	 * Abstract method that creates a concrete <code>EPPResponse</code> from an
	 * XML element.      For example, a <code>EPPDomainInfoResp</code> will be
	 * created by <code>EPPDomainMapFactory</code>     given the
	 * &ltdomain:info-data&gt XML element.
	 *
	 * @param aMapElement command extension XML element.  For example
	 * 		  &ltdomain:info-data&gt.
	 *
	 * @return Concrete <code>EPPResponse</code> associated with command
	 * 		   extension XML element.      For example,
	 * 		   <code>EPPDomainInfoResp</code>.
	 *
	 * @exception EPPCodecException Error creating the concrete
	 * 			  <code>EPPResponse</code>
	 */
	public abstract EPPResponse createResponse(Element aMapElement)
										throws EPPCodecException;

	/**
	 * Abstract method that gets the service information associated with the
	 * concrete <code>EPPMapFactory</code>.  The service information is used
	 * by     <code>EPPFactory</code> for extracting the XML namespace
	 * associated with the service.
	 *
	 * @return service description associated with the concret
	 * 		   <code>EPPMapFactory</code>.
	 */
	abstract public EPPService getService();
	
	
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
