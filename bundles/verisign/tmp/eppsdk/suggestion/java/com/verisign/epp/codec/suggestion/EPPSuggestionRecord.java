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
import com.verisign.epp.util.EqualityUtil;

/**
 * A Record associates a domain label with Cell objects to be returned in a
 * Grid. Each Cell represents a Top Level Domain and associated scores. Taken
 * together, a Record associates a Domain label with various TLDs and scores.
 * 
 * @author jcolosi
 */
public class EPPSuggestionRecord implements EPPCodecComponent {

	private static final long serialVersionUID = -2508113362329827161L;

	private static final String ATT_NAME = "name";
	private static final String ATT_SOURCE = "source";
	private static final String ATT_MORELIKETHIS = "morelikethis";
	private static final String ATT_UNAME = "uName";
	private static final String ATT_PPCVALUE = "ppcvalue";
	static final String ELM_NAME = "suggestion:record";

	private String name = null;
	private String source = null;
	private String moreLikeThis = null;
	private String uName = null;
	private Integer ppcValue = null;
	private List<EPPSuggestionCell> cells = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionRecord () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element.
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionRecord ( final Element aElement )
			throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Constructor.
	 * 
	 * @param aName
	 *        a name
	 */
	public EPPSuggestionRecord ( final String aName ) {
		this();
		setName( aName );
	}


	/**
	 * Constructor.
	 * 
	 * @param aName
	 *        a name
	 * @param aSource
	 *        a source
	 * @param aMoreLikeThis
	 *        a more like this
	 * @param aPpcValue
	 *        a ppc value
	 */
	public EPPSuggestionRecord ( final String aName, final String aSource,
			final String aMoreLikeThis, final Integer aPpcValue ) {
		this();
		setName( aName );
		setSource( aSource );
		setMoreLikeThis( aMoreLikeThis );
		setPpcValue( aPpcValue );
	}


	/**
	 * Constructor.
	 * 
	 * @param aName
	 *        a name
	 * @param aSource
	 *        a source
	 * @param aMoreLikeThis
	 *        a more like this
	 * @param aPpcValue
	 *        a ppc value
	 * @param aUName
	 *        a unicode name
	 */
	public EPPSuggestionRecord ( final String aName, final String aSource,
			final String aMoreLikeThis, final Integer aPpcValue,
			final String aUName ) {
		this();
		setName( aName );
		setSource( aSource );
		setMoreLikeThis( aMoreLikeThis );
		setPpcValue( aPpcValue );
		setUName( aUName );
	}


	/**
	 * Add a cell.
	 * 
	 * @param aCell
	 *        a cell
	 * @throws InvalidValueException
	 */
	public void addCell ( final EPPSuggestionCell aCell )
			throws InvalidValueException {
		if ( aCell == null )
			throw new InvalidValueException( "Cannot add a null Cell" );
		if ( cells == null )
			resetCells();
		cells.add( aCell );
	}


	/**
	 * Cells getter.
	 * 
	 * @return cells.
	 */
	public List<EPPSuggestionCell> getCells () {
		return cells;
	}


	/**
	 * More like this getter.
	 * 
	 * @return more like this
	 */
	public String getMoreLikeThis () {
		return moreLikeThis;
	}


	/**
	 * Unicode name getter.
	 * 
	 * @return unicode name.
	 */
	public String getUName () {
		return uName;
	}


	/**
	 * Ascii name getter.
	 * 
	 * @return a ascii name
	 */
	public String getName () {
		return name;
	}


	/**
	 * Ppc Value getter.
	 * 
	 * @return a ppc value
	 */
	public Integer getPpcValue () {
		return ppcValue;
	}


	/**
	 * Source getter.
	 * 
	 * @return a source
	 */
	public String getSource () {
		return source;
	}

	/**
	 * Is more like this value set.
	 * 
	 * @return true if it is set
	 */
	public boolean isSetMoreLikeThis () {
		return moreLikeThis != null && moreLikeThis.length() > 0;
	}

	/**
	 * Is uName value set.
	 * 
	 * @return true if it is set
	 */
	public boolean isSetUName () {
		return uName != null && uName.length() > 0;
	}

