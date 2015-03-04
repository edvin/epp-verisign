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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.ExceptionUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * A Token describes a part of the &lt;suggestion:key&gt; element which has been
 * seperated by the Suggestions server. This information is optionally returned
 * by the server.
 * 
 * @author jcolosi
 */
public class EPPSuggestionToken implements EPPCodecComponent {

	private static final long serialVersionUID = 6914427963136166599L;

	private static final Logger LOGGER =
			Logger.getLogger( EPPSuggestionToken.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	private static final String ATT_NAME = "name";

	static final String ELM_NAME = "suggestion:token";

	private String name = null;
	private List<EPPSuggestionRelated> related = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionToken () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aName
	 *        a name
	 */
	public EPPSuggestionToken ( final String aName ) {
		setName( aName );
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionToken ( final Element aElement )
			throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Name getter.
	 * 
	 * @return a name.
	 */
	public String getName () {
		return name;
	}


	/**
	 * Related list getter.
	 * 
	 * @return a related list
	 */
	public List<EPPSuggestionRelated> getRelatedList () {
		return related;
	}


	/**
	 * Name setter.
	 * 
	 * @param aName
	 *        a name
	 */
	public void setName ( final String aName ) {
		this.name = aName;
	}


	/**
	 * Add related value.
	 * 
	 * @param aRelated
	 *        a related value
	 * @throws InvalidValueException
	 */
	public void addRelated ( final EPPSuggestionRelated aRelated )
			throws InvalidValueException {
		if ( aRelated == null )
			throw new InvalidValueException( "Cannot add a null related" );
		if ( related == null )
			resetRelatedList();
		related.add( aRelated );
	}


	/**
	 * Reset the related list.
	 */
	public void resetRelatedList () {
		related = new ArrayList<EPPSuggestionRelated>();
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
		return (EPPSuggestionToken) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		this.name = aElement.getAttribute( ATT_NAME );
		if ( name == null )
			ExceptionUtil.missingDuringDecode( "name" );

		NodeList nodes = aElement.getChildNodes();
		Node node = null;
		resetRelatedList();
		int size = nodes.getLength();
		for ( int i = 0; i < size; i++ ) {
			node = nodes.item( i );
			if ( node instanceof Element ) {
				related.add( new EPPSuggestionRelated( (Element) node ) );
			}
		}
		if ( related.size() == 0 )
			related = null;
	}


	@Override
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( name == null )
			ExceptionUtil.missingDuringEncode( "name" );
		root.setAttribute( ATT_NAME, name );
		if ( related != null )
			EPPUtil.encodeCompList( aDocument, root, related );
		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionToken other = (EPPSuggestionToken) o;
			if ( !EqualityUtil.equals( this.name, other.name ) ) {
				LOGGER.error( "EPPSuggestionToken.equals(): name not equal" );
				return false;
			}
			if ( !EqualityUtil.equals( this.related, other.related ) ) {
				LOGGER.error( "EPPSuggestionToken.equals(): related not equal" );
				return false;
			}
			return true;
		}
		return false;
	}
}