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

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPInfoCmd;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.ExceptionUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.codec.suggestion.util.UnsignedLong;
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents an EPP Suggestion &lt;info&gt; command that is used to retrieve
 * domain name suggestions. The &lt;suggestion:info&gt; element MUST contain the
 * following child elements: <br>
 * <br>
 * <ul>
 * <li>A &lt;suggestion:key&gt; element that contains the domain name or
 * keywords for which suggestions are requested. Use <code>getKey</code> and
 * <code>setKey</code> to get and set the element.</li>
 * <li>A &lt;suggestion:language&gt; element that contains the language for
 * which suggestions are requested. Use <code>getLanguage</code> and
 * <code>setLanguage</code> to get and set the element. If not specified, this
 * element defaults to ENG (english)</li>
 * </ul>
 * <br>
 * <code>EPPSuggestionInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated with <code>EPPSuggestionInfoCmd</code>.<br>
 * <br>
 * 
 * @see com.verisign.epp.codec.suggestion.EPPSuggestionInfoResp
 * @author jcolosi
 */
public class EPPSuggestionInfoCmd extends EPPInfoCmd {

	private static final long serialVersionUID = -5908410757699049707L;

	private static final String ELM_FILTER = "suggestion:filter";
	private static final String ELM_FILTERID = "suggestion:filterid";

	static final String ELM_NAME = "suggestion:info";

	private EPPSuggestionFilter filter = null;

	private UnsignedLong filterId = new UnsignedLong(); // non-null

	private String key = null;

	private String language = EPPSuggestionConstants.DEFAULT_LANGUAGE; // defaults
	// to
	// english

	/**
	 * Constructor.
	 */
	public EPPSuggestionInfoCmd () {
		this( null );
	}


	/**
	 * Constructor.
	 * 
	 * @param aTransId
	 *        a transaction id
	 */
	public EPPSuggestionInfoCmd ( final String aTransId ) {
		super( aTransId );
	}


	/**
	 * Filter getter.
	 * 
	 * @return a filter
	 */
	public EPPSuggestionFilter getFilter () {
		return filter;
	}

	/**
	 * FilterId getter.
	 * 
	 * @return a filter id
	 */
	public UnsignedLong getFilterId () {
		return filterId;
	}

	/**
	 * Key getter.
	 * 
	 * @return a key
	 */
	public String getKey () {
		return key;
	}


	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPSuggestionInfoCmd</code>.
	 * 
	 * @return <code>EPPSuggestionMapFactory.NS</code>
	 */
	public String getNamespace () {
		return EPPSuggestionMapFactory.NS;
	}

	/**
	 * Filter setter.
	 * 
	 * @param aFilter a filter
	 */
	public void setFilter ( final EPPSuggestionFilter aFilter ) {
		this.filter = aFilter;
	}

	/**
	 * Filter id setter.
	 * 
	 * @param aFilterId a filter id
	 * @throws InvalidValueException
	 */
	public void setFilterId ( final long aFilterId )
			throws InvalidValueException {
		this.filterId.set( aFilterId );
	}

	/**
	 * Filter id setter.
	 * 
	 * @param aFilterId a filter id
	 * @throws InvalidValueException
	 */
	public void setFilterId ( final UnsignedLong aFilterId ) {
		if ( filterId == null )
			this.filterId.unset();
		else
			this.filterId = aFilterId;
	}

	/**
	 * Unset the filter id.
	 */
	public void unsetFilterId () {
		this.filterId.unset();
	}

	/**
	 * Key setter.
	 * 
	 * @param aKey a key
	 */
	public void setKey ( final String aKey ) {
		this.key = aKey;
	}


	/**
	 * Returns the value for the <code>language</code> element, which should be
	 * one of the
	 * {@link com.verisign.epp.codec.suggestion.EPPSuggestionConstants} language
	 * code constants.
	 * 
	 * @return Language code with the default being
	 *         {@link com.verisign.epp.codec.suggestion.EPPSuggestionConstants#ENGLISH_CODE}
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#ENGLISH_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#GERMAN_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#SPANISH_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#PORTUGUESE_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#FRENCH_CODE
	 */
	public String getLanguage () {
		return language;
	}


	/**
	 * Set the desired <code>language</code> code.
	 * 
	 * @param aLanguage
	 *        One of the
	 *        {@link com.verisign.epp.codec.suggestion.EPPSuggestionConstants}
	 *        language code constants.
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#ENGLISH_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#GERMAN_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#SPANISH_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#PORTUGUESE_CODE
	 * @see com.verisign.epp.codec.suggestion.EPPSuggestionConstants#FRENCH_CODE
	 */
	public void setLanguage ( final String aLanguage ) {
		if ( aLanguage == null ) {
			this.language = EPPSuggestionConstants.DEFAULT_LANGUAGE;
		}
		else {

			this.language = aLanguage.toUpperCase();
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
	protected void doDecode ( final Element aElement )
			throws EPPDecodeException {
		key =
				EPPUtil.decodeString( aElement, EPPSuggestionMapFactory.NS,
						EPPSuggestionConstants.ELM_KEY );
		if ( key == null )
			ExceptionUtil.missingDuringDecode( "key" );

		String decodedLanguage =
				EPPUtil.decodeString( aElement, EPPSuggestionMapFactory.NS,
						EPPSuggestionConstants.ELM_LANGUAGE );

		// default to ENG (for english) if optional language element is not
		// specified
		if ( decodedLanguage == null ) {
			decodedLanguage = EPPSuggestionConstants.DEFAULT_LANGUAGE;
		}

		language = decodedLanguage.toUpperCase();

		filter =
				(EPPSuggestionFilter) EPPUtil.decodeComp( aElement,
						EPPSuggestionMapFactory.NS, ELM_FILTER,
						EPPSuggestionFilter.class );
		if ( filter == null ) {
			filterId.set( EPPUtil.decodeString( aElement,
					EPPSuggestionMapFactory.NS, ELM_FILTERID ) );
		}
	}


	@Override
	protected Element doEncode ( final Document aDocument )
			throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		root.setAttribute( "xmlns:suggestion", EPPSuggestionMapFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPSuggestionMapFactory.NS_SCHEMA );

		if ( key == null )
			ExceptionUtil.missingDuringEncode( "key" );
		EPPUtil.encodeString( aDocument, root, key, EPPSuggestionMapFactory.NS,
				EPPSuggestionConstants.ELM_KEY );

		// Create language element if non-null and not the default value
		if ( (language != null)
				&& (!this.language
						.equals( EPPSuggestionConstants.DEFAULT_LANGUAGE )) ) {
			EPPUtil.encodeString( aDocument, root, language,
					EPPSuggestionMapFactory.NS,
					EPPSuggestionConstants.ELM_LANGUAGE );
		}

		if ( filter != null )
			EPPUtil.encodeComp( aDocument, root, filter );

		if ( filterId.isSet() ) {
			EPPUtil.encodeString( aDocument, root, filterId + "",
					EPPSuggestionMapFactory.NS, ELM_FILTERID );
		}

		return root;
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		return (EPPSuggestionInfoCmd) super.clone();
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			if ( !super.equals( o ) )
				return false;
			EPPSuggestionInfoCmd other = (EPPSuggestionInfoCmd) o;
			if ( !EqualityUtil.equals( this.key, other.key ) )
				return false;
			if ( !this.filterId.equals( other.filterId ) )
				return false;
			if ( !EqualityUtil.equals( this.filter, other.filter ) )
				return false;
			if ( !EqualityUtil.equals( this.language, other.language ) )
				return false;
			return true;
		}
		return false;
	}
}