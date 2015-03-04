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

import com.verisign.epp.codec.gen.EPPDecodeException;

/**
 * Exception class to indicate that the incoming Epp uses an invalid value.
 * 
 * @author jcolosi
 */
public class InvalidValueException extends EPPDecodeException {

	private static final long serialVersionUID = -1162682159801019037L;


	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *        an invalid value
	 */
	public InvalidValueException ( final Object aValue ) {
		super( "Invalid value: \"" + aValue + "\"" );
	}


	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *        an invalid value
	 */
	public InvalidValueException ( long aValue ) {
		super( "Invalid value: \"" + aValue + "\"" );
	}

}