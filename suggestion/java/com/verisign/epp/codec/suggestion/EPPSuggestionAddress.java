/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.� See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA� 02111-1307� USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/

package com.verisign.epp.codec.suggestion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;

/**
 * Represents a client address specified in an
 * <code>EPPSuggestionCoordinates</code> object of an
 * <code>EPPSuggestionGeo</code>. An address consists of a name and a type,
 * where type is either <code>EPPSuggestionAddress.IPV4</code> or
 * <code>EPPSuggestionAddress.IPV6</code>. The default type is
 * <code>EPPSuggestionAddress.IPV6</code>. <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 */
public class EPPSuggestionAddress implements EPPCodecComponent {

	private static final long serialVersionUID = -6391796823155355236L;

	/** XML Element Name of <code>EPPSuggestionAddress</code> root element. */
	static final String ELM_NAME = "suggestion:addr";

	/**
	 * XML Element Local Name of <code>EPPSuggestionAddress</code> root element.
	 */
	static final String ELM_LOCALNAME = "addr";

	/** XML Element Name of <code>EPPSuggestionAddress</code> root element. */
	private final static String ELM_ADDRESS_IP = "ip";

	/** IPV4 IP address constant. */
	private final static String ATTR_IPV4 = "v4";

	/** IPV4 IP address constant. This is the default type. */
	public final static short IPV4 = 0;

	/** IPV6 IP address constant. */
	public final static short IPV6 = 1;

	/** IPV6 IP address constant. */
	public final static String ATTR_IPV6 = "v6";

	/** IP address name */
	private String name = null;

	/** Type of the IP address (<code>IPV4</code> or <code>IPV6</code> */
	private short type = IPV4;


	/**
	 * Default constructor for <code>EPPSuggestionAddress</code>. The name
	 * attribute defaults to <code>null</code> and must be set using
	 * <code>setName</code> before invoking <code>encode</code>. The type
	 * defaults to IPV4.
	 */
	public EPPSuggestionAddress () {
		name = null;
		type = IPV4;
	}


	/**
	 * Constructor for <code>EPPSuggestionAddress</code> that the takes the
	 * string name of the IP address with the type set to <code>IPV4</code>.
	 * 
	 * @param aName
	 *        IP address name.
	 */
	public EPPSuggestionAddress ( final String aName ) {
		this.name = aName;
		this.type = IPV4;
	}


	/**
	 * Constructor for <code>EPPSuggestionAddress</code> that the takes the
	 * string name of the IP address along with the type of the IP address using
	 * either the constant <code>EPPSuggestionAddress.IPV4</code> or
	 * <code>EPPSuggestionAddress.IPV6</code>.
	 * 
	 * @param aName
	 *        IP address name.
	 * @param aType
	 *        <code>EPPSuggestionAddress.IPV4</code> or
	 *        <code>EPPSuggestionAddress.IPV6</code> constant.
	 */
	public EPPSuggestionAddress ( final String aName, final short aType ) {
		this.name = aName;
		this.type = aType;
	}


	/**
	 * Gets the addresss name in the format specified by <code>getType</code>.
	 * 
	 * @return Address name <code>String</code> instance if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getName () {
		return name;
	}


	/**
	 * Sets the address name in the format specified by <code>setType</code>.
	 * 
	 * @param aName
	 *        address name
	 */
	public void setName ( final String aName ) {
		name = aName;
	}


	/**
	 * Sets the address name and address type. <code>aType</code> should be
	 * either <code>EPPSuggestionAddress.IPV4</code> or
	 * <code>EPPSuggestionAddress.IPV6</code>, and the address name should
	 * conform to the format of the type.
	 * 
	 * @param aName
	 *        address name
	 * @param aType
	 *        address type ((<code>EPPSuggestionAddress.IPV4</code> or
	 *        <code>EPPSuggestionAddress.IPV6</code>)
	 */
	public void setName ( final String aName, final short aType ) {
		this.name = aName;
		this.type = aType;
	}


	/**
	 * Gets the type of the address name, which should be either the
	 * <code>EPPSuggestionAddress.IPV4</code> or the
	 * <code>EPPSuggestionAddress.IPV6</code> constant.
	 * 
	 * @return Type of the address (<code>EPPSuggestionAddress.IPV4</code> or
	 *         <code>EPPSuggestionAddress.IPV6</code>)
	 */
	public short getType () {
		return this.type;
	}


	/**
	 * Sets the type of the address name to either the
	 * <code>EPPSuggestionAddress.IPV4</code> or the
	 * <code>EPPSuggestionAddress.IPV6</code> constant.
	 * 
	 * @param aType
	 *        <code>IPV4</code> or <code>IPV6</code>
	 */
	public void setType ( final short aType ) {
		this.type = aType;
	}

	/**
	 * Address as string for log purpose.
	 * 
	 * @return a string
	 */
	public String toLogString () {
		return "name="+name+",type="+type;
	}

	@Override
	public Element encode ( final Document aDocument )
			throws EPPEncodeException {
		// Validate state
		if ( name == null ) {
			throw new EPPEncodeException( "required attribute name is not set" );
		}

		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );

		// Create Element by address type (IPV4 or IPV6)
		if ( type == IPV4 ) {
			root.setAttribute( ELM_ADDRESS_IP, ATTR_IPV4 );
		}
		else if ( type == IPV6 ) {
			root.setAttribute( ELM_ADDRESS_IP, ATTR_IPV6 );
		}
		else {
			throw new EPPEncodeException( "Invalid host address type of "
					+ type );
		}

		// Add address name as text node to address type Element
		root.appendChild( aDocument.createTextNode( name ) );

		return root;
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {

		// name
		this.name = aElement.getFirstChild().getNodeValue();

		// type
		String typeStr = aElement.getAttribute( ELM_ADDRESS_IP );

		if ( (typeStr == null) || (typeStr.equals( ATTR_IPV4 )) ) {
			this.type = IPV4;
		}
		else if ( typeStr.equals( ATTR_IPV6 ) ) {
			this.type = IPV6;
		}
		else {
			throw new EPPDecodeException( "Invalid host address type of "
					+ typeStr );
		}
	}


	@Override
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPSuggestionAddress) ) {
			return false;
		}

		EPPSuggestionAddress theComp = (EPPSuggestionAddress) aObject;

		// Type
		if ( this.type != theComp.type ) {
			return false;
		}

		// Name
		if ( !((this.name == null) ? (theComp.name == null) : this.name
				.equals( theComp.name )) ) {
			return false;
		}

		return true;
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		EPPSuggestionAddress clone = null;

		clone = (EPPSuggestionAddress) super.clone();

		return clone;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString( this );
	}

}
