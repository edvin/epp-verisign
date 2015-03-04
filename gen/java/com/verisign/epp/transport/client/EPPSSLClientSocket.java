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

/**
 * This Class primarily utilizes Sun's JSSE 1.0.2 reference implementation.
 * This reference implementation is simply a proof-of-concept. It is used to
 * demonstrate that the specification is implementable and compatible tests
 * can be written against it.
 */
package com.verisign.epp.transport.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;

/**
 * Secure Socket Layer client socket class that can be configured to connect to
 * a server over SSL / TLS using the <code>EPP.ClientSocketName</code>
 * configuration property. The SSL settings are passed into the
 * <code>EPPSSLClientSocket</code> class using the {@link EPPSSLContext} class.
 * 
 * @see EPPSSLContext
 */
public class EPPSSLClientSocket implements EPPClientCon {

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPSSLClientSocket.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** This attribute maintains an instance of the socket connection. */
	private SSLSocket socket = null;

	/**
	 * Output stream of socket
	 */
	private OutputStream outputStream = null;

	/**
	 * Input stream of socket
	 */
	private InputStream inputStream = null;

	/** Host Name to connect to */
	private String hostName = null;

	/** Port to connect to */
	private int portNumber = 0;

	/** Client host to connect from that determines the route to use */
	private String clientHostName = null;

	/** Is a connection established? */
	private boolean isConnected = true;

	/** Connection timeout in milliseconds. The default is 5000 milliseconds */
	private int conTimeOut = 5000;

	/**
	 * SSL context information used to create the <code>SSLSocket</code>
	 */
	private EPPSSLContext sslContext = null;

	/**
	 * Default constructor that gets the following settings from {@link EPPEnv}:<br>
	 * <br>
	 * <ul>
	 * <li><code>hostName</code> - Defined with the {@link
	 * EPPEnv#getServerName()} method.
	 * <li><code>portNumber</code> - Defined with the {@link
	 * EPPEnv#getServerPort()} method.
	 * <li><code>conTimeout</code> - Defined with the {@link
	 * EPPEnv#getConTimeOut()} method.
	 * <li><code>clientHostName</code> - Defined with the {@link
	 * EPPEnv#getClientHost()} method.
	 * </ul>
	 * 
	 * @throws EPPConException
	 *             If there is a problem getting the default connection
	 *             properties from {@link EPPEnv}.
	 */
	public EPPSSLClientSocket() throws EPPConException {

		// Set attributes based on configuration properties
		try {
			this.hostName = EPPEnv.getServerName();
			this.portNumber = EPPEnv.getServerPort();
			this.conTimeOut = EPPEnv.getConTimeOut();
			this.clientHostName = EPPEnv.getClientHost();
		}
		catch (EPPEnvException myException) {
			cat.error("Connection Failed Due to : " + myException.getMessage(),
					myException);

			throw new EPPConException("Connection Failed Due to : "
					+ myException.getMessage());
		}
	}

