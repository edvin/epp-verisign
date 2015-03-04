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
package com.verisign.epp.framework;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.verisign.epp.codec.gen.EPPMsgQueue;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;

// JUNIT Imports
//import junit.framework.*;
// EPP Imports
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.util.TestErrorHandler;
import com.verisign.epp.util.TestThread;


/**
 * The &lt;EPPPollqueueMgr&gt; is used to handle the Poll command which allows
 * to discover and retrieve client service messages from a server. Once
 * GenHandler issues the get method, EPPPollQueueMgr receives data from
 * EPPPollDataSource and then returns concrete EPPResponse back to GenHandler.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 */
public class EPPPollQueueMgr {
	/** Singleton instance */
	private static EPPPollQueueMgr theQueueMgr = new EPPPollQueueMgr();

	/** Poll data source */
	private EPPPollDataSource dataSource = null;

	/** Poll queue handlers */
	private Hashtable handlers = new Hashtable();

	/**
	 * Private Constrcutor (Singleton pattern.)
	 */
	private EPPPollQueueMgr() {
	}

	/**
	 * Gets instance of EPPPollQueueMgr
	 *
	 * @return DOCUMENT ME!
	 */
	public static EPPPollQueueMgr getInstance() {
		return theQueueMgr;
	}

	/**
	 * Gets data source
	 *
	 * @return DOCUMENT ME!
	 */
	public EPPPollDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Registers Poll Handler which is from config file.
	 *
	 * @param aHandler DOCUMENT ME!
	 */
	public void register(EPPPollHandler aHandler) {
		handlers.put(aHandler.getKind(), aHandler);
	}

	/**
	 * Sets data source which is from config file.
	 *
	 * @param aSource DOCUMENT ME!
	 */
	public void setDataSource(EPPPollDataSource aSource) {
		dataSource = aSource;
	}

	/**
	 * Get EPPResponse from data source
	 *
	 * @param aRecp DOCUMENT ME!
	 * @param aContextData DOCUMENT ME!
	 *
	 * @return EPPResponse Returns the concrete <code>EPPResponse</code>
	 *
	 * @exception EPPPollQueueException Unable to get EPPResponse from data
	 * 			  source
	 */
	public EPPResponse get(Object aRecp, Object aContextData)
					throws EPPPollQueueException {
		EPPPollDataRecord theRecord = null;

		try {
			theRecord = dataSource.get(aRecp, aContextData);
		}
		 catch (EPPPollQueueException ex) {
			if (ex.getType() == EPPPollQueueException.TYPE_QUEUE_EMPTY) {
				// No Record for recipient
				EPPResponse theResponse = new EPPResponse();
				theResponse.setResult(
									  EPPResult.SUCCESS_POLL_NO_MSGS,
									  "Command completed successfully; no messages");

				return theResponse;
			}

			//just re-throw the exception.
			throw ex;
		}

		// Needs to find appropriate handler
		EPPPollHandler theHandler =
			(EPPPollHandler) handlers.get(theRecord.getKind());

		// No associated handler?
		if (theHandler == null) {
			throw new EPPPollQueueException("Handler for kind "
											+ theRecord.getKind()
											+ " does not exist");
		}

		return theHandler.toResponse(theRecord);
	}

	/**
	 * Put object into data source
	 *
	 * @param aRecp DOCUMENT ME!
	 * @param aKind DOCUMENT ME!
	 * @param aData DOCUMENT ME!
	 * @param aContextData DOCUMENT ME!
	 *
	 * @exception EPPPollQueueException Error putting message in queue
	 */
	public void put(
					Object aRecp, String aKind, Object aData,
					Object aContextData) throws EPPPollQueueException {
		dataSource.put(aRecp, aKind, aData, aContextData);
	}

	/**
	 * Delete object from data source
	 *
	 * @param aRecp DOCUMENT ME!
	 * @param aMsgId DOCUMENT ME!
	 * @param aContextData DOCUMENT ME!
	 *
	 * @return EPPResponse
	 *
	 * @exception EPPPollQueueException Error deleting the message
	 */
	public EPPResponse delete(Object aRecp, String aMsgId, Object aContextData)
					   throws EPPPollQueueException {
		int queueSize = 0;

		try {
			queueSize = dataSource.delete(aRecp, aMsgId, aContextData);
		}
		 catch (EPPPollQueueException ex) {
			if (ex.getType() == EPPPollQueueException.TYPE_MSGID_NOT_FOUND) {
				EPPResult theResult =
					new EPPResult(EPPResult.OBJECT_DOES_NOT_EXIST);
				theResult.addValue("<epp:poll msgID=\"" + aMsgId
								   + "\" op=\"ack\"/>");

				EPPResponse theResponse = new EPPResponse();
				theResponse.setResult(theResult);

				return theResponse;
			}

			//just re-throw the exception.
			throw ex;
		}

		EPPResult   theResult   = new EPPResult(EPPResult.SUCCESS);
		EPPResponse theResponse = new EPPResponse();
		theResponse.setResult(theResult);
		theResponse.setMsgQueue(new EPPMsgQueue(new Long(queueSize), aMsgId));

		return theResponse;
	}
}
