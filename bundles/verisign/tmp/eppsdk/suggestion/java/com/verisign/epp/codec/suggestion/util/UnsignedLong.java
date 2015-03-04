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
 * This class doesn't really implement a complete Unsigned Long because it uses
 * a long primitive. The primitive is simply prohibited from being negative, so
 * the maximum value of this class is half of the maximum value of a true
 * UnsignedLong value.
 * 
 * @author jcolosi
 */
public class UnsignedLong implements Serializable {

	private static final long serialVersionUID = -6046327834170745427L;

	public static long NONE = -1;
	public static String NONE_STRING = "";

	protected long longValue = NONE;


	/**
	 * Constructor.
	 */
	public UnsignedLong () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aLongValue
	 *        a unsigned long value
	 * @throws InvalidValueException
	 */
	public UnsignedLong ( final long aLongValue ) throws InvalidValueException {
		set( aLongValue );
	}


	/**
	 * Constructor.
	 * 
	 * @param aLongAsString
	 *        an unsigned long as string
	 * @throws InvalidValueException
	 */
	public UnsignedLong ( final String aLongAsString )
			throws InvalidValueException {
		set( aLongAsString );
	}


	/**
	 * Get the unsigned long value.
	 * 
	 * @return an unsigned long value
	 */
	public long get () {
		return longValue;
	}


	/**
	 * Is the value set.
	 * 
	 * @return true if it is set
	 */
	public boolean isSet () {
		return longValue != NONE;
	}


	/**
	 * Set the unsigned long value.
	 * 
	 * @param aLongValue
	 *        an unsigned long value
	 * @throws InvalidValueException
	 */
	public void set ( final long aLongValue ) throws InvalidValueException {
		if ( aLongValue >= 0 )
			this.longValue = aLongValue;
		else
			throw new InvalidValueException( aLongValue );
	}


	/**
	 * Set the unsigned long value as string.
	 * 
	 * @param aLongAsString
	 *        an unsigned long value as string
	 * @throws InvalidValueException
	 */
	public void set ( final String aLongAsString ) throws InvalidValueException {
		if ( aLongAsString == null || aLongAsString.length() == 0 )
			longValue = NONE;
		else
			set( Long.parseLong( aLongAsString ) );
	}


	/**
	 * Unset the value.
	 */
	public void unset () {
		this.longValue = NONE;
	}


	/**
	 * Randomly generate an unsigned long value.
	 * 
	 * @return an unsigned long value
	 */
	public UnsignedLong getRandom () {
		try {
			return new UnsignedLong( (long) ((Math.random() * 75) + 25) );
		}
		catch ( InvalidValueException x ) {
			return null;
		}
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		try {
			return new UnsignedLong( longValue );
		}
		catch ( InvalidValueException x ) {
			return null;
		}
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			UnsignedLong other = (UnsignedLong) o;
			return this.longValue == other.longValue;
		}
		return false;
	}


	@Override
	public String toString () {
		if ( isSet() )
			return "" + longValue;
		return NONE_STRING;
	}
}
