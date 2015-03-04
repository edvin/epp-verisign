/***********************************************************
Copyright (C) 2005 VeriSign, Inc.

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

/**
 * Exception used to identify when a component can not be found 
 * in one of the factories during a decode operation.  The exception 
 * includes a kind attribute that defines which <code>kind</code> of component 
 * could not be found.  The constants provided defined the possible 
 * values for the <code>kind</code> attribute.
 */
public class EPPComponentNotFoundException extends EPPDecodeException {
	
	/**
	 * Command component not found
	 */
	public static final short COMMAND = 1;
	
	/**
	 * Response component not found
	 */
	public static final short RESPONSE = 2;
	
	/**
	 * Extension component (command/response extension or protocol extension) 
	 * not found.
	 */
	public static final short EXTENSION = 3;
	
	/**
	 * Kind of component not found
	 */
	private short kind;
	
	
	/**
	 * Constructor for EPPComponentNotFoundException that takes an info string.
	 *
	 * @param aKind Kind of component not found using one of the <code>EPPComponentNotFoundException</code> 
	 * constants.
	 * @param aInfo Text description of the exception.
	 */
	public EPPComponentNotFoundException(short aKind, String aInfo) {
		super(aInfo);
		this.kind = aKind;
	}

	// End EPPComponentNotFoundException.EPPComponentNotFoundException(String)

	/**
	 * Constructor for EPPComponentNotFoundException that takes an info string and the
	 * base EPPCodecException.
	 *
	 * @param aKind Kind of component not found using one of the <code>EPPComponentNotFoundException</code> 
	 * constants.
	 * @param aInfo Text description of the exception.
	 * @param aExcep the EPPCodecException letting us know which specific error
	 * 		  has occured to be propagated to the Assembler.
	 */
	public EPPComponentNotFoundException(short aKind, String aInfo, EPPCodecException aExcep) {
		super(aInfo, aExcep);
		this.kind = aKind;
	}

	// End EPPComponentNotFoundException.EPPComponentNotFoundException(String,EPPCodecException)

	/**
	 * Copy Constructor for EPPComponentNotFoundException that takes the base
	 * EPPCodecException.
	 *
	 * @param aKind Kind of component not found using one of the <code>EPPComponentNotFoundException</code> 
	 * constants.
	 * @param aExcep the EPPCodecException letting us know which specific error
	 * 		  has occured to be propagated to the Assembler.
	 */
	public EPPComponentNotFoundException(short aKind, EPPCodecException aExcep) {
		super(aExcep.getMessage(), aExcep);
		this.kind = aKind;
	}
	
	/**
	 * Kind of component not found.
	 * 
	 * @return One of the <code>EPPComponentNotFoundException</code> constants
	 */
	public short getKind() {
		return this.kind;
	}

	/**
	 * Kind of component not found.
	 * 
	 * @param aKind Kind of component not found using one of the <code>EPPComponentNotFoundException</code> 
	 * constants.
	 */
	public void setKind(short aKind) {
		this.kind = aKind;
	}
	
}
