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


// java imports
import java.util.*;

// EPP Imports
import com.verisign.epp.framework.*;


/**
 * The &lt;PollDataSource&gt; is registered and used by EPPPollQueueMgr to
 * demostrate how data are stored into a queue. The data source can be files,
 * databases or others. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class PollDataSource implements EPPPollDataSource {
	/** Message identifier */
	private static int msgId = 0;

	/** In memory poll queue */
	private Vector queue = new Vector();

	/**
	 * Gets data from queue
	 *
	 * @param aRecp Receipt message queue.  Not used by the Server Stub.
	 * @param aContextData Server specific data that is passed  through to the
	 * 		  data source (i.e. database connection).  Not  used by the Server
	 * 		  Stub.
	 *
	 * @return EPPPollDataRecord if exists; <code>null</code> otherwise.
	 *
	 * @exception EPPPollQueueException Error getting message from queue
	 */
	public synchronized EPPPollDataRecord get(
											  Object aRecp, Object aContextData)
									   throws EPPPollQueueException {
		EPPPollDataRecord obj = null;

		try {
			obj = (EPPPollDataRecord) queue.firstElement();
			obj.setSize(queue.size());
		}
		 catch (NoSuchElementException e) {
			throw new EPPPollQueueException(
											EPPPollQueueException.TYPE_QUEUE_EMPTY,
											"PollDataSource.get(), Empty queue");
		}

		return obj;
	}

	/**
	 * Puts data into queue
	 *
	 * @param aRecp Message recipient.  Not used in Server Stub.
	 * @param aKind The type of response data
	 * @param aData Message data to put in queue
	 * @param aContextData Server specific data that is passed  through to the
	 * 		  data source (i.e. database connection).  Not  used by the Server
	 * 		  Stub.
	 */
	public synchronized void put(
								 Object aRecp, String aKind, Object aData,
								 Object aContextData) {
		// Create the poll data record
		EPPPollDataRecord dataRecord =
			new EPPPollDataRecord(aKind, aData, ++msgId + "");

		// Insert poll data record into queue
		queue.addElement(dataRecord);
	}

	/**
	 * Delete data from data source and return number of messages
	 *
	 * @param aRecp Recipient queue.  Not used in Server Stub.
	 * @param aMsgId Message to delete.  Not used in Server Stub, since the
	 * 		  Server Stub will delete the top message of queue
	 * @param aContextData Server specific data that is passed  through to the
	 * 		  data source (i.e. database connection).  Not  used by the Server
	 * 		  Stub.
	 *
	 * @return int Number of messages left in queue
	 *
	 * @exception EPPPollQueueException Error deleting message
	 */
	public synchronized int delete(
								   Object aRecp, String aMsgId,
								   Object aContextData)
							throws EPPPollQueueException {
		if (queue.size() == 0) {
			throw new EPPPollQueueException(
											EPPPollQueueException.TYPE_MSGID_NOT_FOUND,
											"No messages in queue");
		}

		queue.remove(0);

		return queue.size();
	}
}
