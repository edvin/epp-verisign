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

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.ExceptionUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * An <code>EPPSuggestionInfoResp</code> provides an answer to an
 * <code>EPPSuggestionInfoCmd</code> and includes the following attributes:<br>
 * <br>
 * <ul>
 * <li>key - Required suggestion key that matches the
 * <code>EPPSuggestionInfoCmd</code> key</li>
 * <li>answer - Optional answer in either grid or table view</li>
 * <li>tokens - Optional suggestion tokens</li>
 * </ul>
 * 
 * @see EPPSuggestionInfoCmd
 * @author jcolosi
 */
public class EPPSuggestionInfoResp extends EPPResponse {

	private static final long serialVersionUID = -4724121330359790669L;

	private static Logger LOGGER =
			Logger.getLogger( EPPSuggestionInfoResp.class.getName(),
					EPPCatFactory.getInstance().getFactory() );

	static private final String ELM_ANSWER = "suggestion:answer";

	final static String ELM_NAME = "suggestion:infData";

	/**
	 * Suggestion key
	 */
	private String key = null;

	/**
	 * Suggestion language (defaults to ENG (english))
	 */
	private String language = EPPSuggestionConstants.DEFAULT_LANGUAGE;

	/**
	 * Suggestion answer
	 */
	private EPPSuggestionAnswer answer = null;

	/**
	 * Optional suggestion tokens
	 */
	private List<EPPSuggestionToken> tokens = new ArrayList<EPPSuggestionToken>();


	/**
	 * Default constructor that needs the <code>key</code> attribute and the
	 * transid attribute set prior to calling <code>encode</code>.
	 */
	public EPPSuggestionInfoResp () {
	}


	/**
	 * Creates an <code>EPPSuggestionInfoResp</code> only the transaction id
	 * set. The <code>key</code> attribute must be set prior to calling
	 * <code>encode</code>.
	 * 
	 * @param aTransId
	 *        The transaction id containing the server transaction and
	 *        optionally the client transaction id
	 */
	public EPPSuggestionInfoResp ( final EPPTransId aTransId ) {
		super( aTransId );
	}


	/**
	 * Creates an <code>EPPSuggestionInfoResp</code> with the required
	 * attributes set.
	 * 
	 * @param aTransId
	 *        The transaction id containing the server transaction and
	 *        optionally the client transaction id
	 * @param aKey
	 *        Suggestion key
	 */
	public EPPSuggestionInfoResp ( final EPPTransId aTransId, final String aKey ) {
		super( aTransId );
		this.key = aKey;
	}


	/**
	 * Creates an <code>EPPSuggestionInfoResp</code> with the all the
	 * attributes.
	 * 
	 * @param aTransId
	 *        The transaction id containing the server transaction and
	 *        optionally the client transaction id
	 * @param aKey
	 *        Suggestion key
	 * @param aLanguage
	 *        Suggestion language
	 * @param aTokens
	 *        The optional suggestion tokens
	 * @param aAnswer
	 *        the optional answer in table or grid view
	 */
	public EPPSuggestionInfoResp ( final EPPTransId aTransId, final String aKey,
			final String aLanguage, final List<EPPSuggestionToken> aTokens, final EPPSuggestionAnswer aAnswer ) {
		super( aTransId );
		this.key = aKey;
		setLanguage( aLanguage );
		this.tokens = aTokens;
		this.answer = aAnswer;
	}


	/**
	 * Adds a suggestion token to the response.
	 * 
	 * @param aToken
	 *        Suggestion token to add
	 * @throws InvalidValueException
	 */
	public void addToken ( final EPPSuggestionToken aToken )
			throws InvalidValueException {
		if ( aToken == null )
			throw new InvalidValueException( "Cannot add a null Token" );
		if ( this.tokens == null )
			resetTokens();
		this.tokens.add( aToken );
	}


	/**
	 * Is the answer defined?
	 * 
	 * @return <code>true</code> if is defined;<code>false</code> otherwise.
	 */
	public boolean hasAnswer () {
		return (this.answer != null ? true : false);
	}


	/**
	 * Gets the suggestion answer that is either in table or grid view.
	 * 
	 * @return Suggestion answer if defined;<code>null</code> otherwise.
	 */
	public EPPSuggestionAnswer getAnswer () {
		return this.answer;
	}


	/**
	 * Gets the suggestion key.
	 * 
	 * @return Returns the key.
	 */
	public String getKey () {
		return key;
	}


	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPSuggestionInfoResp</code>.
	 * 
	 * @return <code>EPPSuggestionMapFactory.NS</code>
	 */
	public String getNamespace () {
		return EPPSuggestionMapFactory.NS;
	}


	/**
	 * Does the response have tokens defined?
	 * 
	 * @return <code>true</code> if is defined;<code>false</code> otherwise.
	 */
	public boolean hasTokens () {
		return (this.tokens != null && this.tokens.size() != 0 ? true : false);
	}


