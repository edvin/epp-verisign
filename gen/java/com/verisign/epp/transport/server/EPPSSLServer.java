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
import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.apache.log4j.Logger;

import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.transport.EPPServerCon;
import com.verisign.epp.transport.EPPServerThread;
import com.verisign.epp.transport.ServerEventHandler;
import com.verisign.epp.transport.client.EPPSSLContext;
import com.verisign.epp.transport.client.EPPSSLImpl;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;


/**
 * SSL Server class.  This class implements the <code>EPPServerCon</code> 
 * interface and handles SSL communication with a SSL client. 
 */
public class EPPSSLServer implements EPPServerCon {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPSSLServer.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Server socket used to accept new client connections */
	private SSLServerSocket serverSocket = null;

	/** Is the server actively listening for new connections? */
	private boolean listening = true;

	/** The connection handler */
	private ServerEventHandler handler = null;

	/** Server port number to listen on */
	private int portNumber = 0;

	/**
	 * Server SSL socket factory
	 */
	private SSLServerSocketFactory serverSocketFactory = null;
	
	/**
	 * Gets an SSL property list as a string for logging purposes. Examples of
	 * SSL property lists include supported protocols, enabled protocols,
	 * supported cipher suites, and enabled cipher suites.
	 * 
	 * @param aList
	 *            <code>Array</code> of <code>String</code>'s.
	 * @return Space delimited <code>String</code> representing the property
	 *         list if <code>aList</code> is not <code>null</code>;
	 *         <code>null</code> otherwise
	 */
	protected String getSSLPropertyListString(String aList[]) {
		if (aList == null) {
			return null;
		}

		String theStr = "";
		for (int i = 0; i < aList.length; i++) {
			if (i > 0) {
				theStr += " ";
			}
			theStr += aList[i];
		}

		return theStr;
	}
	

	/**
	 * Creates an <code>EPPSSLServer</code> that initializes 
	 * the SSL configuration and gets the port number to listen 
	 * on.  The server will listen on all interfaces.
	 *
	 * @throws EPPConException Error initializing SSL server
	 */
	public EPPSSLServer() throws EPPConException {
		cat.debug("EPPSSLServer.EPPSSLServer(): entering Constructor");
		
		if (!EPPSSLImpl.isInitialized()) {
			cat.info("EPPSSLServer.EPPSSLServer(): Initializing server SSL configuration");
			
			// Initialize the SSL configuration
			EPPSSLImpl.initialize();
						
			cat.info("EPPSSLServer.EPPSSLServer(): server SSL configuration complete");
		}
		else {
			cat.debug("EPPSSLServer.EPPSSLServer(): server SSL configuration already done");			
		}
		
		serverSocketFactory = EPPSSLImpl.getSSLContext().getServerSocketFactory();	
		
		try {
			this.portNumber     = EPPEnv.getServerPort();
		}
		 catch (EPPEnvException myException) {
			cat.error(
					  "Connection Failed Due to : " + myException.getMessage(),
					  myException);
			throw new EPPConException("Connection Failed Due to : "
									  + myException.getMessage());
		}

		cat.debug("EPPSSLServer.EPPSSLServer(): ServerPort = " + this.portNumber);
		cat.debug("EPPSSLServer.EPPSSLServer(): entering Constructor");
	}

	/**
	 * Starts the server by creating the SSL server socket and 
	 * going into connection accept loop.  
	 *
	 * @param aHandler Connection handler 
	 *
	 * @throws EPPConException Error creating server socket
	 */
	public void RunServer(ServerEventHandler aHandler)
				   throws EPPConException {
		cat.debug("EPPSSLServer.RunServer(): entering Method");

		try {
			this.serverSocket =
				(SSLServerSocket) this.serverSocketFactory.createServerSocket(this.portNumber);
		}
		 catch (IOException myException) {
			cat.error(
					  "Could not Create a ServerSocket "
					  + myException.getMessage(), myException);
			throw new EPPConException("Could not Create a ServerSocket "
									  + myException.getMessage());
		}
		
		EPPSSLContext eppSSLContext = EPPSSLImpl.getEPPSSLContext(); 
		
		if (eppSSLContext.hasSSLEnabledProtocols()) {
			cat
					.debug("EPPSSLServer.RunServer(): Enabled Protocols = ["
							+ this.getSSLPropertyListString(eppSSLContext.getSSLEnabledProtocols()) + "]");
			this.serverSocket.setEnabledProtocols(eppSSLContext.getSSLEnabledProtocols());
		}
		else {
			cat
					.debug("EPPSSLServer.RunServer(): Enabled Protocols NOT specified, using providers default");
		}

		if (eppSSLContext.hasSSLEnabledCipherSuites()) {
			cat
					.debug("EPPSSLServer.RunServer(): Enabled Cipher Suites = ["
							+ this.getSSLPropertyListString(eppSSLContext.getSSLEnabledCipherSuites()) + "]");
			this.serverSocket.setEnabledCipherSuites(eppSSLContext.getSSLEnabledCipherSuites());
		}
		else {
			cat
					.debug("EPPSSLServer.RunServer(): Enabled Cipher Suites NOT specified, using providers default");
		}

		/**
		 * Set the Client Authentication true
		 */
		((SSLServerSocket) serverSocket).setNeedClientAuth(true);

		this.handler = aHandler;
		loop();
		close();

		cat.debug("EPPSSLServer.RunServer(): Exiting Method");
	}

	/**
	 * Run the accept loop, where the server will continue 
	 * listening while the listening flag is <code>true</code> as 
	 * defined by the <code>getListening</code> and the 
	 * <code>setListening(boolean)</code> methods.  Inside the 
	 * loop, the server will accept a client connection and 
	 * spawn a new thread to handle the connection.
	 *
	 * @throws EPPConException Any error with accepting 
	 * or handling a client connection
	 */
	public void loop() throws EPPConException {
		cat.debug("EPPSSLServer.loop(): Entering Method");
		
		

		try {
			while (this.listening) {
				EPPServerThread p =
					new EPPServerThread(
										(Socket) serverSocket.accept(),
										this.handler);
				p.start();
			}
		}
		 catch (IOException myException) {
			throw new EPPConException("I/O Error occured when wating for connection");
		}
		 catch (SecurityException myException) {
			cat.error(
					  "security Manger exists and its checkListen method doesn't allow accpet operation",
					  myException);

			throw new EPPConException("security Manger exists and its checkListen method doesn't allow accpet operation");
		}

		cat.debug("EPPSSLServer.loop(): Exting Method");

		return;
	}

	/**
	 * Closing the server socket
	 *
	 * @throws EPPConException Error closing the server socket
	 */
	public void close() throws EPPConException {
		cat.debug("EPPSSLServer.close(): Entering Method");

		try {
			serverSocket.close();
		}
		 catch (IOException myException) {
			cat.error(
					  "Close on Server socket Failed"
					  + myException.getMessage(), myException);
			throw new EPPConException("Close on Server socket Failed"
									  + myException.getMessage());
		}

		cat.debug("EPPSSLServer.close(): Exting Method");
	}
	
	
	/**
	 * Is the server actively listening for connections?
	 * 
	 * @return Listening boolean property
	 */
	public boolean isListening() {
		return this.listening;
	}
	/**
	 * Sets the server listing property that can be used 
	 * to stop the server from listening for new connections.
	 * 
	 * @param aListening <code>false</code> to stop the server 
	 * from listening to new connections.
	 */
	public void setListening(boolean aListening) {
		this.listening = aListening;
	}
}
