/***********************************************************
Copyright (C) 2011 VeriSign, Inc.

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
 * 
 */
package com.verisign.epp.transport.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;

/**
 * EPP SSL client socket connection that goes through the Apache server
 * mod_proxy. The interface is exactly the same as EPPSSLClientSocket, except
 * one additional configuration properties that defines the {@link EPPProxyServersLocator} 
 * class that's used to get the list of proxy servers to connect through.  The 
 * <code>EPPProxyServersLocator</code> configuration property defines the location class.
 * Additional configuration properties might be required including:<br>
 * <br><ul>
 * <li><code>EPP.ProxyServers</code> - Defines the list of proxy servers when 
 * <code>EPPProxyServersLocator</code> is set to <code>com.verisign.epp.transport.client.EPPConfigProxyServersLocator</code>.
 * <li><code>EPP.ProxyServersRandomize</code> is set to <code>true</code> to enable 
 * randomization of the proxy servers to attempt to connect through and <code>false</code> otherwise.  
 * The default setting is <code>true</code> if <code>EPP.ProxyServersRandomize</code> is not set.
 * </ul>
 * <br>
 * The {@link EPPProxyServersLocator} is a static attribute for getting
 * the list of proxy servers connect through.  By default in the constructor 
 * the {@link EPPProxyServersLocator} is set based on the <code>EPPProxyServersLocator</code> 
 * configuration property.  If additional initialization is required for the 
 * {@link EPPProxyServersLocator} used, then the {@link #setLocator(EPPProxyServersLocator)} 
 * should be called prior to instantiating an instance of <code>EPPSSLProxyClientSocket</code> 
 * indirectly via the <code>EPPSession</code>.
 */
public class EPPSSLProxyClientSocket extends EPPSSLClientSocket {

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPSSLProxyClientSocket.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * List of {@link ProxyServer} instances creating from parsing the
	 * EPP.ProxyServers configuration property by {@link
	 * ProxyServer.decodeConfig(String)}.
	 */
	private List proxyServers;

	/**
	 * Randomize <code>proxyServers</code>
	 */
	private boolean proxyServersRandomize;

	/**
	 * Cached version of the locator. This static attribute is lazily
	 * initialized.
	 */
	private static EPPProxyServersLocator locator = null;

	/**
	 * Constructor for initializing a new client socket connection that goes
	 * throw one of a list of proxy servers defined by the EPP.ProxyServers
	 * configuration property.
	 * 
	 * @throws EPPConException
	 *             Error creating <code>EPPSSLProxyClientSocket</code> instance.
	 */
	public EPPSSLProxyClientSocket() throws EPPConException {
		super();

		cat.debug("EPPSSLProxyClientSocket(): start");

		// Get the proxy server properties
		try {
			// Locator not previously instantiated?
			if (EPPSSLProxyClientSocket.locator == null) {

				String theProxyServerLocatorProp = EPPEnv
						.getProxyServerLocator();

				Class locatorClass = Class.forName(theProxyServerLocatorProp);

				if (!EPPProxyServersLocator.class
						.isAssignableFrom(locatorClass)) {
					throw new EPPConException(theProxyServerLocatorProp
							+ " does not implement EPPProxyServersLocator");
				}

				EPPSSLProxyClientSocket.locator = (EPPProxyServersLocator) locatorClass
						.newInstance();
			}

			this.proxyServers = EPPSSLProxyClientSocket.locator
					.getProxyServers();

			this.proxyServersRandomize = EPPEnv.getProxyServersRandomize();
		}
		catch (Exception ex) {
			cat.error("Connection Failed Due to : " + ex.getMessage(), ex);

			throw new EPPConException("Connection Failed Due to : "
					+ ex.getMessage());
		}

		// No proxy servers?
		if (this.proxyServers.size() == 0) {
			throw new EPPConException(
					"No proxy servers defined by EPP.ProxyServers configuration property");
		}

		cat.debug("EPPSSLProxyClientSocket(): exit");
	}

