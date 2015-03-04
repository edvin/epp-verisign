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

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.serverstub;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainTransferCmd;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollMapFactory;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollResponse;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollThreshold;
import com.verisign.epp.codec.persreg.EPPPersRegCreateData;
import com.verisign.epp.codec.persreg.EPPPersRegCreateErrData;
import com.verisign.epp.codec.persreg.EPPPersRegInfoData;
import com.verisign.epp.codec.persreg.EPPPersRegRenewData;
import com.verisign.epp.codec.persreg.EPPPersRegTransferData;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>PersRegDomainHandler</code> class extends
 * <code>DomainHandler</code> to include responding with Personal Registration
 * Extension attributes.   Specifically, the consent identifier and the
 * bundled rate flag are  returned. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class PersRegDomainHandler extends DomainHandler {
	

	private static Logger cat =
		Logger.getLogger(
				 PersRegDomainHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());
	/**
	 * Constructs an instance of PersRegDomainHandler
	 */
	public PersRegDomainHandler() {
	}

	/**
	 * Add the bunded rate flag attribute to the Domain Renew Response. The
	 * flag is set to <code>true</code>.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainRenew(EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doDomainRenew(aEvent, aData);

		((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegRenewData(true));

		return theResponse;
	}

	/**
	 * Add the bunded rate flag attribute to the Domain Create Response. The
	 * flag is set to <code>true</code>.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainCreate(EPPEvent aEvent, Object aData) {
		EPPEventResponse   theResponse = super.doDomainCreate(aEvent, aData);

		EPPDomainCreateCmd theMessage =
			(EPPDomainCreateCmd) aEvent.getMessage();

		// Simulate an error?
		if (theMessage.getName().indexOf("error") != -1) {
			((EPPResponse) theResponse.getResponse()).setResult(EPPResult.ASSOC_PROHIBITS_OP);
			((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegCreateErrData(EPPPersRegCreateErrData.ERROR_DEFREG_EXISTS));
		}else if (theMessage.getName().equalsIgnoreCase("test.com")) {
			// create poll response

			EPPLowBalancePollResponse thePollMsg =
				new EPPLowBalancePollResponse();

			thePollMsg.setRegistrarName("Test Registar");
			thePollMsg.setCreditLimit("1000");
			thePollMsg.setCreditThreshold(new EPPLowBalancePollThreshold(EPPLowBalancePollThreshold.PERCENT, "10"));
			thePollMsg.setAvailableCredit("80");

			try {
				EPPPollQueueMgr.getInstance().put(
					null, EPPLowBalancePollMapFactory.NS,
					thePollMsg, null);
			}catch (EPPPollQueueException ex) {
				cat.error("doDomainCreate: Error putting message [" + thePollMsg + "]");
				EPPResult theResult = new EPPResult(EPPResult.COMMAND_FAILED);
				((EPPResponse) theResponse.getResponse()).setResult(theResult);
			}

		}else {
			((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegCreateData(true));
		}

		return theResponse;
	}

	/**
	 * Add the bunded rate flag attribute to the Domain Transfer Request. The
	 * flag is set to <code>true</code>.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainTransfer(EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doDomainTransfer(aEvent, aData);

		EPPDomainTransferCmd theMessage =
			(EPPDomainTransferCmd) aEvent.getMessage();

		// Is a transfer request?
		if (theMessage.getOp().equals(EPPCommand.OP_REQUEST)) {
			((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegTransferData(true));
		}

		return theResponse;
	}

	/**
	 * Add the consent identifier to the Domain Info Response.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainInfo(EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doDomainInfo(aEvent, aData);

		((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegInfoData("ID:12345"));

		return theResponse;
	}
}
