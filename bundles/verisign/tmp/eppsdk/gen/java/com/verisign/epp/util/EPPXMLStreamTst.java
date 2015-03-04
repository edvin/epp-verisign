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


// Log4j Imports
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.w3c.dom.Document;

import com.verisign.epp.exception.EPPException;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.transport.EPPConFactorySingle;


/**
 * This Class privides a unit test for EPPXMLStream
 *
 * @author P. Amiri
 * @version $Id: EPPXMLStreamTst.java,v 1.2 2004/01/26 21:21:07 jim Exp $
 *
 * @since JDK1.0
 */
public class EPPXMLStreamTst {
	/**
	 * DOCUMENT ME!
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {
		String		    myString = null;

		EPPXMLStreamTst myInstance = new EPPXMLStreamTst();

		Socket		    socket		   = null;
		InputStream     myInputStream  = null;
		OutputStream    myOutputStream = null;
		Document	    myDoc		   = null;
		EPPXMLStream    myStream	   = null;
		EPPClientCon    myConnection   = null;

		/** Initialize the Env */
		EPPEnvSingle env = EPPEnvSingle.getInstance();

		/**
		 * initialize the Environment
		 */
		try {
			env.initialize("epp.config");
		}
		 catch (EPPEnvException e) {
			System.out.println("EPPEvnException is thrown :" + e.getMessage());

			return;
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

		/** Get the Client Connection from factory */
		EPPConFactorySingle myCon = EPPConFactorySingle.getInstance();

		/**
		 * instantial this Class.
		 */
		myStream = new EPPXMLStream();

		/**
		 * Connect to the Server
		 */
		try {
			myConnection = myCon.getEPPConnection();
		}
		 catch (EPPConException myException) {
			System.out.println("EPPApplication.initialze():  "
							   + myException.getMessage());

			return;
		}

		try {
			myConnection.initialize();
		}
		 catch (EPPConException myException) {
			System.out.println("EPPApplication.initialze():  "
							   + myException.getMessage());

			return;
		}

		/**
		 * Set up the Connection
		 */
		try {
			myInputStream	   = myConnection.getInputStream();
			myOutputStream     = myConnection.getOutputStream();
		}
		 catch (EPPConException myException) {
			System.out.println("EPPConException When getting Streams"
							   + myException.getMessage());

			return;
		}

		/**
		 * Parse the Document
		 */
		try {
			boolean theDocFound = false;
			while (!theDocFound) {
				try {
					myDoc = myStream.read(myInputStream);
					theDocFound = true;
				}
				catch (InterruptedIOException ex) {			
					System.out.println("InterruptedIOException, continueing to read: " + 
							ex.getMessage());
				}
			}
		}
		 catch (EPPException myException) {
			System.out.println("EPPException: " + myException.getMessage());

			return;
		}
		 catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());

			return;
		}

		/**
		 * Write a Dom Document
		 */
		try {
			myStream.write(myDoc, myOutputStream);
		}
		 catch (EPPException myException) {
			System.out.println("Exception " + myException.getMessage());

			return;
		}

		/**
		 * Finished so close everything and exit
		 */
		try {
			myInputStream.close();
			myOutputStream.close();
			myConnection.close();
		}
		 catch (EPPConException myException) {
			System.out.println("EPPApplication.endApplication() : "
							   + myException.getMessage());

			return;
		}
		 catch (IOException myException) {
			System.out.println("Exception " + myException.getMessage());

			return;
		}
	}
}
