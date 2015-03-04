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
package com.verisign.epp.interfaces;


// PoolMan Imports
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

import com.codestudio.util.GenericPool;
import com.codestudio.util.GenericPoolManager;
import com.codestudio.util.GenericPoolMetaData;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;
import com.verisign.epp.util.EPPEnvSingle;
import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.EPPTransformer;


/**
 * This class represents a logical application. it should be instantiated once
 * in the application life cycle. Its primary function is to initialize all
 * the utilities and connection pool associated with interfaces.  At the
 * termination of the program, this entity will offer resources to close all
 * established the connections gracefully, and it performs all cleanup tasks
 * associated with utility functions.
 */
public class EPPApplication {
	/** Was the parser pool initialized? */
	private static boolean _parserInitialized = false;

	/**
	 * Initializing <code>EPPApplication</code> using the passed in
	 * configuration file.
	 *
	 * @param myConfigFile DOCUMENT ME!
	 *
	 * @exception EPPCommandException
	 */
	public void initialize(String myConfigFile) throws EPPCommandException {
		
		/** Initialize the Env */
		EPPEnvSingle env = EPPEnvSingle.getInstance();

		/**
		 * initialize the Environment
		 */
		try {
			env.initialize(myConfigFile);
		}
		 catch (EPPEnvException e) {
			throw new EPPCommandException("EPPEnvException is thrown :"
										  + e.getMessage());
		}

		/**
		 * Initialize the Logger
		 */
		try {
			switch (EPPEnv.getLogMode()) {
				case EPPEnv.LOG_BASIC:

					Logger root = Logger.getRootLogger();
					root.setLevel(EPPEnv.getLogLevel());
					root.addAppender(new FileAppender(
													  new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN),
													  EPPEnv.getLogFile(), true));

					break;

				case EPPEnv.LOG_CFG_FILE:
					String theLogCfgFileProp = EPPEnv.getLogCfgFile();
				
					File theLogCfgFile = new File(theLogCfgFileProp);
					
					URL theLogCfgFileURL = null;
					boolean theLoadedFromClassPath;
					
					if (theLogCfgFile.exists()) {
						theLoadedFromClassPath = false;
						theLogCfgFileURL = new URL("file:" + theLogCfgFileProp);
						System.out.println("EPPApplication: Loading log configuration file <" + 
								theLogCfgFileProp + "> from file system");
					}
					else {
						theLoadedFromClassPath = true;
						
						theLogCfgFileURL = ClassLoader.getSystemResource(theLogCfgFileProp);
						
						if (theLogCfgFileURL == null) {
							theLogCfgFileURL = EPPApplication.class.getClassLoader().getResource(theLogCfgFileProp);
							System.out.println("EPPApplication: Loading log configuration file <" + 
									theLogCfgFileProp + "> from EPPApplication ClassLoader");
						}
						else {
							System.out.println("EPPApplication: Loading log configuration file <" + 
									theLogCfgFileProp + "> from system ClassLoader");
						
						}
					}
				
					// Configuration file not found?
					if (theLogCfgFileURL == null) {
						throw new EPPCommandException("EPPApplication: Unable to find configuration file :"
								  + theLogCfgFileProp);
					}
					
					DOMConfigurator.configure(theLogCfgFileURL);

					// Loaded from file system?
					if (!theLoadedFromClassPath) {
						// Add log configuration file watch
						Long logCfgFileWatch = EPPEnv.getLogCfgFileWatch();

						if (logCfgFileWatch != null) {
							DOMConfigurator.configureAndWatch(
										theLogCfgFileProp,
										logCfgFileWatch.longValue());
							System.out.println("EPPApplication: Added watch of log configuration file <" + 
									theLogCfgFileProp + "> every " + logCfgFileWatch + " ms");
						}						
					}
					break;

				case EPPEnv.LOG_CUSTOM:
					// Do nothing
					break;

				default:
					throw new EPPCommandException("Invalid log mode of :"
												  + EPPEnv.getLogMode());
			}
		}
		 catch (EPPEnvException e) {
			throw new EPPCommandException("EPPApplication.initialze():  When initializing Log "
										  + e);
		}
		 catch (IOException e) {
			throw new EPPCommandException("EPPApplication.initialze():  When initializing Log "
										  + e);
		}


		/**
		 * Initialize the EPPCodec
		 */
		try {
			//$ashwin
			//gets the list of all the protocolExtensions,CommandResponseExtensions and MapFactories
			Vector protocolExts = null;
			Vector commandExts = null;

			try {
				protocolExts = EPPEnv.getProtocolExtensions();
			}
			 catch (EPPEnvException ex) {
				//If we get a Excepetion here means most propably the Variables
				//EPP.ProtocolExtensions is set to null in the config file.
				protocolExts = null;
			}

			try {
				commandExts = EPPEnv.getCmdResponseExtensions();
			}

			 catch (EPPEnvException ex) {
				//If we get a Excepetion here means most propably the Variables
				//EPP.CmdRspExtensions is set to null in the config file.
				commandExts = null;
			}

			Vector extensionsVector = new Vector();

			if (
				(protocolExts != null)
					&& protocolExts.elements().hasMoreElements()) {
				for (int i = 0; i < protocolExts.size(); i++) {
					extensionsVector.addElement((String) protocolExts.elementAt(i));
				}
			}

			//end if
			if (
				(commandExts != null)
					&& commandExts.elements().hasMoreElements()) {
				for (int j = 0; j < commandExts.size(); j++) {
					extensionsVector.addElement((String) commandExts.elementAt(j));
				}
			}

			//end if
			//Now instantiate the Codec instance with both the mapfactories and extension factories
			EPPCodec.getInstance().init(
										EPPEnv.getMapFactories(),
										extensionsVector);
		}

