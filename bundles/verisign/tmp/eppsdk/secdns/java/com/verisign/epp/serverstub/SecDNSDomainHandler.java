/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainRenewCmd;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferCmd;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPService;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.framework.EPPDomainHandler;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>SecDNSDomainHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPDomainHandler</code> class. It
 * defines the Server's response to all received EPP Host Commands.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class SecDNSDomainHandler extends EPPDomainHandler {


	/** Used for the server transaction id */
	private static final String svrTransId = "54322-XYZ";

	/** Used for the domain roid */
	private static final String roid = "NS1EXAMPLE1-VRSN";

	/** Used for logging */
	private static Logger cat =
		Logger.getLogger(
						 SecDNSDomainHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());
	
	// Sub-handler for handling version 1.0 extension messages.
	SecDNSV10SubDomainHandler v10SubHandler = new SecDNSV10SubDomainHandler();
	
	// Sub-handler for handling version 1.1 extension messages.
	SecDNSV11SubDomainHandler v11SubHandler = new SecDNSV11SubDomainHandler();



	/**
	 * Handles any common behavior that all Domain commands need to execute
	 * before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPSecDNSDomainHandler</code> This is assumed to be an instance
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
	 * 		  <code>EPPSecDNSDomainHandler</code>
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
		EPPResponse theResponse;
		
		EPPDomainCreateCmd theMessage =
			(EPPDomainCreateCmd) aEvent.getMessage();
		
		EPPTransId theTransId = new EPPTransId(theMessage.getTransId(), this.svrTransId);

		// Both extension versions set?
		if (theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate.class) && 
			theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate.class)) {
			cat.error("SecDNSDomainHandler.doDomainCreate: both v10 and v11 of EPPSecDNSExtCreate passed.");
			
			EPPResult theResult = new EPPResult(EPPResult.PARAM_VALUE_POLICY_ERROR);
			theResult.addExtValueReason("Both v10 and v11 of secDNS extension passed");
			theResponse = new EPPResponse(null, theResult);
		} // v10 extension set?
		else if (theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate.class)) {
			theResponse = this.v10SubHandler.doDomainCreate(theMessage, aData); 
		} // v11 extension set?
		else if (theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate.class)) {
			theResponse = this.v11SubHandler.doDomainCreate(theMessage, aData); 			
		} // no extension set
		else {
			cat.info("SecDNSDomainHandler.doDomainCreate: no EPPSecDNSExtCreate extension");
			theResponse = new EPPDomainCreateResp(theTransId, theMessage.getName(), new Date());
		}
		
		// Set the transaction id (client and server)
		theResponse.setTransId(theTransId);

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

		EPPResponse theResponse;
		
		EPPDomainUpdateCmd theMessage =
			(EPPDomainUpdateCmd) aEvent.getMessage();
		
		EPPTransId theTransId = new EPPTransId(theMessage.getTransId(), this.svrTransId);

		// Both extension versions set?
		if (theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate.class) && 
			theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate.class)) {
			cat.error("SecDNSDomainHandler.doDomainUpdate: both v10 and v11 of EPPSecDNSExtUpdate passed.");
			
			EPPResult theResult = new EPPResult(EPPResult.PARAM_VALUE_POLICY_ERROR);
			theResult.addExtValueReason("Both v10 and v11 of secDNS extension passed");
			theResponse = new EPPResponse(null, theResult);
		} // v10 extension set?
		else if (theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate.class)) {
			theResponse = this.v10SubHandler.doDomainUpdate(theMessage, aData); 
		} // v11 extension set?
		else if (theMessage.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate.class)) {
			theResponse = this.v11SubHandler.doDomainUpdate(theMessage, aData); 			
		} // no extension set
		else {
			cat.info("SecDNSDomainHandler.doDomainUpdate: no EPPSecDNSExtUpdate extension");
			theResponse = new EPPResponse();
		}
		
		// Set the transaction id (client and server)
		theResponse.setTransId(theTransId);

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
	
		EPPDomainInfoCmd theMessage =
			(EPPDomainInfoCmd) aEvent.getMessage();
		
		EPPTransId theTransId = new EPPTransId(theMessage.getTransId(), this.svrTransId);
		
		// EPPDomainInfo Response requires a vector of status
		Vector theStatuses = new Vector();
		theStatuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));
		theStatuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_CLIENT_HOLD));


		// Required EPPDomainInfoResp attributes.
		// trans id, domain name, roid, client id, statuses, created by id, expiration date, Auth Info
		EPPDomainInfoResp theResponse =
			new EPPDomainInfoResp(
								  null, "NS1EXAMPLE1-VRSN", theMessage.getName(), "ClientX",
								  theStatuses, "ClientY", new Date(),
								  new EPPAuthInfo("2fooBAR"));

		// Add Name Servers
		Vector theNses = new Vector();
		theNses.addElement("ns1." + theMessage.getName());
		theNses.addElement("ns2." + theMessage.getName());
		theResponse.setNses(theNses);

		// Add Subordinate Hosts
		Vector theHosts = new Vector();
		theHosts.addElement("ns1." + theMessage.getName());
		theHosts.addElement("ns2." + theMessage.getName());
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
		
		// Set the transaction id (client and server)
		theResponse.setTransId(theTransId);
		
		// Determine what version of the secDNS extension is supported
		SessionData theSessionData = (SessionData) aData;
		boolean hasV10ExtService = false;
		boolean hasV11ExtService = false;
		Enumeration extSvcEnum = theSessionData.getLoginCmd().getExtensionServices().elements();
		while (extSvcEnum.hasMoreElements()) {
			EPPService theExtService = (EPPService) extSvcEnum.nextElement();
			
			if (theExtService.getNamespaceURI().equals(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtFactory.NS)) {
				hasV10ExtService = true;
			}
			else if (theExtService.getNamespaceURI().equals(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtFactory.NS)) {
				hasV11ExtService = true;
			}
		}
		
		// Version 1.1 specified?  This applies if both version 1.0 and 1.1 was specified.
		if (hasV11ExtService) {
			theResponse = this.v11SubHandler.doDomainInfo(theMessage, theResponse, aData); 			
		} // Version 1.0 specified only?
		else if (hasV10ExtService) {
			theResponse = this.v10SubHandler.doDomainInfo(theMessage, theResponse, aData); 						
		} // No secDNS extension URI specified in login
		else { 
			cat.info("SecDNSDomainHandler.doDomainInfo: no secDNS extension URI specified in login");
		}
		
		return new EPPEventResponse(theResponse);
	}
}
