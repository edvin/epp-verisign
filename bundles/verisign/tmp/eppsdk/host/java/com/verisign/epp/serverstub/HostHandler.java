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

import java.util.Date;
import java.util.Enumeration;

// Java imports
import java.util.Vector;

import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.host.*;

// EPP Imports
import com.verisign.epp.framework.*;


/**
 * The <code>HostHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPHostHandler</code> class. It
 * defines the Server's response to all received EPP Host Commands. <br><br>
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class HostHandler extends EPPHostHandler {
	/** DOCUMENT ME! */
	private static final String svrTransId = "54322-XYZ";

	/** DOCUMENT ME! */
	private static final String roid = "NS1EXAMPLE1-VRSN";

	/**
	 * Create an instance of <code>HostHandler</code>
	 */
	public HostHandler() {
	}

	/**
	 * Handles any common behavior that all Host commands need to execute
	 * before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code> This is assumed to be an instance
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
	 * Handles any common behavior that all Host commands need to execute after
	 * they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
		//        System.out.println("PostHandleEvent of the Host Handler");
		//        EPPMessage theMessage = aEvent.getMessage();
		//        System.out.println(theMessage.toString());
	}

	/**
	 * Invoked when a Host Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doHostUpdate(EPPEvent aEvent, Object aData) {
		EPPHostUpdateCmd theMessage = (EPPHostUpdateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Create the Response (Standard EPPResponse)
		EPPResponse theResponse = new EPPResponse(transId);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Host Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doHostInfo(EPPEvent aEvent, Object aData) {
		EPPHostInfoCmd theMessage = (EPPHostInfoCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPHostInfoResp theResponse = new EPPHostInfoResp();

		theResponse.setTransId(transId);
		theResponse.setName(theMessage.getName());
		theResponse.setRoid(roid);

		theResponse.setCreatedBy("createdByLloyd");
		theResponse.setCreatedDate(new Date());
		theResponse.setClientId("clientidX");

		Vector statuses = new Vector();
		statuses.addElement(new EPPHostStatus(EPPHostStatus.ELM_STATUS_LINKED));
		statuses.addElement(new EPPHostStatus(EPPHostStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED));
		theResponse.setStatuses(statuses);

		Vector addresses = new Vector();
		addresses.addElement(new EPPHostAddress("192.1.2.3"));
		theResponse.setAddresses(addresses);

		theResponse.setLastUpdatedBy("updatedX");
		theResponse.setLastUpdatedDate(new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Host Delete command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doHostDelete(EPPEvent aEvent, Object aData) {
		EPPHostDeleteCmd theMessage = (EPPHostDeleteCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Create the Response (Standard EPPResponse)
		EPPResponse theResponse = new EPPResponse(transId);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Host Check command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doHostCheck(EPPEvent aEvent, Object aData) {
		EPPHostCheckCmd theMessage = (EPPHostCheckCmd) aEvent.getMessage();

		Vector		    hosts    = theMessage.getNames();
		Enumeration     eHosts   = hosts.elements();
		Vector		    vResults = new Vector();

		boolean		    available = true;

		while (eHosts.hasMoreElements()) {
			String hostName = (String) eHosts.nextElement();
			available = !available;
			vResults.addElement(new EPPHostCheckResult(hostName, available));
		}

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPHostCheckResp theResponse = new EPPHostCheckResp(transId, vResults);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Host Create command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doHostCreate(EPPEvent aEvent, Object aData) {
		EPPHostCreateCmd theMessage = (EPPHostCreateCmd) aEvent.getMessage();

		// Create the Response (Standard EPPResponse)

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId		  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);
		EPPHostCreateResp theResponse =
			new EPPHostCreateResp(transId, theMessage.getName(), new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}
}
