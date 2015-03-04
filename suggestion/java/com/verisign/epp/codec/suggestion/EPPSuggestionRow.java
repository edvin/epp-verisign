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
import com.verisign.epp.codec.suggestion.util.StatusEnum;
import com.verisign.epp.codec.suggestion.util.UnsignedShort;
import com.verisign.epp.util.EqualityUtil;

/**
 * A Row associates a Fully Qualified Domain Name with a score and status. This
 * suggestion is returned to the client within a Table response.
 * 
 * @author jcolosi
 */
public class EPPSuggestionRow implements EPPCodecComponent {

	private static final long serialVersionUID = -4719859288987079953L;
	
	 private static final String ATT_NAME = "name";
	 private static final String ATT_SCORE = "score";
	 private static final String ATT_STATUS = "status";
	 private static final String ATT_SOURCE = "source";
	 private static final String ATT_MORELIKETHIS = "morelikethis";
	 private static final String ATT_PPCVALUE = "ppcvalue";
	 private static final String ATT_UNAME = "uName";
	 static final String ELM_NAME = "suggestion:row";

	private String name = null;
	private UnsignedShort score = new UnsignedShort(); // non-null
	private StatusEnum status = new StatusEnum(); // non-null
	private String source = null;
	private String moreLikeThis = null;
	private Integer ppcValue = null;
	private String uName = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionRow () {
	}

	/**
	 * Constructor.
	 * 
	 * @param aElement a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionRow ( final Element aElement ) throws EPPDecodeException {
		decode( aElement );
	}

	/**
	 * Constructor.
	 * 
	 * @param aName a name
	 * @param aScore a score
	 * @param aStatus a status
	 * @throws InvalidValueException
	 */
	public EPPSuggestionRow ( final String aName, final short aScore, final String aStatus )
			throws InvalidValueException {
		this();
		setName( aName );
		setScore( aScore );
		setStatus( aStatus );
	}

	/**
	 * Constructor.
	 * 
	 * @param aName a name
	 * @param aScore a score
	 * @param aStatus a status
	 * @param aUName a unicode name
	 * @throws InvalidValueException
	 */
	public EPPSuggestionRow ( final String aName, final short aScore, final String aStatus,
			final String aUName ) throws InvalidValueException {
		this();
		setName( aName );
		setScore( aScore );
		setStatus( aStatus );
		setUName( aUName );
	}

	/**
	 * Constructor.
	 * 
	 * @param aName a name
	 * @param aScore a score
	 * @param aStatus a status
	 * @param aSource a source
	 * @param aMoreLikeThis a more like this flag
	 * @param aPpcValue a ppc value flag
	 * @throws InvalidValueException
	 */
	public EPPSuggestionRow ( final String aName, final short aScore, final String aStatus,
			final String aSource, final String aMoreLikeThis, final Integer aPpcValue )
			throws InvalidValueException {
		this();
		setName( aName );
		setScore( aScore );
		setStatus( aStatus );
		setSource( aSource );
		setMoreLikeThis( aMoreLikeThis );
		setPpcValue( aPpcValue );
	}

	/**
	 * Constructor.
	 * 
	 * @param aName a ascii name
	 * @param aScore a score
	 * @param aStatus a status
	 * @param aSource a source
	 * @param aMoreLikeThis a more like this flag
	 * @param aPpcValue a ppc value flag
	 * @param aUName a unicode name
	 * @throws InvalidValueException
	 */
	public EPPSuggestionRow ( final String aName, final short aScore, final String aStatus,
			final String aSource, final String aMoreLikeThis, final Integer aPpcValue,
			final String aUName ) throws InvalidValueException {
		this();
		setName( aName );
		setScore( aScore );
		setStatus( aStatus );
		setSource( aSource );
		setMoreLikeThis( aMoreLikeThis );
		setPpcValue( aPpcValue );
		setUName( aUName );
	}


	
	/**
	 * More like this getter.
	 * 
	 * @return more like this value
	 */
	public String getMoreLikeThis () {
		return moreLikeThis;
	}

	/**
	 * Unicode name getter.
	 * @return Unicode name
	 */
	public String getUName () {
		return uName;
	}

	/**
	 * Ascii name getter.
	 * 
	 * @return ascii name
	 */
	public String getName () {
		return name;
	}

