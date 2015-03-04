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

/**
 * Interface to locate the EPP proxy server to use. This allows a client to
 * define their own mechanism to determine the proxy servers to use outside of
 * what is defined in the configuration file.
 */
public interface EPPProxyServersLocator {

	/**
	 * Returns a <code>List</code> of <code>EPPProxyServers</code> instances for 
	 * use in identifying the proxy servers to attempt to connect through 
	 * when configured with the <code>EPPSSLProxyClientSocket</code> or 
	 * <code>EPPPlainProxyClientSocket</code> classes.  
	 * 
	 * @return List of <code>EPPProxyServers</code> servers in the correct format if defined;<code>null</code>
	 *         otherwise.
	 */
	List getProxyServers();
}
