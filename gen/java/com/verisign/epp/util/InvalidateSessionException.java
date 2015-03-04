/***********************************************************
 Copyright (C) 2006 VeriSign, Inc.

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
package com.verisign.epp.util;

/**
 * Simple <code>Exception</code> that is thrown by the 
 * utility <code>TestUtil.handleException(EPPSession, Exception)</code> method to 
 * indicate that the session should be invalidated by the calling 
 * method and further use of the session should be terminated.
 * 
 * @see TestUtil#handleException(EPPSession, Exception)
 */
public class InvalidateSessionException extends Exception {

	/**
	 * Default constructor
	 */
	public InvalidateSessionException() {
		super();
	}

	/**
	 * Create with text message desribing the exception.
	 * 
	 * @param message description message
	 */
	public InvalidateSessionException(String message) {
		super(message);
	}

	/**
	 * Create with text message desribing the exception and an associated cause <code>Throwable</code>.
	 * @param message description message
	 * @param cause <code>Throwable</code> that caused the exception
	 */
	public InvalidateSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create with an associated cause <code>Throwable</code>.
	 * @param cause <code>Throwable</code> that caused the exception
	 */
	public InvalidateSessionException(Throwable cause) {
		super(cause);
	}

}