	/**
	 * Ppc value getter.
	 * 
	 * @return a ppc value
	 */
	public Integer getPpcValue () {
		return ppcValue;
	}

	/**
	 * Score getter.
	 * 
	 * @return a score
	 */
	public UnsignedShort getScore () {
		return score;
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
	 * Status getter.
	 * 
	 * @return a status
	 */
	public StatusEnum getStatus () {
		return status;
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
	 * More like this setter.
	 * 
	 * @param aMoreLikeThis more like this value
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
	public void setPpcValue ( final Integer aPpcValue ) {
		this.ppcValue = aPpcValue;
	}

	/**
	 * Ppc value setter.
	 * 
	 * @param aPpcValue a ppc value
	 */
	public void setPpcValue ( final String aPpcValue ) {
		if ( aPpcValue != null && aPpcValue.length() > 0 ) {
			this.ppcValue = new Integer( Integer.parseInt( aPpcValue ) );
		}
	}

	/**
	 * Score setter.
	 * 
	 * @param aScore a score
	 * @throws InvalidValueException
	 */
	public void setScore ( final short aScore ) throws InvalidValueException {
		this.score.set( aScore );
	}

	/**
	 * Score setter.
	 * 
	 * @param aScore a score
	 * @throws InvalidValueException
	 */
	public void setScore ( final String aScore ) throws InvalidValueException {
		this.score.set( aScore );
	}

	/**
	 * Score setter.
	 * 
	 * @param aScore a score
	 * @throws InvalidValueException
	 */
	public void setScore ( final UnsignedShort aScore ) {
		this.score = aScore;
	}


	public void setSource ( final String aSource ) {
		this.source = aSource;
	}

	/**
	 * Status setter.
	 * 
	 * @param aStatus a score
	 * @throws InvalidValueException
	 */
	public void setStatus ( final StatusEnum aStatus ) {
		this.status = aStatus;
	}

	/**
	 * Status setter.
	 * 
	 * @param aStatus a score
	 * @throws InvalidValueException
	 */
	public void setStatus ( final String aStatus ) throws InvalidValueException {
		this.status.set( aStatus );
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
		return (EPPSuggestionRow) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		setName( aElement.getAttribute( ATT_NAME ) );
		if ( name == null )
			ExceptionUtil.missingDuringDecode( ATT_NAME );
		setScore( aElement.getAttribute( ATT_SCORE ) );
		if ( !score.isSet() )
			ExceptionUtil.missingDuringDecode( ATT_SCORE );
		setStatus( aElement.getAttribute( ATT_STATUS ) );
		if ( !status.isSet() )
			ExceptionUtil.missingDuringDecode( ATT_STATUS );

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
	}


	@Override
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( name == null )
			ExceptionUtil.missingDuringEncode( ATT_NAME );
		if ( !score.isSet() )
			ExceptionUtil.missingDuringEncode( ATT_SCORE );
		if ( !status.isSet() )
			ExceptionUtil.missingDuringEncode( ATT_STATUS );
		root.setAttribute( ATT_NAME, name );
		root.setAttribute( ATT_SCORE, score.get() + "" );
		root.setAttribute( ATT_STATUS, status + "" );
		if ( isSetSource() )
			root.setAttribute( ATT_SOURCE, source );
		if ( isSetMoreLikeThis() )
			root.setAttribute( ATT_MORELIKETHIS, moreLikeThis );
		if ( isSetUName() )
			root.setAttribute( ATT_UNAME, uName );
		if ( ppcValue != null )
			root.setAttribute( ATT_PPCVALUE, ppcValue + "" );
		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionRow other = (EPPSuggestionRow) o;
			if ( !EqualityUtil.equals( this.name, other.name ) )
				return false;
			if ( !this.score.equals( other.score ) )
				return false;
			if ( !this.status.equals( other.status ) )
				return false;
			if ( !EqualityUtil.equals( this.source, other.source ) )
				return false;
			if ( !EqualityUtil.equals( this.moreLikeThis, other.moreLikeThis ) )
				return false;
			if ( !EqualityUtil.equals( this.uName, other.uName ) )
				return false;
			if ( !EqualityUtil.equals( this.ppcValue, other.ppcValue ) )
				return false;
			return true;
		}
		return false;
	}

	
}