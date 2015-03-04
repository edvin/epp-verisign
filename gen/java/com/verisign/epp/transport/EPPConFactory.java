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


// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;


/**
 * DOCUMENT ME!
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public abstract class EPPConFactory {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPConFactory.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * DOCUMENT ME!
	 *
	 * @param mySocket DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public EPPClientCon getEPPConnection(String mySocket)
								  throws EPPConException {
		EPPClientCon me = null;

		/**
		 * Set the Log
		 */
		cat.debug("EPPConFactory.getEPPConnection(String): Starting the Method");

		try {
			me = (EPPClientCon) Class.forName(mySocket).newInstance();
		}
		 catch (ClassNotFoundException myException) {
			cat.error(
					  "Class Not Found Exception : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Class Not Found Exception : "
									  + "Class Name " + mySocket + " "
									  + myException.getMessage());
		}
		 catch (InstantiationException myException) {
			cat.error(
					  "Instantiation Exception : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Instantiation Exception : "
									  + "Class Name " + mySocket + " "
									  + myException.getMessage());
		}
		 catch (IllegalAccessException myException) {
			cat.error(
					  "Illegal Access Exception : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Illegal Access Exception : "
									  + "Class Name " + mySocket + " "
									  + myException.getMessage());
		}

		/**
		 * Set the Log
		 */
		cat.debug("EPPConFactory.getEPPConnection(): Ending the Method");

		return me;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public EPPClientCon getEPPConnection() throws EPPConException {
		EPPClientCon me			 = null;
		String		 myClassName = null;

		/**
		 * Set the Log
		 */
		cat.debug("EPPConFactory.getEPPConnection(): Starting the Method");

		try {
			myClassName = EPPEnv.getClientSocketName();
		}
		 catch (EPPEnvException myException) {
			cat.error(
					  "EnvException : " + myException.getMessage(), myException);
			throw new EPPConException("EnvException : "
									  + myException.getMessage());
		}

		try {
			me = (EPPClientCon) Class.forName(myClassName).newInstance();
		}
		 catch (ClassNotFoundException myException) {
			cat.error(
					  "Class Not Found Exception : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Class Not Found Exception : "
									  + "Class Name " + myClassName + " "
									  + myException.getMessage());
		}
		 catch (InstantiationException myException) {
			cat.error(
					  "Instantiation Exception : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Instantiation Exception : "
									  + "Class Name " + myClassName + " "
									  + myException.getMessage());
		}
		 catch (IllegalAccessException myException) {
			cat.error(
					  "Illegal Access Exception : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Illegal Access Exception : "
									  + "Class Name " + myClassName + " "
									  + myException.getMessage());
		}

		/**
		 * Set the Log
		 */
		cat.debug("EPPConFactory.getEPPConnection(): Ending the Method");

		return me;
	}
}
