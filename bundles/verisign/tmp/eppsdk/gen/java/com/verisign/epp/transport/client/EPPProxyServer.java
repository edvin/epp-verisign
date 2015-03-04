/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2011 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */


package com.verisign.epp.transport.client;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;

/**
 * Represents a Proxy Server with an 
 * IP address / Host Name and a port number.  
 * Utility methods are includes to help parse 
 * the <code>EPPEnv.getProxyServers()</code> value 
 * which is driven by the <code>EPP.ProxyServers</code>
 * configuration property.  
 */
public class EPPProxyServer {
	
	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPProxyServer.class
			.getName(), EPPCatFactory.getInstance().getFactory());


	/**
	 * Constant for when no port has been defined.
	 */
	static public int DEFAULT_PORT = 80;

	/**
	 * Server name or IP address of proxy server
	 */
	private String serverName;

	/**
	 * Server port of proxy server
	 */
	private int serverPort = DEFAULT_PORT;

	/**
	 * Default constructor
	 */
	public EPPProxyServer() {
	}

	/**
	 * Constructor that takes both the server name and port.
	 * 
	 * @param aServerName
	 *            Server name or IP address of proxy server
	 * @param aServerPort
	 *            Server port of proxy server
	 */
	public EPPProxyServer(String aServerName, int aServerPort) {
		this.serverName = aServerName;
		this.serverPort = aServerPort;
	}

	/**
	 * Gets the proxy server name or IP address.
	 * 
	 * @return Proxy server name if set; <code>null</code> otherwise.
	 */
	public String getServerName() {
		return this.serverName;
	}

	/**
	 * Sets the proxy server name or IP address.
	 * 
	 * @param aServerName
	 *            Proxy server name or IP address.
	 */
	public void setServerName(String aServerName) {
		this.serverName = aServerName;
	}

	/**
	 * Gets the proxy server port number.
	 * 
	 * @return proxy server port number with default of
	 *         {@link #DEFAULT_PORT} if not explicitly set.
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * Sets the proxy server port number.
	 * 
	 * @param aServerPort
	 *            proxy server port number
	 */
	public void setServerPort(int aServerPort) {
		this.serverPort = aServerPort;
	}

	/**
	 * Parsing a proxy server configuration item that meets the regular
	 * expression. "^\[?(\S+)\]?:(\d+)$". IPv6 addresses need to be
	 * encapsulated in brackets since they use ':' as a separator.
	 * 
	 * @param aConfigItem
	 *            Proxy server configuration item to parse
	 * 
	 * @exception EPPConException
	 *                failure in parsing the <code>aConfigItem</code>
	 */
	public void decodeConfigItem(String aConfigItem) throws EPPConException {
		cat.debug("Starting decodeConfigItem");

		Pattern pattern = Pattern.compile("^\\[?((\\p{Alnum}|:|-|\\.)+)\\]?:(\\d+)$");
		Matcher matcher = pattern.matcher(aConfigItem);

		// Found a match with the expected number of groups?
		if (matcher.matches() && matcher.groupCount() == 3) {			
			// proxy server name
			this.serverName = matcher.group(1);

			// proxy server port
			this.serverPort = Integer.parseInt(matcher.group(3));
			
			cat.debug("Decoding proxy server, name = " + this.serverName + ", port = " + this.serverPort);
		}
		else {
			cat.error("Proxy server config item \""
					+ aConfigItem + "\" format is invalid");
			throw new EPPConException("Proxy server config item \""
					+ aConfigItem + "\" format is invalid");
		}
		
		cat.debug("Ending decodeConfigItem");
	}

	/**
	 * Decodes the configuration value returned by <code>EPPEnv.getProxyServers()</code>.
	 * 
	 * @param aConfigValue
	 *            Configuration value returned by <code>EPPEnv.getProxyServers()</code>.
	 * @return List of decoded <code>ProxyServer</code> instances.
	 * @throws EPPConException
	 *             Error decoding the configuration value
	 */
	static List decodeConfig(String aConfigValue) throws EPPConException {
		
		cat.debug("Starting decodeConfig");

		if (aConfigValue == null) {
			cat.error("aConfigValue parameter is null");
			throw new EPPConException(
					"ProxyServer.decodeConfig() aConfigValue parameter is null");
		}

		List theServers = new ArrayList();
		
		cat.debug("decodeConfig aConfigValue = " + aConfigValue);

		StringTokenizer theTokenizer = new StringTokenizer(aConfigValue, ",");

		while (theTokenizer.hasMoreTokens()) {
			String theConfigItem = theTokenizer.nextToken();

			cat.debug("Found proxy server \"" + theConfigItem + "\"");
			EPPProxyServer theServer = new EPPProxyServer();
			theServer.decodeConfigItem(theConfigItem);

			theServers.add(theServer);
		}
		
		cat.debug("Number of proxy servers = " + theServers.size());

		cat.debug("Ending decodeConfig");
		return theServers;
	}
	
	/**
	 * Convert the <code>EPPProxyServer</code> attributes into a 
	 * <code>String</code> by separating the name and port with a colon 
	 * and enclosing the name in braces if the name contains a colon to
	 * support encoding an IPv6 proxy server.
	 * 
	 * @return Encoded <code>EPPProxyServer</code> as a <code>String</code>
	 */
	public String toString() {
		if (this.serverName != null && (this.serverName.indexOf(':') != -1)) {
			return "[" + this.serverName + "]:" + this.serverPort;
		}
		else {
			return "" + this.serverName + ":" + this.serverPort;
		}
	}

} // End class ProxyServer
