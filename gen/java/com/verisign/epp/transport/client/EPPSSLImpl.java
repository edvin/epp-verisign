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
package com.verisign.epp.transport.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

import com.verisign.epp.exception.EPPException;
import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;


/**
 * EPP SSL Implementation utility class that is used to initialize the 
 * SSL configuration by using configuration properties including:<br>
 * <br><ul>
	 * <li><code>EPP.SSLProtocol</code> - Required property that can be either 
	 * SSL, SSLv2, SSLv3, TLS, or TLSv1
	 * <li><code>EPP.SSLKeyManager</code> - Required SSL key manager property, 
	 * for example, SunX509
	 * <li><code>EPP.SSLKeyStore</code> - Required SSL Keystore format property, 
	 * for example, JKS
	 * <li><code>EPP.SSLKeyFileName</code> - Required Identity SSL Keystore 
	 * file name.  
	 * <li><code>EPP.SSLPassPhrase</code> - Required Identity SSL Keystore 
	 * passphrase.  
	 * <li><code>EPP.SSLKeyPassPhrase</code> - Optional Identity SSL private 
	 * key passphrase.  If not defined, <code>EPP.SSLPassPhrase</code>
	 * is used.
	 * <li><code>EPP.SSLTrustStoreFileName</code> - Optional Trust 
	 * SSK Keystore file name.  If not defined, the trust Keystore of 
	 * the JRE is used.
	 * <li>EPP.SSLTrustStorePassPhrase - Optional Trust SSL Keystore 
	 * passphrase.  This is required if <code>EPP.SSLTrustStoreFileName</code>
	 * is specified.  
 * </ul><br>
 * This class includes a set of static properties and methods that can 
 * be used to initialize SSL and to get the resulting initialized 
 * <code>SSLContext</code> and <code>SSLSocketFactory</code>.  The 
 * <code>initialize</code> method is sychronized and will immediately 
 * return if <code>EPPSSLImpl</code> has already been successfully 
 * initialized.  The method <code>isInitialized</code> can be used to 
 * check if <code>EPPSSLImpl</code> has already been initialized.
 */
public class EPPSSLImpl {

