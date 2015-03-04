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
package com.verisign.epp.serverstub;


// Log4J Imports
import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;

// Java imports
import java.net.*;
import java.util.Enumeration;
import java.util.Vector;

// EPP imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.framework.*;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.transport.*;
import com.verisign.epp.util.*;


/**
 * The <code>Server</code> class is responsible for reading the config file,
 * instantiating an implementation of a ServerSocket (Plain or SSL), and
 * specifying the <code>ServerEventHandler</code> class that will be
 * instantiated with each new client connection.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class Server {
	/** Default port to Listen on */
	public static final int WK_PORT = 2015;

	/** Category for logging */
	private static final Logger cat =
		Logger.getLogger(
						 Server.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Supported mappings.  Be sure that the EPPCodec is initialized with some
	 * or all of these.  See the initializeCodec() method here for details.
	 */
	/** The number of connections to this server */
	private int connection_count = 0;

	/** The port this server listens for connections on */
	private int port;

	/**
	 * Construct a Server instance
	 *
	 * @param configFileName The name of the config file where EPP properites
	 * 		  are located
	 */
	public Server(String configFileName) {
		try {
			// Initialize environment settings
			EPPEnvSingle env = EPPEnvSingle.getInstance();

			env.initialize(configFileName);

			// Initalize the logging facility (Log4J)
			initializeLogging();

			// Initialize the server connection factory
			EPPSrvFactorySingle theFactory = EPPSrvFactorySingle.getInstance();

			// Initialize the dispatcher
			initializeDispatcher();

			// Initialize the data source (in memory queue)
			EPPPollQueueMgr.getInstance().setDataSource(new PollDataSource());

			// Initialize the poll queue
			initializePollQueue();

			cat.info("EPP Server: Starting server...");
			cat.info("EPP Server: Creating server socket...");

			EPPServerCon theServer = theFactory.getEPPServer();
			theServer.RunServer(new ClientConnectionHandler());
		}
		 catch (EPPConException e) {
			cat.error("EPP Server: Connection Exception: " + e);
			e.printStackTrace();
			System.exit(1);
		}
		 catch (EPPEnvException e) {
			e.printStackTrace();
			System.exit(1);
		}
		 catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Initialize the logging based on the EPP.LogMode, EPP.LogLevel, 
	 * EPP.LogFile or the
	 * EPP.LogCfgFile and optionally EPP.LogCfgFileWatch.
	 */
	public void initializeLogging() {
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
						System.out.println("Server.initializeLogging: Loading log configuration file <" + 
								theLogCfgFileProp + "> from file system");
					}
					else {
						theLoadedFromClassPath = true;
						
						theLogCfgFileURL = ClassLoader.getSystemResource(theLogCfgFileProp);
						
						if (theLogCfgFileURL == null) {
							theLogCfgFileURL = Server.class.getClassLoader().getResource(theLogCfgFileProp);
							System.out.println("Server.initializeLogging: Loading log configuration file <" + 
									theLogCfgFileProp + "> from Server ClassLoader");
						}
						else {
							System.out.println("Server.initializeLogging: Loading log configuration file <" + 
									theLogCfgFileProp + "> from system ClassLoader");
						
						}
					}
				
					// Configuration file not found?
					if (theLogCfgFileURL == null) {
						System.err.println("Server.initializeLogging: Unable to find configuration file :"
								  + theLogCfgFileProp);
						System.exit(1);
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
							System.out.println("Server.initializeLogging: Added watch of log configuration file <" + 
									theLogCfgFileProp + "> every " + logCfgFileWatch + " ms");
						}						
					}
					break;

				case EPPEnv.LOG_CUSTOM:
					// Do nothing
					break;

				default:
					System.err.println("Server.initializeLogging(): Invalid log mode of :"
												  + EPPEnv.getLogMode());
					System.exit(1);
					
			}
		}
		 catch (EPPEnvException e) {
		 	System.err.println("Server.initializeLogging():  When initializing Log "
										  + e);
		 	System.exit(1);
		}
		 catch (IOException e) {
		 	System.err.println("Server.initializeLogging():  When initializing Log "
										  + e);
		 	System.exit(1);
		}
	}

	/**
	 * Initialize the poll handler based on the EPP.PollHandlers,
	 */
	public void initializePollQueue() {
		EPPPollQueueMgr thePollQueue = EPPPollQueueMgr.getInstance();

		try {
			Vector handlerClasses = EPPEnv.getPollHandlers();

			if (handlerClasses != null) {
				Enumeration e = handlerClasses.elements();

				while (e.hasMoreElements()) {
					String handlerClassName = (String) e.nextElement();
					Class  handlerClass = Class.forName(handlerClassName);

					// register poll handler
					thePollQueue.register((EPPPollHandler) handlerClass
										  .newInstance());
					cat.info("Successfully loaded poll handler: "
							 + handlerClass.getName());
				}

				//end while
			}

			// end if (handlerClasses != null)
		}

		// end try
		 catch (EPPEnvException e) {
			cat.error("Couldn't initialize the environment", e);
			System.exit(1);
		}
		 catch (InstantiationException e) {
			cat.error(
					  "Couldn't instantiate one of the specified"
					  + "server poll handlers", e);
			System.exit(1);
		}
		 catch (IllegalAccessException e) {
			cat.error(
					  "Couldn't instantiate one of the specified"
					  + "server poll handlers"
					  + "\n The class or initializer is not accessible", e);
			System.exit(1);
		}
		 catch (ClassNotFoundException e) {
			cat.error(
					  "Couldn't instantiate one of the poll handlers"
					  + "listed in the conf file", e);
			System.exit(1);
		}
	}

	/**
	 * Initializes <code>Server</code>'s dispatcher to handle the commmands
	 * defined by the supported EPP mappings. At this point we also get the
	 * list of Command Response extensions class names from the epp.config
	 * file and add the CommandResponseextension classes to the EPPExtFaactory
	 */
	public void initializeDispatcher() {
		/**
		 * Register the EPP Event Handlers with the Dispatcher.  The fully
		 * qualified class names of the event handlers should be listed in the
		 * conf file.  EPPEnv had already read them into a Vector that we can
		 * get.
		 */
		EPPDispatcher theDispatcher = EPPDispatcher.getInstance();

		try {
			Vector	    handlerClasses = EPPEnv.getServerEventHandlers();
			Enumeration e = handlerClasses.elements();

			while (e.hasMoreElements()) {
				String handlerClassName = (String) e.nextElement();
				Class  handlerClass = Class.forName(handlerClassName);
				theDispatcher.registerHandler((com.verisign.epp.framework.EPPEventHandler) handlerClass
											  .newInstance());

				cat.info("Successfully loaded server handler: "
						 + handlerClass.getName());
			}

			/**
			 * Now add the CommandResponse level extensions to the extension
			 * factories provided if any of the command responselevel
			 * extensions exist .This can be done by adding the
			 * commandresponselevel extensions to the EPPFactories method
			 * addExtFactory
			 */
			EPPFactory factory     = EPPFactory.getInstance();
			Vector     commandExts = EPPEnv.getCmdResponseExtensions();

			if (
				(commandExts != null)
					&& commandExts.elements().hasMoreElements()) {
				for (int i = 0; i < commandExts.size(); i++) {
					String commandExtensionClassName =
						(String) commandExts.elementAt(i);

					try {
						factory.addExtFactory(commandExtensionClassName);
					}
					 catch (EPPCodecException ex) {
						cat.error("Couldn't load the Extension Factory"
								  + "associated with the CommandResponseExtensions"
								  + ex);

						System.exit(1);
					}

					cat.info("Successfully loaded Command Extension Class:"
							 + commandExtensionClassName);
				}

				//end for
			}

			//end if
		}

		//end try
		 catch (EPPEnvException e) {
			cat.error("Couldn't initialize the environment", e);
			System.exit(1);
		}
		 catch (InstantiationException e) {
			cat.error(
					  "Couldn't instantiate one of the specified"
					  + " server event handlers", e);
			System.exit(1);
		}
		 catch (IllegalAccessException e) {
			cat.error(
					  "Couldn't instantiate one of the specified"
					  + " server event handlers"
					  + "\n The class or initializer is not accessible", e);
			System.exit(1);
		}
		 catch (ClassNotFoundException e) {
			cat.error(
					  "Couldn't instantiate one of the event handlers"
					  + " listed in the conf file", e);
			System.exit(1);
		}

		/**
		 * See if a specific EPPAssembler has been specified. If so load it.
		 */
		try {
			String assemblerClassName = EPPEnv.getServerEPPAssembler();

			if (
				(assemblerClassName != null)
					&& (assemblerClassName.length() > 0)) {
				cat.info("Found Assembler class in config: ["
						 + assemblerClassName + "]");
				cat.info("Attempting to instantiate " + assemblerClassName
						 + "...");

				Class		 assemblerClass = Class.forName(assemblerClassName);
				EPPAssembler assembler =
					(com.verisign.epp.framework.EPPAssembler) assemblerClass
										.newInstance();

				theDispatcher.setAssembler(assembler);

				cat.info("Successfully set assembler " + assemblerClassName);
			}
			else {
				cat.info("No EPPServerAssembler specified. Using default... "
						 + "(com.verisign.epp.framework.EPPXMLAssembler)");

				theDispatcher.setAssembler(new EPPXMLAssembler());
			}
		}
		 catch (InstantiationException e) {
			cat.error(
					  "Couldn't instantiate the specified"
					  + " Server Assembler class. Check class name "
					  + "\n and environment CLASSPATH", e);
			System.exit(1);
		}
		 catch (IllegalAccessException e) {
			cat.error(
					  "Couldn't instantiate the specified"
					  + " Server Assembler class. Check class name"
					  + "\n and CLASSPATH envrionment variable. "
					  + " The class or initializer is not accessible", e);
			System.exit(1);
		}
		 catch (ClassNotFoundException e) {
			cat.error(
					  "Couldn't instantiate the specified"
					  + " Server Assembler class. Check class name "
					  + "\n and environment CLASSPATH", e);
			System.exit(1);
		}

		/**
		 * Register the ConnectionHandler so that the ServerStub's custom
		 * greeting is always sent to connecting clients.
		 */
		theDispatcher.registerConnectionHandler(new ConnectionHandler());
	}

	/**
	 * Runs the Server
	 *
	 * @param args The command line argument should be the epp.config file
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java Server <config-file>");
			System.exit(1);
		}

		Server svr = new Server(args[0]);
	}
}
