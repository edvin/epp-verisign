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

import com.verisign.epp.codec.emailFwd.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.persreg.*;

// EPP imports
import com.verisign.epp.framework.*;


/**
 * The <code>PersRegDomainHandler</code> class extends
 * <code>EmailFwdHandler</code> to include responding with Personal
 * Registration Extension attributes.   Specifically, the consent identifier
 * and the bundled rate flag are  returned. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class PersRegEmailFwdHandler extends EmailFwdHandler {
	/**
	 * Add the bunded rate flag attribute to the EmailFwd Renew Response. The
	 * flag is set to <code>true</code>.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdRenew(EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doEmailFwdRenew(aEvent, aData);

		((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegRenewData(true));

		return theResponse;
	}

	/**
	 * Add the bunded rate flag attribute to the EmailFwd Create Response. The
	 * flag is set to <code>true</code>.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdCreate(EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doEmailFwdCreate(aEvent, aData);

		EPPEmailFwdCreateCmd theMessage =
			(EPPEmailFwdCreateCmd) aEvent.getMessage();

		// Simulate an error?
		if (theMessage.getName().indexOf("error") != -1) {
			((EPPResponse) theResponse.getResponse()).setResult(EPPResult.ASSOC_PROHIBITS_OP);
			((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegCreateErrData(EPPPersRegCreateErrData.ERROR_DEFREG_EXISTS));
		}
		else {
			((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegCreateData(false));
		}

		return theResponse;
	}

	/**
	 * Add the bunded rate flag attribute to the EmailFwd Transfer Request. The
	 * flag is set to <code>true</code>.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdTransfer(
												  EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doEmailFwdTransfer(aEvent, aData);

		EPPEmailFwdTransferCmd theMessage =
			(EPPEmailFwdTransferCmd) aEvent.getMessage();

		// Is a transfer request?
		if (theMessage.getOp().equals(EPPCommand.OP_REQUEST)) {
			((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegTransferData(true));
		}

		return theResponse;
	}

	/**
	 * Add the consent identifier to the EmailFwd Info Response.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPEmailFwddHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doEmailFwdInfo(EPPEvent aEvent, Object aData) {
		EPPEventResponse theResponse = super.doEmailFwdInfo(aEvent, aData);

		((EPPResponse) theResponse.getResponse()).addExtension(new EPPPersRegInfoData("ID:12345"));

		return theResponse;
	}
}
