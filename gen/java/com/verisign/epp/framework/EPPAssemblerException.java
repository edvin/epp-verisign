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
package com.verisign.epp.framework;


// EPP imports
import com.verisign.epp.exception.EPPException;


/**
 * The EPPAssemblerException defines a set of possible exceptions that can be
 * thrown from an <code>EPPAssembler</code>.  Static instances of
 * EPPAssemblerExceptions are defined here and should be used by clients of
 * this class.  As a general guideline, there is a unique
 * <code>EPPAssemblerException</code> associated with each concrete
 * <code>EPPAssembler</code> class.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.exception.EPPException
 * @see com.verisign.epp.framework.EPPAssembler
 */
public class EPPAssemblerException extends EPPException {
	/**
	 * The typesafe constant XML defines the instance of EPPAssemblerException
	 * that should be thrown from an XMLAssembler
	 */
	public static final EPPAssemblerException XML =
		new EPPAssemblerException(0);

	/**
	 * The typesafe constant FATAL defines the instance of
	 * EPPAssemblerException that should be thrown for fatal client session
	 * errors.
	 */
	public static final EPPAssemblerException FATAL =
		new EPPAssemblerException(1);

	/**
	 * The typesafe constant INTRUPTEDIO defines the instance of
	 * EPPAssemblerException that should be thrown for Intrupted Read/Write
	 * (timed out).
	 */
	public static final EPPAssemblerException INTRUPTEDIO =
		new EPPAssemblerException(2);

	/**
	 * The typesafe constant CLOSECON defines the instance of
	 * EPPAssemblerException that should be thrown for Intrupted Read/Write
	 * (timed out).
	 */
	public static final EPPAssemblerException CLOSECON =
		new EPPAssemblerException(3);

	/**
	 * The typesafe constant MISSINGPARAMETER defines the instance of
	 * EPPAssemblerException that should be thrown for missing input
	 * parameters.
	 */
	public static final EPPAssemblerException MISSINGPARAMETER =
		new EPPAssemblerException(4);

	/**
	 * The typesafe constant COMMANDNOTFOUND defines the instance of
	 * EPPAssemblerException that should be thrown when a command component 
	 * can not be found.  
	 */
	public static final EPPAssemblerException COMMANDNOTFOUND =
		new EPPAssemblerException(5);

	/**
	 * The typesafe constant RESPONSENOTFOUND defines the instance of
	 * EPPAssemblerException that should be thrown when a response component 
	 * can not be found.  
	 */
	public static final EPPAssemblerException RESPONSENOTFOUND =
		new EPPAssemblerException(6);

	/**
	 * The typesafe constant EXTENSIONOTFOUND defines the instance of
	 * EPPAssemblerException that should be thrown when a extension component 
	 * can not be found.  
	 */
	public static final EPPAssemblerException EXTENSIONNOTFOUND =
		new EPPAssemblerException(7);
	
	
	/** Each <code>EPPAssemblerException</code> instance has a unique value */
	private int value;

	/** Is the exception fatal for the client session? */
	private boolean fatal = false;

	/**
	 * Constructs a new <code>EPPAssemblerException</code> instance.
	 *
	 * @param aValue The unique value of this instance.
	 */
	private EPPAssemblerException(int aValue) {
		super("EPPAssemblerException");
		value = aValue;
	}

	/**
	 * Constructs a new <code>EPPAssemblerException</code> instance
	 *
	 * @param aInfo Information that can be included with the
	 * 		  <code>EPPAssemblerException</code>
	 * @param ex An instance of an <code>EPPAssemblerException</code>.  This
	 * 		  should be one of the static instaces defined in
	 * 		  <code>EPPAssemblerException</code>.
	 */
	public EPPAssemblerException(String aInfo, EPPAssemblerException ex) {
		super(aInfo);
		value     = ex.getValue();
		fatal     = false;
	}

	/**
	 * Constructs a new <code>EPPAssemblerException</code> instance with a
	 * fatal indicator parameter.
	 *
	 * @param aInfo Information that can be included with the
	 * 		  <code>EPPAssemblerException</code>
	 * @param ex An instance of an <code>EPPAssemblerException</code>.  This
	 * 		  should be one of the static instaces defined in
	 * 		  <code>EPPAssemblerException</code>.
	 * @param isFatal Is the exception fatal for the client session?
	 */
	public EPPAssemblerException(
								 String aInfo, EPPAssemblerException ex,
								 boolean isFatal) {
		super(aInfo);
		value     = ex.getValue();
		fatal     = isFatal;
	}

	/**
	 * Returns the value of the <code>EPPAssemblerException</code> instance.
	 * This is used for comparisons with the equals() method.
	 *
	 * @return int The value of this <code>EPPAssemblerException</code>
	 * 		   instance
	 */
	private int getValue() {
		return value;
	}

	/**
	 * Returns true if two <code>EPPAssemblerException</code> instances are
	 * equal.
	 *
	 * @param obj DOCUMENT ME!
	 *
	 * @return boolean True if the two <code>EPPAssemblerException</code> have
	 * 		   the same value.
	 */
	public boolean equals(Object obj) {
		return (value == ((EPPAssemblerException) obj).getValue());
	}

	/**
	 * Is the exception fatal for the client session?
	 *
	 * @return <code>true</code> if is fatal; <code>false</code> otherwise.
	 */
	public boolean isFatal() {
		return fatal;
	}
}
