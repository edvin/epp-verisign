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
 * An enumeration of supported tlds. This is used only to generate random
 * commands for the unit test. The server can add or remove tlds so this list is
 * not authoritative.
 * 
 * @author jcolosi
 */
public class TldEnum extends Enum {

	private static final long serialVersionUID = -5834282441401638000L;

	private static String[] DATA = {
			"com", "net", "tv", "cc"
	};


	/**
	 * Constructor.
	 */
	public TldEnum () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *        enum value
	 * @throws InvalidValueException
	 */
	public TldEnum ( final String aValue ) throws InvalidValueException {
		super( aValue );
	}


	private TldEnum ( int aKey ) throws InvalidValueException {
		super( aKey );
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		try {
			return new TldEnum( key );
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