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

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.transport.client.EPPSSLConfig;
import com.verisign.epp.transport.client.EPPSSLContext;
import com.verisign.epp.transport.client.EPPSSLImpl;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;

/**
 * EPP session pool for a specific system name that will handle creating sessions 
 * dynamically, expiring session after an absolute timeout, keeping sessions
 * alive based on an idle timeout, and dynamically grows and shrinks the 
 * pool based on configurable settings.  One of the <code>init</code> methods
 * must be called before useing the pool.  The {@link #init()} method uses
 * configuration settings defined in the {@link com.verisign.epp.util.Environment} 
 * class.  The {@link com.verisign.epp.util.Environment} settings include the 
 * following based on the system name defined by &lt;system&gt;:<br>
 * <br><ul>
 * <li><code>EPP.SessionPool.&lt;system&gt;.poolableClassName</code> - (required) EPPSessionPoolableFactory
 * class used by the pool.  
 * <li><code>EPP.SessionPool.&lt;system&gt;.clientId</code> - (required) Login name
 * <li><code>EPP.SessionPool.&lt;system&gt;.password</code> - (required) password
 * <li><code>EPP.SessionPool.&lt;system&gt;.serverName</code> - Name or IP address of TCP server or 
 * URL of HTTP server
 * <li><code>EPP.SessionPool.&lt;system&gt;.serverPort</code> - (optional) TCP server port number.  
 * This is not used for a HTTP server connection
 * <li><code>EPP.SessionPool.&lt;system&gt;.absoluteTimeout</code> - (optional) Session absolute timeout. 
 * Default is 24 hours
 * <li><code>EPP.SessionPool.&lt;system&gt;.idleTimeout</code> - (optional) Session idle timeout used 
 * to determine when keep alive messages are sent.  Default is 10 minutes.
 * <li><code>EPP.SessionPool.&lt;system&gt;.maxIdle</code> - (optional) Maximum number of idle sessions in pool.
 * Default is 10.
 * <li><code>EPP.SessionPool.&lt;system&gt;.initMaxActive</code> - (optional) Boolean value indicating if 
 * the <code>maxActive</code> sessions should be pre-initialized at 
 * initialization in the {@link #init()} method.  Default is <code>false</code>.
 * <li><code>EPP.SessionPool.&lt;system&gt;.maxActive</code> - (optional) Maximum number of active sessions 
 * in pool.  Default is 10.
 * <li><code>EPP.SessionPool.&lt;system&gt;.maxWait</code> - (optional) Maximum time in milliseconds for a 
 * client to block waiting for a pooled session.  Default is 60 seconds.
 * <li><code>EPP.SessionPool.&lt;system&gt;.minIdle</code> - (optional) Minimum number of idle sessions 
 * in the pool.  Default is 0.
 * <li><code>EPP.SessionPool.&lt;system&gt;.timeBetweenEvictionRunsMillis</code> - (optional) Frequency in milliseconds 
 * of scanning the pool for idle and absolute timeout sessions.  Default is 
 * 60 seconds.  
 * <li><code>EPP.SessionPool.&lt;system&gt;.borrowRetries</code> - (optional) Number of retries to get/create a session
 * when calling {@link #borrowObject()}.  Default is <code>0</code>.  
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLProtocol</code> - (optional) SSL protocol to use.  If defined 
 * the pool will have its own SSL configuration.  The required SSL properties include 
 * <code>SSLKeyStore</code>, <code>SSLKeyFileName</code>, and <code>SSLKeyPassPhrase</code>
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLKeyStore</code> - (optional) Type of identity KeyStore.  Required if 
 * <code>SSLProtocol</code> is defined for pool.  
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLKeyFileName</code> - (optional) Name of the identity KeyStore file.  Required if 
 * <code>SSLProtocol</code> is defined for pool.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLPassPhrase</code> - (optional) The passphrase/password to access 
 * the identity KeyStore file defined by <code>SSLKeyFileName</code>.  Required if 
 * <code>SSLProtocol</code> is defined for pool.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLKeyPassPhrase</code> - (optional) the passphrase/password for the 
 * private key stored in the identity KeyStore.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLTrustStore</code> - (optional) KeyStore type of the Trust Store
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLTrustStoreFileName</code> - (optional) The name of the Trust Store file.
 * If not defined for the pool, the default JDK Trust Store 
 * will be used that is located at the path <code>$JAVA_HOME/lib/security/cacerts</code>.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLTrustStorePassPhrase</code> - (optional) The passphrase/password to access 
 * the Trust Store file defined by the pool <code>SSLTrustStoreFileName</code> property.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLEnabledProtocols</code> - (optional) The space delimited list of 
 * enabled SSL protocols.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLEnabledCipherSuites</code> - (optional) The space delimited list of 
 * SSL cipher suites.
 * <li><code>EPP.SessionPool.&lt;system&gt;.SSLDebug</code> - (optional) Defines the SSL debug Java system property 
 * <code>javax.net.debug</code> value. 
 * </ul>
 */
