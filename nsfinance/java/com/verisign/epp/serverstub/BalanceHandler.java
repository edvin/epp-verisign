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
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.balance.EPPBalanceInfoCmd;
import com.verisign.epp.codec.balance.EPPBalanceInfoResp;
import com.verisign.epp.codec.balance.EPPCreditThreshold;
import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainContact;
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
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollMapFactory;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollResponse;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollThreshold;
import com.verisign.epp.framework.EPPBalanceHandler;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>BalanceHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPBalanceHandler</code> class. It
 * defines the Server's response to all received EPP Balance Commands.
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class BalanceHandler extends EPPBalanceHandler {


	/** Default server transaction id */
	private static final String svrTransId = "54322-XYZ";


	/** DOCUMENT ME! */
	private static Logger cat =
		Logger.getLogger(
						 BalanceHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Constructs an instance of BalanceHandler
	 */
	public BalanceHandler() {
        super();
     }

	/**
	 * Handles any common behavior that all Balance commands need to execute
	 * before they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPBalanceHandler</code> This is assumed to be an instance
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
	 * Handles any common behavior that all Balance commands need to execute
	 * after they execute their command specific behavior.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPBalanceHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}



	/**
	 * Invoked when a Balance Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doBalanceInfo(EPPEvent aEvent, Object aData) {

		EPPBalanceInfoCmd theMessage =
			(EPPBalanceInfoCmd) aEvent.getMessage();

		EPPBalanceInfoResp theResponse = new EPPBalanceInfoResp();
		
		theResponse.setTransId(new EPPTransId(theMessage.getTransId(), svrTransId));	
		theResponse.setCreditLimit(new BigDecimal("1000.00"));
		theResponse.setBalance(new BigDecimal("200.00"));
		theResponse.setAvailableCredit(new BigDecimal("800.00"));
		theResponse.setCreditThreshold(new EPPCreditThreshold(EPPCreditThreshold.FIXED, new BigDecimal("500.00")));

		return new EPPEventResponse(theResponse);
	}

}
