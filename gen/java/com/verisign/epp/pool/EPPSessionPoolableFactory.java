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


import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;

import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.transport.client.EPPSSLContext;
import com.verisign.epp.util.EPPCatFactory;

public class EPPSessionPoolableFactory implements PoolableObjectFactory {
	
	
	/** Log4j category for logging */
	private static Logger log =
		Logger.getLogger(
				EPPSessionPoolableFactory.class.getName(),
				EPPCatFactory.getInstance().getFactory());

	/**
	 * Client id used during session authentication.
	 */
	private String clientId;
	
	/**
	 * Password used during session authentication.
	 */
	private String password;
	
	/**
	 * Session absolute timeout in milliseconds
	 */
	private long absoluteTimeout;
	
	/**
	 * Idle timeout in milliseconds
	 */
	private long idleTimeout;
	
	/**
	 * Name or IP address of TCP server or URL of HTTP server.
	 */
	private String serverName;
	
	/**
	 * Port number of TCP server.  This attribute should be <code>null</code> 
	 * when connecting to a HTTP server.
	 */
	private Integer serverPort;
	
	/**
	 * Name or IP address to connect from.  When <code>null</code> the 
	 * host will be set to the loop back.
	 */
	private String clientHost = null;
	
	/**
	 * SSL context information
	 */
	private EPPSSLContext sslContext = null;
	
	
	/**
	 * Default constructor.  Must set the following attributes for using:<br>
	 * <br><ul>
	 * <li>clientId
	 * <li>password
	 * <li>absoluteTimeout
	 * <li>idleTimeout
	 * </ul>
	 */
	public EPPSessionPoolableFactory() {
		this.clientId = null;
		this.password = null;
		this.absoluteTimeout = -1;
		this.idleTimeout = -1;
	}
		
	/**
	 * Create an EPP session poolable factory with the client id, password used 
	 * to authenticate the session.  
	 * 
	 * @param aClientId Login id used to authenticate
	 * @param aPassword Password used to authenticate
	 * @param aAbsoluteTimeout Session absolute timeout
	 * @param aIdleTimeout Session idle timeout
	 */
	public EPPSessionPoolableFactory(String aClientId, String aPassword, 
			long aAbsoluteTimeout, long aIdleTimeout) {
		
		this.clientId = aClientId;
		this.password = aPassword;
		this.absoluteTimeout = aAbsoluteTimeout;
		this.idleTimeout = aIdleTimeout;
	}
	
	
	
	
	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * @param clientId The clientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
		

	/**
	 * Gets the session absolute timeout.
	 * 
	 * @return Absolute timeout in milliseconds
	 */
	public long getAbsoluteTimeout() {
		return this.absoluteTimeout;
	}
	/**
	 * Sets the session absolute timeout.
	 * 
	 * @param aAbsoluteTimeout Absolute timeout in milliseconds.
	 */
	public void setAbsoluteTimeout(long aAbsoluteTimeout) {
		this.absoluteTimeout = aAbsoluteTimeout;
	}
	/**
	 * Gets the session idle timeout.
	 * 
	 * @return The idle timeout in milliseconds
	 */
	public long getIdleTimeout() {
		return this.idleTimeout;
	}
	
	/**
	 * Sets the session idle timeout.
	 * 
	 * @param aIdleTimeout Idle session in milliseconds
	 */
	public void setIdleTimeout(long aIdleTimeout) {
		this.idleTimeout = aIdleTimeout;
	}
		
	

	/**
	 * Gets the TCP server IP address or host name, 
	 * or the URL of the HTTP server.
	 * 
	 * @return Server host name, IP address, or URL
	 */
	public String getServerName() {
		return this.serverName;
	}


	/**
	 * Sets the TCP server IP address or host name or 
	 * the URL of the HTTP server.
	 * 
	 * @param aServerName Server host name, IP address, or URL
	 */
	public void setServerName(String aServerName) {
		this.serverName = aServerName;
	}


	/**
	 * Gets the TCP server port number.  This will be 
	 * <code>null</code> if connecting to a HTTP server.
	 * 
	 * @return TCP server port number if defined; <code>null</code> otherwise.
	 */
	public Integer getServerPort() {
		return this.serverPort;
	}


	/**
	 * Sets the TCP server port number.
	 * 
	 * @param aServerPort TCP server port number
	 */
	public void setServerPort(Integer aServerPort) {
		this.serverPort = aServerPort;
	}
	
	
	/**
	 * Gets the TCP server IP address or host name 
	 * to connect from.  A <code>null</code> value will 
	 * use the loop back.
	 * 
	 * @return Client host name or IP address if defined;<code>null</code> otherwise.
	 */
	public String getClientHost() {
		return this.clientHost;
	}


	/**
	 * Sets the TCP server IP address or host name 
	 * to connect from.  A <code>null</code> value will 
	 * use the loop back.
	 * 
	 * @param aClientHost Client host name or IP address
	 */
	public void setClientHost(String aClientHost) {
		this.clientHost = aClientHost;
	}
	
