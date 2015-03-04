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

import java.io.*;
import java.net.*;

import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;
import com.verisign.epp.util.EPPEnvSingle;


/**
 * $Id: EPPSampleClient.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPSampleClient {
	/**
	 * DOCUMENT ME!
	 *
	 * @param args DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 */
	public static void main(String[] args) throws IOException {
		Socket		   mySocket = null;
		PrintWriter    out = null;
		BufferedReader in  = null;

		EPPEnvSingle   env = EPPEnvSingle.getInstance();

		try {
			env.initialize("EPP.config");
		}
		 catch (EPPEnvException e) {
			System.out.println("EPPEvnException is thrown :" + e.getMessage());
			System.exit(1);
		}

		try {
			try {
				mySocket =
					new Socket(EPPEnv.getServerName(), EPPEnv.getServerPort());
			}
			 catch (EPPEnvException envException) {
				System.err.println("Could not get Envvariable : "
								   + envException.getMessage());
				System.exit(1);
			}

			out     = new PrintWriter(mySocket.getOutputStream(), true);
			in	    = new BufferedReader(new InputStreamReader(mySocket
															   .getInputStream()));
		}
		 catch (UnknownHostException e) {
			System.err.println("Don't know about host ");
			System.exit(1);
		}
		 catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection.");
			System.exit(1);
		}

		String fromServer;
		String fromUser;

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);

			if (fromServer.equals("Bye.")) {
				break;
			}

			out.println("Hello There Back");
			out.println("Bye.");
		}

		out.close();
		in.close();
		mySocket.close();
	}
}