	/**
	 * Override of the {@link EPPSSLClientSocket#initialize()} that will first
	 * connect to the Apache proxy server, send the target server connection
	 * information to the proxy server, and then initialize the SSL connection
	 * through the proxy server.
	 * 
	 * @exception EPPConException
	 *                Error with SSL connection throw the Apache proxy server
	 */
	public void initialize() throws EPPConException {
		cat.debug("initialize(): start");

		// Ensure that the SSLContext is set
		if (super.getSslContext() == null) {
			EPPSSLImpl.initialize();
			super.setSslContext(EPPSSLImpl.getEPPSSLContext());
		}

		// Get the SSLSocketFactory from EPPSSLImpl
		SSLSocketFactory theSSLSocketFactory = super.getSslContext()
				.getSSLSocketFactory();

		boolean theConnectedToProxy = false;

		List theProxyList = new ArrayList(this.proxyServers);
		Random theRandom = new Random();
		Socket theSocket = null;
		EPPProxyServer theCurrProxy = null;

		cat.debug("initialize(): proxy server list size = "
				+ theProxyList.size());

		// Not connected and more proxy servers
		while (!theConnectedToProxy && theProxyList.size() != 0) {

			int theCurrProxyIndex;

			// Randomize the proxy servers used?
			if (this.proxyServersRandomize) {
				theCurrProxyIndex = theRandom.nextInt(theProxyList.size());
			}
			else {
				theCurrProxyIndex = 0;
			}

			cat.debug("initialize(): proxy server selected index "
					+ theCurrProxyIndex + " of " + theProxyList.size()
					+ " servers");

			theCurrProxy = (EPPProxyServer) theProxyList.get(theCurrProxyIndex);
			theProxyList.remove(theCurrProxyIndex);

			cat.debug("initialize(): proxy server = " + theCurrProxy);

			InetSocketAddress theSocketAddress = new InetSocketAddress(
					theCurrProxy.getServerName(), theCurrProxy.getServerPort());

			theSocket = new Socket();

			try {

				// Want to specify the client host?
				if (super.getClientHostName() != null) {
					cat.debug("initialize(): Binding to client address = "
							+ super.getClientHostName());
					InetSocketAddress theClientSocketAddress = new InetSocketAddress(
							super.getClientHostName(), 0);
					theSocket.bind(theClientSocketAddress);
				}

				cat.debug("initialize(): Attempting to connect proxy server = "
						+ theCurrProxy);

				theSocket.connect(theSocketAddress, super.getConTimeout());

				theSocket.setSoTimeout(super.getConTimeout());

				// Set proxy packets to connect to target server
				OutputStream theStream = theSocket.getOutputStream();

				String theLine;

				theLine = "CONNECT " + super.getHostName() + ":"
						+ super.getPortNumber() + " HTTP/1.1\r\n";
				cat.debug("Proxy Server Send [" + theLine + "]");
				theStream.write(theLine.getBytes());

				theLine = "Host: " + super.getHostName() + ":"
						+ super.getPortNumber() + "\r\n";
				cat.debug("Proxy Server Send [" + theLine + "]");
				theStream.write(theLine.getBytes());

				theLine = "\r\n";
				cat.debug("Proxy Server Send [" + theLine + "]");
				theStream.write(theLine.getBytes());

				theStream.flush();

				BufferedReader theInputStream = new BufferedReader(
						new InputStreamReader(theSocket.getInputStream()));

				// Validate the connection to Proxy Server and read all output
				int theLineNum = 1;
				do {
					theLine = theInputStream.readLine();
					cat.debug("Proxy output line [" + theLine + "]");

					// Validate the connection through the Proxy Server
					if (theLineNum == 1) {
						if (theLine != null
								&& (theLine.indexOf("Connection Established") != -1)) {
							theConnectedToProxy = true;
							cat.info("Proxy Server " + theCurrProxy
									+ " TCP connection established");
						}
						else {
							cat.info("Proxy Server " + theCurrProxy
									+ " TCP connection failed: " + theLine);
							theSocket.close();
							break;
						}
					}

					theLineNum++;
				}
				while (theLine != null && theLine.length() != 0);

			}
			catch (IOException ex) {
				cat.info("Proxy Server " + theCurrProxy
						+ " TCP connection failed: " + ex);
			}

		}

		if (!theConnectedToProxy) {
			cat.error("All Proxy Server connections failed");
			throw new EPPConException("All Proxy Server connections failed");
		}

		// Create the SSLSocket connection using the proxy Socket connection
		SSLSocket theSSLSocket;
		try {
			theSSLSocket = (SSLSocket) theSSLSocketFactory
					.createSocket(theSocket, super.getHostName(), super
							.getPortNumber(), true);
		}
		catch (IOException ex) {
			cat.error("Proxy Server " + theCurrProxy
					+ " failure creating SSLSocket connection to "
					+ super.getHostName() + ":" + super.getPortNumber() + ": "
					+ ex);
			throw new EPPConException("Proxy Server " + theCurrProxy
					+ " failure creating SSLSocket connection to "
					+ super.getHostName() + ":" + super.getPortNumber() + ": "
					+ ex);
		}

		// Log the SSL supported protocols and cipher suites?
		if (cat.isDebugEnabled()) {
			cat
					.debug("EPPSSLClientSocket.initialize(): Supported Protocols = ["
							+ this.getSSLPropertyListString(theSSLSocket
									.getSupportedProtocols())
							+ "], Supported Cipher Suites = ["
							+ this.getSSLPropertyListString(theSSLSocket
									.getSupportedCipherSuites()) + "]");
		}

		// Set additional SSL handshake properties (enabled protocols and
		// enabled cipher suites)
		if (super.getSslContext().hasSSLEnabledProtocols()) {
			cat.debug("EPPSSLClientSocket.initialize(): Enabled Protocols = ["
					+ this.getSSLPropertyListString(super.getSslContext()
							.getSSLEnabledProtocols()) + "]");
			theSSLSocket.setEnabledProtocols(super.getSslContext()
					.getSSLEnabledProtocols());
		}
		else {
			cat
					.debug("EPPSSLClientSocket.initialize(): Enabled Protocols NOT specified, using providers default");
		}

		if (super.getSslContext().hasSSLEnabledCipherSuites()) {
			cat
					.debug("EPPSSLClientSocket.initialize(): Enabled Cipher Suites = ["
							+ this.getSSLPropertyListString(super
									.getSslContext()
									.getSSLEnabledCipherSuites()) + "]");
			theSSLSocket.setEnabledCipherSuites(super.getSslContext()
					.getSSLEnabledCipherSuites());
		}
		else {
			cat
					.debug("EPPSSLClientSocket.initialize(): Enabled Cipher Suites NOT specified, using providers default");
		}

		cat.debug("EPPSSLClientSocket.initialize(): SSL startHandshake");

		// Start the SSL handshake
		try {
			theSSLSocket.startHandshake();
		}
		catch (IOException ex) {
			cat.error("Proxy Server " + theCurrProxy
					+ " failed with SSL handshake to " + super.getHostName()
					+ ":" + super.getPortNumber(), ex);

			throw new EPPConException("Proxy Server " + theCurrProxy
					+ " failed with SSL handshake to " + super.getHostName()
					+ ":" + super.getPortNumber() + ": " + ex.getMessage());
		}

		try {
			super.setInputStream(theSSLSocket.getInputStream());
			super.setOutputStream(theSSLSocket.getOutputStream());
			super.setSocket(theSSLSocket);
			super.setConnected(true);
		}
		catch (IOException ex) {
			cat.error("Failed getting streams from SSL socket: "
					+ ex.getMessage());
			throw new EPPConException(
					"Failed getting streams from SSL socket: "
							+ ex.getMessage());
		}

		cat.debug("initialize(): exit");
	}

