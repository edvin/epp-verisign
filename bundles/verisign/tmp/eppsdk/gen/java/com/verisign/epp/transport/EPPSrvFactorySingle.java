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

/**
 * This is a Singleton Class Please See Singleton Design Pattern $Id:
 * EPPSrvFactorySingle.java,v 1.2 2004/01/07 04:17:57 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPSrvFactorySingle extends EPPSrvFactory {
	/** DOCUMENT ME! */
	private static EPPSrvFactorySingle myInstance = null;

	/**
	 * Private constructor to supresses default public instantiation
	 */
	private EPPSrvFactorySingle() {
		// do nothing 
	}

	/**
	 * This is lazy singleton, after the first call is instantiated  and it
	 * will remain in memory.
	 *
	 * @return an instance of EPPConFactorySingle
	 */
	public static EPPSrvFactorySingle getInstance() {
		if (myInstance == null) {
			myInstance = new EPPSrvFactorySingle();
		}

		return myInstance;
	}
}
