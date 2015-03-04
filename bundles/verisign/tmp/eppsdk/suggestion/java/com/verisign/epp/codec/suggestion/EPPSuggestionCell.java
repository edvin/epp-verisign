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
 * A Cell is an element within a Record, which in turn is an element within the
 * Grid response. A Cell associates a score and status with a TLD. The
 * encapsulating record further associates a domain with these scores.
 * 
 * @author jcolosi
 */
public class EPPSuggestionCell implements EPPCodecComponent {

	private static final long serialVersionUID = -116005839289398939L;

	private static final String ATT_TLD = "tld";
	private static final String ATT_SCORE = "score";
	private static final String ATT_STATUS = "status";
	private static final String ATT_UTLD = "uTld";
	static final String ELM_NAME = "suggestion:cell";

	private String tld = null;
	private UnsignedShort score = new UnsignedShort(); // non-null
	private StatusEnum status = new StatusEnum(); // non-null
	private String uTld = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionCell () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionCell ( final Element aElement )
			throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Constructor.
	 * 
	 * @param aTld
	 *        a tld
	 * @param aScore
	 *        a score
	 * @param aStatus
	 *        a status
	 * @throws InvalidValueException
	 */
	public EPPSuggestionCell ( final String aTld, final short aScore,
			final String aStatus ) throws InvalidValueException {
		this();
		setTld( aTld );
		setScore( aScore );
		setStatus( aStatus );
	}


	/**
	 * Constructor.
	 * 
	 * @param aTld
	 *        a tld
	 * @param aScore
	 *        a score
	 * @param aStatus
	 *        a status
	 * @param aUTld
	 *        a uTld
	 * @throws InvalidValueException
	 */
	public EPPSuggestionCell ( final String aTld, final short aScore,
			final String aStatus, final String aUTld )
			throws InvalidValueException {
		this( aTld, aScore, aStatus );
		setUTld( aUTld );
	}


	/**
	 * Tld Getter.
	 * 
	 * @return a tld
	 */
	public String getTld () {
		return tld;
	}


	/**
	 * uTld Getter.
	 * 
	 * @return a tld
	 */
	public String getUTld () {
		return uTld;
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
	 * Status getter.
	 * 
	 * @return status
	 */
	public StatusEnum getStatus () {
		return status;
	}


	/**
	 * Tld setter.
	 * 
	 * @param aTld
	 *        a tld
	 */
	public void setTld ( final String aTld ) {
		this.tld = aTld;
	}


	/**
	 * uTld setter.
	 * 
	 * @param aUTld
	 *        a uTld
	 */
	public void setUTld ( final String aUTld ) {
		this.uTld = aUTld;
	}


	/**
	 * Score setter.
	 * 
	 * @param aScore
	 *        a score
	 * @throws InvalidValueException
	 */
	public void setScore ( final short aScore ) throws InvalidValueException {
		this.score.set( aScore );
	}


	/**
	 * Score setter.
	 * 
	 * @param aScoreAsString
	 *        a score as string
	 * @throws InvalidValueException
	 */
	public void setScore ( final String aScoreAsString )
			throws InvalidValueException {
		this.score.set( aScoreAsString );
	}


	/**
	 * Score setter.
	 * 
	 * @param aScore
	 *        a score
	 * @throws InvalidValueException
	 */
	public void setScore ( final UnsignedShort aScore ) {
		this.score = aScore;
	}


	/**
	 * Status setter.
	 * 
	 * @param aStatus
	 *        a status
	 */
	public void setStatus ( final StatusEnum aStatus ) {
		this.status = aStatus;
	}


	/**
	 * Status setter.
	 * 
	 * @param aStatusAsString
	 *        a status as string
	 */
	public void setStatus ( final String aStatusAsString )
			throws InvalidValueException {
		this.status.set( aStatusAsString );
	}


	/**
	 * Is uTld value set.
	 * 
	 * @return true if it is set
	 */
	public boolean isSetUTld () {
		return uTld != null && uTld.length() > 0;
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
		return (EPPSuggestionCell) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		setTld( aElement.getAttribute( ATT_TLD ) );
		if ( tld == null )
			ExceptionUtil.missingDuringDecode( ATT_TLD );
		setScore( aElement.getAttribute( ATT_SCORE ) );
		if ( !score.isSet() )
			ExceptionUtil.missingDuringDecode( ATT_SCORE );
		setStatus( aElement.getAttribute( ATT_STATUS ) );
		if ( !status.isSet() )
			ExceptionUtil.missingDuringDecode( ATT_STATUS );
		String tmp = aElement.getAttribute( ATT_UTLD );
		if ( tmp != null && tmp.length() > 0 )
			setUTld( tmp );
	}


	@Override
	public Element encode ( final Document aDocument )
			throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( tld == null )
			ExceptionUtil.missingDuringEncode( ATT_TLD );
		if ( !score.isSet() )
			ExceptionUtil.missingDuringEncode( ATT_SCORE );
		if ( !status.isSet() )
			ExceptionUtil.missingDuringEncode( ATT_STATUS );
		root.setAttribute( ATT_TLD, tld );
		root.setAttribute( ATT_SCORE, score.get() + "" );
		root.setAttribute( ATT_STATUS, status + "" );
		if ( isSetUTld() )
			root.setAttribute( ATT_UTLD, uTld );
		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionCell other = (EPPSuggestionCell) o;
			if ( !EqualityUtil.equals( this.tld, other.tld ) )
				return false;
			if ( !this.score.equals( other.score ) )
				return false;
			if ( !this.status.equals( other.status ) )
				return false;
			if ( !EqualityUtil.equals( this.uTld, other.uTld ) )
				return false;
			return true;
		}
		return false;
	}
}