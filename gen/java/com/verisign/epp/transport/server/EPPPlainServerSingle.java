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
package com.verisign.epp.transport.server;

import com.verisign.epp.transport.EPPConException;


/**
 * DOCUMENT ME!
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPPlainServerSingle extends EPPPlainServer {
	/** DOCUMENT ME! */
	private static EPPPlainServerSingle myInstance = null;

	/**
	 * DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 *
	 * @since JDK1.0 Private constructor supresses default public instantiation
	 */
	private EPPPlainServerSingle() throws EPPConException {
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return an instance of EPPMultiServerSingle
	 *
	 * @throws EPPConException DOCUMENT ME!
	 *
	 * @since JDK1.2
	 */
	public static EPPPlainServerSingle getInstance() throws EPPConException {
		if (myInstance == null) {
			myInstance = new EPPPlainServerSingle();
		}

		return myInstance;
	}
}
