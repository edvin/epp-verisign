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


/**
 * $Id: EPPEventHandler.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPEventHandler implements ServerEventHandler {
	/**
	 * @see java.io
	 */
	private PrintWriter myPrintWriter = null;

	/**
	 * @see java.io
	 */
	private BufferedReader myBufferReader = null;

	/**
	 * @see java.io
	 */
	private InputStream myInputStream = null;

	/**
	 * @see java.io
	 */
	private OutputStream myOutputStream = null;

	/**
	 * DOCUMENT ME!
	 *
	 * @param newInputStream DOCUMENT ME!
	 * @param newOutputStream DOCUMENT ME!
	 */
	public void handleConnection(
								 InputStream newInputStream,
								 OutputStream newOutputStream) {
		myInputStream	   = newInputStream;
		myOutputStream     = newOutputStream;
		myPrintWriter	   = new PrintWriter(myOutputStream, true);
		myBufferReader =
			new BufferedReader(new InputStreamReader(myInputStream));

		myPrintWriter.println("Hello there");

		String fromUser    = null;

		try {
			while ((fromUser = myBufferReader.readLine()) != null) {
				System.out.println("User: " + fromUser);

				if (fromUser.equals("Bye.")) {
					break;
				}
			}
		}
		 catch (IOException myException) {
			System.out.print("This is a exeption" + myException.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws CloneNotSupportedException DOCUMENT ME!
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
