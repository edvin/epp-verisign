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
package com.verisign.epp.interfaces;

import com.verisign.epp.interfaces.EPPApplication;


/**
 * Singleton class for {@link EPPApplication} that is used to 
 * initialize the EPP SDK.
 */
public class EPPApplicationSingle extends EPPApplication {
	/** Singleton instance */
	private static EPPApplicationSingle instance = new EPPApplicationSingle();

	/**
	 * Private constructor for the Singleton Design Pattern
	 */
	private EPPApplicationSingle() {
		// do nothing
	}

	/**
	 * Method to get the Singleton instance of <code>EPPApplicationSingle</code>.
	 *
	 * @return an instance of <code>EPPApplicationSingle</code>
	 */
	public static EPPApplicationSingle getInstance() {
		return instance;
	}
}
