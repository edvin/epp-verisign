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

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.ExceptionUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.util.EqualityUtil;

/**
 * A Coordinates with latitude and longitude as Double. This is used for
 * geolocation suggestions.
 * 
 * @author vraemy
 */
public class EPPSuggestionCoordinates implements EPPCodecComponent {

	private static final long serialVersionUID = -8207657241760578301L;

	private static final String ATT_LAT = "lat";
	private static final String ATT_LNG = "lng";

	static final String ELM_NAME = "suggestion:coordinates";

	private Double lat = null;
	private Double lng = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionCoordinates () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionCoordinates ( final Element aElement )
			throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Constructor.
	 * 
	 * @param aLat
	 *        a latitude
	 * @param aLng
	 *        a longitude
	 * @throws InvalidValueException
	 */
	public EPPSuggestionCoordinates ( final double aLat, final double aLng )
			throws InvalidValueException {
		this();
		setLat( aLat );
		setLng( aLng );

	}


	/**
	 * Latitude Getter.
	 * 
	 * @return a latitude
	 */
	public Double getLat () {
		return lat;
	}


	/**
	 * Longitude getter.
	 * 
	 * @return a longitude
	 */
	public Double getLng () {
		return lng;
	}


	/**
	 * Latitude setter.
	 * 
	 * @param aLat
	 *        a latitude
	 */
	public void setLat ( Double aLat ) {
		this.lat = aLat;
	}


	/**
	 * Longitude setter.
	 * 
	 * @param aLng
	 *        a longitude
	 */
	public void setLng ( Double aLng ) {
		this.lng = aLng;
	}


	/**
	 * Latitude setter.
	 * 
	 * @param aLatAsString
	 *        a latitude as string
	 */
	public void setLat ( final String aLatAsString ) {
		if ( aLatAsString != null && aLatAsString.length() > 0 ) {
			this.lat = new Double( Double.parseDouble( aLatAsString ) );
		}
	}


	/**
	 * Longitude setter.
	 * 
	 * @param aLngAsString
	 *        a longitude as string
	 */
	public void setLng ( final String aLngAsString ) {
		if ( aLngAsString != null && aLngAsString.length() > 0 ) {
			this.lng = new Double( Double.parseDouble( aLngAsString ) );
		}
	}

	/**
	 * Coordinates as string for log purpose.
	 * 
	 * @return a string
	 */
	public String toLogString () {
		return "lat="+lat+",lng="+lng;
	}
	
	@Override
	public Object clone () throws CloneNotSupportedException {
		return (EPPSuggestionCoordinates) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		setLat( aElement.getAttribute( ATT_LAT ) );
		if ( lat == null )
			ExceptionUtil.missingDuringDecode( ATT_LAT );
		setLng( aElement.getAttribute( ATT_LNG ) );
		if ( lng == null )
			ExceptionUtil.missingDuringDecode( ATT_LNG );

	}


	@Override
	public Element encode ( final Document aDocument )
			throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( lat == null )
			ExceptionUtil.missingDuringEncode( ATT_LAT );
		if ( lng == null )
			ExceptionUtil.missingDuringEncode( ATT_LNG );

		root.setAttribute( ATT_LAT, lat.toString() );
		root.setAttribute( ATT_LNG, lng.toString() );

		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionCoordinates other = (EPPSuggestionCoordinates) o;
			if ( !EqualityUtil.equals( this.lat, other.lat ) )
				return false;
			if ( !EqualityUtil.equals( this.lng, other.lng ) )
				return false;

			return true;
		}
		return false;
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