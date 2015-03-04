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
 * An enumeration of the types of weights. This is used only to generate random
 * commands for the unit test. The server can add or remove weights so this list
 * is not authoritative.
 * 
 * @author jcolosi
 */
public class WeightEnum extends Enum {

	private static final long serialVersionUID = -8166315363866349775L;

	private static String[] DATA = {
			"off", "low", "medium", "high"
	};


	/**
	 * Constructor.
	 */
	public WeightEnum () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *        a value
	 * @throws InvalidValueException
	 */
	public WeightEnum ( final String aValue ) throws InvalidValueException {
		super( aValue );
	}


	private WeightEnum ( final int aKey ) throws InvalidValueException {
		super( aKey );
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		try {
			return new WeightEnum( key );
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
		return DATA[ RandomHelper.getInt( DATA.length ) ];
	}

}
