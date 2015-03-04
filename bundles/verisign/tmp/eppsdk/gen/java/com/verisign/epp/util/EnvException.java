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
package com.verisign.epp.util;

/**
 * @see java.lang.Exception
 * @since JDK1.0
 */
import com.verisign.epp.exception.EPPException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class EnvException extends EPPException {
	/**
	 * Constructs an Exception with the specified detail message. This message
	 * should provide information about the source, reasons and any other
	 * useful facts that could justifies the reason the exception is thrown.
	 *
	 * @param myDesc String containing a detailed message.
	 */
	public EnvException(String myDesc) {
		super(myDesc);
	}

	/**
	 * This Constructor is private in order to discourage its usage. The
	 * message is intended to provide information about the source and reasons
	 * the exception is thrown, which could be very useful for identifying and
	 * reason the unexpected behaviors is occurred.
	 */
	private EnvException() {
		super("Unknow Reason EnvException is invoked");
	}
}
