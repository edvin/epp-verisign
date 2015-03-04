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

import java.io.Serializable;

/**
 * This class doesn't really implement a complete Unsigned Short because it uses
 * a short primitive. The primitive is simply prohibited from being negative, so
 * the maximum value of this class is half of the maximum value of a true
 * UnsignedShort value.
 * 
 * @author jcolosi
 */
public class UnsignedShort implements Serializable {

	private static final long serialVersionUID = -6867861918084299519L;

	public static short NONE = -1;
	public static String NONE_STRING = "";

	protected short shortValue = NONE;


	/**
	 * Constructor.
	 */
	public UnsignedShort () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aShortValue
	 *        an unsigned short value
	 * @throws InvalidValueException
	 */
	public UnsignedShort ( final short aShortValue )
			throws InvalidValueException {
		set( aShortValue );
	}


	/**
	 * Constructor.
	 * 
	 * @param aShortValueAsString
	 *        an unsigned short value as string
	 * @throws InvalidValueException
	 */
	public UnsignedShort ( final String aShortValueAsString )
			throws InvalidValueException {
		set( aShortValueAsString );
	}


	/**
	 * Get the unsigned short value.
	 * 
	 * @return an unsigned short value
	 */
	public short get () {
		return shortValue;
	}


	/**
	 * Is the value set.
	 * 
	 * @return true if it is set
	 */
	public boolean isSet () {
		return shortValue != NONE;
	}


	/**
	 * Set an unsigned short value.
	 * 
	 * @param aShortValue
	 *        an unsigned short value
	 * @throws InvalidValueException
	 */
	public void set ( final short aShortValue ) throws InvalidValueException {
		if ( aShortValue >= 0 )
			this.shortValue = aShortValue;
		else
			throw new InvalidValueException( aShortValue );
	}


	/**
	 * Set an unsigned short value as string.
	 * 
	 * @param aShortValueAsString
	 *        an unsigned short value as string
	 * @throws InvalidValueException
	 */
	public void set ( final String aShortValueAsString )
			throws InvalidValueException {
		if ( aShortValueAsString == null || aShortValueAsString.length() == 0 )
			shortValue = NONE;
		else
			set( Short.parseShort( aShortValueAsString ) );
	}


	/**
	 * Unset the value.
	 */
	public void unset () {
		this.shortValue = NONE;
	}


	/**
	 * Get a random unsigned short value.
	 * 
	 * @return an unsigned short value
	 */
	public UnsignedShort getRandom () {
		try {
			return new UnsignedShort( (short) ((Math.random() * 75) + 25) );
		}
		catch ( InvalidValueException x ) {
			return null;
		}
	}


	@Override
	public String toString () {
		if ( isSet() )
			return "" + shortValue;
		return NONE_STRING;
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		try {
			return new UnsignedShort( shortValue );
		}
		catch ( InvalidValueException x ) {
			return null;
		}
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			UnsignedShort other = (UnsignedShort) o;
			return this.shortValue == other.shortValue;
		}
		return false;
	}
}