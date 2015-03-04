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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.verisign.epp.transport.EPPConFactorySingle;
import com.verisign.epp.util.EPPEnvException;
import com.verisign.epp.util.EPPEnvSingle;


/**
 * $Id: EPPConFactoryTst.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPConFactoryTst {
	/** DOCUMENT ME! */
	EPPClientCon myClient = null;

	/**
	 * DOCUMENT ME!
	 *
	 * @param myClientCon DOCUMENT ME!
	 *
	 * @throws EPPConException DOCUMENT ME!
	 */
	public void handleIO(EPPClientCon myClientCon) throws EPPConException {
		/** Start to read and write to Server */
		PrintWriter    out		  = null;
		BufferedReader in = null;
		out     = new PrintWriter(myClientCon.getOutputStream(), true);
		in	    = new BufferedReader(new InputStreamReader(myClientCon
														   .getInputStream()));

		String fromServer;
		String fromUser;

		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);

				if (fromServer.equals("Bye.")) {
					break;
				}

				out.println("Bye.");
				System.out.println("Client: Bye.");
			}
		}
		 catch (IOException myException) {
			throw new EPPConException(myException.getMessage());
		}

		try {
			out.close();
			in.close();
		}
		 catch (IOException myException) {
			throw new EPPConException(myException.getMessage());
		}

		return;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		EPPConFactoryTst    myInstance = new EPPConFactoryTst();
		EPPConFactorySingle myCon = EPPConFactorySingle.getInstance();
		EPPEnvSingle	    env   = EPPEnvSingle.getInstance();

		if (args.length == 0) {
			System.out.println("EPP Configuration File must be specified");
			System.exit(1);
		}

		/**
		 * Get the properties from the file
		 */
		try {
			env.initialize(args[0]);
		}
		 catch (EPPEnvException e) {
			System.out.println("EPPEvnException is thrown :" + e.getMessage());

			return;
		}

		/**
		 * Connect to the Server
		 */
		try {
			myInstance.myClient = myCon.getEPPConnection();
		}
		 catch (EPPConException myException) {
			System.out.println("Exception : " + myException.getMessage());

			return;
		}

		try {
			myInstance.myClient.initialize();
		}
		 catch (EPPConException myException) {
			System.out.println("get EPP Connection Exception when initiallizing : "
							   + myException.getMessage());

			return;
		}

		try {
			myInstance.handleIO(myInstance.myClient);
			;
		}
		 catch (EPPConException myException) {
			System.out.println("EPP Connection Exception getting I/O Stream : "
							   + myException.getMessage());

			return;
		}

		try {
			myInstance.myClient.close();
		}
		 catch (EPPConException myException) {
			System.out.println("get EPP Connection Exception when Closing : "
							   + myException.getMessage());
		}
	}
}
