/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.codec.gen;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------

/**
 * Represents an exception while encoding an XML Element tree from a concrete
 * <code>EPPCodecComponent</code>.
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPEncodeException extends EPPCodecException {
	/**
	 * Constructor for EPPEncodeException that takes an info string.
	 *
	 * @param info Text description of the exception.
	 */
	public EPPEncodeException(String info) {
		super(info);
	}

	// End EPPEncodeException.EPPEncodeException(String)

	/**
	 * Constructor for EPPDecodeException that takes an info string and the
	 * base EPPCodecException.
	 *
	 * @param info Text description of the exception.
	 * @param excep the EPPCodecException letting us know which specific error
	 * 		  has occured to be propagated to the Assembler.
	 */
	public EPPEncodeException(String info, EPPCodecException excep) {
		super(info, excep);
	}

	// End EPPEncodeException.EPPEncodeException(String,EPPCodecException)

	/**
	 * Copy Constructor for EPPEncodeException that takes the base
	 * EPPCodecException.
	 *
	 * @param excep the EPPCodecException letting us know which specific error
	 * 		  has occured to be propagated to the Assembler.
	 */
	public EPPEncodeException(EPPCodecException excep) {
		super(excep.getMessage(), excep);
	}

	// End EPPEncodeException.EPPEncodeException(EPPCodecException)
}
