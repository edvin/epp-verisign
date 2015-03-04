/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.serverstub;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

// Java imports
import java.util.Vector;

import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.nameWatch.*;

// EPP imports
import com.verisign.epp.framework.*;


/**
 * The <code>NameWatchHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPNameWatchHandler</code> class.
 * It defines the Server's response to all received EPP NameWatch Commands.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class NameWatchHandler extends EPPNameWatchHandler {
	/** DOCUMENT ME! */
	private static final String svrTransId = "54322-XYZ";

	/** DOCUMENT ME! */
	private static final String roid = "EXAMPLE1-REP";

	/**
	 * Constructs an instance of NameWatchHandler
	 */
	public NameWatchHandler() {
	}

	/**
	 * Handles any common behavior that all NameWatch commands need to execute
	 * before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchHandler</code> This is assumed to be an
	 * 		  instance of SessionData here.
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
						   throws EPPHandleEventException {
		SessionData sessionData = (SessionData) aData;
		EPPCommand  theMessage = (EPPCommand) aEvent.getMessage();

		if (!sessionData.isLoggedIn()) {
			/**
			 * The client isn't logged in so they can't successfully invoke a
			 * command. Sending COMMAND_FAILED_END
			 */
			/**
			 * Create the transId for the response with the client trans id and
			 * the server trans id.
			 */
			EPPTransId transId =
				new EPPTransId(theMessage.getTransId(), svrTransId);

			// Create the Response (Standard EPPResponse)
			EPPResponse theResponse = new EPPResponse(transId);

			theResponse.setResult(EPPResult.COMMAND_FAILED_END);
			throw new EPPHandleEventException(
											  "The client has not established a session",
											  theResponse);
		}
	}

	/**
	 * Handles any common behavior that all NameWatch commands need to execute
	 * after they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}

	/**
	 * Invoked when a NameWatch Renew command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doNameWatchRenew(EPPEvent aEvent, Object aData) {
		EPPNameWatchRenewCmd theMessage =
			(EPPNameWatchRenewCmd) aEvent.getMessage();
		EPPResponse			 theResponse;

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Test with transId, namewatch roid, and expiration date
		Calendar exDate = Calendar.getInstance();
		exDate.setTime(theMessage.getCurExpDate());
		exDate.add(Calendar.YEAR, theMessage.getPeriod().getPeriod());

		theResponse =
			new EPPNameWatchRenewResp(
									  transId, theMessage.getRoid(),
									  exDate.getTime());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a NameWatch Delete command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doNameWatchDelete(EPPEvent aEvent, Object aData) {
		EPPNameWatchDeleteCmd theMessage =
			(EPPNameWatchDeleteCmd) aEvent.getMessage();

		// Test with transId, namewatch name, and expiration date

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);
		EPPResponse theResponse = new EPPResponse(transId);

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a NameWatch Create command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doNameWatchCreate(EPPEvent aEvent, Object aData) {
		EPPNameWatchCreateCmd theMessage =
			(EPPNameWatchCreateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPResponse theResponse = null;

		if (theMessage.getPeriod() == null) {
			theResponse =
				new EPPNameWatchCreateResp(
										   transId, theMessage.getName(), roid,
										   new GregorianCalendar(1999, 4, 3)
										   .getTime());
		}
		else {
			theResponse =
				new EPPNameWatchCreateResp(
										   transId, theMessage.getName(), roid,
										   new GregorianCalendar(1999, 4, 3)
										   .getTime(),
										   new GregorianCalendar(
																 1999
																 + theMessage.getPeriod()
																			 .getPeriod(),
																 4, 3).getTime());
		}

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a NameWatch Transfer command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doNameWatchTransfer(
												   EPPEvent aEvent, Object aData) {
		EPPNameWatchTransferCmd theMessage =
			(EPPNameWatchTransferCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPNameWatchTransferResp theResponse =
			new EPPNameWatchTransferResp(transId, theMessage.getRoid());

		theResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);

		Calendar reDate = Calendar.getInstance();
		reDate.setTime(new GregorianCalendar(2000, 6, 8).getTime());

		theResponse.setRequestClient("ClientX");
		theResponse.setRequestDate(reDate.getTime());

		theResponse.setActionClient("ClientY");

		Calendar acDate = Calendar.getInstance();
		acDate.setTime(reDate.getTime());
		acDate.add(Calendar.DATE, 5);
		theResponse.setActionDate(acDate.getTime());

		/** The Expiration date is optional */
		Calendar exDate = Calendar.getInstance();
		exDate.setTime(reDate.getTime());

		if (theMessage.getPeriod() != null) {
			exDate.add(Calendar.YEAR, theMessage.getPeriod().getPeriod());
		}
		else {
			exDate.add(Calendar.YEAR, 1);
		}

		theResponse.setExpirationDate(exDate.getTime());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a NameWatch Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doNameWatchUpdate(EPPEvent aEvent, Object aData) {
		EPPNameWatchUpdateCmd theMessage =
			(EPPNameWatchUpdateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Create Update Response (Standard EPPResponse)
		EPPResponse theResponse = new EPPResponse(transId);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a NameWatch Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPNameWatchdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doNameWatchInfo(EPPEvent aEvent, Object aData) {
		EPPNameWatchInfoCmd theMessage =
			(EPPNameWatchInfoCmd) aEvent.getMessage();

		// EPPNameWatchInfo Response requires a vector of status
		Vector statuses = new Vector();
		statuses.addElement(new EPPNameWatchStatus(EPPNameWatchStatus.ELM_STATUS_OK));
		statuses.addElement(new EPPNameWatchStatus(EPPNameWatchStatus.ELM_STATUS_CLIENT_HOLD));

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Set the expiration date to today plus one year
		GregorianCalendar theCal = new GregorianCalendar();
		theCal.setTime(new Date());
		theCal.add(theCal.YEAR, 1);

		// Required EPPNameWatchInfoResp attributes.
		EPPResponse theResponse =
			new EPPNameWatchInfoResp(
									 transId, "doe", theMessage.getRoid(),
									 "jd1234",
									 new EPPNameWatchRptTo(
														   "weekly",
														   "jdoe@example.com"),
									 statuses, "ClientX", "ClientY",
									 new GregorianCalendar(1999, 4, 3).getTime(),
									 theCal.getTime());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}
}