	/**
	 * Gets the current {@link EPPProxyServersLocator} used by
	 * <code>EPPSSLProxyClientSocket</code>. The locator is a static / global
	 * setting for creating socket connections to define the set of proxy
	 * servers to connect through. The locator set can dynamically change the
	 * proxy servers to connect through. If not define before an
	 * <code>EPPSSLProxyClientSocket</code> is instantiated, the default locator
	 * is defined by the <code>EPP.ProxyServersLocator</code> configuration
	 * property.
	 * 
	 * @return {@link EPPProxyServersLocator} instance if defined;
	 *         <code>null</code> otherwise.
	 */
	public static EPPProxyServersLocator getLocator() {
		return locator;
	}

	/**
	 * Sets the {@link EPPProxyServersLocator} to use when getting the list of
	 * proxy servers to connect through. If not define before an
	 * <code>EPPSSLProxyClientSocket</code> is instantiated, the default locator
	 * is defined by the <code>EPP.ProxyServersLocator</code> configuration
	 * property, so if a {@link EPPProxyServersLocator} objects needs further
	 * initialized outside the default constructor,
	 * {@link #setLocator(EPPProxyServersLocator)} must be called before
	 * <code>EPPSSLProxyClientSocket</code> is instantiated indirectly in
	 * creating an <code>EPPSession</code>.
	 * 
	 * @param aLocator
	 *            {@link EPPProxyServersLocator} instance to use to get the list
	 *            of proxy servers to connect through.
	 */
	public static void setLocator(EPPProxyServersLocator aLocator) {
		locator = aLocator;
	}

}
