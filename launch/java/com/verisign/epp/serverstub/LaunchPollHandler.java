/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

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
 ***********************************************************/
package com.verisign.epp.serverstub;

import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainPendActionMsg;
import com.verisign.epp.codec.gen.EPPMsgQueue;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.launch.EPPLaunchInfData;
import com.verisign.epp.framework.EPPPollDataRecord;
import com.verisign.epp.framework.EPPPollHandler;
import com.verisign.epp.framework.EPPPollQueueException;

/**
 * Poll handler for all launch poll messages. The kind associated with the poll
 * handler is <code>LaunchPollHandler.KIND</code>.
 */
public class LaunchPollHandler implements EPPPollHandler {

	/**
	 * Kind of message handled by <code>LaunchPollHandler</code>.
	 */
	public static final String KIND = "LaunchPollHandler";

	/**
	 * Gets the kind associated with NameStore poll messages
	 * 
	 * @return <code>LaunchPollHandler</code>
	 */
	public String getKind() {
		return KIND;
	}

	/**
	 * Convert an <code>EPPPollDataRecord</code> to an <code>EPPResponse</code>
	 * 
	 * @param aRecord
	 *            Poll record to convert
	 * 
	 * @return EPP poll response
	 * 
	 * @throws EPPPollQueueException
	 *             Error processing record
	 */
	public EPPResponse toResponse(EPPPollDataRecord aRecord)
			throws EPPPollQueueException {
		if (!aRecord.getKind().equals(getKind())) {
			throw new EPPPollQueueException("Handler for kind "
					+ aRecord.getKind() + " does not match");
		}

		// Get the concrete response from the record
		EPPResponse theResponse = (EPPResponse) aRecord.getData();

		if (theResponse instanceof EPPDomainInfoResp) {
			String theDesc = "Undefined";
			
			if (theResponse.hasExtension(EPPLaunchInfData.class)) {

				EPPLaunchInfData launchExt = (EPPLaunchInfData) theResponse
						.getExtension(EPPLaunchInfData.class);

				theDesc = "Application " + launchExt.getStatus().getStatus();
			}		
			
			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(), theDesc));
		}
		else if (theResponse instanceof EPPDomainPendActionMsg) {
			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(), "Pending action completed successfully."));
		}
		else {
			throw new EPPPollQueueException("Unable to handle message class <"
					+ theResponse.getClass().getName());
		}

		theResponse.setResult(EPPResult.SUCCESS_POLL_MSG);

		return theResponse;
	}
}
