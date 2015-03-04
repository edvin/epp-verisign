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
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.util.EqualityUtil;

/**
 * A Tld object encapsulates a Top Level Domain. This element allows the client
 * to specify the Top Level Domains to use during suggestions creation.
 * 
 * @author jcolosi
 */
public class EPPSuggestionTld implements EPPCodecComponent {

	private static final long serialVersionUID = -8513613587355415900L;

	static final String ELM_NAME = "suggestion:tld";

	private String tld = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionTld () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element
	 * @throws InvalidValueException
	 */
	public EPPSuggestionTld ( final Element aElement )
			throws InvalidValueException {
		decode( aElement );
	}


	/**
	 * Constructor.
	 * 
	 * @param aTld
	 *        a tld
	 */
	public EPPSuggestionTld ( final String aTld ) {
		setTld( aTld );
	}


	/**
	 * Tld getter.
	 * 
	 * @return a tld
	 */
	public String getTld () {
		return tld;
	}


	/**
	 * Tld setter.
	 * 
	 * @param aTld
	 *        a tld
	 */
	public void setTld ( final String aTld ) {
		if ( aTld != null ) {
			this.tld = aTld.toLowerCase();
		}
		else {
			this.tld = aTld;
		}
	}


	/**
	 * A string representation for logging purpose.
	 * 
	 * @return a string
	 */
	public String toLogString () {
		return tld;
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
		return (EPPSuggestionTld) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws InvalidValueException {
		setTld( aElement.getFirstChild().getNodeValue() );
	}


	@Override
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( tld == null ) {
			throw new EPPEncodeException( "Missing required element: \"tld\"" );
		}
		root.appendChild( aDocument.createTextNode( tld ) );
		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionTld other = (EPPSuggestionTld) o;
			if ( !EqualityUtil.equals( this.tld, other.tld ) )
				return false;
			return true;
		}
		return false;
	}

}