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
import com.verisign.epp.exception.EPPException;


/**
 * Represents a general EPP Codec Exception.  This exception 
 * supports for a limited form of exception value types 
 * without the need for subclassing.  
 */
public class EPPCodecException extends EPPException {
	
	/**
	 * Constant value for generic / undefined value of exception
	 */
	public static final int VALUE_UNDEFINED = 0;
	
	/**
	 * Constant value for missing parameter exception
	 */
	public static final int VALUE_MISSINGPARAMETER = 1;
	
	/**
	 * The typesafe constant MISSINGPARAMETER defines the instance of
	 * EPPCodecException that should be thrown for missing input parameters.
	 */
	public static final EPPCodecException MISSINGPARAMETER =
		new EPPCodecException(VALUE_MISSINGPARAMETER);
	
	// A unique value to be associated to the exception like for Missing
	// Parameter.
	private int value = VALUE_UNDEFINED;

	/**
	 * Constructor for EPPCodecException that takes an info string.
	 *
	 * @param aInfo Text description of the exception.
	 */
	public EPPCodecException(String aInfo) {
		super(aInfo);
	}

	// End EPPCodecException.EPPCodecException(String)

	/**
	 * Constructs a new <code>EPPCodecException</code> instance.
	 *
	 * @param aValue The unique value of this instance using one of the <code>VALUE</code> constants.
	 */
	public EPPCodecException(int aValue) {
		super("EPPCodecException");
		this.value = aValue;
	}
	
	/**
	 * Constructs a new <code>EPPCodecException</code> instance.
	 *
	 * @param aValue The unique value of this instance.
	 * @param aInfo Text description of the exception.
	 */
	public EPPCodecException(int aValue, String aInfo) {
		super(aInfo);
		this.value = aValue;
	}
	

	/**
	 * Constructs a new <code>EPPCodecException</code> instance
	 *
	 * @param aInfo Information that can be included with the
	 * 		  <code>EPPCodecException</code>
	 * @param ex An instance of an <code>EPPCodecException</code>.  This should
	 * 		  be one of the static instances defined in
	 * 		  <code>EPPCodecException</code>.
	 */
	public EPPCodecException(String aInfo, EPPCodecException ex) {
		super(aInfo);
		value = ex.getValue();
	}

	/**
	 * Returns true if the passed in EPPCodecException instances equals this
	 * instance.
	 *
	 * @param obj the exception to be
	 *
	 * @return boolean True if the two <code>EPPAssemblerException</code> have
	 * 		   the same value.
	 */
	public boolean equals(Object obj) {
		return (value == ((EPPCodecException) obj).getValue());
	}

	/**
	 * Returns the value of the <code>EPPCodecException</code> instance.  The 
	 * value should match one of the <code>VALUE</code> constants.
	 *
	 * @return One of the <code>VALUE</code> constants
	 */
	public int getValue() {
		return value;
	}
}