public class EPPSystemSessionPool  {
	
	/**
	 * The default session absolute timeout.
	 */
	public static final long DEFAULT_ABSOLUTE_TIMEOUT = 24 * 60 * 60 * 1000; // 24 hours

	/**
	 * The default session absolute timeout.
	 */
	public static final long DEFAULT_IDLE_TIMEOUT = 10 * 60 * 1000; // 10 minutes

	 /**
     * The default maximum amount of time (in millis) the
     * {@link #borrowObject} method should block before throwing
     * an exception.
     */
   public static final long DEFAULT_MAX_WAIT = 60 * 1000; // 60 second blocking time
    
    /**
     * The default "time between eviction runs" value.
     */
    public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 1 * 60 * 1000; // Every 1 minute

    /**
     * The default cap on the number of "sleeping" instances in the pool.
     */
    public static final int DEFAULT_MAX_IDLE  = 10;

    /**
     * The default cap on the total number of active instances from the pool.
      */
    public static final int DEFAULT_MAX_ACTIVE  = 10;

    /**
     * The default minimum number of "sleeping" instances in the pool
     * before before the evictor thread (if active) spawns new objects.
     */
    public static final int DEFAULT_MIN_IDLE = 0;
    
	/**
	 * Default for the <code>initMaxActive</code> property, which is <code>false</code>.
	 */
	private static final boolean DEFAULT_INIT_MAX_ACTIVE = false;
    
	/**
	 * Default number of retries when attempting to get/create a session when calling 
	 * {@link #borrowObject()}.        
	 */
	private static final int DEFAULT_BORROW_RETRIES = 0;
   
    
    /**
     * Session pool property prefix
     */
	private final static String PROP_PREFIX = "EPP.SessionPool";


	
	/** Log4j category for logging */
	private static Logger log =
		Logger.getLogger(
				EPPSystemSessionPool.class.getName(),
				EPPCatFactory.getInstance().getFactory());
    
	/**
	 * SSL context information
	 */
	private EPPSSLContext sslContext = null;
	
	
	/**
	 * Real pool being used.
	 */
	private GenericObjectPool pool = null;
		
	/**
	 * Config used to configure the pool
	 */
	private GenericObjectPool.Config config = new GenericObjectPool.Config();
	
	/**
	 * The factory associated with the pool
	 */
	private EPPSessionPoolableFactory factory = null;
	
	/**
	 * Pre-initialize the pool to the <code>maxActive</code> setting?  This will cause 
	 *  <code>maxActive</code> sessions to be created and added back to the pool.  The 
	 *  default value is {@link DEFAULT_INIT_MAX_ACTIVE}.
	 */
	private boolean initMaxActive = DEFAULT_INIT_MAX_ACTIVE;
	
