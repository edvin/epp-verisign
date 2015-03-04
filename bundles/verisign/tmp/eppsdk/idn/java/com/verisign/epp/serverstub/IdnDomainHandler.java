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


// Logging Imports
import org.apache.log4j.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

// Java imports
import java.util.Vector;

import com.verisign.epp.codec.domain.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.idnext.EPPIdnLangTag;

// EPP imports
import com.verisign.epp.framework.*;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>IdnDomainHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPDomainHandler</code> class. It
 * defines the Server's response to all received EPP Host Commands.
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class IdnDomainHandler extends EPPDomainHandler {


	/** DOCUMENT ME! */
	private static final String svrTransId = "54322-XYZ";

	/** DOCUMENT ME! */
	private static final String roid = "NS1EXAMPLE1-VRSN";

	/** DOCUMENT ME! */
	private static Logger cat =
		Logger.getLogger(
						 IdnDomainHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Constructs an instance of IdnDomainHandler
	 */
	public IdnDomainHandler() {}

	/**
	 * Handles any common behavior that all Domain commands need to execute
	 * before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPIdnDomainHandler</code> This is assumed to be an instance
	 * 		  of SessionData here.
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
	 * Handles any common behavior that all Domain commands need to execute
	 * after they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPIdnDomainHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}

	/**
	 * Invoked when a Domain Check command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainCheck(EPPEvent aEvent, Object aData) {
		EPPDomainCheckCmd theMessage  = (EPPDomainCheckCmd) aEvent.getMessage();
		EPPResponse		  theResponse;

		// This is just a vector of strings representing Domain Names
		Vector	    vNames = theMessage.getNames();
		Enumeration eNames = vNames.elements();

		Vector	    vResults = new Vector();

		// create a Vector of Ping Results.
		boolean known = true;

		while (eNames.hasMoreElements()) {
			String domainName = (String) eNames.nextElement();
			known = !known;
			vResults.addElement(new EPPDomainCheckResult(domainName, known));
		}

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Response for many domain names
		theResponse = new EPPDomainCheckResp(transId, vResults);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Renew command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainRenew(EPPEvent aEvent, Object aData) {
		EPPDomainRenewCmd theMessage  = (EPPDomainRenewCmd) aEvent.getMessage();
		EPPResponse		  theResponse;

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Test with transId, domain name, roid, and expiration date
		theResponse =
			new EPPDomainRenewResp(transId, theMessage.getName(), new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Delete command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainDelete(EPPEvent aEvent, Object aData) {
		EPPDomainDeleteCmd theMessage =
			(EPPDomainDeleteCmd) aEvent.getMessage();

		// Test with transId, domain name, and expiration date

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPResponse theResponse =
			new EPPDomainCreateResp(transId, theMessage.getName(), new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Create command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainCreate(EPPEvent aEvent, Object aData) {
		EPPDomainCreateCmd theMessage =
			(EPPDomainCreateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId     =
			new EPPTransId(theMessage.getTransId(), svrTransId);
		EPPResponse theResponse =
			new EPPDomainCreateResp(transId, theMessage.getName(), new Date());
		
		
		
		//Get the idn lang extension
        EPPIdnLangTag idnLang = (EPPIdnLangTag)
            theMessage.getExtension(EPPIdnLangTag.class);

		// print language code, if there's an idn Lang extension
        if (idnLang != null) {

            String lang = idnLang.getLang();
            
            cat.info("Language Code: " + lang);
         }
		
		

		if (theMessage.getName().equals("pending.com")) {
			EPPTransId			   pendingTransId =
				new EPPTransId(theMessage.getTransId(), svrTransId);
			EPPDomainPendActionMsg thePollMsg =
				new EPPDomainPendActionMsg(
										   transId, "pending.com", true,
										   pendingTransId, new Date());

			try {
				EPPPollQueueMgr.getInstance().put(
												  null, EPPDomainMapFactory.NS,
												  thePollMsg, null);
			}
			 catch (EPPPollQueueException ex) {
				cat.error("doDomainCreate: Error putting message ["
						  + thePollMsg + "]");

				EPPResult theResult = new EPPResult(EPPResult.COMMAND_FAILED);
				theResponse = new EPPResponse(transId, theResult);

				return new EPPEventResponse(theResponse);
			}

			theResponse.setResult(EPPResult.SUCCESS_PENDING);
		}
		else {
			theResponse.setResult(EPPResult.SUCCESS);
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Transfer command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainTransfer(EPPEvent aEvent, Object aData) {
		EPPDomainTransferCmd theMessage =
			(EPPDomainTransferCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Is a transfer request?
		if (theMessage.getOp().equals(EPPCommand.OP_REQUEST)) {
			// Insert transfer response in poll queue
			EPPDomainTransferResp thePollMsg = new EPPDomainTransferResp();

			thePollMsg.setName("example");
			thePollMsg.setTransferStatus(EPPResponse.TRANSFER_PENDING);
			thePollMsg.setRequestClient("ClientX");
			thePollMsg.setRequestDate(new Date());
			thePollMsg.setActionClient("ClientY");
			thePollMsg.setActionDate(new Date());
			thePollMsg.setExpirationDate(new Date());
			thePollMsg.setResult(EPPResult.SUCCESS);

			try {
				EPPPollQueueMgr.getInstance().put(
												  null, EPPDomainMapFactory.NS,
												  thePollMsg, null);
			}
			 catch (EPPPollQueueException ex) {
				cat.error("doDomainTransfer: Error putting message ["
						  + thePollMsg + "]");

				EPPResult   theResult   =
					new EPPResult(EPPResult.COMMAND_FAILED);
				EPPResponse theResponse = new EPPResponse(transId, theResult);

				return new EPPEventResponse(theResponse);
			}
		}

		EPPDomainTransferResp theResponse =
			new EPPDomainTransferResp(transId, theMessage.getName());
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
	 * Invoked when a Domain Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainUpdate(EPPEvent aEvent, Object aData) {

        EPPDomainUpdateCmd theMessage =
			(EPPDomainUpdateCmd) aEvent.getMessage();

        EPPResponse theResponse = null;

       /**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		theResponse = new EPPResponse(transId);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainInfo(EPPEvent aEvent, Object aData) {
		EPPDomainInfoCmd theMessage = (EPPDomainInfoCmd) aEvent.getMessage();

		// EPPDomainInfo Response requires a vector of status
		Vector statuses = new Vector();
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_CLIENT_HOLD));

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Required EPPDomainInfoResp attributes.
		// trans id, domain name, roid, client id, statuses, created by id, expiration date, Auth Info
		EPPDomainInfoResp theResponse =
			new EPPDomainInfoResp(
								  transId, roid, "example.com", "ClientX",
								  statuses, "ClientY", new Date(),
								  new EPPAuthInfo("2fooBAR"));

		// Add Name Servers
		Vector theNses = new Vector();
		theNses.addElement("ns1.example.com");
		theNses.addElement("ns2.example.com");
		theResponse.setNses(theNses);

		// Add Subordinate Hosts
		Vector theHosts = new Vector();
		theHosts.addElement("ns1.example.com");
		theHosts.addElement("ns2.example.com");
		theResponse.setHosts(theHosts);

		// Add Contacts
		Vector theContacts = new Vector();
		theContacts.addElement(new EPPDomainContact(
													"sh8013",
													EPPDomainContact.TYPE_ADMINISTRATIVE));
		theContacts.addElement(new EPPDomainContact(
													"sh8013",
													EPPDomainContact.TYPE_BILLING));
		theContacts.addElement(new EPPDomainContact(
													"sh8013",
													EPPDomainContact.TYPE_TECHNICAL));
		theResponse.setContacts(theContacts);

		// Set the expiration date to today plus one year
		GregorianCalendar theCal = new GregorianCalendar();
		theCal.setTime(new Date());
		theCal.add(Calendar.YEAR, 1);

		theResponse.setExpirationDate(theCal.getTime());

		// Set the Registrant
		theResponse.setRegistrant("jd1234");

		theResponse.setResult(EPPResult.SUCCESS);
		return new EPPEventResponse(theResponse);
	}
}
