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

import com.verisign.epp.exception.EPPException;


/**
 * <p>
 * Title: EPP SDK
 * </p>
 * 
 * <p>
 * Description: EPP SDK for 1.0 Spec
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: VeriSign
 * </p>
 *
 * @author clloyd
 * @version 1.0
 */
public class EPPParserException extends EPPException {
	/**
	 * Creates a new EPPParserException object.
	 */
	public EPPParserException() {
		super("no message");
	}

	/**
	 * Creates a new EPPParserException object.
	 *
	 * @param info DOCUMENT ME!
	 */
	public EPPParserException(String info) {
		super(info);
	}

	/**
	 * Creates a new EPPParserException object.
	 *
	 * @param aThrowable DOCUMENT ME!
	 */
	public EPPParserException(Throwable aThrowable) {
		super(aThrowable.getMessage());
	}
}
