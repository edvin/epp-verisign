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
package com.verisign.epp.transport;

import com.verisign.epp.exception.EPPException;


/**
 * Connection exception used by classes in the
 * <code>com.verisign.epp.transport</code> package.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPConException extends EPPException {
	/**
	 * Instantiate a <code>EPPConException</code> with a description.
	 *
	 * @param aDesc Description for the exception
	 */
	public EPPConException(String aDesc) {
		super(aDesc);
	}
}
