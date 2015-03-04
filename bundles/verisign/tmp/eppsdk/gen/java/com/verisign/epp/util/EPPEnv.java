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
package com.verisign.epp.util;


// Log4J Imports
import org.apache.log4j.*;

// JDK Imports
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Utility class that contains all of the SDK environment 
 * property values.
 */
public abstract class EPPEnv extends Environment {
	/** Basic log mode.  Uses EPP.LogLevel and EPP.LogFile parameters. */
	public static final int LOG_BASIC = 0;

	/**
	 * Log4J configuration file mode.  Uses EPP.LogCfgFile and
	 * EPP.LogCfgFileWatch parameters.
	 */
	public static final int LOG_CFG_FILE = 1;

	/**
	 * Custom logging mode.  The SDK does not initialize the logging facility.
	 */
	public static final int LOG_CUSTOM = 2;

	/** Prefix for PoolMan Server */
	public static final String POOLMAN_SERVER_PREFIX = "PoolMan.Server.";

	/** Prefix for PoolMan Client */
	public static final String POOLMAN_CLIENT_PREFIX = "PoolMan.Client.";

	/**
	 * Initialize the environment
	 *
	 * @param newConfigFileName The config file read value pairs from
	 * @param aClassLoader DOCUMENT ME!
	 *
	 * @exception EPPEnvException
	 */
	public void initialize(String newConfigFileName, ClassLoader aClassLoader)
					throws EPPEnvException {
		String st = null;

		/** list of required properties. */
		String[] listArr =
		{
			"EPP.ServerName",

		"EPP.ServerPort",

		"EPP.ConTimeOut",

		"EPP.MapFactories",

		"EPP.ProtocolExtensions",

		"EPP.CmdRspExtensions",

		"EPP.ServerEventHandlers",

		"EPP.LogMode"
		};

		/*
		 * Get the properties
		 */
		try {
			envInitialize(newConfigFileName, aClassLoader);
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("Environment Exception : ("
									  + newConfigFileName + ") "
									  + myException.getMessage());
		}

		/*
		 * Validate the required properties
		 */
		try {
			for (int i = 0; i < listArr.length; i++) {
				if (getEnv(listArr[i]) == null) {
					throw new EPPEnvException("EPPEnv : " + listArr[i]
											  + " is Missing from Configuration file");
				}
			}
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("Environment Exception : "
									  + myException.getMessage());
		}
	}

	/**
	 * Initialize the environment
	 *
	 * @param newConfigFileName The config file read value pairs from
	 *
	 * @exception EPPEnvException
	 */
	public void initialize(String newConfigFileName) throws EPPEnvException {
		String st = null;

		/** list of required properties. */
		String[] listArr =
		{
			"EPP.ServerName",

		"EPP.ServerPort",

		"EPP.ConTimeOut",

		"EPP.MapFactories",

		"EPP.ProtocolExtensions",

		"EPP.CmdRspExtensions",

		"EPP.LogMode"
		};

		/*
		 * Get the properties
		 */
		try {
			envInitialize(newConfigFileName);
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("Environment Exception : ("
									  + newConfigFileName + ") "
									  + myException.getMessage());
		}

		/*
		 * Validate the required properties
		 */
		try {
			for (int i = 0; i < listArr.length; i++) {
				if (getEnv(listArr[i]) == null) {
					throw new EPPEnvException("EPPEnv : " + listArr[i]
											  + " is Missing from Configuration file");
				}
			}
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("Environment Exception : "
									  + myException.getMessage());
		}
	}