	/**
	 * Number of retries when attempting to get/create a session when calling 
	 * {@link #borrowObject()}.  {@link #borrowObject()} will retry <code>borrowRetries</code> 
	 * times for successfully borrowing a session after the first failure, so a 
	 * value of <code>0</code> will not implement any retries.      
	 */
	private int borrowRetries = DEFAULT_BORROW_RETRIES;
	
	
	/**
	 * The client identifier used to establish a session
	 */
	private String clientId;
	
	/**
	 * The password used to establish a session
	 */
	private String password;
	
	/**
	 * Idle timeout in milliseconds for session.  Session will be closed 
	 * if no transactions are sent with the session within <code>idleTimeout</code>
	 * milliseconds.
	 */
	private long idleTimeout;
	
	/**
	 * Absolute timeout in milliseconds of session.  Session will be closed
	 * if it has been active more than <code>absoluteTimeout</code> 
	 * milliseconds.
	 */
	private long absoluteTimeout;
	
	/**
	 * Name of system associated with this pool.
	 */
	private String system;
	
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
	 * Default constructor as part of the <i>Singleton Design Pattern</i>. 
	 */
	EPPSystemSessionPool(String aSystem) {
		this.system = aSystem;
	}
	
	
	/**
	 * Initialize the pool with a specific <code>EPPSessionPoolableFactory</code> 
	 * and <code>GenericObjectPool.Config</code> setting.
	 * 
	 * @param aFactory EPP session poolable object factory
	 * @param aConfig Configuration attributes for pool
	 */
	public void init(EPPSessionPoolableFactory aFactory, GenericObjectPool.Config aConfig) {
		this.pool = new GenericObjectPool(aFactory, aConfig);
		
	}	
	
