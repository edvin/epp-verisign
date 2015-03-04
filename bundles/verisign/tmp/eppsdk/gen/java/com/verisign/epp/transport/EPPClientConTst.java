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
package com.verisign.epp.transport;


// Log4j Imports
import org.apache.log4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;
import com.verisign.epp.util.EPPEnvSingle;


/**
 * EPPClientConTst : This class is not part of Implementatin. It is provided
 * for unit testing, Debuging and it may also serve as a Sample Code also.
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPClientConTst {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPClientConTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** DOCUMENT ME! */
	private EPPClientCon myClient = null;

	/** DOCUMENT ME! */
	private InputStream myInputStream = null;

	/** DOCUMENT ME! */
	private OutputStream myOutputStream = null;

	/**
	 * DOCUMENT ME!
	 *
	 * @param newSocketClient DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws ClassNotFoundException DOCUMENT ME!
	 * @throws InstantiationException DOCUMENT ME!
	 * @throws IllegalAccessException DOCUMENT ME!
	 */
	public EPPClientCon mkEPPClientCon(String newSocketClient)
								throws ClassNotFoundException, 
									   InstantiationException, 
									   IllegalAccessException {
		return (EPPClientCon) Class.forName(newSocketClient).newInstance();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		EPPClientConTst myInstance = new EPPClientConTst();

		EPPEnvSingle    env = EPPEnvSingle.getInstance();

		if (args.length == 0) {
			System.out.println("EPP Configuration File must be specified");
			System.exit(1);
		}

		try {
			env.initialize(args[0]);
		}
		 catch (EPPEnvException e) {
			System.out.println("EPPEvnException is thrown :" + e.getMessage());
			System.exit(1);
		}

		/**
		 * Initialize the Logger
		 */
		try {
			Logger root = Logger.getRootLogger();
			root.setLevel(EPPEnv.getLogLevel());
			root.addAppender(new FileAppender(
											  new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN),
											  EPPEnv.getLogFile(), true));
		}
		 catch (EPPEnvException e) {
			System.out.println("EPPEnvException When intializing Log "
							   + e.getMessage());
			System.exit(1);
		}
		 catch (IOException e) {
			System.out.println("IOException When intializing Log "
							   + e.getMessage());
			System.exit(1);
		}

		/**
		 * Set the Log
		 */
		cat.debug("Starting Client Socket ");

		/**
		 * Startup the Client side implementation
		 */
		try {
			myInstance.myClient =
				myInstance.mkEPPClientCon(EPPEnv.getClientSocketName());
		}
		 catch (EPPEnvException myException) {
			cat.error(
					  "EPPEnvException When Selecting Client "
					  + myException.getMessage(), myException);
			System.out.println("EPPEnvException When Selecting Client "
							   + myException.getMessage());
			System.exit(1);
		}
		 catch (ClassNotFoundException myException) {
			cat.error(
					  "Class Not Found Exception : " + myException.getMessage(),
					  myException);
			System.out.println("Class Not Found Exception : "
							   + myException.getMessage());
			System.exit(1);
		}
		 catch (InstantiationException myException) {
			cat.error(
					  "Instantiation Exception : " + myException.getMessage(),
					  myException);
			System.out.println("Instantiation Exception : "
							   + myException.getMessage());
			System.exit(1);
		}
		 catch (IllegalAccessException myException) {
			cat.error(
					  "Illegal Access Exception : " + myException.getMessage(),
					  myException);
			System.out.println("Illegal Access Exception : "
							   + myException.getMessage());
			System.exit(1);
		}

		try {
			myInstance.myClient.initialize();
		}
		 catch (EPPConException myException) {
			cat.error(
					  "got EPP Connection Exception when initiallizing : "
					  + myException.getMessage(), myException);
			System.out.println("got EPP Connection Exception when initiallizing : "
							   + myException.getMessage());
			System.exit(1);
		}

		try {
			myInstance.myOutputStream     = myInstance.myClient.getOutputStream();
			myInstance.myInputStream	  = myInstance.myClient.getInputStream();
		}
		 catch (EPPConException myException) {
			cat.error(
					  "EPP Connection Exception getting I/O Stream : "
					  + myException.getMessage(), myException);
			System.out.println("EPP Connection Exception getting I/O Stream : "
							   + myException.getMessage());
			System.exit(1);
		}

		PrintWriter    out = null;
		BufferedReader in = null;

		/*
		 * make sure enable the auto flush
		 */
		out     = new PrintWriter(myInstance.myOutputStream, true);
		in	    = new BufferedReader(new InputStreamReader(myInstance.myInputStream));

		BufferedReader stdIn =
			new BufferedReader(new InputStreamReader(System.in));
		String		   fromServer;
		String		   fromUser;

		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);

				if (fromServer.equals("Bye.")) {
					break;
				}

				out.println("Bye.");
			}
		}
		 catch (IOException myException) {
			cat.error(
					  "IO Exception while perform IO with Socket : "
					  + myException.getMessage(), myException);
			System.out.println("IO Exception while perform IO with Socket : "
							   + myException.getMessage());
			System.exit(1);
		}

		out.close();

		try {
			in.close();
		}
		 catch (IOException myException) {
			cat.error(
					  "IO Exception when closing Socket BufferedReader : "
					  + myException.getMessage(), myException);
			System.out.println("IO Exception when closing Socket BufferedReader : "
							   + myException.getMessage());
			System.exit(1);
		}

		try {
			stdIn.close();
		}
		 catch (IOException myException) {
			cat.error(
					  "IO Exception when closing Client BufferedReader : "
					  + myException.getMessage(), myException);
			System.out.println("IO Exception when closing Client BufferedReader : "
							   + myException.getMessage());
			System.exit(1);
		}

		try {
			myInstance.myClient.close();
		}
		 catch (EPPConException myException) {
			cat.error(
					  "got EPP Connection Exception when initiallizing : "
					  + myException.getMessage(), myException);
			System.out.println("got EPP Connection Exception when initiallizing : "
							   + myException.getMessage());
			System.exit(1);
		}
	}
}
