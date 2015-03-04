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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.util.EqualityUtil;

/**
 * The Geo encapsulates all of the configurable aspects of a geolocation data
 * for suggestion request.
 * 
 * @author vraemy
 */
public class EPPSuggestionGeo implements EPPCodecComponent {

	private static final long serialVersionUID = 2706550606966436560L;

	static final String ELM_NAME = "suggestion:geo";

	private EPPSuggestionCoordinates coordinates = null;
	private EPPSuggestionAddress addr = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionGeo () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionGeo ( final Element aElement )
			throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Coordinates getter.
	 * 
	 * @return coordinates.
	 */
	public EPPSuggestionCoordinates getCoordinates () {
		return coordinates;
	}


	/**
	 * Coordinates setter.
	 * 
	 * @param aCoordinates
	 *        coordinates
	 * @throws InvalidValueException
	 */
	public void setCoordinates ( final EPPSuggestionCoordinates aCoordinates )
			throws InvalidValueException {
		this.coordinates = aCoordinates;
	}


	/**
	 * Ip Address getter.
	 * 
	 * @return addr
	 */
	public EPPSuggestionAddress getAddr () {
		return addr;
	}


	/**
	 * Ip Address setter.
	 * 
	 * @param aAddr
	 *        addr
	 * @throws InvalidValueException
	 */
	public void setAddr ( final EPPSuggestionAddress aAddr )
			throws InvalidValueException {
		this.addr = aAddr;
	}


	/**
	 * Return a string representation for logging purpose.
	 * 
	 * @return string
	 */
	public String toLogString () {
		if(this.coordinates != null){
			return coordinates.toLogString();
		}
		else if(this.addr != null){
			return addr.toLogString();
		}
		else{
			return "";
		}
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


	@Override
	public Object clone () throws CloneNotSupportedException {
		return (EPPSuggestionGeo) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		NodeList nodes = aElement.getChildNodes();
		Node node = null;
		int size = nodes.getLength();
		if ( size > 0 ) {
			String name = null;
			for ( int i = 0; i < size; i++ ) {
				node = nodes.item( i );
				if ( node instanceof Element ) {
					name = node.getLocalName();
					if ( name.equals( EPPUtil
							.getLocalName( EPPSuggestionCoordinates.ELM_NAME ) ) ) {
						setCoordinates( new EPPSuggestionCoordinates(
								(Element) node ) );
					}
					else if ( name.equals( EPPUtil
							.getLocalName( EPPSuggestionAddress.ELM_NAME ) ) ) {
						EPPSuggestionAddress tmpAddr =
								new EPPSuggestionAddress();
						tmpAddr.decode( (Element) node );
						setAddr( tmpAddr );
					}
				}
			}
		}
	}


	@Override
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( coordinates != null )
			EPPUtil.encodeComp( aDocument, root, coordinates );
		if ( addr != null )
			EPPUtil.encodeComp( aDocument, root, addr );
		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionGeo other = (EPPSuggestionGeo) o;
			if ( !EqualityUtil.equals( this.coordinates, other.coordinates ) )
				return false;
			if ( !EqualityUtil.equals( this.addr, other.addr ) )
				return false;
			return true;
		}
		return false;
	}
}