	/**
	 * Initialize the pool using configuration values defined by 
	 * {@link com.verisign.epp.util.Environment} class.  The 
	 * {@link com.verisign.epp.util.Environment} class and logging must 
	 * be initialized before calling this method.  
	 * 
	 * @throws EPPSessionPoolException On error
	 */
	public void init() throws EPPSessionPoolException {		
		
		// Get configuration settings for pool
		try {
			String theValue;
			
			// clientId
			this.clientId = this.getProperty("clientId");
			if (this.clientId == null) {
				log.error("EPPSystemSessionPool.init(): clientId not defined");
				throw new EPPSessionPoolException("clientId not defined");
			}
			
			// password
			this.password = this.getProperty("password");
			if (this.password == null) {
				log.error("EPPSystemSessionPool.init(): password not defined");
				throw new EPPSessionPoolException("password not defined");
			}
			
			// absoluteTimeout
			theValue = this.getProperty("absoluteTimeout");
			if (theValue != null)
				this.absoluteTimeout = Long.parseLong(theValue);
			else
				this.absoluteTimeout = DEFAULT_ABSOLUTE_TIMEOUT;
			log.info("init(): absolute timeout = " + this.absoluteTimeout + " ms");
			
			// idleTimeout
			theValue = this.getProperty("idleTimeout");
			if (theValue != null)
				this.idleTimeout = Long.parseLong(theValue);
			else
				this.idleTimeout = DEFAULT_IDLE_TIMEOUT;
			log.info("init(): idle timeout = " + this.idleTimeout + " ms");

			// poolableClassName
			theValue = this.getProperty("poolableClassName");
			
			log.info("init(): poolable class name = " + theValue);
			
			try {
				this.factory = (EPPSessionPoolableFactory) Class.forName(theValue).newInstance();				
			}
			 catch (Exception ex) {
				log.error("EPPSystemSessionPool.init(): Exception creating instance of class " + theValue + 
						": " + ex);
				throw new EPPSessionPoolException("Exception creating instance of class " + theValue + 
						": " + ex);
			}			
			 
			// serverName
			this.serverName = this.getProperty("serverName");
			if (this.serverName == null) {
				log.error("EPPSystemSessionPool.init(): serverName not defined");
				throw new EPPSessionPoolException("serverName not defined");
			}
			log.info("init(): serverName = " + this.serverName);
			
			// serverPort
			theValue = this.getProperty("serverPort");
			if (theValue != null)
				this.serverPort = new Integer(theValue);
			log.info("init(): serverPort = " + this.serverPort);
	
			// clientHost
			this.clientHost = this.getProperty("clientHost");
			log.info("init(): clientHost = " + this.clientHost);
			
			// Ensure minEvictableIdleTimeMillis is disabled
			this.config.minEvictableIdleTimeMillis = 0;			
			
			// maxIdle
			theValue = this.getProperty("maxIdle");
			if (theValue != null)
				this.config.maxIdle = Integer.parseInt(theValue);
			else
				this.config.maxIdle = DEFAULT_MAX_IDLE;
			log.info("init(): max idle = " + this.config.maxIdle);
			
			// maxActive
			theValue = this.getProperty("maxActive");
			if (theValue != null)
				this.config.maxActive = Integer.parseInt(theValue);
			else
				this.config.maxActive = DEFAULT_MAX_ACTIVE;
			log.info("init(): max active = " + this.config.maxActive);
			
			// initMaxActive
			theValue = this.getProperty("initMaxActive");
			if (theValue != null)
				this.initMaxActive = (Boolean.valueOf(theValue)).booleanValue();
			else
				this.initMaxActive = DEFAULT_INIT_MAX_ACTIVE;
			log.info("init(): init max active = " + this.initMaxActive);
			
	
			// borrowRetries
			theValue = this.getProperty("borrowRetries");
			if (theValue != null)
				this.borrowRetries = Integer.parseInt(theValue);
			else
				this.borrowRetries = DEFAULT_BORROW_RETRIES;
			log.info("init(): borrow retries = " + this.borrowRetries);
			
			
			// maxWait
			theValue = this.getProperty("maxWait");
			if (theValue != null)
				this.config.maxWait = Integer.parseInt(theValue);
			else
				this.config.maxWait = DEFAULT_MAX_WAIT;
			log.info("init(): max wait = " + this.config.maxWait);

			// minIdle
			theValue = this.getProperty("minIdle");
			if (theValue != null)
				this.config.minIdle = Integer.parseInt(theValue);
			else
				this.config.minIdle = DEFAULT_MIN_IDLE;
			log.info("init(): min idle = " + this.config.minIdle);

			// numTestsPerEvictionRun
			this.config.numTestsPerEvictionRun=-1;  // This will cause all session to be tested
			
			// testOnBorrow
			this.config.testOnBorrow = false;
			
			// testOnReturn
			this.config.testOnReturn = false;
			
			// testWhileIdle
			this.config.testWhileIdle = true; 

			// timeBetweenEvictionRunsMillis
			theValue = this.getProperty("timeBetweenEvictionRunsMillis");
			if (theValue != null)
				this.config.timeBetweenEvictionRunsMillis = Long.parseLong(theValue);
			else
				this.config.timeBetweenEvictionRunsMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
			log.info("init(): time between eviction runs = " + this.config.timeBetweenEvictionRunsMillis + " ms");
			
			// whenExhaustedAction
			this.config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		}
		catch (Exception ex) {
			log.error("EPPSystemSessionPool.init(): Exception referencing Environment property: " + ex);
			throw new EPPSessionPoolException("Exception referencing Environment property: " + ex);			
		}
		 
		// Set factory required attributes
		this.factory.setAbsoluteTimeout(this.absoluteTimeout);
		this.factory.setIdleTimeout(this.idleTimeout);
		this.factory.setClientId(this.clientId);
		this.factory.setPassword(this.password);
		this.factory.setServerName(this.serverName);
		this.factory.setServerPort(this.serverPort);
		this.factory.setClientHost(this.clientHost);
		
		

		// Does pool have its own SSL configuration?
		if (this.getProperty("SSLProtocol") != null) {

			// Create EPPSSLConfig with required properties
			EPPSSLConfig theConfig = new EPPSSLConfig(
					this.getProperty("SSLProtocol"), 
					this.getProperty("SSLKeyStore"), 
					this.getProperty("SSLKeyFileName"), 
					this.getProperty("SSLPassPhrase"));

			// Set optional EPPSSLConfig properties
			theConfig.setIdentityKeyPassPhrase(this
					.getProperty("SSLKeyPassPhrase"));
			theConfig.setSslDebug(this.getProperty("SSLDebug"));

			theConfig.setTrustStore(this.getProperty("SSLTrustStore"), 
					this.getProperty("SSLTrustStoreFileName"), 
					this.getProperty("SSLTrustStorePassPhrase"));

			theConfig.setSSLEnabledProtocols(
					this.getProperty("SSLEnabledProtocols"));
			theConfig.setSSLEnabledCipherSuites(
					this.getProperty("SSLEnabledCipherSuites"));

			try {
				this.sslContext = EPPSSLImpl.initialize(theConfig);
			}
			catch (EPPConException ex) {
				log.error("EPPSystemSessionPool.init(): Exception initializing EPPSSLContext: " + ex);
				throw new EPPSessionPoolException("EPPSystemSessionPool.init(): Exception initializing EPPSSLContext: " + ex);
			}
			
			this.factory.setSSLContext(this.sslContext);
		}
			
		
		this.init(this.factory, this.config);
		
		// Pre-initialize maxActive sessions in pool?
		if (this.initMaxActive && this.config.maxActive > 0) {
			log.info("init(): Pre-initialize maxActive ("
					+ this.config.maxActive + ") sessions");

			EPPSession theSessions[] = new EPPSession[this.config.maxActive];
			
			// Borrow maxActive sessions from pool
			for (int i = 0; i < this.config.maxActive; i++) {

				try {
					theSessions[i] = this.borrowObject();
					log.info("init(): Pre-initialized session #"
							+ (i + 1));
				}
				catch (EPPSessionPoolException ex) {
					log.error("init(): Failure to pre-initialize session #"
									+ (i + 1) + ": " + ex);
				}
			}
			
			// Return maxActive sessions back to pool
			for (int i = 0; i < this.config.maxActive; i++) {
				if (theSessions[i] != null) {
					this.returnObject(theSessions[i]);					
					theSessions[i] = null;
				}
			}
			
		}
		
	}

	
	/**
	 * Closes the session pool contained in <code>EPPSystemSessionPool</code>
	 * cleanly. Cleanly closing the pool means clearing the
	 * pool that will execute an EPP logout for each of the idle sessions and
	 * close the pool.
	 */
	public void close() {
		log.info("close(): closing pool");
		
		// The default pool exists?
		if (this.pool != null) {
			// Clear and close the current pool
			this.pool.clear();
			try {
				this.pool.close();
			}
			catch (Exception ex) {
				log
						.error("EPPSystemSessionPool.close(): Exception closing pool <"
								+ this.pool + ">: " + ex);
			}
		}
		log.info("close(): pool closed");
	} 
	
	
	/**
	 * Borrows a session from the pool.  The session must be returned 
	 * by either calling {@link #invalidateObject(com.verisign.epp.interfaces.EPPSession)} 
	 * or {@link #returnObject(com.verisign.epp.interfaces.EPPSession)}.  This method
	 * will block if there are no idle sessions in the pool for <code>maxWait</code> time.
	 * 
	 * @return Borrowed <code>EPPSession</code> instance.
	 * 
	 * @throws EPPSessionPoolException On error
	 */
	public EPPSession borrowObject() throws EPPSessionPoolException {
		if (this.pool == null) {
			log.error("borrowObject(): pool is null");
			throw new EPPSessionPoolException("EPPSystemSessionPool: pool is null");
		}
		
		EPPSession theSession = null;
		
		// Attempt to borrow session until successful or retries have exceeded.  
		for (int retries = 0; theSession == null
				&& retries <= this.borrowRetries; retries++) {
			try {
				theSession = (EPPSession) pool.borrowObject();
				
				log.debug("borrowObject(): Session = " + theSession
						+ ", Active = " + pool.getNumActive() + ", Idle = "
						+ pool.getNumIdle());
			}
			catch (Exception ex) {
				
				// Number of retries exceeded?
				if (retries >= this.borrowRetries) {
					
					// Throw exception to indicate borrow failure
					log
							.error("borrowObject(): Final exception on borrow session after "
									+ retries + " retries: " + ex);
					throw new EPPSessionPoolException(
							"EPPSystemSessionPool: Exception " + ex);
				}
				else {
					// Continue retrying
					log
							.debug("borrowObject(): Exception on borrow session after "
									+ retries + " retries: " + ex);
				}

			}
			
		}
		
		return theSession;
	}
	
