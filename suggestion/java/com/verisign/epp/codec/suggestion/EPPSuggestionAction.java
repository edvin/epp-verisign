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
import com.verisign.epp.codec.suggestion.util.WeightEnum;
import com.verisign.epp.util.EqualityUtil;

/**
 * Encapsulates a server action to be taken by the Name Suggestions application.
 * Actions indicate how Name Suggestions should be constructed. For intance, the
 * Basic action constructs suggestions by adding a prefix or suffix to the
 * original input. The server can support an unlimited number of actions.
 * 
 * @author jcolosi
 */
public class EPPSuggestionAction implements EPPCodecComponent {

	private static final long serialVersionUID = -2346800563494436852L;

	private static final String ATT_NAME = "name";
	private static final String ATT_WEIGHT = "weight";
	static final String ELM_NAME = "suggestion:action";

	private String name = null;
	private WeightEnum weight = new WeightEnum(); // non-null


	/**
	 * Constructor.
	 */
	public EPPSuggestionAction () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a DOM Element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionAction ( final Element aElement )
			throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Constructor.
	 * 
	 * @param aName
	 *        an action name
	 * @param aWeight
	 *        an action weight
	 * @throws InvalidValueException
	 */
	public EPPSuggestionAction ( final String aName, final String aWeight )
			throws InvalidValueException {
		this();
		setName( aName );
		setWeight( aWeight );
	}


	/**
	 * Name getter.
	 * 
	 * @return a name
	 */
	public String getName () {
		return name;
	}


	/**
	 * Weight getter.
	 * 
	 * @return a weight
	 */
	public WeightEnum getWeight () {
		return weight;
	}


	/**
	 * Name setter.
	 * 
	 * @param aName
	 *        a name
	 */
	public void setName ( final String aName ) {
		if ( aName != null ) {
			this.name = aName.toLowerCase();
		}
		else {
			this.name = aName;
		}
	}


	/**
	 * Weight setter.
	 * 
	 * @param aWeight
	 *        a weight
	 * @throws InvalidValueException
	 */
	public void setWeight ( final String aWeight ) throws InvalidValueException {
		this.weight.set( aWeight );
	}


	/**
	 * Return a string representation for logging purpose.
	 * 
	 * @return a string
	 */
	public String toLogString () {
		return name + ":" + weight;
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		setName( aElement.getAttribute( ATT_NAME ) );
		if ( name == null )
			ExceptionUtil.missingDuringDecode( ATT_NAME );
		setWeight( aElement.getAttribute( ATT_WEIGHT ) );
		if ( weight == null )
			ExceptionUtil.missingDuringDecode( ATT_WEIGHT );
	}


	@Override
	public Element encode ( final Document aDocument )
			throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );

		if ( name == null )
			ExceptionUtil.missingDuringEncode( ATT_NAME );
		if ( !weight.isSet() )
			ExceptionUtil.missingDuringEncode( ATT_WEIGHT );

		root.setAttribute( ATT_NAME, name );
		root.setAttribute( ATT_WEIGHT, weight.toString() );

		return root;
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		return (EPPSuggestionAction) super.clone();
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
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionAction other = (EPPSuggestionAction) o;
			if ( !EqualityUtil.equals( this.name, other.name ) )
				return false;
			if ( !this.weight.equals( other.weight ) )
				return false;
			return true;
		}
		return false;
	}
}