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

import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;


/**
 * $Id: EPPSrvFactory.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public abstract class EPPSrvFactory {
	/**
	 * DOCUMENT ME!
	 *
	 * @param mySocket DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public EPPServerCon getEPPServer(String mySocket) throws EPPConException {
		EPPServerCon me = null;

		try {
			me = (EPPServerCon) Class.forName(mySocket).newInstance();
		}
		 catch (ClassNotFoundException myException) {
			throw new EPPConException("Class Not Found Exception : "
									  + "Class Name " + mySocket + " "
									  + myException.getMessage());
		}
		 catch (InstantiationException myException) {
			throw new EPPConException("Instantiation Exception : "
									  + "Class Name " + mySocket + " "
									  + myException.getMessage());
		}
		 catch (IllegalAccessException myException) {
			throw new EPPConException("Illegal Access Exception : "
									  + "Class Name " + mySocket + " "
									  + myException.getMessage());
		}

		return me;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public EPPServerCon getEPPServer() throws EPPConException {
		EPPServerCon me			 = null;
		String		 myClassName = null;

		try {
			myClassName = EPPEnv.getServerSocketName();
		}
		 catch (EPPEnvException myException) {
			throw new EPPConException("EnvException : "
									  + myException.getMessage());
		}

		try {
			me = (EPPServerCon) Class.forName(myClassName).newInstance();
		}
		 catch (ClassNotFoundException myException) {
			throw new EPPConException("Class Not Found Exception : "
									  + "Class Name " + myClassName + " "
									  + myException.getMessage());
		}
		 catch (InstantiationException myException) {
			throw new EPPConException("Instantiation Exception : "
									  + "Class Name " + myClassName + " "
									  + myException.getMessage());
		}
		 catch (IllegalAccessException myException) {
			throw new EPPConException("Illegal Access Exception : "
									  + "Class Name " + myClassName + " "
									  + myException.getMessage());
		}

		return me;
	}
}