	/**
	 * Remove a borrowed session from the pool based on a known issue with it.  
	 * The should be done if an unexpected exception occurs with the session which might
	 * be due to the server being down or the session being expired.
	 * ;
	 * @param aSession Session that is invalid
	 * 
	 * @throws EPPSessionPoolException On error
	 */
	public void invalidateObject(EPPSession aSession) throws EPPSessionPoolException {
		try {
			pool.invalidateObject(aSession);
			log.debug("invalidateObject(" + aSession + "): Active = " + pool.getNumActive() + 
					", Idle = " + pool.getNumIdle());
		}
		catch (Exception ex) {
			log.error("invalidateObject(" + aSession + "): Caught Exception: " + ex);
			throw new EPPSessionPoolException("EPPSessionPool: Exception " + ex);
		}
	}
	/**
	 * Returned a borrowed session to the pool.  This session must have been 
	 * returned from a call to {@link #borrowObject()}.  
	 * 
	 * @param aSession Session to return
	 * 
	 * @throws EPPSessionPoolException On error
	 */
	public void returnObject(EPPSession aSession) throws EPPSessionPoolException {
		try {
			pool.returnObject(aSession);
			log.debug("returnObject(" + aSession + "): Active = " + pool.getNumActive() + 
					", Idle = " + pool.getNumIdle());
		}
		catch (Exception ex) {
			log.error("returnObject(" + aSession + "): Caught Exception: " + ex);
			throw new EPPSessionPoolException("EPPSessionPool: Exception " + ex);
		}
		
	}
	
