/***********************************************************
 Copyright (C) 2006 VeriSign, Inc.

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

package com.verisign.epp.transport.client;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;


/**
 * The <code>EPPSSLContext</code> contains initialized SSL 
 * objects that can be used to establish SSL connections.  The attributes 
 * include an <code>SSLContext</code>, an <code>SSLSocketFactory</code>, 
 * and an optional set of SSL enabled protocols.  
 */
public class EPPSSLContext {

	/** Secure socket implementation */
	private SSLContext sslContext = null;

	/** SSL Socket Factory */
	private SSLSocketFactory sslSocketFactory = null;

	/**
	 * SSL enabled protocols. If <code>null</code> than it will use the
	 * default for the provider.
	 */
	private String[] sslEnabledProtocols = null;
	
	/**
	 * SSL enabled cipher suites.  If <code>null</code> than it will use 
	 * the default cipher suites for the provider.
	 */
	private String[] sslEnabledCipherSuites = null;
	
	
	/**
	 * Default constructor. 
	 */
	public EPPSSLContext() {
		
	}

	/**
	 * Creates an instance of <code>EPPSSLContext</code> that takes the required set of 
	 * attributes.
	 * 
	 * @param aSSLContext <code>SSLContext</code> instance
	 */
	public EPPSSLContext(SSLContext aSSLContext) {
		this.sslContext = aSSLContext;
		this.sslSocketFactory = aSSLContext.getSocketFactory();
	}
	
	/**
	 * Creates an instance of <code>EPPSSLContext</code> that all of the 
	 * attributes.
	 * 
	 * @param aSSLContext <code>SSLContext</code> instance
	 * @param aSslEnabledProtocols <code>String</code> array of enabled SSL protocols
	 * @param aSslEnabledCipherSuites <code>String</code> array of enabled SSL cipher suites
	 */
	public EPPSSLContext(SSLContext aSSLContext, String[] aSslEnabledProtocols, String[] aSslEnabledCipherSuites) {
		this(aSSLContext);
		this.sslEnabledProtocols = aSslEnabledProtocols;
		this.sslEnabledCipherSuites = aSslEnabledCipherSuites;
	}

	
	
	/** 
	 * Gets the <code>SSLContext</code>.
	 * 
	 * @return <code>SSLContext</code> instance
	 */
	public SSLContext getSSLContext() {
		return this.sslContext;
	}

	/** 
	 * Sets the <code>SSLContext</code>.
	 * 
	 * @param aSSLContext <code>SSLContext</code> to set 
	 */
	public void setSSLContext(SSLContext aSSLContext) {
		this.sslContext = aSSLContext;
	}

	/** 
	 * Checks whether <code>SSLContext</code> has been set.
	 * 
	 * @return <code>true</code> if set; <code>false</code> otherwise.
	 */
	public boolean hasSSLContext() {
		return this.sslContext == null ? false : true;
	}

	/**
	 * Gets the <code>SSLSocketFactory</code>.
	 * 
	 * @return <code>SSLSocketFactory</code> instance
	 */
	public SSLSocketFactory getSSLSocketFactory() {
		return this.sslSocketFactory;
	}

	/**
	 * Sets the <code>SSLSocketFactory</code>, which should be the <code>SocketFactory</code>
	 * of the <code>SSLContext</code> attribute.
	 * 
	 * @param aSSLSocketFactory <code>SSLSocketFactory</code> instance
	 */
	public void setSSLSocketFactory(SSLSocketFactory aSSLSocketFactory) {
		this.sslSocketFactory = aSSLSocketFactory;
	}

	/** 
	 * Checks whether <code>SSLSocketFactory</code> has been set.
	 * 
	 * @return <code>true</code> if set; <code>false</code> otherwise.
	 */
	public boolean hasSSLSocketFactory() {
		return this.sslSocketFactory == null ? false : true;
	}

	/**
	 * Gets the optional SSL enabled protocols <code>String</code> array.  
	 * 
	 * @return <code>>String</code> array if set; <code>null</code> otherwise.
	 */
	public String[] getSSLEnabledProtocols() {
		return this.sslEnabledProtocols;
	}

	/**
	 * Sets the optional SSL enabled protocols <code>String</code> array.
	 * 
	 * @param aSslEnabledProtocols <code>String</code> array of enabled SSL protocols
	 */
	public void setSSLEnabledProtocols(String[] aSslEnabledProtocols) {
		this.sslEnabledProtocols = aSslEnabledProtocols;
	}
	
	/** 
	 * Checks whether SSL enabled protocols has been set.
	 * 
	 * @return <code>true</code> if set; <code>false</code> otherwise.
	 */
	public boolean hasSSLEnabledProtocols() {
		return this.sslEnabledProtocols == null ? false : true;
	}
	
	
	/**
	 * Gets the optional SSL enabled cipher suites <code>String</code> array.  
	 * 
	 * @return <code>>String</code> array if set; <code>null</code> otherwise.
	 */
	public String[] getSSLEnabledCipherSuites() {
		return this.sslEnabledCipherSuites;
	}

	/**
	 * Sets the optional SSL enabled cipher suites <code>String</code> array.
	 * 
	 * @param aSslEnabledCipherSuites <code>String</code> array of enabled SSL cipher suites
	 */
	public void setSSLEnabledCipherSuites(String[] aSslEnabledCipherSuites) {
		this.sslEnabledCipherSuites = aSslEnabledCipherSuites;
	}
	
	/** 
	 * Checks whether SSL enabled cipher suites has been set.
	 * 
	 * @return <code>true</code> if set; <code>false</code> otherwise.
	 */
	public boolean hasSSLEnabledCipherSuites() {
		return this.sslEnabledCipherSuites == null ? false : true;
	}
	

}