	/**
	 * Gets the client host name / IP address to connect from.
	 *
	 * @return Client host name / IP address if defined;<code>null</code> otherwise
	 */
	public static String getClientHost() {
		return Environment.getOption("EPP.ClientHost");
	}


	
	/**
	 * Returns the value of the EPP.ServerName property specified in the config
	 * file.
	 *
	 * @return String value for the property
	 *
	 * @exception EPPEnvException
	 */
	public static String getServerName() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ServerName");
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return st;
	}

	/**
	 * Gets the server name to use in the EPP greeting.  If not set in the
	 * configuration file, the default value of "EPP Server Stub" is returned;
	 *
	 * @return Greeting Server Name if defined; <code>"EPP Server Stub"</code>
	 * 		   otherwise.
	 */
	public static String getGreetingServerName() {
		String st = "EPP Server Stub";

		try {
			st = Environment.getEnv("EPP.GreetingServerName");
		}
		 catch (EnvException myException) {
			// Do nothing, since this is not a required setting.
		}

		return st;
	}

	/**
	 * Returns the value of the EPP.ServerPort property specified in the config
	 * file.
	 *
	 * @return int value for the property
	 *
	 * @exception EPPEnvException
	 */
	public static int getServerPort() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ServerPort");
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return new Integer(st).intValue();
	}

	/**
	 * Returns the connection timeout in number of seconds specified by the
	 * EPP.ConTimeOut property in the config file.
	 *
	 * @return int value for the property
	 *
	 * @exception EPPEnvException
	 */
	public static int getConTimeOut() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ConTimeOut");
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return new Integer(st).intValue();
	}

	/**
	 * Gets the proxy server locator <code>Class</code> as a 
	 * <code>String</code> as defined by the <code>EPP.ProxyServersLcoator</code>
	 * property.
	 * 
	 * @return Proxy server locator <code>Class</code>  as a <code>String</code>
	 * @throws EPPEnvException Property is not defined
	 */
	public static String getProxyServerLocator() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ProxyServersLocator");
		}
		 catch (EnvException ex) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + ex.getMessage());
		}

		return st;
	}
	
	/**
	 * Returns the list of proxy servers defined by the 
	 * EPP.ProxyServers property.  The format of the EPP.ProxyServers 
	 * property should be:<br>
	 * <br>
	 * <server name>:<port>[,<server name>:<port>]*<br>
	 * <br>
	 * <server name> ::= <ip> | logical server name<br>
	 * <ip>          ::= IPv4 address | [<IPv6 address>]<br>
	 * <br>
	 * An example of a EPP.ProxyServers value is:<br>
	 * <br>
	 * samplehost:80,[2620:74:13:3000::80]:80,192.168.10.10:88
	 *
	 * @return String value for the property
	 *
	 * @exception EPPEnvException Property is not defined
	 */
	public static String getProxyServers() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ProxyServers");
		}
		 catch (EnvException ex) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + ex.getMessage());
		}

		return st;
	}
	
	/**
	 * Returns whether not to randomize the proxy servers 
	 * connected through.  The <code>EPP.ProxyServersRandomize</code> 
	 * configuration property is used and if it is not set the default 
	 * value of <code>true</code> is returned.  
	 * 
	 * @return <code>true</code> to randomize;<code>false</code> otherwise.
	 * 
	 * @throws EPPEnvException Property is not defined
	 */
	public static boolean getProxyServersRandomize() throws EPPEnvException {
		String st = null;

		st = Environment.getOption("EPP.ProxyServersRandomize");
		
		if (st != null) {
			return new Boolean(st).booleanValue();			
		}
		else {
			return true;
		}
	}
		
	
	/**
	 * Returns the value of the EPP.ClientSocketName property specified in the
	 * config file.
	 *
	 * @return String value for the property
	 *
	 * @exception EPPEnvException
	 */
	public static String getClientSocketName() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ClientSocketName");
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return st;
	}

	/**
	 * Returns the log facility mode defined by the EPP.LogMode configuration
	 * parameter.
	 *
	 * @return <code>LOG_</code> constants if valid EPP.LogMode setting; -1
	 * 		   otherwise.
	 */
	public static int getLogMode() {
		String cfgMode = Environment.getProperty("EPP.LogMode", "BASIC");

		if (cfgMode.equalsIgnoreCase("BASIC")) {
			return LOG_BASIC;
		}

		else if (cfgMode.equalsIgnoreCase("CFGFILE")) {
			return LOG_CFG_FILE;
		}

		else if (cfgMode.equalsIgnoreCase("CUSTOM")) {
			return LOG_CUSTOM;
		}

		else {
			return -1;
		}
	}

	/**
	 * Returns the value of the EPP.LogLevel property specified in the config
	 * file.  Either both EPP.LogLevel and EPP.LogFile need to be defined or
	 * EPP.LogCfgFile.  <code>initialize</code> will validate this condition.
	 *
	 * @return Log4J Level based on EPP.LogLevel setting if defined;
	 * 		   <code>null</code> otherwise
	 *
	 * @exception EPPEnvException Invalid configuration parameter value
	 */
	public static Level getLogLevel() throws EPPEnvException {
		String st = Environment.getOption("EPP.LogLevel");

		Level  ret = null;

		// Undefined?
		if (st == null) {
			return null;
		}

		// Convert level string to Log4J Property level
		st = st.toUpperCase();

		if (st.equals("DEBUG")) {
			ret = Level.DEBUG;
		}

		else if (st.equals("INFO")) {
			ret = Level.INFO;
		}

		else if (st.equals("WARN")) {
			ret = Level.WARN;
		}

		else if (st.equals("ERROR")) {
			ret = Level.ERROR;
		}

		else if (st.equals("FATAL")) {
			ret = Level.FATAL;
		}

		else {
			throw new EPPEnvException("Log level " + st + " is invalid");
		}

		return ret;
	}

	/**
	 * Returns the value of the EPP.LogFile property specified in the config
	 * file.  Either both EPP.LogLevel and EPP.LogFile need to be defined or
	 * EPP.LogCfgFile.  <code>initialize</code> will validate this condition.
	 *
	 * @return log file if defined; <code>null</code> otherwise
	 *
	 * @throws EPPEnvException DOCUMENT ME!
	 */
	public static String getLogFile() throws EPPEnvException {
		return Environment.getProperty("EPP.LogFile");
	}

	/**
	 * Returns the value of the EPP.LogCfgFile property specified in the config
	 * file.  Either both EPP.LogLevel and EPP.LogFile need to be defined or
	 * EPP.LogCfgFile.  <code>initialize</code> will validate this condition.
	 *
	 * @return log configuration file if defined; <code>null</code> otherwise
	 *
	 * @exception EPPEnvException Log configuration file does not exist
	 */
	public static String getLogCfgFile() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.LogCfgFile");
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return st;
	}

	/**
	 * Returns the value of the EPP.LogCfgFileWatch property specified in the
	 * config file.  This setting can only be made if EPP.LogCfgFile is
	 * defined, and indicates the number of milliseconds to look for changes
	 * to the logging configuration file.
	 *
	 * @return log configuration file watch if defined; <code>null</code>
	 * 		   otherwise
	 *
	 * @exception EPPEnvException Invalid configuration parameter value
	 */
	public static Long getLogCfgFileWatch() throws EPPEnvException {
		Long ret = null;

		try {
			String configAndWatch = Environment.getProperty("EPP.LogCfgFileWatch");

			if (configAndWatch != null) {
				ret = new Long(Environment.getProperty("EPP.LogCfgFileWatch"));
			}
		}

		 catch (NumberFormatException e) {
			throw new EPPEnvException("NumberFormatException"
									  + " converting EPP.LogCfgFileWatch to Long: "
									  + e);
		}

		return ret;
	}

	/**
	 * Returns the value of the EPP.ServerSocketName property specified in the
	 * config file.  This is the class that is instantiated that listens for
	 * connections.
	 *
	 * @return int value for the property
	 *
	 * @exception EPPEnvException
	 */
	public static String getServerSocketName() throws EPPEnvException {
		String st = null;

		try {
			st = Environment.getEnv("EPP.ServerSocketName");
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return st;
	}

	/**
	 * Returns a vector of class names that are factories for each EPP Mapping.
	 * These are the values specified by the EPP.MapFactories property in the
	 * config file.
	 *
	 * @return <code>Vector</code> of fully qualified
	 * 		   <code>EPPMapFactory</code> class <code>Strings</code>.
	 *
	 * @exception EPPEnvException
	 */
	public static Vector getMapFactories() throws EPPEnvException {
		Vector ret = new Vector();

		try {
			String		    setting = Environment.getEnv("EPP.MapFactories");

			StringTokenizer tokenizer = new StringTokenizer(setting);

			while (tokenizer.hasMoreElements()) {
				ret.addElement(tokenizer.nextElement());
			}
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return ret;
	}

	/**
	 * Returns a vector of class names that are ProtocolExtensions.  These are
	 * the values specified by the EPP.ProtocolExtensions property in the
	 * config file.
	 *
	 * @return <code>Vector</code> of fully qualified
	 * 		   <code>EPPProtocolExtension</code> class <code>Strings</code>.
	 *
	 * @exception EPPEnvException
	 */
	public static Vector getProtocolExtensions() throws EPPEnvException {
		Vector ret = new Vector();

		try {
			String setting = Environment.getEnv("EPP.ProtocolExtensions");

			StringTokenizer tokenizer = new StringTokenizer(setting);

			while (tokenizer.hasMoreElements()) {
				ret.addElement(tokenizer.nextElement());
			}
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return ret;
	}

	/**
	 * Returns a vector of class names that are CommandResponseExtensions.
	 * These are the values specified by the EPP.CmdRspExtensions property in
	 * the config file.
	 *
	 * @return <code>Vector</code> of fully qualified
	 * 		   <code>EPPCmdRspExtensions</code> class <code>Strings</code>.
	 *
	 * @exception EPPEnvException
	 */
	public static Vector getCmdResponseExtensions() throws EPPEnvException {
		Vector ret = new Vector();

		try {
			String		    setting =
				Environment.getEnv("EPP.CmdRspExtensions");

			StringTokenizer tokenizer = new StringTokenizer(setting);

			while (tokenizer.hasMoreElements()) {
				ret.addElement(tokenizer.nextElement());
			}
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return ret;
	}

	/**
	 * Returns a vector of class names that are factories for each EPP Mapping.
	 * These are the values specified by the EPP.MapFactories property in the
	 * config file.
	 *
	 * @return <code>Vector</code> of fully qualified
	 * 		   <code>EPPMapFactory</code> class <code>Strings</code>.
	 *
	 * @exception EPPEnvException
	 */
	public static Vector getServerEventHandlers() throws EPPEnvException {
		Vector ret = new Vector();

		try {
			String setting = Environment.getEnv("EPP.ServerEventHandlers");

			StringTokenizer tokenizer = new StringTokenizer(setting);

			while (tokenizer.hasMoreElements()) {
				ret.addElement(tokenizer.nextElement());
			}
		}
		 catch (EnvException myException) {
			throw new EPPEnvException("EPP Environment Exception : "
									  + myException.getMessage());
		}

		return ret;
	}

	/**
	 * Returns a vector of class names that are factories for each EPP Mapping.
	 * These are the values specified by the EPP.MapFactories property in the
	 * config file.
	 *
	 * @return <code>Vector</code> of fully qualified
	 * 		   <code>EPPMapFactory</code> class <code>Strings</code>.
	 */
	public static String getServerEPPAssembler() {
		return Environment.getOption("EPP.ServerAssembler");
	}

	/**
	 * SSL Socket Option. Returns the type of SSL protocol
	 *
	 * @return String value for the property
	 */
	public static String getSSLProtocol() {
		return Environment.getOption("EPP.SSLProtocol");
	}
	
	/**
	 * SSL Socket Option.  Returns the SSL protocols supported.
 	 *
	 * @return <code>String</code> array of protocols if defined;<code>null</code> otherwise
	 */
	public static String[] getSSLEnabledProtocols() {
		String[] theProtocols = null;
		String thePropValue = Environment.getOption("EPP.SSLEnabledProtocols");
		
		// Property is defined?
		if ((thePropValue != null) && (thePropValue.length() != 0)) {
			StringTokenizer tokenizer = new StringTokenizer(thePropValue);
			
			int numProtocols = tokenizer.countTokens();
			
			theProtocols = new String[numProtocols];
			
			for (int i = 0; i < numProtocols; i++) {
				theProtocols[i] = tokenizer.nextToken();
			}			
			
		}
		
		return theProtocols;
	}

	/**
	 * SSL Socket Option. Returns the type of KeyManager used by ssl sockets
	 *
	 * @return String value for the property
	 * 
	 * @deprecated With change to JDK 1.4
	 */
	public static String getSSLKeyManager() {
		return Environment.getOption("EPP.SSLKeyManager");
	}

	/**
	 * SSL Socket Option. Returns the type of KeyStore used by ssl sockets
	 *
	 * @return String value for the property
	 */
	public static String getKeyStore() {
		return Environment.getOption("EPP.SSLKeyStore");
	}

	/**
	 * SSL Socket Option.  Returns the SSL Keys filename.
	 *
	 * @return String value for the property
	 */
	public static String getSSLKeyFileName() {
		return Environment.getOption("EPP.SSLKeyFileName");
	}
	
	/**
	 * Gets the SSL trust store file name.  
	 * 
	 * @return SSL trust store file name if defined;<code>null</code> otherwise.
	 */
	public static String getSSLTrustStoreFileName() {
		return Environment.getOption("EPP.SSLTrustStoreFileName");
	}

	/**
	 * Gets the SSL trust store passphrase.  
	 * 
	 * @return SSL trust store passphrase if defined;<code>null</code> otherwise.
	 */
	public static String getSSLTrustStorePassPhrase() {
		return Environment.getOption("EPP.SSLTrustStorePassPhrase");
	}
	
	/**
	 * Gets the SSL debug setting with the default of &quot;none&quot;.  
	 * Possible values include:<br>
	 * <br><ul>
	 * <li>none - No debug
	 * <li>all - All debug
	 * </ul>
	 * 
	 * @return Value of javax.net.debug property if defined; &quot;none&quot; otherwise.
	 */
	public static String getSSLDebug() {
		return Environment.getProperty("javax.net.debug", "none");
	}

	/**
	 * Gets the optional SSL enabled cipher suites   
	 * 
	 * @return <code>>String</code> array if defined; <code>null</code> otherwise.
	 */	
	public static String[] getSSLEnabledCipherSuites() {
		String[] theSSLEnabledCipherSuites = null;
		String theValue = Environment.getOption("EPP.SSLEnabledCipherSuites");
		// Property is defined?
		if ((theValue != null) && (theValue.length() != 0)) {
			StringTokenizer tokenizer = new StringTokenizer(theValue);
			
			int numSSLEnabledCipherSuites = tokenizer.countTokens();
			
			theSSLEnabledCipherSuites = new String[numSSLEnabledCipherSuites];
			
			for (int i = 0; i < numSSLEnabledCipherSuites; i++) {
				theSSLEnabledCipherSuites[i] = tokenizer.nextToken();
			}			
			
		}
		
		return theSSLEnabledCipherSuites;
	}
	
	
	/**
	 * SSL Socket Option. Returns the SSL Passphrase
	 *
	 * @return String value for the property
	 */
	public static String getSSLPassPhrase() {
		return Environment.getOption("EPP.SSLPassPhrase");
	}

	/**
	 * SSL Socket Option. Returns the SSL Key Passphrase.  If 
	 * this property is not defined, than EPP.SSLPassPhrase 
	 * should be used for both the store passphrase and the 
	 * key passphrase.
	 *
	 * @return String value for the property
	 */
	public static String getSSLKeyPassPhrase() {
		return Environment.getOption("EPP.SSLKeyPassPhrase");
	}

	
	
	/**
	 * PoolMan Server Option. Returns number of initial objects instance create
	 * upon pool instantiation
	 *
	 * @return int initial objects instance
	 */
	public static int getServerParserInitObjs() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("initialObjects")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 1;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns minimum number of objects to maintain in
	 * the pool
	 *
	 * @return int minimum number of objects
	 */
	public static int getServerParserMinSize() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("minimumSize")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 0;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns maximum number of objects to maintain at
	 * any one time in the pool
	 *
	 * @return int maximum number of objects
	 */
	public static int getServerParserMaxSize() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("maximumSize")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return Integer.MAX_VALUE;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns if emergency objects will be created once
	 * the maximum size of a pool is reached but requested are still waiting
	 * on objects.
	 *
	 * @return boolean if emergency objects will be created
	 */
	public static boolean getServerParserMaxSoft() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("maximumSoft")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return true;
		}

		else {
			return new Boolean(opt).booleanValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns the length of time, in seconds, that each
	 * object has to live before being destroyed and removed from the pool
	 *
	 * @return int length of time (seconds)
	 */
	public static int getServerParserObjTimeout() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("objectTimeout")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 1200;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns the length of time, in seconds, that a
	 * client has to keep an object before it can automatically returned to
	 * its pool.
	 *
	 * @return int length of time (seconds)
	 */
	public static int getServerParserUserTimeout() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("userTimeout")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 1200;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns the length of time, in seconds, the pool
	 * skimmer waits between reap cycles.
	 *
	 * @return int length of time (seconds)
	 */
	public static int getServerParserSkimmerFreq() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("skimmerFrequency")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 660;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns each time the pool is sized down by the
	 * skimmer
	 *
	 * @return int each time the pool is sized down by the skimmer
	 */
	public static int getServerParserShrinkBy() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("shrinkBy").toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 5;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Server Option. Returns the PATH to a file that is pool will
	 * append logging information to.
	 *
	 * @return String log file
	 */
	public static String getServerParserLogFile() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("logFile").toString();

		return Environment.getOption(str);
	}

	/**
	 * PoolMan Server Option. Returns if verbose logging information will be
	 * printed
	 *
	 * @return boolean if verbose logging information will be printed
	 */
	public static boolean getServerParserDebug() {
		String str =
			new StringBuffer(POOLMAN_SERVER_PREFIX).append("debugging")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return false;
		}

		else {
			return new Boolean(opt).booleanValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns number of initial objects instance create
	 * upon pool instantiation
	 *
	 * @return int initial objects instance
	 */
	public static int getClientParserInitObjs() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("initialObjects")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 1;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns minimum number of objects to maintain in
	 * the pool
	 *
	 * @return int minimum number of objects
	 */
	public static int getClientParserMinSize() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("minimumSize")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 0;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns maximum number of objects to maintain at
	 * any one time in the pool
	 *
	 * @return int maximum number of objects
	 */
	public static int getClientParserMaxSize() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("maximumSize")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return Integer.MAX_VALUE;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns if emergency objects will be created once
	 * the maximum size of a pool is reached but requested are still waiting
	 * on objects.
	 *
	 * @return boolean if emergency objects will be created
	 */
	public static boolean getClientParserMaxSoft() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("maximumSoft")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return true;
		}

		else {
			return new Boolean(opt).booleanValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns the length of time, in seconds, that each
	 * object has to live before being destroyed and removed from the pool
	 *
	 * @return int length of time (seconds)
	 */
	public static int getClientParserObjTimeout() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("objectTimeout")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 1200;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns the length of time, in seconds, that a
	 * client has to keep an object before it can automatically returned to
	 * its pool.
	 *
	 * @return int length of time (seconds)
	 */
	public static int getClientParserUserTimeout() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("userTimeout")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 1200;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns the length of time, in seconds, the pool
	 * skimmer waits between reap cycles.
	 *
	 * @return int length of time (seconds)
	 */
	public static int getClientParserSkimmerFreq() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("skimmerFrequency")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 660;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns each time the pool is sized down by the
	 * skimmer
	 *
	 * @return int each time the pool is sized down by the skimmer
	 */
	public static int getClientParserShrinkBy() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("shrinkBy").toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return 5;
		}

		else {
			return new Integer(opt).intValue();
		}
	}

	/**
	 * PoolMan Client Option. Returns the PATH to a file that is pool will
	 * append logging information to.
	 *
	 * @return String log file
	 */
	public static String getClientParserLogFile() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("logFile").toString();

		return Environment.getOption(str);
	}

	/**
	 * PoolMan Client Option. Returns if verbose logging information will be
	 * printed
	 *
	 * @return boolean if verbose logging information will be printed
	 */
	public static boolean getClientParserDebug() {
		String str =
			new StringBuffer(POOLMAN_CLIENT_PREFIX).append("debugging")
												   .toString();

		String opt = Environment.getOption(str);

		if (opt == null) {
			return false;
		}

		else {
			return new Boolean(opt).booleanValue();
		}
	}

	/**
	 * Returns a vector of class names that are factories for each EPP Mapping.
	 * These are the values specified by the EPP.MapFactories property in the
	 * config file.
	 *
	 * @return <code>Vector</code> of fully qualified
	 * 		   <code>EPPPollHandler</code> class <code>Strings</code>.
	 *
	 * @exception EPPEnvException
	 */
	public static Vector getPollHandlers() throws EPPEnvException {
		Vector ret = new Vector();

		try {
			String		    setting = Environment.getEnv("EPP.PollHandlers");

			StringTokenizer tokenizer = new StringTokenizer(setting);

			while (tokenizer.hasMoreElements()) {
				ret.addElement(tokenizer.nextElement());
			}
		}
		 catch (EnvException myException) {
			// No EPP.PollHandlers setting found.  This setting
			// is optional, so no exception will be re-thrown and
			// an empty Vector will be returned.
		}

		return ret;
	}

	/**
	 * Gets if XML Schema Validation is enabled.  The default is
	 * <code>false</code>, but this can be changed by setting the
	 * <code>EPP.Validating</code> property.
	 *
	 * @return <code>true</code> to enable XML Schema Validation;
	 * 		   <code>false</code> otherwise.
	 */
	public static boolean getValidating() {
		boolean ret = false;

		try {
			String setting = Environment.getEnv("EPP.Validating");

			ret = new Boolean(setting).booleanValue();

		}
		 catch (EnvException myException) {
			// Do nothing, since this setting is optional
		}
		return ret;
	}


    /**
     * Gets if FullSchemaChecking is enabled on the Xerces Parser instance.
     *
     * Enable full schema grammar constraint checking, including checking
     * which may be time-consuming or memory intensive.
     * Currently, particle unique attribution constraint
     * checking and particle derivation resriction checking
     * are controlled by this option.
     *
     * The default is
     * <code>false</code>, but this can be changed by setting the
     * <code>EPP.FullSchemaChecking</code> property.
     *
     * @return <code>true</code> to enable Full XML Schema Checking;
     * 		   <code>false</code> otherwise.
     */
    public static boolean getFullSchemaChecking() {
        boolean ret = false ;
        try {
            String setting = Environment.getEnv("EPP.FullSchemaChecking");

            ret = new Boolean(setting).booleanValue();

        }
         catch (EnvException myException) {
            // Do nothing, since this setting is optional
        }
        return ret;
    }
}
