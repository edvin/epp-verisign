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


// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.io.IOException;
import java.net.ServerSocket;

import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.transport.EPPServerCon;
import com.verisign.epp.transport.EPPServerThread;
import com.verisign.epp.transport.ServerEventHandler;
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
public class EPPPlainServer implements EPPServerCon {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPPlainServer.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** DOCUMENT ME! */
	private ServerSocket myServerSocket = null;

	/** DOCUMENT ME! */
	private boolean listening = true;

	/** DOCUMENT ME! */
	private ServerEventHandler myHandler = null;

	/** DOCUMENT ME! */
	private int myPortNumber = 0;

	/** DOCUMENT ME! */
	private int myConTimeout = 0;

	/**
	 * Pre-condition the util.Env provide the properties
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public EPPPlainServer() throws EPPConException {
		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.EPPPlainServer(): entering Constructor");

		try {
			myPortNumber     = EPPEnv.getServerPort();
			myConTimeout     = EPPEnv.getConTimeOut();
		}
		 catch (EPPEnvException myException) {
			/**
			 * Log the Error
			 */
			cat.error("Connection Failed Due to : " + myException.getMessage());
			throw new EPPConException("Connection Failed Due to : "
									  + myException.getMessage());
		}

		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.EPPPlainServer(): ServerPort = "
				  + myPortNumber);

		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.EPPPlainServer(): Exiting Constructor");

		return;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param newHandler DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public void RunServer(ServerEventHandler newHandler)
				   throws EPPConException {
		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.RunServer(): Entering Method");

		myHandler = newHandler;

		try {
			myServerSocket = new ServerSocket(myPortNumber);

			//myServerSocket.setSoTimeout(myConTimeout);
		}
		 catch (IOException myException) {
			/**
			 * Log the Error
			 */
			cat.error("IO Exception : " + myException.getMessage());
			throw new EPPConException("IO Exception : "
									  + myException.getMessage());
		}

		loop();
		close();

		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.RunServer(): Exiting Method");

		return;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public void loop() throws EPPConException {
		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.loop(): Entering Method");

		try {
			while (listening) {
				EPPServerThread p =
					new EPPServerThread(myServerSocket.accept(), myHandler);
				p.start();
			}
		}
		 catch (IOException myException) {
			/**
			 * Log the Error
			 */
			cat.error("I/O Error occured when wating for connection");
			throw new EPPConException("I/O Error occured when wating for connection");
		}
		 catch (SecurityException myException) {
			/**
			 * Log the Error
			 */
			cat.error("security Manger exists and its checkListen method doesn't allow accpet operation");
			throw new EPPConException("security Manger exists and its checkListen method doesn't allow accpet operation");
		}

		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.loop(): Exiting Method");

		return;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public void close() throws EPPConException {
		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.close(): Entering Method");

		try {
			myServerSocket.close();
		}
		 catch (Exception myException) {
			/**
			 * Log the Error
			 */
			cat.error("Close on Server socket Failed");
			throw new EPPConException("Close on Server socket Failed");
		}

		/**
		 * Log Debug Message
		 */
		cat.debug("EPPPlainServer.close(): Exiting Method");
	}
}