	/**
	 * Is source value set.
	 * 
	 * @return true if it is set
	 */
	public boolean isSetSource () {
		return source != null && source.length() > 0;
	}

	/**
	 * Reset cells.
	 */
	public void resetCells () {
		cells = new ArrayList<EPPSuggestionCell>();
	}

	/**
	 * More like this setter.
	 * 
	 * @param aMoreLikeThis more like this flag
	 */
	public void setMoreLikeThis ( final String aMoreLikeThis ) {
		this.moreLikeThis = aMoreLikeThis;
	}

	/**
	 * Unicode name setter.
	 * 
	 * @param aUName a unicode name
	 */
	public void setUName ( final String aUName ) {
		this.uName = aUName;
	}

	/**
	 * Ascii name setter.
	 * 
	 * @param aName a ascii name
	 */
	public void setName ( final String aName ) {
		this.name = aName;
	}

	/**
	 * Ppc value setter.
	 * 
	 * @param aPpcValue a ppc value
	 */
	public void setPpcValue ( final int aPpcValue ) {
		this.ppcValue = new Integer( aPpcValue );
	}

	/**
	 * Ppc value setter.
	 * 
	 * @param aPpcValue a ppc value
	 */
	public void setPpcValue ( final Integer aPpcValue ) {
		this.ppcValue = aPpcValue;
	}

	/**
	 * Ppc value setter.
	 * 
	 * @param aPpcValue a ppc value as string
	 */
	public void setPpcValue ( final String aPpcValue ) {
		if ( aPpcValue != null && aPpcValue.length() > 0 ) {
			this.ppcValue = new Integer( Integer.parseInt( aPpcValue ) );
		}
	}

	/**
	 * Source setter.
	 * 
	 * @param aSource a source
	 */
	public void setSource ( final String aSource ) {
		this.source = aSource;
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
		return (EPPSuggestionRecord) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		setName( aElement.getAttribute( ATT_NAME ) );
		if ( name == null )
			ExceptionUtil.missingDuringDecode( ATT_NAME );

		String tmp;
		tmp = aElement.getAttribute( ATT_SOURCE );
		if ( tmp != null && tmp.length() > 0 )
			setSource( tmp );
		tmp = aElement.getAttribute( ATT_MORELIKETHIS );
		if ( tmp != null && tmp.length() > 0 )
			setMoreLikeThis( tmp );
		tmp = aElement.getAttribute( ATT_UNAME );
		if ( tmp != null && tmp.length() > 0 )
			setUName( tmp );

		setPpcValue( aElement.getAttribute( ATT_PPCVALUE ) );

		NodeList nodes = aElement.getChildNodes();
		Node node = null;
		resetCells();
		int size = nodes.getLength();
		for ( int i = 0; i < size; i++ ) {
			node = nodes.item( i );
			if ( node instanceof Element ) {
				cells.add( new EPPSuggestionCell( (Element) node ) );
			}
		}
		if ( cells.size() == 0 )
			cells = null;
	}


	@Override
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( name == null )
			ExceptionUtil.missingDuringEncode( ATT_NAME );
		root.setAttribute( ATT_NAME, name );
		if ( isSetSource() )
			root.setAttribute( ATT_SOURCE, source );
		if ( isSetMoreLikeThis() )
			root.setAttribute( ATT_MORELIKETHIS, moreLikeThis );
		if ( isSetUName() )
			root.setAttribute( ATT_UNAME, uName );
		if ( ppcValue != null )
			root.setAttribute( ATT_PPCVALUE, ppcValue + "" );

		if ( cells != null )
			EPPUtil.encodeCompList( aDocument, root, cells );

		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionRecord other = (EPPSuggestionRecord) o;
			if ( !EqualityUtil.equals( this.name, other.name ) )
				return false;
			if ( !EqualityUtil.equals( this.source, other.source ) )
				return false;
			if ( !EqualityUtil.equals( this.moreLikeThis, other.moreLikeThis ) )
				return false;
			if ( !EqualityUtil.equals( this.uName, other.uName ) )
				return false;
			if ( !EqualityUtil.equals( this.ppcValue, other.ppcValue ) )
				return false;
			if ( !EqualityUtil.equals( this.cells, other.cells ) )
				return false;
			return true;
		}
		return false;
	}

}