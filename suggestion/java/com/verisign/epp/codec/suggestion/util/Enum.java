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
 * An abstract class which provides backbone features of the various
 * Enumerations in the sdk.
 * 
 * @author jcolosi
 */
public abstract class Enum implements Serializable {

	private static final long serialVersionUID = -2861077875297605836L;

	public static int NONE = -1;
	public static String NONE_STRING = "";

	protected int key = NONE;


	/**
	 * Constructor.
	 */
	public Enum () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *        a value
	 * @throws InvalidValueException
	 */
	public Enum ( final String aValue ) throws InvalidValueException {
		set( aValue );
	}


	/**
	 * Constructor. Use this constructor for cloning.
	 * 
	 * @param aKey
	 *        a key
	 */
	protected Enum ( final int aKey ) throws InvalidValueException {
		set( aKey );
	}


	public abstract Object clone () throws CloneNotSupportedException;


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			Enum other = (Enum) o;
			return this.key == other.key;
		}
		return false;
	}


	/**
	 * Get the enum key.
	 * 
	 * @return the enum key
	 */
	public int get () {
		return key;
	}


	/**
	 * Is the key set.
	 * 
	 * @return true if the key is set
	 */
	public boolean isSet () {
		return key != NONE;
	}


	/**
	 * Set the enum based on the key.
	 * 
	 * @param aKey
	 *        a key 
	 * @throws InvalidValueException
	 */
	public void set ( final int aKey ) throws InvalidValueException {
		String[] data = getData();
		if ( aKey < 0 || aKey >= data.length )
			throw new InvalidValueException( aKey );
		this.key = aKey;
	}


	/**
	 * Set the value based on the value.
	 * 
	 * @param aValue a value
	 * @throws InvalidValueException
	 */
	public void set ( final String aValue ) throws InvalidValueException {
		this.key = decode( aValue );
	}


	/**
	 * Return string representation for logging purposes/
	 * 
	 * @return a string
	 */
	public String toLogString () {
		if ( isSet() )
			return "" + getData()[ key ].charAt( 0 );
		return NONE_STRING;
	}


	@Override
	public String toString () {
		if ( isSet() )
			return getData()[ key ];
		return NONE_STRING;
	}


	/**
	 * Unset the enum.
	 */
	public void unset () {
		this.key = NONE;
	}


	/**
	 * Get the possible enum data.
	 * 
	 * @return enum data
	 */
	public abstract String[] getData ();


	private int decode ( final String aValue ) throws InvalidValueException {
		if ( aValue == null || aValue.length() == 0 )
			return NONE;
		String[] data = getData();
		for ( int i = 0; i < data.length; i++ ) {
			if ( aValue.equalsIgnoreCase( data[ i ] ) )
				return i;
		}
		throw new InvalidValueException( aValue );
	}

}