	/**
	 * Gets the contained <code>GenericObjectPool</code>.
	 * 
	 * @return Contained <code>GenericObjectPool</code>
	 */
	public GenericObjectPool getGenericObjectPool() {
		return this.pool;
	}
	
	/**
	 * Gets the session absolute timeout.
	 * 
	 * @return Returns the absolute timeout in milliseconds.
	 */
	public long getAbsoluteTimeout() {
		return absoluteTimeout;
	}
	
	/**
	 * Gets the client identifier used to authenticate.
	 * @return Returns the client identifier.
	 */
	public String getClientId() {
		return clientId;
	}
	
	/**
	 * Gets the configuration for the <code>GenericObjectPool</code>.
	 * 
	 * @return Returns the config.
	 */
	public GenericObjectPool.Config getConfig() {
		return config;
	}
	
	/**
	 * Gets the factory associated with the pool.
	 * 
	 * @return Returns the factory.
	 */
	public EPPSessionPoolableFactory getFactory() {
		return factory;
	}
	
	/**
	 * Gets the session idle timeout.
	 * 
	 * @return Returns the idle timeout in milliseconds.
	 */
	public long getIdleTimeout() {
		return idleTimeout;
	}
	
	/**
	 * Gets the password used for authentication.
	 * 
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Gets the system name associated with this pool.
	 * 
	 * @return Pool system name
	 */
	public String getSystem() {
		return this.system;
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
	 * Gets an environment property associated with the system.
	 * 
	 * @param aProperty The property name without the EPP.SessionPool.&lt;session&gt;. prefix.
	 * 
	 * @return Property value if defined; <code>null</code> otherwise.
	 */
	private String getProperty(String aProperty) {
		return Environment.getProperty(PROP_PREFIX + "." + this.system + "." + aProperty);
	}
	
	
}