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

// Java imports
import java.net.*;
import java.util.Vector;

import com.verisign.epp.codec.gen.*;
import com.verisign.epp.framework.EPPDispatcher;
import com.verisign.epp.serverstub.ClientConnectionHandler;
import com.verisign.epp.serverstub.ConnectionHandler;
import com.verisign.epp.serverstub.GenHandler;
import com.verisign.epp.transport.EPPConException;

// EPP imports
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPEnvException;
import com.verisign.epp.util.EPPEnvSingle;


/**
 * $Id: EPPServerSingleTst.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @author P. Amiri
 * @version 1.0, 03/10/01
 *
 * @since JDK1.0
 */
public class EPPServerSingleTst {
	/**
	 * DOCUMENT ME!
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		EPPEnvSingle env = EPPEnvSingle.getInstance();

		try {
			env.initialize(args[0]);
		}
		 catch (EPPEnvException e) {
			System.out.println("EPPEvnException is thrown :" + e.getMessage());

			return;
		}

		EPPSrvFactorySingle myFactory   = EPPSrvFactorySingle.getInstance();
		EPPServerCon	    myServerCon = null;
		EPPServerSingleTst  test	    = new EPPServerSingleTst();

		//EPPServerSingle myInst = EPPServerSingle.getInstance();
		//myInst.RunServer(new EPPEventHandler());
		test.initializeCodec();
		test.initializeDispatcher();

		try {
			myServerCon = myFactory.getEPPServer();
		}
		 catch (EPPConException myException) {
			System.out.println("EPPServerCon : Exception "
							   + myException.getMessage());

			return;
		}

		try {
			//		    myServerCon.RunServer(new EPPEventHandler());
			//		    myServerCon.RunServer(new EPPEventTstHandler());
			myServerCon.RunServer(new ClientConnectionHandler());
		}
		 catch (EPPConException myException) {
			System.out.println("RunServer : Exception "
							   + myException.getMessage());

			return;
		}

		return;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void initializeCodec() {
		/**
		 * Register the EPP Event Handlers with the Dispatcher.  The fully
		 * qualified class names of the event handlers should be listed in the
		 * conf file.  EPPEnv had already read them into a Vector that we can
		 * get.
		 */
		EPPDispatcher theDispatcher = EPPDispatcher.getInstance();

		try {
			Vector factories = EPPEnv.getMapFactories();
			(EPPCodec.getInstance()).init(factories);
		}
		 catch (EPPCodecException e) {
			e.printStackTrace();
		}
		 catch (EPPEnvException e) {
			e.printStackTrace();
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void initializeDispatcher() {
		/** Register the EPP Event Handlers with the Dispatcher. */
		EPPDispatcher theDispatcher = EPPDispatcher.getInstance();
		theDispatcher.registerHandler(new GenHandler());

		/**
		 * Register the ConnectionHandler so that the ServerStub's custom
		 * greeting is always sent to connecting clients.
		 */
		theDispatcher.registerConnectionHandler(new ConnectionHandler());
	}
}