		//end try
		 catch (EPPEnvException e) {
			throw new EPPCommandException("EPPApplication.initialze():  when initializing EPPCodec: "
										  + e);
		}
		 catch (EPPCodecException e) {
			throw new EPPCommandException("EPPApplication.initialze():  when initializing EPPCodec: "
										  + e);
		}
		 
		// Initialize the Parser Pool
		initParserPool();
		 
	}

	/**
	 * Initialize the XML parser pool, with the name EPPXMLParser.POOL and with
	 * the "com.verisign.epp.util.EPPXMLParser" as the object type. The
	 * remaining configuration settings are retrieved from the
	 * EPPEnv.getClientParser methods.  If there is any error initializing the
	 * pool, and error diagnostic is logged, and the program with stop with a
	 * call to <code>System.exit(1)</code>, since this represents a fatal
	 * error.  The <code>EPPEnv</code> settings referenced include:<br><br>
	 *
	 * <ul>
	 * <li>
	 * getClientParserInitObjs()
	 * </li>
	 * <li>
	 * getClientParserMinSize
	 * </li>
	 * <li>
	 * getClientParserMaxSize()
	 * </li>
	 * <li>
	 * getClientParserMaxSoft()
	 * </li>
	 * <li>
	 * getClientParserObjTimeout()
	 * </li>
	 * <li>
	 * getClientParserUserTimeout()
	 * </li>
	 * <li>
	 * getClientParserSkimmerFreq()
	 * </li>
	 * <li>
	 * getClientParserShrinkBy()
	 * </li>
	 * <li>
	 * getClientParserLogFile()
	 * </li>
	 * <li>
	 * getClientParserDebug()
	 * </li>
	 * </ul>
	 */
	private void initParserPool() {
		// Pool does not exist?
		if (!_parserInitialized) {
			// Create parser pool
			GenericPoolMetaData parserMeta = new GenericPoolMetaData();

			parserMeta.setName( EPPSchemaCachingParser.POOL );
			parserMeta.setObjectType( "com.verisign.epp.util.EPPSchemaCachingParser" );
			parserMeta.setInitialObjects( EPPEnv.getClientParserInitObjs() );
			parserMeta.setMinimumSize( EPPEnv.getClientParserMinSize() );
			parserMeta.setMaximumSize( EPPEnv.getClientParserMaxSize() );
			parserMeta.setMaximumSoft( EPPEnv.getClientParserMaxSoft() );
			parserMeta.setObjectTimeout( EPPEnv.getClientParserObjTimeout() );
			parserMeta.setUserTimeout( EPPEnv.getClientParserUserTimeout() );
			parserMeta.setSkimmerFrequency( EPPEnv.getClientParserSkimmerFreq() );
			parserMeta.setShrinkBy( EPPEnv.getClientParserShrinkBy() );
			parserMeta.setLogFile( EPPEnv.getClientParserLogFile() );
			parserMeta.setDebugging( EPPEnv.getClientParserDebug() );

			GenericPool parserPool = new GenericPool( parserMeta );
			GenericPoolManager.getInstance().addPool( EPPSchemaCachingParser.POOL,
					parserPool );
			
			// Create transformer pool
			GenericPoolMetaData transformerMeta = new GenericPoolMetaData();

			transformerMeta.setName( EPPTransformer.POOL );
			transformerMeta.setObjectType( "com.verisign.epp.util.EPPTransformer" );
			transformerMeta.setInitialObjects( EPPEnv.getClientParserInitObjs() );
			transformerMeta.setMinimumSize( EPPEnv.getClientParserMinSize() );
			transformerMeta.setMaximumSize( EPPEnv.getClientParserMaxSize() );
			transformerMeta.setMaximumSoft( EPPEnv.getClientParserMaxSoft() );
			transformerMeta.setObjectTimeout( EPPEnv.getClientParserObjTimeout() );
			transformerMeta.setUserTimeout( EPPEnv.getClientParserUserTimeout() );
			transformerMeta.setSkimmerFrequency( EPPEnv.getClientParserSkimmerFreq() );
			transformerMeta.setShrinkBy( EPPEnv.getClientParserShrinkBy() );
			transformerMeta.setLogFile( EPPEnv.getClientParserLogFile() );
			transformerMeta.setDebugging( EPPEnv.getClientParserDebug() );

			GenericPool transformerPool = new GenericPool( transformerMeta );
			GenericPoolManager.getInstance().addPool( EPPTransformer.POOL,
					transformerPool );

			_parserInitialized = true;
		}
	}

	/**
	 * Intstance method responsible for Cleanup and termination of the
	 * EPPApplication.
	 *
	 * @exception EPPCommandException
	 */
	public void endApplication() throws EPPCommandException {
		//do nothing
	}
}
