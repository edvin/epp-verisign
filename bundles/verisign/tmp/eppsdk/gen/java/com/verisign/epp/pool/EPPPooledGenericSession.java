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
package com.verisign.epp.pool;

import org.apache.log4j.Logger;

import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.transport.client.EPPSSLContext;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Pooled generic <code>EPPSession</code>.  Timestamp attributes were added
 * to handle absolute session timeout and ensure that idle timeouts don't 
 * occur.  
 */
public class EPPPooledGenericSession extends EPPSession implements EPPPooledSession {

	/** Log4j category for logging */
	private static Logger log =
		Logger.getLogger(
				EPPPooledGenericSession.class.getName(),
				EPPCatFactory.getInstance().getFactory());
		
	/**
	 * When pooled object was created
	 */
	private long createdTime = System.currentTimeMillis();
	
	/**
	 * Last time session was used or a keep alive was sent
	 */
	private long lastTouched = System.currentTimeMillis();
		
	/**
	 * Default constructor for <code>EPPPooledGenericSession</code>.
	 * 
	 * @throws EPPCommandException On error
	 */
	public EPPPooledGenericSession() throws EPPCommandException {
		super();
	}
	
	/**
	 * Constructor that takes an explicit host name and port number 
	 * to connect to.  
	 * 
	 * @param aHostName Host name or IP address of server
	 * @param aPortNumber Server port number
	 * 
	 * @throws EPPCommandException Error connecting to server
	 */
	public EPPPooledGenericSession(String aHostName, int aPortNumber) throws EPPCommandException {
		super(aHostName, aPortNumber);
	}
	
	
	/**
	 * Constructor that takes an explicit server host name, server port number, 
	 * and client host name to connect to.  
	 * 
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aClientHost Host name or IP address to connect from
	 * 
	 * @throws EPPCommandException Error connecting to server
	 */
	public EPPPooledGenericSession(String aHostName, int aPortNumber, String aClientHost) throws EPPCommandException {
		super(aHostName, aPortNumber, aClientHost);
	}
	
	/**
	 * Constructor that takes an explicit host name and port number 
	 * to connect to.  
	 * 
	 * @param aHostName Host name or IP address of server
	 * @param aPortNumber Server port number
	 * @param aSSLContext Optional specific SSL context to use
	 * 
	 * @throws EPPCommandException Error connecting to server
	 */
	public EPPPooledGenericSession(String aHostName, int aPortNumber, EPPSSLContext aSSLContext) throws EPPCommandException {
		super(aHostName, aPortNumber, aSSLContext);
	}
	
	
	/**
	 * Constructor that takes an explicit server host name, server port number, 
	 * and client host name to connect to.  
	 * 
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aClientHost Host name or IP address to connect from
	 * @param aSSLContext Optional specific SSL context to use
	 * 
	 * @throws EPPCommandException Error connecting to server
	 */
	public EPPPooledGenericSession(String aHostName, int aPortNumber, String aClientHost, EPPSSLContext aSSLContext) throws EPPCommandException {
		super(aHostName, aPortNumber, aClientHost, aSSLContext);
	}
	
	/**
	 * Gets the time the pooled object was created.
	 * 
	 * @return Epoch time of creation
	 */
	public long getCreatedTime() {
		return createdTime;
	}
	
	/**
	 * Gets the last time the pooled object was touched.
	 * 
	 * @return Epoch time of touch
	 */
	public long getLastTouched() {
		return lastTouched;
	}
	
	/**
	 * Sets the last touched to the current time.
	 */
	public void touch() {
		this.lastTouched = System.currentTimeMillis();
	}
	
}