	/** Used for logging */
	private static Logger logger = Logger.getLogger(EPPSSLImpl.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * SSL context information
	 */
	private static EPPSSLContext context = null;
	
	
	/**
	 * Has EPPSSLImpl been successfully initialized?
	 */
	private static boolean initialized = false;
	
	
	/**
	 * Gets the SSLContext that has been initialized if 
	 * <code>isInitialized</code> returns <code>true</code>, otherwise
	 * it will be <code>null</code>.  The following configuration properties
	 * are used to initialize SSL:<br>
	 * <br><ul>
	 * </ul>
	 * 
	 * @return Initialized <code>SSLContext</code> if successfully initialized; 
	 * <code>null</code> otherwise.
	 */
	public static SSLContext getSSLContext() {
		if (initialized) {
			return context.getSSLContext();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Is the SSL enabled protocols specified?
	 * 
	 * @return <code>true</code> if specified;<code>false</code> otherwise.
	 */
	public static boolean hasSSLEnabledProtocols() {
		return (context != null ? context.hasSSLEnabledProtocols() : false); 
	}
	
	
	/**
	 * Gets the SSL enabled protocols.
	 * 
	 * @return <code>String</code> array of SSL enabled protocols if defined;<code>null</code> otherwise.
	 */
	public static String[] getSSLEnabledProtocols() {
		return context.getSSLEnabledProtocols();
	}
	
	/**
	 * Is the SSL enabled cipher suites specified?
	 * 
	 * @return <code>true</code> if specified;<code>false</code> otherwise.
	 */
	public static boolean hasSSLEnabledCipherSuites() {
		return (context != null ? context.hasSSLEnabledCipherSuites() : false); 
	}
	
	/**
	 * Gets the SSL enabled cipher suites.
	 * 
	 * @return <code>String</code> array of SSL enabled cipher suites if defined;<code>null</code> otherwise.
	 */
	public static String[] getSSLEnabledCipherSuites() {
		return context.getSSLEnabledCipherSuites();
	}
	
	
	/**
	 * Gets the single <code>EPPSSLContext</code> initialized 
	 * by the <code>EPPSSLImpl</code>.  
	 * 
	 * @return Single <code>EPPSSLContext</code> instance if initialized; <code>null</code> otherwise.
	 */
	public static EPPSSLContext getEPPSSLContext() {
		return context;
	}

	/**
	 * Has EPPSSLImpl been successfully initialized?
	 * @return <code>true</code> if initialized; <code>false</code> otherwise
	 */
	public static boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * Gets the initialize <code>SSLSocketFactory</code>.
	 * 
	 * @return Initialized <code>SSLSocketFactory</code> if successfully initialized; 
	 * <code>null</code> otherwise.
	 */
	public static SSLSocketFactory getSSLSocketFactory() {
		if (initialized) {
			return context.getSSLSocketFactory();
		}
		else {
			return null;
		}
	}
	
	public static EPPSSLContext initialize(EPPSSLConfig aConfig) throws EPPConException {
		logger.debug("initialize(EPPSSLConfig): enter");
		
		// Validate the aConfig parameter
		if (aConfig != null) {
			try {
				aConfig.validate();
			}
			catch (EPPException e) {
				logger.error("initialize(EPPSSLConfig): Config is invalid: " + e.getMessage());
				throw new EPPConException(e.getMessage());
			}
		}
		else {
			logger.error("initialize(EPPSSLConfig): Config is null");
			throw new EPPConException("EPPSSLImpl.initialize(EPPSSLConfig): Config is null");
		}
		
		logger.info("initialize(EPPSSLConfig): Config = " + aConfig);
		
		
		EPPSSLContext theContext = new EPPSSLContext();
		
		KeyManagerFactory theKeyStoreFactory = null;
		KeyStore theKeyStore = null;

		String theProtocol = aConfig.getSslProtocol();
		String theKeyStoreType = aConfig.getIdentityStoreType();
		String theKeyStoreFileName = aConfig.getIdentityFileName();
		char[] thePassphrase = aConfig.getIdentityPassPhraseCharArray();
		char[] theKeyPassphrase = aConfig.getIdentityKeyPassPhraseCharArray();

		if (aConfig.getSslDebug() != null) {
			System.setProperty("javax.net.debug", aConfig.getSslDebug());
		}
				
		/**
		 * Set the protocol. Catch the Exception (s).
		 */
		try {
			/**
			 * Protocol Supported with this implementation SSL Supports some
			 * version of SSL; may support other versions SSLv2 Supports SSL
			 * version 2 or higher SSLv3 Supports SSL version 3; may support
			 * other versions TLS Supports some version of TLS; may support
			 * other versions TLSv1 Supports TLS version 1; may support other
			 * versions
			 */
			theContext.setSSLContext(SSLContext.getInstance(theProtocol));
		}
		catch (NoSuchAlgorithmException ex) {
			logger.error("initialize(EPPSSLConfig): Given Protocol is not Available : " + "(" + theProtocol
					+ ") " + ex.getMessage(), ex);
			throw new EPPConException("initialize(EPPSSLConfig): Given Protocol is not Available : "
					+ "(" + theProtocol + ") " + ex.getMessage());
		}

		// Get the keystore factory given a keystore algorithm
		try {
			theKeyStoreFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		}
		catch (NoSuchAlgorithmException ex) {
			logger.error("initialize(EPPSSLConfig): The default algorithm is not available in this package : "
					+ "(" + KeyManagerFactory.getDefaultAlgorithm() + ") " + ex.getMessage(),
					ex);
			throw new EPPConException(
					"initialize(EPPSSLConfig): The Given algorithm is not available in this package : "
							+ "(" + KeyManagerFactory.getDefaultAlgorithm() + ") "
							+ ex.getMessage());
		}

		// Create the keystore with the specific algorithm
		try {
			theKeyStore = KeyStore.getInstance(theKeyStoreType);
		}
		catch (KeyStoreException ex) {
			logger.error("initialize(EPPSSLConfig): The Given keystore type is not Available" + "("
					+ theKeyStoreType + ") " + ex.getMessage(),
					ex);
			throw new EPPConException(
					"initialize(EPPSSLConfig): The Given keystore type is not Available" + "("
							+ theKeyStoreType + ") "
							+ ex.getMessage());
		}

		// Load the identity keystore from the file system.
		try {
			theKeyStore.load(new FileInputStream(theKeyStoreFileName),
					thePassphrase);
		}
		catch (CertificateException ex) {
			logger.error(
					"initialize(EPPSSLConfig): The given certificates in the keystore could not be loaded "
							+ "(" + theKeyStoreFileName + ") "
							+ ex.getMessage(), ex);
			throw new EPPConException(
					"initialize(EPPSSLConfig): The given certificates in the keystore could not be loaded "
							+ "(" + theKeyStoreFileName + ") "
							+ ex.getMessage());
		}
		catch (NoSuchAlgorithmException ex) {
			logger.error(
					"initialize(EPPSSLConfig): The given algorithm used to check the integrity is not Available "
							+ "(" + theKeyStoreFileName + ") "
							+ ex.getMessage(), ex);
			throw new EPPConException(
					"initialize(EPPSSLConfig): The given algorithm used to check the integrity is not Available "
							+ "(" + theKeyStoreFileName + ") "
							+ ex.getMessage());
		}
		catch (FileNotFoundException ex) {
			logger.error("initialize(EPPSSLConfig): The given keystore file is not found " + "("
					+ theKeyStoreFileName + ") " + ex.getMessage(),
					ex);
			throw new EPPConException("initialize(EPPSSLConfig): The given keystore file is not found "
					+ "(" + theKeyStoreFileName + ") "
					+ ex.getMessage());
		}
		catch (IOException ex) {
			logger.error("initialize(EPPSSLConfig): I/O or format probkem with keystore date " + "("
					+ theKeyStoreFileName + ") " + ex.getMessage(),
					ex);
			throw new EPPConException(
					"initialize(EPPSSLConfig): I/O or format probkem with keystore date " + "("
							+ theKeyStoreFileName + ") "
							+ ex.getMessage());
		}

		// Initialize the keystore factory with the keystore and the 
		// keystore passphrase.
		try {
			theKeyStoreFactory.init(theKeyStore, theKeyPassphrase);
		}
		catch (NoSuchAlgorithmException ex) {
			logger.error("initialize(EPPSSLConfig): The Given algorithm is not valid, "
					+ ex.getMessage(), ex);
			throw new EPPConException("initialize(EPPSSLConfig): The Given algorithm is not valid, "
					+ ex.getMessage());
		}
		catch (UnrecoverableKeyException ex) {
			logger.error("initialize(EPPSSLConfig): The Given key can not be recovered, "
					+ ex.getMessage(), ex);
			throw new EPPConException("initialize(EPPSSLConfig): The Given key can not be recovered, "
					+ ex.getMessage());
		}
		catch (KeyStoreException ex) {
			logger.error("initialize(EPPSSLConfig): The Given keystore is not valid, "
					+ ex.getMessage(), ex);
			throw new EPPConException("initialize(EPPSSLConfig): The Given keystore is not valid, "
					+ ex.getMessage());
		}
		
		// Load trust store 
		TrustManager theTrustManagers[] = null;
		
		String theTrustStoreFileName = aConfig.getTrustStoreFileName();
		char[] theTrustStorePassphrase = aConfig.getTrustStorePassPhraseCharArray();
		String theTrustStoreType = aConfig.getTrustStoreType();
		if (theTrustStoreType == null) {
			theTrustStoreType = theKeyStoreType;
		}
				
		if (theTrustStorePassphrase == null) {
			logger.info("initialize(EPPSSLConfig): Trust Store Pass Phrase property not defined, will use Identity Pass Phrase");					
			theTrustStorePassphrase = thePassphrase;
		}
		
		if (theTrustStoreFileName != null) {
			try {
				TrustManagerFactory theTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				KeyStore theTrustStore = KeyStore.getInstance(theTrustStoreType);
				theTrustStore.load(new FileInputStream(theTrustStoreFileName),
						theTrustStorePassphrase);
				theTrustManagerFactory.init(theTrustStore);
				theTrustManagers = theTrustManagerFactory.getTrustManagers();
			}
			catch (Exception ex) {
				logger.error("Error initializing trust manager: " + ex);
				throw new EPPConException("Error initializing trust manager: " + ex);
			}
			
		}
		else {
			logger.info("initialize(EPPSSLConfig): Trust Store File Name property not defined, will use default trust store");					
		}
 		
		

		// Initialize the SSL context given the identity and trust information.
		try {
			theContext.getSSLContext().init(theKeyStoreFactory.getKeyManagers(), theTrustManagers, null);
		}
		catch (KeyManagementException ex) {
			logger.error("initialize(EPPSSLConfig): The Given key Manager is not valid, "
					+ ex.getMessage(), ex);
			throw new EPPConException("initialize(EPPSSLConfig): The Given key Manager is not valid, "
					+ ex.getMessage());
		}
		
		theContext.setSSLSocketFactory(theContext.getSSLContext().getSocketFactory());
		theContext.setSSLEnabledProtocols(aConfig.getSSLEnabledProtocols());
		theContext.setSSLEnabledCipherSuites(aConfig.getSSLEnabledCipherSuites());
		
	
		logger.debug("initialize(EPPSSLConfig): exit");
		return theContext;
	}
	
	/**
	 * Initialize the <code>EPPSSLImpl</code>, which will create 
	 * an initialize the <code>SSLContext</code>.  If 
	 * <code>EPPSSLImpl</code> has already been successfully initialized, 
	 * this method will simply return.
	 * 
	 * @throws EPPConException Error initializing <code>EPPSSLImpl</code>
	 */
	public static synchronized void initialize() throws EPPConException {
		logger.debug("initialize(): enter");
		
		// Already initialized?
		if (initialized) {
			logger.debug("EPPSSLImpl has already been initialized");
			return;
		}
		
		logger.info("EPPSSLImpl starting initialization");
		
		EPPSSLConfig theConfig = new EPPSSLConfig(EPPEnv.getSSLProtocol(),
				EPPEnv.getKeyStore(),
				EPPEnv.getSSLKeyFileName(),
				EPPEnv.getSSLPassPhrase()
				);
		
		theConfig.setIdentityKeyPassPhrase(EPPEnv.getSSLKeyPassPhrase());
		theConfig.setSslDebug(EPPEnv.getSSLDebug());
		theConfig.setTrustStore(EPPEnv.getKeyStore(), 
				EPPEnv.getSSLTrustStoreFileName(), 
				EPPEnv.getSSLTrustStorePassPhrase());
		
		theConfig.setSSLEnabledProtocols(EPPEnv.getSSLEnabledProtocols());
		theConfig.setSSLEnabledCipherSuites(EPPEnv.getSSLEnabledCipherSuites());
				
		context = initialize(theConfig);
		
		logger.info("EPPSSLImpl successfully initialized");
		initialized = true;
		
		logger.debug("initialize(): exit");
	}
	
}