	/**
	 * Gets the suggestion tokens.
	 * 
	 * @return Returns the tokens if defined;<code>null</code> otherwise.
	 */
	public List<EPPSuggestionToken> getTokens () {
		return this.tokens;
	}


	/**
	 * Gets the EPP response type associated with
	 * <code>EPPSuggestionInfoResp</code>.
	 * 
	 * @return <code>EPPSuggestionInfoResp.ELM_NAME</code>
	 */
	public String getType () {
		return ELM_NAME;
	}


	/**
	 * Resets the tokens to an empty list.
	 */
	public void resetTokens () {
		this.tokens = new ArrayList<EPPSuggestionToken>();
	}


	/**
	 * Sets the suggestion answer in either table or grid view.
	 * 
	 * @param aAnswer
	 *        The suggestion answer
	 */
	public void setAnswer ( final EPPSuggestionAnswer aAnswer ) {
		this.answer = aAnswer;
	}


	/**
	 * Sets the suggestion key
	 * 
	 * @param aKey
	 *        Suggestion key
	 */
	public void setKey ( final String aKey ) {
		this.key = aKey;
	}


	/**
	 * @return Returns the language.
	 */
	public String getLanguage () {
		return language;
	}


	/**
	 * sets the suggestion language
	 * 
	 * @param aLanguage
	 *        The language to set - a null value will force the default to be set
	 */
	public void setLanguage ( final String aLanguage ) {
		if ( aLanguage == null ) {
			language = EPPSuggestionConstants.DEFAULT_LANGUAGE;
		}

		this.language = aLanguage.toUpperCase();
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


	@SuppressWarnings("unchecked")
	@Override
	protected void doDecode ( final Element aElement ) throws EPPDecodeException {
		this.key =
				EPPUtil.decodeString( aElement, EPPSuggestionMapFactory.NS,
						EPPSuggestionConstants.ELM_KEY );
		if ( this.key == null )
			ExceptionUtil.missingDuringDecode( "key" );

		String decodedLanguage =
				EPPUtil.decodeString( aElement, EPPSuggestionMapFactory.NS,
						EPPSuggestionConstants.ELM_LANGUAGE );

		// default to ENG (for english) if optional language element is not
		// specified
		if ( decodedLanguage == null ) {
			decodedLanguage = EPPSuggestionConstants.DEFAULT_LANGUAGE;
		}

		this.language = decodedLanguage.toUpperCase();

		this.answer =
				(EPPSuggestionAnswer) EPPUtil.decodeComp( aElement,
						EPPSuggestionMapFactory.NS, ELM_ANSWER,
						EPPSuggestionAnswer.class );

		this.tokens =
				EPPUtil.decodeCompList( aElement, EPPSuggestionMapFactory.NS,
						EPPSuggestionToken.ELM_NAME, EPPSuggestionToken.class );
	}


	@Override
	protected Element doEncode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		root.setAttribute( "xmlns:suggestion", EPPSuggestionMapFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPSuggestionMapFactory.NS_SCHEMA );

		if ( this.key == null )
			ExceptionUtil.missingDuringEncode( "key" );

		EPPUtil.encodeString( aDocument, root, this.key,
				EPPSuggestionMapFactory.NS, EPPSuggestionConstants.ELM_KEY );

		if ( (this.language != null)
				&& (!this.language
						.equals( EPPSuggestionConstants.DEFAULT_LANGUAGE )) ) {
			this.language = this.language.toUpperCase();
			EPPUtil.encodeString( aDocument, root, this.language,
					EPPSuggestionMapFactory.NS,
					EPPSuggestionConstants.ELM_LANGUAGE );
		}

		EPPUtil.encodeCompList( aDocument, root, this.tokens );

		if ( this.answer != null )
			EPPUtil.encodeComp( aDocument, root, this.answer );

		return root;
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		return (EPPSuggestionInfoResp) super.clone();
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			if ( !super.equals( o ) )
				return false;
			EPPSuggestionInfoResp other = (EPPSuggestionInfoResp) o;
			if ( !EqualityUtil.equals( this.key, other.key ) ) {
				LOGGER.error( "EPPSuggestionInfoResp.equals(): key not equal" );
				return false;
			}
			if ( !EPPUtil.equalLists( this.tokens, other.tokens ) ) {
				LOGGER
						.error( "EPPSuggestionInfoResp.equals(): tokens not equal" );
				return false;
			}
			if ( !EqualityUtil.equals( this.answer, other.answer ) ) {
				LOGGER
						.error( "EPPSuggestionInfoResp.equals(): answer not equal" );
				return false;
			}
			if ( !EqualityUtil.equals( this.language, other.language ) ) {
				LOGGER
						.error( "EPPSuggestionInfoResp.equals(): language not equal" );
				return false;
			}
			return true;
		}
		return false;
	}

}
