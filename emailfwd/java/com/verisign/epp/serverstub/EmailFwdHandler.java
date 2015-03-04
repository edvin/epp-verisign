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


// EPP imports
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResult;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdContact;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdDeleteCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdStatus;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.framework.EPPEmailFwdHandler;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;


/**
 * The <code>EmailFwdHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPEPPEmailFwdHandler</code>
 * class. It defines the Server's response to all received EPP EmailFwd
 * Commands.
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class EmailFwdHandler extends EPPEmailFwdHandler {
	/** DOCUMENT ME! */
	private static final String svrTransId = "54322-XYZ";

	/** DOCUMENT ME! */
	private static final String roid = "NS1EXAMPLE1-VRSN";

	/**
	 * Constructs an instance of EPPEmailFwdHandler
	 */
	public EmailFwdHandler() {
	}

	/**
	 * Handles any common behavior that all EPPEmailFwd commands need to
	 * execute before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwdHandler</code> This is assumed to be an
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
	 * Handles any common behavior that all EPPEmailFwd commands need to
	 * execute after they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwdHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}

	/**
	 * Invoked when a EPPEmailFwd Check command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdCheck(EPPEvent aEvent, Object aData) {
		EPPEmailFwdCheckCmd theMessage  =
			(EPPEmailFwdCheckCmd) aEvent.getMessage();
		EPPResponse		    theResponse;

		// This is just a vector of strings representing EPPEmailFwd Names
		Vector	    vNames = theMessage.getNames();
		Enumeration eNames = vNames.elements();

		Vector	    vResults = new Vector();

		// create a Vector of Ping Results.
		boolean available = true;

		while (eNames.hasMoreElements()) {
			String emailFwdName = (String) eNames.nextElement();
			available = !available;
			vResults.addElement(new EPPEmailFwdCheckResult(emailFwdName, available));
		}

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Response for many emailFwd names
		theResponse = new EPPEmailFwdCheckResp(transId, vResults);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPEmailFwd Renew command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdRenew(EPPEvent aEvent, Object aData) {
		EPPEmailFwdRenewCmd theMessage  =
			(EPPEmailFwdRenewCmd) aEvent.getMessage();
		EPPResponse		    theResponse;

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Test with transId, emailFwd name, roid, and expiration date
		theResponse =
			new EPPEmailFwdRenewResp(transId, theMessage.getName(), new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPEmailFwd Delete command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdDelete(EPPEvent aEvent, Object aData) {
		EPPEmailFwdDeleteCmd theMessage =
			(EPPEmailFwdDeleteCmd) aEvent.getMessage();

		// Test with transId, emailFwd name, and expiration date

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPResponse theResponse =
			new EPPEmailFwdCreateResp(
									  transId, theMessage.getName(), new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPEmailFwd Create command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdCreate(EPPEvent aEvent, Object aData) {
		EPPEmailFwdCreateCmd theMessage =
			(EPPEmailFwdCreateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPResponse theResponse =
			new EPPEmailFwdCreateResp(
									  transId, theMessage.getName(), new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPEmailFwd Transfer command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdTransfer(
												  EPPEvent aEvent, Object aData) {
		EPPEmailFwdTransferCmd theMessage =
			(EPPEmailFwdTransferCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId			    transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPEmailFwdTransferResp theResponse =
			new EPPEmailFwdTransferResp(transId, theMessage.getName());
		theResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);

		theResponse.setRequestClient("ClientX");
		theResponse.setRequestDate(new Date());

		theResponse.setActionClient("ClientY");
		theResponse.setActionDate(new Date());

		/**
		 * The Expiration date is optional
		 */
		theResponse.setExpirationDate(new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPEmailFwd Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdUpdate(EPPEvent aEvent, Object aData) {
		EPPEmailFwdUpdateCmd theMessage =
			(EPPEmailFwdUpdateCmd) aEvent.getMessage();

		// Create Update Response (Standard EPPResponse)

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
	 * Invoked when a EPPEmailFwd Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdInfo(EPPEvent aEvent, Object aData) {
		EPPEmailFwdInfoCmd theMessage =
			(EPPEmailFwdInfoCmd) aEvent.getMessage();

		// EPPEPPEmailFwdInfo Response requires a vector of status
		Vector statuses = new Vector();
		statuses.addElement(new EPPEmailFwdStatus(EPPEmailFwdStatus.ELM_STATUS_OK));
		statuses.addElement(new EPPEmailFwdStatus(EPPEmailFwdStatus.ELM_STATUS_CLIENT_HOLD));

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Required EPPEPPEmailFwdInfoResp attributes.
		// trans id, roid,emailFwd name,forwardTo, client id, statuses, created by id, expiration date, Auth Info
		EPPEmailFwdInfoResp theResponse =
			new EPPEmailFwdInfoResp(
									transId, roid, theMessage.getName(),
									"jdoe@example.com", "ClientX", statuses,
									"ClientY", new Date(),
									new EPPAuthInfo("2fooBAR"));

		// Add Contacts
		Vector theContacts = new Vector();
		theContacts.addElement(new EPPEmailFwdContact(
													  "sh8013",
													  EPPEmailFwdContact.TYPE_ADMINISTRATIVE));
		theContacts.addElement(new EPPEmailFwdContact(
													  "sh8013",
													  EPPEmailFwdContact.TYPE_BILLING));
		theContacts.addElement(new EPPEmailFwdContact(
													  "sh8013",
													  EPPEmailFwdContact.TYPE_TECHNICAL));
		theResponse.setContacts(theContacts);

		// Set the expiration date to today plus one year
		GregorianCalendar theCal = new GregorianCalendar();
		theCal.setTime(new Date());
		theCal.add(theCal.YEAR, 1);

		theResponse.setExpirationDate(theCal.getTime());

		// Set the Registrant
		theResponse.setRegistrant("jd1234");

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}
}
