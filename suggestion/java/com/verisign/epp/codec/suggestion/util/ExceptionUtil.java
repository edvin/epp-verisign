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
import com.verisign.epp.codec.gen.EPPEncodeException;

/**
 * Utility class to facilitate error handling.
 * 
 * @author jcolosi
 */
public class ExceptionUtil {

	/**
	 * Helper method for dealing with missing elements while decoding.
	 * 
	 * @param aElement
	 *        element that is missing
	 * @throws EPPDecodeException
	 */
	public static void missingDuringDecode ( final String aElement )
			throws EPPDecodeException {
		throw new EPPDecodeException( "Missing required element: \"" + aElement
				+ "\"" );
	}


	/**
	 * Helper method for dealing with missing elements while encoding.
	 * 
	 * @param aElement
	 *        element that is missing
	 * @throws EPPDecodeException
	 */
	public static void missingDuringEncode ( final String aElement )
			throws EPPEncodeException {
		throw new EPPEncodeException( "Missing required element: \"" + aElement
				+ "\"" );
	}

}