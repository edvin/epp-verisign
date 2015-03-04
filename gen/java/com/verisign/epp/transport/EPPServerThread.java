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
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;

import org.apache.log4j.Logger;

import com.verisign.epp.util.EPPCatFactory;


/**
 * $Id: EPPServerThread.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPServerThread extends Thread {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPServerThread.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** DOCUMENT ME! */
	private Socket socket = null;

	/** DOCUMENT ME! */
	private InputStream myInputStream = null;

	/** DOCUMENT ME! */
	private OutputStream myOutputStream = null;

	/** DOCUMENT ME! */
	private ServerEventHandler myHandler = null;
	
	/**
	 * Creates a new EPPServerThread object.
	 *
	 * @param newSocket DOCUMENT ME!
	 * @param newHandler DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 */
	public EPPServerThread(Socket newSocket, ServerEventHandler newHandler)
					throws IOException {
		super("EPPMultiServerThread");
		socket		  = newSocket;
		myHandler     = newHandler;
		
		// SSLSocket?
		if (newSocket instanceof SSLSocket) {

			// Register handshake completion listener
			((SSLSocket) newSocket)
					.addHandshakeCompletedListener(new HandshakeCompletedListener() {
						public void handshakeCompleted(
								HandshakeCompletedEvent aEvent) {
							try {
								cat.debug("Server SSL Handshake"
										+ ": Cipher = "
										+ aEvent.getCipherSuite()
										+ ": Protocol = "
										+ aEvent.getSession().getProtocol()
										+ ": Peer = "
										+ aEvent.getSession()
												.getPeerPrincipal().getName()
										+ ": Issuer = "
										+ aEvent.getPeerCertificateChain()[0]
												.getIssuerDN().getName());
							}
							catch (SSLPeerUnverifiedException e) {
								// ignore
							}
						}
					});
		}

		try {
			myInputStream	   = socket.getInputStream();
			/**
			 * JG 2/16/05 - 
			 * Ensured that the output stream is buffered so the EPP header and packet 
			 * are sent in a single packet.  20480 bytes was chosen to ensure that all 
			 * EPP packets can be held in the buffered output stream.  
			 */ 
			myOutputStream     = new BufferedOutputStream(socket.getOutputStream(), 20480);
		}
		 catch (IOException myException) {
			myException.printStackTrace();
			throw new IOException(myException.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void run() {
		cat.info("run(): enter");

		if (myHandler == null) {
			cat.error("The Server Handler is not Set");
			return;
		}

		try {
			ServerEventHandler myConHandler =
				(ServerEventHandler) myHandler.clone();
			myConHandler.handleConnection(myInputStream, myOutputStream);
		}
		 catch (CloneNotSupportedException myException) {
			cat.error(
					  "Server Run : CloneNotSupportedException : "
					  + myException.getMessage(), myException);
			return;
		}

		cat.info("run(): Closing socket");

		try {
			socket.close();
		}
		 catch (IOException myException) {
			cat.error(
					  "Server Run : IOException When Closing the Connection : "
					  + myException.getMessage(), myException);

			return;
		}
		 
		cat.info("run(): exit");
	}
}
