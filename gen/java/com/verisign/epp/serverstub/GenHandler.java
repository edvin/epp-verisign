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

import java.util.Date;

import com.verisign.epp.codec.gen.*;
import com.verisign.epp.framework.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>GenHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPGenHandler</code> class. It
 * defines the Server's response to general EPP Commands.     <br><br>
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class GenHandler extends EPPGenHandler {
	/** Server transaction id */
	private static final String svrTransId = "54322-XYZ";

	/** Class logger */
	private static Logger cat =
		Logger.getLogger(
						 GenHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Construct an instance of GenHandler
	 */
	public GenHandler() {
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param aEvent DOCUMENT ME!
	 * @param aData DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected EPPEventResponse doLogin(EPPEvent aEvent, Object aData) {
		cat.debug("<<< Received Login >>>");

		SessionData sessionData = (SessionData) aData;

		EPPLoginCmd  theMessage = (EPPLoginCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		/**
		 * If they're already logged in then don't let them login again.  Send
		 * a command use error.
		 */
		if (sessionData.isLoggedIn()) {
			cat.error("Cleint already has a session established.");
			cat.error("Sending COMMAND USE ERROR for response...");

			EPPResult result = new EPPResult(EPPResult.COMMAND_USE_ERROR);
			result.addExtValueReason("login command received within the bounds of"
									 + " an existing session");

			return new EPPEventResponse(new EPPResponse(transId, result));
		}

		EPPResponse theResponse = new EPPResponse(transId);

		cat.debug("User has logged in. Setting session data login flag to true");
		sessionData.setLoggedIn(true);
		sessionData.setLoginCmd(theMessage);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Logout command is received.  A successful logout command
	 * ends the session
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPGenHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doLogout(EPPEvent aEvent, Object aData) {
		cat.debug("<<< Received Logout Command >>>");

		SessionData sessionData = (SessionData) aData;

		EPPCommand  theMessage = (EPPCommand) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPResponse theResponse		   = new EPPResponse(transId);
		EPPEventResponse returnEventResponse = null;

		if (sessionData.isLoggedIn()) {
			cat.debug("User is invoked in logout while logged in - Good."
					  + " Logging them out");

			/**
			 * Set the result to 1500 - Success - End Session
			 */
			theResponse.setResult(EPPResult.SUCCESS_END_SESSION);
		}
		else {
			/**
			 * The client isn't logged in so they can't successfully logout
			 * yet. Sending COMMAND_FAILED_END
			 */
			cat.debug("User is invoked in logout while NOT logged in!!!!."
					  + "Sending COMMAND FAILED error...");
			theResponse.setResult(EPPResult.COMMAND_FAILED_END);
		}

		returnEventResponse = new EPPEventResponse(theResponse);

		sessionData.setLogoutOccured(true);

		return returnEventResponse;
	}

	/**
	 * Invoked when an EPP Poll command is received.  Subclasses should define
	 * the behavior when a Poll command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPGenHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doPoll(EPPEvent aEvent, Object aData) {
		SessionData sessionData = (SessionData) aData;

		EPPPollCmd  theMessage = (EPPPollCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId	    transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);
		EPPResponse     theResponse = null;

		EPPPollQueueMgr theMgr = EPPPollQueueMgr.getInstance();

		// REQ
		if (theMessage.getOp().equals(EPPPollCmd.OP_REQ)) {
			cat.debug("doPoll: Poll request received");

			try {
				// The server stub does not use a recipient, thus null
				theResponse = theMgr.get(null, null);
				
				cat.debug("doPoll: Got poll message [" + theResponse + "]");	
			}
			 catch (EPPPollQueueException ex) {
			 	EPPResult theResult;
			 	
			 	// Empty queue?
			 	if (ex.getType() == EPPPollQueueException.TYPE_QUEUE_EMPTY) {
			 		cat.debug("doPoll: Queue is empty");
			 		theResult = new EPPResult(EPPResult.SUCCESS_POLL_NO_MSGS);
			 	}
			 	else {
			 		cat.error("doPoll: Unexpected queue exception: " + ex);
			 		theResult = new EPPResult(EPPResult.COMMAND_FAILED);
			 	}

				theResponse = new EPPResponse(transId, theResult);
			}
		}

		// ACK
		else if (theMessage.getOp().equals(EPPPollCmd.OP_ACK)) {
			String msgId = theMessage.getMsgID();
			cat.debug("doPoll: Poll ack received for message " + msgId);
			
			// Close session test?
			if (msgId.equals("CLOSE-SESSION-TEST")) {
				sessionData.setLogoutOccured(true);
				return null;
			}

			try {
				// The server stub does not use a recipient, thus null
				theResponse = theMgr.delete(null, msgId, null);
			}
			 catch (EPPPollQueueException ex) {
				cat.error("doPoll: Error deleting message " + msgId);

				EPPResult theResult =
					new EPPResult(EPPResult.OBJECT_DOES_NOT_EXIST);
				theResult.addExtValue(new EPPExtValue(
													  "Message could not be found",
													  "<epp:poll msgID=\""
													  + msgId
													  + "\" op=\"ack\"/>"));
				theResponse = new EPPResponse(transId, theResult);
			}
		}
		else {
			cat.error("doPoll: Invalid operation " + theMessage.getOp());

			EPPResult theResult = new EPPResult(EPPResult.COMMAND_SYNTAX_ERROR);
			theResult.addExtValueReason("op=" + theMessage.getOp());
			theResponse = new EPPResponse(transId, theResult);
		}

		theResponse.setTransId(transId);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when an EPP Hello command is received.  Subclasses should define
	 * the behavior when a Hello command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPGenHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doHello(EPPEvent aEvent, Object aData) {
		SessionData sessionData = (SessionData) aData;

		/**
		 * Get greeting set in the session by
		 * <code>ClientConnectionHandler</code> when initial connection was
		 * made.
		 */
		EPPGreeting greeting = sessionData.getGreeting();

		return new EPPEventResponse(greeting);
	}
}
