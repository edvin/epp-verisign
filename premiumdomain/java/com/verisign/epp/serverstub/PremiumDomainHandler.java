/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2007 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */

package com.verisign.epp.serverstub;

import java.math.BigDecimal;
import java.util.Vector;

import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheck;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheckResp;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheckResult;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainReAssignCmd;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;

/**
 * Extension to the standard <code>DomainHandler</code> in support of premium domain.
 */
public class PremiumDomainHandler extends DomainHandler {

	/**
	 * Constructs an instance of PremiumDomainHandler
	 */
	public PremiumDomainHandler () {

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
		
		EPPDomainCheckCmd theMessage = (EPPDomainCheckCmd) aEvent.getMessage();
		
		EPPPremiumDomainCheckResult eppPremiumDomainCheckResult = null;
		Vector premiumResults = new Vector();
				
		EPPEventResponse theEventResponse = super.doDomainCheck(aEvent, aData);						
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
				
		EPPPremiumDomainCheck theExt = (EPPPremiumDomainCheck) theMessage.getExtension( EPPPremiumDomainCheck.class );
		
		if (theResponse.isSuccess() && theExt != null && theExt.getFlag().booleanValue()) {
						
			EPPDomainCheckResp thecheckResponse = (EPPDomainCheckResp)theResponse;
			for (int i = 0; i < thecheckResponse.getCheckResults().size(); i++) {
				
				EPPDomainCheckResult currResult =
					(EPPDomainCheckResult) thecheckResponse.getCheckResults().elementAt(i);
				
				if (currResult.getName().equals( "non-premiumdomain.tv" )) {
					eppPremiumDomainCheckResult = new EPPPremiumDomainCheckResult(currResult.getName(), false);
				} else {	
					eppPremiumDomainCheckResult = new EPPPremiumDomainCheckResult(currResult.getName(), true);
					if (currResult.isAvailable()) {						
						eppPremiumDomainCheckResult.setPrice( new BigDecimal( "125.00" ) );
						eppPremiumDomainCheckResult.setRenewalPrice( new BigDecimal( "75.00" ) );
					} 						
				}
				premiumResults.addElement(eppPremiumDomainCheckResult);
			}
			
			if (premiumResults.size() > 0) {
				theResponse.addExtension(new EPPPremiumDomainCheckResp( premiumResults ));
			}
		}
		
		return theEventResponse;
		
	}
	
	/**
	 * Invoked when a Domain Update command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainUpdate ( EPPEvent aEvent, Object aData ) {

		EPPDomainUpdateCmd theMessage = (EPPDomainUpdateCmd) aEvent
				.getMessage();

		EPPTransId transId = new EPPTransId( theMessage.getTransId(),
				"54322-XYZ" );

		EPPResponse theResponse = new EPPResponse( transId );
		theResponse.setResult( EPPResult.SUCCESS );

		if ( !theMessage.hasExtension( EPPPremiumDomainReAssignCmd.class ) ) {
			System.out.println( "Premium Domain ReAssign command is missing" );
			theResponse.setResult( EPPResult.MISSING_PARAMETER );
			return new EPPEventResponse( theResponse );
		}

		EPPPremiumDomainReAssignCmd theExt = (EPPPremiumDomainReAssignCmd) theMessage
				.getExtension( EPPPremiumDomainReAssignCmd.class );

		if ( theExt.getShortName() == null || theExt.getShortName().trim().equals( "" ) ) {
			theResponse.setResult( EPPResult.MISSING_PARAMETER );
			return new EPPEventResponse( theResponse );
		}

		return new EPPEventResponse( theResponse );
	}

}
