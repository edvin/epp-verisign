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

/**
 * Represents an EPPPollDataSource interface that is implemented by any class
 * that needs to manipulate their own data source. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public interface EPPPollDataSource {
	/**
	 * Gets the next poll data record for the specified  recipient.  The
	 * concrete class used to specify  the recipient is specific to the
	 * concrete <code>EPPPollData</code>.
	 *
	 * @param aRecp Recipient of the poll message.  The  concrete class is
	 * 		  defined by the concrete <code>EPPPollDataSource</code>.
	 * @param aContextData Server specific data that is passed  through to the
	 * 		  data source (i.e. database connection).
	 *
	 * @return Next poll data record.  The record is not removed from the
	 * 		   recipients queue
	 *
	 * @exception EPPPollQueueException Error getting message from queue
	 */
	EPPPollDataRecord get(Object aRecp, Object aContextData)
				   throws EPPPollQueueException;

	/**
	 * Puts a message in the poll queue associated with  <code>aRecp</code>.
	 *
	 * @param aRecp Recipient of the poll message.  The  concrete class is
	 * 		  defined by a concrete <code>EPPPollDataSource</code>.
	 * @param aKind Defines the kind of the poll message.  The  kinds of
	 * 		  messages supported is based on the concrete
	 * 		  <code>EPPPollDataSource</code>.
	 * @param aData Poll message data.  The concrete class is  specific to the
	 * 		  concrete <code>EPPPollDataSource</code>
	 * @param aContextData Server specific data that is passed  through to the
	 * 		  data source (i.e. database connection).
	 *
	 * @exception EPPPollQueueException Error putting message in queue
	 */
	void put(Object aRecp, String aKind, Object aData, Object aContextData)
	  throws EPPPollQueueException;

	/**
	 * Deletes a message from a recipients poll queue my  message identifier.
	 *
	 * @param aRecp Recipient queue.  The  concrete class is defined by the
	 * 		  concrete <code>EPPPollDataSource</code>.
	 * @param aMsgId identifier to delete
	 * @param aContextData Server specific data that is passed  through to the
	 * 		  data source (i.e. database connection).
	 *
	 * @return Number of messages left in queue
	 *
	 * @exception EPPPollQueueException Error deleting the  message
	 */
	int delete(Object aRecp, String aMsgId, Object aContextData)
		throws EPPPollQueueException;
}


// End class EPPPollDataSource