	/**
	 * Initializes an SSL connection to the host and port defined by the server
	 * name and the server port properties of <code>EPPEnv</code>.
	 * 
	 * @exception EPPConException
	 *                Error initializing the connection.
	 */
	public void initialize() throws EPPConException {
		cat.debug("Starting EPPSSLClientSocket.initialize()");

		// SSL not initialized?
		if (this.sslContext == null) {
			EPPSSLImpl.initialize();
			this.sslContext = EPPSSLImpl.getEPPSSLContext();
		}

		// Get the SSLSocketFactory from EPPSSLImpl
		SSLSocketFactory mySSLSocketFactory = this.sslContext
				.getSSLSocketFactory();

		// Create SSLSocket from SSLSocketFactory
		try {

			if (this.clientHostName == null) { 
				cat
						.debug("EPPSSLClientSocket.initialize(): Connecting to server host = "
								+ this.hostName
								+ ", server port = "
								+ this.portNumber);
				this.socket = (SSLSocket) mySSLSocketFactory.createSocket(
						this.hostName, this.portNumber);
			}
			else {
				cat
						.debug("EPPSSLClientSocket.initialize(): Connecting to server host = "
								+ this.hostName
								+ ", server port = "
								+ this.portNumber
								+ " from client host = "
								+ this.clientHostName);
				InetAddress theClientAddress = InetAddress
						.getByName(this.clientHostName);

				this.socket = (SSLSocket) mySSLSocketFactory.createSocket(
						this.hostName, this.portNumber, theClientAddress, 0);
			}

			// Log the SSL supported protocols and cipher suites?
			if (cat.isDebugEnabled()) {
				cat
						.debug("EPPSSLClientSocket.initialize(): Supported Protocols = ["
								+ this.getSSLPropertyListString(this.socket
										.getSupportedProtocols())
								+ "], Supported Cipher Suites = ["
								+ this.getSSLPropertyListString(this.socket
										.getSupportedCipherSuites()) + "]");
			}

			// Set additional SSL handshake properties (enabled protocols and
			// enabled cipher suites)
			if (this.sslContext.hasSSLEnabledProtocols()) {
				cat
						.debug("EPPSSLClientSocket.initialize(): Enabled Protocols = ["
								+ this.getSSLPropertyListString(this.sslContext
										.getSSLEnabledProtocols()) + "]");
				this.socket.setEnabledProtocols(this.sslContext
						.getSSLEnabledProtocols());
			}
			else {
				cat
						.debug("EPPSSLClientSocket.initialize(): Enabled Protocols NOT specified, using providers default");
			}

			if (this.sslContext.hasSSLEnabledCipherSuites()) {
				cat
						.debug("EPPSSLClientSocket.initialize(): Enabled Cipher Suites = ["
								+ this.getSSLPropertyListString(this.sslContext
										.getSSLEnabledCipherSuites()) + "]");
				this.socket.setEnabledCipherSuites(this.sslContext
						.getSSLEnabledCipherSuites());
			}
			else {
				cat
						.debug("EPPSSLClientSocket.initialize(): Enabled Cipher Suites NOT specified, using providers default");
			}

			this.socket.setSoTimeout(this.conTimeOut);
			
			// Register handshake completion listener
			this.socket
					.addHandshakeCompletedListener(new HandshakeCompletedListener() {
						public void handshakeCompleted(
								HandshakeCompletedEvent aEvent) {
							try {
								cat.debug("Client SSL Handshake"
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
		catch (IOException ex) {
			cat.error("Could not create an SSLSocket: ", ex);
			throw new EPPConException("Could not create an SSLSocket: "
					+ ex.getMessage());
		}

		cat.debug("EPPSSLClientSocket.initialize(): SSL startHandshake");

		/*
		 * Before any application data is sent or received, the SSL socket will
		 * do SSL handshaking first to set up the security attributes.
		 */
		try {
			this.socket.startHandshake();
		}
		catch (IOException myException) {
			cat.error("Failed When Handshake : " + myException.getMessage(),
					myException);

			throw new EPPConException("Failed When HandShake : "
					+ myException.getMessage());
		}

		// Set the streams from the socket
		try {
			this.inputStream = this.socket.getInputStream();
			this.outputStream = this.socket.getOutputStream();
		}
		catch (IOException ex) {
			cat.error("Failed getting streams from SSLSocket: ", ex);
			throw new EPPConException("Failed getting streams from SSLSocket: "
					+ ex.getMessage());
		}

		this.isConnected = true;

		cat.debug("Ending EPPSSLClientSocket.initialize()");
	}

	/**
	 * Initializes a SSL connection to a specific host and port. There remainder
	 * of the connection settings is derived from the <code>EPPEnv</code>
	 * properties.
	 * 
	 * @param aHostName
	 *            Host name or IP address of host to connect to
	 * @param aPortNumber
	 *            Port number to connect to
	 * @param aSSLContext
	 *            Optional specific SSL context to use
	 * 
	 * @exception EPPConException
	 *                Error initializing the connection.
	 */
	public void initialize(String aHostName, int aPortNumber,
			EPPSSLContext aSSLContext) throws EPPConException {
		this.hostName = aHostName;
		this.portNumber = aPortNumber;
		this.sslContext = aSSLContext;

		this.initialize();
	}

	/**
	 * Initializes a SSL connection to a specific host and port. There remainder
	 * of the connection settings is derived from the <code>EPPEnv</code>
	 * properties.
	 * 
	 * @param aHostName
	 *            Host name or IP address of host to connect to
	 * @param aPortNumber
	 *            Port number to connect to
	 * @param aClientHostName
	 *            Host name or IP address to connect from
	 * @param aSSLContext
	 *            Optional specific SSL context to use
	 * 
	 * @exception EPPConException
	 *                Error initializing the connection.
	 */
	public void initialize(String aHostName, int aPortNumber,
			String aClientHostName, EPPSSLContext aSSLContext)
			throws EPPConException {
		this.hostName = aHostName;
		this.portNumber = aPortNumber;
		this.clientHostName = aClientHostName;
		this.sslContext = aSSLContext;

		this.initialize();
	}

	/**
	 * Gets the input stream of the <code>Socket</code> connection.
	 * 
	 * @return <code>InputStream</code> of the connected <code>Socket</code>
	 * 
	 * @exception EPPConException
	 *                Input stream not set
	 */
	public InputStream getInputStream() throws EPPConException {
		if (this.inputStream == null) {
			cat.error("getInputStream(): No InputStream set");
			throw new EPPConException("No InputStream set");
		}

		return this.inputStream;
	}

	/**
	 * Sets the input stream of the <code>Socket</code> connection. This can
	 * only be set by a derived class to support tunneling and other extensions.
	 * 
	 * @param aInputStream
	 *            Input stream to set
	 */
	protected void setInputStream(InputStream aInputStream) {
		this.inputStream = aInputStream;
	}

	/**
	 * Gets the output stream of the <code>Socket</code> connection.
	 * 
	 * @return <code>OutputStream</code> of the connected <code>Socket</code>
	 * 
	 * @exception EPPConException
	 *                Output stream not set
	 */
	public OutputStream getOutputStream() throws EPPConException {
		if (this.outputStream == null) {
			cat.error("getInputStream(): No OutputStream set");
			throw new EPPConException("No OutputStream set");
		}

		return this.outputStream;
	}

	/**
	 * Sets the output stream of the <code>Socket</code> connection. This can
	 * only be set by a derived class to support tunneling and other extensions.
	 * 
	 * @param aOutputStream
	 *            Output stream to set
	 */
	protected void setOutputStream(OutputStream aOutputStream) {
		this.outputStream = aOutputStream;
	}

	/**
	 * Gets the enclosing <code>SSLSocket</code> of the connection.
	 * 
	 * @return Gets the enclosing <code>SSLSocket</code> if defined;
	 *         <code>null</code> otherwise.
	 */
	protected SSLSocket getSocket() {
		return this.socket;
	}

	/**
	 * Sets the {@link SSLSocket} to use.
	 * 
	 * @param aSocket
	 *            {@link SSLSocket} to use
	 */
	protected void setSocket(SSLSocket aSocket) {
		this.socket = aSocket;
	}

	/**
	 * Gets the {@link EPPSSLContext} to use for the SSL connection.
	 * 
	 * @return {@link EPPSSLContext} to use
	 */
	protected EPPSSLContext getSslContext() {
		return this.sslContext;
	}

	/**
	 * Sets the {@link EPPSSLContext} to use for the SSL connection
	 * 
	 * @param aSslContext
	 *            {@link EPPSSLContext} to use
	 */
	protected void setSslContext(EPPSSLContext aSslContext) {
		this.sslContext = aSslContext;
	}

	/**
	 * Gets the client host name to use for the connection.
	 * 
	 * @return Client host name if set; <code>null</code> otherwise.
	 */
	protected String getClientHostName() {
		return this.clientHostName;
	}

	/**
	 * Sets the client host name to use for the connection.
	 * 
	 * @param aClientHostName
	 *            Client host name to use
	 */
	protected void setClientHostName(String aClientHostName) {
		this.clientHostName = aClientHostName;
	}

	/**
	 * Gets the host name or IP address to connect to.
	 * 
	 * @return host name or IP address to connect to
	 */
	protected String getHostName() {
		return this.hostName;
	}

	/**
	 * Sets the host name or IP address to connect to.
	 * 
	 * @param aHostName
	 *            Host name or IP address to connect to
	 */
	protected void setHostName(String aHostName) {
		this.hostName = aHostName;
	}

	/**
	 * Gets the port number to connect to.
	 * 
	 * @return port number to connect to
	 */
	protected int getPortNumber() {
		return this.portNumber;
	}

	/**
	 * Sets the port number to connect to.
	 * 
	 * @param aPortNumber
	 *            Port number to connect to
	 */
	protected void setPortNumber(int aPortNumber) {
		this.portNumber = aPortNumber;
	}

	/**
	 * Gets the connection timeout setting.
	 * 
	 * @return connection timeout
	 */
	protected int getConTimeout() {
		return this.conTimeOut;
	}

	/**
	 * Sets the connection timeout setting.
	 * 
	 * @param aConTimeout
	 *            connection timeout
	 */
	protected void setConTimeout(int aConTimeout) {
		this.conTimeOut = aConTimeout;
	}

	/**
	 * Is the connection established?
	 * 
	 * @return <code>true</code> if connected; <code>false</code> otherwise.
	 */
	protected boolean isConnected() {
		return this.isConnected;
	}

	/**
	 * Sets whether the connection is established.
	 * 
	 * @param aIsConnected
	 *            <code>true</code> if connected; <code>false</code> otherwise
	 */
	protected void setConnected(boolean aIsConnected) {
		this.isConnected = aIsConnected;
	}

	/**
	 * Closing the connection.
	 * 
	 * @exception EPPConException
	 *                Error closing the connection
	 */
	public void close() throws EPPConException {
		cat.debug("EPPSSLClientSocket.close(): Starting the Method");

		// Return if already closed
		if (!this.isConnected) {
			return;
		}

		// Closes the streams
		try {
			if (this.outputStream != null) {
				this.outputStream.close();
				this.outputStream = null;
			}

			if (this.inputStream != null) {
				this.inputStream.close();
				this.inputStream = null;
			}
		}
		catch (IOException ex) {
			cat.error("close(): Failure in closing streams", ex);

			throw new EPPConException("Failure closing streams : "
					+ ex.getMessage());
		}
		catch (SecurityException ex) {
			cat.error("close() : Failure in closing streams", ex);

			// ignore this one
		}
		finally {
			this.isConnected = false;
		}

		cat.debug("EPPSSLClientSocket.close(): Ending the Method");
	}

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

}
