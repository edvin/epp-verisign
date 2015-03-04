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

import com.verisign.epp.codec.contact.EPPContactTransferResp;
import com.verisign.epp.codec.gen.EPPMsgQueue;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollResponse;
import com.verisign.epp.codec.rgppoll.EPPRgpPollResponse;
import com.verisign.epp.framework.EPPPollDataRecord;
import com.verisign.epp.framework.EPPPollHandler;
import com.verisign.epp.framework.EPPPollQueueException;

/**
 * NameStore poll handler for all NameStore poll messages. The kind associated
 * with the poll handler is <code>NSPollHandler</code>.
 */
public class NSPollHandler implements EPPPollHandler {

	/**
	 * Kind of message handled by <code>NSPollHandler</code>.
	 */
	public static final String KIND = "NSPollHandler";

	/**
	 * Gets the kind associated with NameStore poll messages
	 * 
	 * @return <code>NSPollHandler</code>
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

		if (theResponse instanceof com.verisign.epp.codec.domain.EPPDomainTransferResp) {
			// Transfer notification
			String theStatus = ((com.verisign.epp.codec.domain.EPPDomainTransferResp) theResponse)
					.getTransferStatus();
			String theDesc = "Undefined";

			if (theStatus
					.equals(com.verisign.epp.codec.domain.EPPDomainTransferResp.TRANSFER_PENDING)) {
				theDesc = "Transfer Requested.";
			} // Transfer approved?
			else if (theStatus
					.equals(com.verisign.epp.codec.domain.EPPDomainTransferResp.TRANSFER_CLIENT_APPROVED)) {
				theDesc = "Transfer Approved.";
			} // Transfer cancelled?
			else if (theStatus
					.equals(com.verisign.epp.codec.domain.EPPDomainTransferResp.TRANSFER_CLIENT_CANCELLED)) {
				theDesc = "Transfer Cancelled.";
			} // Transfer rejected?
			else if (theStatus
					.equals(com.verisign.epp.codec.domain.EPPDomainTransferResp.TRANSFER_CLIENT_REJECTED)) {
				theDesc = "Transfer Rejected.";
			} // Tranfer auto approved?
			else if (theStatus
					.equals(com.verisign.epp.codec.domain.EPPDomainTransferResp.TRANSFER_SERVER_APPROVED)) {
				theDesc = "Transfer Auto Approved.";
			} // Tranfer auto cancelled?
			else if (theStatus
					.equals(com.verisign.epp.codec.domain.EPPDomainTransferResp.TRANSFER_SERVER_CANCELLED)) {
				theDesc = "Transfer Auto Rejected.";
			}

			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(), theDesc));
		}
		else if (theResponse instanceof EPPContactTransferResp) {
			// Transfer notification
			String theStatus = ((EPPContactTransferResp) theResponse)
					.getTransferStatus();
			String theDesc = "Undefined";

			if (theStatus.equals(EPPContactTransferResp.TRANSFER_PENDING)) {
				theDesc = "Transfer Requested.";
			} // Transfer approved?
			else if (theStatus
					.equals(EPPContactTransferResp.TRANSFER_CLIENT_APPROVED)) {
				theDesc = "Transfer Approved.";
			} // Transfer cancelled?
			else if (theStatus
					.equals(EPPContactTransferResp.TRANSFER_CLIENT_CANCELLED)) {
				theDesc = "Transfer Cancelled.";
			} // Transfer rejected?
			else if (theStatus
					.equals(EPPContactTransferResp.TRANSFER_CLIENT_REJECTED)) {
				theDesc = "Transfer Rejected.";
			} // Tranfer auto approved?
			else if (theStatus
					.equals(EPPContactTransferResp.TRANSFER_SERVER_APPROVED)) {
				theDesc = "Transfer Auto Approved.";
			} // Tranfer auto cancelled?
			else if (theStatus
					.equals(EPPContactTransferResp.TRANSFER_SERVER_CANCELLED)) {
				theDesc = "Transfer Auto Rejected.";
			}

			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(), theDesc));
		}
		else if (theResponse instanceof com.verisign.epp.codec.domain.EPPDomainPendActionMsg) {
			// Domain pending action notification
			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(),
					"Pending action completed successfully."));
		}
		else if (theResponse instanceof EPPLowBalancePollResponse) {
			// low balance notification
			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(), "Low Account Balance (SRS)"));
		}
		else if (theResponse instanceof EPPRgpPollResponse) {
			// RGP restore pending notification
			theResponse.setMsgQueue(new EPPMsgQueue(
					new Long(aRecord.getSize()), aRecord.getMsgId(), aRecord
							.getQDate(), "Restore Request Pending"));
		}
		else {
			throw new EPPPollQueueException("Unable to handle message class <"
					+ theResponse.getClass().getName());
		}

		theResponse.setResult(EPPResult.SUCCESS_POLL_MSG);

		return theResponse;
	}
}
