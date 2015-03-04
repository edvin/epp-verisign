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

package com.verisign.epp.codec.suggestion.util;

/**
 * An enumeration of the types of actions. This is used only to generate random
 * actions for the unit test. The server can add or remove actions so this list
 * is not authoritative.
 * 
 * @author jcolosi
 */
public class ActionEnum extends Enum {

	private static final long serialVersionUID = 1669371601494593352L;

	public static final String ACTION_BASIC = "basic";
	public static final String ACTION_RELATED = "related";
	public static final String ACTION_SIMILAR = "similar";
	/** @deprecated this action does not exist **/
	@Deprecated
	public static final String ACTION_SPELLING = "spelling";
	public static final String ACTION_TOPICAL = "topical";
	public static final String ACTION_RANK_BY_INPUT_ASSOCIATION =
			"rank_by_input_association";
	public static final String ACTION_GEO = "geo";
	public static final String ACTION_CREATIVE = "creative";

	private static String[] DATA =
			{
					ACTION_BASIC, ACTION_RELATED, ACTION_SIMILAR,
					ACTION_SPELLING, ACTION_TOPICAL,
					ACTION_RANK_BY_INPUT_ASSOCIATION, ACTION_GEO,
					ACTION_CREATIVE
			};


	/**
	 * Constructor.
	 */
	public ActionEnum () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *        a value
	 * @throws InvalidValueException
	 */
	public ActionEnum ( final String aValue ) throws InvalidValueException {
		super( aValue );
	}


	/**
	 * Constructor.
	 * 
	 * @param aKey
	 *        a key
	 * @throws InvalidValueException
	 */
	private ActionEnum ( final int aKey ) throws InvalidValueException {
		super( aKey );
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		try {
			return new ActionEnum( key );
		}
		catch ( InvalidValueException x ) {
			return null;
		}
	}


	@Override
	public String[] getData () {
		return DATA;
	}


	/**
	 * Return a random enum value.
	 * 
	 * @return a random enum value
	 */
	public static String getRandomString () {
		if ( RandomHelper.p( .5 ) )
			return DATA[ RandomHelper.getInt( DATA.length ) ].toLowerCase();
		else
			return DATA[ RandomHelper.getInt( DATA.length ) ].toUpperCase();
	}

}
