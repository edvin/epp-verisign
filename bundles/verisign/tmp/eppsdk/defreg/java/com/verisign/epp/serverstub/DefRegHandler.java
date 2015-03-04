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

import org.apache.log4j.Logger;

import com.verisign.epp.codec.defReg.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.idnext.EPPIdnLangTag;

// EPP imports
import com.verisign.epp.framework.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>DefRegHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPDefRegHandler</code> class. It
 * defines the Server's response to all received EPP DefReg Commands.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class DefRegHandler extends EPPDefRegHandler {
	/** DOCUMENT ME! */
	private static final String svrTransId = "54322-XYZ";

	/** DOCUMENT ME! */
	private static final String roid = "EXAMPLE1-REP";

	/** DOCUMENT ME! */
	private static Logger cat =
		Logger.getLogger(
				DefRegHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Constructs an instance of EPPDefRegHandler
	 */
	public DefRegHandler() {
	}

	/**
	 * Handles any common behavior that all EPPDefReg commands need to execute
	 * before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegHandler</code> This is assumed to be an
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
	 * Handles any common behavior that all EPPDefReg commands need to execute
	 * after they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}

	/**
	 * Invoked when a EPPDefReg Check command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegCheck(EPPEvent aEvent, Object aData) {
		EPPDefRegCheckCmd theMessage  = (EPPDefRegCheckCmd) aEvent.getMessage();
		EPPResponse		  theResponse;

		// This is just a vector  representing EPPDefReg Objects
		Vector	    vObjects = theMessage.getNames();
		Enumeration eObjects = vObjects.elements();

		Vector	    vResults = new Vector();

		// create a Vector of Ping Results.
		boolean available = true;

		while (eObjects.hasMoreElements()) {
			EPPDefRegName object     = (EPPDefRegName) eObjects.nextElement();
			String		  defRegName = null;
			String		  level		 = null;

			if (object != null) {
				defRegName     = object.getName();

				level = object.getLevel();
			}

			available = !available;

			EPPDefRegCheckResult checkResult =
				new EPPDefRegCheckResult(defRegName, level, available);
			vResults.addElement(checkResult);
		}
		 //while loop

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Response for one or more defReg names
		// construct the <code>EPPDefRegCheckResp</code> object for return.
		theResponse = new EPPDefRegCheckResp(transId, vResults);
		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPDefReg Renew command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegRenew(EPPEvent aEvent, Object aData) {
		EPPDefRegRenewCmd theMessage  = (EPPDefRegRenewCmd) aEvent.getMessage();
		EPPResponse		  theResponse;

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Test with transId, defReg name, roid, and expiration date
		theResponse =
			new EPPDefRegRenewResp(transId, theMessage.getRoid(), new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPDefReg Delete command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegDelete(EPPEvent aEvent, Object aData) {
		EPPDefRegDeleteCmd theMessage =
			(EPPDefRegDeleteCmd) aEvent.getMessage();

		// Test with transId, defReg name, and expiration date

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId     =
			new EPPTransId(theMessage.getTransId(), svrTransId);
		EPPResponse theResponse = new EPPResponse(transId);

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPDefReg Create command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegCreate(EPPEvent aEvent, Object aData) {
		EPPDefRegCreateCmd theMessage =
			(EPPDefRegCreateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		//Get the idn lang extension
        EPPIdnLangTag idnLang = (EPPIdnLangTag)
            theMessage.getExtension(EPPIdnLangTag.class);

		// print language code, if there's an idn Lang extension
        if (idnLang != null) {
        	String lang = idnLang.getLang();
            cat.info("Language Code: " + lang);
         }


		EPPResponse theResponse =
			new EPPDefRegCreateResp(
									transId, theMessage.getDefRegName(), roid,
									new Date(), new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a EPPDefReg Transfer command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegTransfer(EPPEvent aEvent, Object aData) {
		EPPDefRegTransferCmd theMessage =
			(EPPDefRegTransferCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId			  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPDefRegTransferResp theResponse =
			new EPPDefRegTransferResp(transId, roid);
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
	 * Invoked when a EPPDefReg Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegUpdate(EPPEvent aEvent, Object aData) {
		EPPDefRegUpdateCmd theMessage =
			(EPPDefRegUpdateCmd) aEvent.getMessage();

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
	 * Invoked when a EPPDefReg Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEPPDefRegdHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDefRegInfo(EPPEvent aEvent, Object aData) {
		EPPDefRegInfoCmd theMessage = (EPPDefRegInfoCmd) aEvent.getMessage();

		// EPPEPPDefRegInfo Response requires a vector of status
		Vector statuses = new Vector();
		statuses.addElement(new EPPDefRegStatus(EPPDefRegStatus.ELM_STATUS_OK));

		//statuses.addElement(new EPPDefRegStatus(EPPDefRegStatus.ELM_STATUS_CLIENT_HOLD));

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId  transId =
			new EPPTransId(theMessage.getTransId(), svrTransId);

		// Required EPPEPPDefRegInfoResp attributes.
		// trans id, roid,defReg name,forwardTo, client id, statuses, created by id, expiration date, Auth Info
		EPPDefRegInfoResp theResponse =
			new EPPDefRegInfoResp(
								  transId, new EPPDefRegName("premium", "do"),
								  roid, "jd1234", "XYZ-123", "US", new Date(),
								  "sh8013", statuses, "ClientY", "sh8013",
								  new Date(), new EPPAuthInfo("2fooBAR"));

		// Set the expiration date to today plus ten years
		GregorianCalendar theCal = new GregorianCalendar();
		theCal.setTime(new Date());
		theCal.add(Calendar.YEAR, 10);

		theResponse.setExpirationDate(theCal.getTime());

		theResponse.setResult(EPPResult.SUCCESS);

		return new EPPEventResponse(theResponse);
	}
}