	/**
	 * Gets the optional <code>EPPSSLContext</code> associated with the factory.  
	 * 
	 * @return SSL Context if defined; <code>null</code> otherwise
	 */
	public EPPSSLContext getSSLContext() {
		return sslContext;
	}

	/**
	 * Gets the optional <code>EPPSSLContext</code> associated with the factory.  
	 * 
	 * @param aSSLContext SSL Context to use by the factory
	 */
	public void setSSLContext(EPPSSLContext aSSLContext) {
		this.sslContext = aSSLContext;
	}

	
	/**
	 * Session being borrowed from the pool.  
	 * 
	 * @param aSession Session being returned
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#activateObject(java.lang.Object)
	 */
	public void activateObject(Object aSession) throws Exception {
		// Nothing for now
	}
	
	/** 
	 * Destroy object from the pool.
	 * 
	 * @param aSession Session being destroyed
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#destroyObject(java.lang.Object)
	 */
	public void destroyObject(Object aSession) throws Exception {
		log.debug("destroyObject(): enter, session id = " + aSession);
		EPPSession theSession = (EPPSession) aSession;
		
		// Try to end the session gracefully
		try {
			log.debug("destroyObject(): enter, calling end session");
			theSession.endSession();
		}
		catch (Exception ex) {
			// ignore, since the session is being removed
		}
	}
	
	/** 
	 * Creates a new session object.
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
	 */
	public Object makeObject() throws Exception {
		
		log.debug("makeObject(): enter");
		
		EPPSession theSession = this.makeSession();
		
		log.debug("makeObject(): Make session with id = " + theSession);
		
		theSession.setClientID(this.clientId);
		theSession.setPassword(this.password);

		log.debug("makeObject(): establishing session, with session class " + 
				theSession.getClass().getName());
		
		// Establish authenticated session
		try {
			theSession.initSession();
			
			log.debug("makeObject(): established session, with session class " + 
					theSession.getClass().getName());
		}
		catch (EPPCommandException ex) {
			log.error("makeObject(): error initializing session " + 
					theSession.getClass().getName() + ": " + ex);
			
			// Ensure that the connection is closed
			try {
				theSession.endConnection();
			}
			catch (EPPCommandException ex1) {
				// Ignore
			}
			
			throw ex;
		}
		
		log.debug("makeObject(): exit");
		return theSession;
	}
	
	/** 
	 * Session is being returned to the pool.  
	 * 
	 * @param aSession Session being returned
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#passivateObject(java.lang.Object)
	 */
	public void passivateObject(Object aSession) throws Exception {
		// Nothing for now
	}
	
	/** 
	 * Validates a session by sending a keep alive.  If an exception 
	 * occurs from the keep alive, than the session is not valid.  
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#validateObject(java.lang.Object)
	 * 
	 * @param aSession Session to validate
	 * 
	 * @return <code>true</code> if the session is valid; <code>false</code> otherwise.
	 */
	public boolean validateObject(Object aSession) {
		log.debug("validateObject(): enter, session id = " + aSession);
		EPPSession theSession = (EPPSession) aSession;
		EPPPooledSession thePooledSession = (EPPPooledSession) aSession;
		long currentTime = System.currentTimeMillis();
		boolean isValid;
		
		try {
			// Is session past absolute timeout?
			if (currentTime - thePooledSession.getCreatedTime() > 
					this.getAbsoluteTimeout()) {
				log.debug("validateObject(): session id = " + aSession + " is past absolute timeout");				
				isValid = false;
			} // Idle timeout?
			else if (System.currentTimeMillis() - thePooledSession.getLastTouched() > 
					this.getIdleTimeout()) {
				log.debug("validateObject(): session id = " + aSession + " is past idle timeout, sending hello");				
				theSession.hello();
				thePooledSession.touch();
				isValid = true;
			}
			else {
				log.debug("validateObject(): session id = " + aSession + " is valid");				
				isValid = true;
			}
			
		}
		catch (Exception ex) {
			log.debug("validateObject(): session id = " + aSession + " caused Exception: " + ex);				
			isValid = false;
		}
		
		log.debug("validateObject(): exit, isValid = " + isValid);
		return isValid;
	}
	
	/**
	 * Make an EPP session instance for pool.  This can be overridden by a 
	 * derived class to create a custom EPP session instance (i.e. HTTP).
	 * 
	 * @return <code>EPPSession</code> instance
	 */
	protected EPPSession makeSession() throws Exception {
		log.debug("makeSession(): enter");
		
		if (this.serverName == null || this.serverPort == null) {
			throw new EPPSessionPoolException("makeSession(): serverName or serverPort is null");
		}
		
		if (this.clientHost == null) {
			return new EPPPooledGenericSession(this.serverName, this.serverPort.intValue(), this.sslContext);			
		}
		else {
			return new EPPPooledGenericSession(this.serverName, this.serverPort.intValue(), this.clientHost, this.sslContext);			
		}
		
	}
	
	
	
}
