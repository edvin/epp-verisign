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

import com.verisign.epp.codec.contact.EPPContactMapFactory;
import com.verisign.epp.codec.contact.EPPContactPendActionMsg;
import com.verisign.epp.codec.contact.EPPContactTransferResp;
import com.verisign.epp.codec.gen.EPPMsgQueue;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.framework.EPPPollDataRecord;
import com.verisign.epp.framework.EPPPollHandler;
import com.verisign.epp.framework.EPPPollQueueException;


/**
 * The &lt;ContactPollHandler&gt; implements EPPPollHandler for Contact name
 * mapping only. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 */
public class ContactPollHandler implements EPPPollHandler {
	/**
	 * Gets the kind of poll handler.  The kind value is 
	 * used for routing poll messaging to the appropriate 
	 * handler.  Client inserting poll messaging 
	 * should reference the correct value for the 
	 * message kind to ensure that there is a handler 
	 * that can handle the message.  
	 *
	 * @return EPPPollHandler kind which should be unique 
	 * per handler
	 */
	public String getKind() {
		return EPPContactMapFactory.NS;
	}

	/**
	 * Converts the internal poll data record to 
	 * a valid <code>EPPResponse</code>.  
	 *
	 * @param aRecord Poll data record containing 
	 * poll queue meta information like the number of 
	 * messages in the queue as well as the concrete 
	 * poll message to be converted to an <code>EPPResponse</code>.
	 *
	 * @return <code>EPPResponse</code> representing the 
	 * poll message.
	 *
	 * @throws EPPPollQueueException Error converting record to an 
	 * <code>EPPResponse</code>
	 */
	public EPPResponse toResponse(EPPPollDataRecord aRecord)
						   throws EPPPollQueueException {
		if (!aRecord.getKind().equals(getKind())) {
			throw new EPPPollQueueException("Handler for kind "
											+ aRecord.getKind()
											+ " does not match");
		}

		// Get the concrete response from the record
		EPPResponse theResponse = (EPPResponse) aRecord.getData();

		if (theResponse instanceof EPPContactTransferResp) {
			// Set the poll message generic information
			theResponse.setMsgQueue(new EPPMsgQueue(
													new Long(aRecord.getSize()),
													aRecord.getMsgId(),
													aRecord.getQDate(),
													"Transfer Requested."));
		}
		else if (theResponse instanceof EPPContactPendActionMsg) {
			theResponse.setMsgQueue(new EPPMsgQueue(
													new Long(aRecord.getSize()),
													aRecord.getMsgId(),
													aRecord.getQDate(),
													"Pending action completed successfully."));
		}
		else {
			throw new EPPPollQueueException("Unable to handle message class <"
											+ theResponse.getClass().getName());
		}

		theResponse.setResult(EPPResult.SUCCESS_POLL_MSG);

		return theResponse;
	}
}
