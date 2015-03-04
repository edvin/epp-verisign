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

import java.util.List;

import org.apache.log4j.Logger;

import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;

/**
 * Default implementation of the {@link EPPProxyServersLocator} interface that
 * returns the proxy servers setting from the configuration file that are
 * defined by the EPP.ProxyServers property.
 */
public class EPPConfigProxyServersLocator implements EPPProxyServersLocator {

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(
			EPPConfigProxyServersLocator.class.getName(), EPPCatFactory
					.getInstance().getFactory());

	/**
	 * Cached version of the proxy servers. This static attribute is lazily
	 * initialized.
	 */
	private static List proxyServers = null;

	/**
	 * Gets the proxy servers from the {@link EPPEnv} class using the {@link
	 * EPPEnv#getProxyServers()} method and converts them to a <code>List</code>
	 * of <code>EPPProxyServer</code> instances.
	 * 
	 * @return List of <code>EPPProxyServers</code> servers in the correct format if
	 *         defined;<code>null</code> otherwise.
	 */
	public List getProxyServers() {

		// If the proxy servers have not already been retrieved
		if (EPPConfigProxyServersLocator.proxyServers == null) {
			try {
				EPPConfigProxyServersLocator.proxyServers = EPPProxyServer
						.decodeConfig(EPPEnv.getProxyServers());
			}
			catch (Exception ex) {
				cat.error("Failure getting proxy servers from the config", ex);
			}
		}

		return EPPConfigProxyServersLocator.proxyServers;
	}

}
